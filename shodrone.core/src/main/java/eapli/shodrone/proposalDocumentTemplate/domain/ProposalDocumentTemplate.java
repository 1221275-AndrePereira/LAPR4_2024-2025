package eapli.shodrone.proposalDocumentTemplate.domain;


import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * The type ProposalDocumentTemplate.
 */
@Entity
@XmlRootElement
@Getter
@Setter
public class ProposalDocumentTemplate implements AggregateRoot<Long> {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProposalDocumentTemplateId;

    private String ProposalDocumentTemplateName;


    private String ProposalDocumentTemplateFilePath;

    @Override
    public Long identity() {
        return ProposalDocumentTemplateId;
    }

    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof ProposalDocumentTemplate)) return false;
        ProposalDocumentTemplate that = (ProposalDocumentTemplate) other;
        return this.ProposalDocumentTemplateId.equals(that.ProposalDocumentTemplateId);
    }


    /**
     * Instantiates a new template for the show proposal document.
     *
     * @param ProposalDocumentTemplateName the name of the template of the show proposal document
     * @param ProposalDocumentTemplateFilePath the file path of the template of the show proposal document
     */
    public ProposalDocumentTemplate(String ProposalDocumentTemplateName, String ProposalDocumentTemplateFilePath) {
        Preconditions.noneNull(ProposalDocumentTemplateName,ProposalDocumentTemplateFilePath);
        this.ProposalDocumentTemplateName=ProposalDocumentTemplateName;
        this.ProposalDocumentTemplateFilePath=ProposalDocumentTemplateFilePath;
    }

    protected ProposalDocumentTemplate() {

    }
}
