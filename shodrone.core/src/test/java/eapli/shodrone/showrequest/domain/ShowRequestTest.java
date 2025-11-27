package eapli.shodrone.showrequest.domain;

import eapli.framework.general.domain.model.Description;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.ShowProposal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShowRequestTest {

    private Description description;
    private RequestPlace requestPlace;
    private RequestCreationDate requestCreationDate;
    private RequestShowDate requestShowDate;
    private RequestDuration requestDuration;
    private RequestNDrones requestNDrones;

    @Mock
    private ShodroneUser mockCustomer;
    @Mock
    private Figure mockFigure1;
    @Mock
    private Figure mockFigure2;
    private List<Figure> figures;

    // Test data
    private final String validDescriptionStr = "A spectacular drone light show";
    private final Float validLatitude = 40.7128f;
    private final Float validLongitude = -74.0060f;
    private final LocalDateTime validShowDateTime = LocalDateTime.now().plusDays(30).withNano(0); // Avoid nano diffs
    private final LocalDate validCreationDateVal = LocalDate.now();
    private final int validDurationVal = 90;
    private final int validNDronesVal = 50;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock annotated fields

        description = Description.valueOf(validDescriptionStr);
        requestPlace = RequestPlace.valueOf(validLatitude, validLongitude);
        requestCreationDate = RequestCreationDate.valueOf(validCreationDateVal);
        requestShowDate = RequestShowDate.valueOf(validShowDateTime);
        requestDuration = RequestDuration.valueOf(validDurationVal);
        requestNDrones = RequestNDrones.valueOf(validNDronesVal);

        figures = new ArrayList<>();
        figures.add(mockFigure1);
    }

    private ShowRequest createValidShowRequest() {
        return new ShowRequest(
                description,
                requestPlace,
                requestCreationDate,
                requestShowDate,
                requestDuration,
                requestNDrones,
                mockCustomer,
                figures
        );
    }

    // Constructor Tests
    @Test
    void testShowRequest_validConstruction() {
        ShowRequest showRequest = createValidShowRequest();

        assertNotNull(showRequest);
        assertEquals(description, showRequest.getDescription());
        assertEquals(requestPlace, showRequest.getPlaceCoordinates()); // Lombok getter
        assertEquals(requestPlace, showRequest.getPlace());       // Explicit getter
        assertEquals(requestCreationDate, showRequest.getRequestCreateDate()); // Lombok getter
        assertEquals(requestCreationDate, showRequest.getCreationDate());   // Explicit getter
        assertEquals(requestShowDate, showRequest.getRequestShowDate());     // Lombok getter
        assertEquals(requestShowDate, showRequest.getShowDate());           // Explicit getter
        assertEquals(requestDuration, showRequest.getRequestDuration());   // Lombok getter
        assertEquals(requestDuration, showRequest.getDuration());         // Explicit getter
        assertEquals(requestNDrones, showRequest.getRequestNdDrones());    // Lombok getter
        assertEquals(requestNDrones, showRequest.getNdDrones());          // Explicit getter
        assertEquals(mockCustomer, showRequest.getCustomer());
        assertEquals(figures, showRequest.getFigure());
        assertNull(showRequest.identity(), "ID should be null for a new, unpersisted object");
        assertNull(showRequest.version(), "Version should be null for a new, unpersisted object");
        assertNull(showRequest.getProposals(), "Proposals should be null initially as not set by constructor and not initialized by default");
    }

    @Test
    void testShowRequest_nullDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                null, requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, figures
        ));
    }

    @Test
    void testShowRequest_nullRequestPlace_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                description, null, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, figures
        ));
    }

    @Test
    void testShowRequest_nullRequestCreationDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                description, requestPlace, null, requestShowDate, requestDuration, requestNDrones, mockCustomer, figures
        ));
    }

    @Test
    void testShowRequest_nullRequestShowDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                description, requestPlace, requestCreationDate, null, requestDuration, requestNDrones, mockCustomer, figures
        ));
    }

    @Test
    void testShowRequest_nullRequestDuration_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                description, requestPlace, requestCreationDate, requestShowDate, null, requestNDrones, mockCustomer, figures
        ));
    }

    @Test
    void testShowRequest_nullRequestNDrones_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                description, requestPlace, requestCreationDate, requestShowDate, requestDuration, null, mockCustomer, figures
        ));
    }

    @Test
    void testShowRequest_nullCustomer_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                description, requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, null, figures
        ));
    }

    @Test
    void testShowRequest_nullFigureList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                description, requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, null
        ));
    }

    // sameAs Tests
    @Test
    void testSameAs_equalRequests() {
        ShowRequest showRequest1 = createValidShowRequest();
        ShowRequest showRequest2 = new ShowRequest(
                Description.valueOf(validDescriptionStr),
                RequestPlace.valueOf(validLatitude, validLongitude),
                RequestCreationDate.valueOf(validCreationDateVal),
                RequestShowDate.valueOf(validShowDateTime),
                RequestDuration.valueOf(validDurationVal),
                RequestNDrones.valueOf(validNDronesVal),
                mockCustomer, // Customer is not part of sameAs, but keep it consistent for creation
                new ArrayList<>(figures) // Create a new list with same elements for robust comparison
        );
        assertTrue(showRequest1.sameAs(showRequest2));
        assertTrue(showRequest2.sameAs(showRequest1));
    }

    @Test
    void testSameAs_differentDescription() {
        ShowRequest showRequest1 = createValidShowRequest();
        ShowRequest showRequest2 = new ShowRequest(
                Description.valueOf("A completely different show"),
                requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, figures);
        assertFalse(showRequest1.sameAs(showRequest2));
    }

    @Test
    void testSameAs_differentPlace() {
        ShowRequest showRequest1 = createValidShowRequest();
        RequestPlace differentPlace = RequestPlace.valueOf(10.0f, 20.0f);
        ShowRequest showRequest2 = new ShowRequest(
                description, differentPlace,
                requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, figures);
        assertFalse(showRequest1.sameAs(showRequest2));
    }

    @Test
    void testSameAs_differentFigureListContent() {
        ShowRequest showRequest1 = createValidShowRequest(); // Has mockFigure1
        List<Figure> differentFigures = new ArrayList<>();
        differentFigures.add(mockFigure2); // Has mockFigure2
        ShowRequest showRequest2 = new ShowRequest(
                description, requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, differentFigures);
        assertFalse(showRequest1.sameAs(showRequest2));
    }

    @Test
    void testSameAs_differentFigureListSize() {
        ShowRequest showRequest1 = createValidShowRequest(); // Has 1 figure
        List<Figure> differentFigures = new ArrayList<>(figures);
        differentFigures.add(mockFigure2); // Now has 2 figures
        ShowRequest showRequest2 = new ShowRequest(
                description, requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, differentFigures);
        assertFalse(showRequest1.sameAs(showRequest2));
    }

    @Test
    void testSameAs_differentCustomer_shouldBeTrue_asCustomerNotInSameAsLogic() {
        ShowRequest showRequest1 = createValidShowRequest();
        ShodroneUser mockDifferentCustomer = mock(ShodroneUser.class);
        ShowRequest showRequest2 = new ShowRequest(
                description,
                requestPlace,
                requestCreationDate,
                requestShowDate,
                requestDuration,
                requestNDrones,
                mockDifferentCustomer, // Different customer
                new ArrayList<>(figures) // Same figures
        );
        // sameAs does NOT check customer, so this should be true if all other relevant fields are same
        assertTrue(showRequest1.sameAs(showRequest2));
    }


    @Test
    void testSameAs_differentObjectType() {
        ShowRequest showRequest = createValidShowRequest();
        assertFalse(showRequest.sameAs(new Object()));
    }

    @Test
    void testSameAs_nullObject() {
        ShowRequest showRequest = createValidShowRequest();
        assertFalse(showRequest.sameAs(null));
    }

    // Update Methods Tests
    @Test
    void testUpdateShowDescription() {
        ShowRequest showRequest = createValidShowRequest();
        Description newDescription = Description.valueOf("An updated amazing show");
        showRequest.updateShowDescription(newDescription);
        assertEquals(newDescription, showRequest.getDescription());
    }

    @Test
    void testUpdateShowDate() {
        ShowRequest showRequest = createValidShowRequest();
        RequestShowDate newDate = RequestShowDate.valueOf(LocalDateTime.now().plusDays(60).withNano(0));
        showRequest.updateShowDate(newDate);
        assertEquals(newDate, showRequest.getShowDate());
    }

    @Test
    void testUpdateShowDuration() {
        ShowRequest showRequest = createValidShowRequest();
        RequestDuration newDuration = RequestDuration.valueOf(120);
        showRequest.updateShowDuration(newDuration);
        assertEquals(newDuration, showRequest.getDuration());
    }

    @Test
    void testUpdateShowPlace() {
        ShowRequest showRequest = createValidShowRequest();
        RequestPlace newLocation = RequestPlace.valueOf(20.0f, -30.0f);
        showRequest.updateShowPlace(newLocation);
        assertEquals(newLocation, showRequest.getPlace());
    }

    @Test
    void testUpdateShowNDrones() {
        ShowRequest showRequest = createValidShowRequest();
        RequestNDrones newNDrones = RequestNDrones.valueOf(100);
        showRequest.updateShowNDrones(newNDrones);
        assertEquals(newNDrones, showRequest.getNdDrones());
    }

    @Test
    void testUpdateShowCustomer() {
        ShowRequest showRequest = createValidShowRequest();
        ShodroneUser newCustomer = mock(ShodroneUser.class);
        showRequest.updateShowCustomer(newCustomer);
        assertEquals(newCustomer, showRequest.getCustomer());
    }

    @Test
    void testSetFigure_updatesFigureList() { // Testing the Lombok setter
        ShowRequest showRequest = createValidShowRequest();
        List<Figure> newFigures = new ArrayList<>();
        newFigures.add(mockFigure2);
        showRequest.setFigure(newFigures);
        assertEquals(newFigures, showRequest.getFigure());
        assertTrue(showRequest.getFigure().contains(mockFigure2));
        assertEquals(1, showRequest.getFigure().size());
    }

    // addProposal Method Test
    @Test
    void testAddProposal_addsToSetWhenProposalsInitialized() {
        ShowRequest showRequest = createValidShowRequest();
        // The 'proposals' field in ShowRequest is not initialized in the constructor.
        // To test addProposal, we must first set the proposals collection.
        // This highlights a potential improvement for ShowRequest: initialize 'proposals' to an empty Set.
        showRequest.setProposals(new HashSet<>());

        ShowProposal mockProposal = mock(ShowProposal.class);
        showRequest.addProposal(mockProposal);

        assertNotNull(showRequest.getProposals());
        assertEquals(1, showRequest.getProposals().size());
        assertTrue(showRequest.getProposals().contains(mockProposal));
    }

    @Test
    void testAddProposal_throwsNullPointerException_ifProposalsSetNotInitialized() {
        ShowRequest showRequest = createValidShowRequest();
        // At this point, showRequest.getProposals() is null because it's not initialized in the constructor.
        ShowProposal mockProposal = mock(ShowProposal.class);

        assertThrows(NullPointerException.class, () -> {
            showRequest.addProposal(mockProposal);
        }, "addProposal should throw NullPointerException if 'proposals' collection is not initialized.");
    }

    // Identity and Version
    @Test
    void testIdentity_beforePersistence() {
        ShowRequest showRequest = createValidShowRequest();
        assertNull(showRequest.identity(), "Identity should be null before persistence.");
    }

    @Test
    void testVersion_beforePersistence() {
        ShowRequest showRequest = createValidShowRequest();
        assertNull(showRequest.version(), "Version should be null before persistence.");
    }

    // toString() Method Test
    @Test
    void testToString_withAllFieldsSetAndId() {
        ShowRequest showRequest = createValidShowRequest();
        showRequest.setId(1L); // Simulate persisted entity with an ID

        eapli.framework.infrastructure.authz.domain.model.SystemUser mockSystemUser =
                mock(eapli.framework.infrastructure.authz.domain.model.SystemUser.class);
        when(mockCustomer.systemUser()).thenReturn(mockSystemUser);
        when(mockSystemUser.username()).thenReturn(Username.valueOf("testUser123"));

        when(mockFigure1.toString()).thenReturn("Figure[Circle, Color:Red]");

        String expectedString =
                "Show Request ID: 1\n" +
                        "Description: " + validDescriptionStr + "\n" +
                        "Place: " + requestPlace.toString() + "\n" +
                        "Creation Date: " + requestCreationDate.toString() + "\n" +
                        "Show Date: " + requestShowDate.toString() + "\n" +
                        "Duration: " + validDurationVal + " minutes\n" +
                        "Number of Drones: " + validNDronesVal + "\n" +
                        "Customer: testUser123\n" +
                        "Figures: \n" +
                        "Figure[Circle, Color:Red]\n";

        assertEquals(expectedString, showRequest.toString());
    }

    @Test
    void testToString_withNullIdAndCustomerDetails() {
        ShowRequest showRequest = createValidShowRequest();
        // ID is null by default for new objects
        when(mockCustomer.systemUser()).thenReturn(null); // Simulate customer with no system user linked
        when(mockFigure1.toString()).thenReturn("Figure[Square, Color:Blue]");

        String expectedString =
                "Show Request ID: N/A\n" +
                        "Description: " + validDescriptionStr + "\n" +
                        "Place: " + requestPlace.toString() + "\n" +
                        "Creation Date: " + requestCreationDate.toString() + "\n" +
                        "Show Date: " + requestShowDate.toString() + "\n" +
                        "Duration: " + validDurationVal + " minutes\n" +
                        "Number of Drones: " + validNDronesVal + "\n" +
                        "Customer: No customer assigned\n" +
                        "Figures: \n" +
                        "Figure[Square, Color:Blue]\n";

        assertEquals(expectedString, showRequest.toString());
    }

    @Test
    void testToString_withEmptyFigureList() {
        ShowRequest showRequest = new ShowRequest(
                description, requestPlace, requestCreationDate, requestShowDate,
                requestDuration, requestNDrones, mockCustomer, Collections.emptyList() // Empty figure list
        );
        showRequest.setId(2L);
        eapli.framework.infrastructure.authz.domain.model.SystemUser mockSystemUser =
                mock(eapli.framework.infrastructure.authz.domain.model.SystemUser.class);
        when(mockCustomer.systemUser()).thenReturn(mockSystemUser);
        when(mockSystemUser.username()).thenReturn(Username.valueOf("anotherUser"));

        String expectedString =
                "Show Request ID: 2\n" +
                        "Description: " + validDescriptionStr + "\n" +
                        "Place: " + requestPlace.toString() + "\n" +
                        "Creation Date: " + requestCreationDate.toString() + "\n" +
                        "Show Date: " + requestShowDate.toString() + "\n" +
                        "Duration: " + validDurationVal + " minutes\n" +
                        "Number of Drones: " + validNDronesVal + "\n" +
                        "Customer: anotherUser\n" +
                        "Figures: \n"; // No figures listed, just the header

        assertEquals(expectedString, showRequest.toString());
    }
}