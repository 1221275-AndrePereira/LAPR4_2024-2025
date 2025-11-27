package eapli.shodrone.showrequest.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.general.domain.model.Description;
import eapli.framework.application.UseCaseController;

import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import eapli.shodrone.showrequest.domain.ShowRequest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@UseCaseController
public class FindShowRequestController {

    private final ShowRequestRepository showRequestRepository;

    private final AuthorizationService authz;

    private final TransactionalContext txCtx;


    /**
     * Default constructor for the FindShowRequestController class.
     *
     * This constructor initializes the controller by setting up the necessary
     * authorization service, transactional context, and repository for managing
     * show requests. These dependencies are resolved using the project's
     * PersistenceContext and AuthzRegistry.
     *
     * The following components are initialized:
     * - AuthorizationService: Retrieved from the AuthzRegistry to handle
     *   user authentication and authorization.
     * - TransactionalContext: Created using the persistence layer to manage
     *   transactions for database operations.
     * - ShowRequestRepository: Obtained from the persistence layer to interact
     *   with the storage of show requests.
     */
    public FindShowRequestController() {
        this.authz = AuthzRegistry.authorizationService();
        this.txCtx = PersistenceContext.repositories().newTransactionalContext();
        this.showRequestRepository = PersistenceContext.repositories().showRequestCatalogue(txCtx);
    }

    /**
     * Constructor for the FindShowRequestController class.
     *
     * Initializes the controller with the specified authorization service and
     * show request repository. This is used for dependency injection or to customize
     * the controller with specific implementations of these dependencies.
     *
     * @param authz                 The service to handle authorization and authentication.
     * @param showRequestRepository The repository to manage and access show requests.
     * @value txCtx The transactional context for managing database transactions.
     */
    public FindShowRequestController(
            AuthorizationService authz,
            ShowRequestRepository showRequestRepository,
            TransactionalContext txCtx
        ) {
        this.authz = authz;
        this.showRequestRepository = showRequestRepository;
        this.txCtx = txCtx;
    }

    /**
     * Retrieves a ShowRequest by its unique identifier.
     *
     * This method ensures that only authenticated users with the roles
     * CRM_MANAGER or ADMIN can perform the operation. It interacts with
     * the ShowRequestRepository to find the ShowRequest and handles transactional
     * context for database operations.
     *
     * @param id The unique identifier of the ShowRequest to retrieve.
     * @return The ShowRequest associated with the given ID, or null if no such
     *         ShowRequest is found.
     * @throws RuntimeException if an error occurs during the database operation.
     */
    public ShowRequest findShowRequestById(Long id) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);

        if (txCtx != null) {
            try {
                txCtx.beginTransaction();
                Optional<ShowRequest> showRequest = this.showRequestRepository.ofIdentity(id);

                if (showRequest.isPresent()) {
                    ShowRequest result = showRequest.get();
                    // Since this is a read operation, we don't need to commit
                    // Just rollback to release the transaction
                    txCtx.rollback();
                    return result;
                } else {
                    txCtx.rollback();
                    return null;
                }
            } catch (Exception e) {
                if (txCtx.isActive()) {
                    txCtx.rollback();
                }
                throw new RuntimeException("Error finding show request: Unable to complete database operation", e);
            }
        }
        return this.showRequestRepository.ofIdentity(id).orElse(null);
    }

    /**
     * Finds a ShowRequest by its description.
     *
     * This method ensures that the current user is authenticated and authorized
     * with either the CRM_MANAGER or ADMIN roles before retrieving the show request.
     * It attempts to interact with the repository within a transactional context
     * if one is available; otherwise, it directly retrieves the result from the repository.
     *
     * @param description The description of the ShowRequest to find.
     * @return The ShowRequest that matches the given description, or null if no matching ShowRequest is found.
     * @throws RuntimeException If an error occurs during the database operation.
     */
    public ShowRequest findShowRequestByDescription(Description description) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);

        if (txCtx != null) {
            ShowRequest result = null;
            try {
                txCtx.beginTransaction();
                Optional<ShowRequest> showRequest = this.showRequestRepository.findByDescription(description);
                if (showRequest.isPresent()) {
                    result = showRequest.get();
                    txCtx.commit();
                } else {
                    txCtx.rollback();
                }
            } catch (Exception e) {
                if (txCtx.isActive()) {
                    txCtx.rollback();
                }
                throw new RuntimeException("Error finding show request: " + e.getMessage(), e);
            }
            return result;
        }
        return this.showRequestRepository.findByDescription(description).orElse(null);
    }

    /**
     * Retrieves a list of all available show requests.
     *
     * This method ensures that only authenticated users with roles
     * CRM_MANAGER or ADMIN can access the list of show requests.
     * It interacts with the repository to fetch all show requests
     * and leverages the transactional context when available to manage
     * database interactions.
     *
     * @return A list of all show requests present in the system. If no show requests
     *         exist, an empty list is returned.
     * @throws RuntimeException If an error occurs during the process of fetching show requests.
     */
    public List<ShowRequest> listShowRequests() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);

        if (txCtx != null) {
            List<ShowRequest> showRequestList = new ArrayList<>();
            try {
                txCtx.beginTransaction();
                showRequestRepository.obtainAllShowRequests().forEach(showRequestList::add);
                txCtx.commit();
            } catch (Exception e) {
                if (txCtx.isActive()) {
                    txCtx.rollback();
                }
                throw new RuntimeException("Error listing show requests: " + e.getMessage(), e);
            }
            return showRequestList;
        }
        List<ShowRequest> showRequestList = new ArrayList<>();
        showRequestRepository.obtainAllShowRequests().forEach(showRequestList::add);
        return showRequestList;
    }
    /**
     * Finds what Request the Proposal comes from
     * @param showProposal the proposal to search
     * @return the found request where the proposal was found
     */
    public Optional<ShowRequest> findShowRequestByProposal(ShowProposal showProposal) {
        return showRequestRepository.findRequestByProposal(showProposal);
    }

    /**
     * Retrieves a list of show requests associated with a specific customer.
     *
     * This method ensures that the current user is authenticated and has one of
     * the required roles (CRM_MANAGER, ADMIN, or CRM_COLLABORATOR) before performing
     * the operation. The show requests are fetched from the repository and returned
     * as a list. If a transactional context is available, it is used to manage
     * the database operations.
     *
     * @param customer The customer whose associated show requests are to be retrieved.
     * @return A list of show requests associated with the specified customer. If no
     *         requests are found, an empty list is returned.
     * @throws RuntimeException If an error occurs while attempting to retrieve the show requests.
     */
    public List<ShowRequest> findShowRequestsByCustomer(ShodroneUser customer) {
        authz.ensureAuthenticatedUserHasAnyOf(
                ShodroneRoles.CRM_MANAGER,
                ShodroneRoles.ADMIN,
                ShodroneRoles.CRM_COLLABORATOR
            );

        if (txCtx != null) {
            List<ShowRequest> showRequestList = new ArrayList<>();
            try {
                txCtx.beginTransaction();
                showRequestRepository.findByCustomer(customer).forEach(showRequestList::add);
                txCtx.commit();
            } catch (Exception e) {
                if (txCtx.isActive()) {
                    txCtx.rollback();
                }
                throw new RuntimeException("Error finding show requests by customer: " + e.getMessage(), e);
            }
            return showRequestList;
        }
        List<ShowRequest> showRequestList = new ArrayList<>();
        showRequestRepository.findByCustomer(customer).forEach(showRequestList::add);
        return showRequestList;

    }
}