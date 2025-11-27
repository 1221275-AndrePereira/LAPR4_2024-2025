/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eapli.shodrone.infrastructure.boostrapers.demo;


import eapli.framework.actions.Action;
import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.shodrone.infrastructure.boostrapers.UsersBootstrapperBase;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.HashSet;
import java.util.Set;

/**
 * Bootstrapper for the basic backoffice users.
 * <p>
 * This class is responsible for creating the backoffice users in the system. It creates a customer
 * and a manager with the same password.
 *
 */
public class BackofficeUsersBootstrapper extends UsersBootstrapperBase implements Action {

    @SuppressWarnings("squid:S2068")
    private static final String PASSWORD1 = "123Qwe#";

    /**
     * Default constructor.
     */
    @Override
    public boolean execute() {
        registerCustomer("customer", PASSWORD1, "João", "Customer","customer@shodrone.com");
        registerManager("manager", PASSWORD1, "João", "Manager","manager@shodrone.com");
        return true;
    }

    /**
     * Register a Manager user in the system.
     *
     * @param username  the username of the user
     * @param password  the password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     */
    private void registerManager(final String username,final String password,final String firstName,final String lastName,final String email) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.CRM_MANAGER);

        registerUser(username, password, firstName, lastName,email, roles);
    }

    /**
     * Register a Customer user in the system.
     *
     * @param username  the username of the user
     * @param password  the password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     */
    private void registerCustomer(final String username,final String password,final String firstName,final String lastName,final String email) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.CRM_COLLABORATOR);

        registerUser(username, password, firstName, lastName,email, roles);
    }
}
