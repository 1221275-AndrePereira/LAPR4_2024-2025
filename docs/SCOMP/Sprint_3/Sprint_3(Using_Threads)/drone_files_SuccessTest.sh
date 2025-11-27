#!/bin/bash

# This script creates 'droneX.txt' files for a SUCCESSFUL simulation (no collisions).
# All positions are STATIC and designed to avoid collisions.


# Run this script via Makefile:
# make success_files

NUM_DRONES=5
NUM_TICKS=10

# Define static paths for each drone to ensure no collisions
# Drone paths are designed to be far apart.
# Each line represents a time step for a drone.
declare -A DRONE_PATHS
DRONE_PATHS[1]="
1 1 1
1 1 2
1 1 3
1 1 4
1 1 5
1 1 6
1 1 7
1 1 8
1 1 9
1 1 10
"
DRONE_PATHS[2]="
5 5 5
5 5 6
5 5 7
5 5 8
5 5 9
5 5 10
5 5 11
5 5 12
5 5 13
5 5 14
"
DRONE_PATHS[3]="
10 10 10
10 10 11
10 10 12
10 10 13
10 10 14
10 10 15
10 10 16
10 10 17
10 10 18
10 10 19
"
DRONE_PATHS[4]="
1 15 1
1 15 2
1 15 3
1 15 4
1 15 5
1 15 6
1 15 7
1 15 8
1 15 9
1 15 10
"
DRONE_PATHS[5]="
15 1 1
15 1 2
15 1 3
15 1 4
15 1 5
15 1 6
15 1 7
15 1 8
15 1 9
15 1 10
"

for drone_id in $(seq 1 $NUM_DRONES); do
    echo "Creating Drone File ${drone_id} (SUCCESS TEST - Static Paths): "
    # Overwrite file with static path
    echo "${DRONE_PATHS[${drone_id}]}" | sed '/^\s*$/d' > "drone${drone_id}.txt"
    echo "Drone File ${drone_id} Created"
done

echo "All Drone Files Created for SUCCESS TEST scenario (NO COLLISION - STATIC)."