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
package eapli.shodrone.shodroneusermanagement.domain;

import eapli.framework.domain.model.DomainFactory;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.customerRepresentative.domain.CustomerRepresentative;

import java.util.List;

/**
 * Builder for ShodroneUser.
 * <p>
 * This class is used to create instances of ShodroneUser. It provides methods to set the
 * properties of the ShodroneUser and a build method to create the instance.
 * <p>
 * The builder pattern is used to create complex objects step by step, allowing for more readable
 * and maintainable code.
 *
 * */
public class ShodroneUserBuilder implements DomainFactory<ShodroneUser> {

	private SystemUser systemUser;
	private PhoneNumber phoneNumber;
	private VATNumber vatNumber;
	private Address address;
	private ShodroneUserPriority priority;
	private List<CustomerRepresentative> customerRepresentatives;

	/**
	 * Default constructor for ShodroneUserBuilder.
	 * <p>
	 * Initializes the customer representatives list to an empty list.
	 */
	public ShodroneUserBuilder withSystemUser(final SystemUser systemUser) {
		this.systemUser = systemUser;
		return this;
	}

	/**
	 * Sets the phone number for this ShodroneUser.
	 *
	 * @param phoneNumber the phone number
	 * @return this builder instance
	 */
	public ShodroneUserBuilder withPhoneNumber(final PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	/**
	 * Sets the phone number for this ShodroneUser.
	 *
	 * @param phoneNumber the phone number
	 * @return this builder instance
	 */
	public ShodroneUserBuilder withPhoneNumber(final String phoneNumber) {
		this.phoneNumber = new PhoneNumber(phoneNumber);
		return this;
	}

	/**
	 * Sets the VAT number for this ShodroneUser.
	 *
	 * @param vatNumber the VAT number
	 * @return this builder instance
	 */
	public ShodroneUserBuilder withVATNumber(final VATNumber vatNumber) {
		this.vatNumber = vatNumber;
		return this;
	}

	/**
	 * Sets the VAT number for this ShodroneUser.
	 *
	 * @param vatNumber the VAT number
	 * @return this builder instance
	 */
	public ShodroneUserBuilder withVATNumber(final String vatNumber) {
		this.vatNumber = new VATNumber(vatNumber);
		return this;
	}

	/**
	 * Sets the address for this ShodroneUser.
	 *
	 * @param address the address
	 * @return this builder instance
	 */
	public ShodroneUserBuilder withAddress(final Address address) {
		this.address = address;
		return this;
	}

	/**
	 * Sets the address for this ShodroneUser.
	 *
	 * @param streetAddress the street address
	 * @param postalCode    the postal code
	 * @param city          the city
	 * @return this builder instance
	 */
	public ShodroneUserBuilder withAddress(final String streetAddress,
			final String postalCode, final String city) {
		this.address = new Address(streetAddress, postalCode, city);
		return this;
	}

	public ShodroneUserBuilder withPriority(final ShodroneUserPriority priority) {
		this.priority = priority;
		return this;
	}

	/**
	 * Sets the list of customer representatives for this ShodroneUser.
	 *
	 * @param customerRepresentatives the list of customer representatives
	 * @return this builder instance
	 */
	public ShodroneUserBuilder withCustomerRepresentatives(final List<CustomerRepresentative> customerRepresentatives) {
		this.customerRepresentatives = customerRepresentatives;
		return this;
	}

	/**
	 * Adds a customer representative to the list of customer representatives.
	 *
	 * @param customerRepresentative the customer representative to add
	 * @return this builder instance
	 */
	public ShodroneUserBuilder withCustomerRepresentatives(final CustomerRepresentative customerRepresentative) {
		this.customerRepresentatives.add(customerRepresentative);
		return this;
	}

	/**
	 * Creates a new ShodroneUser instance with the properties set in the builder.
	 *
	 * @return a new ShodroneUser instance
	 */
	@Override
	public ShodroneUser build() {
		if (this.priority == null) {
			this.priority = ShodroneUserPriority.Regular;
		}
		return new ShodroneUser(
				this.systemUser,
				this.phoneNumber,
				this.vatNumber,
				this.address,
				this.priority,
				this.customerRepresentatives
		);
	}
}
