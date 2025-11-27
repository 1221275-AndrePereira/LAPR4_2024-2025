package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import java.util.List;

public class ListShowProposalUI extends AbstractUI {

    private final ListShowProposalController proposalController =  new ListShowProposalController();

    @Override
    protected boolean doShow() {
        System.out.println("Which proposals would you like to list?");
        System.out.println("1. Pending Proposals");
        System.out.println("2. Accepted Proposals");
        System.out.println("3. Refused Proposals");
        System.out.println("4. Incomplete Proposals");
        System.out.println("5. Testing Proposals");
        System.out.println("6. Failed Proposals");
        System.out.println("7. Safe Proposals");
        System.out.println("0. Cancel");
        int choice = Console.readOption(1, 7, 0);

        switch (choice) {
            case 1: lister(ProposalStatus.PENDING); break;
            case 2: lister(ProposalStatus.ACCEPTED); break;
            case 3: lister(ProposalStatus.REFUSED); break;
            case 4: lister(ProposalStatus.INCOMPLETE); break;
            case 5: lister(ProposalStatus.TESTING); break;
            case 6: lister(ProposalStatus.FAILED); break;
            case 7: lister(ProposalStatus.SAFE); break;
            case 0: return false;
        }

        return false;
    }

    private void lister (ProposalStatus proposalStatus) {
        final List<ShowProposal> proposals = proposalController.findProposalsByStatus(proposalStatus);
        if (proposals.isEmpty()) {
            System.out.println("There are no proposals with status " + proposalStatus + ".");
            return;
        }
        proposals.forEach(System.out::println);
    }

    @Override
    public String headline() {
        return "List Show Proposals";
    }

}
