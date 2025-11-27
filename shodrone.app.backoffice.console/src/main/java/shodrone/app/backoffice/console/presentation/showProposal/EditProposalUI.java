package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;


import java.util.ArrayList;
import java.util.List;

public class EditProposalUI extends AbstractUI {

    private final ListShowProposalController proposalController =  new ListShowProposalController();

    @Override
    protected boolean doShow() {
        final Iterable<ShowProposal> proposalsList = proposalController.listIncompleteProposals();

        final List<ShowProposal> proposals = new ArrayList<>();
        for (ShowProposal proposal : proposalsList) {
                proposals.add(proposal);
        }
        if (proposals.isEmpty()) {
            System.out.println("There are no incomplete proposals to modify.");
            return false;
        }

        final ShowProposal selectedProposal = selectShowProposal(proposals);
        if (selectedProposal == null) {
            return false;
        }

        selectedProposal.updateStatus();


        boolean done = false;
        while (!done) {

            boolean showGen = selectedProposal.getStatus().equals(ProposalStatus.TESTING);

            System.out.println("\n--- Editing Proposal: " + selectedProposal.identity() + " ---");
            System.out.println("1. Add/Remove Drones for this proposal");
            System.out.println("2. Add/Remove Figures for this proposal");

            if(showGen){
                System.out.println("3. Generate Drone Programs");
            }else{
                System.out.println("\n(!!!To Generate the Drone Program there must both Figures and Drones added!!!)");
            }


            System.out.println("0. Return to main menu");

            final int choice = Console.readInteger("Please select an option:");

            switch (choice) {

                case 1:
                    new AddDronesToProposalUI(selectedProposal).show();
                    break;
                case 2:
                    new AddFiguresToProposalUI(selectedProposal).show();
                    break;

                case 3:
                    if(showGen) {
                        new GenerateProgramsUI(selectedProposal).show();
                        return false;
                    }else{
                        System.out.println("Invalid option.");
                        break;
                    }

                case 0:
                    done = true;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
        return false;

    }

    private ShowProposal selectShowProposal(List<ShowProposal> proposals) {
        final SelectWidget<ShowProposal> proposalSelector = new SelectWidget<>("Select a Show Proposal to add Drones to:", proposals);
        proposalSelector.show();
        ShowProposal selectedProposal = proposalSelector.selectedElement();
        if (selectedProposal == null) {
            System.out.println("Operation Cancelled.");
        }
        return selectedProposal;
    }

    @Override
    public String headline() {
        return "Edit Show Proposals";
    }
}
