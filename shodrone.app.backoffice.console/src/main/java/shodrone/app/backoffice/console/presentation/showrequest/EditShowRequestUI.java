package shodrone.app.backoffice.console.presentation.showrequest;

import eapli.shodrone.shodroneusermanagement.application.ListShodroneUsersController;
import eapli.shodrone.showrequest.application.EditShowRequestController;
import eapli.shodrone.showrequest.application.FindShowRequestController;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import eapli.shodrone.showrequest.domain.*;

import shodrone.app.backoffice.console.utility.ShodroneUserPrinter;

import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.general.domain.model.Description;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Represents a UI component for editing show requests.
 * This class extends the AbstractUI and interacts with the user to gather the necessary
 * information for editing an existing show request, including customer selection, show details, and
 * validation of the input data.
 *
 * The UI uses the EditShowRequestController for managing the underlying logic of
 * editing a show request. It also utilizes FindShowRequestController for finding existing show requests
 * and ListShodroneUsersController for listing and selecting customers.
 */
public class EditShowRequestUI extends AbstractUI {

    private final ListShodroneUsersController listShodroneUsersController = new ListShodroneUsersController();
    private final FindShowRequestController findController = new FindShowRequestController();
    private final EditShowRequestController controller = new EditShowRequestController();
    private static final Logger log = LoggerFactory.getLogger(EditShowRequestUI.class);

    Description showDescription;
    RequestPlace showPlace;
    RequestShowDate showDate;
    RequestDuration showDuration;
    RequestNDrones showNdDrones;
    ShowRequest showRequest;
    ShodroneUser selectedUser;
    int newRequestNdDrones;
    int newRequestDuration;
    Calendar newRequestShowDate;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected boolean doShow() {
        Long showRequestId;

        try {
            showRequestId = Console.readLong("Enter show request ID: ");
        } catch (NumberFormatException e) {
            log.warn(e.getMessage());
            return false;
        }

        // Find Show Request
        showRequest = findController.findShowRequestById(showRequestId);
        if (showRequest == null) {
            log.error("No show request found with ID: {}", showRequestId);
            System.out.println("No show request found with ID: " + showRequestId);
            return false;
        }

        log.debug("Found show request: {}", showRequest); // debug log message

        // Ask user for input
        String descriptionText = Console.readLine(
                "Show Description [" + showRequest.getDescription().toString() + "]: "
        );
        showDescription = descriptionText.isEmpty()
                ? showRequest.getDescription()
                : Description.valueOf(descriptionText);


//        String placeText = Console.readLine(
//                "Show Place [" + showRequest.getPlace().toString() + "]: "
//        );

        final boolean changePlace= Console.readBoolean(
                showRequest.getPlace().toString() + "\n" + "Do you want to change the place? (y/n)"
        );

        Float latitude;
        Float longitude;
        if (changePlace) {
            latitude = Float.valueOf(Console.readLine(
                    "Enter latitude [" + showRequest.getPlace().getLatitude() + "]: "
            ));
            longitude = Float.valueOf(Console.readLine(
                    "Enter longitude [" + showRequest.getPlace().getLongitude() + "]: "));
            showPlace = RequestPlace.valueOf(latitude, longitude);
        } else {
            showPlace = showRequest.getPlace();
        }

        while (true) {
            try {
                String dateString = Console.readLine(
                        "Show Date [" + showRequest.getRequestShowDate().toString() + "]: "
                );
                if (dateString.isEmpty()) {
                    showDate = showRequest.getRequestShowDate();
                    break; // Use existing value
                }
                newRequestShowDate = Calendar.getInstance();
                newRequestShowDate.setTime(dateFormat.parse(dateString));
                showDate = RequestShowDate.valueOf(dateFormat.format(newRequestShowDate.getTime()));
                break; // Valid date
            } catch (Exception e) {
                System.out.println("Error: Invalid date format. Please enter a valid date (yyyy-MM-dd).");
            }
        }


        while(true) {
            try {
                String durationString = Console.readLine(
                        "Show Duration [" + showRequest.getDuration().toString() + " minutes]: ");
                int durationInt = Integer.valueOf(durationString);
                if (durationString.isBlank()) {
                    showDuration = showRequest.getDuration();
                }
                else {
                    if (durationInt > 0) {
                        showDuration = RequestDuration.valueOf(Math.toIntExact(durationInt));
                    }
                }
                break; // Use existing value
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a whole number for duration.");
                break;
            }
        }

        while (true) {
            try {
                String droneString = Console.readLine(
                        "Number of Drones [" + showRequest.getNdDrones().toString() + "]:");
                int dronesInt = Integer.parseInt(droneString);
                if (droneString.isEmpty()) {
                    showNdDrones = showRequest.getNdDrones();
                }
                if (dronesInt > 0) {
                    newRequestNdDrones = dronesInt;
                    showNdDrones = RequestNDrones.valueOf(newRequestNdDrones);
                }
                if (dronesInt == 0) {
                    System.out.println("Error: Number of drones must be greater than zero.");
                    continue; // Ask again
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a whole number for the number of drones.");
                break;
            }
        }


        //ask if user want to change a customer
        final boolean changeCustomer = Console.readBoolean("Do you want to change the customer? (y/n)");

        if (changeCustomer) {
            final Iterable<ShodroneUser> usersIterable = this.listShodroneUsersController.listShodroneUsers();
            final List<ShodroneUser> shodroneUserList = StreamSupport.stream(usersIterable.spliterator(), false)
                    .collect(Collectors.toList());

            //keep only customers
            shodroneUserList.removeIf(user -> !user.systemUser().hasAny(ShodroneRoles.CRM_COLLABORATOR));
            // remove Shodrone user that already own this request
            shodroneUserList.removeIf(user -> user.equals(showRequest.getCustomer()));

            if (shodroneUserList.isEmpty()) {
                System.out.println("There are no Shodrone users registered in the system.");
                log.info("There are no Shodrone users registered in the system.");
                return false;
            }

            System.out.println("Select a Shodrone User to list their Show Requests:");
            final SelectWidget<ShodroneUser> userSelector = new SelectWidget<>(
                    "Available Users:",
                    shodroneUserList,
                    new ShodroneUserPrinter()
            );
            userSelector.show();

            selectedUser = userSelector.selectedElement();

            if (selectedUser == null) {
                //System.out.println("No user selected. Operation cancelled.");
                log.info("No user selected. Operation cancelled.");
                return false;
            }

            // Edit Show Request
            try {
                ShowRequest editedRequest = controller.editShowRequest(
                        showRequest,
                        showDescription,
                        showPlace,
                        showDate,
                        showDuration,
                        showNdDrones,
                        selectedUser

                );
                System.out.println("Show request edited successfully.");
                //show edited data
                //System.out.println(editedRequest);
            } catch (final IllegalArgumentException | IllegalStateException e) {
                log.error("Error editing show request: {}", e.getMessage());
            }
        }
        else {
            // Edit Show Request
            try {
                ShowRequest editedRequest = controller.editShowRequest(
                        showRequest,
                        showDescription,
                        showPlace,
                        showDate,
                        showDuration,
                        showNdDrones,
                        showRequest.getCustomer()
                );
                System.out.println("Show request edited successfully.");
                //show edited data
                //System.out.println(editedRequest);
            } catch (final IllegalArgumentException | IllegalStateException e) {
                log.error("Error editing show request: {}", e.getMessage());
            }
        }

        return false;
    }

    @Override
    public String headline() {
        return "Edit Show Request";
    }
}
