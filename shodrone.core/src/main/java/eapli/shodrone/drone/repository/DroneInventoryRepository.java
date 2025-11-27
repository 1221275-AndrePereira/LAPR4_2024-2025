package eapli.shodrone.drone.repository;

import eapli.shodrone.drone.domain.Drone;
import eapli.framework.domain.repositories.DomainRepository;
import eapli.shodrone.droneModel.domain.DroneModel;

import java.util.List;
import java.util.Optional;

public interface DroneInventoryRepository
        extends DomainRepository<Long, Drone> {
    /**
     * Finds all drones in the inventory.
     *
     * @return A list of all drones.
     */
    List<Drone> allDrones();

    /**
     * Finds all active drones in the inventory.
     *
     * @return A list of all active drones.
     */
    List<Drone> allActiveDrones();

    /**
     * Finds a drone by its serial number.
     *
     * @param serialNumber The serial number of the drone.
     * @return An Optional containing the found drone, or empty if not found.
     */
    Optional<Drone> ofIdentity(Long serialNumber);

    /**
     *  Retrieves active drones of a specific model.
     *
     * @param droneModel The drone model.
     * @return List of all active drones of a model.
     */

    List<Drone> allActiveDronesOfModel(DroneModel droneModel);

    boolean exists(Long serialNumber);
    /**
     * Counts the total number of drones in the inventory.
     *
     * @return The total count of drones.
     */
    long count();

    boolean changeDroneStatus(Long serialNumber, String newStatus);
}
