package eapli.shodrone.showProposal.domain.simulationReport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SimulationReportTest {

    // Helper method to set private field 'id' via reflection
    private void setId(SimulationReport report, Long idValue) {
        try {
            Field idField = SimulationReport.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(report, idValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set id field via reflection", e);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @ParameterizedTest
        @EnumSource(SimulationResult.class)
        @DisplayName("Main constructor sets fields correctly and ID is null")
        void mainConstructor_setsFieldsCorrectly(SimulationResult expectedResult) {
            LocalDateTime before = LocalDateTime.now();
            SimulationReport report = new SimulationReport(expectedResult);
            LocalDateTime after = LocalDateTime.now();

            assertNull(report.getId(), "ID should be null initially for a new entity.");
            assertEquals(expectedResult, report.getSimulationResult(), "SimulationResult should be set as per constructor argument.");
            assertNotNull(report.getCreationDate(), "CreationDate should not be null.");
            assertTrue(!report.getCreationDate().isBefore(before) && !report.getCreationDate().isAfter(after),
                    "Creation date should be the current time. Expected between " + before + " and " + after + ", but was " + report.getCreationDate());
        }

        @Test
        @DisplayName("ORM constructor sets default values and ID is null")
        void ormConstructor_setsDefaultValues() {
            LocalDateTime before = LocalDateTime.now();
            SimulationReport report = new SimulationReport(); // Invokes ORM constructor
            LocalDateTime after = LocalDateTime.now();

            assertNull(report.getId(), "ID should be null initially for an entity created by the ORM constructor.");
            assertEquals(SimulationResult.FAILURE, report.getSimulationResult(), "Default SimulationResult for ORM constructor should be FAILURE.");
            assertNotNull(report.getCreationDate(), "CreationDate should not be null for ORM constructor.");
            assertTrue(!report.getCreationDate().isBefore(before) && !report.getCreationDate().isAfter(after),
                    "Creation date for ORM constructor should be the current time. Expected between " + before + " and " + after + ", but was " + report.getCreationDate());
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterTests {
        @Test
        @DisplayName("getSimulationResult() returns correct value")
        void getSimulationResult_returnsCorrectValue() {
            SimulationReport reportSuccess = new SimulationReport(SimulationResult.SUCCESS);
            assertEquals(SimulationResult.SUCCESS, reportSuccess.getSimulationResult());

            SimulationReport reportFailure = new SimulationReport(SimulationResult.FAILURE);
            assertEquals(SimulationResult.FAILURE, reportFailure.getSimulationResult());

            SimulationReport ormReport = new SimulationReport();
            assertEquals(SimulationResult.FAILURE, ormReport.getSimulationResult());
        }

        @Test
        @DisplayName("getCreationDate() returns non-null and recent date")
        void getCreationDate_returnsRecentDate() {
            SimulationReport report = new SimulationReport(SimulationResult.SUCCESS);
            LocalDateTime creationDate = report.getCreationDate();
            assertNotNull(creationDate);
            // Check if it's very recent (e.g., within a few seconds to account for test execution time)
            assertTrue(creationDate.isAfter(LocalDateTime.now().minusSeconds(5)) &&
                            !creationDate.isAfter(LocalDateTime.now().plusSeconds(1)), // Allow for slight clock differences/execution delays
                    "Creation date should be very close to the current time.");
        }

        @Test
        @DisplayName("getId() returns null initially")
        void getId_returnsNullInitially() {
            SimulationReport report = new SimulationReport(SimulationResult.SUCCESS);
            assertNull(report.getId());

            SimulationReport ormReport = new SimulationReport();
            assertNull(ormReport.getId());
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringTests {
        @Test
        @DisplayName("toString() returns string representation of SimulationResult")
        void toString_returnsSimulationResultString() {
            SimulationReport reportSuccess = new SimulationReport(SimulationResult.SUCCESS);
            assertEquals(SimulationResult.SUCCESS.toString(), reportSuccess.toString());

            SimulationReport reportFailure = new SimulationReport(SimulationResult.FAILURE);
            assertEquals(SimulationResult.FAILURE.toString(), reportFailure.toString());

            SimulationReport ormReport = new SimulationReport(); // Defaults to FAILURE
            assertEquals(SimulationResult.FAILURE.toString(), ormReport.toString());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests (ID-based)")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("equals() is reflexive")
        void equals_isReflexive() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 1L);
            assertTrue(report1.equals(report1), "An object must be equal to itself (non-null ID).");

            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE); // ID is null
            assertTrue(report2.equals(report2), "An object must be equal to itself (null ID).");
        }

        @Test
        @DisplayName("equals() is symmetric")
        void equals_isSymmetric() {
            SimulationReport report1a = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1a, 1L);
            SimulationReport report1b = new SimulationReport(SimulationResult.FAILURE); // Different result, but same ID for test
            setId(report1b, 1L);
            assertTrue(report1a.equals(report1b), "Symmetry check failed for equal non-null IDs.");
            assertTrue(report1b.equals(report1a), "Symmetry check failed for equal non-null IDs (reversed).");

            SimulationReport report2a = new SimulationReport(SimulationResult.SUCCESS); // ID is null
            SimulationReport report2b = new SimulationReport(SimulationResult.FAILURE); // ID is null
            assertTrue(report2a.equals(report2b), "Symmetry check failed for null IDs (Objects.equals(null, null) is true).");
            assertTrue(report2b.equals(report2a), "Symmetry check failed for null IDs (reversed).");

            SimulationReport report3a = new SimulationReport(SimulationResult.SUCCESS);
            setId(report3a, 2L);
            SimulationReport report3b = new SimulationReport(SimulationResult.FAILURE);
            setId(report3b, 3L);
            assertFalse(report3a.equals(report3b), "Symmetry check failed for different IDs.");
            assertFalse(report3b.equals(report3a), "Symmetry check failed for different IDs (reversed).");
        }

        @Test
        @DisplayName("equals() is transitive")
        void equals_isTransitive() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 1L);
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE);
            setId(report2, 1L);
            SimulationReport report3 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report3, 1L);

            assertTrue(report1.equals(report2), "Transitivity pre-condition: report1 equals report2.");
            assertTrue(report2.equals(report3), "Transitivity pre-condition: report2 equals report3.");
            assertTrue(report1.equals(report3), "Transitivity failed for non-null IDs.");

            SimulationReport reportNullId1 = new SimulationReport(SimulationResult.SUCCESS); // ID null
            SimulationReport reportNullId2 = new SimulationReport(SimulationResult.FAILURE); // ID null
            SimulationReport reportNullId3 = new SimulationReport(SimulationResult.SUCCESS); // ID null
            assertTrue(reportNullId1.equals(reportNullId2), "Transitivity pre-condition: reportNullId1 equals reportNullId2.");
            assertTrue(reportNullId2.equals(reportNullId3), "Transitivity pre-condition: reportNullId2 equals reportNullId3.");
            assertTrue(reportNullId1.equals(reportNullId3), "Transitivity failed for null IDs.");
        }

        @Test
        @DisplayName("equals() returns true for same ID, regardless of other fields")
        void equals_trueForSameId() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 10L);
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE); // Different result
            setId(report2, 10L);
            assertEquals(report1, report2, "Objects with the same ID should be equal.");
        }

        @Test
        @DisplayName("equals() returns true for both IDs null")
        void equals_trueForBothIdsNull() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS); // ID is null
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE); // ID is null
            assertEquals(report1, report2, "Objects with both IDs null should be equal.");
        }

        @Test
        @DisplayName("equals() returns false for different IDs")
        void equals_falseForDifferentIds() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 10L);
            SimulationReport report2 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report2, 20L);
            assertNotEquals(report1, report2, "Objects with different IDs should not be equal.");
        }

        @Test
        @DisplayName("equals() returns false for one ID null, other not null")
        void equals_falseForOneIdNull() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 10L);
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE); // ID is null
            assertNotEquals(report1, report2, "Object with non-null ID should not be equal to object with null ID.");
            assertNotEquals(report2, report1, "Object with null ID should not be equal to object with non-null ID.");
        }

        @Test
        @DisplayName("equals() returns false for null object")
        void equals_falseForNullObject() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 1L);
            assertFalse(report1.equals(null), "equals(null) should return false (non-null ID).");

            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE); // ID is null
            assertFalse(report2.equals(null), "equals(null) should return false (null ID).");
        }

        @Test
        @DisplayName("equals() returns false for object of different type")
        void equals_falseForDifferentType() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 1L);
            assertFalse(report1.equals("A String"), "equals() with a different type should return false.");
        }

        @Test
        @DisplayName("hashCode() is consistent for equal objects (same ID)")
        void hashCode_consistentForEqualObjects() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 10L);
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE);
            setId(report2, 10L);
            assertEquals(report1.hashCode(), report2.hashCode(), "Hashcodes should be equal for objects with the same non-null ID.");

            SimulationReport reportNullId1 = new SimulationReport(SimulationResult.SUCCESS); // ID null
            SimulationReport reportNullId2 = new SimulationReport(SimulationResult.FAILURE); // ID null
            assertEquals(reportNullId1.hashCode(), reportNullId2.hashCode(), "Hashcodes should be equal for objects with null IDs (Objects.hash(null)).");
        }

        @Test
        @DisplayName("hashCode() generally differs for objects with different IDs")
        void hashCode_differsForDifferentIds() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 10L);
            SimulationReport report2 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report2, 20L);
            assertNotEquals(report1.hashCode(), report2.hashCode(), "Hashcodes should generally differ for objects with different IDs.");
        }

        @Test
        @DisplayName("hashCode() generally differs for one ID null, other not null")
        void hashCode_differsForOneIdNull() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 10L);
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE); // ID is null
            assertNotEquals(report1.hashCode(), report2.hashCode(), "Hashcodes should differ for one ID null and other non-null (Objects.hash(Long) vs Objects.hash(null)).");
        }
    }

    @Nested
    @DisplayName("compareTo() Method Tests (ID-based)")
    class CompareToTests {
        // Note: The current compareTo implementation (this.id.compareTo(other.id))
        // will throw NullPointerException if either this.id or other.id is null.
        // Tests will reflect this behavior.

        @Test
        @DisplayName("compareTo returns zero when IDs are equal and non-null")
        void compareTo_zeroForEqualNonNullIds() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 5L);
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE);
            setId(report2, 5L);
            assertEquals(0, report1.compareTo(report2), "compareTo should return 0 for equal non-null IDs.");
        }

        @Test
        @DisplayName("compareTo: this.id less than other.id (non-null IDs)")
        void compareTo_thisIdLessThanOtherId() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 1L);
            SimulationReport report2 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report2, 2L);
            assertTrue(report1.compareTo(report2) < 0, "compareTo should return negative if this.id < other.id.");
        }

        @Test
        @DisplayName("compareTo: this.id greater than other.id (non-null IDs)")
        void compareTo_thisIdGreaterThanOtherId() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 10L);
            SimulationReport report2 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report2, 5L);
            assertTrue(report1.compareTo(report2) > 0, "compareTo should return positive if this.id > other.id.");
        }

        @Test
        @DisplayName("compareTo with null other throws NullPointerException")
        void compareTo_nullOther_throwsNullPointerException() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 1L);
            // Accessing other.id will cause NPE if other is null
            assertThrows(NullPointerException.class, () -> report1.compareTo(null),
                    "compareTo(null) should throw NullPointerException when accessing other.id.");
        }

        @Test
        @DisplayName("compareTo when this.id is null throws NullPointerException")
        void compareTo_thisIdIsNull_throwsNullPointerException() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS); // id is null
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE);
            setId(report2, 1L);
            // this.id.compareTo(...) will be null.compareTo(...)
            assertThrows(NullPointerException.class, () -> report1.compareTo(report2),
                    "compareTo should throw NullPointerException if this.id is null.");
        }

        @Test
        @DisplayName("compareTo when other.id is null (and this.id is not) throws NullPointerException")
        void compareTo_otherIdIsNull_thisIdNotNull_throwsNullPointerException() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS);
            setId(report1, 1L);
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE); // id is null
            // Long.compareTo(null) throws NPE.
            assertThrows(NullPointerException.class, () -> report1.compareTo(report2),
                    "compareTo should throw NullPointerException if other.id is null and this.id is not (Long.compareTo(null)).");
        }

        @Test
        @DisplayName("compareTo when both this.id and other.id are null throws NullPointerException")
        void compareTo_bothIdsAreNull_throwsNullPointerException() {
            SimulationReport report1 = new SimulationReport(SimulationResult.SUCCESS); // id is null
            SimulationReport report2 = new SimulationReport(SimulationResult.FAILURE); // id is null
            // this.id.compareTo(other.id) will be null.compareTo(null)
            assertThrows(NullPointerException.class, () -> report1.compareTo(report2),
                    "compareTo should throw NullPointerException if both this.id and other.id are null.");
        }
    }
}