package eapli.shodrone.showProposal.domain.simulationReport;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.figure.domain.CodePath;

import java.io.File;
import java.util.Objects;

public class ReportPath implements ValueObject,Comparable<ReportPath> {

    private final String path;

    /**
     * Private constructor to enforce creation via the factory method.
     *
     * @param path The path string to validate and store.
     */
    private ReportPath(final String path) {

        Preconditions.nonEmpty(path, "Path cannot be null or blank.");
        Preconditions.ensure(!path.contains(".."), "Path cannot contain '..'");

        this.path = path;
    }

    protected ReportPath(final String path, final SimulationResult simulationResult) {
        // for ORM
        this.path = null;
    }

    /**
     * Factory method to create a new CodePath instance.
     *
     * @param path The string representation of the relative file path.
     * @return A new instance of CodePath.
     * @throws IllegalArgumentException if the path is invalid.
     */
    public static ReportPath pathOf(final String path) {
        return new ReportPath(path);
    }

    /**
     * Returns the path string.
     *
     * @return The normalized relative path.
     */
    public String path() {
        return this.path;
    }

    /**
     * Provides a File object representing this path.
     * This is the appropriate place to interact with the filesystem.
     *
     * @return a new File object.
     */
    public File toFile() {
        return new File(this.path);
    }

    /**
     * Performs an alphabetical comparison of the path strings.
     */
    @Override
    public int compareTo(final ReportPath other) {
        return this.path.compareTo(other.path);
    }

    /**
     * Checks for path equality.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportPath that = (ReportPath) o;
        return Objects.equals(this.path, that.path());
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.path);
    }

    /**
     *
     * @return the whole string path of the Code Path
     */
    @Override
    public String toString() {
        return this.path;
    }


}

