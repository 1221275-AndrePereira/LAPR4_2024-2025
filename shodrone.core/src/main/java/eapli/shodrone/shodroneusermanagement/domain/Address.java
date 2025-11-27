package eapli.shodrone.shodroneusermanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.Value;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Represents an address.
 * <p>
 * The address is composed of a street address, postal code, and city.
 * <p>
 * The address is validated using regular expressions to ensure that it meets the required format.
 * <p>
 * The class is immutable and implements the ValueObject interface.
 */
@Embeddable
@Value
@Accessors(fluent = true)
public class Address implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;

    private String streetAddress;
    private String postalCode;
    private String city;

    /**
     * Constructs an Address object with the specified street address, postal code, and city.
     *
     * @param streetAddress the street address
     * @param postalCode    the postal code
     * @param city          the city
     * @throws IllegalArgumentException if any of the parameters are null or empty, or if they do not match the required format
     */
    public Address(final String streetAddress,final String postalCode,final String city) {
        // Regular expressions for validation
        Pattern VALID_ADDRESS = Pattern.compile("^[\\w\\s.,-]+$", Pattern.CASE_INSENSITIVE);
        Pattern VALID_POSTAL_CODE = Pattern.compile("^[0-9]{4}-[0-9]{3}$", Pattern.CASE_INSENSITIVE);
        Pattern VALID_CITY = Pattern.compile("^[\\w\\s.,-]+$", Pattern.CASE_INSENSITIVE);

        // Validate the parameters
        Preconditions.nonEmpty(streetAddress, "Street Address should neither be null nor empty");
        Preconditions.nonEmpty(postalCode, "Postal Code should neither be null nor empty");
        Preconditions.nonEmpty(city, "City should neither be null nor empty");

        // Validate the format using regular expressions
        Preconditions.matches(VALID_ADDRESS, streetAddress, "Invalid Street Address: " + streetAddress);
        Preconditions.matches(VALID_POSTAL_CODE, postalCode, "Invalid Postal Code: " + postalCode);
        Preconditions.matches(VALID_CITY, city, "Invalid City: " + city);

        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
    }

    protected Address() {
        // for ORM
        streetAddress = postalCode = city = "";
    }

    /**
     * Creates a new Address object with the specified street address, postal code, and city.
     *
     * @param streetAddress the street address
     * @param postalCode    the postal code
     * @param city          the city
     * @return a new Address object
     */
    public static Address valueOf(final String streetAddress,final String postalCode,final String city) {
        return new Address(streetAddress, postalCode, city);
    }

    /**
     * Returns the street address.
     *
     * @return the street address
     */
    @Override
    public String toString() {
        return streetAddress + ", " + postalCode + " " + city;
    }
}
