package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.shodrone.drone.domain.Drone;
import eapli.shodrone.drone.domain.DroneStatus;
import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.droneModel.domain.DroneModel;
import shodrone.Application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class JpaDroneInventoryRepository
        extends JpaAutoTxRepository<Drone, Long, Long>
        implements DroneInventoryRepository {

    public JpaDroneInventoryRepository(final TransactionalContext autoTx) {
        super(autoTx, "serialNumber");
    }

    public JpaDroneInventoryRepository(final String puname) {
        super(
            puname,
            Application.settings().extendedPersistenceProperties(),
            "serialNumber"
        );
    }

    @Override
    public List<Drone> allDrones() {
        return (List<Drone>) findAll();
    }

    @Override
    public List<Drone> allActiveDrones() {
        final Map<String, Object> params = new HashMap<>();
        params.put("status", DroneStatus.ACTIVE);
        return match("e.status = :status", params);
    }

    public List<Drone> allActiveDronesOfModel(DroneModel droneModel) {
        final Map<String, Object> params = new HashMap<>();
        params.put("status", DroneStatus.ACTIVE);
        params.put("model", droneModel);

        return match("e.status = :status AND e.model = :model", params);
    }

//    @Override
//    public Optional<Drone> removeDrone(String serialNumber) {
//        final Map<String, Object> params = new HashMap<>();
//        params.put("serialNumber", serialNumber);
//        Optional<Drone> droneToRemove = matchOne("e.serialNumber = :serialNumber", params);
//        droneToRemove.ifPresent(this::delete);
//        return droneToRemove;
//    }

    public boolean exists(Long serialNumber) {
        final Map<String, Object> params = new HashMap<>();
        params.put("serialNumber", serialNumber);
        return matchOne("e.serialNumber.serialNumber = :serialNumber", params).isPresent();
    }

    public boolean changeDroneStatus(Long serialNumber, String newStatus) {
        final Map<String, Object> params = new HashMap<>();
        params.put("serialNumber", serialNumber);
        Optional<Drone> droneOpt = matchOne("e.serialNumber.serialNumber = :serialNumber", params);

        if (droneOpt.isPresent()) {
            Drone drone = droneOpt.get();
            drone.setStatus(DroneStatus.valueOf(newStatus));
            save(drone);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Drone> ofIdentity(Long serialNumber) {
        return findById(serialNumber);
    }

    @Override
    public long count() {
        return super.count();
    }
}
