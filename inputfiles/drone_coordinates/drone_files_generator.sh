#!/bin/bash

NUM_DRONES=50
NUM_TICKS=10
grid_size=20

for drone in $(seq 1 $NUM_DRONES); do
    echo "Creating Drone File $drone: "
    > "drone${drone}.txt"

    for tick in $(seq 1 $NUM_TICKS); do
        x=$((RANDOM % grid_size + 1))
        y=$((RANDOM % grid_size + 1))
        z=$((RANDOM % grid_size + 1))

        echo "$x $y $z" >> "drone${drone}.txt"
    done
    echo "Drone File $drone Created"
done

echo "All Drone Files Created."
