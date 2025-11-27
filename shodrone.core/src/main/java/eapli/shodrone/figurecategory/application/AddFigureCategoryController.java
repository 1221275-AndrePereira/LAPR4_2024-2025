package eapli.shodrone.figurecategory.application;


import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.figurecategory.domain.*;
import eapli.shodrone.figurecategory.repositories.FigureCategoryCatalogue;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.time.LocalDate;


/**
 * The type Add figure category controller.
 */
@UseCaseController
public class AddFigureCategoryController {

    private final AuthorizationService authz;
    private final FigureCategoryCatalogue figureCategoryCatalogue;

    public AddFigureCategoryController(){
        this.authz = AuthzRegistry.authorizationService();
        this.figureCategoryCatalogue= PersistenceContext.repositories().figureCategories();
    }

    public AddFigureCategoryController(final AuthorizationService authz, final FigureCategoryCatalogue figureCategoryCatalogue){
        this.authz = authz;
        this.figureCategoryCatalogue= figureCategoryCatalogue;
    }

    /**
     * Add figure category to catalogue.
     *
     * @param name the figure category name
     * @param description the figure category description
     * @return the figure category
     */
    public FigureCategory addFigureCategory(FigureCategoryName name, String description) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.SHOW_DESIGNER,ShodroneRoles.ADMIN,ShodroneRoles.POWER_USER);
        FigureCategoryBuilder builder = new FigureCategoryBuilder();
        LocalDate currentDate = LocalDate.now();
        FigureCategoryCreationDate creationDate = new FigureCategoryCreationDate(currentDate);
        FigureCategoryLastUpdateDate lastUpdateDate = new FigureCategoryLastUpdateDate(currentDate);

        FigureCategory figureCategory = builder.withName(name).withDescription(description).withCreationDate(creationDate).withLastUpdateDate(lastUpdateDate).withStatus(FigureCategoryStatus.ACTIVE).build();

        return save(figureCategory);

    }


    /**
     * Save figure category.
     *
     * @param figureCategory the figure category
     * @return the figure category
     */
    public FigureCategory save(FigureCategory figureCategory) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.SHOW_DESIGNER,ShodroneRoles.ADMIN,ShodroneRoles.POWER_USER);

        figureCategory = figureCategoryCatalogue.save(figureCategory);

        return figureCategory;
    }
}
