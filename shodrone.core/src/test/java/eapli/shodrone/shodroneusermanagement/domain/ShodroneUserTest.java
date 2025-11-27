package eapli.shodrone.shodroneusermanagement.domain;

import eapli.framework.infrastructure.authz.domain.model.NilPasswordPolicy;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import eapli.shodrone.customerRepresentative.domain.CustomerRepresentative;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShodroneUserTest {

    private SystemUser mockSystemUser;
    private PhoneNumber validPhoneNumber;
    private VATNumber validVatNumber;
    private Address validAddress;
    private ShodroneUserPriority validPriority;
    private List<CustomerRepresentative> validCustomerRepresentatives;
    private CustomerRepresentative mockCustomerRepresentative;

    @BeforeEach
    void setUp() {
        SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
        userBuilder.with("testuser", "password", "Test", "User", "test@shodrone.com");
        mockSystemUser = userBuilder.build();


        validPhoneNumber = PhoneNumber.valueOf("+351912345678");
        validVatNumber = VATNumber.valueOf("123456789"); // Valid VAT
        validAddress = new Address("Main Street 123", "1234-567", "Test City");
        validPriority = ShodroneUserPriority.Regular;

        mockCustomerRepresentative = mock(CustomerRepresentative.class);
        validCustomerRepresentatives = new ArrayList<>();
        validCustomerRepresentatives.add(mockCustomerRepresentative);
    }

    @Test
    void ensureShodroneUserCanBeCreatedWithValidParameters() {
        // Act
        ShodroneUser shodroneUser = new ShodroneUser(
                mockSystemUser,
                validPhoneNumber,
                validVatNumber,
                validAddress,
                validPriority,
                validCustomerRepresentatives
        );

        // Assert
        assertNotNull(shodroneUser);
        assertEquals(mockSystemUser, shodroneUser.systemUser());
        assertEquals(validPhoneNumber, shodroneUser.phoneNumber());
        assertEquals(validVatNumber, shodroneUser.vatNumber());
        assertEquals(validAddress, shodroneUser.address());
        assertEquals(validPriority, shodroneUser.priority());
        assertEquals(validCustomerRepresentatives, shodroneUser.customerRepresentatives());
        assertEquals(validVatNumber, shodroneUser.identity(), "Identity should be the VAT number.");
    }

    @Test
    void ensureNullSystemUserUsernameThrowsIllegalArgumentException() {
        SystemUser userWithNullUsername = mock(SystemUser.class);
        when(userWithNullUsername.username()).thenReturn(null); // Explicitly mock username() to return null

        assertThrows(IllegalArgumentException.class, () -> new ShodroneUser(
                userWithNullUsername, validPhoneNumber, validVatNumber, validAddress, validPriority, validCustomerRepresentatives
        ));
    }


    @Test
    void ensureNullPhoneNumberThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShodroneUser(
                mockSystemUser, null, validVatNumber, validAddress, validPriority, validCustomerRepresentatives
        ));
    }

    @Test
    void ensureNullVatNumberThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShodroneUser(
                mockSystemUser, validPhoneNumber, null, validAddress, validPriority, validCustomerRepresentatives
        ));
    }

    @Test
    void ensureNullAddressThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShodroneUser(
                mockSystemUser, validPhoneNumber, validVatNumber, null, validPriority, validCustomerRepresentatives
        ));
    }

    @Test
    void ensureNullPriorityThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShodroneUser(
                mockSystemUser, validPhoneNumber, validVatNumber, validAddress, null, validCustomerRepresentatives
        ));
    }

    @Test
    void ensureEqualsAndHashCodeAreCorrect() {
        ShodroneUser user1 = new ShodroneUser(
                mockSystemUser, validPhoneNumber, validVatNumber, validAddress, validPriority, validCustomerRepresentatives
        );
        VATNumber differentVat = VATNumber.valueOf("987654321");
        SystemUser mockSystemUser2 = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder())
                .with("anotheruser", "password", "Another", "User", "another@shodrone.com").build();


        ShodroneUser user2 = new ShodroneUser(
                mockSystemUser, validPhoneNumber, validVatNumber, validAddress, validPriority, validCustomerRepresentatives
        );

        ShodroneUser user3 = new ShodroneUser(
                mockSystemUser2, validPhoneNumber, differentVat, validAddress, validPriority, validCustomerRepresentatives
        );

        assertEquals(user1, user2, "Two ShodroneUser objects with the same identity should be equal.");
        assertEquals(user1.hashCode(), user2.hashCode(), "Hashcodes of two equal ShodroneUser objects should be the same.");
        assertNotEquals(user1, user3, "ShodroneUser objects with different identities should not be equal.");
        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());
    }

    @Test
    void ensureSameAsBehavesCorrectly() {
        ShodroneUser user1 = new ShodroneUser(
                mockSystemUser, validPhoneNumber, validVatNumber, validAddress, validPriority, validCustomerRepresentatives
        );
        ShodroneUser user2 = new ShodroneUser(
                mockSystemUser, validPhoneNumber, validVatNumber, validAddress, validPriority, validCustomerRepresentatives
        );
        VATNumber differentVat = VATNumber.valueOf("987654321");
        SystemUser mockSystemUser2 = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder())
                .with("anotheruser", "password", "Another", "User", "another@shodrone.com").build();

        ShodroneUser user3 = new ShodroneUser(
                mockSystemUser2, validPhoneNumber, differentVat, validAddress, validPriority, validCustomerRepresentatives
        );

        assertTrue(user1.sameAs(user2), "sameAs should return true for objects with the same identity.");
        assertFalse(user1.sameAs(user3), "sameAs should return false for objects with different identities.");
        assertFalse(user1.sameAs(null));
        assertFalse(user1.sameAs(new Object()));
    }

    @Test
    void ensureIdentityReturnsVatNumber() {
        ShodroneUser shodroneUser = new ShodroneUser(
                mockSystemUser,
                validPhoneNumber,
                validVatNumber,
                validAddress,
                validPriority,
                validCustomerRepresentatives
        );
        assertEquals(validVatNumber, shodroneUser.identity());
    }

    @Test
    void ensureVatNumberMethodReturnsCorrectVatNumber() {
        ShodroneUser shodroneUser = new ShodroneUser(
                mockSystemUser,
                validPhoneNumber,
                validVatNumber,
                validAddress,
                validPriority,
                validCustomerRepresentatives
        );
        assertEquals(validVatNumber, shodroneUser.vatNumber());
    }

    @Test
    void ensureOrmConstructorExistsAndIsAccessible() {

        try {
            ShodroneUser user = ShodroneUser.class.getDeclaredConstructor().newInstance();
            assertNotNull(user);

            assertNull(user.systemUser());
            assertNull(user.phoneNumber());
            assertNull(user.vatNumber());
            assertNull(user.address());
            assertNull(user.priority());
            assertNotNull(user.customerRepresentatives());
            assertTrue(user.customerRepresentatives().isEmpty());
        } catch (Exception e) {
            fail("Failed to instantiate ShodroneUser using no-arg constructor: " + e.getMessage());
        }
    }
}
