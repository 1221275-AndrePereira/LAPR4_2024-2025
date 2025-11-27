package eapli.shodrone.maintenanceType.domain;

import eapli.framework.domain.model.AggregateRoot;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Entity
@XmlRootElement
@Getter
@Setter
public class MaintenanceType implements AggregateRoot<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceTypeID;

    @Embedded
    private MaintenanceTypeName maintenanceTypeName;

    @Version
    private Long version;

    @Override
    public Long identity() {
        return maintenanceTypeID;
    }

    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof MaintenanceType)) return false;
        MaintenanceType that = (MaintenanceType) other;
        return this.maintenanceTypeName.equals(that.maintenanceTypeName);
    }

    public MaintenanceType(MaintenanceTypeName name) {
        this.maintenanceTypeName = name;
    }

    protected MaintenanceType() { }

    public boolean hasMaintenanceRecords() {
        return false;
    }

    public void changeName(MaintenanceTypeName newName) {
        this.maintenanceTypeName = newName;
    }

    @Override
    public String toString() {
        return String.format("MaintenanceType{id=%d, name='%s'}",
                maintenanceTypeID,
                maintenanceTypeName.toString()
        );
    }
}