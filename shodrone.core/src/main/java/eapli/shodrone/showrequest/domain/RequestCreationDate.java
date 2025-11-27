package eapli.shodrone.showrequest.domain;

import eapli.framework.validations.Preconditions;
import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Calendar;

import java.io.Serial;

/**
 * Represents the creation date of a request.
 */
@Embeddable
@EqualsAndHashCode
public final class RequestCreationDate implements ValueObject, Comparable<RequestCreationDate> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDate creationDate;

    /**
     * Constructor to create a RequestCreationDate instance.
     *
     * @param date The LocalDate representing the creation date.
     */
    private RequestCreationDate(final LocalDate date) {
        Preconditions.nonNull(date, "Creation date cannot be null");
        this.creationDate = date;
    }

    protected RequestCreationDate() {
        // for ORM
        this.creationDate = null;
    }

    /**
     * Factory method to create a RequestCreationDate instance for the current date.
     *
     * @return A new RequestCreationDate instance set to the current date.
     */
    public static RequestCreationDate now() {
        return new RequestCreationDate(LocalDate.now());
    }

    /**
     * Factory method to create a RequestCreationDate instance from a LocalDate.
     *
     * @param date The LocalDate representing the creation date.
     * @return A new RequestCreationDate instance.
     */
    public static RequestCreationDate valueOf(final LocalDate date) {
        return new RequestCreationDate(date);
    }

    /**
     * Factory method to create a RequestCreationDate instance from a Calendar.
     *
     * @param date The Calendar representing the creation date.
     * @return A new RequestCreationDate instance.
     */
    public static RequestCreationDate valueOf(final Calendar date) {
        Preconditions.nonNull(date, "Date cannot be null");
        return new RequestCreationDate(date.toInstant().atZone(date.getTimeZone().toZoneId()).toLocalDate());
    }

    /**
     * Factory method to create a RequestCreationDate instance from a String.
     * Assumes "yyyy-MM-dd" format.
     *
     * @param dateString The date string (e.g., "2023-10-27").
     * @return A new RequestCreationDate instance.
     * @throws DateTimeParseException if the string cannot be parsed.
     */
    public static RequestCreationDate valueOf(final String dateString) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        try {
            return new RequestCreationDate(LocalDate.parse(dateString));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for RequestCreationDate: " + dateString, e);
        }
    }

    /**
     * Factory method to create a RequestCreationDate instance from a String with a specific format.
     *
     * @param dateString The date string.
     * @param formatter The DateTimeFormatter to use for parsing.
     * @return A new RequestCreationDate instance.
     * @throws DateTimeParseException if the string cannot be parsed.
     */
    public static RequestCreationDate valueOf(final String dateString, DateTimeFormatter formatter) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        Preconditions.nonNull(formatter, "Formatter cannot be null");
        try {
            return new RequestCreationDate(LocalDate.parse(dateString, formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for RequestCreationDate: " + dateString +
                                                " with formatter " + formatter, e);
        }
    }

    /**
     * Returns the underlying LocalDate value.
     * @return the creation date
     */
    public LocalDate date() {
        return this.creationDate;
    }

    /**
     * Returns the creation date as a string in the default format (yyyy-MM-dd).
     * @return the creation date as a string
     */
    @Override
    public String toString() {
        return this.creationDate.toString(); // Default ISO format (yyyy-MM-dd)
    }

    /**
     * Compares this RequestCreationDate with another based on the creation date.
     *
     * @param other The other RequestCreationDate to compare with.
     * @return A negative integer, zero, or a positive integer as this date
     *         is less than, equal to, or greater than the specified date.
     */
    @Override
    public int compareTo(final RequestCreationDate other) {
        return this.creationDate.compareTo(other.creationDate);
    }
}