#include "thread_creator.h"
#include <stdio.h>

// Forward declarations for the thread functions
void *collision_detection_thread(void*);
void *simulation_sync_thread(void*);
void *report_generation_thread(void*);

/**
 * @brief Creates and starts the three core simulation threads.
 *
 * This function is called by the main state machine when the state
 * changes to STATE_SIMULATING. It creates the sync, collision, and
 * report threads and then waits for them to complete.
 */
void start_threads(void) {
	pthread_t collision_thread, sync_thread, report_thread;

	// Create the three simulation-specific threads
	if(pthread_create(&collision_thread, NULL, collision_detection_thread, NULL) != 0 ||
	   pthread_create(&sync_thread, NULL, simulation_sync_thread, NULL) != 0 ||
	   pthread_create(&report_thread, NULL, report_generation_thread, NULL) != 0){
	   perror("Failure in creating the simulation threads");
	   return;
	}

	// Wait for all three simulation threads to finish their execution.
	// This blocks the main simulation loop in simulation.c until the
	// current simulation run is complete.
	pthread_join(sync_thread, NULL);
	pthread_join(collision_thread, NULL);
	pthread_join(report_thread, NULL);
}
