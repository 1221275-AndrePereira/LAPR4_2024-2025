package eapli.shodrone.showProposal.domain.proposalDrone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class DroneTypeSettingTest {

    private static String validTxtFilePath;

    @BeforeAll
    static void setupValidFile() throws Exception {
        // Create a temporary .txt file to simulate valid instructions
        validTxtFilePath = "../docs/FigureDSL/sample_DSL_figure_1.txt";
    }

    @Test
    void ensureValidConstructionStoresDataCorrectly() {
        ProposalDroneModel mockProposal = Mockito.mock(ProposalDroneModel.class);
        DroneInstructions instructions = DroneInstructions.valueOf(validTxtFilePath);

        DroneTypeSetting setting = new DroneTypeSetting(mockProposal, instructions);

        assertEquals(mockProposal, setting.proposalModel());
        assertEquals(instructions, setting.instructions());
    }

    @Test
    void ensureEqualsTrueIfSameId() throws Exception {
        ProposalDroneModel model = Mockito.mock(ProposalDroneModel.class);
        DroneInstructions instr = DroneInstructions.valueOf(validTxtFilePath);

        DroneTypeSetting setting1 = new DroneTypeSetting(model, instr);
        DroneTypeSetting setting2 = new DroneTypeSetting(model, instr);

        setId(setting1, 42L);
        setId(setting2, 42L);

        assertEquals(setting1, setting2);
        assertEquals(setting1.hashCode(), setting2.hashCode());
    }

    @Test
    void ensureEqualsFalseIfDifferentId() throws Exception {
        ProposalDroneModel model = Mockito.mock(ProposalDroneModel.class);
        DroneInstructions instr = DroneInstructions.valueOf(validTxtFilePath);

        DroneTypeSetting setting1 = new DroneTypeSetting(model, instr);
        DroneTypeSetting setting2 = new DroneTypeSetting(model, instr);

        setId(setting1, 101L);
        setId(setting2, 102L);

        assertNotEquals(setting1, setting2);
    }

    @Test
    void ensureEqualsFalseIfIdIsNull() {
        ProposalDroneModel model = Mockito.mock(ProposalDroneModel.class);
        DroneInstructions instr = DroneInstructions.valueOf(validTxtFilePath);

        DroneTypeSetting setting1 = new DroneTypeSetting(model, instr);
        DroneTypeSetting setting2 = new DroneTypeSetting(model, instr);

        assertNotEquals(setting1, setting2);
    }

    @Test
    void ensureNoArgConstructorCreatesNullState() {
        DroneTypeSetting empty = new DroneTypeSetting();
        assertNull(empty.instructions());
        assertNull(empty.proposalModel());
    }

    // Utility method to simulate JPA setting the ID
    private void setId(DroneTypeSetting obj, Long idValue) throws Exception {
        var field = DroneTypeSetting.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(obj, idValue);
    }
}
