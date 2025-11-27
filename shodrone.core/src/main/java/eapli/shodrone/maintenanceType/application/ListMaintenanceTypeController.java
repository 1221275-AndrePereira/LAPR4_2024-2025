package eapli.shodrone.maintenanceType.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.maintenanceType.domain.MaintenanceType;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.List;

@UseCaseController
public class ListMaintenanceTypeController {

    private final AuthorizationService authz;
    private final MaintenanceTypeRepository maintenanceTypeRepository;

    public ListMaintenanceTypeController() {
        this.authz = AuthzRegistry.authorizationService();
        this.maintenanceTypeRepository = PersistenceContext.repositories().maintenanceTypes();
    }

    public ListMaintenanceTypeController(final AuthorizationService authz, final MaintenanceTypeRepository maintenanceTypeRepository) {
        this.authz = authz;
        this.maintenanceTypeRepository = maintenanceTypeRepository;
    }

    public Iterable<MaintenanceType> listAllMaintenanceTypes() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.DRONE_TECH, ShodroneRoles.ADMIN, ShodroneRoles.POWER_USER);
        return maintenanceTypeRepository.findAll();
    }

    public List<MaintenanceType> listMaintenanceTypes() {
        Iterable<MaintenanceType> all = listAllMaintenanceTypes();
        List<MaintenanceType> list = new java.util.ArrayList<>();
        all.forEach(list::add);
        return list;
    }
}