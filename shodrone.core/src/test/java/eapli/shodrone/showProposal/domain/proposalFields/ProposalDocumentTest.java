package eapli.shodrone.showProposal.domain.proposalFields;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProposalDocumentTest {

    // Test data
    private final String path1 = "/usr/docs/proposalA.pdf";
    private final String path2 = "/usr/docs/proposalB.pdf";
    private final String path1_identical = "/usr/docs/proposalA.pdf"; // Same content as path1

    // Constructor Tests
    @Test
    void testConstructor_withValidFilePath() {
        ProposalDocument doc = new ProposalDocument(path1);
        assertNotNull(doc);
        assertEquals(path1, doc.getFilePath(), "File path should be correctly set.");
    }

    @Test
    void testConstructor_withEmptyFilePath() {
        // The current implementation allows empty file paths
        ProposalDocument doc = new ProposalDocument("");
        assertNotNull(doc);
        assertEquals("", doc.getFilePath(), "Empty file path should be allowed and set.");
    }

    @Test
    void testConstructor_withNullFilePath() {
        // Current implementation allows null file paths
        ProposalDocument doc = new ProposalDocument(null);
        assertNotNull(doc);
        assertNull(doc.getFilePath(), "Null file path should be allowed and set.");
    }

    @Test
    void testProtectedConstructor_forOrm() {
        // This test acknowledges the ORM constructor.
        // We can't call it directly, but we can create an instance that mimics its state (filePath = null).
        ProposalDocument doc = new ProposalDocument(null); // Simulates state after ORM might construct it
        assertNull(doc.getFilePath(), "File path should be null, similar to ORM-constructed state before population.");
    }

    // Getter Test
    @Test
    void testGetFilePath() {
        ProposalDocument docWithPath = new ProposalDocument(path1);
        assertEquals(path1, docWithPath.getFilePath());

        ProposalDocument docWithEmptyPath = new ProposalDocument("");
        assertEquals("", docWithEmptyPath.getFilePath());

        ProposalDocument docWithNullPath = new ProposalDocument(null);
        assertNull(docWithNullPath.getFilePath());
    }

    // Equals and HashCode Tests (Lombok generated)
    @Test
    void testEquals_sameObject() {
        ProposalDocument doc1 = new ProposalDocument(path1);
        assertTrue(doc1.equals(doc1), "An object should be equal to itself.");
    }

    @Test
    void testEquals_equalObjects_sameFilePath() {
        ProposalDocument doc1 = new ProposalDocument(path1);
        ProposalDocument doc2 = new ProposalDocument(path1_identical);
        assertTrue(doc1.equals(doc2), "Objects with the same file path should be equal.");
        assertTrue(doc2.equals(doc1), "Equality should be symmetric.");
    }

    @Test
    void testEquals_differentFilePaths() {
        ProposalDocument doc1 = new ProposalDocument(path1);
        ProposalDocument doc2 = new ProposalDocument(path2);
        assertFalse(doc1.equals(doc2), "Objects with different file paths should not be equal.");
    }

    @Test
    void testEquals_oneFilePathNull() {
        ProposalDocument doc1 = new ProposalDocument(path1);
        ProposalDocument docNull = new ProposalDocument(null);
        assertFalse(doc1.equals(docNull), "Object with a path should not equal object with null path.");
        assertFalse(docNull.equals(doc1), "Equality should be symmetric for null path comparison.");
    }

    @Test
    void testEquals_bothFilePathsNull() {
        ProposalDocument docNull1 = new ProposalDocument(null);
        ProposalDocument docNull2 = new ProposalDocument(null);
        assertTrue(docNull1.equals(docNull2), "Objects with both file paths null should be equal.");
    }

    @Test
    void testEquals_withNullObject() {
        ProposalDocument doc1 = new ProposalDocument(path1);
        assertFalse(doc1.equals(null), "Object should not be equal to null.");
    }

    @Test
    void testEquals_withDifferentType() {
        ProposalDocument doc1 = new ProposalDocument(path1);
        assertFalse(doc1.equals(new Object()), "Object should not be equal to an object of a different type.");
    }

    @Test
    void testHashCode_equalObjectsHaveEqualHashCodes() {
        ProposalDocument doc1 = new ProposalDocument(path1);
        ProposalDocument doc1_alt = new ProposalDocument(path1_identical);
        assertEquals(doc1.hashCode(), doc1_alt.hashCode(), "Equal objects must have equal hash codes.");

        ProposalDocument docNull1 = new ProposalDocument(null);
        ProposalDocument docNull2 = new ProposalDocument(null);
        assertEquals(docNull1.hashCode(), docNull2.hashCode(), "Objects with null file paths must have equal hash codes.");

        ProposalDocument docEmpty1 = new ProposalDocument("");
        ProposalDocument docEmpty2 = new ProposalDocument("");
        assertEquals(docEmpty1.hashCode(), docEmpty2.hashCode(), "Objects with empty file paths must have equal hash codes.");
    }

    @Test
    void testHashCode_differentValuesPotentiallyDifferentHashCodes() {
        ProposalDocument docWithPath = new ProposalDocument(path1);
        ProposalDocument docWithDifferentPath = new ProposalDocument(path2);
        ProposalDocument docWithNullPath = new ProposalDocument(null);
        ProposalDocument docWithEmptyPath = new ProposalDocument("");

        if (!path1.equals(path2)) {
            assertNotEquals(docWithPath.hashCode(), docWithDifferentPath.hashCode(),
                    "Hash codes for different non-null paths should ideally differ.");
        }
        if (path1 != null) {
            assertNotEquals(docWithPath.hashCode(), docWithNullPath.hashCode(),
                    "Hash codes for a non-null path and a null path should ideally differ.");
        }
        if (path1 != null && !path1.isEmpty()) {
            assertNotEquals(docWithPath.hashCode(), docWithEmptyPath.hashCode(),
                    "Hash codes for a non-empty path and an empty path should ideally differ.");
        }
        assertNotEquals(docWithEmptyPath.hashCode(), docWithNullPath.hashCode(),
                "Hash codes for an empty path and a null path should ideally differ.");
    }

    // CompareTo Tests
    @Test
    void testCompareTo_lessThan() {
        ProposalDocument docA = new ProposalDocument("A_path");
        ProposalDocument docB = new ProposalDocument("B_path");
        assertTrue(docA.compareTo(docB) < 0, "A_path should be less than B_path.");
    }

    @Test
    void testCompareTo_equalTo() {
        ProposalDocument docA1 = new ProposalDocument("A_path");
        ProposalDocument docA2 = new ProposalDocument("A_path");
        assertEquals(0, docA1.compareTo(docA2), "Identical paths should compare as equal.");
    }

    @Test
    void testCompareTo_greaterThan() {
        ProposalDocument docB = new ProposalDocument("B_path");
        ProposalDocument docA = new ProposalDocument("A_path");
        assertTrue(docB.compareTo(docA) > 0, "B_path should be greater than A_path.");
    }

    @Test
    void testCompareTo_withEmptyString() {
        ProposalDocument docA = new ProposalDocument("A_path");
        ProposalDocument docEmpty = new ProposalDocument("");
        assertTrue(docEmpty.compareTo(docA) < 0, "Empty string path should be less than non-empty.");
        assertTrue(docA.compareTo(docEmpty) > 0, "Non-empty path should be greater than empty string path.");
    }

    @Test
    void testCompareTo_thisFilePathIsNull_throwsNullPointerException() {
        ProposalDocument docNull = new ProposalDocument(null);
        ProposalDocument docA = new ProposalDocument("A_path");
        assertThrows(NullPointerException.class, () -> docNull.compareTo(docA),
                "Comparing with this.filePath as null should throw NullPointerException.");
    }

    @Test
    void testCompareTo_otherFilePathIsNull_throwsNullPointerException() {
        ProposalDocument docA = new ProposalDocument("A_path");
        ProposalDocument docNull = new ProposalDocument(null);
        assertThrows(NullPointerException.class, () -> docA.compareTo(docNull),
                "Comparing with other.filePath as null should throw NullPointerException.");
    }

    @Test
    void testCompareTo_bothFilePathsAreNull_throwsNullPointerException() {
        ProposalDocument docNull1 = new ProposalDocument(null);
        ProposalDocument docNull2 = new ProposalDocument(null);
        assertThrows(NullPointerException.class, () -> docNull1.compareTo(docNull2),
                "Comparing when both filePaths are null should throw NullPointerException.");
    }

    @Test
    void testCompareTo_otherObjectIsNull_throwsNullPointerException() {
        ProposalDocument docA = new ProposalDocument("A_path");
        assertThrows(NullPointerException.class, () -> docA.compareTo(null),
                "Comparing with a null object should throw NullPointerException.");
    }
}