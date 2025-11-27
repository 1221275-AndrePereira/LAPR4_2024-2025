package eapli.shodrone.figurecategory.domain;


import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * The type figure category creation date
 */
@Embeddable
public class FigureCategoryCreationDate implements ValueObject, Serializable {


    private LocalDate creationDate;

    protected FigureCategoryCreationDate() {

    }

    /**
     * Instantiates a new figure category creation date.
     *
     * @param creationDate the creation date of the figure category
     */
    public FigureCategoryCreationDate(LocalDate creationDate) {
        if (creationDate == null) throw new IllegalArgumentException("Creation date must exist");
        this.creationDate = creationDate;
    }

    public LocalDate creationDate() {
        return creationDate;
    }
}
