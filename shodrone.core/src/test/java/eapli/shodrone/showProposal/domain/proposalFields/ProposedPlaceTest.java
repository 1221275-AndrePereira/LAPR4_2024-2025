package eapli.shodrone.showProposal.domain.proposalFields;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProposedPlaceTest {

    private final float VALID_LATITUDE_1 = 40.7128f;
    private final float VALID_LONGITUDE_1 = -74.0060f;
    private final float VALID_LATITUDE_2 = 34.0522f;
    private final float VALID_LONGITUDE_2 = -118.2437f;

    @Nested
    @DisplayName("Constructor and Factory Method (valueOf) Tests")
    class ConstructorAndFactoryTests {

        @Test
        @DisplayName("valueOf with valid coordinates succeeds")
        void valueOf_withValidCoordinates_succeeds() {
            ProposedPlace place = ProposedPlace.valueOf(VALID_LATITUDE_1, VALID_LONGITUDE_1);
            assertNotNull(place);
            assertEquals(VALID_LATITUDE_1, place.getLatitude());
            assertEquals(VALID_LONGITUDE_1, place.getLongitude());
        }

        @ParameterizedTest
        @DisplayName("valueOf with invalid latitude throws IllegalArgumentException")
        @ValueSource(floats = {-90.0001f, 90.0001f, -100f, 100f})
        void valueOf_withInvalidLatitude_throwsIllegalArgumentException(float invalidLatitude) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedPlace.valueOf(invalidLatitude, VALID_LONGITUDE_1));
            assertEquals("Latitude must be between -90 and 90 degrees.", ex.getMessage());
        }

        @ParameterizedTest
        @DisplayName("valueOf with invalid longitude throws IllegalArgumentException")
        @ValueSource(floats = {-180.0001f, 180.0001f, -200f, 200f})
        void valueOf_withInvalidLongitude_throwsIllegalArgumentException(float invalidLongitude) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ProposedPlace.valueOf(VALID_LATITUDE_1, invalidLongitude));
            assertEquals("Longitude must be between -180 and 180 degrees.", ex.getMessage());
        }

        @Test
        @DisplayName("valueOf with null latitude throws NullPointerException (due to precondition check unboxing)")
        void valueOf_withNullLatitude_throwsNullPointerException() {
            // Preconditions.ensure attempts to unbox the null Float for comparison, causing NPE
            assertThrows(NullPointerException.class,
                    () -> ProposedPlace.valueOf(null, VALID_LONGITUDE_1));
        }

        @Test
        @DisplayName("valueOf with null longitude throws NullPointerException (due to precondition check unboxing)")
        void valueOf_withNullLongitude_throwsNullPointerException() {
            // Preconditions.ensure attempts to unbox the null Float for comparison, causing NPE
            assertThrows(NullPointerException.class,
                    () -> ProposedPlace.valueOf(VALID_LATITUDE_1, null));
        }

        @Test
        @DisplayName("valueOf with boundary latitudes succeeds")
        void valueOf_withBoundaryLatitudes_succeeds() {
            assertNotNull(ProposedPlace.valueOf(-90.0f, VALID_LONGITUDE_1));
            assertNotNull(ProposedPlace.valueOf(90.0f, VALID_LONGITUDE_1));
            assertNotNull(ProposedPlace.valueOf(0.0f, VALID_LONGITUDE_1));
        }

        @Test
        @DisplayName("valueOf with boundary longitudes succeeds")
        void valueOf_withBoundaryLongitudes_succeeds() {
            assertNotNull(ProposedPlace.valueOf(VALID_LATITUDE_1, -180.0f));
            assertNotNull(ProposedPlace.valueOf(VALID_LATITUDE_1, 180.0f));
            assertNotNull(ProposedPlace.valueOf(VALID_LATITUDE_1, 0.0f));
        }
    }

    @Nested
    @DisplayName("ORM Constructor Test")
    class OrmConstructorTest {
        @Test
        @DisplayName("Protected ORM constructor initializes latitude and longitude to null")
        void protectedConstructor_initializesToNull() {
            ProposedPlace place = new ProposedPlace(); // Invokes protected constructor
            assertNull(place.getLatitude(), "ORM constructor should initialize latitude to null.");
            assertNull(place.getLongitude(), "ORM constructor should initialize longitude to null.");
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterTests {
        @Test
        @DisplayName("getLatitude() and getLongitude() return correct values")
        void getters_returnCorrectValues() {
            ProposedPlace place = ProposedPlace.valueOf(VALID_LATITUDE_1, VALID_LONGITUDE_1);
            assertEquals(VALID_LATITUDE_1, place.getLatitude());
            assertEquals(VALID_LONGITUDE_1, place.getLongitude());
        }

        @Test
        @DisplayName("getters for ORM constructed object return null")
        void getters_forOrmConstructed_returnNull() {
            ProposedPlace place = new ProposedPlace();
            assertNull(place.getLatitude());
            assertNull(place.getLongitude());
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringTests {
        @Test
        @DisplayName("toString() returns correct string representation for valid place")
        void toString_returnsCorrectFormat() {
            ProposedPlace place = ProposedPlace.valueOf(VALID_LATITUDE_1, VALID_LONGITUDE_1);
            String expected = "ProposedPlace{latitude=" + VALID_LATITUDE_1 + ", longitude=" + VALID_LONGITUDE_1 + "}";
            assertEquals(expected, place.toString());
        }

        @Test
        @DisplayName("toString() for ORM constructed object returns format with nulls")
        void toString_forOrmConstructed_returnsFormatWithNulls() {
            ProposedPlace place = new ProposedPlace();
            String expected = "ProposedPlace{latitude=null, longitude=null}";
            assertEquals(expected, place.toString());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {
        private final ProposedPlace place1_A = ProposedPlace.valueOf(VALID_LATITUDE_1, VALID_LONGITUDE_1);
        private final ProposedPlace place2_A = ProposedPlace.valueOf(VALID_LATITUDE_1, VALID_LONGITUDE_1); // Same as place1_A
        private final ProposedPlace place_B_lat = ProposedPlace.valueOf(VALID_LATITUDE_2, VALID_LONGITUDE_1); // Different lat
        private final ProposedPlace place_B_lon = ProposedPlace.valueOf(VALID_LATITUDE_1, VALID_LONGITUDE_2); // Different lon
        private final ProposedPlace place_C_both = ProposedPlace.valueOf(VALID_LATITUDE_2, VALID_LONGITUDE_2); // Different lat & lon
        private final ProposedPlace orm_place1 = new ProposedPlace();
        private final ProposedPlace orm_place2 = new ProposedPlace();

        @Test
        @DisplayName("equals() is reflexive")
        void equals_isReflexive() {
            assertTrue(place1_A.equals(place1_A));
            assertTrue(orm_place1.equals(orm_place1));
        }

        @Test
        @DisplayName("equals() is symmetric")
        void equals_isSymmetric() {
            assertTrue(place1_A.equals(place2_A));
            assertTrue(place2_A.equals(place1_A));

            assertTrue(orm_place1.equals(orm_place2));
            assertTrue(orm_place2.equals(orm_place1));
        }

        @Test
        @DisplayName("equals() is transitive")
        void equals_isTransitive() {
            ProposedPlace place3_A = ProposedPlace.valueOf(VALID_LATITUDE_1, VALID_LONGITUDE_1);
            assertTrue(place1_A.equals(place2_A));
            assertTrue(place2_A.equals(place3_A));
            assertTrue(place1_A.equals(place3_A));
        }

        @Test
        @DisplayName("equals() returns true for equal coordinates")
        void equals_returnsTrueForEqualCoordinates() {
            assertEquals(place1_A, place2_A);
        }

        @Test
        @DisplayName("equals() returns true for two ORM-constructed objects (both fields null)")
        void equals_returnsTrueForTwoOrmObjects() {
            assertEquals(orm_place1, orm_place2);
        }

        @Test
        @DisplayName("equals() returns false for different latitude")
        void equals_returnsFalseForDifferentLatitude() {
            assertNotEquals(place1_A, place_B_lat);
        }

        @Test
        @DisplayName("equals() returns false for different longitude")
        void equals_returnsFalseForDifferentLongitude() {
            assertNotEquals(place1_A, place_B_lon);
        }

        @Test
        @DisplayName("equals() returns false for different latitude and longitude")
        void equals_returnsFalseForDifferentLatitudeAndLongitude() {
            assertNotEquals(place1_A, place_C_both);
        }

        @Test
        @DisplayName("equals() returns false when comparing valid object with ORM object")
        void equals_validWithOrm_returnsFalse() {
            assertNotEquals(place1_A, orm_place1);
            assertNotEquals(orm_place1, place1_A);
        }

        @Test
        @DisplayName("equals() returns false for null object")
        void equals_returnsFalseForNull() {
            assertFalse(place1_A.equals(null));
            assertFalse(orm_place1.equals(null));
        }

        @Test
        @DisplayName("equals() returns false for object of different type")
        void equals_returnsFalseForDifferentType() {
            assertFalse(place1_A.equals(new Object()));
        }

        @Test
        @DisplayName("hashCode() is consistent for equal objects")
        void hashCode_isConsistentForEqualObjects() {
            assertEquals(place1_A.hashCode(), place2_A.hashCode());
            assertEquals(orm_place1.hashCode(), orm_place2.hashCode());
        }

        @Test
        @DisplayName("hashCode() generally differs for different objects")
        void hashCode_differsForDifferentObjects() {
            assertNotEquals(place1_A.hashCode(), place_B_lat.hashCode());
            assertNotEquals(place1_A.hashCode(), place_B_lon.hashCode());
            assertNotEquals(place1_A.hashCode(), place_C_both.hashCode());
            assertNotEquals(place1_A.hashCode(), orm_place1.hashCode(),
                    "HashCode of a valid object and an ORM object (null fields) should ideally differ.");
        }
    }

    @Nested
    @DisplayName("compareTo() Method Tests")
    class CompareToTests {
        // Define points for comparison:
        // P_A (lat: 10, lon: 20)
        // P_B (lat: 10, lon: 30)
        // P_C (lat: 0,  lon: 25)
        // P_D (lat: 10, lon: 20) - same as P_A

        private final ProposedPlace pA = ProposedPlace.valueOf(10f, 20f);
        private final ProposedPlace pB = ProposedPlace.valueOf(10f, 30f); // Same lat, greater lon
        private final ProposedPlace pC = ProposedPlace.valueOf(0f, 25f);  // Smaller lat
        private final ProposedPlace pD = ProposedPlace.valueOf(10f, 20f); // Equal to pA

        private final ProposedPlace ormPlace = new ProposedPlace();

        @Test
        @DisplayName("compareTo returns zero when places are equal")
        void compareTo_returnsZeroWhenEqual() {
            assertEquals(0, pA.compareTo(pD));
        }

        @Test
        @DisplayName("compareTo: this less than other (different latitude, smaller lat comes first)")
        void compareTo_thisLessThanOther_byLatitude() {
            // pC (0, 25) vs pA (10, 20)
            assertTrue(pC.compareTo(pA) < 0, "Place with smaller latitude should come first.");
        }

        @Test
        @DisplayName("compareTo: this less than other (same latitude, different longitude, smaller lon comes first)")
        void compareTo_thisLessThanOther_sameLatitude_byLongitude() {
            // pA (10, 20) vs pB (10, 30)
            assertTrue(pA.compareTo(pB) < 0, "With same latitude, place with smaller longitude should come first.");
        }

        @Test
        @DisplayName("compareTo: this greater than other (different latitude, greater lat comes second)")
        void compareTo_thisGreaterThanOther_byLatitude() {
            // pA (10, 20) vs pC (0, 25)
            assertTrue(pA.compareTo(pC) > 0, "Place with greater latitude should come second.");
        }

        @Test
        @DisplayName("compareTo: this greater than other (same latitude, different longitude, greater lon comes second)")
        void compareTo_thisGreaterThanOther_sameLatitude_byLongitude() {
            // pB (10, 30) vs pA (10, 20)
            assertTrue(pB.compareTo(pA) > 0, "With same latitude, place with greater longitude should come second.");
        }

        @Test
        @DisplayName("compareTo with null other throws IllegalArgumentException")
        void compareTo_withNullOther_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> pA.compareTo(null));
            assertEquals("Cannot compare with null ProposedPlace", ex.getMessage());
        }

        @Test
        @DisplayName("compareTo when 'this' is ORM-constructed (null fields) throws NullPointerException")
        void compareTo_thisIsOrm_otherIsValid_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> ormPlace.compareTo(pA),
                    "Comparing ORM object (null lat/lon) with valid object should throw NPE due to 'this' having null fields accessed for comparison.");
        }

        @Test
        @DisplayName("compareTo when 'other' is ORM-constructed (null fields) throws NullPointerException")
        void compareTo_thisIsValid_otherIsOrm_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> pA.compareTo(ormPlace),
                    "Comparing valid object with ORM object (null lat/lon) should throw NPE due to 'other' having null fields accessed for comparison (unboxing).");
        }

        @Test
        @DisplayName("compareTo when both are ORM-constructed (null fields) throws NullPointerException")
        void compareTo_bothAreOrm_throwsNullPointerException() {
            ProposedPlace anotherOrmPlace = new ProposedPlace();
            assertThrows(NullPointerException.class, () -> ormPlace.compareTo(anotherOrmPlace),
                    "Comparing two ORM objects (null lat/lon) should throw NPE due to null fields being accessed for comparison.");
        }
    }
}