package eapli.shodrone.showProposal.domain.proposalDrone;

import eapli.framework.validations.Preconditions;
import eapli.framework.domain.model.ValueObject;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.io.Serial;

/**
 * Represents the number of drones required for a request.
 */
@Embeddable
@EqualsAndHashCode
public final class ProposalNDrones implements ValueObject, Comparable<ProposalNDrones> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Setter
    private int numberOfDrones;

    /**
     * Constructor to create a RequestNDrones instance.
     *
     * @param number The number of drones.
     */
    public ProposalNDrones(int number) {
        Preconditions.ensure(number > 0, "Proposed number of drones must be greater than zero");

        // Preconditions.ensure(number <= MAX_DRONES, "Number of drones exceeds maximum allowed");
        this.numberOfDrones = number;
    }

    public ProposalNDrones() {
        // for ORM
        this.numberOfDrones = 0; // Or handle appropriately
    }

    /**
     * Factory method to create a RequestNDrones instance.
     *
     * @param number The number of drones.
     * @return A new RequestNDrones instance.
     */
    public static ProposalNDrones valueOf(final int number) {
        return new ProposalNDrones(number);
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
    public int compareTo(final ProposalNDrones other) {
        return Integer.compare(this.numberOfDrones, other.numberOfDrones);
    }
}