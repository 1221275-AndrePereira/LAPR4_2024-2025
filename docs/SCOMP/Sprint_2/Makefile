prog: simulation.c
	gcc -Wall -Wextra -fanalyzer simulation.c report.c -o prog

run: prog
	./prog

dronefiles:
	chmod +x drone_files.sh
	./drone_files.sh

clean:
	rm -fv prog trace.log drone*.txt
