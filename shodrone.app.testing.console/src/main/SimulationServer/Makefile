# Makefile for the ShowSimulator C module

# Instruction for compiling the code on windows:
#Windows Subsystem for Linux (WSL):
#This allows you to run a full Linux distribution directly on Windows,
#where the code would compile and run without any changes.

#Cygwin or MinGW-w64:
#These are toolchains that provide POSIX API compatibility libraries for Windows,
#allowing you to compile the code with minimal to no modifications.

# Instructions for compiling the code on unix based systems:
compile:
	gcc -Wall -g -o server simulation.c thread_creator.c collision_detection.c simulation_sync.c report.c server.c -pthread

compile_lrt:
	gcc -Wall -g -o server simulation.c thread_creator.c collision_detection.c simulation_sync.c report.c server.c -lpthread -lrt

# Run the compiled server program
run_server: compile
	./server

# Generate test files for the drone simulation
generate_drone_files:
	chmod +x drone_files.sh
	./drone_files.sh

# Clean up the generated drone files
clean_drone_files:
	rm -fv prog Simulation_Report.txt trace.log drone*.txt

# Clean up the compiled program
clean:
	rm -fv /dev/shm/sem.drone_semaphore
	rm -fv /dev/shm/sem.parent_semaphore
	rm -fv /dev/shm/sem.report_semaphore
	rm -fv server
