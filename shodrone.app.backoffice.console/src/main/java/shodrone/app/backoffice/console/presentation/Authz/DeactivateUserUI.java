package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.usermanagement.application.DeactivateActiveSystemUserController;

import java.util.ArrayList;
import java.util.List;

/**
 * UI for deactivating a user.
 * <p>
 * This class is responsible for displaying the user interface for deactivating a user in the system.
 * It retrieves the list of active users and allows the user to select one to deactivate.
 */
@SuppressWarnings("squid:S106")
public class DeactivateUserUI extends AbstractUI {

    // Controller for managing user deactivation
    private final DeactivateActiveSystemUserController theController = new DeactivateActiveSystemUserController();

    /**
     * Constructor for DeactivateUserUI.
     */
    @Override
    protected boolean doShow() {
        final List<SystemUser> list = new ArrayList<>();
        // Retrieve the list of active users
        final Iterable<SystemUser> iterable = this.theController.activeUsers();
        if (!iterable.iterator().hasNext()) {
            System.out.println("There is no registered User");
        } else {
            var cont = 1;
            // Display the list of active users
            System.out.println("SELECT User to deactivate\n");
            System.out.printf("%-6s%-10s%-30s%-30s%n", "Nº:", "Username", "Firstname", "Lastname");
            for (final SystemUser user : iterable) {
                list.add(user);
                System.out.printf("%-6d%-10s%-30s%-30s%n", cont, user.username(),
                        user.name().firstName(),
                        user.name().lastName());
                cont++;
            }
            // Prompt the user to select a user to deactivate
            final var option = Console.readInteger("Enter user nº to deactivate or 0 to finish ");
            if (option == 0) {
                System.out.println("No user selected");
            } else {
                try {
                    this.theController.deactivateUser(list.get(option - 1));
                } catch (@SuppressWarnings("unused") final ConcurrencyException ex) {
                    System.out
                            .println(
                                    "WARNING: That entity has already been changed or deleted since you last read it");
                }
            }
        }
        return true;
    }


    @Override
    public String headline() {
        return "Deactivate User";
    }
}

