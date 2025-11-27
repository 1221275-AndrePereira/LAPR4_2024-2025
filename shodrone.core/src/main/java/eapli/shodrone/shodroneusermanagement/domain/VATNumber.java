package eapli.shodrone.shodroneusermanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.strings.util.StringPredicates;

import java.io.Serial;

/**
 * Represents a VAT number.
 *
 * This class is a value object that encapsulates the VAT number and provides
 * validation for its format.
 */
public class VATNumber implements ValueObject, Comparable<VATNumber> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String vatNumber;

    /**
     * Constructs a VATNumber with the specified VAT number.
     *
     * @param vatNumber the VAT number to be set
     * @throws IllegalArgumentException if the VAT number is null, empty, or invalid
     */
    protected VATNumber(final String vatNumber) {
        if (StringPredicates.isNullOrEmpty(vatNumber)) {
            throw new IllegalArgumentException("VAT number cannot be null or empty");
        }
        if (!vatNumber.matches("^(1|2|3|5|6|8|9)\\d{8}$")) {
            throw new IllegalArgumentException("VAT number must be a valid format");
        }
        this.vatNumber = vatNumber;
    }

    protected VATNumber() {
        // for ORM
    }

    /**
     * Returns the VAT number as a string.
     *
     * @return the VAT number
     */
    @Override
    public String toString() {
        return this.vatNumber;
    }

    /**
     * Checks if this VAT number is equal to another object.
     *
     * @param vN the object to compare with
     * @return true if the object is a VATNumber and has the same value, false otherwise
     */
    @Override
    public int compareTo(final VATNumber vN) {
        return this.vatNumber.compareTo(vN.vatNumber);
    }

    /**
     * It turns String into a VATNumber.
     *
     * @param vatNumber the string to be converted
     * @return a VATNumber object
     */
    public static VATNumber valueOf(final String vatNumber) {
        return new VATNumber(vatNumber);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VATNumber that)) {
            return false;
        }
        return this.vatNumber.equals(that.vatNumber);
    }

    @Override
    public int hashCode() {
        return vatNumber != null ? vatNumber.hashCode() : 0;
    }
}
