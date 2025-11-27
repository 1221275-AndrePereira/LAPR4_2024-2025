import console.BaseApplication;
import console.presentation.authz.LoginUI;

import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.pubsub.EventDispatcher;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodronePasswordPolicy;

import shodrone.app.backoffice.console.MainMenu;
import shodrone.infrastructure.authz.AuthenticationCredentialHandler;

/**
 * The ShodroneBackOffice class is a concrete implementation of the BaseApplication class
 * that serves as the main entry point for a back-office application. It configures
 * authorization, handles the application lifecycle, and manages high-level user interface
 * navigation including login and the main menu.
 */
public class ShodroneBackOffice extends BaseApplication {

    /**
     * avoid instantiation of this class.
     */
    private ShodroneBackOffice() {
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(final String[] args) {

        new ShodroneBackOffice().run(args);
    }

    /**
     * Executes the main flow of the application by handling the login process and directing
     * the user to the main menu upon successful authentication.
     *
     * @param args a set of command-line arguments provided during the application start.
     */
    @Override
    protected void doMain(String[] args) {
        // login and go to main menu
        final boolean authenticated = new LoginUI(new AuthenticationCredentialHandler()).show();
        if (authenticated) {
            // go to main menu
            final var menu = new MainMenu();
            menu.mainLoop();
        }
    }

    @Override
    protected String appTitle() {
        return "Shodrone BackOffice";
    }

    @Override
    protected String appGoodbye() {
        return "Shodrone BackOffice";
    }

    @Override
    protected void configureAuthz() {
        AuthzRegistry.configure(PersistenceContext.repositories().users(), new ShodronePasswordPolicy(),
                new PlainTextEncoder());
    }

    @Override
    protected void doSetupEventHandlers(EventDispatcher dispatcher) {
        //dispatcher.subscribe(new NewUserRegisteredFromSignupWatchDog(), NewUserRegisteredFromSignupEvent.class);
        //dispatcher.subscribe(new SignupShodroneUser(), SignupEvent.class);
    }
}
