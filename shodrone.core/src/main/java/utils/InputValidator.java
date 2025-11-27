package utils;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.framework.io.util.Console;
import eapli.framework.strings.util.StringPredicates;
import eapli.shodrone.figure.domain.Keyword;
import eapli.shodrone.shodroneusermanagement.application.ShodroneUserService;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import eapli.shodrone.usermanagement.application.ListSystemUsersController;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Utility class for validating user input.
 * This class provides methods to validate various types of input such as username, password, email, etc.
 * It uses regular expressions to validate the input format and checks for uniqueness where applicable.
 */
public class InputValidator {

    private static final ShodroneUserService shodroneUserService = new ShodroneUserService();
    private static final ListSystemUsersController listSystemUsersController = new ListSystemUsersController();

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9._-]{3,20}$";
    private static final String NAME_REGEX = "^[A-Za-z\\s]{1,50}$";
    private static final String EMAIL_REGEX = "^[\\w.%+-]+@shodrone\\.com$";;
    private static final String VAT_NUMBER_REGEX = "^(1|2|3|5|6|8|9)\\d{8}$";
    private static final String PHONE_NUMBER_REGEX = "^(\\+351|00351)?9[0-9]{8}$";
    private static final String POSTAL_CODE_REGEX = "^[0-9]{4}-[0-9]{3}$";
    private static final String STREET_ADDRESS_REGEX = "^[\\w\\s,.-]{1,100}$";
    private static final String CITY_REGEX = "^[A-Za-z\\s]{1,50}$";
    private static final String VERSION_REGEX = "^[0-9]+(\\.[0-9]+)*$";
    private static final String KEYWORD_REGEX = "^[a-z0-9-]+$";


    /**
     * Validates the input based on the provided prompt and validation criteria.
     *
     * @param prompt       The prompt message to display to the user.
     * @param validator    The validation criteria as a predicate.
     * @param errorMessage The error message to display if validation fails.
     * @return The valid input from the user.
     */
    public static String readValidInput(String prompt, java.util.function.Predicate<String> validator, String errorMessage) {
        String input;
        do {
            input = Console.readLine(prompt);
            if (!validator.test(input)) {
                System.out.println(errorMessage);
            }
        } while (!validator.test(input));
        return input;
    }

    /**
     * Validates the username input and checks for uniqueness.
     *
     * @param username       The prompt message to display to the user.
     * @return true if the username is valid and unique, false otherwise.
     */
    public static boolean isValidUsername(String username) {
        if (!Pattern.matches(USERNAME_REGEX, username)) {
            return false;
        }
        // Check if the username already exists in the system
        Optional<SystemUser> systemUserOptional = listSystemUsersController.find(Username.valueOf(username));
        if (!systemUserOptional.isEmpty()){
            return false;
        }

        return true;
    }

    /**
     * Validates the password input.
     *
     * @param password The password to validate.
     * @return true if the password is valid, false otherwise.
     */
    public static boolean isValidPassword(String password) {
        if (StringPredicates.isNullOrEmpty(password) || (password.length() < 7) ||
                !StringPredicates.containsDigit(password) ) {
            return false;
        }
        return StringPredicates.containsCapital(password);
    }

    /**
     * Validates the name input.
     *
     * @param name The name to validate.
     * @return true if the name is valid, false otherwise.
     */
    public static boolean isValidName(String name) {
        return Pattern.matches(NAME_REGEX, name);
    }

    /**
     * Validates the email input.
     *
     * @param email The email to validate.
     * @return true if the email is valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * Validates the VAT number input.
     *
     * @param vatNumber The VAT number to validate.
     * @return true if the VAT number is valid and unique, false otherwise.
     */
    public static boolean isValidVATNumber(String vatNumber) {
        if (!Pattern.matches(VAT_NUMBER_REGEX, vatNumber)) {
            return false;
        }
        Optional<ShodroneUser> shodroneUserUserUserOptional = shodroneUserService.findShodroneUserByVatNumber(VATNumber.valueOf(vatNumber));
        if (!shodroneUserUserUserOptional.isEmpty()){
            return false;
        }

        return true;
    }

    /**
     * Validates the phone number input.
     *
     * @param phoneNumber The phone number to validate.
     * @return true if the phone number is valid, false otherwise.
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }

    /**
     * Validates the postal code input.
     *
     * @param postalCode The postal code to validate.
     * @return true if the postal code is valid, false otherwise.
     */
    public static boolean isValidPostalCode(String postalCode) {
        return Pattern.matches(POSTAL_CODE_REGEX, postalCode);
    }

    /**
     * Validates the street address input.
     *
     * @param streetAddress The street address to validate.
     * @return true if the street address is valid, false otherwise.
     */
    public static boolean isValidStreetAddress(String streetAddress) {
        return Pattern.matches(STREET_ADDRESS_REGEX, streetAddress);
    }

    /**
     * Validates the city input.
     *
     * @param city The city to validate.
     * @return true if the city is valid, false otherwise.
     */
    public static boolean isValidCity(String city) {
        return Pattern.matches(CITY_REGEX, city);
    }

    /**
     * Validates the username input using a regular expression.
     *
     * @param username The username to validate.
     * @return true if the username is valid, false otherwise.
     */
    public static boolean isValidUsernameRegex(String username) {return Pattern.matches(USERNAME_REGEX, username);
    }

    /**
     * Validates the version input using a regular expression.
     *
     * @param version The version to validate.
     * @return true if the version is valid, false otherwise.
     */
    public static boolean isValidVersion(String version) {return Pattern.matches(VERSION_REGEX, version);}
    
    
    public static boolean isValidKeyword(String keyword) {return Pattern.matches(KEYWORD_REGEX, keyword) && keyword.length() <= Keyword.MAX_LENGTH;}
}