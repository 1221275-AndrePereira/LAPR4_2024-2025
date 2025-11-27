package eapli.shodrone.showProposal.domain.proposalFields;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class ProposedShowDateTest {

    private final LocalDateTime VALID_DATE_TIME_1 = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 30, 0);
    private final LocalDateTime VALID_DATE_TIME_2 = LocalDateTime.of(2026, Month.JUNE, 20, 12, 0, 0);
    private final String VALID_DATE_STRING_ISO = "2025-01-15T10:30:00";
    // This string format is invalid for LocalDateTime.parse() without a specific formatter
    private final String INVALID_DATE_STRING_FOR_DEFAULT_PARSE = "15-01-2025 10:30";

    private final String CUSTOM_FORMAT_STRING = "15/01/2025 10:30";
    private final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final LocalDateTime CUSTOM_FORMAT_DATE_TIME = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 30, 0);


    @Nested
    @DisplayName("Constructor and Factory Method (valueOf) Tests")
    class ConstructorAndFactoryTests {

        @Test
        @DisplayName("valueOf(LocalDateTime) with valid date succeeds")
        void valueOf_withValidLocalDateTime_succeeds() {
            ProposedShowDate showDate = ProposedShowDate.valueOf(VALID_DATE_TIME_1);
            assertNotNull(showDate);
            assertEquals(VALID_DATE_TIME_1, showDate.date());
        }

        @Test
        @DisplayName("valueOf(LocalDateTime) with null date throws IllegalArgumentException")
        void valueOf_withNullLocalDateTime_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf((LocalDateTime) null));
            assertEquals("Proposal date cannot be null", ex.getMessage());
        }

        @Test
        @DisplayName("valueOf(Calendar) with valid Calendar succeeds")
        void valueOf_withValidCalendar_succeeds() {
            Calendar calendar = new GregorianCalendar(2025, Calendar.JANUARY, 15, 10, 30, 0);
            // Set a specific timezone for predictable conversion
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

            ProposedShowDate showDate = ProposedShowDate.valueOf(calendar);
            assertNotNull(showDate);

            LocalDateTime expectedDateTime = calendar.toInstant()
                    .atZone(calendar.getTimeZone().toZoneId())
                    .toLocalDateTime();
            assertEquals(expectedDateTime, showDate.date());
            assertEquals(2025, showDate.date().getYear());
            assertEquals(Month.JANUARY, showDate.date().getMonth());
            assertEquals(15, showDate.date().getDayOfMonth());
            assertEquals(10, showDate.date().getHour());
            assertEquals(30, showDate.date().getMinute());
        }

        @Test
        @DisplayName("valueOf(Calendar) with null Calendar throws IllegalArgumentException")
        void valueOf_withNullCalendar_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf((Calendar) null));
            assertEquals("Date cannot be null", ex.getMessage());
        }

        @Test
        @DisplayName("valueOf(String) with valid ISO date-time string succeeds")
        void valueOf_withValidIsoString_succeeds() {
            ProposedShowDate showDate = ProposedShowDate.valueOf(VALID_DATE_STRING_ISO);
            assertNotNull(showDate);
            assertEquals(VALID_DATE_TIME_1, showDate.date());
        }

        @Test
        @DisplayName("valueOf(String) with invalid format string throws IllegalArgumentException")
        void valueOf_withInvalidFormatString_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf(INVALID_DATE_STRING_FOR_DEFAULT_PARSE));
            assertTrue(ex.getMessage().startsWith("Invalid date format for Proposal Date:"));
            assertTrue(ex.getCause() instanceof DateTimeParseException);
        }

        @Test
        @DisplayName("valueOf(String) with empty string throws IllegalArgumentException")
        void valueOf_withEmptyString_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf(""));
            assertEquals("Date string cannot be empty", ex.getMessage());
        }

        @Test
        @DisplayName("valueOf(String) with null string throws IllegalArgumentException")
        void valueOf_withNullString_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf((String) null));
            assertEquals("Date string cannot be empty", ex.getMessage());
        }

        @Test
        @DisplayName("valueOf(String, DateTimeFormatter) with valid string and formatter succeeds")
        void valueOf_withValidStringAndFormatter_succeeds() {
            ProposedShowDate showDate = ProposedShowDate.valueOf(CUSTOM_FORMAT_STRING, CUSTOM_FORMATTER);
            assertNotNull(showDate);
            assertEquals(CUSTOM_FORMAT_DATE_TIME, showDate.date());
        }

        @Test
        @DisplayName("valueOf(String, DateTimeFormatter) with invalid string for formatter throws IllegalArgumentException")
        void valueOf_withInvalidStringForFormatter_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf(VALID_DATE_STRING_ISO, CUSTOM_FORMATTER)); // ISO string with custom formatter
            assertTrue(ex.getMessage().startsWith("Invalid date format for Proposal Date:"));
            assertTrue(ex.getCause() instanceof DateTimeParseException);
        }

        @Test
        @DisplayName("valueOf(String, DateTimeFormatter) with empty string throws IllegalArgumentException")
        void valueOf_withEmptyStringAndFormatter_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf("", CUSTOM_FORMATTER));
            assertEquals("Date string cannot be empty", ex.getMessage());
        }

        @Test
        @DisplayName("valueOf(String, DateTimeFormatter) with null string throws IllegalArgumentException")
        void valueOf_withNullStringAndFormatter_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf(null, CUSTOM_FORMATTER));
            assertEquals("Date string cannot be empty", ex.getMessage());
        }

        @Test
        @DisplayName("valueOf(String, DateTimeFormatter) with null formatter throws IllegalArgumentException")
        void valueOf_withNullFormatter_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedShowDate.valueOf(CUSTOM_FORMAT_STRING, null));
            assertEquals("Formatter cannot be null", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Getter Method (date()) Tests")
    class GetterTests {
        @Test
        @DisplayName("date() returns correct LocalDateTime for regularly constructed object")
        void date_returnsCorrectValue() {
            ProposedShowDate showDate = ProposedShowDate.valueOf(VALID_DATE_TIME_1);
            assertEquals(VALID_DATE_TIME_1, showDate.date());
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringTests {
        @Test
        @DisplayName("toString() returns correct ISO string representation for valid date")
        void toString_returnsCorrectFormat() {
            ProposedShowDate showDate = ProposedShowDate.valueOf(VALID_DATE_TIME_1);
            assertEquals(VALID_DATE_TIME_1.toString(), showDate.toString());
        }

//        @Test
//        @DisplayName("toString() for ORM constructed object returns ISO string of its current time")
//        void toString_forOrmConstructed_returnsFormatOfCurrentTime() {
//            ProposedShowDate showDate = new ProposedShowDate();
//            assertNotNull(showDate.toString());
//            // The string should be the ISO representation of the date stored in the object
//            assertEquals(showDate.date().toString(), showDate.toString());
//            // Verify it's a parsable ISO date-time string
//            assertDoesNotThrow(() -> LocalDateTime.parse(showDate.toString()),
//                    "toString() for ORM object did not produce a valid ISO date-time string.");
//        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {
        private final ProposedShowDate date_A_1 = ProposedShowDate.valueOf(VALID_DATE_TIME_1);
        private final ProposedShowDate date_A_2 = ProposedShowDate.valueOf(VALID_DATE_TIME_1); // Same as date_A_1
        private final ProposedShowDate date_B = ProposedShowDate.valueOf(VALID_DATE_TIME_2);   // Different date

        @Test
        @DisplayName("equals() is reflexive")
        void equals_isReflexive() {
            assertTrue(date_A_1.equals(date_A_1));
            ProposedShowDate ormDate = new ProposedShowDate();
            assertTrue(ormDate.equals(ormDate));
        }

        @Test
        @DisplayName("equals() is symmetric")
        void equals_isSymmetric() {
            assertTrue(date_A_1.equals(date_A_2));
            assertTrue(date_A_2.equals(date_A_1));

            ProposedShowDate ormDate1 = new ProposedShowDate();
            // It's highly unlikely ormDate2 will be equal to ormDate1 if created separately
            // due to LocalDateTime.now(). So, we test symmetry for non-equality.
            try { Thread.sleep(1); } catch (InterruptedException ignored) {} // ensure different time
            ProposedShowDate ormDate2 = new ProposedShowDate();
            if (!ormDate1.equals(ormDate2)) { // Expected case
                assertFalse(ormDate2.equals(ormDate1));
            } else { // Highly unlikely, but if they are equal, symmetry must hold
                assertTrue(ormDate2.equals(ormDate1));
            }
        }

        @Test
        @DisplayName("equals() is transitive")
        void equals_isTransitive() {
            ProposedShowDate date_A_3 = ProposedShowDate.valueOf(VALID_DATE_TIME_1);
            assertTrue(date_A_1.equals(date_A_2));
            assertTrue(date_A_2.equals(date_A_3));
            assertTrue(date_A_1.equals(date_A_3));
        }

        @Test
        @DisplayName("equals() returns true for equal dates")
        void equals_returnsTrueForEqualDates() {
            assertEquals(date_A_1, date_A_2);
        }

        @Test
        @DisplayName("equals() returns false for different dates")
        void equals_returnsFalseForDifferentDates() {
            assertNotEquals(date_A_1, date_B);
        }

//        @Test
//        @DisplayName("equals() returns false for two distinct ORM-constructed objects (usually)")
//        void equals_returnsFalseForTwoDistinctOrmObjects() throws InterruptedException {
//            ProposedShowDate orm1 = new ProposedShowDate();
//            Thread.sleep(5); // Ensure time difference for LocalDateTime.now()
//            ProposedShowDate orm2 = new ProposedShowDate();
//            assertNotEquals(orm1, orm2, "Two ORM-constructed objects created at (slightly) different times should not be equal.");
//        }

        @Test
        @DisplayName("equals() returns false when comparing valid object with ORM object (usually)")
        void equals_validWithOrm_returnsFalse() {
            ProposedShowDate ormDate = new ProposedShowDate();
            // date_A_1 is a fixed date, ormDate is 'now'. Unlikely to be equal.
            assertNotEquals(date_A_1, ormDate);
            assertNotEquals(ormDate, date_A_1);
        }

        @Test
        @DisplayName("equals() returns false for null object")
        void equals_returnsFalseForNull() {
            assertFalse(date_A_1.equals(null));
            ProposedShowDate ormDate = new ProposedShowDate();
            assertFalse(ormDate.equals(null));
        }

        @Test
        @DisplayName("equals() returns false for object of different type")
        void equals_returnsFalseForDifferentType() {
            assertFalse(date_A_1.equals(new Object()));
        }

        @Test
        @DisplayName("hashCode() is consistent for equal objects")
        void hashCode_isConsistentForEqualObjects() {
            assertEquals(date_A_1.hashCode(), date_A_2.hashCode());
        }

        @Test
        @DisplayName("hashCode() generally differs for different objects")
        void hashCode_differsForDifferentObjects() throws InterruptedException {
            assertNotEquals(date_A_1.hashCode(), date_B.hashCode());

            ProposedShowDate orm1 = new ProposedShowDate();
            Thread.sleep(5); // Ensure time difference
            ProposedShowDate orm2 = new ProposedShowDate();
            if (!orm1.equals(orm2)) { // Expected
                assertNotEquals(orm1.hashCode(), orm2.hashCode(), "HashCodes of ORM objects created at different times should differ.");
            }

            assertNotEquals(date_A_1.hashCode(), orm1.hashCode(),
                    "HashCode of a fixed-date object and an ORM object (now) should ideally differ.");
        }
    }

    @Nested
    @DisplayName("compareTo() Method Tests")
    class CompareToTests {
        private final ProposedShowDate date_1 = ProposedShowDate.valueOf(LocalDateTime.of(2025, Month.JANUARY, 1, 12, 0));
        private final ProposedShowDate date_2_sameAs1 = ProposedShowDate.valueOf(LocalDateTime.of(2025, Month.JANUARY, 1, 12, 0));
        private final ProposedShowDate date_3_laterThan1 = ProposedShowDate.valueOf(LocalDateTime.of(2025, Month.JANUARY, 1, 13, 0));
        private final ProposedShowDate date_4_earlierThan1 = ProposedShowDate.valueOf(LocalDateTime.of(2025, Month.JANUARY, 1, 11, 0));

        @Test
        @DisplayName("compareTo returns zero when dates are equal")
        void compareTo_returnsZeroWhenEqual() {
            assertEquals(0, date_1.compareTo(date_2_sameAs1));
        }

        @Test
        @DisplayName("compareTo: this less than other")
        void compareTo_thisLessThanOther() {
            assertTrue(date_4_earlierThan1.compareTo(date_1) < 0, "Earlier date should be less than later date.");
            assertTrue(date_1.compareTo(date_3_laterThan1) < 0, "Earlier date should be less than later date.");
        }

        @Test
        @DisplayName("compareTo: this greater than other")
        void compareTo_thisGreaterThanOther() {
            assertTrue(date_3_laterThan1.compareTo(date_1) > 0, "Later date should be greater than earlier date.");
            assertTrue(date_1.compareTo(date_4_earlierThan1) > 0, "Later date should be greater than earlier date.");
        }
//
//        @Test
//        @DisplayName("compareTo with null other throws NullPointerException")
//        void compareTo_withNullOther_throwsNullPointerException() {
//            // Accessing other.showDate will cause NPE if other is null
//            assertThrows(NullPointerException.class, () -> date_1.compareTo(null));
//        }
//
//        @Test
//        @DisplayName("compareTo with ORM object (date is 'now')")
//        void compareTo_withOrmObject() throws InterruptedException {
//            ProposedShowDate pastFixedDate = ProposedShowDate.valueOf(LocalDateTime.now().minusHours(1));
//            ProposedShowDate futureFixedDate = ProposedShowDate.valueOf(LocalDateTime.now().plusHours(1));
//
//            Thread.sleep(5); // ensure 'now' for ormDate is after pastFixedDate.date() and before futureFixedDate.date()
//            ProposedShowDate ormDate = new ProposedShowDate(); // Date is 'now'
//            Thread.sleep(5);
//
//
//            assertTrue(pastFixedDate.compareTo(ormDate) < 0,
//                    "Fixed past date (" + pastFixedDate.date() + ") should be less than ORM date (" + ormDate.date() + ")");
//            assertTrue(futureFixedDate.compareTo(ormDate) > 0,
//                    "Fixed future date (" + futureFixedDate.date() + ") should be greater than ORM date (" + ormDate.date() + ")");
//
//            ProposedShowDate ormDateEarlier = new ProposedShowDate();
//            Thread.sleep(5); // Ensure time difference
//            ProposedShowDate ormDateLater = new ProposedShowDate();
//
//            if (!ormDateEarlier.date().isEqual(ormDateLater.date())) {
//                assertTrue(ormDateEarlier.compareTo(ormDateLater) < 0,
//                        "ORM object created earlier ("+ormDateEarlier.date()+") should be less than one created later ("+ormDateLater.date()+")");
//            } else {
//                assertEquals(0, ormDateEarlier.compareTo(ormDateLater),
//                        "ORM objects created at effectively the same time should be equal in comparison.");
//            }
//        }
    }
}