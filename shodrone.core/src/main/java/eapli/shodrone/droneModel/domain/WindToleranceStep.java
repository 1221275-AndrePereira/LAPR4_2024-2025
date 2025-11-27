package eapli.shodrone.droneModel.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;


@Embeddable
@EqualsAndHashCode
public class WindToleranceStep implements ValueObject, Comparable<WindToleranceStep> {

    private static final long serialVersionUID = 1L;

    private final double minWindSpeed; // m/s
    private final double maxWindSpeed; // m/s
    private final double deviationInMeters;

    /**
     * Protected constructor for ORM.
     */
    protected WindToleranceStep() {
        this.minWindSpeed = 0; // Default for ORM, should be properly initialized
        this.maxWindSpeed = 0;
        this.deviationInMeters = 0;
    }

    /**
     * Constructs a WindToleranceStep.
     *
     * @param minWindSpeed      The minimum wind speed for this interval (exclusive for non-first intervals).
     * @param maxWindSpeed      The maximum wind speed for this interval (inclusive).
     * @param deviationInMeters The positional deviation in meters for this wind speed range.
     */
    public WindToleranceStep(final double minWindSpeed, final double maxWindSpeed, final double deviationInMeters) {
        Preconditions.ensure(minWindSpeed >= 0, "Minimum wind speed cannot be negative.");
        Preconditions.ensure(maxWindSpeed > minWindSpeed, "Maximum wind speed must be greater than minimum wind speed.");
        Preconditions.ensure(deviationInMeters >= 0, "Deviation cannot be negative.");

        this.minWindSpeed = minWindSpeed;
        this.maxWindSpeed = maxWindSpeed;
        this.deviationInMeters = deviationInMeters;
    }

    /**
     * Factory method to create a new WindToleranceStep.
     *
     * @param minWindSpeed      The minimum wind speed for this interval.
     * @param maxWindSpeed      The maximum wind speed for this interval.
     * @param deviationInMeters The positional deviation in meters.
     * @return a new WindToleranceStep instance.
     */
    public static WindToleranceStep valueOf(final double minWindSpeed, final double maxWindSpeed, final double deviationInMeters) {
        return new WindToleranceStep(minWindSpeed, maxWindSpeed, deviationInMeters);
    }

    public double minWindSpeed() {
        return minWindSpeed;
    }

    public double maxWindSpeed() {
        return maxWindSpeed;
    }

    public double deviationInMeters() {
        return deviationInMeters;
    }

    @Override
    public String toString() {
        return String.format("Wind: (%.1f, %.1f] m/s -> Deviation: %.2f m", minWindSpeed, maxWindSpeed, deviationInMeters);
    }

    /**
     * Compares based on the minimum wind speed, then maximum.
     * This is useful for sorting steps if needed.
     */
    @Override
    public int compareTo(final WindToleranceStep other) {
        int compareMin = Double.compare(this.minWindSpeed, other.minWindSpeed);
        if (compareMin != 0) {
            return compareMin;
        }
        return Double.compare(this.maxWindSpeed, other.maxWindSpeed);
    }
}
