package eapli.shodrone.figure.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Value Object representing a short, descriptive text.
 * <p>
 * This class ensures that the description is not empty and does not exceed a predefined
 * maximum length. Leading and trailing whitespace are trimmed.
 * It is immutable.
 * </p>
 */
@Embeddable
public final class Description implements ValueObject, Comparable<Description>, Serializable {

    private static final int MAX_LENGTH = 250;

    private final String value;

    /**
     * Constructs a Description object.
     *
     * @param description The raw description text.
     */
    private Description(final String description) {
        final String trimmedDescription = description.trim();

        Preconditions.nonEmpty(
                trimmedDescription,
                "Description cannot be empty"
        );
        Preconditions.ensure(trimmedDescription.length() <= MAX_LENGTH , "Description cannot be longer than " + MAX_LENGTH);

        this.value = trimmedDescription;
    }

    /**
     * Protected no-argument constructor for ORM (JPA) frameworks.
     * This constructor is not for public use.
     */
    protected Description() {
        // for ORM.
        this.value = null;
    }

    /**
     * Factory method to create a new Description instance.
     * This is the intended public way to create a Description.
     *
     * @param description The text for the description.
     * @return A new instance of Description.
     * @throws IllegalArgumentException if the description is invalid.
     */
    public static Description valueOf(final String description) {
        return new Description(description);
    }

    /**
     * Performs an alphabetical comparison between this description and another.
     *
     * @param other The Description to be compared.
     * @return A negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(final Description other) {
        return this.value.compareTo(other.value);
    }

    /**
     * Checks for value equality. Two descriptions are equal if their text value is the same.
     *
     * @param o The other object to compare with.
     * @return true if the other object is a Description with the same text value.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Description that = (Description) o;
        return Objects.equals(this.value, that.value);
    }

    /**
     * Returns the hash code for this Description.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    /**
     * Returns the string representation of the description.
     *
     * @return The description text.
     */
    @Override
    public String toString() {
        return this.value;
    }
}