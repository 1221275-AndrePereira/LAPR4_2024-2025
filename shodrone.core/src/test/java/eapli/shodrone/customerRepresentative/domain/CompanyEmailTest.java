package eapli.shodrone.customerRepresentative.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompanyEmailTest {

    @Test
    void ensureCompanyEmailCanBeCreatedWithValidInput() {
        String validEmail = "test.user@example.com";
        CompanyEmail companyEmail = CompanyEmail.valueOf(validEmail);
        assertNotNull(companyEmail);
        assertEquals(validEmail, companyEmail.toString());
    }

    @Test
    void ensureCompanyEmailWithSubdomainIsValid() {
        String validEmail = "info@sub.domain.co.uk";
        CompanyEmail companyEmail = CompanyEmail.valueOf(validEmail);
        assertNotNull(companyEmail);
        assertEquals(validEmail, companyEmail.toString());
    }

    @Test
    void ensureCompanyEmailWithNumbersInDomainIsValid() {
        String validEmail = "user123@domain123.com";
        CompanyEmail companyEmail = CompanyEmail.valueOf(validEmail);
        assertNotNull(companyEmail);
        assertEquals(validEmail, companyEmail.toString());
    }

    @Test
    void ensureCompanyEmailWithPlusSignInLocalPartIsValid() {
        String validEmail = "user+mailbox@example.com";
        CompanyEmail companyEmail = CompanyEmail.valueOf(validEmail);
        assertNotNull(companyEmail);
        assertEquals(validEmail, companyEmail.toString());
    }

    @Test
    void ensureCompanyEmailWithHyphenInDomainIsValid() {
        String validEmail = "contact@my-company.org";
        CompanyEmail companyEmail = CompanyEmail.valueOf(validEmail);
        assertNotNull(companyEmail);
        assertEquals(validEmail, companyEmail.toString());
    }

    @Test
    void ensureToStringReturnsCorrectEmail() {
        String email = "another.test@example.net";
        CompanyEmail companyEmail = CompanyEmail.valueOf(email);
        assertEquals(email, companyEmail.toString());
    }

    @Test
    void ensureCompareToBehavesCorrectly() {
        CompanyEmail email1 = CompanyEmail.valueOf("a@example.com");
        CompanyEmail email2 = CompanyEmail.valueOf("z@example.com");
        CompanyEmail email3 = CompanyEmail.valueOf("a@example.com");

        assertTrue(email1.compareTo(email2) < 0, "email1 should be less than email2");
        assertTrue(email2.compareTo(email1) > 0, "email2 should be greater than email1");
        assertEquals(0, email1.compareTo(email3), "email1 should be equal to email3");
    }

    @Test
    void ensureEqualsAreCorrect() {
        CompanyEmail email1 = CompanyEmail.valueOf("test@example.com");
        CompanyEmail email2 = CompanyEmail.valueOf("test@example.com");
        CompanyEmail email3 = CompanyEmail.valueOf("another@example.com");

        assertEquals(email1.toString(), email2.toString(), "Two CompanyEmail objects with the same value should be equal.");
        assertNotEquals(email1.toString(), email3.toString(), "CompanyEmail objects with different values should not be equal.");
}

    @Test
    void ensureNullEmailThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(null));
        assertEquals("Company Email cannot be null or empty", exception.getMessage());
    }

    @Test
    void ensureEmptyEmailThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(""));
        assertEquals("Company Email cannot be null or empty", exception.getMessage());
    }

    @Test
    void ensureEmailWithoutAtSymbolThrowsIllegalArgumentException() {
        String invalidEmail = "testexample.com";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(invalidEmail));
        assertEquals("Company Email must be a valid format", exception.getMessage());
    }

    @Test
    void ensureEmailWithoutDomainThrowsIllegalArgumentException() {
        String invalidEmail = "test@";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(invalidEmail));
        assertEquals("Company Email must be a valid format", exception.getMessage());
    }

    @Test
    void ensureEmailWithoutLocalPartThrowsIllegalArgumentException() {
        String invalidEmail = "@example.com";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(invalidEmail));
        assertEquals("Company Email must be a valid format", exception.getMessage());
    }

    @Test
    void ensureEmailWithInvalidCharactersInLocalPartThrowsIllegalArgumentException() {
        String invalidEmail = "test user@example.com"; // Space is invalid
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(invalidEmail));
        assertEquals("Company Email must be a valid format", exception.getMessage());
    }

    @Test
    void ensureEmailWithInvalidCharactersInDomainThrowsIllegalArgumentException() {
        String invalidEmail = "test@exam_ple.com";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(invalidEmail));
        assertEquals("Company Email must be a valid format", exception.getMessage());
    }

    @Test
    void ensureEmailWithoutTopLevelDomainThrowsIllegalArgumentException() {
        String invalidEmail = "test@example";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(invalidEmail));
        assertEquals("Company Email must be a valid format", exception.getMessage());
    }

    @Test
    void ensureEmailWithShortTopLevelDomainThrowsIllegalArgumentException() {
        String invalidEmail = "test@example.c"; // TLD too short
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(invalidEmail));
        assertEquals("Company Email must be a valid format", exception.getMessage());
    }

    @Test
    void ensureEmailEndingWithDotInDomainThrowsIllegalArgumentException() {
        String invalidEmail = "test@example.com.";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CompanyEmail.valueOf(invalidEmail));
        assertEquals("Company Email must be a valid format", exception.getMessage());
    }

    // Test ORM constructor
    @Test
    void ensureOrmConstructorExistsAndCanBeInvoked() {
        try {
            CompanyEmail email = CompanyEmail.class.getDeclaredConstructor().newInstance();
            assertNotNull(email);
            // The ORM constructor doesn't initialize companyEmail, so it will be null.
            assertNull(email.toString(), "ORM constructor should leave companyEmail as null if not initialized.");
        } catch (Exception e) {
            fail("Failed to instantiate CompanyEmail using no-arg constructor: " + e.getMessage());
        }
    }
}
