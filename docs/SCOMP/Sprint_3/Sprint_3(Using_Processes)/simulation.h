#ifndef SIMULATION_H
#define SIMULATION_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <time.h>
#include <stdbool.h>

#define MAX_DRONES 5
#define MAX_TIME_STEPS 10
#define MAX_COLLISIONS 50
#define MAX_LINE_LENGTH 1024
#define GRID_SIZE 10


typedef struct {
    int x;
    int y;
    int z;
} Position;

typedef struct {
    int drone_id;
    int time_step;
    Position position;
} PositionUpdate; //This struct will be responsible for saving the previous positions of each drones in the figure in each time-step

typedef struct {
    int time_step;
    Position position;
    int drone_ids[MAX_DRONES];
    int drone_count;
} Collision;

typedef struct {
    int id; //The id of a drone
    pid_t pid; //The process id of a drone (each drone will be a process)
    Position position; //The position of a drone.
    int write_fd;   // This will be used for the main process to write message to each drone process.
    int read_fd;    // This will be used for the main process to read message from each drone process.
    bool drone_active_status; //The status of a drone.
} Drone;


typedef enum {
    SIM_VALID = 0, //If the figure simulation was succesful and no collisions ocurred
    SIM_FAIL = 1, //If a collision ocurred
    SIM_ERROR = 2, //If an error during simulation ocurred
} SimulationStatus;


#endif



