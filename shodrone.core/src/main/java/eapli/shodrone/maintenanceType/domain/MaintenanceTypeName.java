package eapli.shodrone.maintenanceType.domain;

import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;

import java.io.Serializable;


@Embeddable
public class MaintenanceTypeName implements ValueObject, Serializable {

    private String name;


    protected MaintenanceTypeName() {

    }

    public MaintenanceTypeName(String maintenanceTypeName) {
        if (maintenanceTypeName == null) throw new IllegalArgumentException("Maintenance Type Name must have a name");
        this.name = maintenanceTypeName;
    }

    public String maintenanceTypeName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
