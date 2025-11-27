package eapli.shodrone.infrastructure.boostrapers.demo;

import eapli.framework.actions.Action;
import eapli.shodrone.proposalDocumentTemplate.application.CreateProposalDocumentTemplateController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ProposalDocumentBootstrapper implements Action {


    private static final Logger LOGGER = LogManager.getLogger(ProposalDocumentBootstrapper.class);

    private final CreateProposalDocumentTemplateController controller = new CreateProposalDocumentTemplateController();

    @Override
    public boolean execute() {
        createProposalDocumentTemplate("Template 1",
                                        "."+ File.separator+"inputfiles"+File.separator+"templateFiles"+File.separator+"Template1.txt");

        createProposalDocumentTemplate("Template 2",
                "."+ File.separator+"inputfiles"+File.separator+"templateFiles"+File.separator+"Template2.txt");
        return true;
    }


    public void createProposalDocumentTemplate(String name, String filePath) {
        try{
            controller.createProposalDocumentTemplate(name, filePath);

            LOGGER.info("Successfully registered Proposal Document Template '{}'",name);
        } catch (Exception e){
            LOGGER.error("Error creating proposal document template: {}", e.getMessage());
        }
    }
}
