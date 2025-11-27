package eapli.shodrone.figure.application;

import eapli.framework.application.UseCaseController;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.figure.repository.FigureRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;

import java.util.List;

@UseCaseController
public class ListFigureController {

    FigureRepository repo = PersistenceContext.repositories().figures();

    public List<Figure> listAllFigures() {
        return (List<Figure>) repo.findAll();
    }

    /**
     * Lists all active figures that aren't exclusive to any customer
     * @return a List of all the active figures
     */
    public List<Figure> listActivePublicFigures(){
        return repo.allActivePublicFigures();
    }

    public List<Figure> listExclusiveFigureOfUser(ShodroneUser user){
        return repo.listExclusiveFigureOfUser(user);
    }

    /**
     * @param figure
     * Decomissions a Figure
     */
    public void decomissionFigure(Figure figure){
        figure.deactivate();
        repo.save(figure);
    }

}
