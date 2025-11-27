package eapli.shodrone.customerRepresentative.application;

import eapli.shodrone.usermanagement.dto.SystemUserDetailsForRepresentative;

/**
 * Default values for the customer representative.
 *
 * This class is used to generate default values for the customer representative
 * when creating a new customer representative.
 */
public final class CustomerRepresentativeDefaultValues {


    private static final String REP_USERNAME_DEFAULT = "rep_";
    private static final String REP_DEFAULT_PASSWORD = "123Qwe#";
    private static final String REP_FIRST_NAME_DEFAULT = "Representative of";
    private static final String REP_SYSTEM_EMAIL_DOMAIN = "_rep@shodrone.com";
    private static final String REP_PHONE_NUMBER_DEFAULT = "+351912345678";
    private static final String REP_COMPANY_EMAIL_DOMAIN = "rep@mycompany.com";

    private CustomerRepresentativeDefaultValues() {
        // ORM
    }

    /**
     * Generates a default SystemUserDetailsForRepresentative object with the given customer details.
     *
     * @param customerUsername  The username of the customer.
     * @param customerFirstName The first name of the customer.
     * @param customerLastName  The last name of the customer.
     * @return A SystemUserDetailsForRepresentative object with default values.
     */
    public static SystemUserDetailsForRepresentative buildRepresentativeDetails(String customerUsername, String customerFirstName, String customerLastName) {

        // Generate a default username for the representative
        String repUsername = REP_USERNAME_DEFAULT + customerUsername;

        // Generate a default first name and last name for the representative
        String repEffectiveFirstName = REP_FIRST_NAME_DEFAULT;
        String repEffectiveLastName = customerFirstName + " " + customerLastName;

        // Generate a default email for the representative
        String repSystemEmail = customerFirstName + REP_SYSTEM_EMAIL_DOMAIN;
        String repCompanyEmail = customerFirstName + REP_COMPANY_EMAIL_DOMAIN;

        // Create and return a new SystemUserDetailsForRepresentative object with the default values
        return new SystemUserDetailsForRepresentative(
                repUsername,
                REP_DEFAULT_PASSWORD,
                repEffectiveFirstName,
                repEffectiveLastName,
                repSystemEmail,
                REP_PHONE_NUMBER_DEFAULT,
                repCompanyEmail
        );
    }
}