package eapli.shodrone.showrequest.domain;

import eapli.framework.validations.Preconditions;
import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Represents the duration of a request/show in minutes.
 */
@Embeddable
@EqualsAndHashCode
public final class RequestDuration implements ValueObject, Comparable<RequestDuration> {

    @Serial
    private static final long serialVersionUID = 1L;

    // Store duration in minutes for simplicity in this example
    private final long durationInMinutes;

    /**
     * Constructor to create a RequestDuration from a number of minutes.
     *
     * @param minutes The duration in minutes.
     */
    private RequestDuration(final long minutes) {
        Preconditions.ensure(minutes > 0, "RequestDuration must be greater than zero");
        this.durationInMinutes = minutes;
    }

    /**
     * Constructor to create a RequestDuration from a java.time.Duration.
     *
     * @param duration The java.time.Duration object.
     */
    private RequestDuration(final java.time.Duration duration) {
        Preconditions.nonNull(duration, "RequestDuration cannot be null");
        Preconditions.ensure(
                !duration.isNegative() &&
                    !duration.isZero(),
                "RequestDuration must be positive"
            );
        this.durationInMinutes = duration.toMinutes();
    }


    protected RequestDuration() {
        // for ORM
        this.durationInMinutes = 0; // Or handle appropriately
    }

    /**
     * Factory method to create a RequestDuration instance from a number of minutes.
     *
     * @param minutes The duration in minutes.
     * @return A new RequestDuration instance.
     */
    public static RequestDuration ofMinutes(final long minutes) {
        return new RequestDuration(minutes);
    }

    /**
     * Factory method to create a RequestDuration instance from a java.time.RequestDuration.
     *
     * @param duration The java.time.RequestDuration object.
     * @return A new RequestDuration instance.
     */
    public static RequestDuration fromDuration(final java.time.Duration duration) {
        return new RequestDuration(duration);
    }

    /**
     * Factory method to create a RequestDuration instance from an integer.
     *
     * @param durationMinutes The duration in minutes as an integer.
     * @return A new RequestDuration instance.
     */
    public static RequestDuration valueOf(int durationMinutes) {
        return new RequestDuration(durationMinutes);
    }

    /**
     * Returns the duration in minutes.
     * @return the duration in minutes
     */
    public long minutes() {
        return this.durationInMinutes;
    }

    /**
     * Returns the duration as a java.time.RequestDuration object.
     * @return the duration object
     */
    public java.time.Duration toDuration() {
        return java.time.Duration.ofMinutes(this.durationInMinutes);
    }

    /**
     * Returns the duration as a string.
     * @return the duration string
     */
    @Override
    public String toString() {
        // You might want a more descriptive toString, e.g., including "minutes"
        return String.valueOf(this.durationInMinutes);
    }

    /**
     * Compares this RequestDuration with another based on the duration in minutes.
     *
     * @param other The other RequestDuration to compare with.
     * @return A negative integer, zero, or a positive integer as this duration
     *         is less than, equal to, or greater than the specified duration.
     */
    @Override
    public int compareTo(final RequestDuration other) {
        return Long.compare(this.durationInMinutes, other.durationInMinutes);
    }
}