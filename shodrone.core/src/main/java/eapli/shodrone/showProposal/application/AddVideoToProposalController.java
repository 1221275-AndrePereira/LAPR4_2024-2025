package eapli.shodrone.showProposal.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.showProposal.domain.ProposalVideoLink;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class AddVideoToProposalController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ShowProposalRepository showProposalRepository = PersistenceContext.repositories().showProposals();

    private void checkIfUserIsAuthorized() {
        authz.ensureAuthenticatedUserHasAnyOf(
                ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.ADMIN,
                ShodroneRoles.POWER_USER);
    }

    public boolean addVideoToProposal(ShowProposal proposal, String videoUrl) {
        checkIfUserIsAuthorized();

        proposal.setVideoLink(ProposalVideoLink.valueOf(videoUrl));

        proposal.updateStatus();

        return showProposalRepository.save(proposal) != null;
    }

    public boolean removeVideoFromProposal(ShowProposal proposal) {
        checkIfUserIsAuthorized();

        proposal.setVideoLink(null);

        proposal.updateStatus();

        return showProposalRepository.save(proposal) != null;
    }

}
