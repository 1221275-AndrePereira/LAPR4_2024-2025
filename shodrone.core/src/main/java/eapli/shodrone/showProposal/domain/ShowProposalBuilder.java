package eapli.shodrone.showProposal.domain;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalNDrones;
import eapli.shodrone.showProposal.domain.proposalFields.*;

import java.util.Objects;

public class ShowProposalBuilder {

    private String description;

    private ProposedDuration duration;

    private ProposedPlace proposedPlace;

    private ProposalNDrones numberOfDrones;

    private ProposedShowDate proposedShowDate;

    private Insurance insurance;

    private SystemUser manager;


    public ShowProposalBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the number of drones for the proposal.
     * If numberOfDrones is null, it initializes it to a default empty ProposalNDrones.
     *
     * @param numberOfDrones The number of drones proposed.
     * @return The current instance of ShowProposalBuilder.
     */
    public ShowProposalBuilder withNumberOfDrones(ProposalNDrones numberOfDrones) {
        numberOfDrones = Objects.requireNonNullElse(numberOfDrones, new ProposalNDrones());
        this.numberOfDrones = numberOfDrones;
        return this;
    }

    /**
     * Sets the proposed duration for the show.
     * If duration is null, it initializes it to a default empty ProposedDuration.
     *
     * @param duration The proposed duration of the show.
     * @return The current instance of ShowProposalBuilder.
     */
    public ShowProposalBuilder withDuration(ProposedDuration duration) {
        duration = Objects.requireNonNullElse(duration, new ProposedDuration());
        this.duration = duration;
        return this;
    }

    /**
     * Sets the proposed place for the show.
     * If proposedPlace is null, it initializes it to a default empty ProposedPlace.
     *
     * @param proposedPlace The proposed place for the show.
     * @return The current instance of ShowProposalBuilder.
     */
    public ShowProposalBuilder withProposedPlace(ProposedPlace proposedPlace) {
        proposedPlace = Objects.requireNonNullElse(proposedPlace, new ProposedPlace());
        this.proposedPlace = proposedPlace;
        return this;
    }

    /**
     * Sets the proposed show date.
     * If proposedShowDate is null, it initializes it to a default empty ProposedShowDate.
     *
     * @param proposedShowDate The proposed date for the show.
     * @return The current instance of ShowProposalBuilder.
     */
    public ShowProposalBuilder withProposedShowDate(ProposedShowDate proposedShowDate) {
        proposedShowDate = Objects.requireNonNullElse(proposedShowDate, new ProposedShowDate());
        this.proposedShowDate = proposedShowDate;
        return this;
    }

    /**
     * Sets the insurance amount for the proposal.
     * If insurance is null, it initializes it to a default empty Insurance.
     *
     * @param insurance The insurance amount for the proposal.
     * @return The current instance of ShowProposalBuilder.
     */
    public ShowProposalBuilder withInsurance(Insurance insurance) {
        insurance = Objects.requireNonNullElse(insurance, new Insurance());
        this.insurance = insurance;
        return this;
    }

    public ShowProposalBuilder withManager(SystemUser manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        this.manager = manager;
        return this;
    }

    /**
     * Builds a ShowProposal instance with the current state of the builder.
     *
     * @return A new ShowProposal instance.
     */
    public ShowProposal build() {
        return new ShowProposal(
                description,
                numberOfDrones,
                duration,
                insurance,
                proposedPlace,
                proposedShowDate,
                manager
        );
    }
}
