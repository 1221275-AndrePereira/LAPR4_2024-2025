package eapli.shodrone.showrequest.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class RequestCreationDateTest {

    @Test
    void testValueOfWithValidLocalDate() {
        LocalDate today = LocalDate.now();
        RequestCreationDate creationDate = RequestCreationDate.valueOf(today);
        assertEquals(today, creationDate.date());
    }

    @Test
    void testValueOfWithNullLocalDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestCreationDate.valueOf((LocalDate) null));
    }

    @Test
    void testValueOfWithValidString() {
        RequestCreationDate creationDate = RequestCreationDate.valueOf("2023-10-27");
        assertEquals(LocalDate.of(2023, 10, 27), creationDate.date());
    }

    @Test
    void testValueOfWithEmptyStringThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestCreationDate.valueOf(""));
    }

    @Test
    void testValueOfWithInvalidStringThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestCreationDate.valueOf("27-10-2023"));
    }

    @Test
    void testValueOfWithFormatter() {
        String dateStr = "27/10/2023";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        RequestCreationDate creationDate = RequestCreationDate.valueOf(dateStr, formatter);
        assertEquals(LocalDate.of(2023, 10, 27), creationDate.date());
    }

    @Test
    void testValueOfWithFormatterInvalidThrows() {
        String badDate = "27.10.2023";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        assertThrows(IllegalArgumentException.class, () -> RequestCreationDate.valueOf(badDate, formatter));
    }

    @Test
    void testValueOfWithNullFormatterThrows() {
        assertThrows(IllegalArgumentException.class, () -> RequestCreationDate.valueOf("2023-10-27", null));
    }

    @Test
    void testNowFactoryMethod() {
        RequestCreationDate now = RequestCreationDate.now();
        assertEquals(LocalDate.now(), now.date());
    }

    @Test
    void testToStringReturnsIsoFormat() {
        RequestCreationDate date = RequestCreationDate.valueOf(LocalDate.of(2025, 1, 15));
        assertEquals("2025-01-15", date.toString());
    }

    @Test
    void testCompareTo() {
        RequestCreationDate d1 = RequestCreationDate.valueOf(LocalDate.of(2023, 1, 1));
        RequestCreationDate d2 = RequestCreationDate.valueOf(LocalDate.of(2024, 1, 1));
        RequestCreationDate d3 = RequestCreationDate.valueOf(LocalDate.of(2023, 1, 1));

        assertTrue(d1.compareTo(d2) < 0);
        assertTrue(d2.compareTo(d1) > 0);
        assertEquals(0, d1.compareTo(d3));
    }

    @Test
    void testEqualsAndHashCode() {
        RequestCreationDate a = RequestCreationDate.valueOf(LocalDate.of(2024, 4, 30));
        RequestCreationDate b = RequestCreationDate.valueOf(LocalDate.of(2024, 4, 30));
        RequestCreationDate c = RequestCreationDate.valueOf(LocalDate.of(2024, 5, 1));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
