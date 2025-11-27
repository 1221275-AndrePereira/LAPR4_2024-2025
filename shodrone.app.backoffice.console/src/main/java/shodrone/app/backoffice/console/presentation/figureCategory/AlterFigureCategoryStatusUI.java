package shodrone.app.backoffice.console.presentation.figureCategory;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.figurecategory.application.AlterFigureCategoryStatusController;
import eapli.shodrone.figurecategory.domain.FigureCategoryName;

public class AlterFigureCategoryStatusUI extends AbstractUI {

    private final AlterFigureCategoryStatusController theController = new AlterFigureCategoryStatusController();


    @Override
    protected boolean doShow() {
        final FigureCategoryName name = new FigureCategoryName(Console.readNonEmptyLine("Figure Category Name", "Please enter the figure category name you want to alter the status"));
        try {
            this.theController.alterFigureCategoryStatus(name);
            System.out.println("\nStatus altered successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error altering figure category status: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String headline() {
        return "Alter Figure Category Status";
    }
}
