package eapli.shodrone.figurecategory.domain;

import eapli.framework.domain.model.AggregateRoot;

import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The type Figure Category
 */
@Entity
@XmlRootElement
@Getter
@Setter
public class FigureCategory implements AggregateRoot<Long>{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long figureCategoryID;

    @Embedded
    private FigureCategoryName figureCategoryName;

    private String description;

    @Embedded
    private FigureCategoryCreationDate creationDate;

    @Embedded
    private FigureCategoryLastUpdateDate lastUpdateDate;

    @Enumerated(EnumType.STRING)
    private FigureCategoryStatus status;

    @Override
    public Long identity() {
        return figureCategoryID;
    }

    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof FigureCategory)) return false;
        FigureCategory that = (FigureCategory) other;
        return this.figureCategoryName.equals(that.figureCategoryName);
    }

    /**
     * Instantiates a new figure category.
     *
     * @param figureCategoryName the name of the figure category
     * @param description the description of the figure category
     * @param creationDate the creation date of the figure category
     * @param lastUpdateDate the date of the last update of the figure category
     * @param status the status of the figure category(Active/Inactive)
     */
    public FigureCategory(FigureCategoryName figureCategoryName, String description, FigureCategoryCreationDate creationDate,FigureCategoryLastUpdateDate lastUpdateDate, FigureCategoryStatus status) {
        Preconditions.noneNull(figureCategoryName,description,creationDate,lastUpdateDate,status);
        this.figureCategoryName=figureCategoryName;
        this.description=description;
        this.creationDate=creationDate;
        this.lastUpdateDate=lastUpdateDate;
        this.status=status;
    }


    protected FigureCategory() {

    }

    /**
     * Update the figure category name
     *
     * @param newName the new name of the figure category
     */
    public void updateName(final FigureCategoryName newName) {
        if (newName == null) throw new IllegalArgumentException("Figure Category Name cannot be null");
        this.figureCategoryName = newName;
    }

    /**
     * Update the figure category description
     *
     * @param newDescription the new description of the figure category
     */
    public void updateDescription(final String newDescription) {
        this.description = newDescription;
    }

    /**
     * Update the date of last update of the figure category
     *
     * @param lastUpdateDate the last update date of the figure category
     */
    public void updateLastUpdateDate(final FigureCategoryLastUpdateDate lastUpdateDate) {
        if (lastUpdateDate == null) throw new IllegalArgumentException("The last update date cannot be null");
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Update the status of the figure category(Active -> Inactive/Inactive -> Active)
     */
    public void updateStatus() {
        if (this.status == FigureCategoryStatus.ACTIVE) {
            this.status = FigureCategoryStatus.INACTIVE;
        } else if (this.status == FigureCategoryStatus.INACTIVE) {
            this.status = FigureCategoryStatus.ACTIVE;
        }
    }

    @Override
    public String toString() {
        return "Category: " +
                this.figureCategoryName.figureCategoryName() + " - " +
                this.description;
    }

}
