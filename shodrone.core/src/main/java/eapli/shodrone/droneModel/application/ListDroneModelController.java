package eapli.shodrone.droneModel.application;

import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.droneModel.repository.DroneModelRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class ListDroneModelController {
    // This controller will handle the listing of drone models.
    // It will interact with the DroneModelRepository to fetch the list of models.

    private final DroneModelRepository droneModelRepository;

    /**
     * Constructor for ListDroneModelController.
     * Initializes the DroneModelRepository using the PersistenceContext.
     */
    public ListDroneModelController() {
        this.droneModelRepository = PersistenceContext.repositories().droneModels();
    }

    /**
     * Lists all drone models available in the repository.
     *
     * @return A list of all DroneModel objects.
     */
    public List<DroneModel> listAllDroneModels() {
        return (List<DroneModel>) droneModelRepository.findAll();
    }

    /**
     * Lists a drone model by its name.
     *
     * @param modelName The name of the drone model to search for.
     * @return An Optional containing the DroneModel if found, or empty if not found.
     */
    public Optional<DroneModel> listDroneModelsByName(String modelName) {
        return droneModelRepository.findByModelName(modelName);
    }
}
