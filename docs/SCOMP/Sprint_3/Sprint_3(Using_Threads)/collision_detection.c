#include "simulation.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <pthread.h>

// Checks a drone's new position for collisions with other drones.
void update_drone_position(int drone_id, Position pos, int time_step) {
    // Check if the drone is out of boundaries.
    if (pos.x < 0 || pos.x >= GRID_SIZE || pos.y < 0 || pos.y >= GRID_SIZE || pos.z < 0 || pos.z >= GRID_SIZE) {
        // If out of bounds, deactivate the drone and stop processing it.
        shared_mem->active_drones[drone_id] = 0;
        return;
    }

    for (int i = 0; i < shared_mem->drone_count; i++) {
        // A collision occurs if another drone 'i' is:
        // 1. Not the same drone we are currently checking (i != drone_id).
        // 2. Still active in the simulation.
        // 3. Within 1 unit of distance on all axes (x, y, z), forming a "collision box".
        if (i != drone_id && shared_mem->active_drones[i] &&
            abs(pos.x - shared_mem->positions[i].x) <= 1 &&
            abs(pos.y - shared_mem->positions[i].y) <= 1 &&
            abs(pos.z - shared_mem->positions[i].z) <= 1) {

            // !!! COLLISION DETECTED !!!
            printf("Collision detected between Drone %d and Drone %d at (%d, %d, %d) at time step %d\n",
                   drone_id+1, i+1, pos.x, pos.y, pos.z, time_step);

            // Record the details of the collision in the shared memory.
            shared_mem->collisions[shared_mem->collision_count].time_step = time_step;
            shared_mem->collisions[shared_mem->collision_count].position = pos;
            shared_mem->collisions[shared_mem->collision_count].drone_ids[0] = drone_id;
            shared_mem->collisions[shared_mem->collision_count].drone_ids[1] = i;
            shared_mem->collisions[shared_mem->collision_count].drone_count = 2;
            shared_mem->collision_count++;
            // Update the status to indicate that the simulation has failed.
            shared_mem->status = SIM_FAIL;

            // Deactivate both drones involved in the collision.
            shared_mem->active_drones[drone_id] = 0;
            shared_mem->active_drones[i] = 0;

            // Send a signal to the actual drone processes to notify them.
            kill(drones[drone_id].pid, SIGUSR1);
            kill(drones[i].pid, SIGUSR1);

            // Signal the report generation thread to wake it up.
            pthread_cond_signal(&collision_cond);
        }
    }
}

// The main function for the collision detection thread.
void *collision_detection_thread(void *arg) {
    int time_step = 0;

    while (time_step < MAX_TIME_STEPS) {

        // Lock mutex to ensure safe access.
        // This ensures that we only check for collisions after all drones have moved.
		pthread_mutex_lock(&shared_mem_mutex);
        printf("Collision thread: Waiting for the signal from sync thread at time step %d\n", time_step + 1);
        // Wait for the sync thread to signal that all drones have moved.
        pthread_cond_wait(&sync_cond, &shared_mem_mutex);
        printf("Collision thread: Received signal from sync thread, checking collisions\n");

        // At each time step, loop through every drone.
        for (int i = 0; i < shared_mem->drone_count; i++) {

            // Only check drones that are currently active.
            if (shared_mem->active_drones[i]) {
                // Get the current position of the drone.
                Position pos = shared_mem->positions[i];

                // Check for collisions with this drone's current position.
                update_drone_position(i, pos, time_step + 1);

            }
        }

        // Check if the maximum number of collisions has been reached.
        int collision_threshold = (shared_mem->collision_count >= MAX_COLLISIONS);
        // Unlock the mutex after processing all drones.
        pthread_mutex_unlock(&shared_mem_mutex);

        // If the collision threshold is reached, terminate the collision detection.
        if (collision_threshold) {
            printf("Maximum collision limit reached. Terminating collision detection.\n");
            break;
        }

        time_step++;
    }

    // After the simulation loop finishes, we need to signal the report generation thread.
    // Lock mutex to ensure safe access before signaling the report thread.
    pthread_mutex_lock(&shared_mem_mutex);
    // This ensures the report thread wakes up to write the final summary.
    pthread_cond_signal(&collision_cond);
    // Unlock the mutex after signaling.
    pthread_mutex_unlock(&shared_mem_mutex);
    return NULL;
}