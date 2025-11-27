package eapli.shodrone.integrations.plugins.dsl.translators;

import eapli.shodrone.integrations.plugins.dsl.FigureDSLLexer;
import eapli.shodrone.integrations.plugins.dsl.FigureDSLParser;
import eapli.shodrone.integrations.plugins.dsl.translators.droneOne.DSLTo_DroneOne;
import eapli.shodrone.integrations.plugins.dsl.translators.droneTwo.DSLTo_DroneTwo;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DSLTranslator {

    public static void translate(String inputPath, String outputPath, String languageId) {

        File file = new File(inputPath);
        Scanner sc;

        try {
            sc = new Scanner(file);
        }catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        StringBuilder fileContent = new StringBuilder();

        while(sc.hasNextLine()) {
            fileContent.append(sc.nextLine()).append("\n");
        }

        FigureDSLLexer lexer = new FigureDSLLexer(CharStreams.fromString(fileContent.toString()));


        CommonTokenStream tokens = new CommonTokenStream(lexer);


        FigureDSLParser parser = new FigureDSLParser(tokens);


        ParseTree tree = parser.program();


        if (languageId.equals("DroneOne")){
            DSLTo_DroneOne.traslate(tree, outputPath);
        }
        if(languageId.equals("DroneTwo")){
            DSLTo_DroneTwo.traslate(tree, outputPath);
        }

    }

}
