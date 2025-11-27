package eapli.shodrone.maintenanceType.domain;

import eapli.framework.domain.model.DomainFactory;

public class MaintenanceTypeBuilder implements DomainFactory<MaintenanceType> {

    private MaintenanceTypeName maintenanceTypeName;


    public MaintenanceTypeBuilder withName(MaintenanceTypeName maintenanceTypeName) {
        this.maintenanceTypeName = maintenanceTypeName;
        return this;
    }

    @Override
    public MaintenanceType build() {
        return new MaintenanceType(maintenanceTypeName);
    }
}
