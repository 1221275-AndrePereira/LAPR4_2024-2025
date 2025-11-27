package eapli.shodrone.showProposal.domain.proposalFields;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Calendar;

import java.io.Serial;

/**
 * Represents the show date for a request.
 */
@Embeddable
@EqualsAndHashCode
public final class ProposedShowDate implements ValueObject, Comparable<ProposedShowDate> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDateTime showDate;

    /**
     * Constructor to create a RequestShowDate instance.
     *
     * @param date The LocalDateTime representing the show date.
     */
    private ProposedShowDate(final LocalDateTime date) {
        Preconditions.nonNull(date, "Proposal date cannot be null");
        // Add any other specific validation if needed, e.g., date must be in the future
        // Preconditions.ensure(date.isAfter(LocalDateTime.now()), "Show date must be in the future");
        this.showDate = date;
    }

    public ProposedShowDate() {
        // for ORM
        // to handle incomplete Show Proposal
        this.showDate = null;
    }

    /**
     * Factory method to create a RequestShowDate instance from a LocalDateTime.
     *
     * @param date The LocalDateTime representing the show date.
     * @return A new RequestShowDate instance.
     */
    public static ProposedShowDate valueOf(final LocalDateTime date) {
        return new ProposedShowDate(date);
    }

    /**
     * Factory method to create a RequestShowDate instance from a Calendar.
     *
     * @param date The Calendar representing the show date.
     * @return A new RequestShowDate instance.
     */
    public static ProposedShowDate valueOf(final Calendar date) {
        Preconditions.nonNull(date, "Date cannot be null");
        return new ProposedShowDate(date.toInstant().atZone(date.getTimeZone().toZoneId()).toLocalDateTime());
    }

    /**
     * Factory method to create a RequestShowDate instance from a String.
     * Assumes "yyyy-MM-dd" format.
     *
     * @param dateString The date string (e.g., "2024-01-15").
     * @return A new RequestShowDate instance.
     * @throws DateTimeParseException if the string cannot be parsed.
     */
    public static ProposedShowDate valueOf(final String dateString) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        try {
            return new ProposedShowDate(LocalDateTime.parse(dateString));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for Proposal Date: " + dateString, e);
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
    public static ProposedShowDate valueOf(final String dateString, DateTimeFormatter formatter) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        Preconditions.nonNull(formatter, "Formatter cannot be null");
        try {
            return new ProposedShowDate(LocalDateTime.parse(dateString, formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for Proposal Date: " + dateString +
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
    public int compareTo(final ProposedShowDate other) {
        Preconditions.nonNull(other, "Other date cannot be null");
        return this.showDate.compareTo(other.showDate);
    }
}