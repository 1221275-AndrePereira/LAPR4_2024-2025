package utils;

import java.io.File;
import java.util.Arrays;

public class FileLoader {

    private final String directoryPath = "./inputfiles/drone_coordinates/";

    public FileLoader() {}

    /**
     * Automatically finds all drone files in the standard input directory and returns their names.
     * @return A semicolon-separated string of filenames.
     */
    private String autoFilesLoad() {
        StringBuilder fileSelection = new StringBuilder();
        // This relative path is used by the client to find the files.
        // It assumes the application is run from a directory where this path is valid.
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                Arrays.stream(files)
                        .filter(file -> file.isFile() && file.getName().startsWith("drone") && file.getName().endsWith(".txt"))
                        .forEach(file -> fileSelection.append(file.getName()).append(";")); // Append only the filename
            }
        }

        if (fileSelection.length() > 0) {
            fileSelection.deleteCharAt(fileSelection.length() - 1);
        }
        return fileSelection.toString();
    }

    /**
     * Loads a specified number of drone files from the directory.
     * @param numberOfFiles The number of files to load.
     * @return A semicolon-separated string of filenames.
     */
    public String loadFiles(int numberOfFiles) {
        StringBuilder fileSelection = new StringBuilder();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                Arrays.stream(files)
                        .filter(file -> file.isFile() && file.getName().startsWith("drone") && file.getName().endsWith(".txt"))
                        .limit(numberOfFiles)
                        .forEach(file -> fileSelection.append(file.getName()).append(";")); // Append only the filename
            }
        }

        if (fileSelection.length() > 0) {
            fileSelection.deleteCharAt(fileSelection.length() - 1);
        }
        return fileSelection.toString();
    }

}
