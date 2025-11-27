package eapli.shodrone.app.customer.console.presentation;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import eapli.shodrone.app.customer.console.DaemonClient;

import java.io.IOException;

/**
 * The ManageProposalsUI class provides a user interface for managing proposals
 * in the Shodrone customer application. It allows users to accept or reject
 * proposals and provide feedback.
 * <p>
 * This class extends AbstractUI and implements the doShow method to handle
 * user input for managing proposals.
 * <p>
 * It requires a customer VAT number and a DaemonClient instance to communicate
 * with the Shodrone daemon.
 *
 * @author Daniil Pogorielov
 */
public class ListShowsUI extends AbstractUI {
    private final String customerVat;
    private final DaemonClient client;

    public ListShowsUI(String customerVat, DaemonClient client) {
        this.customerVat = customerVat;
        this.client = client;
    }

    @Override
    public boolean doShow() {
        String response;

        try {
            response = defineRequest();

        } catch (Exception e) {
            System.out.println("Error fetching scheduled shows: " + e.getMessage());
            return false;
        }

        if (response == null || response.isBlank()) {
            System.out.println("No scheduled shows found for you.");
            return false;
        }

        final String[] shows = response.split("\n");

        boolean keepGoing = true;
        while (keepGoing) {
            System.out.println("\n--- My Scheduled Shows ---");

            for (int i = 0; i < shows.length; i++) {
                String[] parts = shows[i].split(";", 4);
                System.out.printf("%d. Date: %s, Duration: %s mins, Place: %s\n", i + 1, parts[1], parts[2], parts[3]);
            }
            System.out.println("--------------------------");
            System.out.println("0. Exit");

            int selectedIndex = Console.readInteger("Select a Show for more info: ");

            if (selectedIndex == 0) {
                keepGoing = false;
            } else if (selectedIndex > 0 && selectedIndex <= shows.length) {

                String selectedShowLine = shows[selectedIndex - 1];
                String showId = selectedShowLine.split(";", 2)[0];

                System.out.println("\nFetching details for Show ID: " + showId + "...");

                try {
                    String detailRequest = String.format("GET_SHOW_DETAILS,\"%s\"", showId);
                    String detailResponse = client.sendRequest(detailRequest);

                    System.out.println("--- Show Details ---");
                    System.out.println(detailResponse);
                    System.out.println("--------------------");
                    Console.readLine("Press Enter to continue...");

                } catch (Exception e) {
                    System.out.println("Error fetching details for show " + showId + ": " + e.getMessage());
                }
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }
        return true;
    }

    @Override
    public String headline() {
        return "Scheduled Shows";
    }

    public String defineRequest() throws IOException {
        System.out.println("1. List past shows");
        System.out.println("2. List future shows");
        System.out.println("3. List all shows");
        System.out.println("0. Exit");

        int choice;
        String request;

        while(true) {
            choice = Console.readInteger("Choose an option: ");
            switch (choice) {
                case 0:
                    return "";
                case 1:
                    request = String.format("LIST_SHOWS,\"%s\",past", customerVat);
                    return client.sendRequest(request);
                case 2:
                    request = String.format("LIST_SHOWS,\"%s\",future", customerVat);
                    return client.sendRequest(request);
                case 3:
                    request = String.format("LIST_SHOWS,\"%s\",all", customerVat);
                    return client.sendRequest(request);

                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }
}
