package shodrone.app.backoffice.console.presentation.proposalDocument;


import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.showProposalDocument.application.GenerateProposalDocumentController;

import java.io.IOException;

public class GenerateProposalDocumentUI extends AbstractUI {


    private final GenerateProposalDocumentController theController = new GenerateProposalDocumentController();

    @Override
    protected boolean doShow() {
        final long showProposalId = Console.readLong("Please enter the id of the show proposal");
        final long proposalDocumentTemplateId = Console.readLong("Please enter the id of the template of the proposal document");
        try {
            try {
                this.theController.generateProposalDocument(showProposalId, proposalDocumentTemplateId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("\nShow proposal document was generated successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error creating show proposal document: " + e.getMessage());
        }

        return false;
    }



    @Override
    public String headline() {
        return "Generate Proposal Document";
    }
}
