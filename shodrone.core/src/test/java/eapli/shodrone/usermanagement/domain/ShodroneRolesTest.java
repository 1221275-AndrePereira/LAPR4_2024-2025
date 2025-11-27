package eapli.shodrone.usermanagement.domain;

import eapli.framework.infrastructure.authz.domain.model.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShodroneRolesTest {

    @Test
    void ensureDefinedRolesAreCorrectlyInitialized() {
        assertEquals(Role.valueOf("POWER_USER"), ShodroneRoles.POWER_USER);
        assertEquals(Role.valueOf("ADMIN"), ShodroneRoles.ADMIN);
        assertEquals(Role.valueOf("SHODRONE_USER"), ShodroneRoles.SHODRONE_USER);
        assertEquals(Role.valueOf("CRM_MANAGER"), ShodroneRoles.CRM_MANAGER);
        assertEquals(Role.valueOf("CRM_COLLABORATOR"), ShodroneRoles.CRM_COLLABORATOR);
        assertEquals(Role.valueOf("SHOW_DESIGNER"), ShodroneRoles.SHOW_DESIGNER);
        assertEquals(Role.valueOf("CUSTOMER_REPRESENTATIVE"), ShodroneRoles.CUSTOMER_REPRESENTATIVE);
    }

    @Test
    void ensureNonUserValuesAdminReturnsExpectedRoles() {
        List<Role> expected = List.of(
                ShodroneRoles.ADMIN,
                ShodroneRoles.CRM_MANAGER,
                ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.SHOW_DESIGNER
        );

        Iterable<Role> actual = ShodroneRoles.nonUserValuesAdmin();
        for (Role role : expected) {
            assertTrue(containsRole(actual, role));
        }
    }

    @Test
    void ensureNonUserValuesNoAdminReturnsExpectedRoles() {
        List<Role> expected = List.of(
                ShodroneRoles.CRM_MANAGER,
                ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.SHOW_DESIGNER
        );

        Iterable<Role> actual = ShodroneRoles.nonUserValuesNoAdmin();
        for (Role role : expected) {
            assertTrue(containsRole(actual, role));
        }

        // Ensure ADMIN is not included
        assertFalse(containsRole(actual, ShodroneRoles.ADMIN));
    }

    private boolean containsRole(Iterable<Role> roles, Role target) {
        for (Role r : roles) {
            if (r.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
