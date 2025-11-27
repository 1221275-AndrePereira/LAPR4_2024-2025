package eapli.shodrone.showProposalDocument.application;


import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplate;
import eapli.shodrone.proposalDocumentTemplate.repositories.ProposalDocumentTemplateRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalDocument;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.showrequest.domain.ShowRequest;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@UseCaseController
public class GenerateProposalDocumentController {

    private final ShowRequestRepository showRequestRepository;
    private final ShowProposalRepository showProposalRepository;
    private final ProposalDocumentTemplateRepository proposalDocumentTemplateRepository;
    private static final Logger LOGGER = LogManager.getLogger(GenerateProposalDocumentController.class);


    public GenerateProposalDocumentController() {
        AuthorizationService authz = AuthzRegistry.authorizationService();
        this.showProposalRepository = PersistenceContext.repositories().showProposals();
        this.proposalDocumentTemplateRepository = PersistenceContext.repositories().proposalDocumentTemplates();
        this.showRequestRepository = PersistenceContext.repositories().showRequestCatalogue();
    }

    public boolean generateProposalDocument(long showProposalId, long proposalDocumentTemplateId) throws IOException {

        ShowProposal showProposal = showProposalRepository.findByIdentifier(showProposalId).orElse(null);
        ProposalDocumentTemplate proposalDocumentTemplate = proposalDocumentTemplateRepository.findByIdentifier(proposalDocumentTemplateId).orElse(null);

        String documentFilePath = createDocumentFilePath(showProposalId);

        StringBuilder content = new StringBuilder();

        final DocumentGenerator generatorPlugin;

        try {
            generatorPlugin = (DocumentGenerator) Class.forName("eapli.shodrone.integrations.plugins.show.proposal.ProposalDocumentGenerator").getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            LOGGER.error("Unable to dynamically load the Generation Plugin", ex);
            throw new IllegalStateException(
                    "Unable to dynamically load the Generation Plugin: " + proposalDocumentTemplate.getProposalDocumentTemplateName(), ex);
        }

        Optional<ShowRequest> optRequest = showRequestRepository.findRequestByProposal(showProposal);
        ShodroneUser user = null;
        if (optRequest.isPresent()) {
            user = optRequest.get().getCustomer();
        }

        content = generatorPlugin.generate(content, showProposal, proposalDocumentTemplate,user);

        File documentFile = new File(documentFilePath);

        BufferedWriter writer = new BufferedWriter(new FileWriter(documentFile));
        writer.write(content.toString());
        writer.close();

        final DocumentValidator validationPlugin;

        try {
            validationPlugin = (DocumentValidator) Class.forName("eapli.shodrone.integrations.plugins.show.proposal.ProposalDocumentValidator").getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            LOGGER.error("Unable to dynamically load the Validation Plugin", ex);
            throw new IllegalStateException(
                    "Unable to dynamically load the Validation Plugin: " + "ProposalDocumentValidator", ex);
        }


        if (validationPlugin.validate(documentFilePath)) {
            showProposal.setProposalDocument(new ProposalDocument(documentFilePath));
            showProposalRepository.save(showProposal);
            return true;
        } else {
            return false;
        }
    }


    private String createDocumentFilePath(long showProposalId) {
        String fileName = "ShowProposal" + showProposalId + ".txt";
        return "." + File.separator + "outputfiles" + File.separator + fileName;

    }


}
