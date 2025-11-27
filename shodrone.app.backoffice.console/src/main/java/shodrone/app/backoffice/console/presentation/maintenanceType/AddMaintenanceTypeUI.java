package shodrone.app.backoffice.console.presentation.maintenanceType;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.maintenanceType.application.AddMaintenanceTypeController;
import eapli.shodrone.maintenanceType.domain.MaintenanceTypeName;

public class AddMaintenanceTypeUI extends AbstractUI {

    private final AddMaintenanceTypeController theController = new AddMaintenanceTypeController();


    @Override
    protected boolean doShow() {
        final MaintenanceTypeName maintenanceTypeName = new MaintenanceTypeName(Console.readNonEmptyLine("Maintenance Type Name", "Please enter the maintenance type name"));
        try {
            this.theController.addMaintenanceType(maintenanceTypeName);
            System.out.println("\nMaintenance Type created successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error creating maintenance type: " + e.getMessage());
        }

        return false;

    }



    @Override
    public String headline() {
        return "Add Maintenance Type";
    }
}
