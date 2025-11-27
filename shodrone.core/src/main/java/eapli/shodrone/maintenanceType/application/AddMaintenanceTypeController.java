package eapli.shodrone.maintenanceType.application;


import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.maintenanceType.domain.MaintenanceType;
import eapli.shodrone.maintenanceType.domain.MaintenanceTypeBuilder;
import eapli.shodrone.maintenanceType.domain.MaintenanceTypeName;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class AddMaintenanceTypeController {


    private final AuthorizationService authz;
    private final MaintenanceTypeRepository maintenanceTypeRepository;


    public AddMaintenanceTypeController() {
        this.authz = AuthzRegistry.authorizationService();
        this.maintenanceTypeRepository = PersistenceContext.repositories().maintenanceTypes();
    }

    public AddMaintenanceTypeController(final AuthorizationService authz, final MaintenanceTypeRepository maintenanceTypeRepository) {
        this.authz = authz;
        this.maintenanceTypeRepository = maintenanceTypeRepository;
    }

    public MaintenanceType addMaintenanceType(MaintenanceTypeName maintenanceTypeName){
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.DRONE_TECH, ShodroneRoles.ADMIN, ShodroneRoles.POWER_USER);
        MaintenanceTypeBuilder builder=new MaintenanceTypeBuilder();

        MaintenanceType maintenanceType=builder.withName(maintenanceTypeName).build();

        return save(maintenanceType);
    }


    public MaintenanceType save(MaintenanceType maintenanceType) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.DRONE_TECH, ShodroneRoles.ADMIN, ShodroneRoles.POWER_USER);

        maintenanceType = maintenanceTypeRepository.save(maintenanceType);

        return maintenanceType;
    }




}
