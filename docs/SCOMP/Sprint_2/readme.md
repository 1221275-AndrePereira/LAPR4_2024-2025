## Auto-Avaliação

Jorge Sousa - 33%

André Pereira – 34%

Pedro Almeida – 34% 






## US's Description

### US261 - "Initiate simulation for a figure"

- In this US, the arrays used in the simulation will be initialized.
- The initial time positions and time-step data will be read from a txt file.
- Each drone will have its own process, pipes set-up and the initial state of each drone will be stored.

### US262 - "Capture and process drone movements"

- In this US, we must run the drone_files.sh script, with the command 'make dronefiles',
which will create files with the name "droneX.txt"
- Each file will contain random data for the drone (X, Y, Z coordinates and speed).,
- After that, we will compile the main.c file, with the following command: 'make prog',
- To run the program, we will use the command: 'make run',
- The main process synchronizes all movements, logs data, and detects collisions.,
- To clear the files created by the script, we will use the command: 'make clean'.
### US263 - "Detect drone collisions in real time"

- In this US, a 3D matrix is used to keep track of the drones in each coordinate
- A collision happens when a drone moves into a coordinate occupied by other drone

### US264 - "Synchronize drone execution with a time step"

- In this US, each drone sleeps during 1 second before sending its next position.
- The 3D matrix will be reset after each time-step to simulate the 3D space only for the current time-step.
### US265 - "Generate a simulation report"

- In this US, a report of the simulation will be generated into a file, which by default we named "Simulation_Report.txt".
- The report.c file will obtain the array of drones and the total number of drones, the array of collisions and the total number of collisions and the status of the simulation.
- The report itself will begin by showing the result of the simulation. Then, it will show the total number of drones and the total number of active and inactive drones. After this it will also show the total number of collisions and, in case at least one occurred during the simulation will display the time step and the position coordinates in which it occurred. 


