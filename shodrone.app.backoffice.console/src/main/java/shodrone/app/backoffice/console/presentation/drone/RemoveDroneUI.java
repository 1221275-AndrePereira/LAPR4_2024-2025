package shodrone.app.backoffice.console.presentation.drone;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.shodrone.drone.application.RemoveDronesInventoryController;
import eapli.shodrone.drone.domain.Drone;
import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

public class RemoveDroneUI extends AbstractUI {

    private final RemoveDronesInventoryController controller = new RemoveDronesInventoryController();
    private final DroneInventoryRepository droneInventoryRepository = PersistenceContext.repositories().droneInventory();
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RemoveDroneUI.class);

    @Override
    protected boolean doShow() {
        // Fetch all drones in the inventory
        Iterable<Drone> allDrones = droneInventoryRepository.findAll();

        // Convert Iterable to List for SelectWidget
        List<Drone> droneList = new ArrayList<>();
        allDrones.forEach(droneList::add);

        if (droneList.isEmpty()) {
            System.out.println("No drones found in the inventory to remove.");
            return false;
        }

        // Let user select a drone to remove (using Drone.toString())
        final SelectWidget<Drone> droneSelector = new SelectWidget<>("Select a Drone to remove:", droneList);
        droneSelector.show();
        final Drone selectedDrone = droneSelector.selectedElement();
        if (selectedDrone == null) {
            System.out.println("No drone selected. Exiting.");
            return false;
        }

        // Confirm removal
        if (!eapli.framework.io.util.Console.readBoolean(
                "Are you sure you want to remove the selected drone with Serial Number " + selectedDrone.getSerialNumber() + "? (y/n)")) {
            System.out.println("Operation cancelled by user.");
            return false;
        }

        // Attempt to remove the drone
        try {
            controller.remove(selectedDrone);
            System.out.println("Drone removed from inventory: " + selectedDrone.getSerialNumber());
            log.info("Drone removed from inventory: {}", selectedDrone);
        } catch (IllegalArgumentException e) {
            log.error("Error removing drone from inventory: {}", e.getMessage());
            System.out.println("Operation failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error while removing drone from inventory: {}", e.getMessage());
            System.out.println("An unexpected error occurred while removing the drone from the inventory.");
            return false;
        }

        return true;
    }

    @Override
    public String headline() {
        return "Remove Drone from Inventory";
    }
}