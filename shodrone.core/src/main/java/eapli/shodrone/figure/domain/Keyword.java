package eapli.shodrone.figure.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A Value Object representing a single keyword tag.
 * <p>
 * This class enforces the following domain rules:
 * <ul>
 * <li>A keyword must be a single word (no spaces).</li>
 * <li>It can only contain alphanumeric characters and hyphens.</li>
 * <li>It has a maximum length.</li>
 * <li>Comparison and equality are <strong>case-insensitive</strong> (e.g., "Java" is the same as "java").</li>
 * </ul>
 * The internal value is always stored in lowercase after trimming whitespace.
 * </p>
 */
@Getter
@Embeddable
public final class Keyword implements ValueObject, Comparable<Keyword>, Serializable {

    public static final int MAX_LENGTH = 20;
    /**
     * Regex to ensure the keyword is a single token containing only letters, numbers, and hyphens.
     * The `^` and `$` anchors ensure the entire string must match.
     */
    private static final Pattern VALID_KEYWORD_PATTERN = Pattern.compile("^[a-z0-9-]+$");

    private final String value;

    /**
     * Constructs a Keyword. The constructor is private to enforce creation
     * through the public factory method, which clearly expresses intent.
     *
     * @param keyword The raw keyword string.
     */
    private Keyword(final String keyword) {
        final String normalized = keyword.trim().toLowerCase();

        Preconditions.nonEmpty(
                normalized,
                "Keyword cannot be null or blank."
        );
        Preconditions.ensure(normalized.length() <= MAX_LENGTH,"Keyword cannot be longer than " + MAX_LENGTH);
        Preconditions.matches(
                VALID_KEYWORD_PATTERN,
                normalized,
                "Keyword must be a single word containing only letters, numbers, or hyphens."
        );

        this.value = normalized;
    }

    protected Keyword() {
        // for ORM
        this.value = null;
    }

    /**
     * Factory method to create a new Keyword instance.
     * This is the intended public way to create a Keyword.
     *
     * @param keyword The text for the keyword.
     * @return A new instance of Keyword.
     * @throws IllegalArgumentException if the keyword is invalid.
     */
    public static Keyword valueOf(final String keyword) {
        return new Keyword(keyword);
    }

    /**
     * Performs a case-insensitive, alphabetical comparison.
     */
    @Override
    public int compareTo(final Keyword other) {
        return this.value.compareTo(other.value);
    }

    /**
     * Checks for value equality.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Keyword that = (Keyword) o;
        return Objects.equals(this.value, that.value);
    }

    /**
     * Returns the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    /**
     * Returns the normalized (lowercase) string representation of the keyword.
     */
    @Override
    public String toString() {
        return this.value;
    }
}
