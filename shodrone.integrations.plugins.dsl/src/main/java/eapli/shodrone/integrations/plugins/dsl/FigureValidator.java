package eapli.shodrone.integrations.plugins.dsl;

import org.antlr.v4.runtime.*;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

/**
 * The FigureValidator class is used to validate a DSL (Domain-Specific Language) file against a predefined grammar.
 * It processes the content of the file, checks for syntax errors, and provides feedback on any validation issues.
 * <p>
 * This class uses ANTLR-generated lexer and parser for DSL grammar processing.
 *
 * @author Daniil Pogorielov
 */
public class FigureValidator {

    static final String DEFAULT_FILE = "./inputfiles/lprog/dslFigureSamples/sample_DSL_figure_1.txt";

    public static boolean validator(String filePath) throws IOException {
        System.out.println("Attempting to validate file: " + filePath + "\n");

        String dslContent = Files.readString(Path.of(filePath));

        CharStream charStream = CharStreams.fromString(dslContent);

        FigureDSLLexer lexer = new FigureDSLLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FigureDSLParser parser = new FigureDSLParser(tokens);

        parser.removeErrorListeners();
        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.addErrorListener(errorListener);

        parser.program();

        List<String> errors = errorListener.getSyntaxErrors();
        if (errors.isEmpty()) {
            System.out.println("✔ Validation successful! The script's syntax is correct according to DSL grammar.");
            return true;
        } else {
            System.out.println("⨯ Validation failed. Found " + errors.size() + " syntax error(s):");
            errors.forEach(error -> System.out.println("- " + error));
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        validator(DEFAULT_FILE);
    }
}
