package eapli.shodrone.showProposal.domain.proposalFields;

public enum ProposalStatus {

    INCOMPLETE,  // 0 Missing essential data for simulation
    TOGENERATE,  // 1
    TESTING,     // 2 Proposal will be tested
    FAILED,      // 3 Proposal inst safe to execute
    SAFE,        // 4 Proposal is safe to be executed

    PENDING,     // 5 Sent to the user for approval
    ACCEPTED,    // 6 Customer accepted these terms
    REFUSED,     // 7 Customer refused this offer
    CLOSED,      // 8 Final state of a proposal
    ARCHIVED;    // 9 Archived proposal that will be just a memory in the system
}
