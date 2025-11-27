// shodrone.core/src/test/java/eapli/shodrone/maintenanceType/domain/MaintenanceTypeTest.java
package eapli.shodrone.maintenanceType.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaintenanceTypeTest {

    @Test
    void testConstructorAndGetters() {
        MaintenanceTypeName name = new MaintenanceTypeName("Preventiva");
        MaintenanceType type = new MaintenanceType(name);

        assertEquals(name, type.getMaintenanceTypeName());
        assertNull(type.identity()); // ainda não persistido, id deve ser null
    }

    @Test
    void testChangeName() {
        MaintenanceTypeName name = new MaintenanceTypeName("Corretiva");
        MaintenanceType type = new MaintenanceType(name);

        MaintenanceTypeName newName = new MaintenanceTypeName("Inspeção");
        type.changeName(newName);

        assertEquals(newName, type.getMaintenanceTypeName());
    }

    @Test
    void testSameAs() {
        MaintenanceTypeName name = new MaintenanceTypeName("Teste");
        MaintenanceType type1 = new MaintenanceType(name);
        MaintenanceType type2 = new MaintenanceType(name);

        assertTrue(type1.sameAs(type2));
        assertFalse(type1.sameAs(null));
        assertFalse(type1.sameAs("string"));
    }

    @Test
    void testHasMaintenanceRecordsAlwaysFalse() {
        MaintenanceType type = new MaintenanceType(new MaintenanceTypeName("Qualquer"));
        assertFalse(type.hasMaintenanceRecords());
    }
}