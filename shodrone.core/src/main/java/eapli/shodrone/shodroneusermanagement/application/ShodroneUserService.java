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
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.Optional;

/**
 * Service to manage Shodrone users.
 * <p>
 * This service is responsible for managing Shodrone users, including
 * creating, updating, and deleting users, as well as retrieving user
 * information.
 */
public class ShodroneUserService {

    // The service is responsible for managing Shodrone users
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    // The repository is responsible for accessing the data store
    private final ShodroneUserRepository repo = PersistenceContext.repositories().shodroneUsers();

    /**
     * Finds a Shodrone user by their username.
     *
     * @param user The username of the Shodrone user to find.
     * @return An Optional containing the Shodrone user if found, or an empty Optional if not found.
     *
     */
    public Optional<ShodroneUser> findShodroneUserByUsername(Username user) {
        // Ensure the authenticated user has the required roles to access this method
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_MANAGER);
        return repo.findByUsername(user);
    }

    /**
     * Finds a Shodrone user by their VAT number.
     *
     * @param vatNumber The VAT number of the Shodrone user to find.
     * @return An Optional containing the Shodrone user if found, or an empty Optional if not found.
     */
    public Optional<ShodroneUser> findShodroneUserByVatNumber(VATNumber vatNumber) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_MANAGER);
        return repo.findByVatNumber(vatNumber);
    }
}
