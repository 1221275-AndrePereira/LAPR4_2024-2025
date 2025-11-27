package shodrone.app.backoffice.console.presentation.figureCategory;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.figurecategory.application.AddFigureCategoryController;
import eapli.shodrone.figurecategory.domain.FigureCategoryName;


public class AddFigureCategoryUI extends AbstractUI {

    private final AddFigureCategoryController theController = new AddFigureCategoryController();


    @Override
    protected boolean doShow() {
        final FigureCategoryName name = new FigureCategoryName(Console.readNonEmptyLine("Figure Category Name", "Please enter the figure category name"));
        final String description = Console.readNonEmptyLine("Description", "Please enter the figure category description");
        try {
            this.theController.addFigureCategory(name,description);
            System.out.println("\nFigure Category created successfully.\n");
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error creating figure category: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String headline() {
        return "Add Figure Category";
    }
}
