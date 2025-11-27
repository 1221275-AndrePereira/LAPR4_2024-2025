package eapli.shodrone.persistence.impl.inmemory;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.shodrone.figure.repository.FigureRepository;
import eapli.shodrone.figurecategory.repositories.FigureCategoryCatalogue;
import eapli.shodrone.infrastructure.persistence.RepositoryFactory;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import eapli.shodrone.proposalDocumentTemplate.repositories.ProposalDocumentTemplateRepository;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;

public abstract class InMemoryRepositoryFactory implements RepositoryFactory {
    @Override
    public TransactionalContext newTransactionalContext() {
        return null;
    }

    @Override
    public UserRepository users(TransactionalContext autoTx) {
        return null;
    }

    @Override
    public UserRepository users() {
        return null;
    }

    @Override
    public FigureRepository figures(TransactionalContext autoTx) {
        return new InMemoryFigureRepository();
    }

    @Override
    public FigureRepository figures() {
        return figures(null);
    }

    @Override
    public FigureCategoryCatalogue figureCategories(TransactionalContext autoTx) {
        return null;
    }

    @Override
    public FigureCategoryCatalogue figureCategories() {
        return null;
    }

    @Override
    public MaintenanceTypeRepository maintenanceTypes(TransactionalContext autoTx) {
        return null;
    }

    @Override
    public MaintenanceTypeRepository maintenanceTypes() {
        return null;
    }

    @Override
    public ShowRequestRepository showRequestCatalogue(TransactionalContext autoTx) {
        return null;
    }

    @Override
    public ShowRequestRepository showRequestCatalogue() {
        return null;
    }

    @Override
    public ShodroneUserRepository shodroneUsers(TransactionalContext txCtx) {
        return null;
    }

    @Override
    public ShodroneUserRepository shodroneUsers() {
        return null;
    }

    @Override
    public ProposalDocumentTemplateRepository proposalDocumentTemplates(TransactionalContext autoTx) {
        return null;
    }

    @Override
    public ProposalDocumentTemplateRepository proposalDocumentTemplates() {
        return null;
    }
}
