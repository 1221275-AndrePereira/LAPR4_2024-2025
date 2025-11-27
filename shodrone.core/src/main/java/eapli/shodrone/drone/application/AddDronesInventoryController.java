package eapli.shodrone.drone.application;

import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.drone.domain.Drone;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Controller class for adding drones to the inventory as part of the application layer.
 */
public class AddDronesInventoryController {
    private final DroneInventoryRepository droneInventoryRepository = PersistenceContext.repositories().droneInventory();
    private static final Logger log = LoggerFactory.getLogger(AddDronesInventoryController.class);

    /**
     * Adds a drone to the inventory by delegating to the repository.
     *
     * @param drone the Drone entity to be added
     * @return the added Drone entity
     * @throws IllegalArgumentException if the drone already exists
     */
    public Drone add(Drone drone) {
        if (droneInventoryRepository.exists(drone.getSerialNumber().serialNumber())) {
            throw new IllegalArgumentException("Drone with serial number " + drone.getSerialNumber() + " already exists.");
        }

        if(!droneInventoryRepository.exists(drone.getSerialNumber().serialNumber())){
            droneInventoryRepository.save(drone);
            return drone;
        }
        else {
            log.error("Drone with serial number {} already exists in the inventory.", drone.getSerialNumber());
            return null;
        }
    }
}
