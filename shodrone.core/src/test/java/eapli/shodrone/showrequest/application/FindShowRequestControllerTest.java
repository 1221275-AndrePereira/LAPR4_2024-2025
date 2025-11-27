package eapli.shodrone.showrequest.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.general.domain.model.Description;

import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import eapli.shodrone.showrequest.domain.ShowRequest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;

class FindShowRequestControllerTest {

    @Mock
    private AuthorizationService mockAuthz;

    @Mock
    private ShowRequestRepository mockShowRequestRepository;

    @Mock
    private TransactionalContext mockTxCtx;

    @Mock
    private ShodroneUser mockShodroneUser;

    @Mock
    private ShowRequest mockShowRequest;

    private FindShowRequestController findShowRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize controller with mocks
        findShowRequestController = new FindShowRequestController(mockAuthz, mockShowRequestRepository, mockTxCtx);

        // Common mock behavior for successful transaction
        doNothing().when(mockTxCtx).beginTransaction(); // Correct way to mock a void method
        doNothing().when(mockTxCtx).commit();
        doNothing().when(mockTxCtx).rollback();
        when(mockTxCtx.isActive()).thenReturn(true);
    }


    @Test
    void findShowRequestById_Success_ReturnsShowRequest() {
        Long requestId = 1L;
        ShowRequest expectedShowRequest = new ShowRequest(/* ... constructor args ... */); // Create a dummy ShowRequest
        // You might need to use setters or a builder for ShowRequest if fields are not easily accessible
        // For example: expectedShowRequest.setId(requestId);

        // Mock repository and authz behavior
        when(mockShowRequestRepository.ofIdentity(requestId)).thenReturn(Optional.of(expectedShowRequest));

        // Execute
        ShowRequest actualShowRequest = findShowRequestController.findShowRequestById(requestId);

        // Verify
        assertNotNull(actualShowRequest);
        assertEquals(expectedShowRequest, actualShowRequest);
        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);
        verify(mockTxCtx).beginTransaction();
        verify(mockShowRequestRepository).ofIdentity(requestId);
        verify(mockTxCtx).rollback(); // In findShowRequestById, it rolls back on success too
        verify(mockTxCtx, never()).commit(); // Commit is not called in the happy path for findById
    }


    @Test
    void findShowRequestById_NotFound_ReturnsNull() {
        Long requestId = 1L;
        when(mockShowRequestRepository.ofIdentity(requestId)).thenReturn(Optional.empty());

        ShowRequest result = findShowRequestController.findShowRequestById(requestId);

        assertNull(result);
        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);
        verify(mockTxCtx).beginTransaction();
        verify(mockShowRequestRepository).ofIdentity(requestId);
        verify(mockTxCtx).rollback();
    }


    @Test
    void findShowRequestById_RepositoryThrowsException_RethrowsRuntimeException() {
        Long requestId = 1L;
        when(mockShowRequestRepository.ofIdentity(requestId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            findShowRequestController.findShowRequestById(requestId);
        });

        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);
        verify(mockTxCtx).beginTransaction();
        verify(mockShowRequestRepository).ofIdentity(requestId);
        verify(mockTxCtx).rollback(); // Ensure rollback is called if transaction was active
    }


    @Test
    void findShowRequestByDescription_Success_ReturnsShowRequest() {
        Description description = Description.valueOf("Test Show");
        ShowRequest expectedShowRequest = new ShowRequest(/* ... */);
        // expectedShowRequest.setDescription(description);

        when(mockShowRequestRepository.findByDescription(description)).thenReturn(Optional.of(expectedShowRequest));

        ShowRequest result = findShowRequestController.findShowRequestByDescription(description);

        assertNotNull(result);
        assertEquals(expectedShowRequest, result);
        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);
        verify(mockTxCtx).beginTransaction();
        verify(mockShowRequestRepository).findByDescription(description);
        verify(mockTxCtx).commit(); // Commit is called on success for findByDescription
    }

    @Test
    void findShowRequestByDescription_NotFound_ReturnsNull() {
        Description description = Description.valueOf("NonExistent Show");
        when(mockShowRequestRepository.findByDescription(description)).thenReturn(Optional.empty());

        ShowRequest result = findShowRequestController.findShowRequestByDescription(description);

        assertNull(result);
        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);
        verify(mockTxCtx).beginTransaction();
        verify(mockShowRequestRepository).findByDescription(description);
        verify(mockTxCtx).rollback(); // Rollback is called if not found
    }


    @Test
    void listShowRequests_Success_ReturnsListOfShowRequests() {
        List<ShowRequest> expectedList = new ArrayList<>();
        expectedList.add(new ShowRequest(/* ... */));
        expectedList.add(new ShowRequest(/* ... */));

        when(mockShowRequestRepository.obtainAllShowRequests()).thenReturn(expectedList);

        List<ShowRequest> actualList = findShowRequestController.listShowRequests();

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);
        verify(mockTxCtx).beginTransaction();
        verify(mockShowRequestRepository).obtainAllShowRequests();
        verify(mockTxCtx).commit();
    }


    @Test
    void listShowRequests_EmptyList_ReturnsEmptyList() {
        when(mockShowRequestRepository.obtainAllShowRequests()).thenReturn(new ArrayList<>());

        List<ShowRequest> result = findShowRequestController.listShowRequests();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);
        verify(mockTxCtx).beginTransaction();
        verify(mockShowRequestRepository).obtainAllShowRequests();
        verify(mockTxCtx).commit();
    }


    @Test
    void findShowRequestsByCustomer_Success_ReturnsListOfShowRequests() {
        ShodroneUser customer = mockShodroneUser;
        List<ShowRequest> expectedList = new ArrayList<>();
        expectedList.add(mockShowRequest);

        when(mockShowRequestRepository.findByCustomer(customer)).thenReturn(expectedList);

        List<ShowRequest> actualList = findShowRequestController.findShowRequestsByCustomer(customer);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_COLLABORATOR);
        verify(mockTxCtx).beginTransaction();
        verify(mockShowRequestRepository).findByCustomer(customer);
        verify(mockTxCtx).commit();
    }


    @Test
    void findShowRequestById_NullTxCtx_Success() {
        // Re-initialize controller with null txCtx
        findShowRequestController = new FindShowRequestController(mockAuthz, mockShowRequestRepository, null);

        Long requestId = 1L;
        ShowRequest expectedShowRequest = new ShowRequest(/* ... */);
        when(mockShowRequestRepository.ofIdentity(requestId)).thenReturn(Optional.of(expectedShowRequest));

        ShowRequest actualShowRequest = findShowRequestController.findShowRequestById(requestId);

        assertNotNull(actualShowRequest);
        assertEquals(expectedShowRequest, actualShowRequest);
        verify(mockAuthz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN);
        verify(mockShowRequestRepository).ofIdentity(requestId);
        verify(mockTxCtx, never()).beginTransaction(); // Ensure no transaction methods called on mockTxCtx
        verify(mockTxCtx, never()).commit();
        verify(mockTxCtx, never()).rollback();
    }
}