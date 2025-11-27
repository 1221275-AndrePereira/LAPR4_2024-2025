package eapli.shodrone.showrequest.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.validations.Preconditions;

import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

public class RemoveShowRequestController {
    private final AuthorizationService authz;
    private final ShowRequestRepository showRequestRepository;

    /**
     * Default constructor.
     * Initializes the controller with necessary services and repositories
     * using the project-specific PersistenceContext.
     */
    public RemoveShowRequestController() {
        this.authz = AuthzRegistry.authorizationService();
        this.showRequestRepository = PersistenceContext.repositories().showRequestCatalogue();
    }

    /**
     * Constructor for injecting dependencies (e.g., for testing or specific DI frameworks).
     *
     * @param authz                 The authorization service.
     * @param showRequestRepository The show request repository.
     */
    public RemoveShowRequestController(AuthorizationService authz, ShowRequestRepository showRequestRepository) {
        this.authz = authz;
        this.showRequestRepository = showRequestRepository;
    }

    /**
     * Removes a show request from the repository.
     * Validates the input parameters before removing the show request.
     *
     * @param showRequestId The ID of the show request to be removed
     * @throws IllegalArgumentException if the show request ID is null or invalid
     */

    public void removeShowRequest(final Long showRequestId) {
        Preconditions.nonNull(showRequestId, "Show request ID cannot be null");

        // Ensure the user has the necessary permissions to remove a show request
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_MANAGER);

        // Find the show request by ID
        final var showRequest = showRequestRepository.ofIdentity(showRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Show request not found"));

        // Remove the show request from the repository
        showRequestRepository.remove(showRequest);
        // Optionally, you can log the removal or perform additional actions here
        System.out.println("Show request with ID " + showRequestId + " has been removed successfully.");
    }
}
