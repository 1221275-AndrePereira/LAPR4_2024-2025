#include "simulation.h"
#include "report.h"
#include "thread_creator.h"
#include "simulation_sync.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <semaphore.h>
#include <pthread.h>
#include <fcntl.h>


SharedMemory *shared_mem;
int drone_count = 0;
Drone drones[MAX_DRONES];
sem_t *drone_semaphore;
sem_t *parent_semaphore;
pthread_mutex_t shared_mem_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t collision_cond = PTHREAD_COND_INITIALIZER;
pthread_cond_t sync_cond = PTHREAD_COND_INITIALIZER;

//Initialize the shared memory area
void initialize_shared_memory_area(void) {
	shared_mem = mmap(NULL,sizeof(SharedMemory), PROT_READ | PROT_WRITE, MAP_SHARED | MAP_ANONYMOUS,-1,0); //Create the Shared Memory area
	//In case an error ocurred
	if(shared_mem == MAP_FAILED){
		perror("Failed to create the shared memory area");
		return;
	}
}

//Initialilze the semaphores
void initialize_semaphores(void) {
	drone_semaphore = sem_open("/drone_semaphore", O_CREAT | O_EXCL, 0644, 0); //Open the semaphore for drone process
	//In case of failure
	if(drone_semaphore == SEM_FAILED){
		perror("Failure in creating the drone process semaphore");
		munmap(shared_mem, sizeof(SharedMemory)); //Unmaps the shared memory area
		return;
	}
	
	parent_semaphore = sem_open("/parent_semaphore", O_CREAT | O_EXCL, 0644, 0); //Open the semaphore for parent process
	
	//In case of failure
	if(parent_semaphore == SEM_FAILED){
		perror("Failure in creating the parent process semaphore");
		sem_close(drone_semaphore);
		sem_unlink("/drone_semaphore"); //CLoses the drone process semaphore
		munmap(shared_mem, sizeof(SharedMemory)); //Unmaps the shared memory area
		return;
	}
}

//Initialize arrays and the simulation status
void initialize_simulation(void) {
    memset(drones, 0, sizeof(drones)); //Initialize the drones array
    memset(shared_mem, 0, sizeof(SharedMemory)); //Initialize the Shared Memory array
    shared_mem->status = SIM_VALID; //Simulation status set to valid as no collisions have ocurred yet
    for(int i=0; i < MAX_DRONES; i++){
		shared_mem->active_drones[i]=1; //Assuming all drones start the simulation as active
	}
    
}

//Initialize the drones for the simulation
int init_drones(Drone *drones) {
    int count = 0;

    for (int i = 0; i < MAX_DRONES; i++) {

		//Fork a new process
        pid_t pid = fork();

        if (pid < 0) {
            perror("Failure to fork drone process");
            continue;
        } else if (pid == 0) {
            // Drone process
            drone_simulation(i); //Just like last Sprint the drone process will call the function drone_simulation that will be responsible for reading the drone's positions from the files. But this time using the Shared Memory area instead of pipes.
            exit(0);
        } else {
			//Main process
            drones[i].id = i;
            drones[i].pid = pid;
            drones[i].drone_active_status = true;

            count++;
        }
    }
    return count;
}

//Cleans up all drone processes, semaphores and the shared memory area
void cleanup(void){
	//Clean up all drone processes
	for (int i = 0; i < drone_count; i++) {
        if (drones[i].drone_active_status) {
            kill(drones[i].pid, SIGTERM);
            waitpid(drones[i].pid, NULL, 0);
        }
    }
    
    sem_close(drone_semaphore); //Close the semaphore for the drone processes
    sem_close(parent_semaphore); //Close the parent process semaphore
    sem_unlink("/drone_semaphore");
    sem_unlink("/parent_semaphore");
    munmap(shared_mem, sizeof(SharedMemory)); //Unmaps the shared memory area
    
    
}


int main() {
	
	//Initialize the shared memory area
	initialize_shared_memory_area();
	
	//Initialize the semaphores
	initialize_semaphores();
	
    // Initialize arrays for simulation
    initialize_simulation();

    // Initialize drones for the simulation
    drone_count = init_drones(drones);
	//"Save" the total drone count to the Shared Memory area
    shared_mem->drone_count = drone_count;
    
    //The key part used in the full simulation sync. Avoids threads starting before all drone positions could be read from the files
    sleep(1);
    
    //Calls thread_creator.c to create and start all three threads
    start_threads();

	//Processes, semaphores and shared memory cleanup
    cleanup();

    return 0;
}

