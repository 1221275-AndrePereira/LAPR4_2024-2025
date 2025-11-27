package shodrone.app.backoffice.console.presentation.drone;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.shodrone.drone.application.ChangeDroneStatusInventoryController;
import eapli.shodrone.drone.domain.Drone;
import eapli.shodrone.drone.domain.DroneStatus;
import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import java.util.List;

public class ChangeDroneStatusUI extends AbstractUI {

    private final ChangeDroneStatusInventoryController controller = new ChangeDroneStatusInventoryController();
    private final DroneInventoryRepository droneInventoryRepository = PersistenceContext.repositories().droneInventory();
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChangeDroneStatusUI.class);

    @Override
    protected boolean doShow() {
        // Fetch all drones
        Iterable<Drone> allDrones = droneInventoryRepository.findAll();
        List<Drone> droneList = (allDrones instanceof List)
                ? (List<Drone>) allDrones
                : new java.util.ArrayList<Drone>() {{
            allDrones.forEach(this::add);
        }};

        if (droneList.isEmpty()) {
            System.out.println("No drones found in the inventory to change status.");
            return false;
        }

        // Let user select a drone
        final SelectWidget<Drone> droneSelector = new SelectWidget<>("Select the Drone to change status:", droneList);
        droneSelector.show();
        final Drone selectedDrone = droneSelector.selectedElement();
        if (selectedDrone == null) {
            System.out.println("No drone selected. Exiting.");
            return false;
        }

        // Let user select a new status
        DroneStatus[] statuses = DroneStatus.values();
        final SelectWidget<DroneStatus> statusSelector = new SelectWidget<>("Select the new status for the drone:", List.of(statuses));
        statusSelector.show();
        final DroneStatus selectedStatus = statusSelector.selectedElement();
        if (selectedStatus == null) {
            System.out.println("No status selected. Exiting.");
            return false;
        }

        // Attempt to change the status
        try {
            boolean success = controller.changeDroneStatus(selectedDrone.getSerialNumber().serialNumber(), selectedStatus.name());
            if (success) {
                System.out.println("Drone status changed successfully to " + selectedStatus.name());
                log.info("Drone status changed: {} -> {}", selectedDrone, selectedStatus.name());
            } else {
                System.out.println("Failed to change drone status.");
                log.error("Failed to change drone status: {} -> {}", selectedDrone, selectedStatus.name());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Operation failed: " + e.getMessage());
            log.error("Error changing drone status: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while changing the drone status.");
            log.error("Unexpected error changing drone status: {}", e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public String headline() {
        return "Change Drone Status";
    }
}