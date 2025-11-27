package eapli.shodrone.shodroneusermanagement.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VATNumberTest {

    @Test
    void ensureVATNumberCanBeCreatedWithValidInput_StartsWith1() {
        String validVAT = "123456789";
        VATNumber vatNumber = VATNumber.valueOf(validVAT);
        assertNotNull(vatNumber);
        assertEquals(validVAT, vatNumber.toString());
    }

    @Test
    void ensureVATNumberCanBeCreatedWithValidInput_StartsWith2() {
        String validVAT = "234567890";
        VATNumber vatNumber = VATNumber.valueOf(validVAT);
        assertNotNull(vatNumber);
        assertEquals(validVAT, vatNumber.toString());
    }

    @Test
    void ensureVATNumberCanBeCreatedWithValidInput_StartsWith3() {
        String validVAT = "345678901";
        VATNumber vatNumber = VATNumber.valueOf(validVAT);
        assertNotNull(vatNumber);
        assertEquals(validVAT, vatNumber.toString());
    }

    @Test
    void ensureVATNumberCanBeCreatedWithValidInput_StartsWith5() {
        String validVAT = "567890123";
        VATNumber vatNumber = VATNumber.valueOf(validVAT);
        assertNotNull(vatNumber);
        assertEquals(validVAT, vatNumber.toString());
    }

    @Test
    void ensureVATNumberCanBeCreatedWithValidInput_StartsWith6() {
        String validVAT = "678901234";
        VATNumber vatNumber = VATNumber.valueOf(validVAT);
        assertNotNull(vatNumber);
        assertEquals(validVAT, vatNumber.toString());
    }

    @Test
    void ensureVATNumberCanBeCreatedWithValidInput_StartsWith8() {
        String validVAT = "890123456";
        VATNumber vatNumber = VATNumber.valueOf(validVAT);
        assertNotNull(vatNumber);
        assertEquals(validVAT, vatNumber.toString());
    }

    @Test
    void ensureVATNumberCanBeCreatedWithValidInput_StartsWith9() {
        String validVAT = "901234567";
        VATNumber vatNumber = VATNumber.valueOf(validVAT);
        assertNotNull(vatNumber);
        assertEquals(validVAT, vatNumber.toString());
    }


    @Test
    void ensureToStringReturnsCorrectVATNumber() {
        String vat = "123456789";
        VATNumber vatNumber = VATNumber.valueOf(vat);
        assertEquals(vat, vatNumber.toString());
    }

    @Test
    void ensureCompareToBehavesCorrectly() {
        VATNumber vat1 = VATNumber.valueOf("123456789");
        VATNumber vat2 = VATNumber.valueOf("987654321");
        VATNumber vat3 = VATNumber.valueOf("123456789");

        assertTrue(vat1.compareTo(vat2) < 0, "vat1 should be less than vat2");
        assertTrue(vat2.compareTo(vat1) > 0, "vat2 should be greater than vat1");
        assertEquals(0, vat1.compareTo(vat3), "vat1 should be equal to vat3");
    }

    @Test
    void ensureNullVATNumberThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(null));
        assertEquals("VAT number cannot be null or empty", exception.getMessage());
    }

    @Test
    void ensureEmptyVATNumberThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(""));
        assertEquals("VAT number cannot be null or empty", exception.getMessage());
    }

    @Test
    void ensureVATNumberWithInvalidLengthThrowsIllegalArgumentException_TooShort() {
        String invalidVAT = "12345678"; // 8 digits
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(invalidVAT));
        assertEquals("VAT number must be a valid format", exception.getMessage());
    }

    @Test
    void ensureVATNumberWithInvalidLengthThrowsIllegalArgumentException_TooLong() {
        String invalidVAT = "1234567890"; // 10 digits
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(invalidVAT));
        assertEquals("VAT number must be a valid format", exception.getMessage());
    }

    @Test
    void ensureVATNumberWithInvalidStartingDigitThrowsIllegalArgumentException_StartsWith0() {
        String invalidVAT = "012345678";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(invalidVAT));
        assertEquals("VAT number must be a valid format", exception.getMessage());
    }

    @Test
    void ensureVATNumberWithInvalidStartingDigitThrowsIllegalArgumentException_StartsWith4() {
        String invalidVAT = "412345678";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(invalidVAT));
        assertEquals("VAT number must be a valid format", exception.getMessage());
    }

    @Test
    void ensureVATNumberWithInvalidStartingDigitThrowsIllegalArgumentException_StartsWith7() {
        String invalidVAT = "712345678";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(invalidVAT));
        assertEquals("VAT number must be a valid format", exception.getMessage());
    }

    @Test
    void ensureVATNumberWithNonDigitCharactersThrowsIllegalArgumentException() {
        String invalidVAT = "12345678A";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(invalidVAT));
        assertEquals("VAT number must be a valid format", exception.getMessage());
    }

    @Test
    void ensureVATNumberWithSpecialCharactersThrowsIllegalArgumentException() {
        String invalidVAT = "123-45678";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VATNumber.valueOf(invalidVAT));
        assertEquals("VAT number must be a valid format", exception.getMessage());
    }

    @Test
    void ensureOrmConstructorExistsAndCanBeInvoked() {
        try {
            VATNumber vat = VATNumber.class.getDeclaredConstructor().newInstance();
            assertNotNull(vat);
            assertNull(vat.toString(), "ORM constructor should leave vatNumber as null if not initialized.");
        } catch (Exception e) {
            fail("Failed to instantiate VATNumber using no-arg constructor: " + e.getMessage());
        }
    }
}
