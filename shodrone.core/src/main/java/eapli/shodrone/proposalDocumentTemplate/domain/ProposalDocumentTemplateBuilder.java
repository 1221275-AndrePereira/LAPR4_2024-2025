package eapli.shodrone.proposalDocumentTemplate.domain;

import eapli.framework.domain.model.DomainFactory;

/**
 * The type ProposalDocumentTemplateBuilder
 */
public class ProposalDocumentTemplateBuilder implements DomainFactory<ProposalDocumentTemplate> {

    private String name;
    private String filePath;


    /**
     * With name proposal document template builder
     *
     * @param name the name of the template for the show proposal document
     * @return the proposal document template builder
     */
    public ProposalDocumentTemplateBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * With file path proposal document template builder
     *
     * @param filePath the file path for the template for the show proposal document
     * @return the proposal document template builder
     */
    public ProposalDocumentTemplateBuilder withFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    @Override
    public ProposalDocumentTemplate build() {
        return new ProposalDocumentTemplate(name, filePath);
    }
}
