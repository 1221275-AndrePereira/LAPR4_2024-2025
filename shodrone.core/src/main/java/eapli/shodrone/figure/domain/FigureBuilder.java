package eapli.shodrone.figure.domain;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A builder for the Figure aggregate root.
 * <p>
 * This class follows the Builder Pattern to provide a fluent and readable API
 * for constructing a valid Figure object.
 */
public final class FigureBuilder {

    private static final int KEYWORD_LIMIT = 5;

    final SystemUser author;
    final FigureCategory category;
    Description description;
    Version figureVersion;
    CodePath dslCode;
    Version dslVersion;
    Set<Keyword> keywords = new HashSet<>();

    ShodroneUser exclusiveCustomer;

    /**
     * Starts building a new Figure with its mandatory associations.
     *
     * @param author   The author of the figure.
     * @param category The category of the figure.
     */
    public FigureBuilder(final SystemUser author, final FigureCategory category) {
        Preconditions.nonNull(author, "Figure must have an author.");
        Preconditions.nonNull(category, "Figure must have a category.");
        this.author = author;
        this.category = category;
    }

    public FigureBuilder withDescription(final String description) {
        this.description = Description.valueOf(description);
        return this;
    }

    public FigureBuilder withKeywords(final Set<String> keywords) {
        this.keywords = keywords.stream().map(Keyword::valueOf).collect(Collectors.toSet());
        return this;
    }

    public FigureBuilder withFigureVersion(final String version) {
        this.figureVersion = Version.valueOf(version);
        return this;
    }

    public FigureBuilder withDsl(final String dslCodePath, final String dslVersion) {
        this.dslCode = CodePath.valueOf(dslCodePath);
        this.dslVersion = Version.valueOf(dslVersion);
        return this;
    }

    public FigureBuilder exclusivelyFor(final ShodroneUser customer) {
        this.exclusiveCustomer = customer;
        return this;
    }

    /**
     * Builds the Figure instance after ensuring all mandatory fields have been set.
     *
     * @return a new, valid Figure instance.
     * @throws IllegalArgumentException if any mandatory field was not provided.
     */
    public Figure build() {
        Preconditions.nonEmpty(keywords, "There must be at least one keyword.");
        Preconditions.ensure(keywords.size() <= KEYWORD_LIMIT, "There must be at least one keyword.");
        Preconditions.nonNull(description, "Description is required for building a Figure.");
        Preconditions.nonNull(figureVersion, "Figure version is required for building a Figure.");
        Preconditions.nonNull(dslCode, "DSL code path is required for building a Figure.");
        Preconditions.nonNull(dslVersion, "DSL version is required for building a Figure.");

        return new Figure(this);
    }
}