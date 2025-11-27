package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.shodrone.showProposal.application.FinalizeProposalController;
import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;

import java.util.List;

public class FinalizeProposalUI extends AbstractUI {

    private final FinalizeProposalController finalizeProposalController = new FinalizeProposalController();
    private final ListShowProposalController listShowProposalController = new ListShowProposalController();

    @Override
    protected boolean doShow() {
        List<ShowProposal> acceptedProposals = listShowProposalController.findProposalsByStatus(ProposalStatus.ACCEPTED);
        List<ShowProposal> refusedProposals = listShowProposalController.findProposalsByStatus(ProposalStatus.REFUSED);

        if (acceptedProposals.isEmpty() && refusedProposals.isEmpty()) {
            System.out.println("There are no accepted or refused proposals available to finalize.");
            return false;
        }

        System.out.println("Which proposals do you want to finalize?");
        System.out.println("1. Accepted Proposals");
        System.out.println("2. Refused Proposals");
        System.out.println("0. Cancel");
        int pchoice = Console.readOption(1, 2, 0);

        ShowProposal proposalToFinalize = null;
        boolean isForAcceptedList = false;

        switch (pchoice) {
            case 1:
                if (acceptedProposals.isEmpty()) {
                    System.out.println("There are no accepted proposals to finalize.");
                    return false;
                }
                System.out.println("\n--- Select Accepted Proposal: ---");
                proposalToFinalize = selectShowProposal(acceptedProposals);
                isForAcceptedList = true;
                break;
            case 2:
                if (refusedProposals.isEmpty()) {
                    System.out.println("There are no refused proposals to finalize.");
                    return false;
                }
                System.out.println("\n--- Select Refused Proposal: ---");
                proposalToFinalize = selectShowProposal(refusedProposals);
                isForAcceptedList = false;
                break;
            case 0:
                System.out.println("Operation Cancelled.");
                return false;
            default:
                System.out.println("Invalid option selected.");
                return false;
        }

        if (proposalToFinalize == null) {
            // selectShowProposal already prints "Operation Cancelled." if selection is cancelled
            return false;
        }

        System.out.println("\n--- Finalizing Proposal: " + proposalToFinalize.identity() + " ---");

        if (isForAcceptedList) { // Proposal is from acceptedProposals
            System.out.println("1. Close proposal");
            System.out.println("2. Archive proposal");
            System.out.println("0. Cancel");
            int choice = Console.readOption(1, 2, 0);

            switch (choice) {
                case 1:
                    try {
                        finalizeProposalController.closeProposal(proposalToFinalize);
                        System.out.println("Proposal closed successfully.");
                        return true;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error closing proposal: " + e.getMessage());
                        return false;
                    }
                    // No break needed after return
                case 2:
                    try {
                        finalizeProposalController.archiveProposal(proposalToFinalize);
                        System.out.println("Proposal archived successfully.");
                        return true;
                    } catch (Exception e) {
                        System.out.println("Error archiving proposal: " + e.getMessage());
                        return false;
                    }
                    // No break needed after return
                case 0:
                    System.out.println("Operation cancelled.");
                    return false;
                default:
                    System.out.println("Invalid option.");
                    return false;
            }
        } else { // Proposal is from refusedProposals
            // According to FinalizeProposalController, only ACCEPTED proposals can be "closed".
            // Refused proposals can be "archived".
            System.out.println("Refused proposals can be archived.");
            System.out.println("1. Archive proposal");
            System.out.println("0. Cancel");
            int choice = Console.readOption(1, 1, 0); // Options are 1 (Archive) or 0 (Cancel)

            switch (choice) {
                case 1:
                    try {
                        finalizeProposalController.archiveProposal(proposalToFinalize);
                        System.out.println("Proposal archived successfully.");
                        return true;
                    } catch (Exception e) {
                        System.out.println("Error archiving proposal: " + e.getMessage());
                        return false;
                    }
                    // No break needed after return
                case 0:
                    System.out.println("Operation cancelled.");
                    return false;
                default:
                    System.out.println("Invalid option.");
                    return false;
            }
        }
    }

    private ShowProposal selectShowProposal(List<ShowProposal> proposals) {
        if (proposals.isEmpty()) {
            System.out.println("There are no proposals in this category to select.");
            return null;
        }
        final SelectWidget<ShowProposal> proposalSelector = new SelectWidget<>("Select a Show Proposal:", proposals);
        proposalSelector.show();
        ShowProposal selectedProposal = proposalSelector.selectedElement();
        if (selectedProposal == null) {
            System.out.println("Operation Cancelled by user during selection.");
        }
        return selectedProposal;
    }

    @Override
    public String headline() {
        return "Close/Archive Proposal";
    }
}