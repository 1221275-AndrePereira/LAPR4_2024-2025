package eapli.shodrone.figurecategory.domain;

/**
 * The enum figure category status
 */
public enum FigureCategoryStatus {

    /**
     * Active status
     */
    ACTIVE ("Active"),

    /**
     * Inactive status
     */
    INACTIVE ("Inactive");

    private final String status;

    FigureCategoryStatus (String status) {
        this.status = status;
    }

    /**
     * Gets figure category status
     *
     * @return the figure category status
     */
    public String getStatus() {
        return status;
    }
}
