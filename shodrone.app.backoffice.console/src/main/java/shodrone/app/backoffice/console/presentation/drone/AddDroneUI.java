package shodrone.app.backoffice.console.presentation.drone;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;

import eapli.shodrone.drone.application.AddDronesInventoryController;
import eapli.shodrone.drone.domain.Drone;
import eapli.shodrone.drone.domain.DroneStatus;
import eapli.shodrone.drone.domain.SerialNumber;
import eapli.shodrone.droneModel.application.ListDroneModelController;
import eapli.shodrone.droneModel.domain.DroneModel;

import java.util.List;

public class AddDroneUI extends AbstractUI {

    private final AddDronesInventoryController controller = new AddDronesInventoryController();
    private final ListDroneModelController listModelController = new ListDroneModelController();
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AddDroneUI.class);

    private SerialNumber serialNumber;
    private DroneStatus droneStatus;
    private DroneModel droneModel;

    @Override
    protected boolean doShow() {
        //Serial input
        final Long serialNumberInput = Console.readLong("Enter Drone Serial Number: ");
        this.serialNumber = SerialNumber.valueOf(serialNumberInput);

        // List status options
        DroneStatus[] options = DroneStatus.values();

        // Select status
        final SelectWidget<DroneStatus> userSelector = new SelectWidget<>("Available statuses:", List.of(options));
        userSelector.show();
        final DroneStatus selectedStatus = userSelector.selectedElement();
        if (selectedStatus == null) {
            System.out.println("No status selected. Exiting.");
            return false;
        }
        this.droneStatus = selectedStatus;

        //List Drone Models
        final List<DroneModel> droneModels = listModelController.listAllDroneModels();
        if (droneModels.isEmpty()) {
            System.out.println("No Drone Models available. Please add a Drone Model first.");
            return false;
        }
        final SelectWidget<DroneModel> modelSelector = new SelectWidget<>("Available Drone Models:", droneModels);
        modelSelector.show();
        final DroneModel selectedModel = modelSelector.selectedElement();
        if (selectedModel == null) {
            System.out.println("No Drone Model selected. Exiting.");
            return false;
        }
        this.droneModel = selectedModel;

        // No programming language prompt here!

        // Create Drone instance and add to inventory
        Drone drone = new Drone(
                this.serialNumber,
                this.droneStatus,
                this.droneModel
        );
        try {
            controller.add(drone);
            log.debug("Drone added to inventory: {}", drone);
        } catch (final IllegalArgumentException e) {
            log.error("Error adding drone to inventory: {}", e.getMessage());
            System.out.println("Operation failed: " + e.getMessage());
            return false;
        } catch (final Exception e) {
            log.error("Unexpected error while adding drone to inventory: {}", e.getMessage());
            System.out.println("An unexpected error occurred while adding the drone to the inventory.");
            return false;
        }

        return true;
    }

    @Override
    public String headline() {
        return "Add Drone to Inventory";
    }
}