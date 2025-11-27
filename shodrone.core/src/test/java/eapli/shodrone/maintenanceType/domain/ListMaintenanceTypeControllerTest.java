package eapli.shodrone.maintenanceType.domain;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.shodrone.maintenanceType.application.ListMaintenanceTypeController;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListMaintenanceTypeControllerTest {

    @Test
    void testListAllMaintenanceTypes() {
        AuthorizationService authz = mock(AuthorizationService.class);
        MaintenanceTypeRepository repo = mock(MaintenanceTypeRepository.class);
        MaintenanceType mt1 = mock(MaintenanceType.class);
        MaintenanceType mt2 = mock(MaintenanceType.class);

        when(repo.findAll()).thenReturn(Arrays.asList(mt1, mt2));

        ListMaintenanceTypeController ctrl = new ListMaintenanceTypeController(authz, repo);
        Iterable<MaintenanceType> result = ctrl.listAllMaintenanceTypes();

        verify(authz).ensureAuthenticatedUserHasAnyOf(any(), any(), any());
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }
}