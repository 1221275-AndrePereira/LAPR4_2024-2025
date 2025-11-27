package eapli.shodrone.maintenanceType.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.shodrone.maintenanceType.domain.MaintenanceType;

public interface MaintenanceTypeRepository extends DomainRepository<Long, MaintenanceType> {

    Iterable<MaintenanceType> obtainAllMaintenanceTypes();

}
