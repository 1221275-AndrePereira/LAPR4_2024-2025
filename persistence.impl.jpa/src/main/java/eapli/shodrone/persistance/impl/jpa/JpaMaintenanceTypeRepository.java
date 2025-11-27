package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.shodrone.maintenanceType.domain.MaintenanceType;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import shodrone.Application;

import java.util.Optional;

public class JpaMaintenanceTypeRepository extends JpaAutoTxRepository<MaintenanceType, Long, Long> implements MaintenanceTypeRepository {

    public JpaMaintenanceTypeRepository(final TransactionalContext autoTx) {
        super(autoTx, "maintenanceTypeId");
    }

    public JpaMaintenanceTypeRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "maintenanceTypeId");
    }

    @Override
    public Optional<MaintenanceType> ofIdentity(Long id) {
        return findById(id);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public Iterable<MaintenanceType> obtainAllMaintenanceTypes() {
        return super.findAll();
    }
}
