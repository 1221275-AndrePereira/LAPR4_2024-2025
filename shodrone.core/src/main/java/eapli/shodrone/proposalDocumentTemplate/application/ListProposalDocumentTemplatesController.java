package eapli.shodrone.proposalDocumentTemplate.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplate;
import eapli.shodrone.proposalDocumentTemplate.repositories.ProposalDocumentTemplateRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.ArrayList;
import java.util.List;

/**
 * The type ListProposalDocumentTemplatesController
 */
@UseCaseController
public class ListProposalDocumentTemplatesController {


    private final AuthorizationService authz;
    private final ProposalDocumentTemplateRepository proposalDocumentTemplateRepository;

    public ListProposalDocumentTemplatesController(){
        this.authz = AuthzRegistry.authorizationService();
        this.proposalDocumentTemplateRepository= PersistenceContext.repositories().proposalDocumentTemplates();
    }

    public ListProposalDocumentTemplatesController(final AuthorizationService authz, final ProposalDocumentTemplateRepository proposalDocumentTemplateRepository){
        this.authz = authz;
        this.proposalDocumentTemplateRepository= proposalDocumentTemplateRepository;
    }

    /**
     * Obtain all proposal document templates and list them
     *
     * @return the list of proposal document templates
     */
    public List<ProposalDocumentTemplate> listProposalDocumentTemplates(){
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER,ShodroneRoles.CRM_COLLABORATOR,ShodroneRoles.CRM_COLLABORATOR,ShodroneRoles.POWER_USER,ShodroneRoles.ADMIN);
        Iterable<ProposalDocumentTemplate> iterable = proposalDocumentTemplateRepository.obtainAllProposalDocumentTemplates();
        List<ProposalDocumentTemplate> proposalDocumentTemplateList = new ArrayList<>();
        iterable.forEach(proposalDocumentTemplateList::add);
        return proposalDocumentTemplateList;
    }
}
