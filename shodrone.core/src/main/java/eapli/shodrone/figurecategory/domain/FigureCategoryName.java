package eapli.shodrone.figurecategory.domain;


import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

/**
 * The type figure category name
 */
@Embeddable
public class FigureCategoryName implements ValueObject, Serializable {

    private String name;

    protected FigureCategoryName() {

    }

    /**
     * Instantiates a new figure category name.
     *
     * @param figureCategoryName the figure category name
     */
    public FigureCategoryName(String figureCategoryName) {
        if (figureCategoryName == null) throw new IllegalArgumentException("Figure Category must have a name");
        this.name = figureCategoryName;
    }

    public String figureCategoryName() {
        return name;
    }


}
