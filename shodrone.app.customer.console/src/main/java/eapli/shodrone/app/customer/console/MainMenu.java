package eapli.shodrone.app.customer.console;

import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.actions.menu.Menu;

import eapli.shodrone.app.customer.console.presentation.ManageProposalsUI;
import eapli.shodrone.app.customer.console.presentation.ListShowsUI;

public class MainMenu extends AbstractUI {

    private static final String SEPARATOR_LABEL = "--------------";
    private static final String RETURN_LABEL = "Return ";
    private static final int EXIT_OPTION = 0;

    private final String customerVat;
    private final DaemonClient client;

    public MainMenu(String customerVat, DaemonClient client, boolean isCustomer) {
        this.customerVat = customerVat;
        this.client = client;
    }

    @Override
    public boolean show() {
        drawFormTitle();
        return doShow();
    }

    @Override
    public boolean doShow() {
        Menu menu = buildMainMenu();
        final MenuRenderer renderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        return renderer.render();
    }

    private Menu buildMainMenu() {

            int currentOption = 1;
            final Menu customerMenu = new Menu("Main Menu");

            ManageProposalsUI manageProposalsUI = new ManageProposalsUI(customerVat, client);
            ListShowsUI listShowsUI = new ListShowsUI(customerVat, client);

            customerMenu.addItem(currentOption++, "Review Pending Proposals(via Code)", manageProposalsUI::show);
            customerMenu.addItem(currentOption++, "List Scheduled Shows", listShowsUI::show);

            customerMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("User session terminated"));

            return customerMenu;

    }

    @Override
    public String headline() {
        return "Shodrone App";
    }
}
