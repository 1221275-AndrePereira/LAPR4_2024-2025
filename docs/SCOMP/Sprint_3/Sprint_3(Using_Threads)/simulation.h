#ifndef SIMULATION_H
#define SIMULATION_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <stdbool.h>
#include <semaphore.h>
#include <pthread.h>
#include <fcntl.h>

#define MAX_DRONES 5
#define MAX_TIME_STEPS 10
#define MAX_COLLISIONS 50
#define MAX_LINE_LENGTH 1024
#define GRID_SIZE 20


typedef struct {
    int x;
    int y;
    int z;
} Position;


typedef struct {
    int time_step;
    Position position;
    int drone_ids[MAX_DRONES];
    int drone_count;
} Collision;

typedef enum {
    SIM_VALID = 0, //If the figure simulation was succesful and no collisions ocurred
    SIM_FAIL = 1, //If a collision ocurred
    SIM_ERROR = 2, //If an error during simulation ocurred
} SimulationStatus;

typedef struct{
	Position positions[MAX_DRONES];
	int active_drones[MAX_DRONES];
	Collision collisions[MAX_COLLISIONS];
	int collision_count;
	int drone_count;
	int current_time_step;
	SimulationStatus status;
} SharedMemory; //Shared memory area that will be used by all the threads

typedef struct {
    int id; //The id of a drone
    pid_t pid; //The process id of a drone (each drone will be a process)
    Position position; //The position of a drone.
    bool drone_active_status; //The status of a drone.
} Drone;

extern SharedMemory *shared_mem;
extern int drone_count;
extern Drone drones[MAX_DRONES];
extern sem_t *drone_semaphore;
extern sem_t *parent_semaphore;
extern pthread_mutex_t shared_mem_mutex;
extern pthread_cond_t collision_cond; //Will be used in the collision_detection thread to notify the report_generation thread about a collision that ocurred
extern pthread_cond_t sync_cond;



#endif



