package eapli.shodrone.daemon.customer.requests;

import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.showProposal.domain.ShowProposal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateProposalStatusRequest extends ProtocolRequest {

    private final long proposalId;
    private final String feedback;
    private final boolean isAccepted;
    private final ShowProposalRepository proposalRepo;

    public UpdateProposalStatusRequest(final String request, final long proposalId, final String feedback, boolean isAccepted, ShowProposalRepository proposalRepo) {
        super(request);
        this.proposalId = proposalId;
        this.feedback = feedback;
        this.isAccepted = isAccepted;
        this.proposalRepo = proposalRepo;
    }

    @Override
    public String execute() {
        log.debug("Executing UpdateProposalStatusRequest");
        ShowProposal proposal = proposalRepo.findByIdentifier(proposalId).orElse(null);
        log.debug("Proposal found: {}", proposal != null ? "true" : "false");
        if (proposal == null) {
            return buildBadRequest("Proposal not found.");
        }
        // check if the proposal is pending
        if (proposal.getStatus() != ProposalStatus.PENDING) {
            return buildBadRequest("Proposal is not pending.");
        }

        if (isAccepted) {
            proposal.accept(feedback);
        } else {
            proposal.reject(feedback);
        }

        proposalRepo.save(proposal);
        return "OK\n";
    }
}