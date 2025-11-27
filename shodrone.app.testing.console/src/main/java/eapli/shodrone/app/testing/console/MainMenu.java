package eapli.shodrone.app.testing.console;

import eapli.framework.actions.menu.Menu;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.menu.HorizontalMenuRenderer;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import eapli.shodrone.app.testing.console.presentation.TestShowProposalUI;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import shodrone.Application;

public class MainMenu extends AbstractUI {

    private static final String SEPARATOR_LABEL = "--------------";
    private static final String RETURN_LABEL = "Return ";
    private static final int EXIT_OPTION = 0;

    private final TestingClient client;
    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    /**
     * Constructor for MainMenu.
     * Initializes the UI with a TestingClient instance.
     *
     * @param client The TestingClient used to communicate with the server.
     */
    public MainMenu(TestingClient client) {
        this.client = client;
    }

    /**
     * Draws the title of the form.
     */
    @Override
    public boolean show() {
        drawFormTitle();
        return doShow();
    }

    /**
     * Draws the title of the form.
     */
    @Override
    public boolean doShow() {
        final Menu menu = buildMainMenu();
        final MenuRenderer renderer;
        if (Application.settings().isMenuLayoutHorizontal()) {
            renderer = new HorizontalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        } else {
            renderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        }
        return renderer.render();
    }

    /**
     * Builds the main menu with options for the user.
     *
     * @return The constructed Menu object.
     */
    private Menu buildMainMenu() {
        final Menu mainMenu = new Menu("Main Menu");

        int currentOption = 1;
        TestShowProposalUI testShowProposalUI = new TestShowProposalUI(client);

        if(authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.ADMIN,ShodroneRoles.DRONE_TECH)) {
            mainMenu.addItem(currentOption++, "Test Show Proposal", testShowProposalUI::show);
        }

        mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("User session terminated"));

        return mainMenu;
    }

    /**
     * Draws the title of the form.
     */
    @Override
    public String headline() {
        return "Testing App Console Main Menu";
    }
}

