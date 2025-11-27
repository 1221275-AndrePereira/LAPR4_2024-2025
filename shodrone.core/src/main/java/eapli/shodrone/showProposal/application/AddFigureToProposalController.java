package eapli.shodrone.showProposal.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.showProposal.domain.*;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalFigure;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for adding figures to a show proposal.
 * This controller handles the logic of updating a show proposal with new figures.
 */
@UseCaseController
public class AddFigureToProposalController {

    // Authorization service to check user permissions
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    // Repository for accessing show proposals
    private final ShowProposalRepository showProposalRepository = PersistenceContext.repositories().showProposals();

    /**
     * Adds figures to a show proposal.
     * This method checks if the user is authorized to perform this action,
     * retrieves the show proposal by its ID, and updates it with the provided figures.
     *
     * @param showProposalId The ID of the show proposal to update.
     * @param currentSelection The list of figures to add to the proposal.
     */
    public void setAllFiguresForProposal(final Long showProposalId, final List<Figure> currentSelection) {
        // Check if the user has the necessary permissions to modify the show proposal
        authz.ensureAuthenticatedUserHasAnyOf(
                ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.CRM_MANAGER,
                ShodroneRoles.ADMIN,
                ShodroneRoles.POWER_USER);

        // Get the show proposal ID
        ShowProposal showProposal = showProposalRepository.findByIdentifier(showProposalId)
                .orElseThrow(() -> new IllegalArgumentException("ShowProposal with ID " + showProposalId + " not found."));


        // Turns the List<Figure> into a List<ProposalFigure> so it can be saved in the ShowProposal
        List<ProposalFigure> newProposalFigures = new ArrayList<>();
        for (Figure figure : currentSelection) {
            newProposalFigures.add(new ProposalFigure(figure));
        }

        // Update the show proposal with the new figures
        showProposal.updateFigures(newProposalFigures);
        // Update the status of the show proposal
        showProposal.updateStatus();
        // Save the updated show proposal back to the repository
        showProposalRepository.save(showProposal);
    }
}