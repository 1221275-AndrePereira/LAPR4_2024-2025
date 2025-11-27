#include "report.h"


void *report_generation_thread(void *arg){


	FILE *fp = fopen("Simulation_Report.txt", "w"); //File will be open to write the simulation report in it.

    if (fp == NULL) {
        perror("Error while trying to open file");
        return NULL;
    }

    //Title of the simulation report
    fprintf(fp, "===================================\n");
    fprintf(fp, "       FIGURE SIMULATION REPORT     \n");
    fprintf(fp, "===================================\n\n");
    
    int last_reported_collision = 0;
    
    pthread_mutex_lock(&shared_mem_mutex); //Lock the shared memory mutex object



    while (shared_mem->current_time_step < MAX_TIME_STEPS && shared_mem->collision_count < MAX_COLLISIONS){ //While the number of time-steps are lower than the maximum and the number of collisions does not exceed the threshold
		pthread_cond_wait(&collision_cond, &shared_mem_mutex); //Waits for the signal sent by the collision_detection thread
		for (int i = last_reported_collision; i < shared_mem->collision_count; i++) { //Processes each collision at a time
            fprintf(fp, "-------COLLISION------- %d:\n", i + 1);
            fprintf(fp, "Time Step: %d\n", shared_mem->collisions[i].time_step); //The timestep in which the collision occurred
            fprintf(fp, "Position: (%d, %d, %d)\n\n", shared_mem->collisions[i].position.x, 
													  shared_mem->collisions[i].position.y,
													  shared_mem->collisions[i].position.z); //The position in a 3D space in which the collision occurred
			fprintf(fp, "Drones Involved in the Collision: %d and %d\n\n", 
                    shared_mem->collisions[i].drone_ids[0]+1,
                    shared_mem->collisions[i].drone_ids[1]+1);
        }
        last_reported_collision = shared_mem->collision_count;
        
        fflush(fp);
	}
	
	
	//Writes the final result of the figure simulation that was done
    fprintf(fp, "FINAL SIMULATION RESULT: %s\n\n", obtain_simulation_result(shared_mem->status));


    fprintf(fp, "------SIMULATION CONTENT------\n\n");

    //Writes the total number of drones in this simulation
    fprintf(fp, "Total number of drones: %d\n\n",drone_count);

    int active_drones = 0; //Will count the total number of currently active drones
    for (int i = 0; i < shared_mem->drone_count; i++) {
        if (shared_mem->active_drones[i]) {
            active_drones++;
        }
    }
    int inactive_drones = shared_mem->drone_count - active_drones; //Will count the total number of currently inactive drones

    //Writes the total number of active drones in this simulation
    fprintf(fp, "Total number of active drones: %d\n",active_drones);
    //Writes the total number of inactive drones in this simulation
    fprintf(fp, "Total number of inactive drones: %d\n",inactive_drones);

    //Writes the total number of collisions that occurred during simulation
    fprintf(fp, "Total collisions: %d\n\n", shared_mem->collision_count);


    //End of simulation report file
    fprintf(fp, "===================================\n");
    fprintf(fp, "===================================\n");

    fclose(fp); //Closing the file for writing
    pthread_mutex_unlock(&shared_mem_mutex); //Unlock the shared memory mutex object
    
    printf("Simulation report was generated\n");
    return NULL;
}


const char* obtain_simulation_result(SimulationStatus simulationStatus){
	switch(simulationStatus){
		case SIM_VALID:
			return "The figure has passed validation"; //The figure simulation was succesful (no collisions occured).
		case SIM_FAIL:
			return "The figure has failed validation (At least one collision occured)"; //The figure simulation was not succesful (Collisions occured).
		case SIM_ERROR:
			return "Simulation error"; //The figure simulation had an unexpected error.
		default:
			return "Unknown simulation status";
	}
}
