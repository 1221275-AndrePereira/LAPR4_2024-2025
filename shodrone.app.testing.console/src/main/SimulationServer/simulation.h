#ifndef SIMULATION_H
#define SIMULATION_H

// Standard library includes
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <stdbool.h>
#include <semaphore.h>
#include <pthread.h>
#include <fcntl.h>

// Project-wide constants
#define MAX_DRONES 500
#define MAX_TIME_STEPS 10
#define MAX_COLLISIONS 50
#define MAX_LINE_LENGTH 1024
#define GRID_SIZE 20
#define MAX_FILENAME_LEN 128

// Type Definitions
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
    SIM_VALID = 0,
    SIM_FAIL = 1,
    SIM_ERROR = 2,
} SimulationStatus;

// Updated state machine
typedef enum {
    STATE_IDLE,
    STATE_SIMULATING,
    STATE_DONE // Indicates simulation is finished and result is ready
} SimulationState;

typedef struct {
    Position positions[MAX_DRONES];
    int active_drones[MAX_DRONES];
    Collision collisions[MAX_COLLISIONS];
    int collision_count;
    int drone_count;
    int current_time_step;
    SimulationStatus status;
    bool simulation_paused;
    SimulationState sim_state;
    char drone_files[MAX_DRONES][MAX_FILENAME_LEN];
    bool drones_to_deactivate[MAX_DRONES];
} SharedMemory;

typedef struct {
    int id;
    pid_t pid;
    Position position;
    bool drone_active_status;
} Drone;

// Global variable declarations
extern SharedMemory *shared_mem;
extern int drone_count;
extern Drone drones[MAX_DRONES];
extern sem_t *drone_semaphore;
extern sem_t *parent_semaphore;
extern sem_t *startup_semaphore;
extern pthread_mutex_t shared_mem_mutex;
extern pthread_cond_t collision_cond;
extern pthread_cond_t sync_cond;
extern pthread_cond_t state_change_cond;
extern pthread_cond_t client_cond;

// --- Function Prototypes ---
// From simulation.c
void initialize_shared_memory_area(void);
void initialize_semaphores(void);
void initialize_simulation(void);
int init_drones(Drone *drones);
void cleanup(void);

// From thread_creator.c
void start_threads(void);

// From server.c
void *server_thread(void *arg);

// From simulation_sync.c
void drone_simulation(int drone_id);

#endif // SIMULATION_H
