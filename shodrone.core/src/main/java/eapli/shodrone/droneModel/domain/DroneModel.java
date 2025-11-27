package eapli.shodrone.droneModel.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.validations.Preconditions;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;


@Entity
public class DroneModel implements AggregateRoot<Long> {

    @Id
    @Getter
    @GeneratedValue
    private long id;

    @Getter
    @Setter
    private String modelName;

    @Embedded
    @Getter
    private DroneLanguage language;

    @Embedded
    @Getter
    private WindToleranceProfile windToleranceProfile;

    /**
     * Constructs a DroneModel.
     *
     * @param modelName            The name of the drone model.
     * @param language             The programming language of the drone model.
     * @param windToleranceProfile The wind tolerance profile for this drone model.
     */
    public DroneModel(
            final String modelName,
            final DroneLanguage language,
            final WindToleranceProfile windToleranceProfile
    ) {
        Preconditions.noneNull(modelName, windToleranceProfile);
        Preconditions.ensure(!modelName.trim().isEmpty(), "Model name cannot be empty.");

        this.windToleranceProfile = windToleranceProfile;
        this.language = language; // Can be null if not required
        this.modelName = modelName;
    }

    public DroneModel() {
        // ORM
        // to handle incomplete Show Proposal
        this.modelName = "";
        this.language = new DroneLanguage();
        this.windToleranceProfile = new WindToleranceProfile();
    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof DroneModel that)) {
            return false;
        }
        return DomainEntities.areEqual(this, that);
    }

    public boolean isSafeToFlyAt(double windSpeed) {
        Preconditions.ensure(windSpeed >= 0, "Wind speed cannot be negative.");
        return windSpeed <= this.windToleranceProfile.getMaxSafeOperatingWindSpeed();
    }

    public double getExpectedDeviationAt(double windSpeed) {
        return this.windToleranceProfile.getDeviationForWindSpeed(windSpeed);
    }

    @Override
    public String toString() {
        return "DroneModel" + id + ":" +
                "\nModel Name = " + modelName +
                "\nLanguage Path = " + language +
                "\nWind Tolerance Profile = " + windToleranceProfile;
    }
}