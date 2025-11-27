package eapli.shodrone.figure.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A Value Object representing a semantic version number (e.g., "1.0", "2.11.5").
 * <p>
 * This class enforces the format MAJOR.MINOR.PATCH..., where each component is a non-negative integer.
 * It is immutable and provides correct semantic comparison. For example, "2.1.0" is considered
 * greater than "1.9.9", and "1.10" is greater than "1.2".
 * </p>
 *
 * @author Your Name
 */
@Embeddable
public final class Version implements ValueObject, Comparable<Version>, Serializable {

    /**
     * The regex pattern for a valid version string.
     * Ensures it consists of numbers separated by dots.
     */
    private static final Pattern VALID_VERSION_PATTERN = Pattern.compile("^[0-9]+(\\.[0-9]+)*$");

    private final String value;

    /**
     * Constructs a Version object.
     *
     * @param version The string representation of the version.
     * @throws IllegalArgumentException if the version is null, empty, or has an invalid format.
     */
    private Version(final String version) {
        Preconditions.matches(
                VALID_VERSION_PATTERN,
                version,
                "The Version must be in a valid format (e.g., '1.0', '2.1.12')."
        );
        this.value = version;
    }

    protected Version() {
        // for ORM.
        this.value = null;
    }

    /**
     * Factory method to create a new Version instance.
     *
     * @param version The string representation of the version to create.
     * @return A new instance of Version.
     * @throws IllegalArgumentException if the version is null, empty, or has an invalid format.
     */
    public static Version valueOf(final String version) {
        return new Version(version);
    }

    /**
     * Compares this version to another version numerically.
     * <p>
     * For example: "1.10" is greater than "1.2". A simple string comparison would fail this test.
     * If a version is a prefix of another (e.g., "1.2" vs "1.2.1"), the longer one is considered greater.
     * </p>
     *
     * @param o The Version object to be compared.
     * @return A negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(final Version o) {
        if (this.equals(o)) {
            return 0;
        }

        String[] thisParts = this.value.split("\\.");
        String[] otherParts = o.value.split("\\.");

        int length = Math.max(thisParts.length, otherParts.length);
        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
            int otherPart = i < otherParts.length ? Integer.parseInt(otherParts[i]) : 0;

            if (thisPart < otherPart) {
                return -1;
            }
            if (thisPart > otherPart) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Checks for value equality.
     *
     * @param o The other object to compare with.
     * @return true if the other object is a Version with the same value.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Version that = (Version) o;
        return Objects.equals(this.value, that.value);
    }

    /**
     * Returns the hash code.
     *
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    /**
     * Returns the string representation of the version.
     *
     * @return The version string (e.g., "1.2.3").
     */
    @Override
    public String toString() {
        return this.value;
    }
}