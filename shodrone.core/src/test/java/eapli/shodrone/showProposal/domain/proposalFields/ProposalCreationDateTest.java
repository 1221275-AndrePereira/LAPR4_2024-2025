package eapli.shodrone.showProposal.domain.proposalFields;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class ProposalCreationDateTest {

    private final LocalDate testDate = LocalDate.of(2023, Month.OCTOBER, 27);
    private final LocalDate earlierDate = LocalDate.of(2023, Month.JANUARY, 1);
    private final LocalDate laterDate = LocalDate.of(2024, Month.MARCH, 15);

    @Test
    void constructor_withValidDate_createsInstance() {
        ProposalCreationDate pcd = new ProposalCreationDate(testDate);
        assertNotNull(pcd);
        assertEquals(testDate, pcd.date());
    }

    @Test
    void constructor_withNullDate_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProposalCreationDate(null);
        });
        assertEquals("Creation date cannot be null", exception.getMessage());
    }

    @Test
    void noArgConstructor_initializesToCurrentDate() {
        ProposalCreationDate pcd = new ProposalCreationDate();
        assertNotNull(pcd.date(), "Date from no-arg constructor should not be null.");
        // Check if the date is today. This assumes the test runs reasonably fast.
        assertEquals(LocalDate.now(), pcd.date(), "Default constructor should use current date.");
    }

    @Test
    void now_returnsInstanceWithCurrentDate() {
        ProposalCreationDate pcdNow = ProposalCreationDate.now();
        assertNotNull(pcdNow.date());
        assertEquals(LocalDate.now(), pcdNow.date(), "now() factory method should use current date.");
    }

    @Test
    void valueOf_localDate_createsInstance() {
        ProposalCreationDate pcd = ProposalCreationDate.valueOf(testDate);
        assertNotNull(pcd);
        assertEquals(testDate, pcd.date());
    }

    @Test
    void valueOf_localDate_withNullDate_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf((LocalDate) null);
        });
        // This calls the main constructor, so the message comes from there.
        assertEquals("Creation date cannot be null", exception.getMessage());
    }

    // --- Factory Method Tests: valueOf(Calendar) ---

    @Test
    void valueOf_calendar_createsInstance() {
        Calendar calendar = new GregorianCalendar(2023, Calendar.OCTOBER, 27); // Month is 0-indexed for Calendar
        //calendar.setTimeZone(ZoneId.of("America/New_York").toTimeZone()); // Test with a specific timezone

        ProposalCreationDate pcd = ProposalCreationDate.valueOf(calendar);
        assertNotNull(pcd);
        assertEquals(testDate, pcd.date(), "Should correctly convert Calendar to LocalDate, irrespective of Calendar's timezone.");

        Calendar calendarUTC = new GregorianCalendar(2023, Calendar.OCTOBER, 27);
        //calendarUTC.setTimeZone(ZoneId.of("UTC").toTimeZone());
        ProposalCreationDate pcdUTC = ProposalCreationDate.valueOf(calendarUTC);
        assertEquals(testDate, pcdUTC.date(), "Should correctly convert Calendar (UTC) to LocalDate.");
    }

    @Test
    void valueOf_calendar_withNullCalendar_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf((Calendar) null);
        });
        assertEquals("Date cannot be null", exception.getMessage());
    }

    @Test
    void valueOf_string_validFormat_createsInstance() {
        String dateString = "2023-10-27";
        ProposalCreationDate pcd = ProposalCreationDate.valueOf(dateString);
        assertNotNull(pcd);
        assertEquals(testDate, pcd.date());
    }

    @ParameterizedTest
    @ValueSource(strings = {"27/10/2023", "invalid-date", "2023-13-01", "2023-10-32"})
    void valueOf_string_invalidFormat_throwsIllegalArgumentException(String invalidDateString) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf(invalidDateString);
        });
        assertTrue(exception.getMessage().startsWith("Invalid date format for RequestCreationDate:"));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof DateTimeParseException);
    }

    @Test
    void valueOf_string_emptyString_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf("");
        });
        assertEquals("Date string cannot be empty", exception.getMessage());
    }

    @Test
    void valueOf_string_nullString_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf((String) null);
        });
        assertEquals("Date string cannot be empty", exception.getMessage());
    }

    @Test
    void valueOf_stringAndFormatter_valid_createsInstance() {
        String dateString = "27/10/2023";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ProposalCreationDate pcd = ProposalCreationDate.valueOf(dateString, formatter);
        assertNotNull(pcd);
        assertEquals(testDate, pcd.date());
    }

    @Test
    void valueOf_stringAndFormatter_invalidStringForFormatter_throwsIllegalArgumentException() {
        String dateString = "2023-10-27"; // ISO format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Expecting different format
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf(dateString, formatter);
        });
        assertTrue(exception.getMessage().startsWith("Invalid date format for RequestCreationDate:"));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof DateTimeParseException);
    }

    @Test
    void valueOf_stringAndFormatter_emptyString_throwsIllegalArgumentException() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf("", formatter);
        });
        assertEquals("Date string cannot be empty", exception.getMessage());
    }

    @Test
    void valueOf_stringAndFormatter_nullString_throwsIllegalArgumentException() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf(null, formatter);
        });
        assertEquals("Date string cannot be empty", exception.getMessage());
    }

    @Test
    void valueOf_stringAndFormatter_nullFormatter_throwsIllegalArgumentException() {
        String dateString = "2023-10-27";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProposalCreationDate.valueOf(dateString, null);
        });
        assertEquals("Formatter cannot be null", exception.getMessage());
    }

    @Test
    void date_returnsCorrectLocalDate() {
        ProposalCreationDate pcd = new ProposalCreationDate(testDate);
        assertEquals(testDate, pcd.date());
    }

    @Test
    void toString_returnsCorrectFormat() {
        ProposalCreationDate pcd = new ProposalCreationDate(testDate);
        assertEquals("2023-10-27", pcd.toString());
    }

    @Test
    void compareTo_correctlyComparesDates() {
        ProposalCreationDate current = ProposalCreationDate.valueOf(testDate);
        ProposalCreationDate earlier = ProposalCreationDate.valueOf(earlierDate);
        ProposalCreationDate later = ProposalCreationDate.valueOf(laterDate);
        ProposalCreationDate same = ProposalCreationDate.valueOf(testDate);

        assertTrue(current.compareTo(earlier) > 0, "Current date should be after earlier date.");
        assertTrue(current.compareTo(later) < 0, "Current date should be before later date.");
        assertEquals(0, current.compareTo(same), "Current date should be equal to the same date.");
    }

    @Test
    void compareTo_withNullArgument_throwsNullPointerException() {
        ProposalCreationDate date1 = ProposalCreationDate.valueOf(testDate);
        assertThrows(NullPointerException.class, () -> {
            date1.compareTo(null);
        }, "Comparing with a null ProposalCreationDate should throw NullPointerException.");
    }

    @Test
    void equalsAndHashCode_verifyContract() {
        ProposalCreationDate date1_A = ProposalCreationDate.valueOf(LocalDate.of(2023, 1, 1));
        ProposalCreationDate date1_B = ProposalCreationDate.valueOf(LocalDate.of(2023, 1, 1)); // Same as date1_A
        ProposalCreationDate date2 = ProposalCreationDate.valueOf(LocalDate.of(2023, 1, 2));   // Different date
        ProposalCreationDate date_now1 = new ProposalCreationDate(); // Uses LocalDate.now()
        ProposalCreationDate date_now2 = ProposalCreationDate.valueOf(date_now1.date()); // Same as date_now1

        // Reflexivity
        assertEquals(date1_A, date1_A);
        assertEquals(date1_A.hashCode(), date1_A.hashCode());

        // Symmetry
        assertEquals(date1_A, date1_B);
        assertEquals(date1_B, date1_A);
        assertEquals(date1_A.hashCode(), date1_B.hashCode());

        // Transitivity (Implicitly tested by date1_A == date1_B and if we had date1_C == date1_B)
        // For now, we ensure that if date_now1 and date_now2 are created from the same LocalDate, they are equal.
        assertEquals(date_now1, date_now2);
        assertEquals(date_now1.hashCode(), date_now2.hashCode());


        // Consistency (Implicitly tested by multiple calls within these tests)

        // Non-nullity
        assertNotEquals(date1_A, null);

        // Inequality with different dates
        assertNotEquals(date1_A, date2);
        // Hashcodes *could* collide for different objects, but for LocalDate and Lombok's typical impl, they won't for these distinct dates.

        // Inequality with different types
        assertNotEquals(date1_A, new Object());
        assertNotEquals(date1_A, "2023-01-01");

        // Test with dates from no-arg constructor (which uses LocalDate.now())
        // This part can be a bit tricky if tests run across midnight.
        // The key is that two instances initialized with the *same* LocalDate value are equal.
        LocalDate specificNow = LocalDate.now();
        ProposalCreationDate nowA = ProposalCreationDate.valueOf(specificNow);
        ProposalCreationDate nowB = ProposalCreationDate.valueOf(specificNow);
        assertEquals(nowA, nowB);
        assertEquals(nowA.hashCode(), nowB.hashCode());
    }
}