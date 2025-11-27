package eapli.shodrone.app.testing.console.presentation;

import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.application.TestShowProposalController;
import eapli.shodrone.app.testing.console.TestingClient;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.domain.simulationReport.SimulationReport;
import eapli.shodrone.showProposal.domain.simulationReport.SimulationResult;

import java.io.IOException;
import java.util.List;

public class TestShowProposalUI extends AbstractUI {

    private final TestShowProposalController testController;
    private final ListShowProposalController proposalController;

    private final TestingClient client;

    public TestShowProposalUI(TestingClient client) {
        this.client = client;
        this.proposalController = new ListShowProposalController();
        this.testController = new TestShowProposalController();
    }

    @Override
    protected boolean doShow() {
        final List<ShowProposal> proposals = proposalController.findProposalsByStatus(ProposalStatus.TESTING);
        if (proposals.isEmpty()) {
            System.out.println("There are no proposals to test found");
            return false;
        }

        final ShowProposal selectedProposal = selectShowProposal(proposals);
        if (selectedProposal == null) {
            return false;
        }


        String requestPayload = testController.message(selectedProposal);
        if (requestPayload.isEmpty()) {
            System.out.println("Could not find any drone files. Please check the execution path.");
            Console.readLine("\nPress Enter to continue...");
            return false;
        }

        try {
            String request = String.format("SIMULATE;%s", requestPayload);
            String response = client.sendRequest(request);

            if (response.isEmpty()) {
                System.out.println("No response received from the server. Please check the connection.");
            } else if (response.startsWith("SUCCESS")) {
                System.out.println("Simulation status: " + response);
                testController.assignReport(selectedProposal.identity(), new SimulationReport(SimulationResult.SUCCESS));
            } else if (response.startsWith("FAILURE")) {
                System.out.println("Simulation status: " + response);
                testController.assignReport(selectedProposal.identity(), new SimulationReport(SimulationResult.FAILURE));
            }
        } catch (IOException e) {
            System.out.println("Error managing proposal: " + e.getMessage());
        }
        Console.readLine("\nPress Enter to continue...");
        return false;
    }

    private ShowProposal selectShowProposal(List<ShowProposal> proposals) {
        final SelectWidget<ShowProposal> proposalSelector = new SelectWidget<>("Select a Show Proposal to add test:", proposals);
        proposalSelector.show();
        ShowProposal selectedProposal = proposalSelector.selectedElement();
        if (selectedProposal == null) {
            System.out.println("Operation Cancelled.");
        }
        return selectedProposal;
    }

    @Override
    public String headline() {
        return "Test Show Proposal";
    }

}
