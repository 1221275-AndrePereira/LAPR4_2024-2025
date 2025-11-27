package eapli.shodrone.infrastructure.boostrapers.demo;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.figure.application.AddFigureController;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.figurecategory.application.AddFigureCategoryController;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.figurecategory.domain.FigureCategoryName;
import eapli.shodrone.shodroneusermanagement.application.ListShodroneUsersController;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.usermanagement.application.ListSystemUsersController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eapli.framework.actions.Action;
import utils.FileSelector;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;


public class FigureBootstrapper implements Action {

    private static final Logger LOGGER = LogManager.getLogger(FigureBootstrapper.class);

    ListSystemUsersController listSystemUsersController = new ListSystemUsersController();
    AddFigureController figureController = new AddFigureController();
    AddFigureCategoryController figureCategoryController = new AddFigureCategoryController();
    ListShodroneUsersController listShodroneUsersController = new ListShodroneUsersController();
    SystemUser systemUser;

    @Override
    public boolean execute() {

        Optional<SystemUser> user = listSystemUsersController.find(Username.valueOf("admin"));
        user.ifPresent(value -> systemUser = value);

        FigureCategory c1 = figureCategoryController.addFigureCategory(new FigureCategoryName("Animals"),"Figures that include Animals");
        FigureCategory c2 = figureCategoryController.addFigureCategory(new FigureCategoryName("Objects"),"Figures that include Objects");
        FigureCategory c3 = figureCategoryController.addFigureCategory(new FigureCategoryName("People"),"Figures that include People");

        Iterable<ShodroneUser> users =listShodroneUsersController.listShodroneUsers();
        Iterator<ShodroneUser> iterator = users.iterator();

        ShodroneUser u1 = iterator.next();
        ShodroneUser u2 = iterator.next();
        ShodroneUser u3 = iterator.next();

        System.out.println(users);

        String f1 = ("sample_DSL_figure_1.txt");
        String f2 = ("sample_DSL_figure_2.txt");
        String f3 = ("sample_DSL_figure_3.txt");

        registerFigure("Monkey", Set.of("Monkey","Banana","Jungle"),"1.2", c1,f1,null);
        registerFigure("Hammer", Set.of("Build","Nail","Tool"),"1.3", c2, f2,u2);
        registerFigure("Marcelo Rebelo de Sousa", Set.of("Person","Politics"),"1.2", c3,f3,u3);
        registerFigure("Mongolia", Set.of("Country","Region","Culture","Asia"),"1.4.20", c1,f2,u1);
        registerFigure("Donald Trump", Set.of("Person","Politics","America"),"23.2.3.40", c3,f2,null);
        registerFigure("Circle", Set.of("Shape","Geometry","Circle"),"2.72", c2,f1,null);
        registerFigure("Square", Set.of("Shape","Geometry","Square"),"3.1", c2,f1,null);
        registerFigure("Triangle", Set.of("Shape","Geometry","Triangle"),"1.0", c2,f1,null);
        registerFigure("Rectangle", Set.of("Shape","Geometry","Rectangle"),"1.0", c2,f1,null);


        return true;
    }

    private void registerFigure(
            final String description,
            final Set<String> keywords,
            final String version,
            final FigureCategory category,
            final String path,
            final ShodroneUser figureUser
    )
    {
        try {

            figureController.defineAuthor(systemUser);
            Figure figure = figureController.addFigure(description,keywords,version,category,path,figureUser);

            if (figure != null) {
                LOGGER.info("Successfully registered figure: {}", description);
            }

        } catch (final ConcurrencyException | IntegrityViolationException | IOException e) {
            LOGGER.warn("Assuming {} already exists (activate trace log for details)", description);
            LOGGER.trace("Assuming existing record", e);
        }
    }

}
