package eapli.shodrone.usermanagement.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SystemUserDetailsForRepresentativeTest {

    private final String validUsername = "repUser";
    private final String validPassword = "password123";
    private final String validFirstName = "John";
    private final String validLastName = "Doe";
    private final String validSystemEmail = "john.doe@shodrone.com";
    private final String validPhoneNumber = "+1234567890";
    private final String validCompanyEmail = "john.doe@company.com";

    @Test
    void ensureDtoCanBeCreatedWithValidData() {
        // Act
        SystemUserDetailsForRepresentative dto = new SystemUserDetailsForRepresentative(
                validUsername,
                validPassword,
                validFirstName,
                validLastName,
                validSystemEmail,
                validPhoneNumber,
                validCompanyEmail
        );

        // Assert
        assertNotNull(dto);
        assertEquals(validUsername, dto.username);
        assertEquals(validPassword, dto.password);
        assertEquals(validFirstName, dto.firstName);
        assertEquals(validLastName, dto.lastName);
        assertEquals(validSystemEmail, dto.systemEmail);
        assertEquals(validPhoneNumber, dto.representativePhoneNumber);
        assertEquals(validCompanyEmail, dto.representativeCompanyEmail);
    }

    @Test
    void ensureNullUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SystemUserDetailsForRepresentative(
                    null, validPassword, validFirstName, validLastName,
                    validSystemEmail, validPhoneNumber, validCompanyEmail
            );
        });
    }

    @Test
    void ensureNullPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SystemUserDetailsForRepresentative(
                    validUsername, null, validFirstName, validLastName,
                    validSystemEmail, validPhoneNumber, validCompanyEmail
            );
        });
    }

    @Test
    void ensureNullFirstNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SystemUserDetailsForRepresentative(
                    validUsername, validPassword, null, validLastName,
                    validSystemEmail, validPhoneNumber, validCompanyEmail
            );
        });
    }

    @Test
    void ensureNullLastNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SystemUserDetailsForRepresentative(
                    validUsername, validPassword, validFirstName, null,
                    validSystemEmail, validPhoneNumber, validCompanyEmail
            );
        });
    }

    @Test
    void ensureNullSystemEmailThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SystemUserDetailsForRepresentative(
                    validUsername, validPassword, validFirstName, validLastName,
                    null, validPhoneNumber, validCompanyEmail
            );
        });
    }

    @Test
    void ensureNullRepresentativePhoneNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SystemUserDetailsForRepresentative(
                    validUsername, validPassword, validFirstName, validLastName,
                    validSystemEmail, null, validCompanyEmail
            );
        });
    }

    @Test
    void ensureNullRepresentativeCompanyEmailThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SystemUserDetailsForRepresentative(
                    validUsername, validPassword, validFirstName, validLastName,
                    validSystemEmail, validPhoneNumber, null
            );
        });
    }
}