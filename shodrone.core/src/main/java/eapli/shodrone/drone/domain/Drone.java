package eapli.shodrone.drone.domain;

import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.validations.Preconditions;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Drone implements AggregateRoot<Long> {

    @EmbeddedId
    @Setter
    @Getter
    private SerialNumber serialNumber;

    @Setter
    @Getter
    private String removalReason;

    @Embedded
    @Setter
    @Getter
    private DroneRemovalDate removalDate;

    @Setter
    @Getter
    private DroneStatus status;

    @ManyToOne
    @Getter
    private DroneModel model;

    public Drone(
                SerialNumber serialNumber,
                DroneStatus status,
                DroneModel model
        ) {
        Preconditions.noneNull(serialNumber, status, model);

        this.serialNumber = serialNumber;
        this.removalReason = null;
        this.removalDate = null;
        this.status = status;
        this.model = model;
    }

    public Drone() {
        // ORM
    }

    @Override
    public Long identity() {
        return this.serialNumber.serialNumber();
    }

    @Override
    public boolean sameAs(Object other) {
        return other instanceof Drone && ((Drone) other).serialNumber.equals(this.serialNumber);
    }

    @Override
    public String toString() {
        return "Serial Number: " + serialNumber +
                ", Status: " + (status != null ? status : "N/A") +
                ", Model: " + (model != null ? model.getModelName() : "N/A");
    }

}
