package eapli.shodrone.figure.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Entity
@Table(name ="FIGURE", uniqueConstraints = @UniqueConstraint(columnNames = {"DESCRIPTION", "FIGURE_VERSION"}))
public class Figure implements AggregateRoot<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private SystemUser author;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    private ShodroneUser exclusiveCustomer;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value", column = @Column(name = "DESCRIPTION", nullable = false))})
    private Description description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "FIGURE_KEYWORDS", joinColumns = @JoinColumn(name = "FIGURE_ID"))
    @AttributeOverride(name = "value", column = @Column(name = "KEYWORD", nullable = false))
    private Set<Keyword> keywords;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value", column = @Column(name = "FIGURE_VERSION", nullable = false))})
    private Version figureVersion;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value", column = @Column(name = "DSL_CODE", nullable = false, columnDefinition = "CLOB"))})
    private CodePath dslCode;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value", column = @Column(name = "DSL_VERSION", nullable = false))})
    private Version dslVersion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private FigureCategory category;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDate creationDate;

    @Column(name = "ACTIVE")
    private boolean isActive;


    /**
     * Protected constructor for ORM.
     */
    protected Figure() {
        this.author = null;
        this.category = null;
        this.description = new Description();
        this.figureVersion = new Version();
        this.dslCode = new CodePath();
        this.dslVersion = new Version();
        this.keywords = new HashSet<>();
        this.exclusiveCustomer = null;
        this.creationDate = null;
        this.isActive = true;
    }

    /**
     * Private constructor to be called by the Builder.
     * Ensures all mandatory fields are present and the object is valid upon creation.
     */
    public Figure(FigureBuilder builder) {
        this.author = builder.author;
        this.category = builder.category;
        this.description = builder.description;
        this.figureVersion = builder.figureVersion;
        this.dslCode = builder.dslCode;
        this.dslVersion = builder.dslVersion;
        this.keywords = builder.keywords;

        this.exclusiveCustomer = builder.exclusiveCustomer;

        this.creationDate = LocalDate.now();
        this.isActive = true;
    }

    /**
     * Deactivates the figure.
     */
    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public boolean sameAs(final Object other) {
        if (!(other instanceof Figure that)) {
            return false;
        }

        return this.identity().equals(that.identity());
    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Figure figure = (Figure) o;

        return this.id != null && this.id.equals(figure.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return //"Figure: " +
                "\ndescription: " + description +
                "\nkeywords: " + keywords +
                "\nfigureVersion: " + figureVersion +
                "\nexclusiveCustomer: " + (exclusiveCustomer != null ? exclusiveCustomer.identity() : "null") +
                "\n";

    }
}