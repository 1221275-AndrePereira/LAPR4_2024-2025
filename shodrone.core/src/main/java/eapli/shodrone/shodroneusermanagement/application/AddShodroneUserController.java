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

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.time.util.CurrentTimeCalendars;
import eapli.shodrone.customerRepresentative.application.CustomerRepresentativeDefaultValues;
import eapli.shodrone.customerRepresentative.domain.CustomerRepresentative;
import eapli.shodrone.customerRepresentative.domain.CustomerRepresentativeBuilder;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.*; // ShodroneUserBuilder is here
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import eapli.shodrone.usermanagement.dto.SystemUserDetailsForRepresentative;

import java.util.*;

/**
 * Controller for the use case of adding a new Shodrone user.
 * <p>
 * This class is responsible for handling the business logic related to the
 * creation of a new Shodrone user. It interacts with the UserManagementService
 * and the ShodroneUserRepository to perform the necessary operations.
 */
@UseCaseController
public class AddShodroneUserController {

	// The UserManagementService is responsible for user management operations
	private final UserManagementService userSvc = AuthzRegistry.userService();
	// The ShodroneUserRepository is responsible for data access operations
	private final ShodroneUserRepository shodroneUserRepository = PersistenceContext.repositories().shodroneUsers();

	/**
	 * Creates a new Shodrone user with the provided details.s
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param vatNumberInput
	 * @param streetAddress
	 * @param postalCode
	 * @param city
	 * @param phoneNumberInput
	 * @param createdOn
	 * @param roles
	 * @return The newly created Shodrone user.
	 *
	 * This method is responsible for creating a new Shodrone user with the
	 * provided details. It first registers a new system user using the
	 * UserManagementService, and then creates a new ShodroneUser
	 * object with the provided
	 * details. The method also creates a new
	 * CustomerRepresentative object and associates it with the
	 * ShodroneUser. Finally, it saves the
	 * newly created ShodroneUser to the
	 * repository and returns it.
	 */
	public ShodroneUser signup(final String username, final String password, final String firstName,
							   final String lastName, final String email, final String vatNumberInput,
							   final String streetAddress, final String postalCode, final String city,
							   final String phoneNumberInput, final Calendar createdOn, final Set<Role> roles,
							   final ShodroneUserPriority priority) {

		// Firstly, register the Shodrone user as a System user
		SystemUser newSystemUser = userSvc.registerNewUser(username, password, firstName, lastName, email, roles, createdOn);

		// Create value objects for ShodroneUser
		VATNumber vat = VATNumber.valueOf(vatNumberInput);
		Address address = Address.valueOf(streetAddress, postalCode, city);
		PhoneNumber phoneNumber = PhoneNumber.valueOf(phoneNumberInput);

		ShodroneUser newShodroneUser = null;


		if (roles.contains(ShodroneRoles.CUSTOMER)) {
			// Generates the data for the representative
			SystemUserDetailsForRepresentative repDetails = CustomerRepresentativeDefaultValues.buildRepresentativeDetails(
					username, firstName, lastName
			);

			// Set the roles for the representative
			Set<Role> repRoles = new HashSet<>();
			repRoles.add(ShodroneRoles.CUSTOMER_REPRESENTATIVE);

			// Register a new system user for the representative
			SystemUser repSystemUser = userSvc.registerNewUser(
					repDetails.username, repDetails.password, repDetails.firstName, repDetails.lastName,
					repDetails.systemEmail, repRoles, createdOn
			);

			// Create a new CustomerRepresentative object using its builder
			CustomerRepresentativeBuilder repBuilder = new CustomerRepresentativeBuilder();
			repBuilder.withSystemUser(repSystemUser)
					.withPhoneNumber(repDetails.representativePhoneNumber)
					.withCompanyEmail(repDetails.representativeCompanyEmail);
			CustomerRepresentative newRep = repBuilder.build();
			List<CustomerRepresentative> representatives = new ArrayList<>();
			representatives.add(newRep);

			// Create a new ShodroneUser object using the ShodroneUserBuilder
			ShodroneUserBuilder shodroneUserBuilder = new ShodroneUserBuilder();
			shodroneUserBuilder.withSystemUser(newSystemUser)
					.withPhoneNumber(phoneNumber)
					.withVATNumber(vat)
					.withAddress(address)
					.withPriority(priority)
					.withCustomerRepresentatives(representatives);

			newShodroneUser = shodroneUserBuilder.build();
		} else {
			// Create a new ShodroneUser object using the ShodroneUserBuilder
			ShodroneUserBuilder shodroneUserBuilder = new ShodroneUserBuilder();
			shodroneUserBuilder.withSystemUser(newSystemUser)
					.withPhoneNumber(phoneNumber)
					.withVATNumber(vat)
					.withAddress(address)
					.withPriority(priority);

			newShodroneUser = shodroneUserBuilder.build();
		}


		// Save the new ShodroneUser to the repository
		return this.shodroneUserRepository.save(newShodroneUser);
	}

	/**
	 * Creates a new Regular Shodrone user with the provided details.
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param vatNumber
	 * @param streetAddress
	 * @param postalCode
	 * @param city
	 * @param phoneNumber
	 * @param roles
	 * @return The newly created Shodrone user.
	 *
	 * This method is responsible for creating a new Shodrone user with the
	 * provided details. It uses the current time as the createdOn parameter.
	 */
	public ShodroneUser signup(final String username, final String password, final String firstName,
							   final String lastName, final String email, final String vatNumber, final String streetAddress,
							   final String postalCode, final String city, final String phoneNumber, final Set<Role> roles) {

		return signup(username, password, firstName, lastName, email, vatNumber, streetAddress, postalCode, city,
				phoneNumber, CurrentTimeCalendars.now(), roles, ShodroneUserPriority.Regular);
	}

	/**
	 * Creates a new Shodrone user with the provided details and provide priority.
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param vatNumber
	 * @param streetAddress
	 * @param postalCode
	 * @param city
	 * @param phoneNumber
	 * @param roles
	 * @param priority
	 * @return The newly created Shodrone user.
	 *
	 * This method is responsible for creating a new Shodrone user with the
	 * provided details. It uses the current time as the createdOn parameter.
	 */
	public ShodroneUser signup(final String username, final String password, final String firstName,
							   final String lastName, final String email, final String vatNumber, final String streetAddress,
							   final String postalCode, final String city, final String phoneNumber, final Set<Role> roles,
							   final ShodroneUserPriority priority) {

		return signup(username, password, firstName, lastName, email, vatNumber, streetAddress, postalCode, city,
				phoneNumber, CurrentTimeCalendars.now(), roles, priority);
	}

}