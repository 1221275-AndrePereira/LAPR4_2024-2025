package eapli.shodrone.showrequest.domain;

import eapli.framework.general.domain.model.Description;
import eapli.framework.domain.model.DomainFactory;
import eapli.framework.validations.Preconditions;

import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.figure.domain.Figure;

import java.util.ArrayList;
import java.util.List;

/**
 * The type ShowRequest category builder
 */
public class ShowRequestBuilder implements DomainFactory<ShowRequest> {

    private Description description;
    private RequestPlace requestPlace;
    private RequestCreationDate requestCreateDate;
    private RequestShowDate requestShowDate;
    private RequestDuration requestDuration;
    private RequestNDrones requestNdDrones;
    private ShodroneUser customer;
    private List<Figure> figure;


    /**
     * Build a ShowRequest object
     *
     * @return - returning a ShowRequest object
     */
    @Override
    public ShowRequest build() {
        if (this.figure == null) {
            this.figure = new ArrayList<>();
        }

        return new ShowRequest(
                description,
                requestPlace,
                requestCreateDate,
                requestShowDate,
                requestDuration,
                requestNdDrones,
                customer,
                figure
        );
    }

    /**
     * With description builder
     *
     * @param description - show description
     * @return Description
     */
    public ShowRequestBuilder withDescription(Description description) {
        this.description = description;
        return this;
    }

    /**
     * With place category builder
     *
     * @param requestPlace - show-place date
     * @return RequestPlace
     */
    public ShowRequestBuilder withRequestPlace(RequestPlace requestPlace) {
        this.requestPlace = requestPlace;
        return this;
    }

    /**
     * With creation date category builder
     *
     * @param requestCreateDate - original show request generation date
     *                          this value can't be edited in the future
     * @return RequestCreationDate
     */
    public ShowRequestBuilder withRequestCreateDate(RequestCreationDate requestCreateDate) {
        this.requestCreateDate = requestCreateDate;
        return this;
    }

    /**
     * With show date category builder
     *
     * @param requestShowDate - marked show date
     * @return RequestShowDate
     */
    public ShowRequestBuilder withRequestShowDate(RequestShowDate requestShowDate) {
        this.requestShowDate = requestShowDate;
        return this;
    }

    /**
     * With duration category builder
     *
     * @param requestDuration - show duration
     * @return RequestDuration
     */
    public ShowRequestBuilder withRequestDuration(RequestDuration requestDuration) {
        this.requestDuration = requestDuration;
        return this;
    }

    /**
     * With number of drones category builder
     *
     * @param requestNdDrones - number of drones
     * @return RequestNDrones
     */
    public ShowRequestBuilder withRequestNdDrones(RequestNDrones requestNdDrones) {
        this.requestNdDrones = requestNdDrones;
        return this;
    }

    /**
     * With customer category builder
     *
     * @param customer - customer object
     * @return ShodroneUser
     */
    public ShowRequestBuilder withCustomer(ShodroneUser customer) {
        this.customer = customer;
        return this;
    }

    /**
     * Sets the figures for the show proposal.
     * Null figures in the input list are ignored.
     *
     * @param figuresParameter The list of figures to associate with the show.
     * @return The current instance of ShowRequestBuilder.
     */
    public ShowRequestBuilder withFigure(List<Figure> figuresParameter) {
        Preconditions.nonNull(figuresParameter, "Input figures list cannot be null.");

        if (this.figure == null) {
            this.figure = new ArrayList<>();
        } else {
             this.figure.clear();
        }

        for (Figure fig : figuresParameter) {
            if (fig != null) {
                this.figure.add(fig);
            }
        }
        return this;
    }

}
