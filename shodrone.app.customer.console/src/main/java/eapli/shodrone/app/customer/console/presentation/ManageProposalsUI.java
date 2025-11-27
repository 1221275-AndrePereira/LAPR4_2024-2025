package eapli.shodrone.app.customer.console.presentation;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import eapli.shodrone.app.customer.console.DaemonClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

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
public class ManageProposalsUI extends AbstractUI {
    private final String customerVat;
    private final DaemonClient client;

    public ManageProposalsUI(String customerVat, DaemonClient client) {
        this.customerVat = customerVat;
        this.client = client;
    }

    @Override
    protected boolean doShow() {

        String code = Console.readLine("Insert Code referent to the desired Proposal:");
        String response = String.format("USE_CODE,\"%s\"",code);

        long proposalID = Long.parseLong(response);

        String[] parts = response.split(";", 3);

        String fileName = parts[0];
        long fileSize = Long.parseLong(parts[1]);
        String base64Content = parts[2];

        byte[] fileBytes = Base64.getDecoder().decode(base64Content);

        try {
            Files.write(Paths.get(fileName), fileBytes);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
            return false;
        }

        System.out.println("Successfully downloaded '" + fileName + "' (" + fileSize + " bytes).");

        evaluateProposal(proposalID);

        return false;
    }

    @Override
    public String headline() {
        return "Manage a Proposal";
    }


    public void evaluateProposal(long proposalId){

        System.out.println("1. Accept Proposal");
        System.out.println("2. Reject Proposal");
        System.out.println("0. Cancel");
        int choice = Console.readOption(1,2,0);
        if (choice == 0) return;

        final String feedback = Console.readLine("Please provide a short feedback message:");
        String command = (choice == 1) ? "ACCEPT_PROPOSAL" : "REJECT_PROPOSAL";

        try {
            String request = String.format("%s,\"%s\",%d,\"%s\"", command, customerVat, proposalId, feedback);
            String response = client.sendRequest(request);

            if (response.startsWith("OK")) {
                System.out.println("\nProposal status updated successfully!");
            } else {
                System.out.println("\nFailed to update proposal: " + response);
            }
        } catch (Exception e) {
            System.out.println("Error managing proposal: " + e.getMessage());
        }
        Console.readLine("\nPress Enter to continue...");
    }
}
