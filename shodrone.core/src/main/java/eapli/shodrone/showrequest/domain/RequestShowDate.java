package eapli.shodrone.showrequest.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;
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

    private final LocalDateTime showDate;

    /**
     * Constructor to create a RequestShowDate instance.
     *
     * @param date The LocalDateTime representing the show date.
     */
    private RequestShowDate(final LocalDateTime date) {
        Preconditions.nonNull(date, "Show date cannot be null");
        // Add any other specific validation if needed, e.g., date must be in the future
        // Preconditions.ensure(date.isAfter(LocalDateTime.now()), "Show date must be in the future");
        this.showDate = date;
    }

    protected RequestShowDate() {
        // for ORM
        this.showDate = null;
    }

    /**
     * Factory method to create a RequestShowDate instance from a LocalDateTime.
     *
     * @param date The LocalDateTime representing the show date.
     * @return A new RequestShowDate instance.
     */
    public static RequestShowDate valueOf(final LocalDateTime date) {
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
        return new RequestShowDate(date.toInstant().atZone(date.getTimeZone().toZoneId()).toLocalDateTime());
    }

    /**
     * Creates a new RequestShowDate instance from the given date string.
     * The date string should be in a valid format parseable by LocalDateTime.
     *
     * @param dateString The date string to parse and use for creating the RequestShowDate instance.
     *                   This value must not be null or empty.
     * @return A new RequestShowDate instance based on the provided date string.
     * @throws IllegalArgumentException if the dateString is empty or cannot be parsed into a valid LocalDateTime.
     */
    public static RequestShowDate valueOf(final String dateString) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        try {
            return new RequestShowDate(LocalDateTime.parse(dateString));
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
            return new RequestShowDate(LocalDateTime.parse(dateString, formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for RequestShowDate: " + dateString +
                    " with formatter " + formatter, e);
        }
    }


    /**
     * Returns the underlying LocalDateTime value.
     * @return the show date
     */
    public LocalDateTime date() {
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