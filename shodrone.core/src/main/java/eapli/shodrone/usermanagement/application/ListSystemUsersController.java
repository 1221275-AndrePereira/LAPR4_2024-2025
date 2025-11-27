package eapli.shodrone.usermanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.Optional;

/**
 * Controller for listing system users.
 * <p>
 * This class is responsible for handling the use case of listing system users in the application.
 * It provides methods to retrieve all users and find a specific user by their username.
 */
@UseCaseController
public class ListSystemUsersController {

    // Authorization service to check user permissions
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    // User management service to handle user-related operations
    private final UserManagementService userSvc = AuthzRegistry.userService();

    /**
     * Retrieves all system users.
     * <p>
     * @return an iterable collection of all system users
     */
        public Iterable<SystemUser> allUsers() {
            // Check if the authenticated user has the required permissions
            authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

            return userSvc.allUsers();
        }

    /**
     * Finds a system user by their username.
     * @param u the username of the system user to find
     * @return an Optional containing the found system user, or an empty Optional if no user was found
     */
        public Optional<SystemUser> find(final Username u) {
            return userSvc.userOfIdentity(u);
        }
}

