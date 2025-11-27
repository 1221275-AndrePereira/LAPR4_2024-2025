package shodrone.app.backoffice.console.utility;

import eapli.framework.visitor.Visitor;
import eapli.shodrone.showrequest.domain.ShowRequest;

public class ShowRequestPrinter implements Visitor<ShowRequest> {
    @Override
    public void visit(ShowRequest visitee) {
        System.out.printf("ID: %d \nDescription: %-30s \nDate: %s",
                visitee.identity(),
                visitee.getDescription() != null ? visitee.getDescription().toString() : "N/A",
                visitee.getShowDate() != null ? visitee.getShowDate().toString() : "N/A"
            );
    }
}