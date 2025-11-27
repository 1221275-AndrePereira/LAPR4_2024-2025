package eapli.shodrone.integrations.plugins.dsl.translators.droneOne;

import org.antlr.v4.runtime.tree.ParseTree;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DSLTo_DroneOne {


    public static void traslate(ParseTree tree, String outputPath) {

        DroneOneCodeGeneratingVisitor droneOneVisitor = new DroneOneCodeGeneratingVisitor();

        List<String> generatedDroneOneCode = droneOneVisitor.visit(tree);

        File generatedFile = new File(outputPath);

        File parentDir = generatedFile.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                System.out.println("Created output directory: " + parentDir.getPath());
            } else {
                System.err.println("Failed to create output directory: " + parentDir.getPath());
                return;
            }
        }

        String accumulatedCommands = "";

        try {
            FileWriter writer = new FileWriter(generatedFile);

            if (generatedDroneOneCode != null) {
                for (String command : generatedDroneOneCode) {
                    accumulatedCommands = accumulatedCommands + command + "\n";
                }
            } else {
                System.out.println("No code generated or an error occurred.");
            }
            writer.write(accumulatedCommands);
            writer.close();
        }catch (IOException e) {
            System.out.println("Error creating File writer " + e.getMessage());
        }


        System.out.println("Successfully generated: " +  generatedFile.getName());


    }
}
