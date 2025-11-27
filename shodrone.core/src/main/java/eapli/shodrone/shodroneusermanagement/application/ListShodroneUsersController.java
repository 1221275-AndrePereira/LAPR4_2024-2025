/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eapli.shodrone.shodroneusermanagement.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.customerRepresentative.domain.CustomerRepresentative;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.Optional;

/**
 * Controller for listing Shodrone users.
 * <p>
 * This class is responsible for handling the logic of listing Shodrone users
 * and ensuring that the user has the necessary permissions to perform this
 * action.
 */
public class ListShodroneUsersController {
    // The authorization service is used to check if the user has the necessary permissions
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    // The repository for Shodrone users is used to access the data
    private final ShodroneUserRepository repo = PersistenceContext.repositories().shodroneUsers();

    /**
     * Lists all Shodrone users.
     * <p>
     * This method checks if the authenticated user has the necessary permissions
     * to list Shodrone users. If the user does not have the required permissions,
     * an exception is thrown.
     *
     * @return An iterable collection of Shodrone users.
     */
    public Iterable<ShodroneUser> listShodroneUsers() {
        // Check if the authenticated user has the necessary permissions
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_MANAGER);
        return repo.findAll();
    }

    /**
     * Lists all active Shodrone users.
     * <p>
     * This method checks if the authenticated user has the necessary permissions
     * to list active Shodrone users. If the user does not have the required
     * permissions, an exception is thrown.
     *
     * @return An iterable collection of active Shodrone users.
     */
    public Iterable<ShodroneUser> activeClientUsers() {
        // Check if the authenticated user has the necessary permissions
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

        return this.repo.findAllActive();
    }

    public Optional<ShodroneUser> findCustomerByRepresentative(Username user) {
        return this.repo.findCustomerByRepresentative(user);
    }

    public Optional<ShodroneUser> findShodroneUserByUsername(Username user) {
        return this.repo.findByUsername(user);
    }
}
