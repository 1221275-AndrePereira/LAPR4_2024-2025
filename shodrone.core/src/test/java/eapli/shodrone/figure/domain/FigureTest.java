package eapli.shodrone.figure.domain;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FigureTest {

    private SystemUser mockAuthor;
    private ShodroneUser mockExclusiveCustomer;
    private Set<Keyword> keywords;
    private Set<String> keywordsString;
    private FigureCategory category;
    private String code;
    private final String FIGURE_DESCRIPTION = "Test Figure";
    private final String FIGURE_VERSION_STR = "1.0";
    private final String DSL_VERSION_STR = "1.1.23";

    @BeforeEach
    void setUp() {
        mockAuthor = mock(SystemUser.class);
        mockExclusiveCustomer = mock(ShodroneUser.class);
        FigureCategory mockCategory1 = mock(FigureCategory.class);

        keywordsString = new HashSet<>(Set.of("test", "test2"));
        keywords = new HashSet<>(Set.of(Keyword.valueOf("test"), Keyword.valueOf("test2")));
        category = mockCategory1;
        code = "docs/FigureDSL/sample_DSL_figure_1.txt";
    }

    private Figure createTestFigure(ShodroneUser exclusiveCustomer) {
        FigureBuilder builder = new FigureBuilder(mockAuthor,category)
                .withDescription(FIGURE_DESCRIPTION)
                .withFigureVersion(FIGURE_VERSION_STR)
                .withKeywords(keywordsString)
                .exclusivelyFor(exclusiveCustomer)
                .withDsl(code, DSL_VERSION_STR);

        return builder.build();
    }

    @Test
    void constructor_validArguments_createsInstance() {
        Figure figure = createTestFigure(null);

        assertNotNull(figure);
        assertTrue(mockAuthor.equals(figure.getAuthor()));
        assertEquals(keywords,(figure.getKeywords()));
        assertEquals(category, figure.getCategory());
        assertEquals(FIGURE_DESCRIPTION, figure.getDescription().toString());
        assertEquals(Version.valueOf(FIGURE_VERSION_STR), figure.getFigureVersion());
        assertEquals(code, figure.getDslCode().toString());
        assertEquals(Version.valueOf(DSL_VERSION_STR), figure.getDslVersion());
        assertNull(figure.getExclusiveCustomer());
        assertEquals(LocalDate.now(), figure.getCreationDate());
        assertTrue(figure.isActive());
        assertNull(figure.identity());
    }

    @Test
    void constructor_withExclusiveCustomer_setsCustomer() {
        Figure figure = createTestFigure(mockExclusiveCustomer);
        assertNotNull(figure.getExclusiveCustomer());
        assertEquals(mockExclusiveCustomer, figure.getExclusiveCustomer());
    }

    @Test
    void constructor_figureVersionObjectCorrectlyCreated() {
        Figure figure = createTestFigure(null);
        assertEquals(FIGURE_VERSION_STR, figure.getFigureVersion().toString());
    }

    @Test
    void constructor_dslVersionObjectCorrectlyCreated() {
        Figure figure = createTestFigure(null);
        assertEquals(DSL_VERSION_STR, figure.getDslVersion().toString());
    }

    @Test
    void deActivate_setsIsActiveToFalse() {
        Figure figure = createTestFigure(null);
        assertTrue(figure.isActive());
        figure.deactivate();
        assertFalse(figure.isActive());
    }

    @Test
    void sameAs_identicalFigure_returnsTrue() {
        FigureBuilder builder = new FigureBuilder(mockAuthor,category)
                .withDescription(FIGURE_DESCRIPTION)
                .withFigureVersion(FIGURE_VERSION_STR)
                .withKeywords(keywordsString)
                .exclusivelyFor(mockExclusiveCustomer)
                .withDsl(code, DSL_VERSION_STR);

        FigureBuilder builder2 = new FigureBuilder(mockAuthor,category)
                .withDescription(FIGURE_DESCRIPTION)
                .withFigureVersion(FIGURE_VERSION_STR)
                .withKeywords(keywordsString)
                .withDsl(code, DSL_VERSION_STR);

        assertNotEquals(builder2.build(), builder.build());
    }


    @Test
    void sameAs_differentDescription_returnsFalse() {
        Figure figure1 = createTestFigure(null);

        FigureBuilder builder = new FigureBuilder(mockAuthor,category)
                .withDescription(FIGURE_DESCRIPTION)
                .withFigureVersion(FIGURE_VERSION_STR)
                .withKeywords(keywordsString)
                .withDsl(code, DSL_VERSION_STR);

        assertNotEquals(figure1, builder.build());
    }

    @Test
    void sameAs_differentFigureVersion_returnsFalse() {
        Figure figure1 = createTestFigure(null);
        FigureBuilder builder = new FigureBuilder(mockAuthor,category)
                .withDescription(FIGURE_DESCRIPTION)
                .withFigureVersion("1.1")
                .withKeywords(keywordsString)
                .withDsl(code, DSL_VERSION_STR);
        assertNotEquals(figure1, builder.build());
    }

    @Test
    void toString_containsDescriptionAndVersion() {
        Figure figure = createTestFigure(null);
        String str = figure.toString();
        assertTrue(str.contains(FIGURE_DESCRIPTION));
        assertTrue(str.contains(FIGURE_VERSION_STR));
        assertTrue(str.contains(keywords.toString()));
    }

    @Test
    void identity_newFigure_returnsNull() {
        Figure figure = createTestFigure(null);
        assertNull(figure.identity());
    }
}