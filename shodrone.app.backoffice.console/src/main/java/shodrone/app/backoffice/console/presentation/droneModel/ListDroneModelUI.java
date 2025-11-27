package shodrone.app.backoffice.console.presentation.droneModel;

import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.droneModel.application.ListDroneModelController;
import eapli.shodrone.droneModel.domain.DroneModel;

import java.util.List;

public class ListDroneModelUI extends AbstractUI {

    private final ListDroneModelController controller = new ListDroneModelController();

    @Override
    protected boolean doShow() {
        System.out.println("\n--- List of Drone Models ---");
        List<DroneModel> droneModels = controller.listAllDroneModels();

        if (droneModels.isEmpty()) {
            System.out.println("No drone models found.");
            return false;
        }

        for (DroneModel model : droneModels) {
            System.out.println("-----------------------------------");
            System.out.println("ID: " + model.identity());
            System.out.println("Name: " + model.getModelName());
            System.out.println("Language: " + (model.getLanguage() != null ? model.getLanguage().language() : "N/A"));
            System.out.println("Wind Tolerance Profile:");
            if (model.getWindToleranceProfile() != null) {
                model.getWindToleranceProfile().getSteps().forEach(step ->
                        System.out.printf("  Wind: (%.1f, %.1f] m/s -> Deviation: %.2f m\n",
                                step.minWindSpeed(), step.maxWindSpeed(), step.deviationInMeters())
                );
                System.out.printf("  Max Safe Operating Wind Speed: %.1f m/s\n",
                        model.getWindToleranceProfile().getMaxSafeOperatingWindSpeed());
            }
            System.out.println("-----------------------------------");
        }

        return false;
    }

    @Override
    public String headline() {
        return "List Drone Models";
    }
}