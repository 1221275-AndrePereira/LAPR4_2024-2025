package shodrone.app.backoffice.console.presentation.maintenanceType;

import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.maintenanceType.application.ListMaintenanceTypeController;
import eapli.shodrone.maintenanceType.domain.MaintenanceType;

public class ListMaintenanceTypesUI extends AbstractUI {

    private final ListMaintenanceTypeController theController = new ListMaintenanceTypeController();

    @Override
    protected boolean doShow() {
        System.out.println("Maintenance Types:");
        Iterable<MaintenanceType> maintenanceTypes = theController.listAllMaintenanceTypes();
        for (MaintenanceType mt : maintenanceTypes) {
            System.out.println(mt); // assumes MaintenanceType.toString() is informative
        }
        return false;
    }

    @Override
    public String headline() {
        return "List Maintenance Types";
    }
}