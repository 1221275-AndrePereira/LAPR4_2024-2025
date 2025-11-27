package eapli.shodrone.showProposal.domain.proposalFields;

import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;


@Embeddable
@EqualsAndHashCode
public class ProposalDocument implements ValueObject,Comparable<ProposalDocument> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final String filePath;


    public ProposalDocument(String filePath) {
        this.filePath = filePath;
    }


    protected ProposalDocument() {
        //for ORM
        this.filePath = null; // Or handle appropriately
    }



    @Override
    public int compareTo(ProposalDocument o) {
        return this.filePath.compareTo(o.filePath);
    }
}
