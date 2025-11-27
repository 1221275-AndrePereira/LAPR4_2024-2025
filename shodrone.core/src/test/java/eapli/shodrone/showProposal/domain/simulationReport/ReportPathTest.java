package eapli.shodrone.showProposal.domain.simulationReport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReportPathTest {

    private final String VALID_PATH_1 = "reports/simulation1.pdf";
    private final String VALID_PATH_2 = "reports/archive/simulation2.csv";
    private final String PATH_WITH_DOT_DOT_EXAMPLE = "../sensitive_data/report.txt";

    // The SimulationResult parameter in the ORM constructor is not used by ReportPath.
    // We can pass null or a dummy object if SimulationResult was a concrete class.
    // For this test, null suffices as the parameter's value doesn't affect ReportPath's state.
    private final SimulationResult DUMMY_SIMULATION_RESULT = null;

    @Nested
    @DisplayName("Constructor and Factory Method (pathOf) Tests")
    class ConstructorAndFactoryTests {

        @Test
        @DisplayName("pathOf with valid path succeeds")
        void pathOf_withValidPath_succeeds() {
            ReportPath reportPath = ReportPath.pathOf(VALID_PATH_1);
            assertNotNull(reportPath);
            assertEquals(VALID_PATH_1, reportPath.path());
        }

        @ParameterizedTest
        @DisplayName("pathOf with null, empty, or blank path throws IllegalArgumentException")
        @NullAndEmptySource // Covers null and ""
        @ValueSource(strings = {" ", "  ", "\t", "\n"}) // Covers blank strings
        void pathOf_withNullEmptyOrBlankPath_throwsIllegalArgumentException(String invalidPath) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ReportPath.pathOf(invalidPath));
            assertEquals("Path cannot be null or blank.", ex.getMessage());
        }

        @Test
        @DisplayName("pathOf with path containing '..' (example) throws IllegalArgumentException")
        void pathOf_withDotDotPathExample_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ReportPath.pathOf(PATH_WITH_DOT_DOT_EXAMPLE));
            assertEquals("Path cannot contain '..'", ex.getMessage());
        }

        @ParameterizedTest
        @DisplayName("pathOf with path containing '..' in various forms throws IllegalArgumentException")
        @ValueSource(strings = {"..", "a/../b", "/../a", "a/..", "a/b/..", "a/b/../c"})
        void pathOf_withVariousDotDotPaths_throwsIllegalArgumentException(String invalidPath) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ReportPath.pathOf(invalidPath));
            assertEquals("Path cannot contain '..'", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("ORM Constructor Test")
    class OrmConstructorTest {
        @Test
        @DisplayName("Protected ORM constructor initializes path to null, ignoring parameters")
        void protectedConstructor_initializesPathToNull() {
            // The 'String path' and 'SimulationResult' arguments to the ORM constructor are ignored.
            ReportPath reportPath = new ReportPath("this_path_is_ignored", DUMMY_SIMULATION_RESULT);
            assertNull(reportPath.path(), "Path from ORM constructor should be null.");
        }
    }

    @Nested
    @DisplayName("Getter Method (path()) Tests")
    class GetterTests {
        @Test
        @DisplayName("path() returns correct path for regularly constructed object")
        void path_returnsCorrectValue() {
            ReportPath reportPath = ReportPath.pathOf(VALID_PATH_1);
            assertEquals(VALID_PATH_1, reportPath.path());
        }

        @Test
        @DisplayName("path() for ORM constructed object returns null")
        void path_forOrmConstructed_returnsNull() {
            ReportPath reportPath = new ReportPath("ignored", DUMMY_SIMULATION_RESULT);
            assertNull(reportPath.path());
        }
    }

    @Nested
    @DisplayName("toFile() Method Tests")
    class ToFileTests {
        @Test
        @DisplayName("toFile() returns correct File object for valid path")
        void toFile_returnsCorrectFileObject() {
            ReportPath reportPath = ReportPath.pathOf(VALID_PATH_1);
            File file = reportPath.toFile();
            assertNotNull(file);
            assertEquals(VALID_PATH_1, file.getPath());
        }

        @Test
        @DisplayName("toFile() for ORM constructed object (null path) throws NullPointerException")
        void toFile_forOrmConstructed_throwsNullPointerException() {
            ReportPath reportPath = new ReportPath("ignored", DUMMY_SIMULATION_RESULT);
            // new File(null) throws NullPointerException
            assertThrows(NullPointerException.class, reportPath::toFile,
                    "toFile() should throw NullPointerException when internal path is null.");
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringTests {
        @Test
        @DisplayName("toString() returns the path string for valid path")
        void toString_returnsPathString() {
            ReportPath reportPath = ReportPath.pathOf(VALID_PATH_1);
            assertEquals(VALID_PATH_1, reportPath.toString());
        }

        @Test
        @DisplayName("toString() for ORM constructed object (null path) returns null")
        void toString_forOrmConstructed_returnsNull() {
            ReportPath reportPath = new ReportPath("ignored", DUMMY_SIMULATION_RESULT);
            // The method is `return this.path;`, so it will return null if path is null.
            assertNull(reportPath.toString());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests (assuming corrected equals method)")
    class EqualsAndHashCodeTests {
        // These tests assume the equals method is corrected to:
        // final ReportPath that = (ReportPath) o;
        // See the note at the beginning of the file.

        private final ReportPath pathA1 = ReportPath.pathOf(VALID_PATH_1);
        private final ReportPath pathA2 = ReportPath.pathOf(VALID_PATH_1); // Same as pathA1
        private final ReportPath pathB = ReportPath.pathOf(VALID_PATH_2);   // Different path

        private final ReportPath ormPath1 = new ReportPath("ignored1", DUMMY_SIMULATION_RESULT); // path is null
        private final ReportPath ormPath2 = new ReportPath("ignored2", DUMMY_SIMULATION_RESULT); // path is null

        @Test
        @DisplayName("equals() is reflexive")
        void equals_isReflexive() {
            assertTrue(pathA1.equals(pathA1));
            assertTrue(ormPath1.equals(ormPath1)); // null path equals null path
        }

        @Test
        @DisplayName("equals() is symmetric")
        void equals_isSymmetric() {
            assertTrue(pathA1.equals(pathA2));
            assertTrue(pathA2.equals(pathA1));

            assertTrue(ormPath1.equals(ormPath2)); // Both have null paths
            assertTrue(ormPath2.equals(ormPath1));
        }

        @Test
        @DisplayName("equals() is transitive")
        void equals_isTransitive() {
            ReportPath pathA3 = ReportPath.pathOf(VALID_PATH_1);
            assertTrue(pathA1.equals(pathA2));
            assertTrue(pathA2.equals(pathA3));
            assertTrue(pathA1.equals(pathA3));

            ReportPath ormPath3 = new ReportPath("ignored3", DUMMY_SIMULATION_RESULT); // path is null
            assertTrue(ormPath1.equals(ormPath2));
            assertTrue(ormPath2.equals(ormPath3));
            assertTrue(ormPath1.equals(ormPath3));
        }

        @Test
        @DisplayName("equals() returns true for equal paths")
        void equals_returnsTrueForEqualPaths() {
            assertEquals(pathA1, pathA2);
        }

        @Test
        @DisplayName("equals() returns true for ORM objects (both paths null)")
        void equals_returnsTrueForOrmObjectsWithNullPaths() {
            assertEquals(ormPath1, ormPath2);
        }

        @Test
        @DisplayName("equals() returns false for different paths")
        void equals_returnsFalseForDifferentPaths() {
            assertNotEquals(pathA1, pathB);
        }

        @Test
        @DisplayName("equals() returns false for valid path vs ORM object (null path)")
        void equals_validPathVsOrmObject_returnsFalse() {
            assertNotEquals(pathA1, ormPath1);
            assertNotEquals(ormPath1, pathA1);
        }

        @Test
        @DisplayName("equals() returns false for null object")
        void equals_returnsFalseForNull() {
            assertFalse(pathA1.equals(null));
            assertFalse(ormPath1.equals(null));
        }

        @Test
        @DisplayName("equals() returns false for object of different type")
        void equals_returnsFalseForDifferentType() {
            assertFalse(pathA1.equals(new Object()));
            assertFalse(ormPath1.equals(new Object()));
        }

        @Test
        @DisplayName("hashCode() is consistent for equal objects")
        void hashCode_isConsistentForEqualObjects() {
            assertEquals(pathA1.hashCode(), pathA2.hashCode());
            assertEquals(ormPath1.hashCode(), ormPath2.hashCode()); // Objects.hash(null) is consistent
        }

        @Test
        @DisplayName("hashCode() generally differs for different objects")
        void hashCode_differsForDifferentObjects() {
            assertNotEquals(pathA1.hashCode(), pathB.hashCode());
            // Hashcode of a valid path and a null path will likely differ
            if (pathA1.path() != null) { // Ensure pathA1 is not null itself for this comparison
                assertNotEquals(pathA1.hashCode(), ormPath1.hashCode());
            }
        }
    }

    @Nested
    @DisplayName("compareTo() Method Tests")
    class CompareToTests {
        private final ReportPath path_A_lex = ReportPath.pathOf("a/report.txt");
        private final ReportPath path_A_lex_same = ReportPath.pathOf("a/report.txt");
        private final ReportPath path_B_lex_greater = ReportPath.pathOf("b/report.txt");
        private final ReportPath path_C_lex_smaller = ReportPath.pathOf("a/another.txt");

        private final ReportPath ormPathWithNullInternal = new ReportPath("ignored", DUMMY_SIMULATION_RESULT); // internal path is null

        @Test
        @DisplayName("compareTo returns zero when paths are equal")
        void compareTo_returnsZeroWhenEqual() {
            assertEquals(0, path_A_lex.compareTo(path_A_lex_same));
        }

        @Test
        @DisplayName("compareTo: this less than other")
        void compareTo_thisLessThanOther() {
            assertTrue(path_C_lex_smaller.compareTo(path_A_lex) < 0, "'a/another.txt' should be less than 'a/report.txt'");
            assertTrue(path_A_lex.compareTo(path_B_lex_greater) < 0, "'a/report.txt' should be less than 'b/report.txt'");
        }

        @Test
        @DisplayName("compareTo: this greater than other")
        void compareTo_thisGreaterThanOther() {
            assertTrue(path_A_lex.compareTo(path_C_lex_smaller) > 0, "'a/report.txt' should be greater than 'a/another.txt'");
            assertTrue(path_B_lex_greater.compareTo(path_A_lex) > 0, "'b/report.txt' should be greater than 'a/report.txt'");
        }

        @Test
        @DisplayName("compareTo with null other throws NullPointerException")
        void compareTo_withNullOther_throwsNullPointerException() {
            // Accessing other.path will cause NPE if other is null
            assertThrows(NullPointerException.class, () -> path_A_lex.compareTo(null));
        }

        @Test
        @DisplayName("compareTo when this.path is null (ORM object) and other.path is valid throws NullPointerException")
        void compareTo_thisPathIsNull_otherIsValid_throwsNullPointerException() {
            // this.path.compareTo(...) will be null.compareTo(...)
            assertThrows(NullPointerException.class, () -> ormPathWithNullInternal.compareTo(path_A_lex));
        }

        @Test
        @DisplayName("compareTo when this.path is valid and other.path is null (ORM object) throws NullPointerException")
        void compareTo_thisIsValid_otherPathIsNull_throwsNullPointerException() {
            // ...compareTo(other.path) will be ...compareTo(null)
            assertThrows(NullPointerException.class, () -> path_A_lex.compareTo(ormPathWithNullInternal));
        }

        @Test
        @DisplayName("compareTo when both this.path and other.path are null (ORM objects) throws NullPointerException")
        void compareTo_bothPathsAreNull_throwsNullPointerException() {
            ReportPath ormPathOtherWithNullInternal = new ReportPath("another_ignored", DUMMY_SIMULATION_RESULT);
            // this.path.compareTo(other.path) will be null.compareTo(null)
            assertThrows(NullPointerException.class, () -> ormPathWithNullInternal.compareTo(ormPathOtherWithNullInternal));
        }
    }
}