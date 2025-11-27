package eapli.shodrone.figurecategory.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FigureCategoryTest {

    private FigureCategory figureCategory;

    @BeforeEach
    void setUp() {
        FigureCategoryName figureCategoryName = new FigureCategoryName("Category 1");
        String description = "Description";
        LocalDate currentDate = LocalDate.now();
        FigureCategoryCreationDate creationDate = new FigureCategoryCreationDate(currentDate);
        FigureCategoryLastUpdateDate lastUpdateDate = new FigureCategoryLastUpdateDate(currentDate);
        FigureCategoryStatus status= FigureCategoryStatus.ACTIVE;

        figureCategory = new FigureCategory(figureCategoryName,description,creationDate,lastUpdateDate,status);

    }

    @Test
    void testUpdateName(){
        FigureCategoryName newFigureCategoryName = new FigureCategoryName("New name");
        figureCategory.updateName(newFigureCategoryName);
        assertEquals(newFigureCategoryName.figureCategoryName(),figureCategory.getFigureCategoryName().figureCategoryName());
    }

    @Test
    void testUpdateDescription(){
        String newDescription = "New Description";
        figureCategory.updateDescription(newDescription);
        assertEquals(newDescription,figureCategory.getDescription());
    }

    @Test
    void testUpdateLastUpdateDate(){
        FigureCategoryLastUpdateDate newLastUpdateDate = new FigureCategoryLastUpdateDate(LocalDate.now());
        figureCategory.updateLastUpdateDate(newLastUpdateDate);
        assertEquals(newLastUpdateDate,figureCategory.getLastUpdateDate());
    }

    @Test
    void testUpdateStatusToInactive(){
        figureCategory.updateStatus();
        assertEquals(FigureCategoryStatus.INACTIVE,figureCategory.getStatus());
    }


}