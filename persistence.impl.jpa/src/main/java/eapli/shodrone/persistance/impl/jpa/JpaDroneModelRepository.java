package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.droneModel.repository.DroneModelRepository;
import shodrone.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JpaDroneModelRepository
        extends JpaAutoTxRepository<DroneModel, Long, Long> implements DroneModelRepository {

    public JpaDroneModelRepository(final TransactionalContext autoTx) {
        super(autoTx, "id");
    }

    public JpaDroneModelRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "id");
    }

    @Override
    public Iterable<DroneModel> obtainAllDroneModels() {
        return findAll();
    }

    public Optional<DroneModel> findByModelName(String modelNumber) {
        final Map<String, Object> params = new HashMap<>();
        params.put("modelName", modelNumber);
        return matchOne("e.modelName = :modelName", params);
    }

    @Override
    public Optional<DroneModel> ofIdentity(Long id) {
        return findById(id);
    }

    @Override
    public void deleteOfIdentity(Long id) {
        Optional<DroneModel> droneModel = findById(id);
        droneModel.ifPresent(this::delete);
    }
}
