package eapli.shodrone.droneModel.domain;

import eapli.framework.validations.Preconditions;
import eapli.framework.domain.model.ValueObject;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;

import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the complete wind tolerance profile for a drone model.
 * It consists of a series of continuous, ordered wind tolerance steps.
 */
@Embeddable
@EqualsAndHashCode
public class WindToleranceProfile implements ValueObject {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final double MAX_REASONABLE_WIND_SPEED = 100.0; // m/s, arbitrary sanity check limit
    private static final double MAX_REASONABLE_DEVIATION = 100.0; // meters, arbitrary sanity check limit


    @ElementCollection(fetch = FetchType.EAGER) // Eager fetch as it's part of the DroneModel value
    @OrderColumn(name = "step_order") // Ensures the list is stored and retrieved in order
    private final List<WindToleranceStep> steps;

    /**
     * The maximum wind speed (m/s) at which the drone is considered safe to fly
     * according to this profile. This is derived from the last step.
     */
    private final double maxSafeOperatingWindSpeed;

    /**
     * Public constructor for ORM.
     */
    public WindToleranceProfile() {
        this.steps = new ArrayList<>();
        this.maxSafeOperatingWindSpeed = 0;
    }

    /**
     * Constructs a WindToleranceProfile.
     *
     * @param steps A list of WindToleranceStep objects. The list must not be empty,
     * must start from a wind speed of 0, and the steps must be continuous.
     */
    public WindToleranceProfile(final List<WindToleranceStep> steps) {
        Preconditions.nonNull(steps, "Wind tolerance steps list cannot be null.");
        Preconditions.ensure(!steps.isEmpty(), "Wind tolerance profile must have at least one step.");

        // Sort steps to ensure they are in order before validation, though UI should provide them ordered.
        List<WindToleranceStep> sortedSteps = new ArrayList<>(steps);
        Collections.sort(sortedSteps);

        validateStepsContinuityAndCoverage(sortedSteps);

        this.steps = Collections.unmodifiableList(new ArrayList<>(sortedSteps)); // Store an immutable copy
        this.maxSafeOperatingWindSpeed = calculateMaxSafeOperatingWindSpeed(this.steps);
    }

    /**
     * Validates the continuity and coverage of wind tolerance steps.
     * - Must start at 0.
     * - Must be continuous (no gaps, no overlaps based on the (min, max] convention).
     * - Values must be within reasonable limits.
     */
    private void validateStepsContinuityAndCoverage(List<WindToleranceStep> sortedSteps) {
        Preconditions.ensure(sortedSteps.get(0).minWindSpeed() == 0.0,
                "Wind tolerance profile must start with a minimum wind speed of 0.0 m/s.");

        for (int i = 0; i < sortedSteps.size(); i++) {
            WindToleranceStep currentStep = sortedSteps.get(i);

            // Sanity checks for values
            Preconditions.ensure(currentStep.maxWindSpeed() <= MAX_REASONABLE_WIND_SPEED,
                    "Maximum wind speed in a step exceeds reasonable limits: " + currentStep.maxWindSpeed());
            Preconditions.ensure(currentStep.deviationInMeters() <= MAX_REASONABLE_DEVIATION,
                    "Deviation in a step exceeds reasonable limits: " + currentStep.deviationInMeters());


            if (i > 0) {
                WindToleranceStep previousStep = sortedSteps.get(i - 1);
                // Using a small epsilon for double comparison to handle potential floating point inaccuracies
                final double epsilon = 0.00001;
                Preconditions.ensure(Math.abs(currentStep.minWindSpeed() - previousStep.maxWindSpeed()) < epsilon,
                        "Wind tolerance steps must be continuous. Gap detected after step: " + previousStep +
                                " before step: " + currentStep);
            }
        }
    }

    /**
     * Calculates the maximum safe operating wind speed based on the defined steps.
     * This is the maximum wind speed of the last defined interval.
     * The project statement implies a "not safe to fly" beyond the last defined step.
     */
    private double calculateMaxSafeOperatingWindSpeed(List<WindToleranceStep> profileSteps) {
        if (profileSteps.isEmpty()) {
            return 0.0; // Should not happen due to constructor precondition
        }
        // The max wind speed of the last step is considered the limit of safe operation.
        return profileSteps.get(profileSteps.size() - 1).maxWindSpeed();
    }

