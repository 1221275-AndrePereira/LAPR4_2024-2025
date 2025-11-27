package eapli.shodrone.showrequest.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;

/**
 * Represents the place (location) for a request/show.
 * This could be a simple string or a more complex object (e.g., with coordinates).
 * For this example, a simple non-empty string is used.
 */
@Embeddable
@EqualsAndHashCode
public final class RequestPlace implements ValueObject, Comparable<RequestPlace> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final Float longitude;

    @Getter
    private final Float latitude;


    /**
     * Constructor to create a RequestPlace instance.
     *
     * @param latitude
     * @param longitude
     */
    private RequestPlace(final Float latitude, final Float longitude) {
        Preconditions.ensure(
                -90.0f <= latitude && latitude <= 90.0f,
                "Latitude must be between -90 and 90 degrees."
        );
        Preconditions.ensure(
                -180.0f <= longitude && longitude <= 180.0f,
                "Longitude must be between -180 and 180 degrees."
        );

        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected RequestPlace() {
        // for ORM
        this.latitude = null;
        this.longitude = null;
    }

    /**
     * Factory method to create a place instance.
     *
     * @param latitude The latitude of the place.
     * @param longitude The longitude of the place.
     */
    public static RequestPlace valueOf(final Float latitude, final Float longitude) {
        return new RequestPlace(latitude, longitude);
    }

    /**
     * Returns the place description string.
     *
     * @return the place description
     */
    @Override
    public String toString() {
        return "RequestPlace{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    /**
     * Compares this RequestPlace with another based on the place description.
     *
     * @param other The other RequestPlace to compare with.
     * @return A negative integer, zero, or a positive integer as this place
     *         description is less than, equal to, or greater than the specified
     *         place description.
     */
    @Override
    public int compareTo(final RequestPlace other) {
        if (other == null) {
            return 1; // this instance is greater than null
        }
        // Compare based on latitude and then longitude
        int latComparison = this.latitude.compareTo(other.latitude);
        if (latComparison != 0) {
            return latComparison;
        }
        return this.longitude.compareTo(other.longitude);
    }
}