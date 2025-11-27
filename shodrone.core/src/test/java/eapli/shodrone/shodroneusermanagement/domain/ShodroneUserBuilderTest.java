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

class ShodroneUserBuilderTest {

    private ShodroneUserBuilder builder;
    private SystemUser mockSystemUser;
    private PhoneNumber validPhoneNumber;
    private String validPhoneNumberStr;
    private VATNumber validVatNumber;
    private String validVatNumberStr;
    private Address validAddress;
    private String validStreetAddressStr;
    private String validPostalCodeStr;
    private String validCityStr;
    private ShodroneUserPriority specificPriority;
    private CustomerRepresentative mockCustomerRepresentative1;
    private CustomerRepresentative mockCustomerRepresentative2;

    @BeforeEach
    void setUp() {
        builder = new ShodroneUserBuilder();

        SystemUserBuilder systemUserBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
        systemUserBuilder.with("testbuilderuser", "passwordB1", "Builder", "User", "builder@shodrone.com");
        mockSystemUser = systemUserBuilder.build();

        validPhoneNumberStr = "+351911222333";
        validPhoneNumber = PhoneNumber.valueOf(validPhoneNumberStr);

        validVatNumberStr = "234567890"; // Valid VAT
        validVatNumber = VATNumber.valueOf(validVatNumberStr);

        validStreetAddressStr = "Builder Avenue 456";
        validPostalCodeStr = "4567-890";
        validCityStr = "BuildVille";
        validAddress = new Address(validStreetAddressStr, validPostalCodeStr, validCityStr);

        specificPriority = ShodroneUserPriority.VIP;

        mockCustomerRepresentative1 = mock(CustomerRepresentative.class);
        mockCustomerRepresentative2 = mock(CustomerRepresentative.class);
    }

    @Test
    void ensureBuildWithAllParametersSetCorrectly() {
        // Arrange
        List<CustomerRepresentative> representatives = new ArrayList<>();
        representatives.add(mockCustomerRepresentative1);

        // Act
        ShodroneUser user = builder
                .withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withVATNumber(validVatNumber)
                .withAddress(validAddress)
                .withPriority(specificPriority)
                .withCustomerRepresentatives(representatives)
                .build();

        // Assert
        assertNotNull(user);
        assertEquals(mockSystemUser, user.systemUser());
        assertEquals(validPhoneNumber, user.phoneNumber());
        assertEquals(validVatNumber, user.vatNumber());
        assertEquals(validAddress, user.address());
        assertEquals(specificPriority, user.priority());
        assertEquals(representatives, user.customerRepresentatives());
        assertEquals(1, user.customerRepresentatives().size());
        assertSame(mockCustomerRepresentative1, user.customerRepresentatives().get(0));
    }

    @Test
    void ensureBuildWithStringParametersForValueObjects() {
        // Arrange
        List<CustomerRepresentative> representatives = new ArrayList<>();
        representatives.add(mockCustomerRepresentative1);

        // Act
        ShodroneUser user = builder
                .withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumberStr)
                .withVATNumber(validVatNumberStr)
                .withAddress(validStreetAddressStr, validPostalCodeStr, validCityStr)
                .withPriority(specificPriority)
                .withCustomerRepresentatives(representatives)
                .build();

        // Assert
        assertNotNull(user);
        assertEquals(mockSystemUser, user.systemUser());
        assertEquals(validPhoneNumber.toString(), user.phoneNumber().toString());
        assertEquals(validVatNumber.toString(), user.vatNumber().toString());
        assertEquals(validAddress, user.address());
        assertEquals(specificPriority, user.priority());
        assertEquals(representatives, user.customerRepresentatives());
    }

    @Test
    void ensureBuildAssignsDefaultPriorityWhenNotSet() {
        // Act
        ShodroneUser user = builder
                .withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withVATNumber(validVatNumber)
                .withAddress(validAddress)
                .withCustomerRepresentatives(new ArrayList<>())
                .build();

        // Assert
        assertNotNull(user);
        assertEquals(ShodroneUserPriority.Regular, user.priority(), "Default priority should be Regular.");
    }

    @Test
    void ensureWithCustomerRepresentativesListSetsCorrectly() {
        // Arrange
        List<CustomerRepresentative> representatives = new ArrayList<>();
        representatives.add(mockCustomerRepresentative1);
        representatives.add(mockCustomerRepresentative2);

        // Act
        ShodroneUser user = builder
                .withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withVATNumber(validVatNumber)
                .withAddress(validAddress)
                .withCustomerRepresentatives(representatives)
                .build();

        // Assert
        assertNotNull(user);
        assertEquals(representatives, user.customerRepresentatives());
        assertEquals(2, user.customerRepresentatives().size());
        assertTrue(user.customerRepresentatives().contains(mockCustomerRepresentative1));
        assertTrue(user.customerRepresentatives().contains(mockCustomerRepresentative2));
    }

    @Test
    void ensureWithCustomerRepresentativesSingleObjectAddsToList() {
        ShodroneUserBuilder newBuilder = new ShodroneUserBuilder();
        newBuilder.withCustomerRepresentatives(new ArrayList<>());

        // Act
        newBuilder.withCustomerRepresentatives(mockCustomerRepresentative1);
        newBuilder.withCustomerRepresentatives(mockCustomerRepresentative2);

        ShodroneUser user = newBuilder
                .withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withVATNumber(validVatNumber)
                .withAddress(validAddress)
                .build();


        // Assert
        assertNotNull(user);
        assertNotNull(user.customerRepresentatives());
        assertEquals(2, user.customerRepresentatives().size());
        assertTrue(user.customerRepresentatives().contains(mockCustomerRepresentative1));
        assertTrue(user.customerRepresentatives().contains(mockCustomerRepresentative2));
    }

    @Test
    void ensureBuildFailsIfRequiredParametersForShodroneUserConstructorAreMissing() {
        ShodroneUserBuilder builderMissingSystemUser = new ShodroneUserBuilder()
                .withPhoneNumber(validPhoneNumber)
                .withVATNumber(validVatNumber)
                .withAddress(validAddress)
                .withPriority(specificPriority)
                .withCustomerRepresentatives(new ArrayList<>());

        ShodroneUserBuilder builderMissingPhone = new ShodroneUserBuilder()
                .withSystemUser(mockSystemUser)
                .withVATNumber(validVatNumber)
                .withAddress(validAddress)
                .withPriority(specificPriority)
                .withCustomerRepresentatives(new ArrayList<>());

        ShodroneUserBuilder builderMissingVAT = new ShodroneUserBuilder()
                .withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withAddress(validAddress)
                .withPriority(specificPriority)
                .withCustomerRepresentatives(new ArrayList<>());

        ShodroneUserBuilder builderMissingAddress = new ShodroneUserBuilder()
                .withSystemUser(mockSystemUser)
                .withPhoneNumber(validPhoneNumber)
                .withVATNumber(validVatNumber)
                .withPriority(specificPriority)
                .withCustomerRepresentatives(new ArrayList<>());
    }
}
