package eapli.shodrone.daemon.customer.requests;

import eapli.framework.csv.util.CsvLineMarshaler;
import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.ShowProposal;


import java.util.Optional;

public class ShowDetailRequest extends ProtocolRequest {

    ListShowProposalController controller = new ListShowProposalController();
    long showID;

    public ShowDetailRequest(String request, String[] tokens) {
        super(request);
        this.showID = Long.parseLong(CsvLineMarshaler.unquote(tokens[0]));

    }

    @Override
    public String execute() {

        Optional<ShowProposal> proposal = controller.findByID(this.showID);
        String responde = "";

        if (proposal.isPresent()) {
            ShowProposal s = proposal.get();

            responde = ("Show" + s.identity() + "{" +
                    "\n   |Date          |" + s.getProposedShowDate() +
                    "\n   |Video Link    |" + s.getVideoLink() +
                    "\n   |Duration(m)   |" + s.getDuration() +
                    "\n   |Place         |" + s.getProposedPlace() +
                    "\n   |Drone Amount  |" + s.getProposalNDrones() +
                    "\n   |Figures       |" + s.getFigures()
            );
        }

        return responde;
    }

}
