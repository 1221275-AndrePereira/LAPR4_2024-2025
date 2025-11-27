#include "simulation.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <pthread.h>

/**
 * @brief Checks for a collision between a drone and all others.
 * Starts from drone_id + 1 to avoid duplicate checks.
 * @param drone_id The ID of the primary drone to check.
 */
void check_drone_for_collisions(int drone_id) {
    if (!shared_mem->active_drones[drone_id]) return;

    Position pos = shared_mem->positions[drone_id];

    for (int other_drone_id = drone_id + 1; other_drone_id < shared_mem->drone_count; other_drone_id++) {
        if (!shared_mem->active_drones[other_drone_id]) continue;

        if (abs(pos.x - shared_mem->positions[other_drone_id].x) <= 1 &&
            abs(pos.y - shared_mem->positions[other_drone_id].y) <= 1 &&
            abs(pos.z - shared_mem->positions[other_drone_id].z) <= 1) {

            printf("Collision detected between Drone %d and Drone %d at time step %d\n",
                   drone_id + 1, other_drone_id + 1, shared_mem->current_time_step);

            if (shared_mem->collision_count < MAX_COLLISIONS) {
                shared_mem->collisions[shared_mem->collision_count].time_step = shared_mem->current_time_step;
                shared_mem->collisions[shared_mem->collision_count].position = pos;
                shared_mem->collisions[shared_mem->collision_count].drone_ids[0] = drone_id;
                shared_mem->collisions[shared_mem->collision_count].drone_ids[1] = other_drone_id;
                shared_mem->collision_count++;
            }

            shared_mem->status = SIM_FAIL;
            shared_mem->drones_to_deactivate[drone_id] = true;
            shared_mem->drones_to_deactivate[other_drone_id] = true;
            pthread_cond_signal(&collision_cond);
        }
    }
}

/**
 * @brief The main function for the collision detection thread.
 */
void *collision_detection_thread(void *arg) {
    pthread_mutex_lock(&shared_mem_mutex);

    while (shared_mem->current_time_step < MAX_TIME_STEPS) {
        pthread_cond_wait(&sync_cond, &shared_mem_mutex);

        if (shared_mem->current_time_step >= MAX_TIME_STEPS) {
            break;
        }

        memset(shared_mem->drones_to_deactivate, 0, sizeof(shared_mem->drones_to_deactivate));

        for (int i = 0; i < shared_mem->drone_count; i++) {
            check_drone_for_collisions(i);
        }

        for (int i = 0; i < shared_mem->drone_count; i++) {
            if (shared_mem->drones_to_deactivate[i] && shared_mem->active_drones[i]) {
                shared_mem->active_drones[i] = 0;
                printf("Deactivating Drone %d\n", i + 1);
                kill(drones[i].pid, SIGUSR1);
            }
        }
    }

    pthread_mutex_unlock(&shared_mem_mutex);

    printf("Collision detection thread finished.\n");
    return NULL;
}
