package shodrone.app.backoffice.console.presentation.proposalDocument;

import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.proposalDocumentTemplate.application.ListProposalDocumentTemplatesController;
import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplate;

import java.util.List;

public class ListProposalDocumentTemplatesUI extends AbstractUI {

    private final ListProposalDocumentTemplatesController theController = new ListProposalDocumentTemplatesController();

    @Override
    protected boolean doShow() {

        final List<ProposalDocumentTemplate> proposalDocumentTemplateList = this.theController.listProposalDocumentTemplates();

        if (proposalDocumentTemplateList.isEmpty()) {
            System.out.println("There are no templates for show proposal documents available");
            return false;
        } else {
            for(ProposalDocumentTemplate proposalDocumentTemplate : proposalDocumentTemplateList){
                System.out.println(proposalDocumentTemplate.getProposalDocumentTemplateName()+" - "+proposalDocumentTemplate.getProposalDocumentTemplateFilePath());
                System.out.println('\n');
            }
            return true;
        }
    }

    @Override
    public String headline() {
        return "List Proposal Document Templates";
    }
}
