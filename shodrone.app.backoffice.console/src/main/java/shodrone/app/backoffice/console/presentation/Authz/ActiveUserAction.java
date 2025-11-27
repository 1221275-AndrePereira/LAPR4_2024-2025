package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.actions.Action;

/**
 * This class is responsible for executing the action of displaying the active user UI.
 * It implements the Action interface.
 */
public class ActiveUserAction implements Action {

    /**
     * This method executes the action of displaying the active user UI.
     * @return true if the action was executed successfully, false otherwise.
     */
    @Override
    public boolean execute() {
        return new ActiveUserUI().show();
    }
}
