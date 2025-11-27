package shodrone.app.backoffice.console.presentation.proposalDocument;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.proposalDocumentTemplate.application.CreateProposalDocumentTemplateController;

public class CreateProposalDocumentTemplateUI extends AbstractUI {

    private final CreateProposalDocumentTemplateController theController = new CreateProposalDocumentTemplateController();

    @Override
    protected boolean doShow() {
        final String name = Console.readNonEmptyLine("Template Name", "Please enter the name of the template");
        final String filePath = Console.readNonEmptyLine("Template File Path", "Please enter the file path for the template");
        try {
            this.theController.createProposalDocumentTemplate(name, filePath);
            System.out.println("\nShow Proposal Document Template created successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error creating show proposal document template: " + e.getMessage());
        }

        return false;
    }




    @Override
    public String headline() {
        return "Create Proposal Document Template";
    }
}
