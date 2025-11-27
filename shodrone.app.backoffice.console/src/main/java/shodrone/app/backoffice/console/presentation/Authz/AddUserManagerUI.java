package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import eapli.framework.actions.menu.MenuItem;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import eapli.shodrone.shodroneusermanagement.application.AddShodroneUserController;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUserPriority;
import eapli.shodrone.usermanagement.application.AddSystemUserController;
import eapli.shodrone.usermanagement.domain.ShodronePasswordPolicy;
import utils.InputValidator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UI for adding a new user to the system as a Manager.
 */
@SuppressWarnings("java:S106")
public class AddUserManagerUI extends AbstractUI {

    // Controller for adding a system user
    private final AddSystemUserController theController = new AddSystemUserController();
    // Controller for adding a Shodrone user
    private final AddShodroneUserController addShodroneUserController = new AddShodroneUserController();

    /**
     * Constructor for AddUserManagerUI.
     */
    @Override
    protected boolean doShow() {
        final String username = InputValidator.readValidInput("Username: ", InputValidator::isValidUsername, "Invalid username or already in use. Please try again.");
        final String password = InputValidator.readValidInput("Password (At least 1 Capital and 1 Number, Mininum 7 digits): ", InputValidator::isValidPassword, "Invalid password. Please try again.");
        System.out.println("Password Strength: " + ShodronePasswordPolicy.strength(password).toString());
        final String firstName = InputValidator.readValidInput("First Name: ", InputValidator::isValidName, "Invalid first name. Please try again.");
        final String lastName = InputValidator.readValidInput("Last Name: ", InputValidator::isValidName, "Invalid last name. Please try again.");
        final String email = InputValidator.readValidInput("Email (it needs to end with @shodrone.com): ", InputValidator::isValidEmail, "Invalid email. Please try again.");

        final Set<Role> roleTypes = new HashSet<>();
        boolean show;

        // Show the role selection menu
        do {
            show = showRoles(roleTypes);
        } while (!show);

        try {
            // Show the user priority selection menus
            List<ShodroneUserPriority> priorities = Arrays.asList(ShodroneUserPriority.values());

            final SelectWidget<ShodroneUserPriority> prioritySelector = new SelectWidget<>("Select Shodrone User Priority", priorities);
            prioritySelector.show();

            ShodroneUserPriority selectedPriority = prioritySelector.selectedElement();

            // Check if the user selected a priority
            if (selectedPriority == null) {
                System.out.println("No priority selected. Defaulting to Regular.");
                selectedPriority = ShodroneUserPriority.Regular;
            }

            // Ask for the VAT number, phone number, and address
            final String vatNumber = InputValidator.readValidInput("VAT Number (It needs to start with (1|2|3|5|6|8|9) + 8 digits): ", InputValidator::isValidVATNumber, "Invalid VAT number or already in use. Please try again.");
            final String phoneNumber = InputValidator.readValidInput("Phone Number (It needs to start with [(optional)+351|00351] + 9XXXXXXXX):", InputValidator::isValidPhoneNumber, "Invalid phone number. Please try again.");
            System.out.println("Address: ");
            final String streetAddress = InputValidator.readValidInput("Street Address: ", InputValidator::isValidStreetAddress, "Invalid street address. Please try again.");
            final String postalCode = InputValidator.readValidInput("Postal Code (XXXX-XXX): ", InputValidator::isValidPostalCode, "Invalid postal code. Please try again.");
            final String city = InputValidator.readValidInput("City: ", InputValidator::isValidCity, "Invalid city. Please try again.");

            // Add the user to the system
            this.addShodroneUserController.signup(username, password, firstName, lastName, email, vatNumber, streetAddress,
                    postalCode, city, phoneNumber, roleTypes, selectedPriority);
        } catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
            System.out.println("Username already exists");
        }
        return false;
    }


    /**
     * Returns the title of the UI.
     *
     * @return the title of the UI
     */
    @Override
    public String headline() {
        return "Add User (@Manager)";
    }

    /**
     * Displays the role selection menu and returns true if a role is selected.
     *
     * @param roleTypes the set of selected roles
     * @return true if a role is selected, false otherwise
     */
    private boolean showRoles(final Set<Role> roleTypes) {
        final Menu rolesMenu = buildRolesMenu(roleTypes);
        final MenuRenderer renderer = new VerticalMenuRenderer(rolesMenu, MenuItemRenderer.DEFAULT);

        return renderer.render();
    }

    /**
     * Builds the roles menu with the available role types.
     *
     * @param roleTypes the set of selected roles
     * @return the roles menu
     */
    private Menu buildRolesMenu(final Set<Role> roleTypes) {
        final Menu rolesMenu = new Menu();
        int i = 0;
        rolesMenu.addItem(MenuItem.of(i++, "NO ROLE", Actions.SUCCESS));

        for (final Role roleType : theController.getRoleTypesNonAdmin()) {
            rolesMenu.addItem(
                    MenuItem.of(i++, roleType.toString(), () -> roleTypes.add(roleType)));
        }
        return rolesMenu;
    }


}
