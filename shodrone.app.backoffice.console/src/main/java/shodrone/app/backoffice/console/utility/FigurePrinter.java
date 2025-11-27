package shodrone.app.backoffice.console.utility;

import eapli.framework.visitor.Visitor;
import eapli.shodrone.figure.domain.Figure;

public class FigurePrinter implements Visitor<Figure> {

    @Override
    public void visit(final Figure visitee) {
        String description = "[No Description]";
        if (visitee.identity() != null) {
            description = visitee.getDescription().toString();
        }

        String version = "[No Version]";
        if (visitee.identity() != null) {
            version = visitee.getDslVersion().toString();
        }

        // Consistent display format: id, description, version
        System.out.printf("id: %d, Description: %s, DSL version: %s%n", visitee.identity(), description, version);
    }
}
