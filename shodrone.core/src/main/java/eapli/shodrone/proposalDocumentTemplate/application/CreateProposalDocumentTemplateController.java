package eapli.shodrone.proposalDocumentTemplate.application;


import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplate;
import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplateBuilder;
import eapli.shodrone.proposalDocumentTemplate.repositories.ProposalDocumentTemplateRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;


/**
 * The type CreateProposalDocumentTemplateController
 */
@UseCaseController
public class CreateProposalDocumentTemplateController {

    private final ProposalDocumentTemplateRepository proposalDocumentTemplateRepository;

    public CreateProposalDocumentTemplateController(){
        this.proposalDocumentTemplateRepository= PersistenceContext.repositories().proposalDocumentTemplates();
    }

    public CreateProposalDocumentTemplateController(final ProposalDocumentTemplateRepository proposalDocumentTemplateRepository){
        this.proposalDocumentTemplateRepository= proposalDocumentTemplateRepository;
    }


    /**
     * Create a template for the show proposal document(Add them to a repository)
     *
     * @param name the name of template for the show proposal document
     * @param filePath the file path for the template for the show proposal document
     * @return the template for the show proposal document
     */
    public ProposalDocumentTemplate createProposalDocumentTemplate(String name, String filePath) {
        ProposalDocumentTemplateBuilder builder = new ProposalDocumentTemplateBuilder();

        ProposalDocumentTemplate proposalDocumentTemplate = builder.withName(name).withFilePath(filePath).build();

        return save(proposalDocumentTemplate);

    }


    /**
     * Save the template for the show proposal document
     *
     * @param proposalDocumentTemplate the template for the show proposal document
     * @return the template for the show proposal document
     */
    public ProposalDocumentTemplate save(ProposalDocumentTemplate proposalDocumentTemplate) {

        proposalDocumentTemplate = proposalDocumentTemplateRepository.save(proposalDocumentTemplate);

        return proposalDocumentTemplate;
    }
}
