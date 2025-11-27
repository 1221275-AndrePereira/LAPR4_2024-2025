#ifndef REPORT_H
#define REPORT_H

#include "simulation.h"

void generate_report(Drone *drones, int drone_count, Collision *collisions, int collision_count, SimulationStatus simulationStatus);
                     
void store_report_in_file(const char *filename, Drone *drones, int drone_count, Collision *collisions, int collision_count, SimulationStatus simulationStatus);

const char* obtain_simulation_result(SimulationStatus simulationStatus);
	

#endif
