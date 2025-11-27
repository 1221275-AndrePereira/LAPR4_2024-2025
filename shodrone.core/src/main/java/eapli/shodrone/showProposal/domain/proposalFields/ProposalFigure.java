package eapli.shodrone.showProposal.domain.proposalFields;

import eapli.framework.validations.Preconditions;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.showProposal.domain.proposalDrone.DroneTypeSetting;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalDroneModel;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * An Entity that stores Figures that will make part of a certain ShowProposal
 */

@Getter
@Entity
public class ProposalFigure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DroneTypeSetting> typeSettings = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private final Figure figure;

    /**
     * Creates a ProposalFigure for a specific Figure.
     * @param figure The figure to be included in the proposal.
     */
    public ProposalFigure(final Figure figure ) {
        Preconditions.nonNull(figure, "Figure cannot be null");
        Preconditions.ensure(figure.isActive(), "Figure must be active to be included in a proposal");

        this.figure = figure;
        this.typeSettings = new HashSet<>();
    }

    /**
     * Default constructor for ORM purposes.
     * This constructor is used by the ORM framework and should not be used directly.
     */
    protected ProposalFigure() {
        // for ORM
        this.figure = null;
    }

    /**
     * Associates a drone type setting with this figure in the proposal.
     * @param typeSetting The setting to add.
     */
    public void addTypeSetting(final DroneTypeSetting typeSetting) {
        this.typeSettings.add(typeSetting);
    }

    /**
     * Retrieves Figure associated with this ProposalFigure.
     * @return The Figure associated with this ProposalFigure.
     */
    public Figure figure() {
        return this.figure;
    }

    /**
     * Method to compare two ProposalFigure objects for equality.
     * @param o
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalDroneModel that = (ProposalDroneModel) o;
        return id != null && id.equals(that.getId());
    }

    /**
     * Method to generate a hash code for the ProposalFigure object.
     * @return hash code of the object.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Method toString for the ProposalFigure object.
     * @return String representation of the ProposalFigure object.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(figure.toString());
        Set<DroneTypeSetting> typeSettings = getTypeSettings();
        if (typeSettings.isEmpty()) {
            sb.append("No drone type settings");
        }
        for (DroneTypeSetting setting : typeSettings) {
            sb.append(setting.toString()).append("\n");
        }
        return sb.toString();
    }
}
