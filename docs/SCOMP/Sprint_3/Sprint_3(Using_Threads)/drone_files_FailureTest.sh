#!/bin/bash

# This script creates 'droneX.txt' files for a FAILURE simulation (with collisions).
# All positions are STATIC and designed to cause a collision.


# Run this script via Makefile:
# make failure_files

NUM_DRONES=5
NUM_TICKS=10

# Collision parameters (at time step 3, drones 1 and 2 on position (5, 5, 5)).
COLLISION_TIME_STEP=3
COLLISION_X=5
COLLISION_Y=5
COLLISION_Z=5

declare -A DRONE_PATHS
DRONE_PATHS[1]="
3 3 3
4 4 4
${COLLISION_X} ${COLLISION_Y} ${COLLISION_Z}
6 6 6
7 7 7
8 8 8
9 9 9
10 10 10
11 11 11
12 12 12
"
DRONE_PATHS[2]="
7 7 7
6 6 6
${COLLISION_X} $((COLLISION_Y + 1)) ${COLLISION_Z} # Collides with Drone 1 (within 1 unit)
4 4 4
3 3 3
2 2 2
1 1 1
0 0 0
0 0 1
0 0 2
"
DRONE_PATHS[3]="
10 0 0
10 0 1
10 0 2
10 0 3
10 0 4
10 0 5
10 0 6
10 0 7
10 0 8
10 0 9
"
DRONE_PATHS[4]="
0 10 0
0 10 1
0 10 2
0 10 3
0 10 4
0 10 5
0 10 6
0 10 7
0 10 8
0 10 9
"
DRONE_PATHS[5]="
18 18 18
18 18 17
18 18 16
18 18 15
18 18 14
18 18 13
18 18 12
18 18 11
18 18 10
18 18 9
"

for drone_id in $(seq 1 $NUM_DRONES); do
    echo "Creating Drone File ${drone_id} (FAILURE TEST - Static Paths): "
    # Overwrite file with static path
    echo "${DRONE_PATHS[${drone_id}]}" | sed '/^\s*$/d' > "drone${drone_id}.txt"
    echo "Drone File ${drone_id} Created"
done

echo "All Drone Files Created for FAILURE TEST scenario (WITH COLLISION - STATIC)."
