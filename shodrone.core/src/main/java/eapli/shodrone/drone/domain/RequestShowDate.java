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

/**
 * Represents the show date for a request.
 */
@Embeddable
@EqualsAndHashCode
public final class RequestShowDate implements ValueObject, Comparable<RequestShowDate> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDate showDate;

    /**
     * Constructor to create a RequestShowDate instance.
     *
     * @param date The LocalDate representing the show date.
     */
    private RequestShowDate(final LocalDate date) {
        Preconditions.nonNull(date, "Show date cannot be null");
        // Add any other specific validation if needed, e.g., date must be in the future
        // Preconditions.ensure(date.isAfter(LocalDate.now()), "Show date must be in the future");
        this.showDate = date;
    }

    public RequestShowDate() {
        // for ORM
        this.showDate = null;
    }

    /**
     * Factory method to create a RequestShowDate instance from a LocalDate.
     *
     * @param date The LocalDate representing the show date.
     * @return A new RequestShowDate instance.
     */
    public static RequestShowDate valueOf(final LocalDate date) {
        return new RequestShowDate(date);
    }

    /**
     * Factory method to create a RequestShowDate instance from a Calendar.
     *
     * @param date The Calendar representing the show date.
     * @return A new RequestShowDate instance.
     */
    public static RequestShowDate valueOf(final Calendar date) {
        Preconditions.nonNull(date, "Date cannot be null");
        return new RequestShowDate(date.toInstant().atZone(date.getTimeZone().toZoneId()).toLocalDate());
    }

    /**
     * Factory method to create a RequestShowDate instance from a String.
     * Assumes "yyyy-MM-dd" format.
     *
     * @param dateString The date string (e.g., "2024-01-15").
     * @return A new RequestShowDate instance.
     * @throws DateTimeParseException if the string cannot be parsed.
     */
    public static RequestShowDate valueOf(final String dateString) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        try {
            return new RequestShowDate(LocalDate.parse(dateString));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for RequestShowDate: " + dateString, e);
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
    public static RequestShowDate valueOf(final String dateString, DateTimeFormatter formatter) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        Preconditions.nonNull(formatter, "Formatter cannot be null");
        try {
            return new RequestShowDate(LocalDate.parse(dateString, formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for RequestShowDate: " + dateString +
                    " with formatter " + formatter, e);
        }
    }


    /**
     * Returns the underlying LocalDate value.
     * @return the show date
     */
    public LocalDate date() {
        return this.showDate;
    }

    /**
     * Returns the show date as a string.
     * @return the show date string
     */
    @Override
    public String toString() {
        return this.showDate.toString(); // Default ISO format (yyyy-MM-dd)
    }

    /**
     * Compares this RequestShowDate with another based on the show date.
     *
     * @param other The other RequestShowDate to compare with.
     * @return A negative integer, zero, or a positive integer as this show date
     *         is less than, equal to, or greater than the specified show date.
     */
    @Override
    public int compareTo(final RequestShowDate other) {
        return this.showDate.compareTo(other.showDate);
    }
}