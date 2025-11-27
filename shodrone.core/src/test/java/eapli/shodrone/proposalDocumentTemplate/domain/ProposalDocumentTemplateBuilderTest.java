package eapli.shodrone.proposalDocumentTemplate.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProposalDocumentTemplateBuilderTest {


    @Test
    public void ensureFullBuild(){
        ProposalDocumentTemplateBuilder builder = new ProposalDocumentTemplateBuilder();
        builder.withName("Proposal Document");
        builder.withFilePath("proposalDocumentTemplate/proposalDocumentTemplate.txt");
        ProposalDocumentTemplate proposalDocumentTemplate = builder.build();

        assertEquals("Proposal Document", proposalDocumentTemplate.getProposalDocumentTemplateName());
        assertEquals("proposalDocumentTemplate/proposalDocumentTemplate.txt",proposalDocumentTemplate.getProposalDocumentTemplateFilePath());
    }

}