package eapli.shodrone.showProposal.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class FinalizeProposalController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ShowProposalRepository showProposalRepository = PersistenceContext.repositories().showProposals();

    private void checkIfUserIsAuthorized() {
        authz.ensureAuthenticatedUserHasAnyOf(
                ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.ADMIN,
                ShodroneRoles.POWER_USER);
    }


    public void closeProposal (ShowProposal proposal) {
        if (proposal == null) {
            throw new IllegalArgumentException("Proposal cannot be null");
        }
        if (proposal.getStatus() != ProposalStatus.ACCEPTED){
            throw new IllegalArgumentException("Proposal is not accepted");
        }
        checkIfUserIsAuthorized();
        proposal.setStatus(ProposalStatus.CLOSED);
        showProposalRepository.save(proposal);
    }

    public void archiveProposal (ShowProposal proposal) {
        if (proposal == null) {
            throw new IllegalArgumentException("Proposal cannot be null");
        }
        checkIfUserIsAuthorized();
        proposal.setStatus(ProposalStatus.ARCHIVED);
        showProposalRepository.save(proposal);
    }

}
