package eapli.shodrone.integrations.plugins.show.proposal;

import eapli.shodrone.showProposalDocument.application.DocumentValidator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileNotFoundException;
import java.io.File;

import java.util.Scanner;
import java.util.List;

public class ProposalDocumentValidator implements DocumentValidator {

    static final String DEFAULT_FILE = "./inputfiles/lprog/showProposalSamples/ExampleWithoutRepresentative.txt";

    @Override
    public boolean validate(String filePath) {

        File file = new File(filePath);

        Scanner sc;

        try {
            sc = new Scanner(file);
        }catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }

        StringBuilder fileContent = new StringBuilder();

        while(sc.hasNextLine()) {
            fileContent.append(sc.nextLine()).append("\n");
        }

        ShowProposalDocumentLexer lexer=new ShowProposalDocumentLexer(CharStreams.fromString(fileContent.toString()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ShowProposalDocumentParser parser = new ShowProposalDocumentParser(tokens);

        parser.removeErrorListeners();
        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.addErrorListener(errorListener);



        try {
            parser.start();
        } catch (RuntimeException e) {
            System.err.println("A runtime exception occurred during parsing: " + e.getMessage());
            return false;
        }


        List<String> errors = errorListener.getSyntaxErrors();
        if (errors.isEmpty()) {
            System.out.println("✔ Validation successful! The document syntax is correct according to the show proposal document grammar.");
            return true;
        } else {
            System.out.println("⨯ Validation failed. Found " + errors.size() + " syntax error(s):");
            errors.forEach(error -> System.out.println("- " + error));
            return false;
        }
    }

    public static void main(String[] args) {
        ProposalDocumentValidator validator = new ProposalDocumentValidator();
        boolean isValid = validator.validate(DEFAULT_FILE);
        System.out.println(isValid ? "Document is valid!" : "Document is invalid!");
    }
}
