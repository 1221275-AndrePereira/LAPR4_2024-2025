package eapli.shodrone.customerRepresentative.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.strings.util.StringPredicates;

/**
 * Represents a company email address.
 * This class is immutable and implements the ValueObject interface.
 * It validates the email format upon creation.
 *
 */
public class CompanyEmail implements ValueObject, Comparable<CompanyEmail>
{
    private static final long serialVersionUID = 1L;

    private String companyEmail;

    /**
     * Constructor for creating a CompanyEmail instance.
     *
     * @param companyEmail the email address to be validated and set
     * @throws IllegalArgumentException if the email is null, empty, or not in a valid format
     */
    protected CompanyEmail(final String companyEmail) {
        if (StringPredicates.isNullOrEmpty(companyEmail)) {
            throw new IllegalArgumentException("Company Email cannot be null or empty");
        }
        if (!companyEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Company Email must be a valid format");
        }
        this.companyEmail = companyEmail;
    }

    protected CompanyEmail() {
        // for ORM
    }

    /**
     * Returns the email address as a string.
     *
     * @return the email address
     */
    @Override
    public String toString() {
        return this.companyEmail;
    }

    /**
     * Checks if this CompanyEmail is equal to another object.
     *
     * @param cE the object to compare with
     * @return true if the other object is a CompanyEmail with the same email address, false otherwise
     */
    @Override
    public int compareTo(final CompanyEmail cE) {
        return this.companyEmail.compareTo(cE.companyEmail);
    }

    /**
     * Turns a string into a CompanyEmail object.
     *
     * @param companyEmail the email address to be converted
     * @return a new CompanyEmail object
     */
    public static CompanyEmail valueOf(final String companyEmail) {
        return new CompanyEmail(companyEmail);
    }
}
