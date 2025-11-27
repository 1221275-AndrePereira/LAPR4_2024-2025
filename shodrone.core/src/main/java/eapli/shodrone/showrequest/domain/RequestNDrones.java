package eapli.shodrone.showrequest.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Represents the number of drones required for a request.
 */
@Embeddable
@EqualsAndHashCode
public final class RequestNDrones implements ValueObject, Comparable<RequestNDrones> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int numberOfDrones;

    /**
     * Constructor to create a RequestNDrones instance.
     *
     * @param number The number of drones.
     */
    private RequestNDrones(final int number) {
        Preconditions.ensure(number > 0, "Number of drones must be greater than zero");
        // Add any other validation, e.g., maximum number of drones
        // Preconditions.ensure(number <= MAX_DRONES, "Number of drones exceeds maximum allowed");
        this.numberOfDrones = number;
    }

    protected RequestNDrones() {
        // for ORM
        this.numberOfDrones = 0; // Or handle appropriately
    }

    /**
     * Factory method to create a RequestNDrones instance.
     *
     * @param number The number of drones.
     * @return A new RequestNDrones instance.
     */
    public static RequestNDrones valueOf(final int number) {
        return new RequestNDrones(number);
    }

    /**
     * Returns the number of drones.
     * @return the number of drones
     */
    public int number() {
        return this.numberOfDrones;
    }

    /**
     * Returns the number of drones as a string.
     * @return the number of drones
     */
    @Override
    public String toString() {
        return String.valueOf(this.numberOfDrones);
    }

    /**
     * Compares this RequestNDrones instance with another for ordering.
     *
     * @param other The other RequestNDrones instance to compare with.
     * @return A negative integer, zero, or a positive integer as this instance is less than,
     *         equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(final RequestNDrones other) {
        return Integer.compare(this.numberOfDrones, other.numberOfDrones);
    }
}