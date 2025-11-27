
# US233 - Add figure

## 4. Tests and Implementation

´´
@Test
void addFigure_success_noExclusiveUser() throws IOException {

        Path p = Paths.get("../docs/FigureDSL/").toAbsolutePath();
        String resolvedPathString = p.resolve("sample_DSL_figure_1.txt").toString();

        File file = new File(resolvedPathString);

        Figure result = figureController.addFigure(description1, Set.of("Monkey", "Banana", "Jungle"), v1,cs1, Set.of(file.getAbsolutePath()), null);



        assertNotNull(result);
        assertEquals(description1, result.getDescription());
        assertEquals(Version.valueOf(v1), result.getFigureVersion());
        assertEquals(mockSystemUser, result.getAuthor());
        assertNull(result.getExclusiveCustomer());
        assertFalse(result.getCode().contains(file.getAbsolutePath()));

        verify(mockFigureRepository).save(any(Figure.class));
    }
´´