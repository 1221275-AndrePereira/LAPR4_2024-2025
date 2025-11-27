package eapli.shodrone.showProposal.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.application.UserSession;
import eapli.framework.application.UseCaseController;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.showProposal.domain.proposalFields.*;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalNDrones;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.showProposal.domain.*;

import eapli.shodrone.showrequest.domain.ShowRequest;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UseCaseController
public class CreateShowProposalController {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(CreateShowProposalController.class);

    private SystemUser author;


    private final ShowRequestRepository showRequestRepository;

    AuthorizationService authz = AuthzRegistry.authorizationService();

    /**
     * Default constructor.
     * Initializes the controller with necessary services and repositories
     * using the project-specific PersistenceContext.
     */
    public CreateShowProposalController() {
        this.showRequestRepository = PersistenceContext.repositories().showRequestCatalogue();
    }

    /**
     * Creates a new show proposal with the provided details.
     *
     * @param numberOfDrones     The number of drones proposed.
     * @param duration           The proposed duration of the show.
     * @param proposedPlace      The proposed place for the show.
     * @param proposedShowDate   The proposed date for the show.
     */
    public ShowProposal createShowProposal(
            ShowRequest showRequest,
            String description,
            ProposalNDrones numberOfDrones,
            ProposedDuration duration,
            ProposedPlace proposedPlace,
            ProposedShowDate proposedShowDate,
            Insurance insurance
    ) {

        if(authz.hasSession()) {
            defineAuthor(authz.session()
                    .orElseThrow(() -> new IllegalStateException("No user authenticated in the current session."))
                    .authenticatedUser()
            );
        }

        final ShowProposal newShowProposal = new ShowProposalBuilder()
                .withDescription(description)
                .withNumberOfDrones(numberOfDrones)
                .withDuration(duration)
                .withProposedPlace(proposedPlace)
                .withProposedShowDate(proposedShowDate)
                .withInsurance(insurance)
                .withManager(this.author)
                .build();

        log.debug("Creating new show proposal: {}", newShowProposal);

        List<Figure> previousFigures = showRequest.getFigure();
        List<ProposalFigure> figures = new ArrayList<>();
        for(Figure figure : previousFigures) {
            figures.add(new ProposalFigure(figure));
        }

        newShowProposal.updateFigures(figures);

        showRequest.addProposal(newShowProposal);
        showRequestRepository.save(showRequest);

        return newShowProposal;

    }

    private void defineAuthor(SystemUser systemUser) {this.author = systemUser;}
}
