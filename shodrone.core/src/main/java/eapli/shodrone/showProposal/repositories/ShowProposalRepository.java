package eapli.shodrone.showProposal.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.domain.ShowProposal;
import java.util.List;
import java.util.Optional;

public interface ShowProposalRepository extends DomainRepository<Long, ShowProposal> {

    // get all proposals
    Iterable<ShowProposal> obtainAllShowProposals();

    List<ShowProposal> findByStatus(ProposalStatus status);

    Iterable<ShowProposal> findAllIncompleteProposals();

    List<ShowProposal> findAllTestingProposals();

    /**
     * Returns the proposals with a given status for a specific customer.
     *
     * @param vat the customer's VAT number
     * @param status the desired proposal status
     * @return an iterable collection of show proposals
     */
    Iterable<ShowProposal> findByCustomerAndStatus(String vat, ProposalStatus status);

    Iterable<ShowProposal> findByCustomer(ShodroneUser shodroneUser);

    Optional<ShowProposal> findByIdentifier(Long id);

}
