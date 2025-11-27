package eapli.shodrone.maintenanceType.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.maintenanceType.domain.MaintenanceType;
import eapli.shodrone.maintenanceType.domain.MaintenanceTypeName;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.Optional;

@UseCaseController
public class EditMaintenanceTypeController {

    private final AuthorizationService authz;
    private final MaintenanceTypeRepository maintenanceTypeRepository; // Optional, for testability
    private final TransactionalContext tx;

    // Production constructor
    public EditMaintenanceTypeController() {
        this.authz = AuthzRegistry.authorizationService();
        this.tx = PersistenceContext.repositories().newTransactionalContext();
        this.maintenanceTypeRepository = PersistenceContext.repositories().maintenanceTypes(tx);
    }

    // Test constructor
    public EditMaintenanceTypeController(AuthorizationService authz, MaintenanceTypeRepository repo) {
        this.authz = authz;
        this.maintenanceTypeRepository = repo;
        this.tx = null;
    }

    public MaintenanceType editMaintenanceType(Long id, MaintenanceTypeName newName) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.DRONE_TECH, ShodroneRoles.ADMIN, ShodroneRoles.POWER_USER);

        if (tx != null) tx.beginTransaction();
        try {
            Optional<MaintenanceType> opt = maintenanceTypeRepository.ofIdentity(id);
            if (opt.isEmpty()) {
                throw new IllegalArgumentException("Maintenance type not found.");
            }

            MaintenanceType maintenanceType = opt.get();

            if (maintenanceType.hasMaintenanceRecords()) {
                throw new IllegalStateException("Cannot edit a maintenance type with maintenance records.");
            }

            maintenanceType.changeName(newName);
            MaintenanceType updated = maintenanceTypeRepository.save(maintenanceType);

            if (tx != null) tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}