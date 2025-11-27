package eapli.shodrone.showrequest.application;

import eapli.framework.general.domain.model.Description;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showrequest.domain.*;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//Test for RegisterShowRequestController.java
class AddShowRequestControllerTest {
//
//    @Mock
//    private ShowRequestRepository mockShowRequestRepository;
//
//    @Mock
//    private AuthorizationService authz;
//
//    @Mock
//    private ShodroneUser mockShodroneUser;
//
//    @Mock
//    private SystemUser mockSystemUser;
//
//    private Calendar requestCreateDateTime;
//    private Calendar requestShowDateTime;
//    private String descriptionText;
//    private String placeText;
//    private int durationMinutes;
//    private int numberOfDrones;
//    private SimpleDateFormat dateFormat;
//
//    Description description;
//    RequestPlace place;
//    RequestCreationDate cDate;
//    RequestShowDate date;
//    RequestDuration duration;
//    RequestNDrones numDrones;
//
//    private RegisterShowRequestController controller;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        controller = new RegisterShowRequestController(mockShowRequestRepository);
//
//        descriptionText = "Test Show Description";
//        placeText = "Test Show Location";
//        durationMinutes = 60;
//        numberOfDrones = 10;
//
//        TimeZone utc = TimeZone.getTimeZone("UTC");
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setTimeZone(utc);
//
//        requestCreateDateTime = Calendar.getInstance(utc);
//        requestCreateDateTime.set(2024, Calendar.MARCH, 15, 10, 0, 0);
//        requestCreateDateTime.set(Calendar.MILLISECOND, 0);
//
//        requestShowDateTime = Calendar.getInstance(utc);
//        requestShowDateTime.set(2024, Calendar.MARCH, 20, 18, 0, 0);
//        requestShowDateTime.set(Calendar.MILLISECOND, 0);
//
//        description = Description.valueOf(descriptionText);
//        place = RequestPlace.valueOf(placeText);
//        cDate = RequestCreationDate.valueOf(requestCreateDateTime);
//        date = RequestShowDate.valueOf(requestShowDateTime);
//        duration = RequestDuration.valueOf(durationMinutes);
//        numDrones = RequestNDrones.valueOf(numberOfDrones);
//
//        lenient().when(mockShodroneUser.systemUser()).thenReturn(mockSystemUser);
//        lenient().when(mockShowRequestRepository.save(any(ShowRequest.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//    }
//
//    @Test
//    void registerShowRequest_successful_whenUserIsCustomer() {
//
//        when(mockSystemUser.hasAny(ShodroneRoles.CRM_COLLABORATOR)).thenReturn(true);
//
//        ShowRequest req = controller.registerShowRequest(
//                description,
//                place,
//                cDate,
//                date,
//                duration,
//                numDrones,
//                mockShodroneUser
//        );
//
//        assertNotNull(req);
//        assertEquals(descriptionText, req.getDescription().toString());
//        assertEquals(placeText, req.getPlace().toString());
//        assertEquals(dateFormat.format(requestCreateDateTime.getTime()), req.getCreationDate().toString());
//        assertEquals(dateFormat.format(requestShowDateTime.getTime()), req.getShowDate().toString());
//        assertEquals(durationMinutes, req.getDuration().minutes());
//        assertEquals(numberOfDrones, req.getNdDrones().number());
//        assertEquals(mockShodroneUser, req.getCustomer());
//
//        verify(mockShowRequestRepository, times(1)).save(req);
//    }
//
//    @Test
//    void constructor_withNullRepository_throwsIllegalArgumentException() {
//        assertThrows(IllegalArgumentException.class, () -> new RegisterShowRequestController(null));
//    }
//
//    @Test
//    void constructor_withValidRepository_succeeds() {
//        assertNotNull(controller);
//    }
//
//    @Test
//    void isCustomer_whenUserHasCrmCollaboratorRole_returnsTrue() {
//        when(mockSystemUser.hasAny(ShodroneRoles.CRM_COLLABORATOR)).thenReturn(true);
//        assertTrue(controller.isCustomer(mockShodroneUser));
//    }
//
//    @Test
//    void isCustomer_whenUserDoesNotHaveCrmCollaboratorRole_returnsFalse() {
//        when(mockSystemUser.hasAny(ShodroneRoles.CRM_COLLABORATOR)).thenReturn(false);
//        assertFalse(controller.isCustomer(mockShodroneUser));
//    }
//
//    @Test
//    void isCustomer_whenSystemUserThrowsException_returnsFalse() {
//        when(mockShodroneUser.systemUser()).thenThrow(new RuntimeException("Simulated error accessing system user"));
//        assertFalse(controller.isCustomer(mockShodroneUser));
//    }
//
//    @Test
//    void isCustomer_withNullUser_returnsFalse() {
//        assertFalse(controller.isCustomer(null));
//    }
//
//    @Test
//    void registerShowRequest_withValidDataAndCustomerUser_savesAndReturnsShowRequest() {
//        when(mockSystemUser.hasAny(ShodroneRoles.CRM_COLLABORATOR)).thenReturn(true);
//
//        ShowRequest expectedSavedRequest = mock(ShowRequest.class);
//        when(mockShowRequestRepository.save(any(ShowRequest.class))).thenReturn(expectedSavedRequest);
//
//        ShowRequest result = controller.registerShowRequest(
//                Description.valueOf(descriptionText),
//                place,
//                cDate,
//                date,
//                duration,
//                numDrones,
//                mockShodroneUser
//        );
//
//        assertNotNull(result);
//        assertEquals(expectedSavedRequest, result);
//
//        ArgumentCaptor<ShowRequest> showRequestCaptor = ArgumentCaptor.forClass(ShowRequest.class);
//        verify(mockShowRequestRepository, times(1)).save(showRequestCaptor.capture());
//
//        ShowRequest capturedRequest = showRequestCaptor.getValue();
//        assertNotNull(capturedRequest);
//        assertEquals(descriptionText, capturedRequest.getDescription().toString());
//        assertEquals(placeText, capturedRequest.getPlace().toString());
//        assertEquals(dateFormat.format(requestCreateDateTime.getTime()), capturedRequest.getCreationDate().toString());
//        assertEquals(dateFormat.format(requestShowDateTime.getTime()), capturedRequest.getRequestShowDate().toString());
//        assertEquals(durationMinutes, capturedRequest.getDuration().minutes());
//        assertEquals(numberOfDrones, capturedRequest.getNdDrones().number());
//        assertEquals(mockShodroneUser, capturedRequest.getCustomer());
//    }
//
//    @Test
//    void registerShowRequest_withValidDataAndNonCustomerUser_returnsNullAndDoesNotSave() {
//        // Simulate a non-customer user by returning false for the role check
//        when(mockSystemUser.hasAny(ShodroneRoles.CRM_COLLABORATOR)).thenReturn(false);
//
//        ShowRequest result = controller.registerShowRequest(
//                description,
//                place,
//                cDate,
//                date,
//                duration,
//                numDrones,
//                mockShodroneUser
//        );
//
//        assertNull(result);
//        verify(mockShowRequestRepository, never()).save(any(ShowRequest.class));
//    }
//
//    @Test
//    void registerShowRequest_withNullDescription_throwsIllegalArgumentException() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                controller.registerShowRequest(
//                        null,
//                        place,
//                        cDate,
//                        date,
//                        duration,
//                        numDrones,
//                        mockShodroneUser
//                )
//        );
//        assertEquals("At least one of the required method parameters is null", exception.getMessage());
//    }
//
//    @Test
//    void registerShowRequest_withNullPlace_throwsIllegalArgumentException() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                controller.registerShowRequest(
//                        description,
//                        null,
//                        cDate,
//                        date,
//                        duration,
//                        numDrones,
//                        mockShodroneUser
//                )
//        );
//        assertEquals("At least one of the required method parameters is null", exception.getMessage());
//    }
//
//    @Test
//    void registerShowRequest_withNullCreationDate_throwsIllegalArgumentException() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                controller.registerShowRequest(
//                        description,
//                        place,
//                        null,
//                        date,
//                        duration,
//                        numDrones,
//                        mockShodroneUser
//                )
//        );
//        assertEquals("At least one of the required method parameters is null", exception.getMessage());
//    }
//
//    @Test
//    void registerShowRequest_withNullShowDate_throwsIllegalArgumentException() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                controller.registerShowRequest(
//                        description,
//                        place,
//                        cDate,
//                        null,
//                        duration,
//                        numDrones,
//                        mockShodroneUser
//                )
//        );
//        assertEquals("At least one of the required method parameters is null", exception.getMessage());
//    }
//
//    @Test
//    void registerShowRequest_withNullDuration_throwsIllegalArgumentException() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                controller.registerShowRequest(
//                        description,
//                        place,
//                        cDate,
//                        date,
//                        null,
//                        numDrones,
//                        mockShodroneUser
//                )
//        );
//        assertEquals("At least one of the required method parameters is null", exception.getMessage());
//    }
//
//    @Test
//    void registerShowRequest_withNullNumberOfDrones_throwsIllegalArgumentException() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                controller.registerShowRequest(
//                        description,
//                        place,
//                        cDate,
//                        date,
//                        duration,
//                        null,
//                        mockShodroneUser
//                )
//        );
//        assertEquals("At least one of the required method parameters is null", exception.getMessage());
//    }
//
//    @Test
//    void registerShowRequest_withNullCustomer_throwsIllegalArgumentException() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                controller.registerShowRequest(
//                        description,
//                        place,
//                        cDate,
//                        date,
//                        duration,
//                        numDrones,
//                        null
//                )
//        );
//        assertEquals("At least one of the required method parameters is null", exception.getMessage());
//    }
}
