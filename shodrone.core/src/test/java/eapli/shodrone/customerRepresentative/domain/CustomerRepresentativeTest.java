package eapli.shodrone.customerRepresentative.domain;

import eapli.framework.infrastructure.authz.domain.model.NilPasswordPolicy;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import eapli.shodrone.shodroneusermanagement.domain.PhoneNumber; // Assuming PhoneNumber is in this package
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepresentativeTest {

    private SystemUser mockSystemUser;
    private PhoneNumber validPhoneNumber;
    private CompanyEmail validCompanyEmail;

    @BeforeEach
    void setUp() {
        SystemUserBuilder systemUserBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
        systemUserBuilder.with("repUser", "password", "Rep", "User", "rep@shodrone.com");
        mockSystemUser = systemUserBuilder.build();

        validPhoneNumber = PhoneNumber.valueOf("+351912345678");
        validCompanyEmail = CompanyEmail.valueOf("rep.contact@company.com");
    }

    @Test
    void ensureCustomerRepresentativeCanBeCreatedWithValidParameters() {
        // Act
        CustomerRepresentative representative = new CustomerRepresentative(
                mockSystemUser,
                validPhoneNumber,
                validCompanyEmail
        );

        // Assert
        assertNotNull(representative);
        assertEquals(mockSystemUser, representative.systemUser());
        assertEquals(validPhoneNumber, representative.phoneNumber());
        assertEquals(validCompanyEmail, representative.companyEmail());
        assertNull(representative.identity(), "ID should be null for a new, unpersisted entity.");
    }

    @Test
    void ensureNullSystemUserThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CustomerRepresentative(
                null, validPhoneNumber, validCompanyEmail
        ));
    }

    @Test
    void ensureNullPhoneNumberThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CustomerRepresentative(
                mockSystemUser, null, validCompanyEmail
        ));
    }

    @Test
    void ensureNullCompanyEmailThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CustomerRepresentative(
                mockSystemUser, validPhoneNumber, null
        ));
    }

    // Test getters
    @Test
    void systemUserGetterReturnsCorrectUser() {
        CustomerRepresentative representative = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        assertSame(mockSystemUser, representative.systemUser());
    }

    @Test
    void phoneNumberGetterReturnsCorrectNumber() {
        CustomerRepresentative representative = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        assertSame(validPhoneNumber, representative.phoneNumber());
    }

    @Test
    void companyEmailGetterReturnsCorrectEmail() {
        CustomerRepresentative representative = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        assertSame(validCompanyEmail, representative.companyEmail());
    }

    @Test
    void ensureEqualsAndHashCodeBehaveCorrectlyForPersistedEntitiesWithSameId() throws NoSuchFieldException, IllegalAccessException {
        CustomerRepresentative rep1 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        CustomerRepresentative rep2 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);

        Field idField = CustomerRepresentative.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(rep1, 1L);
        idField.set(rep2, 1L);

        assertEquals(rep1, rep2, "Persisted entities with the same ID should be equal.");
        assertEquals(rep1.hashCode(), rep2.hashCode(), "Hashcodes of persisted entities with the same ID should be equal.");
    }

    @Test
    void ensureEqualsAndHashCodeBehaveCorrectlyForPersistedEntitiesWithDifferentId() throws NoSuchFieldException, IllegalAccessException {
        CustomerRepresentative rep1 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        CustomerRepresentative rep2 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);

        Field idField = CustomerRepresentative.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(rep1, 1L);
        idField.set(rep2, 2L); // Different ID

        assertNotEquals(rep1, rep2, "Persisted entities with different IDs should not be equal.");
    }


    // Test sameAs method
    @Test
    void ensureSameAsBehavesCorrectlyForUnpersistedEntities() {
        CustomerRepresentative rep1 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);

        assertTrue(rep1.sameAs(rep1), "An instance should be sameAs itself.");
        assertFalse(rep1.sameAs(null));
        assertFalse(rep1.sameAs(new Object()));
    }

    @Test
    void ensureSameAsBehavesCorrectlyForPersistedEntitiesWithSameId() throws NoSuchFieldException, IllegalAccessException {
        CustomerRepresentative rep1 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        CustomerRepresentative rep2 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);

        Field idField = CustomerRepresentative.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(rep1, 1L);
        idField.set(rep2, 1L);

        assertTrue(rep1.sameAs(rep2), "Persisted entities with the same ID should be sameAs.");
    }

    @Test
    void ensureSameAsBehavesCorrectlyForPersistedEntitiesWithDifferentId() throws NoSuchFieldException, IllegalAccessException {
        CustomerRepresentative rep1 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        CustomerRepresentative rep2 = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);

        Field idField = CustomerRepresentative.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(rep1, 1L);
        idField.set(rep2, 2L);

        assertFalse(rep1.sameAs(rep2), "Persisted entities with different IDs should not be sameAs.");
    }

    // Test identity method
    @Test
    void ensureIdentityReturnsNullForNewEntity() {
        CustomerRepresentative representative = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        assertNull(representative.identity(), "Identity (ID) should be null for a new, unpersisted entity.");
    }

    @Test
    void ensureIdentityReturnsIdAfterSetting() throws NoSuchFieldException, IllegalAccessException {
        CustomerRepresentative representative = new CustomerRepresentative(mockSystemUser, validPhoneNumber, validCompanyEmail);
        Long expectedId = 123L;

        Field idField = CustomerRepresentative.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(representative, expectedId);

        assertEquals(expectedId, representative.identity(), "Identity should return the set ID.");
    }

    // Test ORM constructor
    @Test
    void ensureOrmConstructorExistsAndInitializesFieldsToNull() {
        try {
            CustomerRepresentative representative = CustomerRepresentative.class.getDeclaredConstructor().newInstance();
            assertNotNull(representative);
            assertNull(representative.systemUser());
            assertNull(representative.phoneNumber());
            assertNull(representative.companyEmail());
            assertNull(representative.identity());
        } catch (Exception e) {
            fail("Failed to instantiate CustomerRepresentative using no-arg constructor: " + e.getMessage());
        }
    }
}
