package eapli.shodrone.droneModel.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.droneModel.dto.WindToleranceStepDTO; // Import DTO
import eapli.shodrone.droneModel.domain.DroneLanguage;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.droneModel.domain.WindToleranceProfile; // Import VO
import eapli.shodrone.droneModel.domain.WindToleranceStep;   // Import VO
import eapli.shodrone.droneModel.repository.DroneModelRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@UseCaseController
public class AddDroneModelController {

    private final DroneModelRepository droneModelRepository;

    public AddDroneModelController() {
        this.droneModelRepository = PersistenceContext.repositories().droneModels();
    }

    public AddDroneModelController(final DroneModelRepository droneModelRepository) {
        this.droneModelRepository = droneModelRepository;
    }

    /**
     * Adds a new drone model to the repository.
     *
     * @param modelName        The name of the drone model.
     * @param languageCode     The code for the drone model's language.
     * @param windStepsDTOList A list of DTOs representing the wind tolerance steps.
     * @return The newly created DroneModel.
     * @throws IllegalArgumentException if preconditions are not met (e.g., empty name, null language, invalid steps).
     * @throws IllegalStateException if a model with the same name already exists.
     */
    public DroneModel addDroneModel(String modelName, String languageCode, List<WindToleranceStepDTO> windStepsDTOList) {
        Preconditions.ensure(!modelName.trim().isEmpty(), "Model name cannot be empty.");
        Preconditions.ensure(languageCode != null && !languageCode.trim().isEmpty(), "Language code cannot be empty.");
        Preconditions.nonNull(windStepsDTOList, "Wind tolerance steps list cannot be null.");
        Preconditions.ensure(!windStepsDTOList.isEmpty(), "Wind tolerance steps list cannot be empty.");

        // Check if model name already exists
        Optional<DroneModel> existingModel = droneModelRepository.findByModelName(modelName);
        if (existingModel.isPresent()) {
            throw new IllegalStateException("A drone model with the name '" + modelName + "' already exists.");
        }

        final DroneLanguage language = DroneLanguage.valueOf(languageCode);

        // Convert DTOs to WindToleranceStep Value Objects
        List<WindToleranceStep> windToleranceSteps = windStepsDTOList.stream()
                .map(dto -> WindToleranceStep.valueOf(
                        dto.getMinWindSpeed(),
                        dto.getMaxWindSpeed(),
                        dto.getDeviation()
                    )
                ).collect(Collectors.toList());

        // Create the WindToleranceProfile; its constructor will validate continuity and other rules.
        final WindToleranceProfile windToleranceProfile = new WindToleranceProfile(windToleranceSteps);

        final DroneModel droneModel = new DroneModel(modelName, language, windToleranceProfile);

        return this.droneModelRepository.save(droneModel);
    }
}
