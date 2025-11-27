package eapli.shodrone.showProposalDocument.application;

import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplate;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showrequest.domain.ShowRequest;

public interface DocumentGenerator {

    StringBuilder generate(StringBuilder content, ShowProposal proposal, ProposalDocumentTemplate proposalDocumentTemplate, ShodroneUser user);
}
