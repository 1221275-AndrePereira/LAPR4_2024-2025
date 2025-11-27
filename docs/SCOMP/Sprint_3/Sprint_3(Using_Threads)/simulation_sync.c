#include "simulation_sync.h" // Include the header file for synchronization functions
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>

// Function to read drone positions from a file
int read_drone_file(const char *filename, Position *positions, int max_positions) {
    FILE *file = fopen(filename, "r"); // Open the specified file for reading
    if (!file) {
        perror("Error opening drone file"); // Print error if file cannot be opened
        return -1; // Return -1 to indicate failure
    }

    int i = 0; // Initialize index for positions
    char line[MAX_LINE_LENGTH]; // Buffer for reading lines from the file
    // Read positions from the file until the maximum number of positions is reached
    while (i < max_positions && fgets(line, MAX_LINE_LENGTH, file)) {
        int x, y, z; // Variables to hold the position coordinates
        // Parse the line to extract x, y, z coordinates
        if (sscanf(line, "%d %d %d", &x, &y, &z) == 3) {
            positions[i].x = x; // Store x coordinate
            positions[i].y = y; // Store y coordinate
            positions[i].z = z; // Store z coordinate
            i++; // Increment the index
        }
    }

    fclose(file); // Close the file after reading
    return i; // Return the number of positions read
}

// Function for each drone's simulation
void drone_simulation(int drone_id) {
    char filename[32]; // Buffer to hold the filename
    snprintf(filename, sizeof(filename), "drone%d.txt", drone_id + 1); // Create filename based on drone ID

    Position positions[MAX_TIME_STEPS]; // Array to hold positions for the drone
    int num_positions = read_drone_file(filename, positions, MAX_TIME_STEPS); // Read positions from the file
    if (num_positions < 0) { // Check if reading was successful
        fprintf(stderr, "Drone %d: Failed to read %s\n", drone_id + 1, filename); // Print error message
        pthread_mutex_lock(&shared_mem_mutex); // Lock the shared memory mutex
        shared_mem->active_drones[drone_id] = 0; // Mark the drone as inactive
        pthread_mutex_unlock(&shared_mem_mutex); // Unlock the mutex
        exit(1); // Exit the drone process
    }

    printf("Drone %d: Read %d positions\n", drone_id + 1, num_positions); // Print the number of positions read

    int current_position = 0; // Initialize the current position index

    while (1) { // Infinite loop for the drone's simulation
        sem_wait(drone_semaphore); // Wait for the signal to proceed

        pthread_mutex_lock(&shared_mem_mutex); // Lock the shared memory mutex

        // Check if the drone is still active
        if (!shared_mem->active_drones[drone_id]) {
            pthread_mutex_unlock(&shared_mem_mutex); // Unlock the mutex
            printf("Drone %d: Deactivated, exiting\n", drone_id + 1); // Print deactivation message
            break; // Exit the loop
        }

        // Check if the maximum time steps have been reached
        if (shared_mem->current_time_step >= MAX_TIME_STEPS) {
            pthread_mutex_unlock(&shared_mem_mutex); // Unlock the mutex
            printf("Drone %d: Max time steps reached, exiting\n", drone_id + 1); // Print message
            break; // Exit the loop
        }

        // Update the position if there are more positions to process
        if (current_position < num_positions) {
            shared_mem->positions[drone_id] = positions[current_position]; // Update shared memory with the current position
            printf("Drone %d: Position %d: (%d, %d, %d) at time step %d\n",
                   drone_id + 1, current_position+1,
                   positions[current_position].x,
                   positions[current_position].y,
                   positions[current_position].z,
                   shared_mem->current_time_step + 1); // Print the current position and time step
            current_position++; // Move to the next position
        } else {
            // If no more positions are available
            printf("Drone %d: No more positions, staying at last position\n", drone_id + 1); // Print message
        }

        pthread_mutex_unlock(&shared_mem_mutex); // Unlock the mutex

        sem_post(parent_semaphore); // Signal the parent process that the drone has submitted its position
    }

    printf("Drone %d: Finished simulation\n", drone_id + 1); // Print completion message
    exit(0); // Exit the drone process
}

// Function for the simulation synchronization thread
void *simulation_sync_thread(void *arg) {
    // Loop through each time step of the simulation
    for (shared_mem->current_time_step = 0; shared_mem->current_time_step < MAX_TIME_STEPS; shared_mem->current_time_step++) {
        printf("\n[Main] Time Step: %d\n", shared_mem->current_time_step + 1); // Print the current time step

        pthread_mutex_lock(&shared_mem_mutex); // Lock the shared memory mutex
        int active_count = 0; // Initialize active drone count
        // Count the number of active drones
        for (int i = 0; i < shared_mem->drone_count; i++) {
            if (shared_mem->active_drones[i]) {
                active_count++; // Increment count for active drones
            }
        }

        // Check if the collision threshold has been reached
        int collision_threshold = (shared_mem->collision_count >= MAX_COLLISIONS);
        pthread_mutex_unlock(&shared_mem_mutex); // Unlock the mutex

        // If no active drones are remaining, end the simulation
        if (active_count == 0) {
            printf("No active drones remaining, ending simulation\n");
            break; // Exit the loop
        }

        // If the collision threshold is reached, stop the simulation
        if (collision_threshold) {
            printf("Simulation stopped due to max number of collisions\n");
            break; // Exit the loop
        }

        // Signal all active drones to submit their positions
        for (int i = 0; i < active_count; i++) {
            sem_post(drone_semaphore); // Signal each active drone
        }

        // Wait for all active drones to submit their positions
        for (int i = 0; i < active_count; i++) {
            sem_wait(parent_semaphore); // Wait for each drone's submission
        }

        pthread_mutex_lock(&shared_mem_mutex); // Lock the mutex
        pthread_cond_signal(&sync_cond); // Signal the synchronization condition variable
        pthread_mutex_unlock(&shared_mem_mutex); // Unlock the mutex

        usleep(50000); // Sleep for a short duration to control the simulation speed
    }

    printf("Sync thread: Simulation complete, signaling threads to finish\n"); // Print completion message

    pthread_mutex_lock(&shared_mem_mutex); // Lock the mutex
    pthread_cond_broadcast(&sync_cond); // Signal all waiting threads to finish
    pthread_cond_signal(&collision_cond); // Signal the collision condition variable
    pthread_mutex_unlock(&shared_mem_mutex); // Unlock the mutex

    return NULL; // Exit the thread
}
