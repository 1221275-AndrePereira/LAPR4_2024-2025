import console.BaseApplication;

import eapli.framework.infrastructure.pubsub.EventDispatcher;

import eapli.framework.io.util.Console;
import eapli.shodrone.app.customer.console.presentation.DaemonLoginUI;
import eapli.shodrone.app.customer.console.DaemonClient;
import eapli.shodrone.app.customer.console.MainMenu;

/**
 * The ShodroneCustomerApp class is the main entry point for the Shodrone Customer Application.
 * It extends the BaseApplication class to provide application-specific behavior, such as
 * connecting to the ShodroneDaemon, handling user login, and initializing the main menu.
 * <p>
 * This is a final class and cannot be extended further.
 *
 * @author Daniil Pogorielov
 */
public final class ShodroneCustomerApp extends BaseApplication {

    private ShodroneCustomerApp() {}

    public static void main(final String[] args) {
        new ShodroneCustomerApp().run(args);
    }

    protected void doMain(String[] args) {
        DaemonClient client;
        boolean localHost = Console.readBoolean("Do you want to use local host? (y/n)");

        if (localHost) {
            client = new DaemonClient();
            if (!client.connect()) {
                System.out.println("Could not connect to the Shodrone server. " +
                        "Please, make sure the daemon is running!");
                return;
            }
        }else {
            String ip = Console.readNonEmptyLine("Enter IP address: ", "Example: 127.0.0.1");
            int port = Console.readInteger("Enter port: ");
            client = new DaemonClient(ip, port);
            if (!client.connect()) {
                System.out.println("Could not connect to the Shodrone server. " +
                        "Please, make sure the ip and post is correct!");
                return;
            }
        }

        final DaemonLoginUI loginUI = new DaemonLoginUI(client);
        final boolean isLoggedIn = loginUI.show();
        if (isLoggedIn) {
            final var menu = new MainMenu(DaemonLoginUI.getLoggedInUserVat(), client,DaemonLoginUI.isCustomer());
            menu.mainLoop();
        }

        client.disconnect();
    }

    @Override
    protected String appTitle() {
        return "ShoDrone Customer App";
    }

    @Override
    protected String appGoodbye() {
        return "Thank you for using Shodrone!";
    }

    @Override
    protected void configureAuthz() {
        // Not needed for the client app
    }

    @Override
    protected void doSetupEventHandlers(EventDispatcher dispatcher) {
        // Not needed for the client app
    }
}
