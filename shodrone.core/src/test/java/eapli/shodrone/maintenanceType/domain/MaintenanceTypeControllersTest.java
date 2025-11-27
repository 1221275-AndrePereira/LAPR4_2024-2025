package eapli.shodrone.maintenanceType.domain;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.shodrone.maintenanceType.domain.MaintenanceType;
import eapli.shodrone.maintenanceType.domain.MaintenanceTypeName;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import eapli.shodrone.maintenanceType.application.AddMaintenanceTypeController;
import eapli.shodrone.maintenanceType.application.EditMaintenanceTypeController;
import eapli.shodrone.maintenanceType.application.ListMaintenanceTypeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaintenanceTypeControllersTest {

    private AuthorizationService authz;
    private MaintenanceTypeRepository repo;

    @BeforeEach
    void setUp() {
        authz = mock(AuthorizationService.class);
        repo = mock(MaintenanceTypeRepository.class);
    }

    @Test
    void testAddMaintenanceType() {
        AddMaintenanceTypeController controller = new AddMaintenanceTypeController(authz, repo);
        MaintenanceTypeName name = new MaintenanceTypeName("TestType");
        MaintenanceType type = mock(MaintenanceType.class);

        when(repo.save(any())).thenReturn(type);

        MaintenanceType result = controller.addMaintenanceType(name);

        assertNotNull(result);
        verify(authz, times(2)).ensureAuthenticatedUserHasAnyOf(any());
        verify(repo).save(any());
    }

    @Test
    void testEditMaintenanceType_Success() {
        EditMaintenanceTypeController controller = new EditMaintenanceTypeController(authz, repo);
        MaintenanceTypeName newName = new MaintenanceTypeName("NewName");
        MaintenanceType type = mock(MaintenanceType.class);

        when(repo.ofIdentity(1L)).thenReturn(Optional.of(type));
        when(type.hasMaintenanceRecords()).thenReturn(false);
        when(repo.save(type)).thenReturn(type);

        MaintenanceType result = controller.editMaintenanceType(1L, newName);

        assertNotNull(result);
        verify(type).changeName(newName);
        verify(repo).save(type);
    }

    @Test
    void testEditMaintenanceType_NotFound() {
        EditMaintenanceTypeController controller = new EditMaintenanceTypeController(authz, repo);
        when(repo.ofIdentity(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> controller.editMaintenanceType(1L, new MaintenanceTypeName("X")));
    }

    @Test
    void testEditMaintenanceType_WithRecords() {
        EditMaintenanceTypeController controller = new EditMaintenanceTypeController(authz, repo);
        MaintenanceType type = mock(MaintenanceType.class);

        when(repo.ofIdentity(1L)).thenReturn(Optional.of(type));
        when(type.hasMaintenanceRecords()).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> controller.editMaintenanceType(1L, new MaintenanceTypeName("X")));
    }

    @Test
    void testListMaintenanceType() {
        ListMaintenanceTypeController controller = new ListMaintenanceTypeController(authz, repo);
        MaintenanceType type1 = mock(MaintenanceType.class);
        MaintenanceType type2 = mock(MaintenanceType.class);

        when(repo.findAll()).thenReturn(Arrays.asList(type1, type2));

        List<MaintenanceType> result = controller.listMaintenanceTypes();

        assertEquals(2, result.size());
        verify(authz, times(1)).ensureAuthenticatedUserHasAnyOf(any());
        verify(repo).findAll();
    }
}