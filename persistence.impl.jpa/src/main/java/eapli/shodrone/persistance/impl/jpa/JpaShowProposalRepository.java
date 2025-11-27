package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import jakarta.persistence.TypedQuery;
import shodrone.Application;

import java.util.*;


public class JpaShowProposalRepository
        extends JpaAutoTxRepository<ShowProposal, Long, Long> implements ShowProposalRepository {

    /**
     * Default constructor for the JpaShowProposalRepository class.
     * <p>
     * This constructor initializes the repository with the default persistence unit name
     * and extended persistence properties.
     */
    public JpaShowProposalRepository(final TransactionalContext autoTx) {
        super(autoTx, "id");
    }

    /**
     * Constructor for the JpaShowProposalRepository class.
     * <p>
     * This constructor initializes the repository with the specified persistence unit name
     * and extended persistence properties.
     *
     * @param puname The persistence unit name.
     */
    public JpaShowProposalRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "id");
    }

    /**
     * Retrieves all show proposals.
     *
     * @return An Iterable of all show proposals.
     */
    public Iterable<ShowProposal> obtainAllShowProposals() {
        return findAll();
    }

    /**
     * Finds show proposals by their status.
     *
     * @param status The status of the proposals to find.
     * @return A list of show proposals with the specified status.
     */
    public List<ShowProposal> findByStatus(final ProposalStatus status) {
        final TypedQuery<ShowProposal> query = entityManager().createQuery(
                "SELECT sp FROM ShowProposal sp WHERE sp.status = :status",
                ShowProposal.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public Iterable<ShowProposal> findAllIncompleteProposals() {
        final TypedQuery<ShowProposal> query = entityManager().createQuery(
                "SELECT sp FROM ShowProposal sp WHERE sp.status = :status",
                ShowProposal.class);
        query.setParameter("status", ProposalStatus.INCOMPLETE);
        return query.getResultList();
    }

    public List<ShowProposal> findAllTestingProposals() {
        final TypedQuery<ShowProposal> query = entityManager().createQuery(
                "SELECT sp FROM ShowProposal sp WHERE sp.status <> :status",
                ShowProposal.class);
        query.setParameter("status", ProposalStatus.TESTING);
        return query.getResultList();
    }

    @Override
    public Iterable<ShowProposal> findByCustomerAndStatus(String vat, ProposalStatus status) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("vat", vat);
        parameters.put("status", status);
        return match("e.shodroneUser.vatNumber = :vat AND e.status = :status", parameters);
    }

    @Override
    public Iterable<ShowProposal> findByCustomer(ShodroneUser shodroneUser) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("shodroneUser", shodroneUser);
        return match("e.shodroneUser = :shodroneUser", parameters);
    }

    /**
     * Finds a ShowProposal based on its identifier.
     *
     * @param id The identifier of the ShowProposal to find.
     * @return An Optional containing the found ShowProposal or empty if no matching ShowProposal is found.
     */
    public Optional<ShowProposal> findByIdentifier(Long id) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return matchOne("e.id = :id", parameters);
    }

    /**
     * Finds a show proposal by its ID.
     *
     * @param id The ID of the show proposal.
     * @return An Optional containing the found show proposal, or empty if not found.
     */
    @Override
    public Optional<ShowProposal> ofIdentity(Long id) {
        return findById(id);
    }

    /**
     * Counts the total number of show proposals in the repository.
     *
     * @return The total count of show proposals.
     */
    @Override
    public long count() {
        return super.count();
    }
}
