package eapli.shodrone.shodroneusermanagement.repository;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;

import java.util.Optional;

/**
 * Repository interface for managing ShodroneUser entities.
 * <p>
 * This interface extends the DomainRepository interface and provides methods
 * for finding ShodroneUser entities by their username and VAT number.
 */
public interface ShodroneUserRepository extends DomainRepository<VATNumber, ShodroneUser> {

    /**
     * Finds a ShodroneUser by their username.
     *
     * @param user the username of the ShodroneUser to find
     * @return an Optional containing the found ShodroneUser, or an empty Optional if not found
     */
    Optional<ShodroneUser> findByUsername(Username user);

    /**
     * Finds a ShodroneUser by their VAT number.
     *
     * @param number the VAT number of the ShodroneUser to find
     * @return an Optional containing the found ShodroneUser, or an empty Optional if not found
     */
    default Optional<ShodroneUser> findByVatNumber(
            final VATNumber number) {
        return ofIdentity(number);
    }

    /**
     * Finds all active ShodroneUsers.
     *
     */
    Iterable<ShodroneUser> findAllActive();

    Optional<ShodroneUser> findCustomerByRepresentative(Username username);
}
