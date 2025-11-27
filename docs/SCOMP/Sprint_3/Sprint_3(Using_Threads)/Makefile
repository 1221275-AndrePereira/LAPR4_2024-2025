prog:
	gcc -Wall -g -o prog simulation.c thread_creator.c collision_detection.c simulation_sync.c report.c -pthread -lrt

run: prog
	./prog

# RANDOM simulation
dronefiles:
	chmod +x drone_files.sh
	./drone_files.sh

# SUCCESSFUL (no collision) simulation
success_files:
	chmod +x drone_files_SuccessTest.sh
	./drone_files_SuccessTest.sh

# FAILED (with collision) simulation
failure_files:
	chmod +x drone_files_FailureTest.sh
	./drone_files_FailureTest.sh

clean:
	rm -fv prog Simulation_Report.txt trace.log drone*.txt
	rm -fv /dev/shm/sem.drone_semaphore
	rm -fv /dev/shm/sem.parent_semaphore