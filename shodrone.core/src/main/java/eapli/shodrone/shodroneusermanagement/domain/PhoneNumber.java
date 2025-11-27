package eapli.shodrone.shodroneusermanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.strings.util.StringPredicates;

/**
 * Represents a phone number.
 * <p>
 * A phone number is a string that must match the following regex:
 * <p>
 * ^(?:(?:\\+351|00351)[\\s-]?)?(2\\d{8}|9[1236]\\d{7})$
 * <p>
 * This regex allows for both local and international formats.
 */
public class PhoneNumber implements ValueObject, Comparable<PhoneNumber> {
    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    /**
     * Constructs a PhoneNumber object.
     *
     * @param phoneNumber the phone number string
     * @throws IllegalArgumentException if the phone number is null, empty, or does not match the regex
     */
    public PhoneNumber(final String phoneNumber) {
        if (StringPredicates.isNullOrEmpty(phoneNumber)) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        if (!phoneNumber.matches("^(?:(?:\\+351|00351)[\\s-]?)?(2\\d{8}|9[1236]\\d{7})$")) {
            throw new IllegalArgumentException("Phone number must be a valid format");
        }
        this.phoneNumber = phoneNumber;
    }

    protected PhoneNumber() {
        // for ORM
    }

    /**
     * Returns the phone number string.
     *
     * @return the phone number string
     */
    @Override
    public String toString() {
        return this.phoneNumber;
    }

    /**
     * Returns the phone number string.
     *
     * @return the phone number string
     */
    @Override
    public int compareTo(final PhoneNumber pN) {
        return this.phoneNumber.compareTo(pN.phoneNumber);
    }

    /**
     * Returns the hash code of the phone number.
     *
     * @param phoneNumber the phone number string
     * @return the hash code of the phone number
     */
    public static PhoneNumber valueOf(final String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }

}
