#include "simulation.h"
#include "report.h"
#include "thread_creator.h"
#include "simulation_sync.h"
#include "server.h"

// Global Variable Definitions
SharedMemory *shared_mem;
int drone_count = 0;
Drone drones[MAX_DRONES];
sem_t *drone_semaphore;
sem_t *parent_semaphore;
sem_t *startup_semaphore;
pthread_mutex_t shared_mem_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t collision_cond = PTHREAD_COND_INITIALIZER;
pthread_cond_t sync_cond = PTHREAD_COND_INITIALIZER;
pthread_cond_t state_change_cond = PTHREAD_COND_INITIALIZER;
pthread_cond_t client_cond = PTHREAD_COND_INITIALIZER;

void initialize_shared_memory_area(void) {
    shared_mem = mmap(NULL, sizeof(SharedMemory), PROT_READ | PROT_WRITE, MAP_SHARED | MAP_ANONYMOUS, -1, 0);
    if (shared_mem == MAP_FAILED) {
        perror("mmap failed");
        exit(EXIT_FAILURE);
    }
}

void initialize_semaphores(void) {
    sem_unlink("/drone_sem");
    sem_unlink("/parent_sem");
    sem_unlink("/startup_sem");
    drone_semaphore = sem_open("/drone_sem", O_CREAT, 0644, 0);
    parent_semaphore = sem_open("/parent_sem", O_CREAT, 0644, 0);
    startup_semaphore = sem_open("/startup_sem", O_CREAT, 0644, 0);
    if (drone_semaphore == SEM_FAILED || parent_semaphore == SEM_FAILED || startup_semaphore == SEM_FAILED) {
        perror("sem_open failed");
        exit(EXIT_FAILURE);
    }
}

/**
 * @brief Resets the simulation state.
 * @note This function assumes the caller holds the shared_mem_mutex.
 */
void initialize_simulation(void) {
    // DEADLOCK FIX: Removed mutex lock/unlock from this function. The caller must hold the lock.
    memset(shared_mem, 0, sizeof(SharedMemory));
    shared_mem->status = SIM_VALID;
    shared_mem->sim_state = STATE_IDLE;
    for (int i = 0; i < MAX_DRONES; i++) {
        shared_mem->active_drones[i] = 1; // Mark all drone slots as potentially active
    }
}

int init_drones(Drone *drones_array) {
    int count = 0;
    for (int i = 0; i < drone_count; i++) {
        pid_t pid = fork();
        if (pid == -1) {
            perror("fork failed");
            continue;
        }
        if (pid == 0) { // Child process
            drone_simulation(i);
            exit(0);
        } else { // Parent process
            drones_array[i] = (Drone){.id = i, .pid = pid, .drone_active_status = true};
            count++;
        }
    }
    return count;
}

void cleanup(void) {
    for (int i = 0; i < drone_count; i++) {
        if (drones[i].pid > 0) {
            kill(drones[i].pid, SIGTERM);
            waitpid(drones[i].pid, NULL, 0);
            drones[i].pid = 0;
        }
    }
    drone_count = 0;
}

int main() {
    initialize_shared_memory_area();
    initialize_semaphores();
    // Initial setup needs to be protected
    pthread_mutex_lock(&shared_mem_mutex);
    initialize_simulation();
    pthread_mutex_unlock(&shared_mem_mutex);


    pthread_t server_t;
    if (pthread_create(&server_t, NULL, server_thread, NULL) != 0) {
        perror("pthread_create for server failed");
        exit(EXIT_FAILURE);
    }

    while (1) {
        pthread_mutex_lock(&shared_mem_mutex);
        while (shared_mem->sim_state != STATE_SIMULATING) {
            pthread_cond_wait(&state_change_cond, &shared_mem_mutex);
        }
        drone_count = shared_mem->drone_count;
        pthread_mutex_unlock(&shared_mem_mutex);

        printf("State change to SIMULATING. Starting simulation with %d drones.\n", drone_count);
        init_drones(drones);

        for (int i = 0; i < drone_count; i++) {
            sem_wait(startup_semaphore);
        }

        int active_drone_count = 0;
        pthread_mutex_lock(&shared_mem_mutex);
        for(int i = 0; i < shared_mem->drone_count; i++) {
            if(shared_mem->active_drones[i]) {
                active_drone_count++;
            }
        }
        pthread_mutex_unlock(&shared_mem_mutex);

        if (active_drone_count == 0 && drone_count > 0) {
            printf("All drones failed to start. Aborting simulation.\n");
            cleanup();
            pthread_mutex_lock(&shared_mem_mutex);
            shared_mem->status = SIM_ERROR;
            shared_mem->sim_state = STATE_DONE;
            pthread_cond_signal(&client_cond);
            pthread_mutex_unlock(&shared_mem_mutex);
            continue;
        }

        start_threads();
        cleanup();

        printf("\nSimulation complete. Result is ready for client.\n");
        pthread_mutex_lock(&shared_mem_mutex);
        shared_mem->sim_state = STATE_DONE;
        pthread_cond_signal(&client_cond);
        pthread_mutex_unlock(&shared_mem_mutex);
    }

    pthread_join(server_t, NULL);
    sem_close(drone_semaphore);
    sem_close(parent_semaphore);
    sem_close(startup_semaphore);
    sem_unlink("/drone_sem");
    sem_unlink("/parent_sem");
    sem_unlink("/startup_sem");
    munmap(shared_mem, sizeof(SharedMemory));
    return 0;
}
