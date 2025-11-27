#include "thread_creator.h"

void *collision_detection_thread(void*);
void *simulation_sync_thread(void*);
void *report_generation_thread(void*);


//Starts all three threads
void start_threads(void) {
	pthread_t collision_thread, sync_thread, report_thread;
	
	//Create all three threads
	if(pthread_create(&collision_thread,NULL,collision_detection_thread,NULL)||
	   pthread_create(&sync_thread,NULL,simulation_sync_thread,NULL)||
	   pthread_create(&report_thread,NULL,report_generation_thread,NULL)){
	   perror("Failure in creating the threads"); //In case an error ocurred
	   return;
	}
	
	pthread_join(collision_thread,NULL);
	pthread_join(sync_thread,NULL);
	pthread_join(report_thread,NULL); 
	
	//By using these three pthread_join's we are blocking the simulation 
	//from advancing past this code into cleanup in the main function of the simulation.c 
	//until all threads have finished their code
	
	
}

