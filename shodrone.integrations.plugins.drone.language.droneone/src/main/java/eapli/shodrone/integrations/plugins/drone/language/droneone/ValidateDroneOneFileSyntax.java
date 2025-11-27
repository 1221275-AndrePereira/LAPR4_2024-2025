package eapli.shodrone.integrations.plugins.drone.language.droneone;

//import eapli.shodrone.integrations.plugins.drone.language.droneone.DroneOne_LanguageLexer;
//import eapli.shodrone.integrations.plugins.drone.language.droneone.DroneOne_LanguageParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ValidateDroneOneFileSyntax {

    public static boolean validate(String filePath) {
        File file = new File(filePath);

        Scanner sc;
        try {
            sc = new Scanner(file);
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            return false;
        }

        StringBuilder fileContent = new StringBuilder();

        while(sc.hasNextLine()) {
            fileContent.append(sc.nextLine()).append("\n");
        }

        DroneOne_LanguageLexer lexer = new DroneOne_LanguageLexer(CharStreams.fromString(fileContent.toString()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DroneOne_LanguageParser parser = new DroneOne_LanguageParser(tokens);

        parser.removeErrorListeners();
        SimpleSyntaxErrorFlagListener errorListener = new SimpleSyntaxErrorFlagListener();
        parser.addErrorListener(errorListener);

        try {
            parser.file();
        } catch (RuntimeException e) {
             System.err.println("A runtime exception occurred during parsing: " + e.getMessage());
            return false;
        }

        if (errorListener.hasErrorOccurred()) {
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("⨯ The file "+file.getName() +" is NOT VALID according to the DroneOne language format. \nErrors:");
            errorListener.getErrors().forEach(error -> System.out.println("- " + error));
            System.out.println("-------------------------------------------------------------------------------------");
            return false;
        } else {
            System.out.println("✔ The file "+file.getName() +" is VALID according to the DroneOne language format.");
            System.out.println("-------------------------------------------------------------------------------------");
            return true;
        }
    }

}
