package shodrone.app.backoffice.console.presentation.droneModel;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import eapli.shodrone.droneModel.application.AddDroneModelController;
import eapli.shodrone.droneModel.dto.WindToleranceStepDTO;
import eapli.shodrone.droneModel.domain.DroneModel;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class AddDroneModelUI extends AbstractUI {

    private final AddDroneModelController controller = new AddDroneModelController();
    private static final Logger log = LoggerFactory.getLogger(AddDroneModelUI.class);

    private boolean checkIfFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }

    @Override
    protected boolean doShow() {
        final String basePath = "inputfiles/lprog/droneProgrammingLanguage/";

        final String modelName = Console.readNonEmptyLine("Enter Drone Model Name: ",
                "Model name cannot be empty. Please try again.");

        String languageCode;

        while(true) {
            System.out.println(basePath);
            String languageFileName = Console.readNonEmptyLine(
                    "Enter language code filename (e.g., 'DroneCode.txt'): ",
                    "Language code cannot be empty. Please try again.");

            languageCode = basePath + languageFileName;

            if (checkIfFileExists(languageCode)) {
                log.info("Language file found: {}", languageCode);
                break;
            } else {
                System.out.println("ERROR: Language file does not exist at the specified path: " + languageCode);
                log.error("Language file does not exist at the specified path: {}", languageCode);
            }
        }

        // --- Wind Tolerance Profile Input ---
        System.out.println("\n--- Define Wind Tolerance Profile ---");
        List<WindToleranceStepDTO> windStepsDTOList = new ArrayList<>();
        double currentMinWindSpeed = 0.0; // First step always starts at 0
        boolean addMoreSteps = true;
        int stepNumber = 1;

        System.out.println("You will now define wind tolerance steps. " +
                "Each step defines a wind speed range and the drone's positional deviation within that range.");
        System.out.println("The first step starts at 0.0 m/s. Subsequent steps must be continuous.");

        while (addMoreSteps) {
            System.out.printf("\nDefining Step %d:\n", stepNumber);
            double minSpeedForThisStep;

            if (stepNumber == 1) {
                minSpeedForThisStep = 0.0;
                System.out.printf("Minimum wind speed for this step: %.1f m/s (fixed for first step)\n",
                        minSpeedForThisStep
                    );
            } else {
                minSpeedForThisStep = currentMinWindSpeed; // This is the max of the previous step
                System.out.printf("Minimum wind speed for this step: > %.1f m/s (continuous from previous step)\n",
                        minSpeedForThisStep
                    );
            }

            double maxWindSpeed;
            while (true) {
                maxWindSpeed = Console.readDouble(
                        String.format("Enter maximum wind speed for this step (m/s) (must be > %.1f): ",
                                minSpeedForThisStep
                            ));
                if (maxWindSpeed > minSpeedForThisStep) {
                    break;
                }
                System.out.println("Invalid maximum wind speed. " +
                        "It must be greater than the minimum wind speed for this step.");
            }

            double deviation;
            while (true) {
                deviation = Console.readDouble(
                        "Enter positional deviation for this wind speed range (meters, >= 0): "
                );
                if (deviation >= 0) {
                    break;
                }
                System.out.println("Invalid deviation. It must be a non-negative value.");
            }

            windStepsDTOList.add(new WindToleranceStepDTO(minSpeedForThisStep, maxWindSpeed, deviation));
            System.out.printf("Step %d added: Wind (%.1f, %.1f] m/s -> Deviation: %.2f m\n",
                    stepNumber, minSpeedForThisStep, maxWindSpeed, deviation);

            currentMinWindSpeed = maxWindSpeed; // For the next step's minimum
            stepNumber++;

            if (!Console.readBoolean("Do you want to add another wind tolerance step? (y/n)")) {
                System.out.printf("The maximum wind speed of the last step (%.1f m/s) will be considered " +
                        "the limit for safe operation. Beyond this, it's considered unsafe to fly.\n",
                        currentMinWindSpeed
                    );
                addMoreSteps = false;
            }
        }

        if (windStepsDTOList.isEmpty()) {
            System.out.println("ERROR: At least one wind tolerance step must be defined.");
            return false;
        }

        // --- Call Controller to Add Drone Model ---
        try {
            System.out.println("\nAttempting to create drone model...");
            DroneModel createdDroneModel = controller.addDroneModel(modelName, languageCode, windStepsDTOList);
            log.info("Successfully created drone model: {}", createdDroneModel.getId());
            System.out.println("\nDrone Model created successfully!");
            System.out.println("-----------------------------------");
            System.out.println("ID: " + createdDroneModel.identity());
            System.out.println("Name: " + createdDroneModel.getModelName());
            System.out.println("Language: " + createdDroneModel.getLanguage());
            System.out.println("Wind Tolerance Profile:");
            createdDroneModel.getWindToleranceProfile().getSteps().forEach(step ->
                    System.out.printf("  Wind: (%.1f, %.1f] m/s -> Deviation: %.2f m\n",
                            step.minWindSpeed(), step.maxWindSpeed(), step.deviationInMeters())
            );
            System.out.printf("  Max Safe Operating Wind Speed: %.1f m/s\n",
                    createdDroneModel.getWindToleranceProfile().getMaxSafeOperatingWindSpeed());
            System.out.println("-----------------------------------");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\nError occurred while creating the drone model: " + e.getMessage());
            log.error("Error occurred  {} ", e.getMessage());
            log.debug(e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred while creating the drone model: " + e.getMessage());
            return false;
        }

        return false;
    }

    @Override
    public String headline() {
        return "Add New Drone Model";
    }
}