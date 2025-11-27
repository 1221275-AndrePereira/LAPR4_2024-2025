package eapli.shodrone.showProposal.domain.proposalFields;

import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.showProposal.domain.proposalDrone.DroneTypeSetting;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProposalFigureTest {

    void setupValidFigure() {

    }

    @Test
    void testCreateProposalFigureWithValidFigure() {
        Figure mockFigure = mock(Figure.class);
        when(mockFigure.isActive()).thenReturn(true);

        ProposalFigure proposalFigure = new ProposalFigure(mockFigure);

        assertEquals(mockFigure, proposalFigure.figure());
        assertEquals(0, proposalFigure.getTypeSettings().size());

    }

    @Test
    void testAddAdditionalTypeSetting() {
        Figure mockFigure = mock(Figure.class);
        when(mockFigure.isActive()).thenReturn(true);

        DroneTypeSetting setting1 = mock(DroneTypeSetting.class);
        DroneTypeSetting setting2 = mock(DroneTypeSetting.class);

        ProposalFigure proposalFigure = new ProposalFigure(mockFigure);
        proposalFigure.addTypeSetting(setting1);
        proposalFigure.addTypeSetting(setting2);

        Set<DroneTypeSetting> settings = proposalFigure.getTypeSettings();
        assertEquals(2, settings.size());
        assertTrue(settings.contains(setting1));
        assertTrue(settings.contains(setting2));
    }

    @Test
    void testRejectsInactiveFigure() {
        Figure mockFigure = mock(Figure.class);
        when(mockFigure.isActive()).thenReturn(false);
        DroneTypeSetting mockSetting = mock(DroneTypeSetting.class);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProposalFigure(mockFigure)
        );
        assertTrue(ex.getMessage().contains("Figure must be active"));
    }

    @Test
    void testRejectsNullFigure() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProposalFigure(null)
        );
        assertTrue(ex.getMessage().contains("Figure cannot be null"));
    }
}
