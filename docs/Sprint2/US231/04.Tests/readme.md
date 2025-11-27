
# US233 - List figure

## 4. Tests and Implementation

````
@Test
void listActiveFigures_repositoryReturnsFigures_returnsListOfFigures() {
Figure mockFigure1 = mock(Figure.class);
Figure mockFigure2 = mock(Figure.class);
List<Figure> expectedFigures = List.of(mockFigure1, mockFigure2);

        when(mockFigureRepository.allActiveFigures()).thenReturn(expectedFigures);

        List<Figure> actualFigures = listFigureController.listActiveFigures();

        assertNotNull(actualFigures);
        assertEquals(2, actualFigures.size());
        assertEquals(expectedFigures, actualFigures);
        verify(mockFigureRepository).allActiveFigures();
    }
````