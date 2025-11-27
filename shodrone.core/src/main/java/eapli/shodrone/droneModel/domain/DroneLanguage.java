package eapli.shodrone.droneModel.domain;

import eapli.framework.validations.Preconditions;
import eapli.framework.domain.model.ValueObject;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class DroneLanguage implements ValueObject, Comparable<DroneLanguage> {

    private final String languageName;

    public DroneLanguage(String language) {
        //Preconditions.ensure(language.matches("[a-zA-Z0-9_./-]+"), "Language must be a valid path.");
        Preconditions.ensure(!language.trim().isEmpty(), "Language cannot be empty.");
        this.languageName = language;
    }

    protected DroneLanguage() {
        this.languageName = "null";
    }

    public String language() {
        return this.languageName;
    }

    public static DroneLanguage valueOf(final String language) {
        return new DroneLanguage(language);
    }

    @Override
    public int compareTo(DroneLanguage o) {
        assert this.languageName != null;
        assert o.languageName != null;
        return this.languageName.compareTo(o.languageName);
    }

    @Override
    public String toString() {
        return language();
    }
}
