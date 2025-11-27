package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.framework.general.domain.model.Description;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showrequest.domain.ShowRequest;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;

import shodrone.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JpaShowRequestRepository
        extends JpaAutoTxRepository<ShowRequest, Long, Long> implements ShowRequestRepository {

    /**
     * Default constructor for the JpaShowRequestRepository class.
     * <p>
     * This constructor initializes the repository with the default persistence unit name
     * and extended persistence properties.
     */
    public JpaShowRequestRepository(final TransactionalContext autoTx) {
        super(autoTx, "id");
    }

    /**
     * Constructor for the JpaShowRequestRepository class.
     * <p>
     * This constructor initializes the repository with the specified persistence unit name
     * and extended persistence properties.
     *
     * @param puname The persistence unit name.
     */
    public JpaShowRequestRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "id");
    }

    /**
     * Finds a show request by its description.
     *
     * @param description The description of the show request.
     * @return An Optional containing the found show request, or empty if not found.
     */
    @Override
    public Optional<ShowRequest> findByDescription(Description description) {
        final Map<String, Object> params = new HashMap<>();
        params.put("description", description);
        return matchOne("e.description = :description", params);
    }

    /**
     * Finds all show requests.
     *
     * @return An Iterable of all show requests.
     */
    @Override
    public Iterable<ShowRequest> obtainAllShowRequests() {
        return findAll();
    }

    /**
     * Finds all show requests made by a specific customer.
     *
     * @param customer The customer who made the show request.
     * @return An Iterable of show requests made by the specified customer.
     */
    @Override
    public Iterable<ShowRequest> findByCustomer(ShodroneUser customer) {
        final Map<String, Object> params = new HashMap<>();
        params.put("customer", customer);
        return match("e.customer = :customer", params);
    }

    /**
     * Finds what Request the Proposal comes from
     * @param showProposal the proposal to search
     * @return the found request where the proposal was found
     */
    @Override
    public Optional<ShowRequest> findRequestByProposal(ShowProposal showProposal) {
        final Map<String, Object> params = new HashMap<>();
        params.put("proposal", showProposal);

        return matchOne(":proposal MEMBER OF e.proposals", params);
    }

    /**
     * Finds a show request by its ID.
     *
     * @param id The ID of the show request.
     * @return An Optional containing the found show request, or empty if not found.
     */
    @Override
    public Optional<ShowRequest> ofIdentity(Long id) {
        return findById(id);
    }

    /**
     * Counts the total number of show requests in the repository.
     *
     * @return The total count of show requests.
     */
    @Override
    public long count() {
        return super.count();
    }

}
