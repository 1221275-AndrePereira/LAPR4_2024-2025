package shodrone.app.backoffice.console.presentation.showrequest;

import eapli.shodrone.shodroneusermanagement.application.ListShodroneUsersController;
import eapli.shodrone.showrequest.application.FindShowRequestController;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import eapli.shodrone.showrequest.domain.ShowRequest;

import shodrone.app.backoffice.console.utility.ShodroneUserPrinter;

import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.presentation.console.AbstractUI;

import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import java.util.Comparator;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Represents a UI component for listing show requests associated with a specific user.
 * This class extends the AbstractUI and interacts with the user to select a Shodrone user
 * and display their associated show requests.
 *
 * The UI uses the ListShodroneUsersController for listing users and the FindShowRequestController
 * for retrieving show requests associated with the selected user.
 */
public class ListShowRequestUI extends AbstractUI {

    private static final Logger log = LoggerFactory.getLogger(ListShowRequestUI.class);
    private final ListShodroneUsersController listShodroneUsersController = new ListShodroneUsersController();
    private final FindShowRequestController findShowRequestController = new FindShowRequestController();

    private String getUserDisplayString(ShodroneUser user) {
        if (user == null) {
            return "[Unknown User]";
        }
        String vatStr = "[No VAT]";
        if (user.identity() != null) {
            vatStr = user.identity().toString();
        }

        String emailStr = "[No Email]";
        if (user.systemUser() != null && user.systemUser().email() != null) {
            emailStr = user.systemUser().email().toString();
        }
        // Consistent display format: (Email) [VAT]
        return String.format("(%s) [%s]", emailStr, vatStr);
    }

    @Override
    protected boolean doShow() {
        final Iterable<ShodroneUser> usersIterable = this.listShodroneUsersController.listShodroneUsers();
        final List<ShodroneUser> shodroneUserList = StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());

        //keep only customers
        shodroneUserList.removeIf(user -> !user.systemUser().hasAny(ShodroneRoles.CUSTOMER));

        if (shodroneUserList.isEmpty()) {
            System.out.println("There are no Shodrone users registered in the system.");
            log.info("There are no Shodrone users registered in the system.");
            return false;
        }

        System.out.println("Select a Shodrone User to list their Show Requests:");
        final SelectWidget<ShodroneUser> userSelector = new SelectWidget<>("Available Users:", shodroneUserList, new ShodroneUserPrinter());
        userSelector.show();
        final ShodroneUser selectedUser = userSelector.selectedElement();

        if (selectedUser == null) {
            //System.out.println("No user selected. Operation cancelled.");
            log.info("No user selected. Operation cancelled.");
            return false;
        }

        String selectedUserDisplayString = getUserDisplayString(selectedUser);
        log.info("User selected: {}", selectedUserDisplayString);



        final List<ShowRequest> userShowRequests = findShowRequestController.findShowRequestsByCustomer(selectedUser);
        if (!userShowRequests.isEmpty() && userShowRequests.getFirst().getId() != null) {
            userShowRequests.sort(Comparator.comparing(ShowRequest::getId));
        } else if (!userShowRequests.isEmpty()) {
            log.warn("ShowRequest ID might not be comparable; List may not be sorted by ID.");
        }


        System.out.println("\n--- Show Requests for User: " + selectedUserDisplayString + " ---");
        for (ShowRequest showRequest : userShowRequests) {
            System.out.println(
                    "-------------------------------------\n" +
                            showRequest.toString()
                    );
        }
        System.out.println("-------------------------------------");

        return false;
    }

    @Override
    public String headline() {
        return "List Show Requests by User";
    }
}