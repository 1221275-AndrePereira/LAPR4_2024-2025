package eapli.shodrone.usermanagement.dto; // Ou um pacote de DTOs partilhado

import eapli.framework.validations.Preconditions;

/**
 * DTO for system user details for a representative.
 * This class is used to transfer data between the system and the representative.
 */
public class SystemUserDetailsForRepresentative {
    public final String username;
    public final String password;
    public final String firstName;
    public final String lastName;
    public final String systemEmail;
    public final String representativePhoneNumber;
    public final String representativeCompanyEmail;

    /**
     * Constructor for SystemUserDetailsForRepresentative.
     *
     * @param username                   the username of the system user
     * @param password                   the password of the system user
     * @param firstName                  the first name of the system user
     * @param lastName                   the last name of the system user
     * @param systemEmail                the email of the system user
     * @param representativePhoneNumber   the phone number of the representative
     * @param representativeCompanyEmail  the company email of the representative
     */
    public SystemUserDetailsForRepresentative(String username, String password, String firstName, String lastName,
                                              String systemEmail, String representativePhoneNumber,
                                              String representativeCompanyEmail) {
        // Validate that none of the parameters are null
        Preconditions.noneNull(username, password, firstName, lastName, systemEmail, representativePhoneNumber, representativeCompanyEmail);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemEmail = systemEmail;
        this.representativePhoneNumber = representativePhoneNumber;
        this.representativeCompanyEmail = representativeCompanyEmail;
    }
}