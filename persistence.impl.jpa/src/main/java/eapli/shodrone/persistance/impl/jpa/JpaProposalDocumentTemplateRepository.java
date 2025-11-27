package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplate;
import eapli.shodrone.proposalDocumentTemplate.repositories.ProposalDocumentTemplateRepository;
import shodrone.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JpaProposalDocumentTemplateRepository extends JpaAutoTxRepository<ProposalDocumentTemplate, Long, Long> implements ProposalDocumentTemplateRepository {

    public JpaProposalDocumentTemplateRepository(final TransactionalContext autoTx) {
        super(autoTx, "proposalDocumentTemplateId");
    }

    public JpaProposalDocumentTemplateRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "proposalDocumentTemplateId");
    }

    @Override
    public Optional<ProposalDocumentTemplate> ofIdentity(Long id) {
        return findById(id);
    }

    @Override
    public long count() {
        return super.count();
    }

    public Optional<ProposalDocumentTemplate> findByIdentifier(Long id) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return matchOne("e.id = :id", parameters);
    }

    @Override
    public Iterable<ProposalDocumentTemplate> obtainAllProposalDocumentTemplates() {
        return super.findAll();
    }

    @Override
    public Optional<ProposalDocumentTemplate> searchByProposalDocumentTemplateName(Designation name) {
        final Map<String, Object> params = new HashMap<>();
        params.put("proposalDocumentTemplateName", name);
        return matchOne("e.proposalDocumentTemplateName = :proposalDocumentTemplateName", params);
    }
}
