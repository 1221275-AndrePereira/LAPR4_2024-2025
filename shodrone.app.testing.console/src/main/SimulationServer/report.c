#include "report.h"
#include <stdio.h>
#include <pthread.h>

const char* obtain_simulation_result(SimulationStatus simulationStatus);

void *report_generation_thread(void *arg){
	FILE *fp = fopen("./outputfiles/simulationReport/Simulation_Report.txt", "w");
    if (fp == NULL) return NULL;

    fprintf(fp, "===================================\n");
    fprintf(fp, "       FIGURE SIMULATION REPORT     \n");
    fprintf(fp, "===================================\n\n");

    int last_reported_collision = 0;

    pthread_mutex_lock(&shared_mem_mutex);

    while (shared_mem->current_time_step < MAX_TIME_STEPS) {
		pthread_cond_wait(&collision_cond, &shared_mem_mutex);

        for (int i = last_reported_collision; i < shared_mem->collision_count; i++) {
            fprintf(fp, "-------COLLISION------- %d:\n", i + 1);
            fprintf(fp, "Time Step: %d\n", shared_mem->collisions[i].time_step);
            fprintf(fp, "Position: (%d, %d, %d)\n", shared_mem->collisions[i].position.x,
													  shared_mem->collisions[i].position.y,
													  shared_mem->collisions[i].position.z);
			fprintf(fp, "Drones Involved: %d and %d\n\n",
                    shared_mem->collisions[i].drone_ids[0]+1,
                    shared_mem->collisions[i].drone_ids[1]+1);
        }
        last_reported_collision = shared_mem->collision_count;
        fflush(fp);
	}

    fprintf(fp, "\n------SIMULATION SUMMARY------\n\n");
    fprintf(fp, "FINAL SIMULATION RESULT: %s\n", obtain_simulation_result(shared_mem->status));

    int active_drones = 0;
    for (int i = 0; i < shared_mem->drone_count; i++) {
        if (shared_mem->active_drones[i]) active_drones++;
    }

    fprintf(fp, "Total drones in simulation: %d\n", shared_mem->drone_count);
    fprintf(fp, "Drones active at end: %d\n", active_drones);
    fprintf(fp, "Drones inactive at end: %d\n", shared_mem->drone_count - active_drones);
    fprintf(fp, "Total collisions: %d\n\n", shared_mem->collision_count);

    fprintf(fp, "===================================\n");
    fprintf(fp, "        END OF REPORT\n");
    fprintf(fp, "===================================\n");

    pthread_mutex_unlock(&shared_mem_mutex);

    fclose(fp);
    printf("Simulation report was generated: Simulation_Report.txt\n");
    return NULL;
}

const char* obtain_simulation_result(SimulationStatus simulationStatus){
	switch(simulationStatus){
		case SIM_VALID:
			return "The figure has passed validation (No Collisions)";
		case SIM_FAIL:
			return "The figure has failed validation (Collision(s) Occurred)";
		default:
			return "Unknown simulation status";
	}
}
