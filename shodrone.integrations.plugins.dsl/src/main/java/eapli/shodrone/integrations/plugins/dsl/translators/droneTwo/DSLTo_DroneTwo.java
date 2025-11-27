package eapli.shodrone.integrations.plugins.dsl.translators.droneTwo;

import org.antlr.v4.runtime.tree.ParseTree;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class DSLTo_DroneTwo {


    public static String traslate(ParseTree tree, String outputPath) {

        DroneTwoCodeGeneratingVisitor DroneTwoVisitor = new DroneTwoCodeGeneratingVisitor();

        List<String> generatedDroneTwoCode = DroneTwoVisitor.visit(tree);

        File generatedFile = new File(outputPath);

        String accumulatedCommands = "";

        try {
            FileWriter writer = new FileWriter(generatedFile);

            if (generatedDroneTwoCode != null) {
                for (String command : generatedDroneTwoCode) {
                    accumulatedCommands = accumulatedCommands + command + "\n";
                }
            } else {
                System.out.println("No code generated or an error occurred.");
            }
            writer.write(accumulatedCommands);
            writer.close();
        }catch (IOException e) {
            System.out.println("Error creating File writer" + e.getMessage());
        }

        System.out.println("Successfully generated: " +  generatedFile.getName());
        return accumulatedCommands;
    }
}
