package eapli.shodrone.showrequest.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RequestPlaceTest {

    private static final Float VALID_LATITUDE = 40.7128f;
    private static final Float VALID_LONGITUDE = -74.0060f;

    @Test
    void ensureValidRequestPlaceCanBeCreated() {
        RequestPlace place = RequestPlace.valueOf(VALID_LATITUDE, VALID_LONGITUDE);
        assertNotNull(place);
        assertEquals(VALID_LATITUDE, place.getLatitude());
        assertEquals(VALID_LONGITUDE, place.getLongitude());
    }

    @Test
    void ensureRequestPlaceCannotBeCreatedWithNullLatitude() {
        assertThrows(NullPointerException.class, () -> RequestPlace.valueOf(null, VALID_LONGITUDE));
    }

    @Test
    void ensureRequestPlaceCannotBeCreatedWithNullLongitude() {
        assertThrows(NullPointerException.class, () -> RequestPlace.valueOf(VALID_LATITUDE, null));
    }

    @ParameterizedTest
    @CsvSource({
            "-90.1, -74.0060", // Latitude too low
            "90.1, -74.0060",  // Latitude too high
    })
    void ensureRequestPlaceCannotBeCreatedWithInvalidLatitude(float latitude, float longitude) {
        assertThrows(IllegalArgumentException.class, () -> RequestPlace.valueOf(latitude, longitude));
    }

    @ParameterizedTest
    @CsvSource({
            "40.7128, -180.1", // Longitude too low
            "40.7128, 180.1"   // Longitude too high
    })
    void ensureRequestPlaceCannotBeCreatedWithInvalidLongitude(float latitude, float longitude) {
        assertThrows(IllegalArgumentException.class, () -> RequestPlace.valueOf(latitude, longitude));
    }

    @Test
    void ensureValidBoundaryValuesForLatitude() {
        assertDoesNotThrow(() -> RequestPlace.valueOf(-90.0f, VALID_LONGITUDE));
        assertDoesNotThrow(() -> RequestPlace.valueOf(90.0f, VALID_LONGITUDE));
    }

    @Test
    void ensureValidBoundaryValuesForLongitude() {
        assertDoesNotThrow(() -> RequestPlace.valueOf(VALID_LATITUDE, -180.0f));
        assertDoesNotThrow(() -> RequestPlace.valueOf(VALID_LATITUDE, 180.0f));
    }

    @Test
    void testGetters() {
        RequestPlace place = RequestPlace.valueOf(VALID_LATITUDE, VALID_LONGITUDE);
        assertEquals(VALID_LATITUDE, place.getLatitude());
        assertEquals(VALID_LONGITUDE, place.getLongitude());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestPlace place1 = RequestPlace.valueOf(VALID_LATITUDE, VALID_LONGITUDE);
        RequestPlace place2 = RequestPlace.valueOf(VALID_LATITUDE, VALID_LONGITUDE);
        RequestPlace place3 = RequestPlace.valueOf(41.0f, -75.0f);
        RequestPlace place4 = RequestPlace.valueOf(VALID_LATITUDE, -75.0f); // Different longitude
        RequestPlace place5 = RequestPlace.valueOf(41.0f, VALID_LONGITUDE); // Different latitude


        // Reflexivity
        assertEquals(place1, place1);
        assertEquals(place1.hashCode(), place1.hashCode());

        // Symmetry
        assertEquals(place1, place2);
        assertEquals(place2, place1);
        assertEquals(place1.hashCode(), place2.hashCode());

        // Transitivity (implicit via place1, place2)

        // Inequality
        assertNotEquals(place1, place3);
        assertNotEquals(place1.hashCode(), place3.hashCode()); // Hashcodes *could* collide, but unlikely for these simple values
        // and good hash functions. Lombok's default should be fine.

        assertNotEquals(place1, place4);
        assertNotEquals(place1, place5);


        // Inequality with null
        assertNotEquals(null, place1);

        // Inequality with different type
        assertNotEquals("SomeString", place1);
    }

    @Test
    void testToString() {
        RequestPlace place = RequestPlace.valueOf(VALID_LATITUDE, VALID_LONGITUDE);
        String expectedString = "RequestPlace{latitude=" + VALID_LATITUDE + ", longitude=" + VALID_LONGITUDE + "}";
        assertEquals(expectedString, place.toString());
    }

    @Test
    void testCompareTo() {
        RequestPlace place1 = RequestPlace.valueOf(40.0f, -70.0f);
        RequestPlace place2 = RequestPlace.valueOf(40.0f, -70.0f); // Equal
        RequestPlace place3 = RequestPlace.valueOf(30.0f, -70.0f); // Smaller latitude
        RequestPlace place4 = RequestPlace.valueOf(50.0f, -70.0f); // Larger latitude
        RequestPlace place5 = RequestPlace.valueOf(40.0f, -80.0f); // Smaller longitude
        RequestPlace place6 = RequestPlace.valueOf(40.0f, -60.0f); // Larger longitude

        assertEquals(0, place1.compareTo(place2), "place1 should be equal to place2");
        assertTrue(place1.compareTo(place3) > 0, "place1 should be greater than place3 (latitude)");
        assertTrue(place1.compareTo(place4) < 0, "place1 should be less than place4 (latitude)");
        assertTrue(place1.compareTo(place5) > 0, "place1 should be greater than place5 (longitude)");
        assertTrue(place1.compareTo(place6) < 0, "place1 should be less than place6 (longitude)");

        // Test comparison with null
        assertEquals(1, place1.compareTo(null), "place1 should be greater than null");
    }
}