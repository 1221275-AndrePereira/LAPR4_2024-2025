package eapli.shodrone.customerRepresentative.repository;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.customerRepresentative.domain.CustomerRepresentative;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;

import java.util.Optional;

/**
 * Repository for Customer Representatives.
 * <p>
 * This repository is responsible for managing the persistence of Customer Representative entities.
 * It extends the DomainRepository interface, which provides basic CRUD operations.
 * <p>
 * The repository allows searching for Customer Representatives by their username and retrieving all active representatives.
 */
public interface CustomerRepresentativeRepository extends DomainRepository<Long, CustomerRepresentative> {

    /**
     * Find a Customer Representative by their username.
     * @param user the username of the Customer Representative
     * @return an Optional containing the Customer Representative if found, or empty if not found
     */
    Optional<CustomerRepresentative> findByUsername(Username user);

    /**
     * Find a Customer Representative by their Shodrone User.
     * @param id the Shodrone User of the Customer Representative
     * @return an Optional containing the Customer Representative if found, or empty if not found
     */
    Optional<CustomerRepresentative> findById(Long id);

}
