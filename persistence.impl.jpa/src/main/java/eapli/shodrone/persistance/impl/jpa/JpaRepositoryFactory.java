package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.framework.infrastructure.authz.repositories.impl.jpa.JpaAutoTxUserRepository;
import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.droneModel.repository.DroneModelRepository;
import eapli.shodrone.figure.repository.FigureRepository;
import eapli.shodrone.figurecategory.repositories.FigureCategoryCatalogue;
import eapli.shodrone.infrastructure.persistence.RepositoryFactory;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import eapli.shodrone.proposalDocumentTemplate.repositories.ProposalDocumentTemplateRepository;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import shodrone.Application;

public class JpaRepositoryFactory implements RepositoryFactory {

    @Override
    public TransactionalContext newTransactionalContext() {
        return JpaAutoTxRepository.buildTransactionalContext(Application.settings().persistenceUnitName(),
                Application.settings().extendedPersistenceProperties());
    }

    @Override
    public UserRepository users(final TransactionalContext autoTx) {
        return new JpaAutoTxUserRepository(autoTx);
    }

    @Override
    public UserRepository users() {
        return new JpaAutoTxUserRepository(Application.settings().persistenceUnitName(),
                Application.settings().extendedPersistenceProperties());
    }

    @Override
    public FigureRepository figures(TransactionalContext autoTx) {
        return new JpaFigureRepository(autoTx);
    }

    @Override
    public FigureRepository figures() {
        return new JpaFigureRepository(Application.settings().persistenceUnitName());
    }

    @Override
    public FigureCategoryCatalogue figureCategories(TransactionalContext autoTx) {
        return new JpaFigureCategoryCatalogue(autoTx);
    }

    @Override
    public FigureCategoryCatalogue figureCategories() {
        return new JpaFigureCategoryCatalogue(Application.settings().persistenceUnitName());
    }


    @Override
    public MaintenanceTypeRepository maintenanceTypes(TransactionalContext autoTx) {
        return new JpaMaintenanceTypeRepository(autoTx);
    }

    @Override
    public MaintenanceTypeRepository maintenanceTypes() {
        return new JpaMaintenanceTypeRepository(Application.settings().persistenceUnitName());
    }

    @Override
    public ShowRequestRepository showRequestCatalogue(TransactionalContext autoTx) {
        return new JpaShowRequestRepository(autoTx);
    }

    @Override
    public ShowRequestRepository showRequestCatalogue() {
        return new JpaShowRequestRepository(Application.settings().persistenceUnitName());
    }

    @Override
    public JpaShodroneUserRepository shodroneUsers(final TransactionalContext autoTx) {
        return new JpaShodroneUserRepository(autoTx);
    }

    @Override
    public JpaShodroneUserRepository shodroneUsers() {
        return new JpaShodroneUserRepository(Application.settings().persistenceUnitName());
    }

    @Override
    public DroneModelRepository droneModels() {
        return new JpaDroneModelRepository(Application.settings().persistenceUnitName());
    }

    @Override
    public DroneModelRepository droneModels(final TransactionalContext autoTx) {
        return new JpaDroneModelRepository(autoTx);
    }

    @Override
    public DroneInventoryRepository droneInventory() {
        return new JpaDroneInventoryRepository(Application.settings().persistenceUnitName());
    }

    @Override
    public DroneInventoryRepository droneInventory(final TransactionalContext autoTx) {
        return new JpaDroneInventoryRepository(autoTx);
    }

    @Override
    public ShowProposalRepository showProposals(final TransactionalContext autoTx) {
        return new JpaShowProposalRepository(autoTx);
    }

    @Override
    public ShowProposalRepository showProposals() {
        return new JpaShowProposalRepository(Application.settings().persistenceUnitName());
    }

    @Override
    public ProposalDocumentTemplateRepository proposalDocumentTemplates(TransactionalContext autoTx) {
        return new JpaProposalDocumentTemplateRepository(autoTx);
    }

    @Override
    public ProposalDocumentTemplateRepository proposalDocumentTemplates() {
        return new JpaProposalDocumentTemplateRepository(Application.settings().persistenceUnitName());
    }
}
