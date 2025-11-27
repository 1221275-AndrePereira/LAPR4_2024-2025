package shodrone.app.backoffice.console.presentation.Authz;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.MenuItem;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.actions.menu.Menu;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUserPriority;
import eapli.shodrone.usermanagement.application.AddSystemUserController;
import eapli.shodrone.shodroneusermanagement.application.AddShodroneUserController;
import eapli.shodrone.usermanagement.domain.ShodronePasswordPolicy;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import utils.InputValidator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UI for adding a new user.
 */
@SuppressWarnings("java:S106")
public class AddUserAdminUI extends AbstractUI {

    // Controller for adding a system user
    private final AddSystemUserController theController = new AddSystemUserController();
    // Controller for adding a Shodrone user
    private final AddShodroneUserController addShodroneUserController = new AddShodroneUserController();

    /**
     * Constructor for AddUserAdminUI.
     */
    @Override
    protected boolean doShow() {
        // Display the menu for adding a new user and get the user data
        final String username = readValidInput("Username: ", InputValidator::isValidUsername, "Invalid username or already in use. Please try again.");
        final String password = readValidInput("Password (At least 1 Capital and 1 Number, Mininum 7 digits): ", InputValidator::isValidPassword, "Invalid password. Please try again.");
        System.out.println("Password Strength: " + ShodronePasswordPolicy.strength(password).toString());
        final String firstName = readValidInput("First Name: ", InputValidator::isValidName, "Invalid first name. Please try again.");
        final String lastName = readValidInput("Last Name: ", InputValidator::isValidName, "Invalid last name. Please try again.");
        final String email = readValidInput("Email (it needs to end with @shodrone.com): ", InputValidator::isValidEmail, "Invalid email. Please try again.");

        // Display the menu for selecting roles and get the selected roles
        final Set<Role> roleTypes = new HashSet<>();
        boolean show;

        do {
            show = showRoles(roleTypes);
        } while (!show);

        try {
            // If the user has the role of Power User or Admin, add them as a system user
            if (roleTypes.contains(ShodroneRoles.POWER_USER) || roleTypes.contains(ShodroneRoles.ADMIN)) {
                this.theController.addUser(username, password, firstName, lastName, email, roleTypes);
            } else { // If the user has the role of Shodrone User, add them as a Shodrone user
                // Display the menu for selecting Shodrone user priority
                List<ShodroneUserPriority> priorities = Arrays.asList(ShodroneUserPriority.values());

                final SelectWidget<ShodroneUserPriority> prioritySelector = new SelectWidget<>("Select Shodrone User Priority", priorities);
                prioritySelector.show();

                ShodroneUserPriority selectedPriority = prioritySelector.selectedElement();

                // If no priority is selected, default to Regular
                if (selectedPriority == null) {
                    System.out.println("No priority selected. Defaulting to Regular.");
                    selectedPriority = ShodroneUserPriority.Regular;
                }

                // Get the VAT number, phone number, address, postal code, and city from the user
                final String vatNumber = readValidInput("VAT Number (It needs to start with (1|2|3|5|6|8|9) + 8 digits): ", InputValidator::isValidVATNumber, "Invalid VAT number or already in use. Please try again.");
                final String phoneNumber = readValidInput("It needs to start with [(optional)+351|00351] + 9XXXXXXXX):", InputValidator::isValidPhoneNumber, "Invalid phone number. Please try again.");
                System.out.println("Address: ");
                final String streetAddress = readValidInput("Street Address: ", InputValidator::isValidStreetAddress, "Invalid street address. Please try again.");
                final String postalCode = readValidInput("Postal Code (XXXX-XXX): ", InputValidator::isValidPostalCode, "Invalid postal code. Please try again.");
                final String city = readValidInput("City: ", InputValidator::isValidCity, "Invalid city. Please try again.");

                // Add the Shodrone user with the provided details
                this.addShodroneUserController.signup(username, password, firstName, lastName, email, vatNumber, streetAddress,
                        postalCode, city, phoneNumber, roleTypes, selectedPriority);
            }
        } catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
            System.out.println("Username already exists");
        }
        return false;
    }

    /**
     * @return the title of the UI
     */
    @Override
    public String headline() {
        return "Add User (@Admin)";
    }

    /**
     * Displays the menu for selecting roles and returns true if the user selects a role.
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
     * Builds the menu for selecting roles.
     *
     * @param roleTypes the set of selected roles
     * @return the menu for selecting roles
     */
    private Menu buildRolesMenu(final Set<Role> roleTypes) {
        final Menu rolesMenu = new Menu();
        int i = 0;
        rolesMenu.addItem(MenuItem.of(i++, "NO ROLE", Actions.SUCCESS));

        for (final Role roleType : theController.getRoleTypesAdmin()) {
            rolesMenu.addItem(
                    MenuItem.of(i++, roleType.toString(), () -> roleTypes.add(roleType)));
        }
        return rolesMenu;
    }

    /**
     * Reads valid input from the user based on the provided prompt and validation function.
     *
     * @param prompt      the prompt to display to the user
     * @param validator   the validation function
     * @param errorMessage the error message to display if validation fails
     * @return the valid input from the user
     */
    private String readValidInput(String prompt, java.util.function.Predicate<String> validator, String errorMessage) {
        String input;
        do {
            input = Console.readLine(prompt);
            if (!validator.test(input)) {
                System.out.println(errorMessage);
            }
        } while (!validator.test(input));
        return input;
    }
}