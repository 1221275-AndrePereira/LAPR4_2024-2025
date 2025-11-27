package shodrone.app.backoffice.console.presentation.maintenanceType;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.maintenanceType.application.EditMaintenanceTypeController;
import eapli.shodrone.maintenanceType.domain.MaintenanceTypeName;

public class EditMaintenanceTypeUI extends AbstractUI {

    private final EditMaintenanceTypeController theController = new EditMaintenanceTypeController();

    @Override
    protected boolean doShow() {
        try {
            Long id = Long.parseLong(Console.readNonEmptyLine("Maintenance Type ID:", "Please enter a valid ID"));
            String newNameStr = Console.readNonEmptyLine("New Maintenance Type Name:", "Please enter the new name");
            MaintenanceTypeName newName = new MaintenanceTypeName(newNameStr);
            theController.editMaintenanceType(id, newName);
            System.out.println("\nMaintenance Type edited successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error editing maintenance type: " + e.getMessage());
        }
        return false;
    }

    @Override
    public String headline() {
        return "Edit Maintenance Type";
    }
}