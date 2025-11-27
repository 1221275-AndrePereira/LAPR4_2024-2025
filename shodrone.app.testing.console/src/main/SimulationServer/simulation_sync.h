#ifndef SIMULATION_SYNC_H
#define SIMULATION_SYNC_H

#include "simulation.h"

void drone_simulation(int drone_id);
int read_drone_file(const char *filename, Position *positions, int max_positions);

#endif