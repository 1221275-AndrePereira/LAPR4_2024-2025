package eapli.shodrone.figurecategory.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.figurecategory.repositories.FigureCategoryCatalogue;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import java.util.ArrayList;
import java.util.List;

/**
 * The type ListFigureCategoriesController
 */
@UseCaseController
public class ListFigureCategoriesController {


    private final AuthorizationService authz;
    private final FigureCategoryCatalogue figureCategoryCatalogue;

    public ListFigureCategoriesController(){
        this.authz = AuthzRegistry.authorizationService();
        this.figureCategoryCatalogue= PersistenceContext.repositories().figureCategories();
    }

    public ListFigureCategoriesController(final AuthorizationService authz, final FigureCategoryCatalogue figureCategoryCatalogue){
        this.authz = authz;
        this.figureCategoryCatalogue= figureCategoryCatalogue;
    }

    /**
     * Obtain all figure categories from the catalogue and list them
     *
     * @return the list of figure categories
     */
    public List<FigureCategory> listFigureCategories(){
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.SHOW_DESIGNER,ShodroneRoles.CRM_MANAGER,ShodroneRoles.CRM_COLLABORATOR,ShodroneRoles.POWER_USER,ShodroneRoles.ADMIN);
        Iterable<FigureCategory> iterable = figureCategoryCatalogue.obtainAllFigureCategories();
        List<FigureCategory> figureCategoryList = new ArrayList<>();
        iterable.forEach(figureCategoryList::add);
        return figureCategoryList;
    }
}
