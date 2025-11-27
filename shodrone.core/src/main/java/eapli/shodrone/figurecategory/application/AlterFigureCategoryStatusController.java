package eapli.shodrone.figurecategory.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.figurecategory.domain.FigureCategoryName;
import eapli.shodrone.figurecategory.repositories.FigureCategoryCatalogue;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

/**
 * The type AlterFigureCategoryStatusController
 */
@UseCaseController
public class AlterFigureCategoryStatusController {

    private final AuthorizationService authz;
    private final FigureCategoryCatalogue figureCategoryCatalogue;

    public AlterFigureCategoryStatusController(){
        this.authz = AuthzRegistry.authorizationService();
        this.figureCategoryCatalogue= PersistenceContext.repositories().figureCategories();
    }

    public AlterFigureCategoryStatusController(final AuthorizationService authz, final FigureCategoryCatalogue figureCategoryCatalogue){
        this.authz = authz;
        this.figureCategoryCatalogue= figureCategoryCatalogue;
    }

    /**
     * Alter figure category's status depending on the current one (Active -> Inactive / Inactive -> Active)
     *
     * @param name The name of the figure category
     * @return the figure category
     */
    public FigureCategory alterFigureCategoryStatus(FigureCategoryName name) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER,ShodroneRoles.ADMIN,ShodroneRoles.SHOW_DESIGNER);
        FigureCategory figureCategory = figureCategoryCatalogue.searchByFigureCategoryName(Designation.valueOf(name.figureCategoryName())).orElseThrow(() -> new IllegalArgumentException("No figure category found with name: " + name.figureCategoryName()));

        figureCategory.updateStatus();

        return save(figureCategory);
    }


    /**
     * Save figure category.
     *
     * @param figureCategory the figure category
     * @return the figure category
     */
    public FigureCategory save(FigureCategory figureCategory) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.SHOW_DESIGNER);

        figureCategory = figureCategoryCatalogue.save(figureCategory);

        return figureCategory;
    }
}
