package shodrone.app.backoffice.console.presentation.drone;

import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.drone.application.ListDronesInventoryController;
import eapli.shodrone.drone.domain.Drone;

public class ListDronesInventoryUI extends AbstractUI {

    private final ListDronesInventoryController controller = new ListDronesInventoryController();

    @Override
    protected boolean doShow() {
        System.out.println("\n--- Drones in Inventory ---");
        Iterable<Drone> drones = controller.listAllDrones();

        boolean found = false;
        for (Drone drone : drones) {
            found = true;
            System.out.println("------------------------------");
            System.out.println("Serial Number: " + drone.getSerialNumber());
            System.out.println("Status: " + drone.getStatus());
            System.out.println("Model: " + (drone.getModel() != null ? drone.getModel().getModelName() : "N/A"));
            // Add more fields if needed
        }

        if (!found) {
            System.out.println("No drones found in the inventory.");
        } else {
            System.out.println("------------------------------");
        }

        return false;
    }

    @Override
    public String headline() {
        return "List All Drones in Inventory";
    }
}