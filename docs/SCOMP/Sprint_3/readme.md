## Auto-Avaliação

Jorge Sousa - 33%

André Pereira – 34%

Pedro Almeida – 34%

## US's Description

### US361 - "Initiate hybrid simulation environment with shared memory"

- This US is represented by the files: simulation.c and simulation.h.
- This US is responsible for initializing the simulation using shared memory(initializes the shared memory), initializing the drone processes and the semaphores to be used.


### US362 - "Implement function-specific threads in the parent process"

- This US is represented by the files: thread_creator.c and thread_creator.h.
- This US is responsible for creating the three dedicated threads the simulation will use: a thread for the report generation; a thread for the collision detection and another thread for the simulation synchronization.
- After creating the threads, it also blocks the advance of the code past this file into the main file until all threads have finalized.

### US363 - "Notify report thread via condition variables upon collision"

- This US is represented by the files: collision_detection.c
- This US is responsible for detecting the collisions between drones. 
- A collision occurs when two drones are still active and their positions are within a distance of 1 from each other on all three axes(x, y, z).
- When a collision is detected, the drone's status is set to inactive and the collision is logged in a shared memory structure.
- The thread will also notify the report thread of the collision by using a condition variable.

### US364 - "Enforce step-by-step simulation synchronization"

- This US is represented by the file: simulation_sync.c 
- This US is responsible for ensuring step-by-step synchronization of the drone simulation. 
- Each drone runs its simulation in separate threads, but all drones must only proceed to the next simulation step when all have completed the current step. 
- Synchronization is implemented using condition variables and mutexes, so that each drone thread waits until all others are ready before proceeding. 
- In this way, the simulation remains synchronized, preventing any drone from advancing faster than the others.


### US365 - "Generate and store final simulation report"

- This US is represented by the files: report.c and report.h.
- This will be one of the three dedicated threads of the program.
- Just like her Sprint 2 counterpart, this US is responsible for generating the final report of the simulation.
- The report will include the final result of the simulation by obtaining the status of the simulation. It also includes the total number of drones involved in the simulation and the total number of active and inactive drones(in this simulation a drone becomes inactive after being involved in a collision), the number of collisions that ocurred and the time-step and position in which the collision ocurred.
- The collisions will be obtained in real time by being notified of their ocurrence by the thread responsible for their detection.
- The report will be saved into a file called Simulation_Report.txt.


## Files

### drone_files.sh

- Script used to create txt files with randomized drone positions

### simulation.c

- The main file for the simulation.
- Represents US361.

### thread_creator.c

- The file containing the code responsible for creating the threads.
- Represents US362.

### collision_detection.c

- The file containing the thread responsible for detecting the collisions.
- Represents US363.

### simulation_sync.c

- The file containing the thread responsible for handling the synchronization of the simulation.
- This file will also contain the code necessary to read the drone file and initialize the drone's positions.
- Represents US364.

### report.c

- The file containing the thread responsible for generating the final report and store it in a file called Simulation_Report.txt.
- Represents US365.



## Notes

- Updating the file reading and drone initialization previously handled in US262 last Sprint has now been delegated to US364