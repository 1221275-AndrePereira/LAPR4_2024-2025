@echo off
:: Enable delayed expansion to correctly handle variables inside loops
setlocal enabledelayedexpansion

:: This script creates a set of files, each containing random 3D coordinates.

:: --- Configuration ---
set "NUM_DRONES=50"
set "NUM_TICKS=10"

echo Starting drone file creation...
echo.

:: Main loop to iterate through each drone
for /l %%d in (1, 1, %NUM_DRONES%) do (
    echo Creating Drone File %%d...

    :: Create or overwrite the file for the current drone
    > "drone%%d.txt"

    :: Inner loop to generate data for each tick
    for /l %%t in (1, 1, %NUM_TICKS%) do (
        :: Generate random numbers for x, y, and z coordinates (between 1 and 10)
        set /a "x=!RANDOM! %% 10 + 1"
        set /a "y=!RANDOM! %% 10 + 1"
        set /a "z=!RANDOM! %% 10 + 1"

        :: With delayed expansion, we use !variable! to get the current value inside a loop
        :: and append the coordinates to the drone's file.
        echo !x! !y! !z! >> "drone%%d.txt"
    )
    echo Drone File %%d Created.
    echo.
)

echo All drone files have been created successfully.

:: End local environment. This discards any changes to variables,
:: so manual cleanup is no longer needed.
endlocal

