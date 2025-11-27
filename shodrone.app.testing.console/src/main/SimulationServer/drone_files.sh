#!/bin/bash

NUM_DRONES=5
NUM_TICKS=10

for drone in $(seq 1 $NUM_DRONES); do
    echo "Creating Drone File $drone: "
    > "drone${drone}.txt"

    for tick in $(seq 1 $NUM_TICKS); do
        x=$((RANDOM % 10 + 1))
        y=$((RANDOM % 10 + 1))
        z=$((RANDOM % 10 + 1))

        echo "$x $y $z" >> "drone${drone}.txt"
    done
    echo "Drone File $drone Created"
done

echo "All Drone Files Created."
