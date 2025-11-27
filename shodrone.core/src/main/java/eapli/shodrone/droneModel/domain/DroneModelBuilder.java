package eapli.shodrone.droneModel.domain;

import eapli.framework.domain.model.DomainFactory;
import eapli.framework.validations.Preconditions;

import java.util.List; // Required for WindToleranceProfile if passing steps directly

public class DroneModelBuilder implements DomainFactory<DroneModel> {

    private String modelName;
    private DroneLanguage language;
    private WindToleranceProfile windToleranceProfile; // Added

    public DroneModelBuilder withModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public DroneModelBuilder withLanguage(DroneLanguage language) {
        this.language = language;
        return this;
    }

    /**
     * Sets the wind tolerance profile for the drone model being built.
     * @param windToleranceProfile The wind tolerance profile.
     * @return The builder instance.
     */
    public DroneModelBuilder withWindToleranceProfile(WindToleranceProfile windToleranceProfile) {
        this.windToleranceProfile = windToleranceProfile;
        return this;
    }

    @Override
    public DroneModel build() {
        // Preconditions for building can be added here if desired,
        // though the DroneModel constructor will also validate.
        Preconditions.noneNull(modelName, language, windToleranceProfile);
        return new DroneModel(modelName, language, windToleranceProfile);
    }
}