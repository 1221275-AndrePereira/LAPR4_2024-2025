package eapli.shodrone.figurecategory.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.figurecategory.domain.FigureCategoryLastUpdateDate;
import eapli.shodrone.figurecategory.domain.FigureCategoryName;
import eapli.shodrone.figurecategory.repositories.FigureCategoryCatalogue;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.time.LocalDate;

/**
 * The type EditFigureCategoryController
 */
@UseCaseController
public class EditFigureCategoryController {

    private final AuthorizationService authz;
    private final FigureCategoryCatalogue figureCategoryCatalogue;

    public EditFigureCategoryController(){
        this.authz = AuthzRegistry.authorizationService();
        this.figureCategoryCatalogue= PersistenceContext.repositories().figureCategories();
    }

    public EditFigureCategoryController(final AuthorizationService authz, final FigureCategoryCatalogue figureCategoryCatalogue){
        this.authz = authz;
        this.figureCategoryCatalogue= figureCategoryCatalogue;
    }

    /**
     * Edit the figure category with new name and new description.
     *
     * @param currentName the current name of the figure category
     * @param newName the new name of the figure category
     * @param newDescription the new description of the figure category
     * @return the figure category
     */
    public FigureCategory editFigureCategory(FigureCategoryName currentName, FigureCategoryName newName, String newDescription) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.SHOW_DESIGNER);
        FigureCategory figureCategory = figureCategoryCatalogue.searchByFigureCategoryName(Designation.valueOf(currentName.figureCategoryName())).orElseThrow(() -> new IllegalArgumentException("No figure category found with name: " + currentName.figureCategoryName()));

        figureCategory.updateName(newName);
        figureCategory.updateDescription(newDescription);
        figureCategory.updateLastUpdateDate(new FigureCategoryLastUpdateDate(LocalDate.now()));

        return save(figureCategory);
    }



    /**
     * Save figure category.
     *
     * @param figureCategory the figure category
     * @return the figure category
     */
    public FigureCategory save(FigureCategory figureCategory) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.SHOW_DESIGNER);

        figureCategory = figureCategoryCatalogue.save(figureCategory);

        return figureCategory;
    }
}
