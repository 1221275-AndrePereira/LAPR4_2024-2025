package eapli.shodrone.showProposal.domain.proposalDrone;

import eapli.shodrone.droneModel.domain.DroneModel;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * An Entity that stores the DroneModels that will make part of a certain ShowProposal
 */

@Getter
@Entity
public class ProposalDroneModel {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private final DroneModel model;

    @Embedded
    private final ProposalNDrones ndrones;

    /**
     * Constructor to create a new ProposalDroneModel.
     * @param droneModel The proposal-specific drone model.
     * @param proposalNDrones, respective amount of drones of that droneModel taking part.
     */
    public ProposalDroneModel(final DroneModel droneModel, final ProposalNDrones proposalNDrones) {
        this.model = droneModel;
        this.ndrones = proposalNDrones;
    }

    public ProposalDroneModel() {
        // for ORM.
        this.model = null;
        this.ndrones = null;
    }

    /**
     * Returns the DroneModel associated with this proposal.
     * @return The DroneModel.
     */
    public DroneModel droneModel() {
        return this.model;
    }

    /**
     * Returns the number of drones associated with this proposal.
     * @return The ProposalNDrones instance representing the number of drones.
     */
    public ProposalNDrones nDrones() {
        return this.ndrones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalDroneModel that = (ProposalDroneModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}