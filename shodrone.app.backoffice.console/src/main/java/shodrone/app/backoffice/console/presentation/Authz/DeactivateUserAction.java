package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.actions.Action;

/**
 * Action to deactivate a user.
 * This action is part of the backoffice console application.
 * It uses the DeactivateUserUI class to perform the action.
 */
public class DeactivateUserAction implements Action {

    /**
     * Executes the action to deactivate a user.
     * It creates an instance of DeactivateUserUI and calls its show method.
     *
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean execute() {
        return new DeactivateUserUI().show();
    }
}
