#include "simulation.h"
#include "report.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <math.h>

Drone drones[MAX_DRONES];
int drone_count = 0;
int collision_count = 0;
int current_time_step = 0;
Collision collisions[MAX_COLLISIONS];
SimulationData sim_data;
int grid[GRID_SIZE][GRID_SIZE][GRID_SIZE];

void initialize_simulation(void);
int init_drones(Drone *drones);
void load_simulation_data(const char *filename);
void run_simulation(Drone *drones, int drone_count, Collision *collisions, int *collision_count, SimulationStatus *status);
void update_drone_position(int drone_id, int x, int y, int z, Drone *drones, Collision *collisions, int *collision_count, int time_step);
void reset_grid(void);
int compare_positions(Position a, Position b);

int main() {
    // Initialize arrays for simulation
    initialize_simulation();

    // Initialize drones for the simulation
    drone_count = init_drones(drones);

    // Simulation status
    SimulationStatus simulationStatus = SIM_VALID;

    // Run the simulation
    run_simulation(drones, drone_count, collisions, &collision_count, &simulationStatus);

    // Generate report
    generate_report(drones, drone_count, collisions, collision_count, simulationStatus);

    // Clean up drone processes
    for (int i = 0; i < drone_count; i++) {
        if (drones[i].drone_active_status) {
            kill(drones[i].pid, SIGTERM);
            waitpid(drones[i].pid, NULL, 0);
        }
    }

    return 0;
}

void initialize_simulation(void) {
    memset(drones, 0, sizeof(drones));
    memset(collisions, 0, sizeof(collisions));
    memset(&sim_data, 0, sizeof(sim_data));
    reset_grid();
    current_time_step = 0;
    collision_count = 0;
}

int read_drone_file(const char *filename, Position *positions, int max_positions) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Error opening drone file");
        return -1;
    }

    int i = 0;
    char line[MAX_LINE_LENGTH];
    while (i < max_positions && fgets(line, MAX_LINE_LENGTH, file)) {
        int x, y, z;
        if (sscanf(line, "%d,%d,%d", &x, &y, &z) == 3) {
            positions[i].x = x;
            positions[i].y = y;
            positions[i].z = z;
            i++;
        }
    }

    fclose(file);
    return i;
}

void drone_simulation(int drone_id, int read_fd, int write_fd) {
    char filename[32];
    snprintf(filename, sizeof(filename), "drone%d.txt", drone_id + 1);

    Position positions[MAX_TIME_STEPS];
    int num_positions = read_drone_file(filename, positions, MAX_TIME_STEPS);

    if (num_positions < 0) {
        exit(1);
    }

    for (int i = 0; i < num_positions; i++) {
        // Send position to main process
        write(write_fd, &positions[i], sizeof(Position));
        sleep(1); // Simulate time step
    }

    exit(0);
}

int init_drones(Drone *drones) {
    int count = 0;

    for (int i = 0; i < MAX_DRONES; i++) {
        int main_to_drone[2];
        int drone_to_main[2];

        if (pipe(main_to_drone) < 0 || pipe(drone_to_main) < 0) {
            perror("Failed to create pipes");
            continue;
        }

        pid_t pid = fork();

        if (pid < 0) {
            perror("Failure to fork drone process");
            close(main_to_drone[0]);
            close(main_to_drone[1]);
            close(drone_to_main[0]);
            close(drone_to_main[1]);
            continue;
        } else if (pid == 0) {
            // Drone process
            close(main_to_drone[1]);
            close(drone_to_main[0]);
            drone_simulation(i, main_to_drone[0], drone_to_main[1]);
            exit(0);
        } else {
            // Main process
            close(main_to_drone[0]);
            close(drone_to_main[1]);

            drones[count].id = count;
            drones[count].pid = pid;
            drones[count].position.x = sim_data.positions[i][0].x;
            drones[count].position.y = sim_data.positions[i][0].y;
            drones[count].position.z = sim_data.positions[i][0].z;
            drones[count].write_fd = main_to_drone[1];
            drones[count].read_fd = drone_to_main[0];
            drones[count].drone_active_status = true;

            count++;
        }
    }
    return count;
}

