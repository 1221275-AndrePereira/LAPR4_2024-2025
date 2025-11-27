package eapli.shodrone.showProposal.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProposalVideoLinkTest {

    private final String VALID_VIDEO_LINK_1 = "https://example.com/video1.mp4";
    private final String VALID_VIDEO_LINK_2 = "https://example.com/video2.mkv";
    private final String EMPTY_VIDEO_LINK = "";

    @Nested
    @DisplayName("Constructor and Factory Method (valueOf) Tests")
    class ConstructorAndFactoryTests {

        @Test
        @DisplayName("Main constructor with valid video link succeeds")
        void mainConstructor_withValidLink_succeeds() {
            ProposalVideoLink link = new ProposalVideoLink(VALID_VIDEO_LINK_1);
            assertNotNull(link);
            assertEquals(VALID_VIDEO_LINK_1, link.video());
        }

        @Test
        @DisplayName("Main constructor with empty video link succeeds")
        void mainConstructor_withEmptyLink_succeeds() {
            ProposalVideoLink link = new ProposalVideoLink(EMPTY_VIDEO_LINK);
            assertNotNull(link);
            assertEquals(EMPTY_VIDEO_LINK, link.video());
        }

        @ParameterizedTest
        @DisplayName("Main constructor with null video link throws IllegalArgumentException")
        @NullSource
        void mainConstructor_withNullLink_throwsIllegalArgumentException(String nullVideoLink) {
            assertThrows(IllegalArgumentException.class,
                    () -> new ProposalVideoLink(nullVideoLink));
        }

        @Test
        @DisplayName("valueOf factory method with valid video link succeeds")
        void valueOf_withValidLink_succeeds() {
            ProposalVideoLink link = ProposalVideoLink.valueOf(VALID_VIDEO_LINK_1);
            assertNotNull(link);
            assertEquals(VALID_VIDEO_LINK_1, link.video());
        }

        @Test
        @DisplayName("valueOf factory method with empty video link succeeds")
        void valueOf_withEmptyLink_succeeds() {
            ProposalVideoLink link = ProposalVideoLink.valueOf(EMPTY_VIDEO_LINK);
            assertNotNull(link);
            assertEquals(EMPTY_VIDEO_LINK, link.video());
        }

        @ParameterizedTest
        @DisplayName("valueOf factory method with null video link throws IllegalArgumentException")
        @NullSource
        void valueOf_withNullLink_throwsIllegalArgumentException(String nullVideoLink) {
            assertThrows(IllegalArgumentException.class,
                    () -> ProposalVideoLink.valueOf(nullVideoLink));
        }
    }

    @Nested
    @DisplayName("ORM Constructor Test")
    class OrmConstructorTest {
        @Test
        @DisplayName("Default ORM constructor initializes video to empty string")
        void ormConstructor_initializesVideoToEmptyString() {
            ProposalVideoLink link = new ProposalVideoLink();
            assertNotNull(link);
            assertEquals(EMPTY_VIDEO_LINK, link.video(), "Video link from ORM constructor should be an empty string.");
        }
    }

    @Nested
    @DisplayName("Getter Method (video()) Tests")
    class GetterTests {
        @Test
        @DisplayName("video() returns correct link for regularly constructed object")
        void video_returnsCorrectValue() {
            ProposalVideoLink link = new ProposalVideoLink(VALID_VIDEO_LINK_1);
            assertEquals(VALID_VIDEO_LINK_1, link.video());
        }

        @Test
        @DisplayName("video() for ORM constructed object returns empty string")
        void video_forOrmConstructed_returnsEmptyString() {
            ProposalVideoLink link = new ProposalVideoLink();
            assertEquals(EMPTY_VIDEO_LINK, link.video());
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringTests {
        @Test
        @DisplayName("toString() returns correct string representation")
        void toString_returnsCorrectRepresentation() {
            ProposalVideoLink link = new ProposalVideoLink(VALID_VIDEO_LINK_1);
            String expectedString = "ProposalVideoLink{video='" + VALID_VIDEO_LINK_1 + "'}";
            assertEquals(expectedString, link.toString());
        }

        @Test
        @DisplayName("toString() for ORM constructed object (empty video) returns correct representation")
        void toString_forOrmConstructed_returnsCorrectRepresentation() {
            ProposalVideoLink link = new ProposalVideoLink();
            String expectedString = "ProposalVideoLink{video='" + EMPTY_VIDEO_LINK + "'}";
            assertEquals(expectedString, link.toString());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests (Lombok generated)")
    class EqualsAndHashCodeTests {

        private final ProposalVideoLink linkA1 = new ProposalVideoLink(VALID_VIDEO_LINK_1);
        private final ProposalVideoLink linkA2 = new ProposalVideoLink(VALID_VIDEO_LINK_1); // Same as linkA1
        private final ProposalVideoLink linkB = new ProposalVideoLink(VALID_VIDEO_LINK_2);   // Different link
        private final ProposalVideoLink linkEmpty1 = new ProposalVideoLink(EMPTY_VIDEO_LINK);
        private final ProposalVideoLink linkEmpty2 = new ProposalVideoLink(EMPTY_VIDEO_LINK);
        private final ProposalVideoLink ormLink1 = new ProposalVideoLink(); // video is ""
        private final ProposalVideoLink ormLink2 = new ProposalVideoLink(); // video is ""


        @Test
        @DisplayName("equals() is reflexive")
        void equals_isReflexive() {
            assertTrue(linkA1.equals(linkA1));
            assertTrue(linkEmpty1.equals(linkEmpty1));
            assertTrue(ormLink1.equals(ormLink1));
        }

        @Test
        @DisplayName("equals() is symmetric")
        void equals_isSymmetric() {
            assertTrue(linkA1.equals(linkA2));
            assertTrue(linkA2.equals(linkA1));

            assertTrue(linkEmpty1.equals(linkEmpty2));
            assertTrue(linkEmpty2.equals(linkEmpty1));

            assertTrue(ormLink1.equals(ormLink2));
            assertTrue(ormLink2.equals(ormLink1));

            // Check symmetry between empty string and ORM default
            assertTrue(linkEmpty1.equals(ormLink1));
            assertTrue(ormLink1.equals(linkEmpty1));
        }

        @Test
        @DisplayName("equals() is transitive")
        void equals_isTransitive() {
            ProposalVideoLink linkA3 = new ProposalVideoLink(VALID_VIDEO_LINK_1);
            assertTrue(linkA1.equals(linkA2));
            assertTrue(linkA2.equals(linkA3));
            assertTrue(linkA1.equals(linkA3));

            ProposalVideoLink linkEmpty3 = new ProposalVideoLink(EMPTY_VIDEO_LINK);
            assertTrue(linkEmpty1.equals(linkEmpty2));
            assertTrue(linkEmpty2.equals(linkEmpty3));
            assertTrue(linkEmpty1.equals(linkEmpty3));
        }

        @Test
        @DisplayName("equals() returns true for equal video links")
        void equals_returnsTrueForEqualLinks() {
            assertEquals(linkA1, linkA2);
            assertEquals(linkEmpty1, linkEmpty2);
            assertEquals(ormLink1, ormLink2);
            assertEquals(linkEmpty1, ormLink1); // Empty string vs ORM default
        }

        @Test
        @DisplayName("equals() returns false for different video links")
        void equals_returnsFalseForDifferentLinks() {
            assertNotEquals(linkA1, linkB);
            assertNotEquals(linkA1, linkEmpty1);
        }

        @Test
        @DisplayName("equals() returns false for null object")
        void equals_returnsFalseForNull() {
            assertFalse(linkA1.equals(null));
            assertFalse(ormLink1.equals(null));
        }

        @Test
        @DisplayName("equals() returns false for object of different type")
        void equals_returnsFalseForDifferentType() {
            assertFalse(linkA1.equals(new Object()));
            assertFalse(ormLink1.equals(new Object()));
        }

        @Test
        @DisplayName("hashCode() is consistent for equal objects")
        void hashCode_isConsistentForEqualObjects() {
            assertEquals(linkA1.hashCode(), linkA2.hashCode());
            assertEquals(linkEmpty1.hashCode(), linkEmpty2.hashCode());
            assertEquals(ormLink1.hashCode(), ormLink2.hashCode());
            assertEquals(linkEmpty1.hashCode(), ormLink1.hashCode());
        }

        @Test
        @DisplayName("hashCode() generally differs for different objects")
        void hashCode_differsForDifferentObjects() {
            assertNotEquals(linkA1.hashCode(), linkB.hashCode());
            assertNotEquals(linkA1.hashCode(), linkEmpty1.hashCode());
        }
    }

    @Nested
    @DisplayName("compareTo() Method Tests")
    class CompareToTests {
        private final ProposalVideoLink link_A_lex = ProposalVideoLink.valueOf("http://example.com/a");
        private final ProposalVideoLink link_A_lex_same = ProposalVideoLink.valueOf("http://example.com/a");
        private final ProposalVideoLink link_B_lex_greater = ProposalVideoLink.valueOf("http://example.com/b");
        private final ProposalVideoLink link_C_lex_smaller = ProposalVideoLink.valueOf("http://example.com/aa"); // "aa" < "b"
        private final ProposalVideoLink link_Empty = ProposalVideoLink.valueOf("");
        private final ProposalVideoLink ormLink = new ProposalVideoLink(); // ""

        @Test
        @DisplayName("compareTo returns zero when video links are equal")
        void compareTo_returnsZeroWhenEqual() {
            assertEquals(0, link_A_lex.compareTo(link_A_lex_same));
            assertEquals(0, link_Empty.compareTo(ormLink));
        }

        @Test
        @DisplayName("compareTo: this less than other")
        void compareTo_thisLessThanOther() {
            assertTrue(link_C_lex_smaller.compareTo(link_B_lex_greater) < 0, "'http://example.com/aa' should be less than 'http://example.com/b'");
            assertTrue(link_A_lex.compareTo(link_B_lex_greater) < 0, "'http://example.com/a' should be less than 'http://example.com/b'");
            assertTrue(link_Empty.compareTo(link_A_lex) < 0, "Empty string should be less than non-empty string");
        }

        @Test
        @DisplayName("compareTo: this greater than other")
        void compareTo_thisGreaterThanOther() {
            assertTrue(link_B_lex_greater.compareTo(link_A_lex) > 0, "'http://example.com/b' should be greater than 'http://example.com/a'");
            assertTrue(link_A_lex.compareTo(link_Empty) > 0, "Non-empty string should be greater than empty string");
        }

        @Test
        @DisplayName("compareTo with null other throws NullPointerException")
        void compareTo_withNullOther_throwsNullPointerException() {
            // Accessing other.video will cause NPE if other is null
            assertThrows(NullPointerException.class, () -> link_A_lex.compareTo(null));
        }

        @Test
        @DisplayName("compareTo when this.video is null (not possible with current constructor logic) vs valid other")
        void compareTo_thisVideoIsNull_otherIsValid_throwsNullPointerException() {
            // This scenario is hard to test directly without reflection or changing the class,
            // as the constructor prevents `this.video` from being null.
            // If it were possible for `this.video` to be null:
            // ProposalVideoLink linkWithNullVideo = ... // somehow create with null video
            // assertThrows(NullPointerException.class, () -> linkWithNullVideo.compareTo(link_A_lex));
            // For now, this test is more of a conceptual check.
            // The current implementation `this.video.compareTo(other.video)` would throw NPE if `this.video` was null.
        }

        @Test
        @DisplayName("compareTo when other.video is null (not possible with current constructor logic)")
        void compareTo_otherVideoIsNull_throwsNullPointerException() {
            // Similar to the above, `other.video` cannot be null if `other` is a valid ProposalVideoLink.
            // The `compareTo(null)` test covers the case where `other` itself is null.
            // If `other.video` could be null while `other` is not, `this.video.compareTo(null)` would throw NPE.
        }
    }
}