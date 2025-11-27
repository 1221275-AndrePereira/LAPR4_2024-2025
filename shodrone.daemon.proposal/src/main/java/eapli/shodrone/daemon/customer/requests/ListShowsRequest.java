package eapli.shodrone.daemon.customer.requests;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.domain.ShowProposal;

import java.time.LocalDateTime;
import java.util.stream.StreamSupport;

public class ListShowsRequest extends ProtocolRequest {

    private final String customerVat;
    private final ListShowProposalController listController = new ListShowProposalController();
    private final ShodroneUserRepository userRepo;
    private final String argument;

    public ListShowsRequest(final String request, final String customerVat,final String argument, ShodroneUserRepository userRepo) {
        super(request);
        this.customerVat = customerVat;
        this.userRepo = userRepo;
        this.argument = argument;
    }

    @Override
    public String execute() {
        ShodroneUser customer = userRepo.ofIdentity(VATNumber.valueOf(customerVat)).orElse(null);
        if (customer == null) {
            return buildBadRequest("Customer not found.");
        }

        Iterable<ShowProposal> proposals = listController.listShowProposalsByCustomer(customer);
        Iterable<ShowProposal> acceptedProposals;

        switch (argument) {
            case "all":
                acceptedProposals = StreamSupport.stream(proposals.spliterator(), false)
                    .filter(p -> p.getStatus() == ProposalStatus.ACCEPTED)
                    .toList();
                break;

            case"past":
                acceptedProposals = StreamSupport.stream(proposals.spliterator(), false)
                        .filter(p -> p.getStatus() == ProposalStatus.ACCEPTED && p.getProposedShowDate().date().isAfter(LocalDateTime.now()))
                        .toList();
                break;

            case"future":
                acceptedProposals = StreamSupport.stream(proposals.spliterator(), false)
                        .filter(p -> p.getStatus() == ProposalStatus.ACCEPTED && p.getProposedShowDate().date().isBefore(LocalDateTime.now()))
                        .toList();
                break;

            default:
                System.out.println("Invalid argument " + argument +".");
                return "";


        }
            return buildResponse(acceptedProposals);
        }

    private String buildResponse(Iterable<ShowProposal> proposals) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"ID\", \"Show Date\"\n");
        for (ShowProposal p : proposals) {
            sb.append(String.format("%d, \"%s\"\n", p.identity(), p.getProposedShowDate().toString()));
        }
        return sb.toString();
    }
}