package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.usermanagement.application.DeactivateActiveSystemUserController;

import java.util.ArrayList;
import java.util.List;

/**
 * UI for activating a user.
 * <p>
 * This class is responsible for displaying the list of inactive users and allowing
 * the user to select one to activate.
 * <p>
 * It extends the AbstractUI class and implements the doShow() method to handle
 * the user interaction.
 */
@SuppressWarnings("squid:S106")
public class ActiveUserUI extends AbstractUI {
    private final DeactivateActiveSystemUserController theController = new DeactivateActiveSystemUserController();

    /**
     * Constructor for ActiveUserUI.
     * <p>
     * This constructor initializes the controller for managing user activation.
     */
    @Override
    protected boolean doShow() {
        final List<SystemUser> list = new ArrayList<>();
        final Iterable<SystemUser> iterable = this.theController.inactiveUsers();
        // Check if there are any inactive users
        if (!iterable.iterator().hasNext()) {
            System.out.println("There is no registered User");
        } else {
            // Display the list of inactive users
            var cont = 1;
            System.out.println("SELECT User to activate\n");
            System.out.printf("%-6s%-10s%-30s%-30s%n", "Nº:", "Username", "Firstname", "Lastname");
            for (final SystemUser user : iterable) {
                list.add(user);
                System.out.printf("%-6d%-10s%-30s%-30s%n", cont, user.username(),
                        user.name().firstName(),
                        user.name().lastName());
                cont++;
            }
            // Prompt the user to select an inactive user to activate
            final var option = Console.readInteger("Enter user nº to activate or 0 to finish ");
            if (option == 0) {
                System.out.println("No user selected");
            } else {
                try {
                    this.theController.activateUser(list.get(option - 1));
                } catch (@SuppressWarnings("unused") final ConcurrencyException ex) {
                    System.out
                            .println(
                                    "WARNING: That entity has already been changed or deleted since you last read it");
                }
            }
        }
        return true;
    }

    /**
     * This method is used to provide a string representation of the UI.
     * <p>
     * It returns the name of the UI class.
     *
     * @return A string representing the name of the UI class.
     */
    @Override
    public String headline() {
        return "Activate User";
    }
}

