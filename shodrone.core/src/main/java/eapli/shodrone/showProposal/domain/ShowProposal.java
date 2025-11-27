package eapli.shodrone.showProposal.domain;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.showProposal.domain.proposalDrone.DroneTypeSetting;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalDroneModel;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalNDrones;
import eapli.shodrone.showProposal.domain.proposalFields.*;
import eapli.shodrone.showProposal.domain.simulationReport.SimulationReport;
import eapli.shodrone.showProposal.domain.simulationReport.SimulationResult;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ShowProposal implements AggregateRoot<Long> {

    @Id
    @GeneratedValue //(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    private ProposalCreationDate creationDate;

    @Getter
    @Setter
    private ProposalStatus status;

    @Getter
    @Setter
    private ProposalVideoLink videoLink;

    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "simulation_report_id")
    private SimulationReport simulationReport;

    @Getter
    @Setter
    private ProposedDuration duration;

    @Getter
    @Setter
    private ProposedPlace proposedPlace;

    @Getter
    @Setter
    private ProposedShowDate proposedShowDate;

    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProposalFigure> figures;

    @Getter
    @Setter
    private ProposalNDrones proposalNDrones;

    @Setter
    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    private Set<ProposalDroneModel> droneModels;

    @Column
    private String customerFeedback;

    @Getter
    @Setter
    @ManyToOne
    private SystemUser manager;

    @Getter
    @Setter
    private Insurance insuranceAmount;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private ProposalDocument proposalDocument;

    @Getter
    private DownLoadCode downLoadCode;


    public ShowProposal(
            String description,
            ProposalNDrones numberOfDrones,
            ProposedDuration duration,
            Insurance insurance,
            ProposedPlace proposedPlace,
            ProposedShowDate proposedShowDate,
            SystemUser manager
    ) {

        Preconditions.noneNull(
                duration,
                proposedPlace,
                numberOfDrones,
                proposedShowDate);

        this.description = description;
        this.proposalNDrones = numberOfDrones;
        this.duration = duration;
        this.insuranceAmount = insurance;
        this.creationDate = new ProposalCreationDate(LocalDate.now());
        this.proposedPlace = proposedPlace;
        this.proposedShowDate = proposedShowDate;
        this.manager = manager;
        this.figures = new ArrayList<>();
        this.videoLink = new ProposalVideoLink();
        updateStatus();
    }

    /**
     * Default constructor for JPA.
     */
    public ShowProposal() {
        // for JPA
        this.proposalNDrones = new ProposalNDrones();
        this.status = ProposalStatus.INCOMPLETE;
        this.duration = new ProposedDuration();
        this.insuranceAmount = new Insurance();
        this.videoLink = new ProposalVideoLink();
        this.creationDate = new ProposalCreationDate();
        this.proposedPlace = new ProposedPlace();
        this.proposedShowDate = new ProposedShowDate();
        this.simulationReport = new SimulationReport();
        this.figures = new ArrayList<>();
        this.droneModels = new HashSet<>();
        this.customerFeedback = "";
        this.downLoadCode = new DownLoadCode();
    }

    /**
     * Returns the ID of the ShowProposal.
     *
     * @return The ID of the ShowProposal.
     */
    @Override
    public Long identity() {
        return this.id;
    }

    /**
     * Accepts the ShowProposal with a feedback message.
     *
     * @param feedback The feedback message for acceptance.
     */
    public void accept(String feedback) {
        this.status = ProposalStatus.ACCEPTED;
        this.customerFeedback = feedback;
    }

    /**
     * Rejects the ShowProposal with a feedback message.
     *
     * @param feedback The feedback message for rejection.
     */
    public void reject(String feedback) {
        this.status = ProposalStatus.REFUSED;
        this.customerFeedback = feedback;
    }

    /**
     * Updates the List<ProposalFigure> of the ShowProposal with the new figures ordered by the customer.
     *
     * @param newFigures the List of ProposalFigures to update the ShowProposal with.
     */
    public void updateFigures(List<ProposalFigure> newFigures) {
        // Ensure the new figures list is not null and contains active figures
        Preconditions.nonNull(newFigures, "No figures provided to update the proposal");
        //Ensure all figures are active
        for (ProposalFigure figure : newFigures) {
            Preconditions.ensure(figure.figure().isActive(), "All figures must be active to be included in a proposal");
        }

        // Clear the previous list of figures
        this.figures.clear();

        // Add the new ordered list of figures to the proposal
        this.figures.addAll(newFigures);
    }

    private void setFigures(List<ProposalFigure> figures){
        this.figures = figures;
    }

    public void clearFigures(){
        this.figures.clear();
    }

    /**
     * Send the show proposal to customer(changes status from "SAFE" to "PENDING")
     * Generates the code to later download document on Show info
     */
    public void sendToCustomer() {
            this.downLoadCode = DownLoadCode.generateNewCode(this.id);
            this.status = ProposalStatus.PENDING;
    }

    /**
     * Associates its corresponding Report of the Show Simulation
     *
     * @param simulationReport the Entity holding the data on the Report of the Simulation
     */
    public void assignReport(final SimulationReport simulationReport) {
        this.simulationReport = simulationReport;
        this.updateStatus();
    }

    /**
     * Changes the Status based on how advanced are its fields.
     * Missing Drones or Figures (INCOMPLETE)
     * Missing Report or Video (TESTING)
     * The simulation reported Success (SAFE / Ready to be sent to customer)
     * The simulation reported Failure (FAILED / will have to be tested)
     */
    public void updateStatus() {
        if (this.getStatus() == ProposalStatus.REFUSED || this.getStatus() == ProposalStatus.ACCEPTED || this.getStatus() == ProposalStatus.PENDING) {
            System.out.println("This proposals state already advanced to " + this.getStatus() + " state. Can't be changed automatically");
            return;
        }
        if (this.getFigures().isEmpty() || this.getDroneModels().isEmpty()) {
            this.status = ProposalStatus.INCOMPLETE;

        } else if (this.videoLink.video().isEmpty() || this.simulationReport == null) {
            this.status = ProposalStatus.TESTING;

        } else if (this.simulationReport.getSimulationResult() != null) {

            if (this.simulationReport.getSimulationResult() == SimulationResult.SUCCESS) {
                this.status = ProposalStatus.SAFE;
            }
            if (this.simulationReport.getSimulationResult() == SimulationResult.FAILURE) {
                this.status = ProposalStatus.FAILED;
            }
        }
    }
    /**
     * Changes the current amount of drones in this proposal
     *
     * @param numberOfDrones number of drones pretended
     */

    public void updateNdrones(int numberOfDrones) {
        this.proposalNDrones = ProposalNDrones.valueOf(numberOfDrones);
    }


    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof ShowProposal that)) {
            return false;
        }
        if (this == that) {
            return true;
        }
        return
                this.status.equals(that.status) &&
                        this.duration.equals(that.duration) &&
                        this.videoLink.equals(that.videoLink) &&
                        this.creationDate.equals(that.creationDate) &&
                        this.proposedPlace.equals(that.proposedPlace) &&
                        this.proposedShowDate.equals(that.proposedShowDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Show Proposal ID
        sb.append("Show Proposal ID: ");
        if (this.id != null) {
            sb.append(this.id);
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Description
        sb.append("Description: ");
        if (this.description != null && !this.description.isEmpty()) {
            sb.append(this.description);
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Creation Date
        sb.append("Creation Date: ");
        if (this.creationDate != null && this.creationDate.date() != null) {
            sb.append(this.creationDate.date().toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Status
        sb.append("Status: ");
        if (this.status != null) {
            sb.append(this.status.toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Proposed Show Date
        sb.append("Proposed Show Date: ");
        if (this.proposedShowDate != null && this.proposedShowDate.date() != null) {
            sb.append(this.proposedShowDate.date().toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Proposed Place
        sb.append("Proposed Place: ");
        if (this.proposedPlace != null && this.proposedPlace.getLatitude() != null && this.proposedPlace.getLongitude() != null) {
            sb.append("Lat: ").append(this.proposedPlace.getLatitude())
                    .append(", Lon: ").append(this.proposedPlace.getLongitude());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Proposed Duration
        sb.append("Proposed Duration: ");
        if (this.duration != null && this.duration.minutes() != 0) {
            sb.append(this.duration.minutes()).append(" minutes");
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Number of Drones
        sb.append("Number of Drones: ");
        if (this.proposalNDrones != null && this.proposalNDrones.number() != 0) {
            sb.append(this.proposalNDrones.number());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Insurance Amount
        sb.append("Insurance Amount: ");
        if (this.insuranceAmount != null && this.insuranceAmount.getInsuranceAmount() != null) {
            sb.append(this.insuranceAmount.getInsuranceAmount().toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Manager
        sb.append("Manager: ");
        if (this.manager != null && this.manager.username() != null) {
            sb.append(this.manager.username().toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Video Link
        sb.append("Video Link: ");
        if (this.videoLink != null && this.videoLink.video() != null && !this.videoLink.video().isEmpty()) {
            sb.append(this.videoLink.video());
        } else {
            sb.append("No Video Link Provided");
        }
        sb.append("\n");

        // Figure
        sb.append("Figures: \n");
        List<ProposalFigure> figures = getFigures();
        for (ProposalFigure figure : figures) {
            sb.append(figure.toString()).append("\n");
        }

        sb.append("-------------------------------------");

        return sb.toString();
    }
}
