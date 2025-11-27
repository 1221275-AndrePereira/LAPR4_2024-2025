package eapli.shodrone.shodroneusermanagement.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private final String validStreetAddress = "123 Main St, Apt 4B";
    private final String validPostalCode = "1234-567";
    private final String validCity = "Springfield";

    @Test
    void ensureAddressCanBeCreatedWithValidParameters() {
        // Act
        Address address = new Address(validStreetAddress, validPostalCode, validCity);

        // Assert
        assertNotNull(address);
        assertEquals(validStreetAddress, address.streetAddress());
        assertEquals(validPostalCode, address.postalCode());
        assertEquals(validCity, address.city());
    }

    @Test
    void ensureValueOfCreatesValidAddress() {
        // Act
        Address address = Address.valueOf(validStreetAddress, validPostalCode, validCity);

        // Assert
        assertNotNull(address);
        assertEquals(validStreetAddress, address.streetAddress());
        assertEquals(validPostalCode, address.postalCode());
        assertEquals(validCity, address.city());
    }

    @Test
    void ensureToStringReturnsCorrectFormat() {
        // Arrange
        Address address = new Address(validStreetAddress, validPostalCode, validCity);
        String expectedString = validStreetAddress + ", " + validPostalCode + " " + validCity;

        // Act & Assert
        assertEquals(expectedString, address.toString());
    }

    @Test
    void ensureNullStreetAddressThrowsIllegalArgumentException() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(null, validPostalCode, validCity);
        });
        assertEquals("Street Address should neither be null nor empty", exception.getMessage());
    }

    @Test
    void ensureEmptyStreetAddressThrowsIllegalArgumentException() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address("", validPostalCode, validCity);
        });
        assertEquals("Street Address should neither be null nor empty", exception.getMessage());
    }

    @Test
    void ensureNullPostalCodeThrowsIllegalArgumentException() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, null, validCity);
        });
        assertEquals("Postal Code should neither be null nor empty", exception.getMessage());
    }

    @Test
    void ensureEmptyPostalCodeThrowsIllegalArgumentException() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, "", validCity);
        });
        assertEquals("Postal Code should neither be null nor empty", exception.getMessage());
    }

    @Test
    void ensureInvalidPostalCodeFormatThrowsIllegalArgumentException_TooManyDigitsBeforeHyphen() {
        String invalidPostalCode = "12345-678";
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, invalidPostalCode, validCity);
        });
        assertEquals("Invalid Postal Code: " + invalidPostalCode, exception.getMessage());
    }

    @Test
    void ensureInvalidPostalCodeFormatThrowsIllegalArgumentException_TooFewDigitsAfterHyphen() {
        String invalidPostalCode = "1234-56";
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, invalidPostalCode, validCity);
        });
        assertEquals("Invalid Postal Code: " + invalidPostalCode, exception.getMessage());
    }

    @Test
    void ensureInvalidPostalCodeFormatThrowsIllegalArgumentException_NoHyphen() {
        String invalidPostalCode = "1234567";
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, invalidPostalCode, validCity);
        });
        assertEquals("Invalid Postal Code: " + invalidPostalCode, exception.getMessage());
    }

    @Test
    void ensureInvalidPostalCodeFormatThrowsIllegalArgumentException_LettersPresent() {
        String invalidPostalCode = "ABCD-EFG";
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, invalidPostalCode, validCity);
        });
        assertEquals("Invalid Postal Code: " + invalidPostalCode, exception.getMessage());
    }

    @Test
    void ensureNullCityThrowsIllegalArgumentException() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, validPostalCode, null);
        });
        assertEquals("City should neither be null nor empty", exception.getMessage());
    }

    @Test
    void ensureEmptyCityThrowsIllegalArgumentException() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, validPostalCode, "");
        });
        assertEquals("City should neither be null nor empty", exception.getMessage());
    }


    @Test
    void ensureInvalidStreetAddressCharacterThrowsIllegalArgumentException() {
        String invalidStreet = "123 Main St $";
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(invalidStreet, validPostalCode, validCity);
        });
        assertEquals("Invalid Street Address: " + invalidStreet, exception.getMessage());
    }

    @Test
    void ensureInvalidCityCharacterThrowsIllegalArgumentException() {
        String invalidCityName = "New York$";
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(validStreetAddress, validPostalCode, invalidCityName);
        });
        assertEquals("Invalid City: " + invalidCityName, exception.getMessage());
    }


    @Test
    void ensureEqualsAndHashCodeAreCorrect() {
        // Arrange
        Address address1 = new Address(validStreetAddress, validPostalCode, validCity);
        Address address2 = new Address(validStreetAddress, validPostalCode, validCity);
        Address address3 = new Address("456 Oak Ave", "8901-234", "Otherville");

        // Assert
        assertEquals(address1, address2, "Two addresses with the same values should be equal.");
        assertEquals(address1.hashCode(), address2.hashCode(), "Hashcodes of two equal addresses should be the same.");
        assertNotEquals(address1, address3, "Addresses with different values should not be equal.");
        assertNotEquals(address1.hashCode(), address3.hashCode(), "Hashcodes of two different addresses should ideally be different (though not strictly required, good for HashMap performance).");
        assertNotEquals(address1, null, "Address should not be equal to null.");
        assertNotEquals(address1, new Object(), "Address should not be equal to an object of a different type.");
    }

    @Test
    void ensureOrmConstructorInitializesFieldsToEmptyStrings() {
        // Act
        Address address = new Address();

        // Assert
        assertEquals("", address.streetAddress(), "ORM constructor should initialize streetAddress to empty string.");
        assertEquals("", address.postalCode(), "ORM constructor should initialize postalCode to empty string.");
        assertEquals("", address.city(), "ORM constructor should initialize city to empty string.");
    }
}
