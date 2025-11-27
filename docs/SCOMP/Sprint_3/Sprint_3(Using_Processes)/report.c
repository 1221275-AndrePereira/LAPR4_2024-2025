#include "report.h"


void generate_report(Drone *drones, int drone_count, Collision *collisions, int collision_count, SimulationStatus simulationStatus){

	const char *filename = "Simulation_Report.txt"; //Simulation report will be generated into file called "Simulation_Report.txt"

	store_report_in_file(filename, drones, drone_count, collisions, collision_count, simulationStatus);

	printf("Simulation report was generated");
}

void store_report_in_file(const char *filename, Drone *drones, int drone_count, Collision *collisions, int collision_count, SimulationStatus simulationStatus){


	FILE *fp = fopen(filename, "w"); //File will be open to write the simulation report in it.

    if (fp == NULL) {
        perror("Error while trying to open file");
        return;
    }

    //Title of the simulation report
    fprintf(fp, "===================================\n");
    fprintf(fp, "       FIGURE SIMULATION REPORT     \n");
    fprintf(fp, "===================================\n\n");

    //Writes the final result of the figure simulation that was done
    fprintf(fp, "FINAL SIMULATION RESULT: %s\n\n", obtain_simulation_result(simulationStatus));


    fprintf(fp, "------SIMULATION CONTENT------\n\n");

    //Writes the total number of drones in this simulation
    fprintf(fp, "Total number of drones: %d\n\n",drone_count);

    int active_drones = 0; //Will count the total number of currently active drones
    for (int i = 0; i < drone_count; i++) {
        if (drones[i].drone_active_status) {
            active_drones++;
        }
    }
    int inactive_drones = drone_count - active_drones; //Will count the total number of currently inactive drones

    //Writes the total number of active drones in this simulation
    fprintf(fp, "Total number of active drones: %d\n",active_drones);
    //Writes the total number of inactive drones in this simulation
    fprintf(fp, "Total number of inactive drones: %d\n",inactive_drones);

    //Writes the total number of collisions that occurred during simulation
    fprintf(fp, "Total collisions: %d\n\n", collision_count);

    if(collision_count>0){ //If any collision occurred
		for (int i = 0; i < collision_count; i++) {
            fprintf(fp, "-------COLLISION------- %d:\n", i + 1);
            fprintf(fp, "Time Step: %d\n", collisions[i].time_step); //The timestep in which the collision occurred
            fprintf(fp, "Position: (%d, %d, %d)\n\n", collisions[i].position.x, collisions[i].position.y, collisions[i].position.z); //The position in a 3D space in which the collision occurred
        }
	}


    //End of simulation report file
    fprintf(fp, "===================================\n");
    fprintf(fp, "===================================\n");

    fclose(fp); //Closing the file for writing
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
