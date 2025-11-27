package eapli.shodrone.showrequest.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.general.domain.model.Description;
import eapli.framework.validations.Preconditions;

import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;

import eapli.shodrone.showProposal.domain.ShowProposal;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "show_requests")
public class ShowRequest implements AggregateRoot<Long> {

    @Id
    @GeneratedValue //(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Embedded
    private Description description;

    @Embedded
    private RequestPlace placeCoordinates;

    @Embedded
    private RequestCreationDate requestCreateDate;

    @Embedded
    private RequestShowDate requestShowDate;

    @Embedded
    private RequestDuration requestDuration;

    @Embedded
    private RequestNDrones requestNdDrones;

    @ManyToOne
    private ShodroneUser customer;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<ShowProposal> proposals;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Figure> figure;

    //TODO: Workflow history:
//    @OneToOne
//    private ShodroneUser author;
//    private String createdBy;
//    private LocalDate createdAt;
//    private String lastModifiedBy;
//    private LocalDate lastModifiedAt;
//
//    @ElementCollection
//    private List<String> history; // To track changes

    /**
     * Constructor for ShowRequest
     *
     * @param description       Description of the show request
     * @param placeCoordinates      Place of the show request
     * @param requestCreateDate Creation date of the show request
     * @param requestShowDate   Show date of the show request
     * @param requestDuration   Duration of the show request
     * @param requestNdDrones   Number of drones for the show request
     */
    public ShowRequest(
            Description description,
            RequestPlace placeCoordinates,
            RequestCreationDate requestCreateDate,
            RequestShowDate requestShowDate,
            RequestDuration requestDuration,
            RequestNDrones requestNdDrones,
            ShodroneUser customer,
            List<Figure> figure
    ) {

        Preconditions.noneNull(
                description,
                placeCoordinates,
                requestCreateDate,
                requestShowDate,
                requestDuration,
                requestNdDrones,
                customer,
                figure
        );

        this.description = description;
        this.placeCoordinates = placeCoordinates;
        this.requestCreateDate = requestCreateDate;
        this.requestShowDate = requestShowDate;
        this.requestDuration = requestDuration;
        this.requestNdDrones = requestNdDrones;
        this.customer = customer;
        this.figure = figure;
    }

    /**
     * Compares the current ShowRequest instance with another object to determine if they are the same.
     * Two ShowRequest objects are considered the same if all their attributes are equal.
     *
     * @param other the object to be compared with the current ShowRequest instance
     * @return true if the specified object is a ShowRequest and all attributes are equal; false otherwise
     */
    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof ShowRequest that)) {
            return false;
        }

        if (this == that) {
            return true;
        }

                return this.description.equals(that.description) &&
                this.placeCoordinates.equals(that.placeCoordinates) &&
                this.requestCreateDate.equals(that.requestCreateDate) &&
                this.requestShowDate.equals(that.requestShowDate) &&
                this.requestDuration.equals(that.requestDuration) &&
                this.requestNdDrones.equals(that.requestNdDrones) &&
                this.figure.equals(that.figure);
    }

    public RequestPlace getPlace() {
        return this.placeCoordinates;
    }

    public RequestCreationDate getCreationDate() {
        return this.requestCreateDate;
    }

    public RequestShowDate getShowDate() {
        return this.requestShowDate;
    }

    public RequestDuration getDuration() {
        return this.requestDuration;
    }

    public RequestNDrones getNdDrones() {
        return this.requestNdDrones;
    }

    public Long version() {
        return this.version;
    }

    @Override
    public Long identity() {
        return this.id;
    }

    public void updateShowDescription(Description newDescription) {
        this.description = newDescription;
    }

    public void updateShowDate(RequestShowDate newDate) {
        this.requestShowDate = newDate;
    }

    public void updateShowDuration(RequestDuration newDuration) {
        this.requestDuration = newDuration;
    }

    public void updateShowPlace(RequestPlace newLocation) {
        this.placeCoordinates = newLocation;
    }

    public void updateShowNDrones(RequestNDrones newNDrones) {
        this.requestNdDrones = newNDrones;
    }

    public void updateShowCustomer(ShodroneUser newCustomer) {
        this.customer = newCustomer;
    }

    public void addProposal(ShowProposal newProposal) {this.proposals.add(newProposal);}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Show Request ID
        sb.append("Show Request ID: ");
        if (getId() != null) {
            sb.append(getId());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Description
        sb.append("Description: ");
        if (getDescription() != null) {
            sb.append(getDescription().toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Place
        sb.append("Place: ");
        if (getPlace() != null) {
            sb.append(getPlace().toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Creation Date
        sb.append("Creation Date: ");
        if (getCreationDate() != null) {
            sb.append(getCreationDate().toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Show Date
        sb.append("Show Date: ");
        if (getShowDate() != null) {
            sb.append(getShowDate().toString());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Duration
        sb.append("Duration: ");
        if (getDuration() != null) {
            sb.append(getDuration().minutes()).append(" minutes");
        } else {
            sb.append("N/A minutes");
        }
        sb.append("\n");

        // Number of Drones
        sb.append("Number of Drones: ");
        if (getNdDrones() != null) {
            sb.append(getNdDrones().number());
        } else {
            sb.append("N/A");
        }
        sb.append("\n");

        // Customer
        sb.append("Customer: ");
        if (getCustomer() != null && getCustomer().systemUser() != null) {
            sb.append(getCustomer().systemUser().username());
        } else {
            sb.append("No customer assigned");
        }
        sb.append("\n");

        // Figure
        sb.append("Figures: \n");
        List<Figure> figures = getFigure();
        for (Figure figure : figures) {
            sb.append(figure.toString()).append("\n");
        }

        return sb.toString();
    }
}