    /**
     * @return An unmodifiable list of the wind tolerance steps.
     */
    public List<WindToleranceStep> getSteps() {
        return steps; // Already unmodifiable from constructor
    }

    /**
     * @return The maximum wind speed (m/s) at which the drone is considered safe to fly.
     */
    public double getMaxSafeOperatingWindSpeed() {
        return maxSafeOperatingWindSpeed;
    }

    /**
     * Gets the deviation for a given wind speed.
     *
     * @param windSpeed the wind speed in m/s.
     * @return the deviation in meters.
     * @throws IllegalArgumentException if the wind speed is negative or exceeds the max safe operating wind speed.
     */
    public double getDeviationForWindSpeed(double windSpeed) {
        Preconditions.ensure(windSpeed >= 0, "Wind speed cannot be negative.");
        if (windSpeed > this.maxSafeOperatingWindSpeed) {
            // As per US240 example: "15 < wind - not safe to fly"
            // This implies we don't define a deviation, but rather it's an unsafe condition.
            // The caller should check maxSafeOperatingWindSpeed first, or this method
            // could throw a specific "UnsafeFlightConditionException".
            // For now, let's return a marker or throw.
            throw new IllegalArgumentException(
                    String.format("Wind speed %.1f m/s exceeds maximum safe operating speed of %.1f m/s for this drone model.",
                            windSpeed, this.maxSafeOperatingWindSpeed)
            );
        }

        for (WindToleranceStep step : this.steps) {
            // Convention: first step [0, max], subsequent (min, max]
            boolean isInStep;
            if (step.minWindSpeed() == 0.0) { // First step
                isInStep = windSpeed >= step.minWindSpeed() && windSpeed <= step.maxWindSpeed();
            } else { // Subsequent steps
                isInStep = windSpeed > step.minWindSpeed() && windSpeed <= step.maxWindSpeed();
            }

            if (isInStep) {
                return step.deviationInMeters();
            }
        }
        // Should ideally not be reached if windSpeed <= maxSafeOperatingWindSpeed
        // and steps are continuous and cover up to maxSafeOperatingWindSpeed.
        // This might indicate an issue with step definitions or the input windSpeed.
        // However, due to floating point comparisons, if windSpeed is exactly on a boundary
        // not covered by the (min,max] logic for the last step, it might fall through.
        // The project example "wind <= 5", "5 < wind <= 7" suggests the boundaries are handled.
        // If the last step is (X, Y], a windSpeed of X would not be covered by it.
        // The first step [0, A] covers 0.
        // Let's assume the last step's maxWindSpeed is the absolute upper limit.
        if (!this.steps.isEmpty() && windSpeed == 0.0 && this.steps.get(0).minWindSpeed() == 0.0) {
            return this.steps.get(0).deviationInMeters();
        }


        // This case should ideally be prevented by the windSpeed > maxSafeOperatingWindSpeed check,
        // or if windSpeed is exactly maxSafeOperatingWindSpeed and it's the end of the last interval.
        if (!this.steps.isEmpty() && Math.abs(windSpeed - this.steps.get(this.steps.size()-1).maxWindSpeed()) < 0.00001 ) {
            return this.steps.get(this.steps.size()-1).deviationInMeters();
        }


        // Fallback or error if no step is found, though logic should prevent this for valid wind speeds.
        throw new IllegalStateException("Could not determine deviation for wind speed " + windSpeed +
                ". Profile might be incomplete or logic error. Max safe speed: " + maxSafeOperatingWindSpeed);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WindToleranceProfile:\n");
        for (WindToleranceStep step : steps) {
            sb.append("  ").append(step.toString()).append("\n");
        }
        sb.append("  Max Safe Operating Wind Speed: ").append(String.format("%.1f m/s", maxSafeOperatingWindSpeed));
        return sb.toString();
    }
}
