package eapli.shodrone.drone.application;

import eapli.shodrone.drone.domain.Drone;
import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for removing drones from the inventory as part of the application layer.
 */
public class RemoveDronesInventoryController {
    private final DroneInventoryRepository droneInventoryRepository = PersistenceContext.repositories().droneInventory();

    private static final Logger log = LoggerFactory.getLogger(RemoveDronesInventoryController.class);

    /**
     * Removes a drone from the inventory by delegating to the repository.
     *
     * @param drone the Drone entity to be removed
     * @return the removed Drone entity
     * @throws IllegalArgumentException if the drone does not exist
     */
    public Drone remove(Drone drone) {
        if (!droneInventoryRepository.exists(drone.getSerialNumber().serialNumber())) {
            throw new IllegalArgumentException("Drone with serial number " + drone.getSerialNumber() + " does not exist.");
        }

        if(droneInventoryRepository.exists(drone.getSerialNumber().serialNumber())){
            droneInventoryRepository.remove(drone);
            return drone;
        }
        else {
            log.error("Drone with serial number {} does not exist in the inventory.", drone.getSerialNumber());
            return null;
        }
    }
}
