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

import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.shodrone.infrastructure.boostrapers.TestDataConstants;
import eapli.shodrone.shodroneusermanagement.application.AddShodroneUserController;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eapli.framework.actions.Action;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;

import java.util.HashSet;
import java.util.Set;

/**
 * Bootstrapper that demonstrates how a Shodrone User can be added to the
 * system directly.
 *
 *
 */
public class ShodroneUserBootstrapper implements Action {
    private static final Logger LOGGER = LogManager.getLogger(ShodroneUserBootstrapper.class);

    private final AddShodroneUserController addShodroneUserController = new AddShodroneUserController();

    /**
     * Default constructor.
     */
    @Override
    public boolean execute() {
        // Signup manager users. They will be created and active immediately.
        register(
                "manager",
                TestDataConstants.PASSWORD1,
                "Joao",
                "Ventura",
                "joao.ventura@shodrone.com",
                "210987654",
                "Rua de Cedofeita 567, 1 Andar",
                "4050-174",
                "Porto",
                TestDataConstants.USER_PHONE_NUMBER,
                ShodroneRoles.CRM_MANAGER
        );

        register(
                "maria.manager",
                TestDataConstants.PASSWORD1,
                "Maria",
                "Barbosa",
                "mary@shodrone.com",
                "220123456",
                "Avenida da Liberdade 200, Escritorio 3",
                "1250-142",
                "Lisboa",
                TestDataConstants.USER_PHONE_NUMBER,
                ShodroneRoles.CRM_MANAGER
        );

        // Signup collaborators users. They will be created and active immediately.
        register(
                "tiago.silva.collaborator",
                TestDataConstants.PASSWORD1,
                "Tiago",
                "Silva Sousa",
                "tiago.sousa@shodrone.com",
                "212345678",
                "Avenida dos Aliados 123, 3 Frente",
                "4000-064",
                "Porto",
                "+351919876543",
                ShodroneRoles.CRM_COLLABORATOR
        );

        register(
                "ana.pereira.collaborator",
                TestDataConstants.PASSWORD1,
                "Ana",
                "Pereira Gomes",
                "ana.gomes@shodrone.com",
                "234567890",
                "Rua de Santa Catarina 456, Loja B",
                "4000-442",
                "Porto",
                "+351921234567",
                ShodroneRoles.CRM_COLLABORATOR

        );

        // Signup customer users. They will be created and active immediately.
        register(
                "joao.costa.customer",
                TestDataConstants.PASSWORD1,
                "Joao",
                "Costa Almeida",
                "joao.almeida@shodrone.com",
                "256789012",
                "Praca da Liberdade 789, Escritorio 5",
                "4000-322",
                "Porto",
                "+351939876500",
                ShodroneRoles.CUSTOMER
        );

        register(
                "marta.silva.customer",
                TestDataConstants.PASSWORD1,
                "Marta",
                "Silva Ferreira",
                "marta.ferreira@shodrone.com",
                "267890123",
                "Rua de Camoes 123, 2 Andar",
                "4000-123",
                "Porto",
                "+351938765432",
                ShodroneRoles.CUSTOMER
        );

        register(
                "john.doe.dronetech",
                TestDataConstants.PASSWORD1,
                "John",
                "Doe",
                "johndoe@shodrone.com",
                "267890124",
                "Rua 123",
                "1234-567",
                "Porto",
                "+351938765432",
                ShodroneRoles.DRONE_TECH
        );

        return true;
    }

    /**
     * Helper method to register a Customer user.
     * With the new flow, all users are created directly.
     */
    private ShodroneUser register(
            final String username,
            final String password,
            final String firstName,
            final String lastName,
            final String email,
            final String vatNumber,
            final String streetAddress,
            final String postalCode,
            final String city,
            final String phoneNumber,
            final Role role
        )
    {
        ShodroneUser user = null;
        try {
            final Set<Role> roles = new HashSet<>();
            roles.add(role);

            user = addShodroneUserController.signup(username, password, firstName, lastName, email, vatNumber, streetAddress,
                    postalCode, city, phoneNumber, roles);

            LOGGER.info("Successfully registered user: {}", username);

        } catch (final ConcurrencyException | IntegrityViolationException e) {
            LOGGER.warn("Assuming {} already exists (activate trace log for details)", username);
            LOGGER.trace("Assuming existing record", e);
        }
        return user;
    }
}