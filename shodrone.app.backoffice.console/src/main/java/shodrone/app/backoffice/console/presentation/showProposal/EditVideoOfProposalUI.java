package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.shodrone.showProposal.application.AddVideoToProposalController;
import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;

import java.util.List;

public class EditVideoOfProposalUI extends AbstractUI  {

    private final ListShowProposalController listShowProposalController =  new ListShowProposalController();
    private final AddVideoToProposalController addVideoController = new AddVideoToProposalController();

    @Override
    protected boolean doShow() {
        List<ShowProposal> proposals = listShowProposalController.findProposalsByStatus(ProposalStatus.SAFE);
        if (proposals.isEmpty()) {
            System.out.println("There are no proposals to add a video to.");
            return false;
        }

        System.out.println("\n--- Select Proposal: ---");
        ShowProposal proposal = selectShowProposal(proposals);
        if (proposal == null) {
            return false;
        }

        System.out.println("\n--- Editing Proposal: " + proposal.identity() + " ---");
        System.out.println("1. Add Video for this proposal");
        System.out.println("2. Remove Video for this proposal");
        int choice = Console.readOption(1, 2, 0);

        switch (choice) {
            case 1:
                String videoLink = Console.readNonEmptyLine(
                        "Enter video link: (Enter to help)",
                        "Provide a link or a path to the file"
                    );
                addVideoController.addVideoToProposal(proposal, videoLink); break;
            case 2: addVideoController.removeVideoFromProposal(proposal); break;
            case 0: return false;
        }

        return false;
    }

    private ShowProposal selectShowProposal(List<ShowProposal> proposals) {
        final SelectWidget<ShowProposal> proposalSelector = new SelectWidget<>("Select a Show Proposal:", proposals);
        proposalSelector.show();
        ShowProposal selectedProposal = proposalSelector.selectedElement();
        if (selectedProposal == null) {
            System.out.println("Operation Cancelled.");
        }
        return selectedProposal;
    }

    @Override
    public String headline() {
        return "Add/Remove Video of Proposal";
    }

}
