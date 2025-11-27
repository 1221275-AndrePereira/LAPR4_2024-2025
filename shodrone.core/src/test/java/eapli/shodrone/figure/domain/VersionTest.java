package eapli.shodrone.figure.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

    @Test
    void valueOf_validVersion_createsInstance() {
        String validVersionStr = "1.0.0";
        Version version = Version.valueOf(validVersionStr);
        assertNotNull(version);
        assertEquals(validVersionStr, version.toString());
    }

    @Test
    void valueOf_validVersionSingleNumber_createsInstance() {
        String validVersionStr = "1";
        Version version = Version.valueOf(validVersionStr);
        assertNotNull(version);
        assertEquals(validVersionStr, version.toString());
    }

    @Test
    void valueOf_validVersionMultipleDots_createsInstance() {
        String validVersionStr = "1.2.3.4";
        Version version = Version.valueOf(validVersionStr);
        assertNotNull(version);
        assertEquals(validVersionStr, version.toString());
    }

    @Test
    void valueOf_nullVersion_NullPointerException() {
        assertThrows(NullPointerException.class, () -> Version.valueOf(null));
    }

    @Test
    void valueOf_emptyVersion_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Version.valueOf(""));
    }

    @Test
    void valueOf_invalidFormatAlphabetical_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Version.valueOf("abc"));
    }

    @Test
    void valueOf_invalidFormatStartsWithDot_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Version.valueOf(".1.0"));
    }

    @Test
    void valueOf_invalidFormatEndsWithDot_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Version.valueOf("1.0."));
    }

    @Test
    void valueOf_invalidFormatContainsLetters_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Version.valueOf("1.a.0"));
    }

    @Test
    void equals_sameVersionString_returnsTrue() {
        Version v1 = Version.valueOf("1.0");
        Version v2 = Version.valueOf("1.0");
        assertEquals(v1, v2);
    }

    @Test
    void equals_differentVersionString_returnsFalse() {
        Version v1 = Version.valueOf("1.0");
        Version v2 = Version.valueOf("1.1");
        assertNotEquals(v1, v2);
    }

    @Test
    void equals_nullObject_returnsFalse() {
        Version v1 = Version.valueOf("1.0");
        assertNotEquals(null, v1);
    }

    @Test
    void equals_differentClass_returnsFalse() {
        Version v1 = Version.valueOf("1.0");
        assertNotEquals("1.0", v1);
    }

    @Test
    void compareTo_differentVersions() {
        Version v1 = Version.valueOf("1.0");
        Version v2 = Version.valueOf("1.0.1");
        Version v3 = Version.valueOf("2.0");
        Version v4 = Version.valueOf("1.0");

        assertTrue(v1.compareTo(v2) < 0);
        assertTrue(v1.compareTo(v3) < 0);
        assertEquals(0, v1.compareTo(v4));
        assertTrue(v3.compareTo(v1) > 0);
    }

    @Test
    void toString_returnsVersionString() {
        String versionStr = "2.3.4";
        Version version = Version.valueOf(versionStr);
        assertEquals(versionStr, version.toString());
    }
}