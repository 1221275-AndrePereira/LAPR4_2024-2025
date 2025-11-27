package eapli.shodrone.shodroneusermanagement.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {

    @ParameterizedTest
    @ValueSource(strings = {"912345678", "922345678", "932345678", "962345678"})
    void ensureValidMobileNumbersAreAccepted(String validNumber) {
        PhoneNumber pn = new PhoneNumber(validNumber);
        assertEquals(validNumber, pn.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"+351912345678", "00351912345678", "+351 912345678", "00351 912345678"})
    void ensureValidMobileNumbersWithInternationalPrefixAreAccepted(String validNumber) {
        PhoneNumber pn = new PhoneNumber(validNumber);
        assertEquals(validNumber, pn.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"212345678", "222345678"})
    void ensureValidLandlineNumbersAreAccepted(String validNumber) {
        PhoneNumber pn = new PhoneNumber(validNumber);
        assertEquals(validNumber, pn.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"+351212345678", "00351212345678", "+351 212345678", "00351 212345678"})
    void ensureValidLandlineNumbersWithInternationalPrefixAreAccepted(String validNumber) {
        PhoneNumber pn = new PhoneNumber(validNumber);
        assertEquals(validNumber, pn.toString());
    }


    @Test
    void ensureNullPhoneNumberThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(null));
    }

    @Test
    void ensureEmptyPhoneNumberThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(""));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456789",
            "91234567",
            "21234567",
            "9123456789",
            "2123456789",
            "942345678",
            "abcde",
            "+35191234567",
            "+35112345678",
            "00351 9123456"
    })
    void ensureInvalidPhoneNumberFormatsThrowIllegalArgumentException(String invalidNumber) {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(invalidNumber));
    }

    @Test
    void testToString() {
        String numberStr = "911222333";
        PhoneNumber pn = new PhoneNumber(numberStr);
        assertEquals(numberStr, pn.toString());
    }

    @Test
    void testCompareTo() {
        PhoneNumber pn1 = new PhoneNumber("912345678");
        PhoneNumber pn2 = new PhoneNumber("912345679");
        PhoneNumber pn3 = new PhoneNumber("212345678");
        PhoneNumber pn1Again = new PhoneNumber("912345678");

        assertTrue(pn1.compareTo(pn2) < 0);
        assertTrue(pn2.compareTo(pn1) > 0);
        assertTrue(pn1.compareTo(pn3) > 0);
        assertEquals(0, pn1.compareTo(pn1Again));
    }

    @Test
    void testValueOf() {
        String numberStr = "961234567";
        PhoneNumber pn1 = PhoneNumber.valueOf(numberStr);
        PhoneNumber pn2 = new PhoneNumber(numberStr);
        assertEquals(pn1.toString(), pn2.toString());
    }

    @Test
    void testEquals() {
        PhoneNumber pn1 = new PhoneNumber("912345678");
        PhoneNumber pn2 = new PhoneNumber("912345678");
        PhoneNumber pn3 = new PhoneNumber("933333333");

        // Test equals
        assertEquals(0, pn1.compareTo(pn2) , "PhoneNumbers with the same value should be equal.");
        assertNotEquals(pn1, pn3, "PhoneNumbers with different values should not be equal.");
        assertNotEquals(null, pn1, "PhoneNumber should not be equal to null.");
        assertNotEquals(pn1, new Object(), "PhoneNumber should not be equal to an object of a different type.");
    }

    @Test
    void ensureConstructorWithValidNumberDoesNotThrowException() {
        assertDoesNotThrow(() -> new PhoneNumber("911234567"));
        assertDoesNotThrow(() -> new PhoneNumber("+351911234567"));
        assertDoesNotThrow(() -> new PhoneNumber("00351911234567"));
        assertDoesNotThrow(() -> new PhoneNumber("211234567"));
        assertDoesNotThrow(() -> new PhoneNumber("+351211234567"));
    }
}