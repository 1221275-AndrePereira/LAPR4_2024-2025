package eapli.shodrone.customerRepresentative.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.shodroneusermanagement.domain.Address;
import eapli.shodrone.shodroneusermanagement.domain.PhoneNumber;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import jakarta.persistence.*;

/**
 * Represents a customer representative in the system.
 * <p>
 * This class is an aggregate root and contains information about the representative's
 * system user, phone number, and company email.
 */
@Entity
@Table(name = "customer_representative")
public class CustomerRepresentative implements AggregateRoot<Long> {
    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "systemUser_username", referencedColumnName = "username")
    private SystemUser systemUser;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private CompanyEmail companyEmail;

    /**
     * Constructs a new CustomerRepresentative with the specified system user, phone number, and company email.
     *
     * @param systemUser   The system user associated with the representative.
     * @param phoneNumber  The phone number of the representative.
     * @param companyEmail The company email of the representative.
     */
    public CustomerRepresentative(SystemUser systemUser, final PhoneNumber phoneNumber,
                                  final CompanyEmail companyEmail) {
        Preconditions.noneNull(companyEmail, phoneNumber, systemUser);

        this.systemUser = systemUser;
        this.phoneNumber = phoneNumber;
        this.companyEmail = companyEmail;
    }

    protected CustomerRepresentative() {
        // for ORM
    }

    public SystemUser systemUser() {
        return this.systemUser;
    }

    public PhoneNumber phoneNumber() {
        return this.phoneNumber;
    }

    public CompanyEmail companyEmail() {
        return this.companyEmail;
    }

    /**
     *  Compares this CustomerRepresentative to another object for equality.
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    /**
     *  Returns the hash code of this CustomerRepresentative.
     * @return the hash code of this CustomerRepresentative
     */
    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(Object other) {
        return DomainEntities.areEqual(this, other);
    }

    @Override
    public Long identity() {
        return this.id;
    }

}
