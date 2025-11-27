package eapli.shodrone.customerRepresentative.domain;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.shodroneusermanagement.domain.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CustomerRepresentativeBuilderTest {

    private CustomerRepresentativeBuilder builder;
    private SystemUser mockSystemUser;
    private PhoneNumber validPhoneNumber;
    private CompanyEmail validCompanyEmail;

    @BeforeEach
    void setUp() {
        builder = new CustomerRepresentativeBuilder();
        mockSystemUser = mock(SystemUser.class); // Mocking SystemUser
        // Assuming PhoneNumber and CompanyEmail have valid constructors for these strings
        validPhoneNumber = PhoneNumber.valueOf("+351912345678");
        validCompanyEmail = CompanyEmail.valueOf("rep@example.com");
    }

    @Test
    void build_withAllPropertiesSetUsingObjects_createsRepresentative() {
        // Arrange
        builder.withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withCompanyEmail(validCompanyEmail);

        // Act
        CustomerRepresentative representative = builder.build();

        // Assert
        assertNotNull(representative);
        assertEquals(mockSystemUser, representative.systemUser(), "SystemUser should match.");
        assertEquals(validPhoneNumber, representative.phoneNumber(), "PhoneNumber should match.");
        assertEquals(validCompanyEmail, representative.companyEmail(), "CompanyEmail should match.");
    }

    @Test
    void build_withAllPropertiesSetUsingStrings_createsRepresentative() {
        // Arrange
        String phoneNumberStr = "+351966555444";
        String companyEmailStr = "test.rep@company.com";

        builder.withSystemUser(mockSystemUser)
                .withPhoneNumber(phoneNumberStr)
                .withCompanyEmail(companyEmailStr);

        // Act
        CustomerRepresentative representative = builder.build();

        // Assert
        assertNotNull(representative);
        assertEquals(mockSystemUser, representative.systemUser());
        assertEquals(phoneNumberStr, representative.phoneNumber().toString(), "Phone number string should match.");
        assertEquals(companyEmailStr, representative.companyEmail().toString(), "Company email string should match.");
    }

    @Test
    void build_withoutSystemUser_throwsExceptionViaConstructor() {
        // Arrange
        builder.withPhoneNumber(validPhoneNumber)
                .withCompanyEmail(validCompanyEmail);

        // Act & Assert
        // The CustomerRepresentative constructor will throw due to Preconditions.noneNull
        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        }, "Building without SystemUser should trigger Precondition in CustomerRepresentative constructor.");
    }

    @Test
    void build_withoutPhoneNumber_throwsExceptionViaConstructor() {
        // Arrange
        builder.withSystemUser(mockSystemUser)
                .withCompanyEmail(validCompanyEmail);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        }, "Building without PhoneNumber should trigger Precondition in CustomerRepresentative constructor.");
    }

    @Test
    void build_withoutCompanyEmail_throwsExceptionViaConstructor() {
        // Arrange
        builder.withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        }, "Building without CompanyEmail should trigger Precondition in CustomerRepresentative constructor.");
    }

    @Test
    void withSystemUser_setsSystemUserCorrectly() {
        // Arrange
        builder.withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber) // Need other valid fields for build to succeed
                .withCompanyEmail(validCompanyEmail);

        // Act
        CustomerRepresentative representative = builder.build();

        // Assert
        assertEquals(mockSystemUser, representative.systemUser());
    }

    @Test
    void withPhoneNumber_object_setsPhoneNumberCorrectly() {
        // Arrange
        builder.withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withCompanyEmail(validCompanyEmail);

        // Act
        CustomerRepresentative representative = builder.build();

        // Assert
        assertEquals(validPhoneNumber, representative.phoneNumber());
    }

    @Test
    void withPhoneNumber_string_setsPhoneNumberCorrectly() {
        // Arrange
        String phoneNumberStr = "+351933333333";
        builder.withSystemUser(mockSystemUser)
                .withPhoneNumber(phoneNumberStr)
                .withCompanyEmail(validCompanyEmail);

        // Act
        CustomerRepresentative representative = builder.build();

        // Assert
        assertEquals(phoneNumberStr, representative.phoneNumber().toString());
    }

    @Test
    void withCompanyEmail_object_setsCompanyEmailCorrectly() {
        // Arrange
        builder.withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withCompanyEmail(validCompanyEmail);

        // Act
        CustomerRepresentative representative = builder.build();

        // Assert
        assertEquals(validCompanyEmail, representative.companyEmail());
    }

    @Test
    void withCompanyEmail_string_setsCompanyEmailCorrectly() {
        // Arrange
        String companyEmailStr = "another.rep@domain.org";
        builder.withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withCompanyEmail(companyEmailStr);

        // Act
        CustomerRepresentative representative = builder.build();

        // Assert
        assertEquals(companyEmailStr, representative.companyEmail().toString());
    }

    @Test
    void builderMethodsReturnSameBuilderInstanceForChaining() {
        CustomerRepresentativeBuilder instance1 = builder.withSystemUser(mockSystemUser);
        CustomerRepresentativeBuilder instance2 = builder.withPhoneNumber(validPhoneNumber);
        CustomerRepresentativeBuilder instance3 = builder.withCompanyEmail(validCompanyEmail);

        assertSame(builder, instance1, "withSystemUser should return the same builder instance.");
        assertSame(builder, instance2, "withPhoneNumber should return the same builder instance.");
        assertSame(builder, instance3, "withCompanyEmail should return the same builder instance.");
    }
}
