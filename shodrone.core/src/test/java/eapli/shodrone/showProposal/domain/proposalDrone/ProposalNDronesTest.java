package eapli.shodrone.showProposal.domain.proposalDrone;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProposalNDronesTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 100})
    void ensureValidDroneNumbersAreAccepted(int validNumber) {
        ProposalNDrones nd = new ProposalNDrones(validNumber);
        assertEquals(validNumber, nd.number());
        assertEquals(String.valueOf(validNumber), nd.toString());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void ensureInvalidDroneNumbersThrowException(int invalidNumber) {
        assertThrows(IllegalArgumentException.class, () -> new ProposalNDrones(invalidNumber));
    }

    @Test
    void testCompareTo() {
        ProposalNDrones nd1 = new ProposalNDrones(5);
        ProposalNDrones nd2 = new ProposalNDrones(10);
        ProposalNDrones nd3 = new ProposalNDrones(5);

        assertTrue(nd1.compareTo(nd2) < 0);
        assertTrue(nd2.compareTo(nd1) > 0);
        assertEquals(0, nd1.compareTo(nd3));
    }

    @Test
    void testToString() {
        ProposalNDrones nd = new ProposalNDrones(7);
        assertEquals("7", nd.toString());
    }

    @Test
    void testValueOfCreatesEquivalentObject() {
        ProposalNDrones nd1 = ProposalNDrones.valueOf(8);
        ProposalNDrones nd2 = new ProposalNDrones(8);
        assertEquals(nd1, nd2);
    }

    @Test
    void testEqualsAndHashCode() {
        ProposalNDrones nd1 = new ProposalNDrones(3);
        ProposalNDrones nd2 = new ProposalNDrones(3);
        ProposalNDrones nd3 = new ProposalNDrones(4);

        assertEquals(nd1, nd2);
        assertEquals(nd1.hashCode(), nd2.hashCode());

        assertNotEquals(nd1, nd3);
        assertNotEquals(nd1, null);
        assertNotEquals(nd1, new Object());
    }

    @Test
    void ensureNoArgConstructorSetsDefault() {
        ProposalNDrones nd = new ProposalNDrones();
        assertEquals(0, nd.number());
    }

    @Test
    void ensureSetterChangesValue() {
        ProposalNDrones nd = new ProposalNDrones();
        nd.setNumberOfDrones(12);
        assertEquals(12, nd.number());
    }
}
