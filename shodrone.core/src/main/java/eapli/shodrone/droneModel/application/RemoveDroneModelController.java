package eapli.shodrone.droneModel.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.droneModel.repository.DroneModelRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import java.util.Optional;

@UseCaseController
public class RemoveDroneModelController {

    private final DroneModelRepository droneModelRepository;

    // Correct constructor: no return type, matches class name
    public RemoveDroneModelController() {
        this.droneModelRepository = PersistenceContext.repositories().droneModels();
    }

    public RemoveDroneModelController(final DroneModelRepository droneModelRepository) {
        this.droneModelRepository = droneModelRepository;
    }

    /**
     * Removes a drone model by its model name.
     *
     * @param modelName The name of the drone model to remove.
     * @return The removed DroneModel.
     * @throws IllegalArgumentException if the model name is empty.
     * @throws IllegalStateException if the model does not exist.
     */
    public DroneModel removeDroneModel(String modelName) {
        Preconditions.ensure(modelName != null && !modelName.trim().isEmpty(), "Model name cannot be empty.");

        Optional<DroneModel> existingModel = droneModelRepository.findByModelName(modelName);
        if (existingModel.isEmpty()) {
            throw new IllegalStateException("A drone model with the name '" + modelName + "' does not exist.");
        }

        droneModelRepository.remove(existingModel.get());
        return existingModel.get();
    }
}