package eapli.shodrone.showrequest.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class RequestShowDateTest {

    private final LocalDateTime testDateTime1 = LocalDateTime.of(2024, Month.JULY, 20, 10, 30, 0);
    private final LocalDateTime testDateTime2 = LocalDateTime.of(2024, Month.AUGUST, 21, 12, 45, 0);
    // testDateTime1.toString() produces "2024-07-20T10:30"
    private final String testDateTime1IsoString = testDateTime1.toString();

    // --- valueOf(LocalDateTime) ---
    @Test
    void valueOf_LocalDateTime_validDate_createsInstance() {
        RequestShowDate rsd = RequestShowDate.valueOf(testDateTime1);
        assertNotNull(rsd);
        assertEquals(testDateTime1, rsd.date());
    }

    @Test
    void valueOf_LocalDateTime_nullDate_throwsIllegalArgumentException() {
        Executable action = () -> RequestShowDate.valueOf((LocalDateTime) null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertEquals("Show date cannot be null", exception.getMessage());
    }

    // --- valueOf(Calendar) ---
    @Test
    void valueOf_Calendar_validCalendar_createsInstance() {
        Calendar calendar = GregorianCalendar.from(testDateTime1.atZone(ZoneId.systemDefault()));
        RequestShowDate rsd = RequestShowDate.valueOf(calendar);
        assertNotNull(rsd);
        assertEquals(testDateTime1, rsd.date());
    }

    @Test
    void valueOf_Calendar_nullCalendar_throwsIllegalArgumentException() {
        Executable action = () -> RequestShowDate.valueOf((Calendar) null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertEquals("Date cannot be null", exception.getMessage());
    }

    // --- valueOf(String) ---
    @Test
    void valueOf_String_validIsoDateTimeString_createsInstance() {
        RequestShowDate rsd = RequestShowDate.valueOf(testDateTime1IsoString);
        assertNotNull(rsd);
        assertEquals(testDateTime1, rsd.date());
    }

    @Test
    void valueOf_String_invalidFormatString_throwsIllegalArgumentException() {
        String invalidDateString = "20-07-2024 10:30"; // Not ISO LocalDateTime format
        Executable action = () -> RequestShowDate.valueOf(invalidDateString);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertTrue(exception.getMessage().startsWith("Invalid date format for RequestShowDate:"));
        assertInstanceOf(DateTimeParseException.class, exception.getCause());
    }

    @Test
    void valueOf_String_onlyDateString_throwsIllegalArgumentException() {
        // LocalDateTime.parse("2024-07-20") throws DateTimeParseException
        // because it expects time components.
        // The Javadoc for valueOf(String) says "Assumes "yyyy-MM-dd" format."
        // which conflicts with LocalDateTime.parse behavior for date-only strings.
        // This test verifies the current behavior.
        String dateOnlyString = "2024-07-20";
        Executable action = () -> RequestShowDate.valueOf(dateOnlyString);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertTrue(exception.getMessage().startsWith("Invalid date format for RequestShowDate:"));
        assertInstanceOf(DateTimeParseException.class, exception.getCause());
    }


    @Test
    void valueOf_String_emptyString_throwsIllegalArgumentException() {
        Executable action = () -> RequestShowDate.valueOf("");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertEquals("Date string cannot be empty", exception.getMessage());
    }

    @Test
    void valueOf_String_nullString_throwsIllegalArgumentException() {
        Executable action = () -> RequestShowDate.valueOf((String) null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertEquals("Date string cannot be empty", exception.getMessage());
    }

    // --- valueOf(String, DateTimeFormatter) ---
    @Test
    void valueOf_StringAndFormatter_validStringAndFormatter_createsInstance() {
        String dateString = "20/07/2024 10:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        RequestShowDate rsd = RequestShowDate.valueOf(dateString, formatter);
        assertNotNull(rsd);
        // Expected LocalDateTime based on parsing "20/07/2024 10:30"
        LocalDateTime expectedDateTime = LocalDateTime.of(2024, Month.JULY, 20, 10, 30);
        assertEquals(expectedDateTime, rsd.date());
    }

    @Test
    void valueOf_StringAndFormatter_invalidFormatForFormatter_throwsIllegalArgumentException() {
        String dateString = "2024-07-20T10:30:00"; // ISO format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // Expects different format
        Executable action = () -> RequestShowDate.valueOf(dateString, formatter);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertTrue(exception.getMessage().startsWith("Invalid date format for RequestShowDate:"));
        assertInstanceOf(DateTimeParseException.class, exception.getCause());
    }

    @Test
    void valueOf_StringAndFormatter_emptyString_throwsIllegalArgumentException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Executable action = () -> RequestShowDate.valueOf("", formatter);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertEquals("Date string cannot be empty", exception.getMessage());
    }

    @Test
    void valueOf_StringAndFormatter_nullString_throwsIllegalArgumentException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Executable action = () -> RequestShowDate.valueOf(null, formatter);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertEquals("Date string cannot be empty", exception.getMessage());
    }

    @Test
    void valueOf_StringAndFormatter_nullFormatter_throwsIllegalArgumentException() {
        String dateString = "20/07/2024 10:30";
        Executable action = () -> RequestShowDate.valueOf(dateString, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);
        assertEquals("Formatter cannot be null", exception.getMessage());
    }

    // --- date() method ---
    @Test
    void date_returnsCorrectLocalDateTime() {
        RequestShowDate rsd = RequestShowDate.valueOf(testDateTime1);
        assertEquals(testDateTime1, rsd.date());
    }

    // --- toString() method ---
    @Test
    void toString_returnsCorrectFormat() {
        // The Javadoc for toString() says "// Default ISO format (yyyy-MM-dd)"
        // but LocalDateTime.toString() returns "yyyy-MM-ddTHH:mm:ss.SSSSSSSSS" (or shorter if fields are zero)
        // This test verifies the current behavior, which is LocalDateTime's default toString.
        RequestShowDate rsd = RequestShowDate.valueOf(testDateTime1);
        assertEquals(testDateTime1.toString(), rsd.toString()); // e.g., "2024-07-20T10:30"
    }

    // --- equals() and hashCode() ---
    @Test
    void equals_sameInstance_returnsTrue() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        assertTrue(rsd1.equals(rsd1)); // Using assertTrue for boolean checks
    }

    @Test
    void equals_sameDateValue_returnsTrue() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        RequestShowDate rsd2 = RequestShowDate.valueOf(testDateTime1);
        assertTrue(rsd1.equals(rsd2));
        assertTrue(rsd2.equals(rsd1));
    }

    @Test
    void equals_differentDateValue_returnsFalse() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        RequestShowDate rsd2 = RequestShowDate.valueOf(testDateTime2);
        assertFalse(rsd1.equals(rsd2));
        assertFalse(rsd2.equals(rsd1));
    }

    @Test
    void equals_nullObject_returnsFalse() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        assertFalse(rsd1.equals(null));
    }

    @Test
    void equals_differentTypeObject_returnsFalse() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        assertFalse(rsd1.equals(new Object()));
    }

    @Test
    void hashCode_consistency() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        assertEquals(rsd1.hashCode(), rsd1.hashCode());
    }

    @Test
    void hashCode_equalObjects_equalHashCodes() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        RequestShowDate rsd2 = RequestShowDate.valueOf(testDateTime1);
        assertEquals(rsd1.hashCode(), rsd2.hashCode());
    }

    @Test
    void hashCode_differentObjects_generallyDifferentHashCodes() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        RequestShowDate rsd2 = RequestShowDate.valueOf(testDateTime2);
        // It's possible for non-equal objects to have the same hash code (collision),
        // but for these distinct LocalDateTime values, it's highly unlikely with default implementations.
        assertNotEquals(rsd1.hashCode(), rsd2.hashCode());
    }

    // --- compareTo() method ---
    @Test
    void compareTo_lessThan_returnsNegative() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1); // Earlier
        RequestShowDate rsd2 = RequestShowDate.valueOf(testDateTime2); // Later
        assertTrue(rsd1.compareTo(rsd2) < 0);
    }

    @Test
    void compareTo_greaterThan_returnsPositive() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1); // Earlier
        RequestShowDate rsd2 = RequestShowDate.valueOf(testDateTime2); // Later
        assertTrue(rsd2.compareTo(rsd1) > 0);
    }

    @Test
    void compareTo_equalTo_returnsZero() {
        RequestShowDate rsd1 = RequestShowDate.valueOf(testDateTime1);
        RequestShowDate rsd2 = RequestShowDate.valueOf(testDateTime1);
        assertEquals(0, rsd1.compareTo(rsd2));
    }
}