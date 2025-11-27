package utils;

import eapli.framework.io.util.Console;
import shodrone.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class FileSelector {

    private final Path dslBaseDirectory;
    private final Set<String> allowedExtensions = Set.of(".txt");


    public FileSelector() {
        String basePathString = Application.settings().dslScriptsPath();
        this.dslBaseDirectory = Paths.get(basePathString);
        initializeDirectory();
    }

    private void initializeDirectory() {
        if (!Files.exists(dslBaseDirectory)) {
            System.out.println("Warning: DSL base directory '" + dslBaseDirectory + "' does not exist.");

            try {
                Files.createDirectories(dslBaseDirectory);
                System.out.println("Created DSL directory: " + dslBaseDirectory);
            } catch (IOException e) {
                System.err.println("Error creating DSL directory: " + dslBaseDirectory + " - " + e.getMessage());
            }
        }
    }

    private boolean hasAllowedExtension(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        for (String ext : allowedExtensions) {
            if (lowerCaseFileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public String collectDslFilePathsByFilename() {
        String selectedFilePath = null;

        while(true) {

            if (!Files.isDirectory(dslBaseDirectory) || !Files.isReadable(dslBaseDirectory)) {
                System.out.println("Cannot access or list files from the base DSL directory: " + dslBaseDirectory);
                continue;
            }

            String fileNameInput = Console.readLine("Enter DSL filename (e.g., 'my_figure.txt'):").trim();

            if (fileNameInput.isEmpty()) {
                System.out.println("Filename cannot be empty. Please try again.");
                continue;
            }

            File file = new File(fileNameInput);
            String resolvedPathString;
            if (file.isAbsolute() || file.getParent() != null) {
                resolvedPathString = file.getAbsolutePath();
            } else {
                resolvedPathString = dslBaseDirectory.resolve(fileNameInput).toString();
            }

            File selectedFile = new File(resolvedPathString);

            if (selectedFile.exists() && selectedFile.isFile()) {
                if (hasAllowedExtension(selectedFile.getName())) {
                    selectedFilePath = selectedFile.getAbsolutePath();
                    System.out.println("Added: " + selectedFile.getAbsolutePath());

                } else {
                    System.out.println("Error: File not found or is not valid: " + selectedFile.getAbsolutePath());
                    continue;
                }

            }
            break;
        }

        return selectedFilePath;
    }

    public String getFullDSLPath(String fileNameInput) {
        File file = new File(fileNameInput);
        String resolvedPathString;
        if (file.isAbsolute() || file.getParent() != null) {
            resolvedPathString = file.getPath();
        } else {
            resolvedPathString = dslBaseDirectory.resolve(fileNameInput).toString();
        }

        File selectedFile = new File(resolvedPathString);
        if (selectedFile.exists() && selectedFile.isFile()) {
            if (hasAllowedExtension(selectedFile.getName())) {
                return selectedFile.getPath();
            }
        }

        return null;
    }
}