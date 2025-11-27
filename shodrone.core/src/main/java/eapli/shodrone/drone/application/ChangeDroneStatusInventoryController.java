package eapli.shodrone.drone.application;

import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

public class ChangeDroneStatusInventoryController {

    private final DroneInventoryRepository droneInventoryRepository = PersistenceContext.repositories().droneInventory();

    /**
     * Changes the status of a drone in the inventory.
     *
     * @param serialNumber the serial number of the drone
     * @param newStatus the new status to set for the drone
     * @return true if the status was successfully changed, false otherwise
     */
    public boolean changeDroneStatus(Long serialNumber, String newStatus) {
        if (!droneInventoryRepository.exists(serialNumber)) {
            throw new IllegalArgumentException("Drone with serial number " + serialNumber + " does not exist.");
        }

        return droneInventoryRepository.changeDroneStatus(serialNumber, newStatus);
    }
}
