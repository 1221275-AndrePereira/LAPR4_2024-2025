package eapli.shodrone.showrequest.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.temporal.ChronoUnit;

public class RequestDurationTest {



    @Test
    void testValueOf_validMinutes() {
        RequestDuration requestDuration = RequestDuration.valueOf(60);
        assertNotNull(requestDuration);
        assertEquals(60, requestDuration.minutes());
    }

    @Test
    void testValueOf_zeroMinutes_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestDuration.valueOf(0));
    }

    @Test
    void testValueOf_negativeMinutes_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestDuration.valueOf(-30));
    }

    @Test
    void testOfMinutes_validMinutes() {
        RequestDuration requestDuration = RequestDuration.ofMinutes(120);
        assertNotNull(requestDuration);
        assertEquals(120, requestDuration.minutes());
    }

    @Test
    void testFromDuration_validJavaDuration() {
        java.time.Duration javaDuration = java.time.Duration.of(90, ChronoUnit.MINUTES);
        RequestDuration requestDuration = RequestDuration.fromDuration(javaDuration);
        assertNotNull(requestDuration);
        assertEquals(90, requestDuration.minutes());
    }

    @Test
    void testFromDuration_zeroJavaDuration_throwsException() {
        java.time.Duration javaDuration = java.time.Duration.ZERO;
        assertThrows(IllegalArgumentException.class, () -> RequestDuration.fromDuration(javaDuration));
    }

    @Test
    void testFromDuration_negativeJavaDuration_throwsException() {
        java.time.Duration javaDuration = java.time.Duration.of(-10, ChronoUnit.MINUTES);
        assertThrows(IllegalArgumentException.class, () -> RequestDuration.fromDuration(javaDuration));
    }

    @Test
    void testEquals() {
        RequestDuration requestDuration1 = RequestDuration.valueOf(45);
        RequestDuration requestDuration2 = RequestDuration.valueOf(45);
        RequestDuration requestDuration3 = RequestDuration.valueOf(60);

        assertEquals(requestDuration1, requestDuration2);
        assertNotEquals(requestDuration1, requestDuration3);
        // Test equality with a different object type
        assertNotEquals(requestDuration1, new Object());
    }

    @Test
    void testCompareTo() {
        RequestDuration requestDuration1 = RequestDuration.valueOf(30);
        RequestDuration requestDuration2 = RequestDuration.valueOf(60);
        RequestDuration requestDuration3 = RequestDuration.valueOf(30);

        assertTrue(requestDuration1.compareTo(requestDuration2) < 0);
        assertTrue(requestDuration2.compareTo(requestDuration1) > 0);
        assertEquals(0, requestDuration1.compareTo(requestDuration3));
    }

    @Test
    void testToString() {
        RequestDuration requestDuration = RequestDuration.valueOf(75);
        assertEquals("75", requestDuration.toString());
    }
}