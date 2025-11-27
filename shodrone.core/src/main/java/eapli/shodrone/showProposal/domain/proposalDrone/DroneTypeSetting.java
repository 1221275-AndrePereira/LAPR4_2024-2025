package eapli.shodrone.showProposal.domain.proposalDrone;

import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

/**
 * An entity that links a specific drone model within a proposal
 * to its generated instruction set.
 */
@Getter
@Entity
public class DroneTypeSetting implements ValueObject,Comparable<DroneTypeSetting>{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // LAZY fetching is often a good default for ManyToOne
    private final ProposalDroneModel proposalModel;

    @Embedded
    private final DroneInstructions instructions;

    /**
     * Constructor for creating a new DroneTypeSetting.
     * @param droneModel The proposal-specific drone model.
     * @param instructions The generated instructions for that model.
     */
    public DroneTypeSetting(final ProposalDroneModel droneModel, final DroneInstructions instructions) {
        this.proposalModel = droneModel;
        this.instructions = instructions;
    }

    /**
     * Protected no-arg constructor for ORM.
     */
    public DroneTypeSetting() {
        // for ORM.
        this.proposalModel = null;
        this.instructions = null;
    }

    /**
     * Returns the proposal model associated with this drone type setting.
     * @return The ProposalDroneModel.
     */
    public ProposalDroneModel proposalModel() {
        return proposalModel;
    }

    /**
     * Returns the drone instructions associated with this drone type setting.
     * @return The DroneInstructions.
     */
    public DroneInstructions instructions() {
        return instructions;
    }

    public static DroneTypeSetting valueOf(final ProposalDroneModel droneModel, final DroneInstructions instructions) {
        return new DroneTypeSetting(droneModel, instructions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroneTypeSetting that = (DroneTypeSetting) o;

        return this.id != null && this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public int compareTo(DroneTypeSetting o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return "DroneTypeSetting: " +
                "\nProposalModel: " + proposalModel +
                "\nInstructions: " + instructions +
                '\n';

    }
}
