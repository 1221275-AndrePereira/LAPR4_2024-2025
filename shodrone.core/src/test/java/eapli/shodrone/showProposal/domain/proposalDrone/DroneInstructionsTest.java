package eapli.shodrone.showProposal.domain.proposalDrone;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DroneInstructionsTest {

    private static final String BASE_PATH = "../docs/FigureDSL/";

    @Test
    void ensureValidSampleFile1IsAccepted() {
        String path = BASE_PATH + "sample_DSL_figure_1.txt";
        DroneInstructions di = DroneInstructions.valueOf(path);
        assertEquals(path, di.toString());
    }

    @Test
    void ensureValidSampleFile2IsAccepted() {
        String path = BASE_PATH + "sample_DSL_figure_2.txt";
        DroneInstructions di = DroneInstructions.valueOf(path);
        assertEquals(path, di.toString());
    }

    @Test
    void ensureValidSampleFile3IsAccepted() {
        String path = BASE_PATH + "sample_DSL_figure_3.txt";
        DroneInstructions di = DroneInstructions.valueOf(path);
        assertEquals(path, di.toString());
    }

    @Test
    void ensureNonExistentFileThrowsException() {
        String path = BASE_PATH + "nonexistent_file.txt";
        assertFalse(new File(path).exists());
        assertThrows(IllegalArgumentException.class, () -> DroneInstructions.valueOf(path));
    }

    @Test
    void ensureNonTxtFileThrowsException() {
        String path = BASE_PATH + "sample_DSL_figure_2.pdf";
        // Simulate the existence of a non-txt file (ensure the file exists beforehand if running this test)
        File file = new File(path);
        if (file.exists()) {
            assertThrows(IllegalArgumentException.class, () -> DroneInstructions.valueOf(path));
        } else {
            System.out.println("Skipped non-txt test: " + path + " does not exist.");
        }
    }

    @Test
    void ensureNullPathThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> DroneInstructions.valueOf(null));
    }

    @Test
    void testCompareToWithSamePath() {
        String path = BASE_PATH + "sample_DSL_figure_1.txt";
        DroneInstructions di1 = DroneInstructions.valueOf(path);
        DroneInstructions di2 = DroneInstructions.valueOf(path);
        assertEquals(0, di1.compareTo(di2));
    }

    @Test
    void testCompareToDifferentPaths() {
        DroneInstructions di1 = DroneInstructions.valueOf(BASE_PATH + "sample_DSL_figure_1.txt");
        DroneInstructions di2 = DroneInstructions.valueOf(BASE_PATH + "sample_DSL_figure_2.txt");
        assertNotEquals(0, di1.compareTo(di2));
    }

    @Test
    void testEquality() {
        DroneInstructions di1 = DroneInstructions.valueOf(BASE_PATH + "sample_DSL_figure_1.txt");
        DroneInstructions di2 = DroneInstructions.valueOf(BASE_PATH + "sample_DSL_figure_1.txt");
        DroneInstructions di3 = DroneInstructions.valueOf(BASE_PATH + "sample_DSL_figure_2.txt");

        assertEquals(di1.toString(), di2.toString());
        assertNotEquals(di1.toString(), di3.toString());
    }

    @Test
    void testToString() {
        String path = BASE_PATH + "sample_DSL_figure_1.txt";
        DroneInstructions di = DroneInstructions.valueOf(path);
        assertEquals(path, di.toString());
    }

    @Test
    void ensureNoArgConstructorSetsNull() {
        DroneInstructions di = new DroneInstructions();
        assertNull(di.toString());
    }
}
