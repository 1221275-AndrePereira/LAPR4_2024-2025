package eapli.shodrone.showProposal.application;

import eapli.framework.application.UseCaseController;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;

import java.util.List;
import java.util.Optional;


@UseCaseController
public class ListShowProposalController {

    private final ShowProposalRepository repository = PersistenceContext.repositories().showProposals();

    /**
     * Lists all show proposals.
     *
     * @return a stream of all show proposals.
     */
    public Iterable<ShowProposal> listAllShowProposals() {
        return repository.obtainAllShowProposals();
    }

    /**
     * Lists all show proposals for a specific customer.
     *
     * @param customer the VAT number of the customer.
     * @return an iterable collection of show proposals for the specified customer.
     */
    public Iterable<ShowProposal> listShowProposalsByCustomer(ShodroneUser customer) {
        return repository.findByCustomer(customer);
    }

    /**
     * Lists all show proposal with INCOMPLETE Status.
     * @return an iterable collection of the corresponding show proposals
     */
    public Iterable<ShowProposal> listIncompleteProposals() {

        return repository.findAllIncompleteProposals();
    }

    public List<ShowProposal> findAllTestingProposals() {
        return repository.findAllTestingProposals();
    }


    public Optional<ShowProposal> findByID(long id){
        return repository.findByIdentifier(id);
    }

    public List<ShowProposal> findProposalsByStatus(ProposalStatus proposalStatus) {
        return repository.findByStatus(proposalStatus);
    }

}
