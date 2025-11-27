package shodrone.app.backoffice.console.presentation.showrequest;

import eapli.shodrone.shodroneusermanagement.application.ListShodroneUsersController;
import eapli.shodrone.showrequest.application.RemoveShowRequestController;
import eapli.shodrone.showrequest.application.FindShowRequestController;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import eapli.shodrone.showrequest.domain.ShowRequest;

import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import shodrone.app.backoffice.console.utility.ShodroneUserPrinter;
import shodrone.app.backoffice.console.utility.ShowRequestPrinter;

import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import java.util.Comparator;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class RemoveShowRequestUI extends AbstractUI {

    private final ListShodroneUsersController listShodroneUsersController = new ListShodroneUsersController();
    private final FindShowRequestController findShowRequestController = new FindShowRequestController();
    private final RemoveShowRequestController removeController = new RemoveShowRequestController();
    private static final Logger log = LoggerFactory.getLogger(RemoveShowRequestUI.class);

    private String getUserDisplayString(ShodroneUser user) {
        if (user == null) {
            return "[Unknown User]";
        }
        String vatStr = "[No VAT]";
        if (user.identity() != null) { // Assuming identity() gives VAT or a unique ID for ShodroneUser
            vatStr = user.identity().toString();
        }

        String emailStr = "[No Email]";
        if (user.systemUser() != null && user.systemUser().email() != null) {
            emailStr = user.systemUser().email().toString();
        }
        return String.format("(%s) [%s]", emailStr, vatStr);
    }

    @Override
    protected boolean doShow() {
        // 1. Select User
        final Iterable<ShodroneUser> usersIterable = this.listShodroneUsersController.listShodroneUsers();
        final List<ShodroneUser> shodroneUserList = StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());

        // Filter for customers (users with CRM_COLLABORATOR role)
        shodroneUserList.removeIf(user -> !user.systemUser().hasAny(ShodroneRoles.CUSTOMER));

        if (shodroneUserList.isEmpty()) {
            System.out.println("There are no customers (CRM Collaborators) registered in the system.");
            log.info("There are no customers registered in the system for show request removal.");
            return false;
        }

        System.out.println("Select a Customer to list their Show Requests for removal:");
        // Using ShodroneUserPrinter if available and appropriate, otherwise inline display
        final SelectWidget<ShodroneUser> userSelector = new SelectWidget<>(
                "Available Customers:", shodroneUserList, new ShodroneUserPrinter());
        userSelector.show();
        final ShodroneUser selectedUser = userSelector.selectedElement();

        if (selectedUser == null) {
            log.info("No user selected. Operation cancelled.");
            return false;
        }

        String selectedUserDisplayString = getUserDisplayString(selectedUser);
        log.info("User selected for show request removal: {}", selectedUserDisplayString);

        // 2. List Show Requests for the selected user
        final List<ShowRequest> userShowRequests = findShowRequestController.findShowRequestsByCustomer(selectedUser);

        if (userShowRequests.isEmpty()) {
            System.out.println("No show requests found for user: " + selectedUserDisplayString);
            log.info("No show requests found for user: {}", selectedUserDisplayString);
            return false;
        }

        // Sort if ID is available and comparable
        if (!userShowRequests.isEmpty() && userShowRequests.get(0).getId() != null) {
            userShowRequests.sort(Comparator.comparing(ShowRequest::getId));
        } else if (!userShowRequests.isEmpty()) {
            log.warn("ShowRequest ID might not be comparable; List may not be sorted by ID.");
        }

        System.out.println("\n--- Show Requests for User: " + selectedUserDisplayString + " ---");
        // Using ShowRequestPrinter if available and appropriate, otherwise inline display
        final SelectWidget<ShowRequest> requestSelector =
            new SelectWidget<>("Select Show Request to remove:", userShowRequests, new ShowRequestPrinter());
        requestSelector.show();
        final ShowRequest selectedShowRequest = requestSelector.selectedElement();

        if (selectedShowRequest == null) {
            log.info("No show request selected for removal. Operation cancelled.");
            return false;
        }

        // 3. Confirm and Remove
        System.out.println("\nYou selected to remove Show Request with ID: " + selectedShowRequest.identity());
        System.out.println("Description: " +
            (selectedShowRequest.getDescription() != null ? selectedShowRequest.getDescription().toString() : "N/A"));

        final boolean confirm = Console.readBoolean("Are you sure you want to remove this show request? (y/n)");

        if (confirm) {
            try {
                removeController.removeShowRequest(selectedShowRequest.identity());
                // The controller's removeShowRequest method already prints a success message.
                log.info("Successfully removed show request with ID: {}", selectedShowRequest.identity());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                log.error("Error removing show request: {}", e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                log.error("Unexpected error removing show request:", e);
            }
        } else {
            System.out.println("Removal cancelled by user.");
            log.info("Removal of show request ID {} cancelled by user.", selectedShowRequest.identity());
        }

        return false;
    }

    @Override
    public String headline() {
        return "Remove Show Request";
    }
}