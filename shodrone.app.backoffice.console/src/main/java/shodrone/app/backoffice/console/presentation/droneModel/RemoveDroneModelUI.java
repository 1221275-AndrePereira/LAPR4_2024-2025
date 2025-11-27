package shodrone.app.backoffice.console.presentation.droneModel;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.droneModel.application.RemoveDroneModelController;
import eapli.shodrone.droneModel.domain.DroneModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveDroneModelUI extends AbstractUI {

    private final RemoveDroneModelController controller = new RemoveDroneModelController();
    private static final Logger log = LoggerFactory.getLogger(RemoveDroneModelUI.class);

    @Override
    protected boolean doShow() {
        final String modelName = Console.readNonEmptyLine("Enter the name of the drone model to remove: ",
                "Model name cannot be empty. Please try again.");

        try {
            System.out.println("\nAttempting to remove drone model...");
            DroneModel removedModel = controller.removeDroneModel(modelName);
            log.info("Successfully removed drone model: {}", removedModel.identity());

            System.out.println("\nDrone Model removed successfully!");
            System.out.println("-----------------------------------");
            System.out.println("ID: " + removedModel.identity());
            System.out.println("Name: " + removedModel.getModelName());
            System.out.println("Language: " + (removedModel.getLanguage() != null ? removedModel.getLanguage().language() : "N/A"));
            System.out.println("Wind Tolerance Profile:");
            if (removedModel.getWindToleranceProfile() != null) {
                removedModel.getWindToleranceProfile().getSteps().forEach(step ->
                        System.out.printf("  Wind: (%.1f, %.1f] m/s -> Deviation: %.2f m\n",
                                step.minWindSpeed(), step.maxWindSpeed(), step.deviationInMeters())
                );
                System.out.printf("  Max Safe Operating Wind Speed: %.1f m/s\n",
                        removedModel.getWindToleranceProfile().getMaxSafeOperatingWindSpeed());
            }
            System.out.println("-----------------------------------");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\nError occurred while removing the drone model: " + e.getMessage());
            log.error("Error occurred: {}", e.getMessage());
            log.debug("Stacktrace: ", e);
            return false;
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred while removing the drone model: " + e.getMessage());
            log.error("Unexpected error: ", e);
            return false;
        }

        return true;
    }

    @Override
    public String headline() {
        return "Remove Drone Model";
    }
}