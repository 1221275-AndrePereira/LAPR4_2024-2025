package eapli.shodrone.drone.domain;

import jakarta.persistence.Embeddable;


public enum DroneStatus {
    ACTIVE,
    INACTIVE,
    MAINTENANCE,
    BROKEN,
    REMOVED
}
