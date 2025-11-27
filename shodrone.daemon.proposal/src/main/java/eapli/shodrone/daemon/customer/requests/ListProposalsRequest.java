package eapli.shodrone.daemon.customer.requests;

import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.domain.ShowProposal;

import java.util.stream.StreamSupport;

import java.time.LocalDateTime;

public class ListProposalsRequest extends ProtocolRequest {

    private final String customerVat;
    private final String filter;
    private final ListShowProposalController listController = new ListShowProposalController();
    private final ShodroneUserRepository userRepo;

    public ListProposalsRequest(final String request, final String customerVat, final String filter, ShodroneUserRepository userRepo) {
        super(request);
        this.customerVat = customerVat;
        this.filter = filter;
        this.userRepo = userRepo;
    }

    /**
     * Executes the request to list show proposals for a customer based on the specified filter.
     *
     * @return A string response containing the filtered list of show proposals in CSV format.
     */
    @Override
    public String execute() {
        // Find the customer
        ShodroneUser customer = userRepo.ofIdentity(VATNumber.valueOf(customerVat)).orElse(null);
        if (customer == null) {
            return buildBadRequest("Customer not found.");
        }

        // Get all proposals for the customer
        Iterable<ShowProposal> proposals = listController.listShowProposalsByCustomer(customer);

        return buildResponse(StreamSupport.stream(proposals.spliterator(), false)
                .filter(p -> p.getStatus() == ProposalStatus.ACCEPTED)
                .toList());
    }

//    private String buildResponse(Iterable<ShowProposal> proposals) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("\"ID\", \"Show Date\", \"Status\"\n");
//        for (ShowProposal p : proposals) {
//            sb.append(String.format("%d, \"%s\", \"%s\"\n", p.identity(), p.getProposedShowDate().toString(), p.getStatus().toString()));
//        }
//        return sb.toString();
//    }

    /**
     * Builds a CSV response string for the list of show proposals.
     * @param proposals The iterable collection of ShowProposal objects.
     * @return A formatted string representing the proposals in CSV format.
     */
    private String buildResponse(Iterable<ShowProposal> proposals) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"ID\", \"Show Date\", \"Duration\", \"Place\"\n");
        for (ShowProposal p : proposals) {
            sb.append(String.format("%d, \"%s\", %d, \"%s\"\n",
                    p.identity(),
                    p.getProposedShowDate().toString(),
                    p.getDuration().minutes(),
                    p.getProposedPlace().toString()
                    // TODO: Add drone model and Figures
                )
            );
        }
        return sb.toString();
    }
}