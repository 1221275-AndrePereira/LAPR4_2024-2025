package eapli.shodrone.showProposal.domain.proposalDrone;

import eapli.shodrone.droneModel.domain.DroneModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProposalDroneModelTest {

    @Test
    void ensureValidConstructionStoresDataCorrectly() {
        DroneModel mockModel = mock(DroneModel.class);
        ProposalNDrones nDrones = ProposalNDrones.valueOf(3);

        ProposalDroneModel proposal = new ProposalDroneModel(mockModel, nDrones);

        assertEquals(mockModel, proposal.droneModel());
        assertEquals(nDrones, proposal.nDrones());
    }

    @Test
    void ensureGetterReturnsSameDroneModel() {
        DroneModel mockModel = mock(DroneModel.class);
        ProposalDroneModel proposal = new ProposalDroneModel(mockModel, ProposalNDrones.valueOf(2));

        assertSame(mockModel, proposal.droneModel());
    }

    @Test
    void ensureGetterReturnsSameNDrones() {
        ProposalNDrones nDrones = ProposalNDrones.valueOf(4);
        ProposalDroneModel proposal = new ProposalDroneModel(mock(DroneModel.class), nDrones);

        assertSame(nDrones, proposal.nDrones());
    }

    @Test
    void ensureEqualsIsTrueForSameId() {
        DroneModel model = mock(DroneModel.class);
        ProposalNDrones nDrones = ProposalNDrones.valueOf(2);

        ProposalDroneModel pdm1 = new ProposalDroneModel(model, nDrones);
        ProposalDroneModel pdm2 = new ProposalDroneModel(model, nDrones);

        // Use reflection to simulate persistence-assigned IDs
        setId(pdm1, 1L);
        setId(pdm2, 1L);

        assertEquals(pdm1, pdm2);
        assertEquals(pdm1.hashCode(), pdm2.hashCode());
    }

    @Test
    void ensureEqualsIsFalseForDifferentIds() {
        DroneModel model = mock(DroneModel.class);
        ProposalNDrones nDrones = ProposalNDrones.valueOf(2);

        ProposalDroneModel pdm1 = new ProposalDroneModel(model, nDrones);
        ProposalDroneModel pdm2 = new ProposalDroneModel(model, nDrones);

        setId(pdm1, 1L);
        setId(pdm2, 2L);

        assertNotEquals(pdm1, pdm2);
    }

    @Test
    void ensureEqualsIsFalseForNullId() {
        DroneModel model = mock(DroneModel.class);
        ProposalDroneModel pdm1 = new ProposalDroneModel(model, ProposalNDrones.valueOf(1));
        ProposalDroneModel pdm2 = new ProposalDroneModel(model, ProposalNDrones.valueOf(1));

        // no IDs set (remain null)
        assertNotEquals(pdm1, pdm2);
    }

    @Test
    void ensureNoArgConstructorCreatesNullStateForORM() {
        ProposalDroneModel pdm = new ProposalDroneModel();

        assertNull(pdm.droneModel());
        assertNull(pdm.nDrones());
    }

    // Helper method to set ID using reflection (simulate ORM persistence)
    private void setId(ProposalDroneModel proposal, Long idValue) {
        try {
            var field = ProposalDroneModel.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(proposal, idValue);
        } catch (ReflectiveOperationException e) {
            fail("Failed to set ID via reflection: " + e.getMessage());
        }
    }
}
