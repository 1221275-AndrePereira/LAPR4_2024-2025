package eapli.shodrone.shodroneusermanagement.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.customerRepresentative.domain.CustomerRepresentative;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Shodrone user.
 * <p>
 * This class is an aggregate root and is responsible for managing the user's information,
 * including their VAT number, phone number, address, and customer representatives.
 * <p>
 * The class is annotated with JPA annotations to enable persistence in a relational database.
 * It uses an embedded ID for the VAT number and includes a version field for optimistic locking.
 */
@Entity
@Table(name = "ShodroneUser")
public class ShodroneUser implements AggregateRoot<VATNumber> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    @EmbeddedId
    private VATNumber vatNumber;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private ShodroneUserPriority priority;

    @OneToOne(optional = false)
    private SystemUser systemUser;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CustomerRepresentative> customerRepresentatives = new ArrayList<>();


    /**
     * Constructor for creating a Shodrone user with the specified parameters.
     *
     * @param systemUser              The system user associated with this Shodrone user.
     * @param phoneNumber             The phone number of the Shodrone user.
     * @param vatNumber               The VAT number of the Shodrone user.
     * @param address                 The address of the Shodrone user.
     * @param priority                The priority of the Shodrone user.
     * @param customerRepresentatives The list of customer representatives associated with this Shodrone user.
     */
    public ShodroneUser(
            SystemUser systemUser,
            final PhoneNumber phoneNumber,
            final VATNumber vatNumber,
            final Address address,
            final ShodroneUserPriority priority,
            final List<CustomerRepresentative> customerRepresentatives
    ) {
        // Validate the parameters to ensure they are not null
        Preconditions.noneNull(phoneNumber, systemUser, systemUser.username(), vatNumber, address, priority);

        this.systemUser = systemUser;
        this.phoneNumber = phoneNumber;
        this.vatNumber = vatNumber;
        this.address = address;
        this.priority = priority;
        this.customerRepresentatives = customerRepresentatives;
    }

    protected ShodroneUser() {
        // for ORM
    }

    /**
     * @return The SystemUser associated with this Shodrone user.
     */
    public SystemUser systemUser() {
        return this.systemUser;
    }

    /**
     * @return The PhoneNumber of this Shodrone user.
     */
    public PhoneNumber phoneNumber() {
        return this.phoneNumber;
    }

    /**
     * @return The Address of this Shodrone user.
     */
    public Address address() {
        return this.address;
    }

    /**
     * @return The ShodroneUserPriority of this Shodrone user.
     */
    public ShodroneUserPriority priority() {
        return this.priority;
    }

    /**
     * @return The list of CustomerRepresentatives associated with this Shodrone user.
     */
    public List<CustomerRepresentative> customerRepresentatives() {
        return this.customerRepresentatives;
    }


    /**
     * Compares this Shodrone user with another object for equality.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    /**
     * Returns a hash code value for this Shodrone user.
     *
     * @return The hash code value for this Shodrone user.
     */
    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(Object other) {
        return DomainEntities.areEqual(this, other);
    }

    /**
     * Returns the VAT number of this Shodrone user.
     *
     * @return The VAT number of this Shodrone user.
     */
    public VATNumber vatNumber() {
        return identity();
    }

    /**
     * Returns the ID of Shodrone user. That is also the VAT number.
     *
     * @return Returns the ID of Shodrone user.
     */
    @Override
    public VATNumber identity() {
        return this.vatNumber;
    }
}
