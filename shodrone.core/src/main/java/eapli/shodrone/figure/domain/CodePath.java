package eapli.shodrone.figure.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Value Object representing a relative path to a file.
 * <p>
 * This class ensures that the path is a valid, relative path string intended
 * to point to a file (i.e., does not end with a separator). It does **not**
 * interact with the filesystem to check for the file's existence, as a
 * Value Object should only validate its own state.
 * </p>
 */
@Embeddable
public final class CodePath implements ValueObject, Comparable<CodePath>, Serializable {

    private final String value;

    /**
     * Private constructor to enforce creation via the factory method.
     *
     * @param path The path string to validate and store.
     */
    private CodePath(final String path) {

        Preconditions.nonEmpty(path, "Path cannot be null or blank.");
        Preconditions.ensure(!path.contains(".."), "Path cannot contain '..'");

        this.value = path;
    }

    protected CodePath() {
        // for ORM
        this.value = null;
    }

    /**
     * Factory method to create a new CodePath instance.
     *
     * @param path The string representation of the relative file path.
     * @return A new instance of CodePath.
     * @throws IllegalArgumentException if the path is invalid.
     */
    public static CodePath valueOf(final String path) {
        return new CodePath(path);
    }

    /**
     * Returns the path string.
     *
     * @return The normalized relative path.
     */
    public String path() {
        return this.value;
    }

    /**
     * Provides a File object representing this path.
     * This is the appropriate place to interact with the filesystem.
     *
     * @return a new File object.
     */
    public File toFile() {
        return new File(this.value);
    }

    /**
     * Performs an alphabetical comparison of the path strings.
     */
    @Override
    public int compareTo(final CodePath other) {
        return this.value.compareTo(other.value);
    }

    /**
     * Checks for value equality.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CodePath that = (CodePath) o;
        return Objects.equals(this.value, that.value);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    /**
     *
     * @return the whole string value of the Code Path
     */
    @Override
    public String toString() {
        return this.value;
    }
}
