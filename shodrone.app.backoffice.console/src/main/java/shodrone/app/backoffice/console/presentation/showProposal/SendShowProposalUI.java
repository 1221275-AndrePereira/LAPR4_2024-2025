package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.showProposal.application.SendShowProposalController;

public class SendShowProposalUI extends AbstractUI {

    private final SendShowProposalController theController = new SendShowProposalController();


    @Override
    protected boolean doShow() {
        final long showProposalId = Console.readLong("Show Proposal ID");
        try {
            this.theController.sendProposalToCustomer(showProposalId);
            System.out.println("\nShow proposal sent successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error sending show proposal: " + e.getMessage());
        }

        return false;
    }
    @Override
    public String headline() {
        return "Send Show Proposal";
    }
}
