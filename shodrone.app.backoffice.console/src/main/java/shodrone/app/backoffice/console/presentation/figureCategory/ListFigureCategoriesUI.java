package shodrone.app.backoffice.console.presentation.figureCategory;

import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.figurecategory.application.ListFigureCategoriesController;
import eapli.shodrone.figurecategory.domain.FigureCategory;

import java.util.List;

public class ListFigureCategoriesUI extends AbstractUI {

    private final ListFigureCategoriesController theController = new ListFigureCategoriesController();


    @Override
    protected boolean doShow() {

        final List<FigureCategory> figureCategoryList = this.theController.listFigureCategories();

        if (figureCategoryList.isEmpty()) {
            System.out.println("There are no figure categories available");
            return false;
        } else {
            for(FigureCategory figureCategory : figureCategoryList){
                System.out.println(figureCategory.getFigureCategoryName().figureCategoryName()+" - "+figureCategory.getDescription()+" - created on: "+figureCategory.getCreationDate().creationDate()+" and last updated on: "+figureCategory.getLastUpdateDate().lastUpdateDate()+" with status: "+figureCategory.getStatus());
                System.out.println('\n');
            }
            return true;
        }
    }

    @Override
    public String headline() {
        return "List Figure Categories";
    }
}