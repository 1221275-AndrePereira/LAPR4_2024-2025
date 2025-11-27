package eapli.shodrone.droneModel.repository;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.shodrone.droneModel.domain.DroneModel;

import java.util.Optional;

public interface DroneModelRepository extends DomainRepository<Long, DroneModel> {

    Optional<DroneModel> findByModelName(String modelName);

    Iterable<DroneModel> obtainAllDroneModels();
}