void reset_grid(void) {
    memset(grid, -1, sizeof(grid));
}

int compare_positions(Position a, Position b) {
    return (abs(a.x - b.x) <= 1 && abs(a.y - b.y) <= 1 && abs(a.z - b.z) <= 1);
}

void update_drone_position(int drone_id, int x, int y, int z, Drone *drones, Collision *collisions, int *collision_count, int time_step) {
    if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE || z < 0 || z >= GRID_SIZE) {
        return;
    }

    if (grid[x][y][z] != -1) {
        // Collision detected
        printf("Collision detected between Drone %d and Drone %d at (%d, %d, %d) at time step %d\n",
               drone_id, grid[x][y][z], x, y, z, time_step);

        collisions[*collision_count].time_step = time_step;
        collisions[*collision_count].position.x = x;
        collisions[*collision_count].position.y = y;
        collisions[*collision_count].position.z = z;
        collisions[*collision_count].drone_ids[0] = drone_id;
        collisions[*collision_count].drone_ids[1] = grid[x][y][z];
        collisions[*collision_count].drone_count = 2;
        (*collision_count)++;

        // Notify drones
        kill(drones[drone_id].pid, SIGUSR1);
        kill(drones[grid[x][y][z]].pid, SIGUSR1);

        drones[drone_id].drone_active_status = false;
        drones[grid[x][y][z]].drone_active_status = false;
    } else {
        grid[x][y][z] = drone_id;
        drones[drone_id].position.x = x;
        drones[drone_id].position.y = y;
        drones[drone_id].position.z = z;
    }
}

void run_simulation(Drone *drones, int drone_count, Collision *collisions, int *collision_count, SimulationStatus *status) {
    for (current_time_step = 0; current_time_step < sim_data.time_step_count; current_time_step++) {
        printf("\n[Main] Time Step: %d\n", current_time_step);

        reset_grid();

        for (int i = 0; i < drone_count; i++) {
            if (!drones[i].drone_active_status) {
                continue;
            }

            Position pos;
            ssize_t n = read(drones[i].read_fd, &pos, sizeof(Position));

            if (n <= 0) {
                fprintf(stderr, "[Main] Error reading from Drone %d at time step %d\n", i, current_time_step);
                drones[i].drone_active_status = false;
                continue;
            }

            // Update position and check for collisions
            update_drone_position(i, pos.x, pos.y, pos.z, drones, collisions, collision_count, current_time_step);

            // Additional pairwise collision check
            for (int j = 0; j < i; j++) {
                if (drones[j].drone_active_status && compare_positions(drones[i].position, drones[j].position)) {
                    printf("Pairwise collision detected between Drone %d and Drone %d at (%d, %d, %d) at time step %d\n",
                           i, j, pos.x, pos.y, pos.z, current_time_step);

                    collisions[*collision_count].time_step = current_time_step;
                    collisions[*collision_count].position.x = pos.x;
                    collisions[*collision_count].position.y = pos.y;
                    collisions[*collision_count].position.z = pos.z;
                    collisions[*collision_count].drone_ids[0] = i;
                    collisions[*collision_count].drone_ids[1] = j;
                    collisions[*collision_count].drone_count = 2;
                    (*collision_count)++;

                    drones[i].drone_active_status = false;
                    drones[j].drone_active_status = false;

                    kill(drones[i].pid, SIGUSR1);
                    kill(drones[j].pid, SIGUSR1);
                }
            }

            printf("[Main] Time Step: %d | Drone: %d | Position(X = %d, Y = %d, Z = %d)\n",
                   current_time_step, i, pos.x, pos.y, pos.z);
        }

        if (*collision_count > 0) {
            *status = SIM_FAIL;
        }
    }

    if (*collision_count == 0) {
        *status = SIM_VALID;
    }
}