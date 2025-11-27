package eapli.shodrone.shodroneusermanagement.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShodroneUserPriorityTest {

    @Test
    void ensureEnumValuesExist() {
        assertNotNull(ShodroneUserPriority.Regular);
        assertNotNull(ShodroneUserPriority.VIP);
    }

    @Test
    void ensureEnumValuesHaveCorrectNames() {
        assertEquals("Regular", ShodroneUserPriority.Regular.name());
        assertEquals("VIP", ShodroneUserPriority.VIP.name());
    }

    @Test
    void ensureValueOfWorksCorrectly() {
        assertEquals(ShodroneUserPriority.Regular, ShodroneUserPriority.valueOf("Regular"));
        assertEquals(ShodroneUserPriority.VIP, ShodroneUserPriority.valueOf("VIP"));
    }

    @Test
    void ensureValueOfThrowsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            ShodroneUserPriority.valueOf("INVALID_PRIORITY");
        });
    }

    @Test
    void ensureValuesMethodReturnsAllDefinedPriorities() {
        ShodroneUserPriority[] priorities = ShodroneUserPriority.values();
        assertEquals(2, priorities.length, "There should be exactly two priority values.");
        assertTrue(contains(priorities, ShodroneUserPriority.Regular));
        assertTrue(contains(priorities, ShodroneUserPriority.VIP));
    }

    private boolean contains(ShodroneUserPriority[] array, ShodroneUserPriority value) {
        for (ShodroneUserPriority item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }

    @Test
    void testEnumOrdinality() {
        assertEquals(0, ShodroneUserPriority.Regular.ordinal());
        assertEquals(1, ShodroneUserPriority.VIP.ordinal());
    }

    @Test
    void testEnumEquality() {
        assertSame(ShodroneUserPriority.Regular, ShodroneUserPriority.valueOf("Regular"));
        assertNotSame(ShodroneUserPriority.Regular, ShodroneUserPriority.VIP);
    }
}