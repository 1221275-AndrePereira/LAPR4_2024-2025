package eapli.shodrone.maintenanceType.domain;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.shodrone.maintenanceType.application.EditMaintenanceTypeController;
import eapli.shodrone.maintenanceType.domain.MaintenanceType;
import eapli.shodrone.maintenanceType.domain.MaintenanceTypeName;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditMaintenanceTypeControllerTest {

    @Test
    void testEditMaintenanceType() {
        AuthorizationService authz = mock(AuthorizationService.class);
        MaintenanceTypeRepository repo = mock(MaintenanceTypeRepository.class);

        MaintenanceType mt = mock(MaintenanceType.class);
        when(repo.ofIdentity(any())).thenReturn(java.util.Optional.of(mt));
        when(repo.save(mt)).thenReturn(mt);

        EditMaintenanceTypeController ctrl = new EditMaintenanceTypeController(authz, repo);
        MaintenanceType result = ctrl.editMaintenanceType(1L, new MaintenanceTypeName("Corretiva"));

        verify(authz).ensureAuthenticatedUserHasAnyOf(any(), any(), any());
        verify(mt).changeName(any());
        verify(repo).save(mt);
        assertEquals(mt, result);
    }
}