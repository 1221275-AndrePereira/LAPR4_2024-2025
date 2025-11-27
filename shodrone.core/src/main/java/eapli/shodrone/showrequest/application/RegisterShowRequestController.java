package eapli.shodrone.showrequest.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.general.domain.model.Description;
import eapli.framework.validations.Preconditions;

import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showrequest.domain.*;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller for the use case of registering a new Show Request.
 */
@UseCaseController
public class RegisterShowRequestController {

    private static final Logger log = LoggerFactory.getLogger(RegisterShowRequestController.class);
    private final ShowRequestRepository showRequestRepository;

    /**
     * Default constructor.
     * Initializes the controller with necessary services and repositories
     * using the project-specific PersistenceContext.
     */
    public RegisterShowRequestController() {
        this.showRequestRepository = PersistenceContext.repositories().showRequestCatalogue();
    }

    /**
     * Constructor for injecting dependencies (e.g., for testing or specific DI frameworks).
     *
     * @param showRequestRepository The show request repository.
     */
    public RegisterShowRequestController(final ShowRequestRepository showRequestRepository) {
        Preconditions.noneNull(showRequestRepository);
        this.showRequestRepository = showRequestRepository;
    }

    /**
     * Registers a new show request with the provided details and stores it in the repository.
     * Validates the input parameters before creating and saving the show request.
     * Returns the created ShowRequest if the user is a customer; otherwise, returns null.
     *
     * @param description       Description of the show to be requested
     * @param requestPlace      Location of the show
     * @param requestCreateDate Date and time the request is created
     * @param requestShowDate   Scheduled date and time for the show
     * @param requestDuration   Duration of the show in minutes
     * @param requestNdDrones   Number of drones required for the show
     * @param customer          User making the show request
     * @return The created ShowRequest if the user is a customer, or null otherwise
     * @throws IllegalArgumentException if any input parameter is null, empty, or invalid
     */
    public ShowRequest registerShowRequest(
            Description description,
            RequestPlace requestPlace,
            RequestCreationDate requestCreateDate,
            RequestShowDate requestShowDate,
            RequestDuration requestDuration,
            RequestNDrones requestNdDrones,
            ShodroneUser customer,
            List<Figure> figure
    ) {
        //data validation:
        Preconditions.noneNull(
                description,
                requestPlace,
                requestCreateDate,
                requestShowDate,
                requestDuration,
                requestNdDrones,
                customer,
                figure
        );

        final ShowRequest newShowRequest = new
                ShowRequestBuilder().
                withDescription(description).
                withRequestPlace(requestPlace).
                withRequestCreateDate(requestCreateDate).
                withRequestShowDate(requestShowDate).
                withRequestDuration(requestDuration).
                withRequestNdDrones(requestNdDrones).
                withCustomer(customer).
                withFigure(figure).
                build();

        return isCustomer(customer) ? this.showRequestRepository.save(newShowRequest) : null;
    }

    /**
     * Checks if the given ShodroneUser has the CRM_COLLABORATOR role, indicating they are a customer.
     *
     * @param shodroneUser The ShodroneUser to check for the CRM_COLLABORATOR role.
     * @return true if the user has the CRM_COLLABORATOR role, false otherwise.
     */
    public boolean isCustomer(ShodroneUser shodroneUser) {
        try {
            return shodroneUser.systemUser().hasAny(ShodroneRoles.CUSTOMER);
        } catch (Exception e) {
            log.error("Error checking if user is a customer: {}", e.getMessage());
            return false;
        }
    }
}