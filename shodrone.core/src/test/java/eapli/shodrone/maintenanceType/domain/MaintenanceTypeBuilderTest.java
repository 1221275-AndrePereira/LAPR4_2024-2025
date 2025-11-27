package eapli.shodrone.maintenanceType.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MaintenanceTypeBuilderTest {

    @Test
    public void ensureBuildWithNameTest() {
        MaintenanceTypeBuilder builder = new MaintenanceTypeBuilder();
        builder.withName(new MaintenanceTypeName("Maintenance"));
        MaintenanceType maintenanceType = builder.build();

        assertEquals("Maintenance", maintenanceType.getMaintenanceTypeName().maintenanceTypeName());
    }


}