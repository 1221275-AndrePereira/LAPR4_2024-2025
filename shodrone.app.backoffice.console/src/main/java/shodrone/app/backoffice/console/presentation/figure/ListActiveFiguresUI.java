package shodrone.app.backoffice.console.presentation.figure;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.figure.application.ListFigureController;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.figure.domain.Keyword;
import utils.ListPrompt;

import java.util.*;

public class ListActiveFiguresUI extends AbstractUI {

    ListFigureController controller = new ListFigureController();

    @Override
    protected boolean doShow() {

        List<Figure> figures = controller.listAllFigures();

        boolean searched = false;
        while(true){

            if (figures == null || !figures.iterator().hasNext()) {
                System.out.println("The are no usable figures");
                break;
            }

            for (Figure figure : figures) {
                System.out.println(figure);
            }

            System.out.println("Options");
            if(searched){
                System.out.println("2 - Reset List");
            }else{
                System.out.println("2 - Search by description/keyword");
            }

            System.out.println("1 - Decomission from current result");
            System.out.println("0 - Exit");

            int awsner = Console.readInteger("");

            if(awsner<3 && awsner>0){
                if(awsner==1){
                    Figure removed = Decomission(figures);
                    figures.remove(removed);
                    if(figures.isEmpty()){
                        System.out.println("No results left. Reseting list.\n");
                        figures = controller.listActivePublicFigures();
                    }
                }
                if(awsner==2){
                    if(searched){
                        figures = controller.listActivePublicFigures();
                        searched = false;
                    }else{
                        figures = Search(figures);
                        searched = true;
                    }


                }
            } else if (awsner==0) {
                break;
            }else {System.out.println("Please select one of the valid options");}

        }
        return true;
    }

    private List<Figure> Search(List<Figure> figures){

        String searchTerm = Console.readLine("Enter search term: ");

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            System.out.println("Search term is empty.");
            return Collections.emptyList();
        }

        if (figures == null || figures.isEmpty()) {
            System.out.println("No figures provided to search within.");
            return Collections.emptyList();
        }

        final String normalizedSearchTerm = searchTerm.trim().toLowerCase();
        List<Figure> matchedFigures = new ArrayList<>();


        for (Figure figure : figures) {
            if (figure == null) {
                continue;
            }

            boolean found = false;

            String description = figure.getDescription().toString();
            if (description != null && description.toLowerCase().contains(normalizedSearchTerm)) {
                matchedFigures.add(figure);
                found = true;
            }

            if (!found) {
                Set<Keyword> keywords = figure.getKeywords();
                if (keywords != null) {
                    for (Keyword keyword : keywords) {
                        if (keyword != null) {
                            assert keyword.toString() != null;
                            if (keyword.toString().toLowerCase().contains(normalizedSearchTerm)) {
                                matchedFigures.add(figure);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (matchedFigures.isEmpty()) {
            System.out.println("No figures found matching '" + searchTerm + "'.");
        } else {
            System.out.println("Found " + matchedFigures.size() + " figure(s) matching '" + searchTerm + "'.");
        }

        return matchedFigures;



    }

    private Figure Decomission(List<Figure> figures){
        Figure figure = null;

        ListPrompt<Figure> listPrompt = new ListPrompt<>("Select a figure",figures);
        Optional<Figure> o = listPrompt.selectItem();
        if (o.isPresent()) {
            figure = o.get();
        }

        if(figure != null) {
            controller.decomissionFigure(figure);
        }else{
            System.out.println("Failed retrieving figure");
        }
        return figure;
    }

    @Override
    public String headline() {
        return "List Figures";
    }
}
