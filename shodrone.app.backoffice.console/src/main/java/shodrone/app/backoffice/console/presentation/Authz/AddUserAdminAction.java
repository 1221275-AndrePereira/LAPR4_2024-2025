package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.actions.Action;

/**
 * Action to add a new user with admin privileges.
 */
public class AddUserAdminAction implements Action {

    /**
     * Executes the action to add a new user with admin privileges.
     *
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean execute() {
        return new AddUserAdminUI().show();
    }
}
