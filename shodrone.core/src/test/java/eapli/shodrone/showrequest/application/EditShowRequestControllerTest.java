package eapli.shodrone.showrequest.application;

import eapli.framework.general.domain.model.Description;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;

import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showrequest.domain.*;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@Ignore
@Disabled
class EditShowRequestControllerTest {
//
//    @Mock
//    private AuthorizationService mockAuthz;
//    @Mock
//    private ShowRequestRepository mockShowRequestRepository;
//    @Mock
//    private ShodroneUser mockCustomer;
//    @Mock
//    private SystemUser mockSystemUser; // For stubbing mockCustomer.systemUser()
//
//    private ShowRequest mockRequest;
//
//    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//    private EditShowRequestController controller;
//
//    Description description;
//    RequestPlace place;
//    RequestShowDate date;
//    RequestDuration duration;
//    RequestNDrones numDrones;
//
//
//    private SimpleDateFormat newDateFormat;
//    private String newCustomer;
//
//    Description newDescription;
//    RequestPlace newPlace;
//    RequestShowDate newDate;
//    RequestDuration newDuration;
//    RequestNDrones newNDrones;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        mockAuthz = mock(AuthorizationService.class);
//
//        mockShowRequestRepository = mock(ShowRequestRepository.class);
//
//        controller = new EditShowRequestController(mockAuthz, mockShowRequestRepository);
//
//        String originalText = "Test Show Description";
//        String orginalPlaceText = "Test Show Location";
//        int originalDurationMinutes = 60;
//        int originalNumberOfDrones = 128;
//
//        TimeZone utc = TimeZone.getTimeZone("UTC");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setTimeZone(utc);
//
//        Calendar originalShowDateTime = Calendar.getInstance(utc);
//        originalShowDateTime.set(2026, Calendar.MARCH, 20, 18, 0, 0);
//        originalShowDateTime.set(Calendar.MILLISECOND, 0);
//
//        Calendar creationDate = Calendar.getInstance(utc);
//        creationDate.set(2026, Calendar.JUNE, 5, 18, 0, 0);
//        creationDate.set(Calendar.MILLISECOND, 0);
//
//        description = Description.valueOf(originalText);
//        place = RequestPlace.valueOf(orginalPlaceText);
//        date = RequestShowDate.valueOf(originalShowDateTime);
//        RequestCreationDate cDate = RequestCreationDate.valueOf(creationDate);
//        duration = RequestDuration.valueOf(originalDurationMinutes);
//        numDrones = RequestNDrones.valueOf(originalNumberOfDrones);
//
//
//        String newDescriptionText = "New Test Show Description";
//        String newPlaceText = "New Test Show Location";
//        int newDurationMinutes = 30;
//        int newNumberOfDrones = 256;
//
//        Calendar newRequestShowDateTime = Calendar.getInstance(utc);
//        newRequestShowDateTime.set(2024, Calendar.MARCH, 20, 18, 0, 0);
//        newRequestShowDateTime.set(Calendar.MILLISECOND, 0);
//
//        newDescription = Description.valueOf(newDescriptionText);
//        newPlace = RequestPlace.valueOf(newPlaceText);
//        newDate = RequestShowDate.valueOf(newRequestShowDateTime);
//        newDuration = RequestDuration.valueOf(newDurationMinutes);
//        newNDrones = RequestNDrones.valueOf(newNumberOfDrones);
//
//        lenient().when(mockCustomer.systemUser()).thenReturn(mockSystemUser);
//        lenient().when(mockShowRequestRepository.save(any(ShowRequest.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        mockRequest = new ShowRequest(
//                description,
//                place,
//                cDate,
//                date,
//                duration,
//                numDrones,
//                mockCustomer
//        );
//    }
//
//    @Test
//    void ensureEditShowRequestUpdatesCorrectly() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // Act
//        ShowRequest editedRequest = controller.editShowRequest(
//                mockRequest,
//                newDescription,
//                newPlace,
//                newDate,
//                newDuration,
//                newNDrones,
//                mockCustomer
//        );
//
//        // Assert
//        assertNotNull(editedRequest);
//        assertEquals(newDescription, editedRequest.getDescription());
//        assertEquals(newPlace, editedRequest.getPlace());
//        assertEquals(newDate, editedRequest.getShowDate());
//        assertEquals(newDuration, editedRequest.getDuration());
//        assertEquals(newNDrones, editedRequest.getNdDrones());
//        assertEquals(mockCustomer, editedRequest.getCustomer());
//
//        verify(mockShowRequestRepository).save(editedRequest);
//    }
//
//    @Test
//    void ensureEditShowRequestNotUpdatesWrong() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // another mockCustomer for testing
//        ShodroneUser anotherMockCustomer = mock(ShodroneUser.class);
//        when(anotherMockCustomer.systemUser()).thenReturn(mockSystemUser);
//
//        // Act
//        ShowRequest editedRequest = controller.editShowRequest(
//                mockRequest,
//                newDescription,
//                newPlace,
//                newDate,
//                newDuration,
//                newNDrones,
//                anotherMockCustomer
//        );
//
//        // Assert
//        assertNotNull(editedRequest);
//        assertNotEquals(description, editedRequest.getDescription());
//        assertNotEquals(place, editedRequest.getPlace());
//        assertNotEquals(date, editedRequest.getShowDate());
//        assertNotEquals(duration, editedRequest.getDuration());
//        assertNotEquals(numDrones, editedRequest.getNdDrones());
//        assertNotEquals(mockCustomer, editedRequest.getCustomer());
//
//        verify(mockShowRequestRepository).save(editedRequest);
//    }
//
//
//
//
//    @Test
//    void ensureEditShowRequestHandlesInvalidRequest() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.editShowRequest(
//                        null,
//                        newDescription,
//                        newPlace,
//                        newDate,
//                        newDuration,
//                        newNDrones,
//                        mockCustomer
//                )
//        );
//    }
//
//    @Test
//    void ensureEditShowRequestHandlesInvalidDescription() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.editShowRequest(
//                        mockRequest,
//                        null,
//                        newPlace,
//                        newDate,
//                        newDuration,
//                        newNDrones,
//                        mockCustomer
//                )
//        );
//    }
//
//    @Test
//    void ensureEditShowRequestHandlesInvalidPlace() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.editShowRequest(
//                        mockRequest,
//                        newDescription,
//                        null,
//                        newDate,
//                        newDuration,
//                        newNDrones,
//                        mockCustomer
//                )
//        );
//    }
//
//    @Test
//    void ensureEditShowRequestHandlesInvalidDate() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.editShowRequest(
//                        mockRequest,
//                        newDescription,
//                        newPlace,
//                        null, // Invalid date
//                        newDuration,
//                        newNDrones,
//                        mockCustomer
//                )
//        );
//    }
//
//    @Test
//    void ensureEditShowRequestHandlesInvalidDuration() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.editShowRequest(
//                        mockRequest,
//                        newDescription,
//                        newPlace,
//                        newDate,
//                        null, // Invalid duration
//                        newNDrones,
//                        mockCustomer
//                )
//        );
//    }
//
//    @Test
//    void ensureEditShowRequestHandlesNullNDrones() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.editShowRequest(
//                        mockRequest,
//                        newDescription,
//                        newPlace,
//                        newDate,
//                        newDuration,
//                        null,
//                        mockCustomer
//                )
//        );
//    }
//
//    @Test
//    void ensureEditShowRequestHandlesInvalidCustomer() {
//        // Arrange
//        when(mockShowRequestRepository.ofIdentity(any())).thenReturn(java.util.Optional.of(mockRequest));
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.editShowRequest(
//                        mockRequest,
//                        newDescription,
//                        newPlace,
//                        newDate,
//                        newDuration,
//                        newNDrones,
//                        null // Invalid customer
//                )
//        );
//    }

}