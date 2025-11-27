package eapli.shodrone.app.customer.console.presentation;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.csv.util.CsvLineMarshaler;
import eapli.framework.io.util.Console;

import eapli.shodrone.app.customer.console.DaemonClient;

import lombok.Getter;

/**
 * The DaemonLoginUI class provides a user interface for logging into the Shodrone daemon.
 * It extends the AbstractUI class and implements the doShow method to handle user input
 * for login credentials, validate them against the daemon, and manage login attempts.
 * <p>
 * This class is responsible for capturing the logged-in user's VAT (Value Added Tax) number
 * and providing feedback on the success or failure of the login attempt.
 *
 * @author Daniil Pogorielov
 */
public class DaemonLoginUI extends AbstractUI {

    @Getter
    private static String loggedInUserVat;
    @Getter
    private static boolean isCustomer;
    private final DaemonClient client;

    public DaemonLoginUI(DaemonClient client) {
        this.client = client;
    }

    @Override
    protected boolean doShow() {
        final int maxAttempts = 3;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            System.out.printf("Attempt %d of %d%n", attempts + 1, maxAttempts);
            final String username = Console.readLine("Username:");
            final String password = Console.readLine("Password:");

            try {
                String request = String.format("LOGIN,\"%s\",\"%s\"", username, password);
                String response = client.sendRequest(request);

                if (response.startsWith("LOGGED_IN")) {
                    String[] tokens = CsvLineMarshaler.tokenize(response).toArray(new String[0]);
                    loggedInUserVat = CsvLineMarshaler.unquote(tokens[1]);
                    isCustomer = Boolean.parseBoolean(CsvLineMarshaler.unquote(tokens[2]));

                    System.out.println("\nLogin Successful! Welcome, " + username + "!");
                    return true;
                } else {
                    System.out.println("\nLogin Failed: " + response);
                    attempts++;
                }
            } catch (Exception e) {
                System.out.println("Error during login: " + e.getMessage());
                attempts++;
            }

            if (attempts < maxAttempts) {
                System.out.println("Please try again.");
            } else {
                System.out.println("Maximum login attempts reached. Access denied.");
            }
        }
        return false;
    }

    @Override
    public String headline() {
        return "Customer Login";
    }
}