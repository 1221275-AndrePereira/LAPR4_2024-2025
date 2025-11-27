package eapli.shodrone.showProposal.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class ProposalVideoLink implements ValueObject, Comparable<ProposalVideoLink> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String video;

    /**
     * Constructor to create a ProposalVideoLink instance.
     *
     * @param video The video identifier.
     */
    public ProposalVideoLink(String video) {
        Preconditions.nonNull(video);
        this.video = video;
    }

    /**
     * Default constructor for ORM purposes.
     * This constructor should not be used directly in application code.
     */
    public ProposalVideoLink() {
        // for ORM
        this.video = ""; // Or handle appropriately
    }

    /**
     * Factory method to create a ProposalVideoLink instance.
     *
     * @param video The video identifier.
     * @return A new ProposalVideoLink instance.
     */
    public static ProposalVideoLink valueOf(String video) {
        return new ProposalVideoLink(video);
    }

    /**
     * Returns the video identifier.
     *
     * @return the video identifier
     */
    public String video() {
        return this.video;
    }

    /**
     * Returns the video identifier as a string.
     *
     * @return the video identifier
     */
    @Override
    public int compareTo(ProposalVideoLink other) {
        return this.video.compareTo(other.video);
    }

    @Override
    public String toString() {
        return "ProposalVideoLink{" +
                "video='" + video + '\'' +
                '}';
    }
}
