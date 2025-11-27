package eapli.shodrone.usermanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

/**
 * Controller for deactivating and activating system users.
 * <p>
 * This controller provides methods to deactivate and activate system users, as well as retrieve
 * lists of active and inactive users. It ensures that only authorized users can perform these
 * actions.
 */
@UseCaseController
public class DeactivateActiveSystemUserController {

    // Authorization service to check user permissions
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    // User management service to handle user-related operations
    private final UserManagementService userSvc = AuthzRegistry.userService();

    /**
     * Deactivates a system user.
     *
     * @param user the system user to deactivate
     * @return the deactivated system user
     */
    public SystemUser deactivateUser(final SystemUser user) {
        // Ensure the user has the required permissions to deactivate a user
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

        return userSvc.deactivateUser(user);
    }

    /**
     * Activates a system user.
     *
     * @param user the system user to activate
     * @return the activated system user
     */
    public SystemUser activateUser(final SystemUser user) {
        // Ensure the user has the required permissions to activate a user
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

        return userSvc.activateUser(user);
    }

    /**
     * Retrieves a list of active system users.
     *
     * @return an iterable collection of active system users
     */
    public Iterable<SystemUser> activeUsers() {
        // Ensure the user has the required permissions to access active users
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

        return userSvc.activeUsers();
    }

    /**
     * Retrieves a list of inactive system users.
     *
     * @return an iterable collection of inactive system users
     */
    public Iterable<SystemUser> inactiveUsers() {
        // Ensure the user has the required permissions to access inactive users
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

        return userSvc.deactivatedUsers();
    }
}
