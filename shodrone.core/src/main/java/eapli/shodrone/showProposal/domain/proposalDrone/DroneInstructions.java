package eapli.shodrone.showProposal.domain.proposalDrone;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import org.springframework.security.core.parameters.P;

import java.io.File;


@Embeddable
public final class DroneInstructions implements ValueObject, Comparable<DroneInstructions> {

    private final String instructionsPath;

    /**
     * Private constructor to enforce creation via the factory method.
     * @param instructionsPath The path to the instructions file.
     */
    private DroneInstructions(final String instructionsPath) {
        Preconditions.noneNull(instructionsPath, "Instructions path cannot be null.");

        // Ensure if the path exists and is a valid file path, it must be a '.txt' file.
        File file = new File(instructionsPath);
        Preconditions.ensure( file.exists() && file.isFile() && file.getName().endsWith(".txt"),
                "The provided instructions path must point to a valid .txt file.");

        this.instructionsPath = instructionsPath;
    }

    /**
     * Factory method to create a new DroneInstructions instance.
     * @param instructionsPath The path to the instructions file.
     * @return a new DroneInstructions instance.
     */
    public static DroneInstructions valueOf(final String instructionsPath) {
        return new DroneInstructions(instructionsPath);
    }

    /**
     * Protected no-arg constructor for ORM.
     */
    public DroneInstructions() {
        // for ORM
        this.instructionsPath = null;
    }

    @Override
    public int compareTo(final DroneInstructions o) {
        return this.instructionsPath.compareTo(o.instructionsPath);
    }

    @Override
    public String toString() {
        return this.instructionsPath;
    }
}