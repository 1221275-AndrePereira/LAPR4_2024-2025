package eapli.shodrone.usermanagement.domain;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserBuilderHelperTest {

    @Test
    void ensureBuilderReturnsInstanceOfSystemUserBuilder() {
        SystemUserBuilder builder = UserBuilderHelper.builder();


        assertNotNull(builder, "The builder() method should not return null.");
        assertTrue(builder instanceof SystemUserBuilder, "The returned object should be an instance of SystemUserBuilder.");
    }

    @Test
    void ensureBuilderIsLikelyConfiguredCorrectly() {
        SystemUserBuilder builder = UserBuilderHelper.builder();
        assertNotNull(builder);

         try {
             SystemUser user = builder.withUsername("testuser")
                                     .withPassword("ValidPass1")

                                     .withName("Test", "User")
                                     .withEmail("test@shodrone.com")
                                     .withRoles(ShodroneRoles.SHODRONE_USER)
                                     .build();
             assertNotNull(user);
         } catch (Exception e) {
             fail("Building a user with the helper-provided builder should not fail with valid inputs, " +
                  "assuming ShodronePasswordPolicy and PlainTextEncoder are correctly set up: " + e.getMessage());
         }
    }
}