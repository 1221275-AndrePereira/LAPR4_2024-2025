package eapli.shodrone.showProposal.domain.proposalFields;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DownLoadCodeTest {

    @Test
    void generateNewCode_startsWithPAndHasCorrectLengthForStandardId() {
        long id = 1L; // Standard ID, results in 2 characters for ID part
        DownLoadCode downLoadCode = DownLoadCode.generateNewCode(id);
        String codeValue = downLoadCode.value();

        assertNotNull(codeValue, "Generated code value should not be null.");
        assertTrue(codeValue.startsWith("P"), "Code should start with 'P'.");
        // Expected length: P (1) + random (7) + ID (2 for "01") = 10
        assertEquals(10, codeValue.length(), "Code length should be 10 for a standard two-digit formatted ID.");
    }

    @ParameterizedTest
    @CsvSource({
            "0, 00, 10", // ID, Expected Suffix, Expected Length
            "5, 05, 10",
            "12, 12, 10",
            "99, 99, 10"
    })
    void generateNewCode_idIsCorrectlyFormattedAndLengthIsCorrect(long id, String expectedIdSuffix, int expectedLength) {
        DownLoadCode downLoadCode = DownLoadCode.generateNewCode(id);
        String codeValue = downLoadCode.value();

        assertNotNull(codeValue, "Generated code value should not be null.");
        assertTrue(codeValue.startsWith("P"), "Code should start with 'P'.");
        assertEquals(expectedLength, codeValue.length(), "Code length mismatch for ID " + id);
        assertTrue(codeValue.endsWith(expectedIdSuffix),
                "Code for ID " + id + " should end with '" + expectedIdSuffix + "', but was: '" + codeValue.substring(codeValue.length() - expectedIdSuffix.length()) + "'.");
    }

    @ParameterizedTest
    @CsvSource({
            "100, 100, 11",  // ID > 99
            "1234, 1234, 12" // Larger ID
    })
    void generateNewCode_idGreaterThan99_extendsLengthCorrectly(long id, String expectedIdSuffix, int expectedLength) {
        DownLoadCode downLoadCode = DownLoadCode.generateNewCode(id);
        String codeValue = downLoadCode.value();

        assertNotNull(codeValue, "Generated code value should not be null.");
        assertTrue(codeValue.startsWith("P"), "Code should start with 'P'.");
        assertEquals(expectedLength, codeValue.length(), "Code length mismatch for ID " + id);
        assertTrue(codeValue.endsWith(expectedIdSuffix),
                "Code for ID " + id + " should end with '" + expectedIdSuffix + "'.");
    }

    @ParameterizedTest
    @CsvSource({
            "-1, -1, 10",   // Negative ID, resulting in 2 char suffix "-1"
            "-9, -9, 10",   // Negative ID, resulting in 2 char suffix "-9"
            "-10, -10, 11", // Negative ID, resulting in 3 char suffix "-10"
            "-123, -123, 12" // Negative ID, resulting in 4 char suffix "-123"
    })
    void generateNewCode_negativeId_isHandledByStringFormat(long id, String expectedIdSuffix, int expectedLength) {
        DownLoadCode downLoadCode = DownLoadCode.generateNewCode(id);
        String codeValue = downLoadCode.value();

        assertNotNull(codeValue, "Generated code value should not be null.");
        assertTrue(codeValue.startsWith("P"), "Code should start with 'P'.");
        assertEquals(expectedLength, codeValue.length(), "Code length mismatch for ID " + id);
        assertTrue(codeValue.endsWith(expectedIdSuffix),
                "Code for ID " + id + " should end with '" + expectedIdSuffix + "'.");
    }

    @Test
    void generateNewCode_isRandomForSameId() {
        long id = 25L;
        DownLoadCode code1 = DownLoadCode.generateNewCode(id);
        DownLoadCode code2 = DownLoadCode.generateNewCode(id);

        assertNotNull(code1.value());
        assertNotNull(code2.value());

        // Check that the full codes are different (highly probable due to 7 random chars)
        assertNotEquals(code1.value(), code2.value(), "Generated codes for the same ID should be different due to randomness.");

        // Check that prefix and suffix (ID part) are the same
        assertEquals("P", code1.value().substring(0, 1), "Prefix should be 'P'.");
        assertEquals("P", code2.value().substring(0, 1), "Prefix should be 'P'.");

        String expectedIdSuffix = String.format("%02d", id);
        assertEquals(expectedIdSuffix, code1.value().substring(8), "Suffix (ID part) should be the same.");
        assertEquals(expectedIdSuffix, code2.value().substring(8), "Suffix (ID part) should be the same.");

        // Check that the random part (middle 7 characters) is different
        assertNotEquals(code1.value().substring(1, 8), code2.value().substring(1, 8), "Random part (middle 7 chars) should be different.");
    }

    @Test
    void generateNewCode_isUniqueForDifferentIds() {
        DownLoadCode code1 = DownLoadCode.generateNewCode(10L);
        DownLoadCode code2 = DownLoadCode.generateNewCode(11L);

        assertNotNull(code1.value());
        assertNotNull(code2.value());
        assertNotEquals(code1.value(), code2.value(), "Generated codes for different IDs should be different.");
    }

    @Test
    void value_returnsTheGeneratedCode() {
        DownLoadCode downLoadCode = DownLoadCode.generateNewCode(7L);
        String generatedCode = downLoadCode.value(); // Get the code once

        // Assert that subsequent calls to value() return the same string instance or at least an equal string
        assertEquals(generatedCode, downLoadCode.value(), "value() should consistently return the generated code string.");
    }

    @Test
    void compareTo_correctlyComparesCodesLexicographically() {
        // Since the constructor DownLoadCode(String) is private, we rely on generateNewCode.
        // The test verifies that compareTo delegates to String.compareTo on the code values.
        DownLoadCode codeAlpha = DownLoadCode.generateNewCode(1L); // e.g., Pabcdefg01
        DownLoadCode codeBeta = DownLoadCode.generateNewCode(1L);  // e.g., Pxyzabcd01 (different random part)
        DownLoadCode codeGamma = DownLoadCode.generateNewCode(2L); // e.g., Pqrstuvw02

        // Test with itself
        assertEquals(0, codeAlpha.compareTo(codeAlpha), "A code should be equal to itself in comparison.");

        // Test consistency: sgn(x.compareTo(y)) == -sgn(y.compareTo(x))
        int alphaBetaComparison = codeAlpha.compareTo(codeBeta);
        int betaAlphaComparison = codeBeta.compareTo(codeAlpha);
        assertEquals(Integer.signum(alphaBetaComparison), -Integer.signum(betaAlphaComparison),
                "Comparison should be antisymmetric.");

        // Test based on the underlying string comparison
        assertEquals(
                Integer.signum(codeAlpha.value().compareTo(codeBeta.value())),
                Integer.signum(alphaBetaComparison),
                "compareTo should reflect lexicographical string comparison of code values."
        );
        assertEquals(
                Integer.signum(codeAlpha.value().compareTo(codeGamma.value())),
                Integer.signum(codeAlpha.compareTo(codeGamma)),
                "compareTo should reflect lexicographical string comparison of code values."
        );
        assertEquals(
                Integer.signum(codeBeta.value().compareTo(codeGamma.value())),
                Integer.signum(codeBeta.compareTo(codeGamma)),
                "compareTo should reflect lexicographical string comparison of code values."
        );
    }

    @Test
    void noArgConstructor_createsInstanceWithNullCode() {
        DownLoadCode downLoadCode = new DownLoadCode();
        // The 'code' field will be null until set by ORM or other means.
        assertNull(downLoadCode.value(), "Code should be null for an instance created with the no-arg constructor before ORM hydration.");
    }
}