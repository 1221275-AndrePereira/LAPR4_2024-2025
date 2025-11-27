
# US234 - Decomission Figure

## 4. Tests and Implementation

````
@Test
void decomissionFigureTest() {
Figure mockFigure = mock(Figure.class);
when(mockFigureRepository.save(mockFigure)).thenReturn(mockFigure);

        Figure result = listFigureController.decomissionFigure(mockFigure);

        assertNotNull(result);
        verify(mockFigure).deActivate();
        verify(mockFigureRepository).save(mockFigure);
        assertEquals(mockFigure, result);
    }
````
