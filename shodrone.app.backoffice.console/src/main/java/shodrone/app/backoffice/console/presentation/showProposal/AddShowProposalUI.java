package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.drone.application.ListDronesInventoryController;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.showProposal.application.CreateShowProposalController;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalNDrones;
import eapli.shodrone.showProposal.domain.proposalFields.Insurance;
import eapli.shodrone.showProposal.domain.proposalFields.ProposedDuration;
import eapli.shodrone.showProposal.domain.proposalFields.ProposedPlace;
import eapli.shodrone.showProposal.domain.proposalFields.ProposedShowDate;
import eapli.shodrone.showrequest.application.FindShowRequestController;
import eapli.shodrone.showrequest.domain.ShowRequest;
import utils.ListPrompt;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddShowProposalUI extends AbstractUI {

    FindShowRequestController findShowRequestController = new FindShowRequestController();
    CreateShowProposalController createShowProposalController = new CreateShowProposalController();
    ListDronesInventoryController  listDronesInventoryController = new ListDronesInventoryController();

    String dateFormat = "yyyy-MM-dd";

    @Override
    protected boolean doShow() {

    //----COLLECTED VALUES------------
        ShowRequest showRequest;
        ProposedShowDate proposalShowDate;
        ProposedPlace proposedPlace;
        ProposedDuration proposalDuration;
        ProposalNDrones proposalNDrones;
        Insurance insurance;
    //--------------------------------

        final int availableDrones = listDronesInventoryController.listAllDrones().size();

        final List<ShowRequest> showRequestList = findShowRequestController.listShowRequests();

        ListPrompt<ShowRequest> listPrompt = new ListPrompt<>("Show Request List", showRequestList);

        Optional<ShowRequest> opShowRequest = listPrompt.selectItem();

        if (opShowRequest.isPresent()) {
            showRequest = opShowRequest.get();
        }else{
            return false;
        }


        do {
            String input = Console.readLine("Adjust current Date of the Show(YYYY-MM-DDT00:00): " + showRequest.getShowDate() +" ? (Press ENTER to keep it)");

            if (input == null || input.isBlank()) {
                assert showRequest.getShowDate().date() != null;
                proposalShowDate = ProposedShowDate.valueOf(showRequest.getShowDate().date());
                break;
            } else {
                try {
                    ProposedShowDate newProposedDate = ProposedShowDate.valueOf(input);
                    LocalDateTime newDate = newProposedDate.date();
                    LocalDateTime today = LocalDateTime.now();

                    if (newDate.isBefore(today)) {
                        System.out.println("Error: Show date cannot be in the past.");
                    } else {
                        proposalShowDate = ProposedShowDate.valueOf(input);
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Please use yyyy-MM-ddT00:00.");
                }
            }
        } while (true);

        System.out.println("Final selected date: " + proposalShowDate);

        float latitude;
        float longitude;

        while (true) {
            String input = Console.readLine("Adjust request Place of the Show: " + showRequest.getPlace() + " ? (Press ENTER to keep it)");
            if(input == null || input.isBlank()) {
                proposedPlace = ProposedPlace.valueOf(showRequest.getPlace().getLatitude(), showRequest.getPlace().getLongitude());
                break;
            }
            while (true) {
                try {
                    latitude = Float.parseFloat(
                            Console.readLine("Enter show Latitude (Previous: "+showRequest.getPlace().getLatitude() + ") (Press ENTER to return)"));
                    if (latitude < -90.0f || latitude > 90.0f) {
                        throw new IllegalArgumentException();
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "Error: Latitude must be a number between -90.0 and 90.0 degrees. Please try again.");
                }
            }
            try {
                longitude = Float.parseFloat(
                        Console.readNonEmptyLine(
                                "Enter show Longitude (Previous: "+ showRequest.getPlace().getLongitude() +" ) (Press ENTER to return)\n(Current adjusted latitude: "+ latitude +" ) ", "Longitude must be a number"));
                if (longitude < -180.0f || longitude > 180.0f) {
                    throw new IllegalArgumentException();
                }
                proposedPlace = ProposedPlace.valueOf(latitude, longitude);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Error: Latitude must be a number between -180.0 and 180.0 degrees. Please try again.");
            }
        }

        while (true) {
            String duration = Console.readLine(
                    "Adjust request Duration of the Show: "+showRequest.getDuration()+" ? (Press ENTER to keep it)");
            try {
                if(duration.isEmpty()) {
                    proposalDuration = ProposedDuration.valueOf(showRequest.getDuration().minutes());
                    break;
                }
                int durationInt = Integer.valueOf(duration);
                if (durationInt > 0) {
                    proposalDuration = ProposedDuration.valueOf(durationInt);
                    break;
                } else {
                    System.out.println("Error: Duration must be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a whole number for duration.");
            }
        }

        while(true) {
            String n = Console.readLine("Adjust request number of Drones: "+ showRequest.getNdDrones().number()+ " ? (Press ENTER to keep it)");
            System.out.println("Available Drones: " + availableDrones);
            try {
                if (n.isEmpty()) {
                    proposalNDrones = ProposalNDrones.valueOf(showRequest.getNdDrones().number());
                    break;
                }
                int nDrones = Integer.parseInt(n);
                if (nDrones > 0 && nDrones <= availableDrones) {
                    proposalNDrones = ProposalNDrones.valueOf(nDrones);
                    break;
                }else{
                    System.out.println("Error: Invalid input. Number should be greater than 0 and less than the Available amount of drones.");
                }

            }catch (NumberFormatException e){
                System.out.println("Error: Invalid input. Please enter a whole number of drones.");
            }
        }

        while(true) {
            try {
                long n = Console.readLong("Input the desired Insurance to attribute to this show:");
                insurance = Insurance.valueOf(n);
                break;
            }catch (NumberFormatException e){
                System.out.println("Error: Invalid input. Please enter a whole number of insurance.");
            }
        }

        createShowProposalController.createShowProposal(
                showRequest,
                showRequest.getDescription().toString(),
                proposalNDrones,
                proposalDuration,
                proposedPlace,
                proposalShowDate,
                insurance
        );

        return false;
    }

    @Override
    public String headline() {
        return "New Show Proposal";
    }
}
