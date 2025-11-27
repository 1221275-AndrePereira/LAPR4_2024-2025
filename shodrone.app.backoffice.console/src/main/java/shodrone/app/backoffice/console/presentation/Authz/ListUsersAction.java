package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.actions.Action;

/**
 * This class implements the action to list users in the system.
 */
public class ListUsersAction implements Action {

    /**
     * Executes the action to list users.
     *
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean execute() {
        return new ListUsersUI().show();
    }
}
