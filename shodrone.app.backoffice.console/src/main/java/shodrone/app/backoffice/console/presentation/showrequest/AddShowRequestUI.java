package shodrone.app.backoffice.console.presentation.showrequest;

import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.general.domain.model.Description;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import eapli.shodrone.shodroneusermanagement.application.ListShodroneUsersController;
import eapli.shodrone.showProposal.domain.proposalFields.ProposedShowDate;
import eapli.shodrone.showrequest.application.RegisterShowRequestController;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.figure.application.ListFigureController;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.showrequest.domain.*;

import shodrone.app.backoffice.console.utility.ShodroneUserPrinter;
import shodrone.app.backoffice.console.utility.FigurePrinter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class AddShowRequestUI extends AbstractUI {

    private final RegisterShowRequestController showRequestcontroller = new RegisterShowRequestController();
    private final ListShodroneUsersController listShodroneUsersController = new ListShodroneUsersController();
    private final ListFigureController listFigureController = new ListFigureController();
    private static final Logger log = LoggerFactory.getLogger(AddShowRequestUI.class);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected boolean doShow() {
        final Iterable<ShodroneUser> usersIterable = this.listShodroneUsersController.listShodroneUsers();

        final List<ShodroneUser> shodroneUserList = StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());

        //keep only customers
        shodroneUserList.removeIf(user -> !user.systemUser().hasAny(ShodroneRoles.CUSTOMER));

        if (shodroneUserList.isEmpty()) {
            System.out.println("There are no customers registered in the system.");
            //log.info("There are no Shodrone users registered in the system.");
            return false;
        }

        System.out.println("Select a customer to list their Show Requests:");
        final SelectWidget<ShodroneUser> userSelector =
                new SelectWidget<>("Available Users:", shodroneUserList, new ShodroneUserPrinter());
        userSelector.show();
        final ShodroneUser selectedUser = userSelector.selectedElement();

        if (selectedUser == null) {
            System.out.println("No customer selected. Operation cancelled.");
            //log.info("No user selected. Operation cancelled.");
            return false;
        }

        final String descriptionText = Console.readNonEmptyLine("Show Description",
                "Enter show request description: ");
        Description description = Description.valueOf(descriptionText);

        float latitude = 0.0f;
        boolean lsuccess = false;

        while (!lsuccess) {
            try {
                latitude = Float.parseFloat(
                        Console.readNonEmptyLine("Enter show Latitude", "Latitude must be a number"));
                if (latitude < -90.0f || latitude > 90.0f) {
                    throw new IllegalArgumentException();
                }
                lsuccess = true; // If validation passes, exit the loop
            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Error: Latitude must be a number between -90.0 and 90.0 degrees. Please try again.");
            }
        }

        float longitude = 0.0f;
        boolean llsuccess = false;

        while (!llsuccess) {
            try {
                longitude = Float.parseFloat(
                        Console.readNonEmptyLine(
                                "Enter show Longitude", "Longitude must be a number"));
                if (longitude < -180.0f || longitude > 180.0f) {
                    throw new IllegalArgumentException();
                }
                llsuccess = true; // If validation passes, exit the loop
            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Error: Latitude must be a number between -180.0 and 180.0 degrees. Please try again.");
            }
        }

        RequestPlace requestPlace = RequestPlace.valueOf(latitude, longitude);

        final Calendar requestCreateDateCalendar = Calendar.getInstance();
        String formattedCreateDateCalendar = dateFormat.format(requestCreateDateCalendar.getTime());

        final RequestCreationDate requestCreateDate = RequestCreationDate.valueOf(formattedCreateDateCalendar);

        RequestShowDate requestShowDate;

        do {
            String input = Console.readLine("Add show date of the Show(YYYY-MM-DDT00:00): ");

            if (input == null || input.isBlank()) {
                System.out.println("Error: Show date cannot be empty.");
            } else {
                try {
                    RequestShowDate newProposedDate = RequestShowDate.valueOf(input);
                    LocalDateTime newDate = newProposedDate.date();
                    LocalDateTime theToday = LocalDateTime.now();

                    if (newDate.isBefore(theToday)) {
                        System.out.println("Error: Show date cannot be in the past.");
                    } else {
                        requestShowDate = RequestShowDate.valueOf(input);
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Please use yyyy-MM-ddT00:00.");
                }
            }
        } while (true);

        int requestDurationInt;
        while (true) {
            int durationInt = Console.readInteger(
                    "Show Duration (in minutes)");
            try {
                requestDurationInt = durationInt;
                if (requestDurationInt > 0) {
                    break; // Valid positive integer
                } else {
                    System.out.println("Error: Duration must be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a whole number for duration.");
            }
        }

        final RequestDuration requestDuration = RequestDuration.valueOf(requestDurationInt);

        int requestNdDronesInt;
        while (true) {
            int dronesInt = Console.readInteger(
                    "Number of Drones");
            try {
                requestNdDronesInt = dronesInt;
                if (requestNdDronesInt > 0) {
                    break; // Valid positive integer
                } else {
                    System.out.println("Error: Number of drones must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a whole number for the number of drones.");
            }
        }

        RequestNDrones requestNdDrones = RequestNDrones.valueOf(requestNdDronesInt);

        final List<Figure> allFigures = this.listFigureController.listActivePublicFigures();
        final List<Figure> selectedFigures = new ArrayList<>();
        System.out.println("Select a figures:");

        boolean continueAddingFigures = true;

        while(continueAddingFigures) {
            final SelectWidget<Figure> figureSelector =
                    new SelectWidget<>("Available Figures:", allFigures, new FigurePrinter());
            figureSelector.show();
            Figure selectedFigure = figureSelector.selectedElement();

            if (selectedFigure != null) {
                // check if this figure already in the list
                if (selectedFigures.contains(selectedFigure)) {
                    System.out.println("This figure is already selected. Please choose another one.");
                    continue;
                }
                selectedFigures.add(selectedFigure);
            }

            if (selectedFigure == null) {
                System.out.println("Operation ended.");
                continueAddingFigures = false;
            }
        }

        if (selectedFigures.isEmpty()) {
            System.out.println("No figures selected. Operation cancelled.");
            return false;
        }

        try {
            this.showRequestcontroller.registerShowRequest(
                    description,
                    requestPlace,
                    requestCreateDate,
                    requestShowDate,
                    requestDuration,
                    requestNdDrones,
                    selectedUser,
                    selectedFigures
                );
            System.out.println("\nShow request created successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error creating show request: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String headline() {
        return "Add Show Request";
    }

}