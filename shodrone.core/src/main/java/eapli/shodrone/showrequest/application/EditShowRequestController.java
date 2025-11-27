package eapli.shodrone.showrequest.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.general.domain.model.Description;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showrequest.domain.*;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;


@UseCaseController
public class EditShowRequestController {
    private final AuthorizationService authz;
    private final ShowRequestRepository showRequestRepository;

    /**
     * Default constructor.
     * Initializes the controller with necessary services and repositories
     * using the project-specific PersistenceContext.
     */
    public EditShowRequestController() {
        this.authz = AuthzRegistry.authorizationService();
        this.showRequestRepository = PersistenceContext.repositories().showRequestCatalogue();
    }

    /**
     * Constructor for injecting dependencies (e.g., for testing or specific DI frameworks).
     *
     * @param authz                 The authorization service.
     * @param showRequestRepository The show request repository.
     *
     */
    public EditShowRequestController(
            final AuthorizationService authz,
            final ShowRequestRepository showRequestRepository
    ) {
        Preconditions.noneNull(authz, showRequestRepository);
        this.authz = authz;
        this.showRequestRepository = showRequestRepository;
    }

    /**
     * Edits an existing show request with the provided details and updates it in the repository.
     * Validates the input parameters before updating the show request.
     *
     * @param showRequest        The existing show request to be edited
     * @param description        New description of the show to be requested
     * @param requestPlace       New location of the show
     * @param requestShowDate    New scheduled date and time for the show
     * @param requestDuration    New duration of the show in minutes
     * @param requestNdDrones    New number of drones required for the show
     * @param customer           User making the show request
     * @return The updated ShowRequest
     * @throws IllegalArgumentException if any input parameter is null, empty, or invalid
     */
    public ShowRequest editShowRequest(
            final ShowRequest showRequest,
            Description description,
            RequestPlace requestPlace,
            RequestShowDate requestShowDate,
            RequestDuration requestDuration,
            RequestNDrones requestNdDrones,
            ShodroneUser customer
    ) {

        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_MANAGER);
        //data validation:
        Preconditions.noneNull(
                showRequest,
                description,
                requestPlace,
                requestShowDate,
                requestDuration,
                requestNdDrones,
                customer
        );

        // update the show request with the new values
        showRequest.updateShowDescription(description);
        showRequest.updateShowDate(requestShowDate);
        showRequest.updateShowDuration(requestDuration);
        showRequest.updateShowPlace(requestPlace);
        showRequest.updateShowNDrones(requestNdDrones);
        showRequest.updateShowCustomer(customer);

        return showRequestRepository.save(showRequest); // updates the join table
    }
}
