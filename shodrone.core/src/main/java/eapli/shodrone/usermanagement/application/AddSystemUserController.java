package eapli.shodrone.usermanagement.application;


import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.time.util.CurrentTimeCalendars;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.Calendar;
import java.util.Set;

/**
 * Controller for adding a new system user.
 * <p>
 * This class is responsible for handling the logic of adding a new system user to the system.
 * It ensures that the authenticated user has the necessary permissions to perform this action.
 */
public class AddSystemUserController {

    // Authorization service to check user permissions
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    // User management service to handle user-related operations
    private final UserManagementService userSvc = AuthzRegistry.userService();

    /**
     * Retrieves the available role types for admins so that they can assign roles to new users.
     *
     * @return an iterable collection of role types for non-admin users
     */
    public Iterable<Role> getRoleTypesAdmin() {
        return ShodroneRoles.nonUserValuesAdmin();
    }

    /**
     * Retrieves the available role types for non-admins so that they can assign roles to new users.
     *
     * @return an iterable collection of role types for non-admin users
     */
    public Iterable<Role> getRoleTypesNonAdmin() {
        return ShodroneRoles.nonUserValuesNoAdmin();
    }

    /**
     * Adds a new user to the system with the specified details.
     *
     * @param username  the username of the new user
     * @param password  the password of the new user
     * @param firstName the first name of the new user
     * @param lastName  the last name of the new user
     * @param email     the email address of the new user
     * @param roles     the roles assigned to the new user
     * @return the newly created SystemUser object
     */
    public SystemUser addUser(String username, String password, String firstName, String lastName, String email, Set<Role> roles) {

        return addUser(
                username,
                password,
                firstName,
                lastName,
                email,
                roles,
                CurrentTimeCalendars.now());
    }

    /**
     * Adds a new user to the system with the specified details and creation date.
     *
     * @param username  the username of the new user
     * @param password  the password of the new user
     * @param firstName the first name of the new user
     * @param lastName  the last name of the new user
     * @param email     the email address of the new user
     * @param roles     the roles assigned to the new user
     * @param createdOn the creation date of the new user
     * @return the newly created SystemUser object
     */
    public SystemUser addUser(String username, String password, String firstName, String lastName, String email, Set<Role> roles, Calendar createdOn) {

        // Ensure the authenticated user has the necessary permissions to add a new user
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_MANAGER);

        return userSvc.registerNewUser(
                username,
                password,
                firstName,
                lastName,
                email,
                roles,
                createdOn);
    }

}
