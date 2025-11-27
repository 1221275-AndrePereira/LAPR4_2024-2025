package eapli.shodrone.usermanagement.domain;

import eapli.framework.infrastructure.authz.domain.model.Role;

import java.util.List;

/**
 * This class contains the roles used in the Shodrone system.
 * <p>
 * The roles are used to control access to the system and its features.
 * <p>
 * The roles are defined as constants in this class.
 * <p>
 */
public final class ShodroneRoles {

    /**
     * Power User.
     */
    public static final Role POWER_USER = Role.valueOf("POWER_USER");

    /**
     * System Administrator.
     */
    public static final Role ADMIN = Role.valueOf("ADMIN");


    /**
     * Utente.
     */
    public static final Role SHODRONE_USER = Role.valueOf("SHODRONE_USER");

    public static final Role CRM_MANAGER = Role.valueOf("CRM_MANAGER");

    public static final Role CRM_COLLABORATOR = Role.valueOf("CRM_COLLABORATOR");

    public static final Role SHOW_DESIGNER = Role.valueOf("SHOW_DESIGNER");

    public static final Role CUSTOMER = Role.valueOf("CUSTOMER");

    public static final Role CUSTOMER_REPRESENTATIVE = Role.valueOf("CUSTOMER_REPRESENTATIVE");

    public static final Role DRONE_TECH=Role.valueOf("DRONE_TECH");

    /**
     * Get all available roles to create a User for an admin
     */
    public static Iterable<Role> nonUserValuesAdmin() {
        return List.of(new Role[] {ADMIN, CRM_MANAGER, CRM_COLLABORATOR, SHOW_DESIGNER, CUSTOMER,DRONE_TECH});
    }

    /**
     * Get all available roles to create a User for a non-admin user
     *
     */
    public static Iterable<Role> nonUserValuesNoAdmin() {
        return List.of(new Role[] {CRM_MANAGER, CRM_COLLABORATOR, SHOW_DESIGNER, CUSTOMER,DRONE_TECH});
    }

}
