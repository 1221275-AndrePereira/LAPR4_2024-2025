package eapli.shodrone.app.testing.console.presentation;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.app.testing.console.TestingClient;
import java.io.File;
import java.util.Arrays;

public class RunSimulationUI extends AbstractUI {

    private final TestingClient client;

    private final String directoryPath = "./inputfiles/drone_coordinates/";

    /**
     * Constructor for RunSimulationUI.
     * Initializes the UI with a TestingClient instance.
     * @param client The TestingClient used to communicate with the server.
     */
    public RunSimulationUI(TestingClient client) {
        this.client = client;
    }

    /**
     * Allows the user to manually enter the names of files for simulation.
     * @return A semicolon-separated string of filenames.
     */
    private String fileSelection() {
        StringBuilder fileSelection = new StringBuilder();
        System.out.println("Enter the drone data filenames to simulate one by one (e.g., drone1.txt).");
        while (true) {
            String fileName = Console.readLine("Enter filename (or press Enter to finish): ");
            if (fileName.isBlank()) {
                break;
            }
            fileSelection.append(fileName).append(";");
        }

        if (fileSelection.length() > 0) {
            fileSelection.deleteCharAt(fileSelection.length() - 1);
        }
        return fileSelection.toString();
    }

    /**
     * Automatically finds all drone files in the standard input directory and returns their names.
     * @return A semicolon-separated string of filenames.
     */
    private String autoFilesLoad() {
        StringBuilder fileSelection = new StringBuilder();
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
     * Displays the UI for running a simulation.
     * It prompts the user to either automatically load drone files or manually enter them.
     * After gathering the file names, it sends a request to the server to run the simulation.
     * @return false to indicate that the UI should not be closed.
     */
    @Override
    public boolean doShow() {
        String requestPayload;
        boolean loadDrones = Console.readBoolean("Do you want to automatically load all drone files from the input directory? (Y/N): ");
        if (loadDrones) {
            requestPayload = autoFilesLoad();
            if (requestPayload.isEmpty()) {
                System.out.println("Could not find any drone files automatically. Please check the execution path.");
                Console.readLine("\nPress Enter to continue...");
                return false;
            }
            System.out.printf("Found the following files to simulate:\n%s\n", requestPayload.replace(";", "\n"));
        } else {
            requestPayload = fileSelection();
        }

        if (requestPayload.isEmpty()) {
            System.out.println("No files were selected for simulation.");
            Console.readLine("\nPress Enter to continue...");
            return false;
        }

        try {
            String request = String.format("SIMULATE;%s", requestPayload);
            String response = client.sendRequest(request);
            System.out.println("\n--- Simulation Server Response ---");
            System.out.println(response);
            System.out.println("--------------------------");
        } catch (Exception e) {
            System.out.println("Error: Unable to run simulation. Details: " + e.getMessage());
        }
        Console.readLine("\nPress Enter to continue...");
        return false;
    }

    /**
     * Returns the title of the UI.
     * @return The title of the UI.
     */
    @Override
    public String headline() {
        return "Run Simulation";
    }
}