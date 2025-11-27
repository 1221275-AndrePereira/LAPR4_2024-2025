package eapli.shodrone.infrastructure.boostrapers.demo;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.actions.Action;

import eapli.shodrone.droneModel.application.ListDroneModelController;
import eapli.shodrone.drone.application.AddDronesInventoryController;
import eapli.shodrone.droneModel.application.AddDroneModelController;
import eapli.shodrone.droneModel.dto.WindToleranceStepDTO;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.drone.domain.SerialNumber;
import eapli.shodrone.drone.domain.DroneStatus;
import eapli.shodrone.drone.domain.Drone;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DroneBootstrapper implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(DroneBootstrapper.class);

    private final AddDronesInventoryController addDronesController = new AddDronesInventoryController();
    private final AddDroneModelController addDroneModelController = new AddDroneModelController();
    private final ListDroneModelController listDroneModelController = new ListDroneModelController();


    @Override
    public boolean execute() {
        // Drone Models
        DroneModel model1 = createDroneModel("DJI Matrice 300 RTK", "DroneOne");
        DroneModel model2 = createDroneModel("Parrot Anafi USA", "DroneTwo");
        DroneModel model3 = createDroneModel("Yuneec H520E", "DroneTwo");
        DroneModel model4 = createDroneModel("DroneOne", "DroneOne");
        DroneModel model5 = createDroneModel("DroneTwo", "DroneTwo");

        // Drones
        createDrone(100000L, model4);
        createDrone(200000L, model5);
        createDrone(100001L, model1);
        createDrone(300002L, model1);
        createDrone(400003L, model2);
        createDrone(500004L, model3);
        createDrone(600005L, model3);


        return true;
    }

    private DroneModel createDroneModel(String modelName, String language) {
        try {
            List<WindToleranceStepDTO> windToleranceSteps = new ArrayList<>();
            windToleranceSteps.add(new WindToleranceStepDTO(0, 5, 1));
            windToleranceSteps.add(new WindToleranceStepDTO(5, 10, 2));
            windToleranceSteps.add(new WindToleranceStepDTO(10, 20, 3));

            DroneModel model = addDroneModelController.addDroneModel(modelName, language, windToleranceSteps);
            LOGGER.info("Successfully registered Drone Model '{}'", modelName);
            return model;
        } catch (final IntegrityViolationException | ConcurrencyException e) {
            // Assume it already exists
            return listDroneModelController.listDroneModelsByName(modelName).isPresent()
                    ? listDroneModelController.listDroneModelsByName(modelName).get() : null;
        }
    }


    private void createDrone(Long serialNumber, DroneModel model) {
        try {
            Drone drone = new Drone(SerialNumber.valueOf(serialNumber), DroneStatus.ACTIVE, model);
            addDronesController.add(drone);
            LOGGER.info("Successfully registered Drone '{}'", serialNumber);
        } catch (final IntegrityViolationException | ConcurrencyException e) {
            // Assume it already exists
        }
    }
}
