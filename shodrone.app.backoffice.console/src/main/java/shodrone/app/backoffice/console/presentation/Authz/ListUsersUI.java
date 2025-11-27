package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import eapli.shodrone.usermanagement.application.ListSystemUsersController;
import shodrone.app.backoffice.console.utility.SystemUserPrinter;

/**
 * User Interface for listing system users.
 * <p>
 * This class extends the AbstractListUI to provide a console-based interface for
 * listing system users.
 * <p>
 * It uses the ListSystemUsersController to retrieve the list of users and
 * displays them in a formatted manner.
 */
@SuppressWarnings({ "squid:S106" })
public class ListUsersUI extends AbstractListUI<SystemUser> {
    private final ListSystemUsersController theController = new ListSystemUsersController();

    /**
     * Constructor for ListUsersUI.
     * <p>
     * Initializes the controller for listing system users.
     */
    @Override
    public String headline() {
        return "List Users";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<SystemUser> elements() {
        return theController.allUsers();
    }

    @Override
    protected Visitor<SystemUser> elementPrinter() {
        return new SystemUserPrinter();
    }

    @Override
    protected String elementName() {
        return "User";
    }

    /**
     * Returns the header for the list of system users.
     * @return The formatted header string.
     */
    @Override
    protected String listHeader() {
        return String.format("#  %-30s%-30s%-30s%-30s", "USERNAME", "F. NAME", "L. NAME", "STATUS");
    }

}

