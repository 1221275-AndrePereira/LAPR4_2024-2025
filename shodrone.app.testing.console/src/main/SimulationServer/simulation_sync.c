#include "simulation.h"
#include "simulation_sync.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>

int read_drone_file(const char *filename, Position *positions, int max_positions) {
    char full_path[1024]; // Increased buffer size

    // If filename starts with '/', it's an absolute path, so use it directly.
    // Otherwise, construct the relative path from the server's location.
    if (filename[0] == '/') {
        snprintf(full_path, sizeof(full_path), "%s", filename);
    } else {
        snprintf(full_path, sizeof(full_path), "./inputfiles/drone_coordinates/%s", filename);
    }

    FILE *file = fopen(full_path, "r");
    if (!file) {
        // Print the full path for easier debugging
        fprintf(stderr, "Error opening drone file: %s\n", full_path);
        perror("fopen");
        return -1;
    }

    int i = 0;
    char line[MAX_LINE_LENGTH];
    while (i < max_positions && fgets(line, MAX_LINE_LENGTH, file)) {
        if (sscanf(line, "%d %d %d", &positions[i].x, &positions[i].y, &positions[i].z) == 3) {
            i++;
        }
    }
    fclose(file);
    return i;
}

void drone_simulation(int drone_id) {
    char filename[MAX_FILENAME_LEN];
    pthread_mutex_lock(&shared_mem_mutex);
    strncpy(filename, shared_mem->drone_files[drone_id], MAX_FILENAME_LEN);
    pthread_mutex_unlock(&shared_mem_mutex);

    Position positions[MAX_TIME_STEPS];
    int num_positions = read_drone_file(filename, positions, MAX_TIME_STEPS);

    if (num_positions <= 0) {
        pthread_mutex_lock(&shared_mem_mutex);
        shared_mem->active_drones[drone_id] = 0;
        pthread_mutex_unlock(&shared_mem_mutex);

        // Signal the parent process that startup is complete (even if failed)
        sem_post(startup_semaphore);
        exit(1);
    }

    // Signal the parent process that startup is complete
    sem_post(startup_semaphore);

    int current_position_index = 0;
    while (1) {
        sem_wait(drone_semaphore);
        pthread_mutex_lock(&shared_mem_mutex);
        if (!shared_mem->active_drones[drone_id] || shared_mem->current_time_step >= MAX_TIME_STEPS) {
            pthread_mutex_unlock(&shared_mem_mutex);
            break;
        }
        if (current_position_index < num_positions) {
            shared_mem->positions[drone_id] = positions[current_position_index++];
        }
        pthread_mutex_unlock(&shared_mem_mutex);
        sem_post(parent_semaphore);
    }
    exit(0);
}

void *simulation_sync_thread(void *arg) {
    for (int t = 0; t < MAX_TIME_STEPS; t++) {
        pthread_mutex_lock(&shared_mem_mutex);
        shared_mem->current_time_step = t;

        int active_count = 0;
        for (int i = 0; i < shared_mem->drone_count; i++) {
            if (shared_mem->active_drones[i]) active_count++;
        }

        if (active_count == 0) {
            printf("No active drones remaining, ending simulation.\n");
            shared_mem->current_time_step = MAX_TIME_STEPS; // Force termination
            pthread_mutex_unlock(&shared_mem_mutex);
            break;
        }

        printf("\n[Sync Thread] Time Step: %d\n", t + 1);
        pthread_mutex_unlock(&shared_mem_mutex);

        for (int i = 0; i < active_count; i++) sem_post(drone_semaphore);
        for (int i = 0; i < active_count; i++) sem_wait(parent_semaphore);

        pthread_mutex_lock(&shared_mem_mutex);
        pthread_cond_signal(&sync_cond);
        pthread_mutex_unlock(&shared_mem_mutex);

        usleep(50000); // 50ms delay between steps
    }

    // Set final state and broadcast to terminate other threads cleanly
    pthread_mutex_lock(&shared_mem_mutex);
    shared_mem->current_time_step = MAX_TIME_STEPS;
    pthread_cond_broadcast(&sync_cond);
    pthread_cond_broadcast(&collision_cond);
    pthread_mutex_unlock(&shared_mem_mutex);

    printf("Sync thread: Simulation loop complete.\n");
    return NULL;
}
