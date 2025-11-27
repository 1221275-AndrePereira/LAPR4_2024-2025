package eapli.shodrone.proposalDocumentTemplate.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.general.domain.model.Designation;
import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplate;

import java.util.Optional;

public interface ProposalDocumentTemplateRepository extends DomainRepository<Long, ProposalDocumentTemplate> {

    Optional<ProposalDocumentTemplate> searchByProposalDocumentTemplateName(Designation name);

    Optional<ProposalDocumentTemplate> findByIdentifier(Long id);

    Iterable<ProposalDocumentTemplate> obtainAllProposalDocumentTemplates();
}
