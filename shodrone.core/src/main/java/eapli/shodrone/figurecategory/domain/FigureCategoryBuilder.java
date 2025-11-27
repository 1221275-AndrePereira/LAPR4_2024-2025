package eapli.shodrone.figurecategory.domain;

import eapli.framework.domain.model.DomainFactory;

/**
 * The type figure category builder
 */
public class FigureCategoryBuilder implements DomainFactory<FigureCategory>{

    private FigureCategoryName name;
    private String description;
    private FigureCategoryCreationDate creationDate;
    private FigureCategoryLastUpdateDate lastUpdateDate;
    private FigureCategoryStatus status;


    /**
     * With name figure category builder
     *
     * @param name the figure category name
     * @return the figure category builder
     */
    public FigureCategoryBuilder withName(FigureCategoryName name) {
        this.name = name;
        return this;
    }

    /**
     * With description figure category builder
     *
     * @param description the figure category description
     * @return the figure category builder
     */
    public FigureCategoryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * With creation date figure category builder
     *
     * @param creationDate the figure category creation date
     * @return the figure category builder
     */
    public FigureCategoryBuilder withCreationDate(FigureCategoryCreationDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    /**
     * With last update date figure category builder
     *
     * @param lastUpdateDate the figure category last update date
     * @return the figure category builder
     */
    public FigureCategoryBuilder withLastUpdateDate(FigureCategoryLastUpdateDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    /**
     * With status figure category builder
     *
     * @param status the figure category status
     * @return the figure category builder
     */
    public FigureCategoryBuilder withStatus(FigureCategoryStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public FigureCategory build() {
        return new FigureCategory(name, description, creationDate, lastUpdateDate, status);
    }
}
