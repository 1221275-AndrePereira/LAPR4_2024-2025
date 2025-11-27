package eapli.shodrone.showrequest.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class RequestNDronesTest {

    @Test
    void testValueOfWithInvalidNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestNDrones.valueOf(-1));
    }

    @Test
    void testValueOfWithZeroNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestNDrones.valueOf(0));
    }

    @Test
    void testValueOfWithValidNumber() {
        RequestNDrones drones = RequestNDrones.valueOf(3);
        assertEquals(3, drones.number());
    }

    @Test
    void testToStringReturnsNumber() {
        RequestNDrones drones = RequestNDrones.valueOf(5);
        assertEquals("5", drones.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestNDrones a = RequestNDrones.valueOf(2);
        RequestNDrones b = RequestNDrones.valueOf(2);
        RequestNDrones c = RequestNDrones.valueOf(4);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void testCompareTo() {
        RequestNDrones d1 = RequestNDrones.valueOf(2);
        RequestNDrones d2 = RequestNDrones.valueOf(3);
        RequestNDrones d3 = RequestNDrones.valueOf(2);

        assertTrue(d1.compareTo(d2) < 0);
        assertTrue(d2.compareTo(d1) > 0);
        assertEquals(0, d1.compareTo(d3));
    }

    @Test
    void testValueOfWithZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestNDrones.valueOf(0));
    }

    @Test
    void testValueOfWithNegativeNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestNDrones.valueOf(-5));
    }
}
