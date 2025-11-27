package eapli.shodrone.drone.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

@Embeddable
@EqualsAndHashCode
public class DroneRemovalDate implements ValueObject, Comparable<DroneRemovalDate> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDate removalDate;

    /**
     * Constructor to create a RequestShowDate instance.
     *
     * @param date The LocalDate representing the show date.
     */
    private DroneRemovalDate(final LocalDate date) {
        Preconditions.nonNull(date, "Drone removal date cannot be null");
        // Add any other specific validation if needed, e.g., date must be in the future
        // Preconditions.ensure(date.isAfter(LocalDate.now()), "Show date must be in the future");
        this.removalDate = date;
    }

    public DroneRemovalDate() {
        // for ORM
        this.removalDate = null;
    }

    /**
     * Factory method to create a RequestShowDate instance from a LocalDate.
     *
     * @param date The LocalDate representing the show date.
     * @return A new RequestShowDate instance.
     */
    public static DroneRemovalDate valueOf(final LocalDate date) {
        return new DroneRemovalDate(date);
    }

    /**
     * Factory method to create a RequestShowDate instance from a Calendar.
     *
     * @param date The Calendar representing the show date.
     * @return A new RequestShowDate instance.
     */
    public static DroneRemovalDate valueOf(final Calendar date) {
        Preconditions.nonNull(date, "Date cannot be null");
        return new DroneRemovalDate(date.toInstant().atZone(date.getTimeZone().toZoneId()).toLocalDate());
    }

    /**
     * Factory method to create a RequestShowDate instance from a String.
     * Assumes "yyyy-MM-dd" format.
     *
     * @param dateString The date string (e.g., "2024-01-15").
     * @return A new RequestShowDate instance.
     * @throws DateTimeParseException if the string cannot be parsed.
     */
    public static DroneRemovalDate valueOf(final String dateString) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        try {
            return new DroneRemovalDate(LocalDate.parse(dateString));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for removal date: " + dateString, e);
        }
    }

    /**
     * Factory method to create a RequestShowDate instance from a String with a specific format.
     *
     * @param dateString The date string.
     * @param formatter The DateTimeFormatter to use for parsing.
     * @return A new RequestShowDate instance.
     * @throws DateTimeParseException if the string cannot be parsed.
     */
    public static DroneRemovalDate valueOf(final String dateString, DateTimeFormatter formatter) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        Preconditions.nonNull(formatter, "Formatter cannot be null");
        try {
            return new DroneRemovalDate(LocalDate.parse(dateString, formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date format for RequestShowDate: " +
                    dateString +
                    " with formatter " +
                    formatter, e
            );
        }
    }


    /**
     * Returns the underlying LocalDate value.
     * @return the show date
     */
    public LocalDate date() {
        return this.removalDate;
    }

    /**
     * Returns the show date as a string.
     * @return the show date string
     */
    @Override
    public String toString() {
        return this.removalDate.toString(); // Default ISO format (yyyy-MM-dd)
    }

    /**
     * Compares this RequestShowDate with another based on the show date.
     *
     * @param other The other RequestShowDate to compare with.
     * @return A negative integer, zero, or a positive integer as this show date
     *         is less than, equal to, or greater than the specified show date.
     */
    @Override
    public int compareTo(final DroneRemovalDate other) {
        return this.removalDate.compareTo(other.removalDate);
    }
}
