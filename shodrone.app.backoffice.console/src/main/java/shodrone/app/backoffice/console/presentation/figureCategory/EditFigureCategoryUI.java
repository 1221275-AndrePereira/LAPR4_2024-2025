package shodrone.app.backoffice.console.presentation.figureCategory;


import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.figurecategory.application.EditFigureCategoryController;
import eapli.shodrone.figurecategory.domain.FigureCategoryName;

public class EditFigureCategoryUI extends AbstractUI {

    private final EditFigureCategoryController theController = new EditFigureCategoryController();


    @Override
    protected boolean doShow() {
        final FigureCategoryName currentName = new FigureCategoryName(Console.readNonEmptyLine("Figure Category Current Name", "Please enter the figure category name you want to edit"));

        FigureCategoryName newName = new FigureCategoryName(Console.readNonEmptyLine("Figure Category New Name", "Please enter the figure category's new name"));


        final String newDescription = Console.readNonEmptyLine("Figure Category New Description", "Please enter the figure category's new Description'");


        try {
            this.theController.editFigureCategory(currentName,newName,newDescription);
            System.out.println("\nFigure Category edited successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error editing figure category: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String headline() {
        return "Edit Figure Category";
    }
}
