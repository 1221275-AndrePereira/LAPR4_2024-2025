package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.actions.Action;

/**
 * Action to add a new user as a manager.
 */
public class AddUserManagerAction implements Action {

    /**
     * Executes the action to add a new user as a manager.
     *
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean execute() {
        return new AddUserManagerUI().show();
    }
}
