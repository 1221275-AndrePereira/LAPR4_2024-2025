package eapli.shodrone.showProposal.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

/**
 * The type SendShowProposalController
 */
@UseCaseController
public class SendShowProposalController {

    private final AuthorizationService authz;
    private final ShowProposalRepository showProposalRepository;

    public SendShowProposalController(){
        authz = AuthzRegistry.authorizationService();
        showProposalRepository = PersistenceContext.repositories().showProposals();
    }

    public SendShowProposalController(final AuthorizationService authz, final ShowProposalRepository showProposalRepository) {
        this.authz = authz;
        this.showProposalRepository = showProposalRepository;
    }

    /**
     * Send show proposal to customer(alter the proposal status from "SAFE" to "PENDING)
     *
     * @param showProposalId The id of the show proposal
     * @return the show proposal
     */
    public ShowProposal sendProposalToCustomer(final long showProposalId){
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_COLLABORATOR,ShodroneRoles.CRM_MANAGER,ShodroneRoles.ADMIN,ShodroneRoles.POWER_USER);

        ShowProposal showProposal=showProposalRepository.findByIdentifier(showProposalId).orElse(null);

        assert showProposal != null;
        showProposal.updateStatus();

        if (showProposal.getStatus() != ProposalStatus.SAFE) {
            throw new IllegalStateException("The proposal is not ready to be sent");
        }

        showProposal.sendToCustomer();

        return showProposalRepository.save(showProposal);
    }




}
