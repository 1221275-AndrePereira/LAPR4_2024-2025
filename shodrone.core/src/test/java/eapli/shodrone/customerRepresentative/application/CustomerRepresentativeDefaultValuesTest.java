package eapli.shodrone.customerRepresentative.application;

import eapli.shodrone.usermanagement.dto.SystemUserDetailsForRepresentative;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepresentativeDefaultValuesTest {

    @Test
    void buildRepresentativeDetails_withValidInputs_returnsCorrectlyPopulatedDto() {
        // Arrange
        String customerUsername = "custUser123";
        String customerFirstName = "Customer";
        String customerLastName = "One";

        // Expected derived values
        String expectedRepUsername = "rep_custUser123";
        String expectedRepPassword = "123Qwe#"; // From REP_DEFAULT_PASSWORD
        String expectedRepFirstName = "Representative of"; // From REP_FIRST_NAME_DEFAULT
        String expectedRepEffectiveLastName = "Customer One";
        String expectedRepSystemEmail = "Customer_rep@shodrone.com"; // customerFirstName + REP_SYSTEM_EMAIL_DOMAIN
        String expectedRepPhoneNumber = "+351912345678"; // From REP_PHONE_NUMBER_DEFAULT
        String expectedRepCompanyEmail = "Customerrep@mycompany.com"; // customerFirstName + REP_COMPANY_EMAIL_DOMAIN

        // Act
        SystemUserDetailsForRepresentative details = CustomerRepresentativeDefaultValues.buildRepresentativeDetails(
                customerUsername, customerFirstName, customerLastName
        );

        // Assert
        assertNotNull(details, "The returned DTO should not be null.");
        assertEquals(expectedRepUsername, details.username, "Representative username should be correctly generated.");
        assertEquals(expectedRepPassword, details.password, "Representative password should match the default.");
        assertEquals(expectedRepFirstName, details.firstName, "Representative first name should match the default prefix.");
        assertEquals(expectedRepEffectiveLastName, details.lastName, "Representative last name should be correctly generated.");
        assertEquals(expectedRepSystemEmail, details.systemEmail, "Representative system email should be correctly generated.");
        assertEquals(expectedRepPhoneNumber, details.representativePhoneNumber, "Representative phone number should match the default.");
        assertEquals(expectedRepCompanyEmail, details.representativeCompanyEmail, "Representative company email should be correctly generated.");
    }

    @Test
    void buildRepresentativeDetails_withDifferentValidInputs_returnsCorrectlyPopulatedDto() {
        // Arrange
        String customerUsername = "anotherCust";
        String customerFirstName = "Jane";
        String customerLastName = "Smith";

        // Expected derived values
        String expectedRepUsername = "rep_anotherCust";
        String expectedRepPassword = "123Qwe#";
        String expectedRepFirstName = "Representative of";
        String expectedRepEffectiveLastName = "Jane Smith";
        String expectedRepSystemEmail = "Jane_rep@shodrone.com";
        String expectedRepPhoneNumber = "+351912345678";
        String expectedRepCompanyEmail = "Janerep@mycompany.com";

        // Act
        SystemUserDetailsForRepresentative details = CustomerRepresentativeDefaultValues.buildRepresentativeDetails(
                customerUsername, customerFirstName, customerLastName
        );

        // Assert
        assertNotNull(details);
        assertEquals(expectedRepUsername, details.username);
        assertEquals(expectedRepPassword, details.password);
        assertEquals(expectedRepFirstName, details.firstName);
        assertEquals(expectedRepEffectiveLastName, details.lastName);
        assertEquals(expectedRepSystemEmail, details.systemEmail);
        assertEquals(expectedRepPhoneNumber, details.representativePhoneNumber);
        assertEquals(expectedRepCompanyEmail, details.representativeCompanyEmail);
    }

    @Test
    void buildRepresentativeDetails_withEmptyCustomerNames_generatesAsExpected() {
        String customerUsername = "emptyNameCust";
        String customerFirstName = ""; // Empty
        String customerLastName = "";  // Empty

        // Expected derived values
        String expectedRepUsername = "rep_emptyNameCust";
        String expectedRepPassword = "123Qwe#";
        String expectedRepFirstName = "Representative of";
        String expectedRepEffectiveLastName = " ";
        String expectedRepSystemEmail = "_rep@shodrone.com"; // "" + "_rep@shodrone.com"
        String expectedRepPhoneNumber = "+351912345678";
        String expectedRepCompanyEmail = "rep@mycompany.com"; // "" + "rep@mycompany.com"

        // Act
        SystemUserDetailsForRepresentative details = CustomerRepresentativeDefaultValues.buildRepresentativeDetails(
                customerUsername, customerFirstName, customerLastName
        );

        // Assert
        assertNotNull(details);
        assertEquals(expectedRepUsername, details.username);
        assertEquals(expectedRepPassword, details.password);
        assertEquals(expectedRepFirstName, details.firstName);
        assertEquals(expectedRepEffectiveLastName, details.lastName);
        assertEquals(expectedRepSystemEmail, details.systemEmail);
        assertEquals(expectedRepPhoneNumber, details.representativePhoneNumber);
        assertEquals(expectedRepCompanyEmail, details.representativeCompanyEmail);
    }

    @Test
    void buildRepresentativeDetails_inputStringsAreUsedCorrectlyInConcatenation() {
        String custUser = "uName";
        String custFirst = "First";
        String custLast = "Last";

        SystemUserDetailsForRepresentative details = CustomerRepresentativeDefaultValues.buildRepresentativeDetails(custUser, custFirst, custLast);

        assertTrue(details.username.endsWith(custUser), "Username should end with customer's username.");
        assertTrue(details.lastName.contains(custFirst), "Last name should contain customer's first name.");
        assertTrue(details.lastName.contains(custLast), "Last name should contain customer's last name.");
        assertTrue(details.systemEmail.startsWith(custFirst), "System email should start with customer's first name.");
        assertTrue(details.representativeCompanyEmail.startsWith(custFirst), "Company email should start with customer's first name.");
    }
}
