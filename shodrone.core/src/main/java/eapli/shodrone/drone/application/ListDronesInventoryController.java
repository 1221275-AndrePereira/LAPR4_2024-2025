package eapli.shodrone.drone.application;

import eapli.shodrone.drone.domain.Drone;
import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import java.util.List;

public class ListDronesInventoryController {

    private final DroneInventoryRepository droneInventoryRepository = PersistenceContext.repositories().droneInventory();

    /**
     * Lists all drones in the inventory.
     *
     * @return a list of all Drone entities in the inventory
     */
    public List<Drone> listAllDrones() {
        return droneInventoryRepository.allDrones();
    }

    /**
     * Returns a list of all drones of the same model
     * @param model model of the drones to find
     * @return
     */
    public List<Drone> listAllDronesOfModel(DroneModel model) {
        return droneInventoryRepository.allActiveDronesOfModel(model);
    }

}
