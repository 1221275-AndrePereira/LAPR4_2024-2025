package eapli.shodrone.showrequest.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.general.domain.model.Description;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showrequest.domain.ShowRequest;

import java.util.Optional;

/**
 * Interface for the ShowRequest repository.
 */
public interface ShowRequestRepository extends DomainRepository<Long, ShowRequest> {

    /**
     * Finds a show request by its description.
     *
     * @param description The description of the show request.
     * @return An Optional containing the found show request, or empty if not found.
     */
    Optional<ShowRequest> findByDescription(Description description);

    /**
     * Finds a show request.
     *
     * @return An Optional containing the found show request, or empty if not found.
     */
    Iterable<ShowRequest> obtainAllShowRequests();

    /**
     * Finds all show requests made by a specific customer.
     *
     * @param customer The customer who made the show request.
     * @return An Iterable of show requests made by the specified customer.
     */
    Iterable<ShowRequest> findByCustomer(ShodroneUser customer);
    /**
     * Finds what Request the Proposal comes from
     * @param showProposal the proposal to search
     * @return the found request where the proposal was found
     */
    Optional<ShowRequest> findRequestByProposal(ShowProposal showProposal);
}
