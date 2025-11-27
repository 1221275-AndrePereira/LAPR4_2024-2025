package eapli.shodrone.showProposal.domain.proposalFields;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ProposedDurationTest {

    private final long VALID_MINUTES = 60L;
    private final Duration VALID_JAVA_DURATION = Duration.ofMinutes(VALID_MINUTES);

    @Nested
    @DisplayName("Constructor and Factory Methods Tests")
    class ConstructorAndFactoryTests {

        @Test
        @DisplayName("Public constructor with valid positive minutes succeeds")
        void constructor_withValidPositiveMinutes_succeeds() {
            ProposedDuration duration = new ProposedDuration(VALID_MINUTES);
            assertNotNull(duration);
            assertEquals(VALID_MINUTES, duration.minutes());
        }

        @ParameterizedTest
        @DisplayName("Public constructor with zero or negative minutes throws IllegalArgumentException")
        @ValueSource(longs = {0L, -1L, -100L})
        void constructor_withZeroOrNegativeMinutes_throwsIllegalArgumentException(long minutes) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ProposedDuration(minutes));
            assertEquals("Proposed duration must be greater than zero", ex.getMessage());
        }

        @Test
        @DisplayName("Factory ofMinutes with valid positive minutes succeeds")
        void ofMinutes_withValidPositiveMinutes_succeeds() {
            ProposedDuration duration = ProposedDuration.ofMinutes(VALID_MINUTES);
            assertNotNull(duration);
            assertEquals(VALID_MINUTES, duration.minutes());
        }

        @ParameterizedTest
        @DisplayName("Factory ofMinutes with zero or negative minutes throws IllegalArgumentException")
        @ValueSource(longs = {0L, -1L, -100L})
        void ofMinutes_withZeroOrNegativeMinutes_throwsIllegalArgumentException(long minutes) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ProposedDuration.ofMinutes(minutes));
            assertEquals("Proposed duration must be greater than zero", ex.getMessage());
        }

        @Test
        @DisplayName("Factory fromDuration with valid positive java.time.Duration succeeds")
        void fromDuration_withValidPositiveDuration_succeeds() {
            ProposedDuration duration = ProposedDuration.fromDuration(VALID_JAVA_DURATION);
            assertNotNull(duration);
            assertEquals(VALID_MINUTES, duration.minutes());
        }

        @Test
        @DisplayName("Factory fromDuration with null java.time.Duration throws IllegalArgumentException")
        void fromDuration_withNullDuration_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ProposedDuration.fromDuration(null));
            assertEquals("Proposed duration cannot be null", ex.getMessage());
        }

        @Test
        @DisplayName("Factory fromDuration with zero java.time.Duration throws IllegalArgumentException")
        void fromDuration_withZeroDuration_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ProposedDuration.fromDuration(Duration.ZERO));
            assertEquals("Proposed duration must be positive", ex.getMessage());
        }

        @Test
        @DisplayName("Factory fromDuration with negative java.time.Duration throws IllegalArgumentException")
        void fromDuration_withNegativeDuration_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ProposedDuration.fromDuration(Duration.ofMinutes(-5)));
            assertEquals("Proposed duration must be positive", ex.getMessage());
        }

        @Test
        @DisplayName("Factory valueOf with valid positive minutes succeeds")
        void valueOf_withValidPositiveMinutes_succeeds() {
            ProposedDuration duration = ProposedDuration.valueOf(VALID_MINUTES);
            assertNotNull(duration);
            assertEquals(VALID_MINUTES, duration.minutes());
        }

        @ParameterizedTest
        @DisplayName("Factory valueOf with zero or negative minutes throws IllegalArgumentException")
        @ValueSource(longs = {0L, -1L, -100L})
        void valueOf_withZeroOrNegativeMinutes_throwsIllegalArgumentException(long minutes) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ProposedDuration.valueOf(minutes));
            assertEquals("Proposed duration must be greater than zero", ex.getMessage());
        }

        @Test
        @DisplayName("Protected ORM constructor initializes duration to 0")
        void protectedConstructor_initializesToZero() {
            ProposedDuration duration = new ProposedDuration(); // Invokes protected constructor
            assertEquals(0L, duration.minutes(), "ORM constructor should initialize minutes to 0 as per current implementation.");
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterTests {
        @Test
        @DisplayName("minutes() returns correct value")
        void minutes_returnsCorrectValue() {
            ProposedDuration duration = new ProposedDuration(VALID_MINUTES);
            assertEquals(VALID_MINUTES, duration.minutes());
        }

        @Test
        @DisplayName("toDuration() returns correct java.time.Duration")
        void toDuration_returnsCorrectJavaDuration() {
            ProposedDuration duration = new ProposedDuration(VALID_MINUTES);
            assertEquals(VALID_JAVA_DURATION, duration.toDuration());
        }

        @Test
        @DisplayName("toDuration() for ORM constructed object (0 minutes)")
        void toDuration_forOrmConstructed_returnsZeroDuration() {
            ProposedDuration duration = new ProposedDuration(); // 0 minutes
            assertEquals(Duration.ZERO, duration.toDuration());
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringTests {
        @Test
        @DisplayName("toString() returns string representation of minutes")
        void toString_returnsStringOfMinutes() {
            ProposedDuration duration = new ProposedDuration(VALID_MINUTES);
            assertEquals(String.valueOf(VALID_MINUTES), duration.toString());
        }

        @Test
        @DisplayName("toString() for ORM constructed object (0 minutes)")
        void toString_forOrmConstructed_returnsZeroString() {
            ProposedDuration duration = new ProposedDuration();
            assertEquals("0", duration.toString());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {
        private final ProposedDuration duration1_60 = ProposedDuration.ofMinutes(60L);
        private final ProposedDuration duration2_60 = ProposedDuration.ofMinutes(60L);
        private final ProposedDuration duration_90 = ProposedDuration.ofMinutes(90L);
        private final ProposedDuration orm_duration_0 = new ProposedDuration(); // 0 minutes
        private final ProposedDuration orm_duration_0_alt = new ProposedDuration(); // 0 minutes


        @Test
        @DisplayName("equals() is reflexive")
        void equals_isReflexive() {
            assertTrue(duration1_60.equals(duration1_60));
            assertTrue(orm_duration_0.equals(orm_duration_0));
        }

        @Test
        @DisplayName("equals() is symmetric")
        void equals_isSymmetric() {
            assertTrue(duration1_60.equals(duration2_60));
            assertTrue(duration2_60.equals(duration1_60));

            assertTrue(orm_duration_0.equals(orm_duration_0_alt));
            assertTrue(orm_duration_0_alt.equals(orm_duration_0));
        }

        @Test
        @DisplayName("equals() is transitive")
        void equals_isTransitive() {
            ProposedDuration duration3_60 = ProposedDuration.ofMinutes(60L);
            assertTrue(duration1_60.equals(duration2_60));
            assertTrue(duration2_60.equals(duration3_60));
            assertTrue(duration1_60.equals(duration3_60));
        }

        @Test
        @DisplayName("equals() returns true for equal durations")
        void equals_returnsTrueForEqualDurations() {
            assertEquals(duration1_60, duration2_60);
            assertEquals(orm_duration_0, orm_duration_0_alt);
        }

        @Test
        @DisplayName("equals() returns false for different durations")
        void equals_returnsFalseForDifferentDurations() {
            assertNotEquals(duration1_60, duration_90);
            assertNotEquals(duration1_60, orm_duration_0);
        }

        @Test
        @DisplayName("equals() returns false for null")
        void equals_returnsFalseForNull() {
            assertFalse(duration1_60.equals(null));
        }

        @Test
        @DisplayName("equals() returns false for different type")
        void equals_returnsFalseForDifferentType() {
            assertFalse(duration1_60.equals(new Object()));
        }

        @Test
        @DisplayName("hashCode() is consistent for equal objects")
        void hashCode_isConsistentForEqualObjects() {
            assertEquals(duration1_60.hashCode(), duration2_60.hashCode());
            assertEquals(orm_duration_0.hashCode(), orm_duration_0_alt.hashCode());
        }

        @Test
        @DisplayName("hashCode() differs for different objects (usually)")
        void hashCode_differsForDifferentObjects() {
            // This is not a strict requirement but good practice for hash code quality
            assertNotEquals(duration1_60.hashCode(), duration_90.hashCode());
            assertNotEquals(duration1_60.hashCode(), orm_duration_0.hashCode());
        }
    }

    @Nested
    @DisplayName("compareTo() Method Tests")
    class CompareToTests {
        private final ProposedDuration duration_30 = ProposedDuration.ofMinutes(30L);
        private final ProposedDuration duration_60 = ProposedDuration.ofMinutes(60L);
        private final ProposedDuration duration_60_alt = ProposedDuration.ofMinutes(60L);
        private final ProposedDuration duration_90 = ProposedDuration.ofMinutes(90L);
        private final ProposedDuration orm_duration_0 = new ProposedDuration(); // 0 minutes

        @Test
        @DisplayName("compareTo() returns negative when this duration is less")
        void compareTo_returnsNegativeWhenThisIsLess() {
            assertTrue(duration_30.compareTo(duration_60) < 0);
            assertTrue(orm_duration_0.compareTo(duration_30) < 0);
        }

        @Test
        @DisplayName("compareTo() returns zero when durations are equal")
        void compareTo_returnsZeroWhenEqual() {
            assertEquals(0, duration_60.compareTo(duration_60_alt));
            assertEquals(0, orm_duration_0.compareTo(new ProposedDuration()));
        }

        @Test
        @DisplayName("compareTo() returns positive when this duration is greater")
        void compareTo_returnsPositiveWhenThisIsGreater() {
            assertTrue(duration_90.compareTo(duration_60) > 0);
            assertTrue(duration_60.compareTo(orm_duration_0) > 0);
        }

        @Test
        @DisplayName("compareTo() with null throws NullPointerException")
        void compareTo_withNull_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> duration_60.compareTo(null));
        }
    }
}