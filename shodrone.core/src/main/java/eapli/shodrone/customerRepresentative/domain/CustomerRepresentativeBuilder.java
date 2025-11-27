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
package eapli.shodrone.customerRepresentative.domain;

import eapli.framework.domain.model.DomainFactory;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.shodroneusermanagement.domain.Address;
import eapli.shodrone.shodroneusermanagement.domain.PhoneNumber;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;

/**
 * Builder for creating instances of {@link CustomerRepresentative}.
 *
 * This class provides a fluent interface for setting the required fields of
 * the
 * CustomerRepresentative object. It ensures that all required fields are
 * provided
 * before creating the object.
 *
 */
public class CustomerRepresentativeBuilder implements DomainFactory<CustomerRepresentative> {

	private SystemUser systemUser;
	private PhoneNumber phoneNumber;
	private CompanyEmail companyEmail;

	public CustomerRepresentativeBuilder withSystemUser(final SystemUser systemUser) {
		this.systemUser = systemUser;
		return this;
	}

	public CustomerRepresentativeBuilder withPhoneNumber(final PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public CustomerRepresentativeBuilder withPhoneNumber(final String phoneNumber) {
		this.phoneNumber = new PhoneNumber(phoneNumber);
		return this;
	}

	public CustomerRepresentativeBuilder withCompanyEmail(final CompanyEmail companyEmail) {
		this.companyEmail = companyEmail;
		return this;
	}

	public CustomerRepresentativeBuilder withCompanyEmail(final String companyEmail) {
		this.companyEmail = new CompanyEmail(companyEmail);
		return this;
	}

	/**
	 * Builds a new instance of {@link CustomerRepresentative} using the
	 * provided values.
	 *
	 * @return a new instance of {@link CustomerRepresentative}
	 */
	@Override
	public CustomerRepresentative build() {
		return new CustomerRepresentative(this.systemUser, this.phoneNumber, this.companyEmail);
	}
}
