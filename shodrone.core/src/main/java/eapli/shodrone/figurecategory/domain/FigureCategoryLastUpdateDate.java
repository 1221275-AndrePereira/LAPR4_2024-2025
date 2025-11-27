package eapli.shodrone.figurecategory.domain;


import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * The type figure category last update date
 */
@Embeddable
public class FigureCategoryLastUpdateDate implements ValueObject, Serializable {

    private LocalDate lastUpdateDate;

    protected FigureCategoryLastUpdateDate() {

    }

    /**
     * Instantiates a new figure category last update date.
     *
     * @param lastUpdateDate the figure category last update date
     */
    public FigureCategoryLastUpdateDate(LocalDate lastUpdateDate) {
        if (lastUpdateDate == null) throw new IllegalArgumentException("Last update date must exist");
        this.lastUpdateDate = lastUpdateDate;
    }

    public LocalDate lastUpdateDate() {
        return lastUpdateDate;
    }
}
