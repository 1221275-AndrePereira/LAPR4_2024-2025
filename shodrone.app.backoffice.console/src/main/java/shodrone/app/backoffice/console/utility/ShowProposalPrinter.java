package shodrone.app.backoffice.console.utility;

import eapli.framework.visitor.Visitor;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showrequest.domain.ShowRequest;

public class ShowProposalPrinter implements Visitor<ShowProposal> {
    @Override
    public void visit(ShowProposal visitee) {
        System.out.printf(
                visitee.toString()
        );
    }
}
