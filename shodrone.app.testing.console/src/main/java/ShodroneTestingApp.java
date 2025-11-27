import console.BaseApplication;

import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.pubsub.EventDispatcher;

import eapli.framework.io.util.Console;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodronePasswordPolicy;
import eapli.shodrone.app.testing.console.TestingClient;
import eapli.shodrone.app.testing.console.MainMenu;

import shodrone.infrastructure.authz.AuthenticationCredentialHandler;

import console.presentation.authz.LoginUI;

public class ShodroneTestingApp extends BaseApplication {

    /**
     * Private constructor to prevent instantiation.
     * Use the main method to run the application.
     */
    private ShodroneTestingApp() {}

    /**
     * Main method to run the Shodrone Testing Application.
     * It initializes the application and starts the main loop.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(final String[] args) {
        new ShodroneTestingApp().run(args);
    }

    /**
     * Returns the singleton instance of the ShodroneTestingApp.
     *
     * @return The singleton instance of ShodroneTestingApp.
     */
    @Override
    protected void doMain(String[] args) {
        final boolean authenticated = new LoginUI(new AuthenticationCredentialHandler()).show();
        TestingClient client;
        if (authenticated) {
            boolean localHost = Console.readBoolean("Do you want to use local host? (y/n)");
            if (localHost) {
                client = new TestingClient();
            }
            else {
                String ip = Console.readNonEmptyLine("Enter IP address: ", "Example: 127.0.0.1");
                int port = Console.readInteger("Enter port: ");
                client = new TestingClient(ip, port);
            }
            if (!client.connect()) {
                System.out.println("Could not connect to the Shodrone Simulation server. Please, make sure the server is running!");
                return;
            }
            final var menu = new MainMenu(client);
            menu.mainLoop();
        }
    }

    /**
     * Returns the title of the application.
     *
     * @return The title of the Shodrone Testing Application.
     */
    @Override
    protected String appTitle() {
        return "ShoDrone Testing App";
    }

    /**
     * Returns the welcome message for the application.
     *
     * @return The welcome message for the Shodrone Testing Application.
     */
    @Override
    protected String appGoodbye() {
        return "Thank you for using Shodrone Testing App!";
    }

    /**
     * Configures the authentication and authorization for the application.
     * This method sets up the user repository and password policy.
     */
    @Override
    protected void configureAuthz() {
        AuthzRegistry.configure(PersistenceContext.repositories().users(), new ShodronePasswordPolicy(),
                new PlainTextEncoder());
    }

    /**
     * Sets up the event handlers for the application.
     * This method is currently not needed for the testing app.
     *
     * @param dispatcher The event dispatcher to register event handlers.
     */
    @Override
    protected void doSetupEventHandlers(EventDispatcher dispatcher) {
        // Not needed for the testing app
    }
}
