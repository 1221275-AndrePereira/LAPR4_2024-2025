package shodrone.app.backoffice.console.utility;

import eapli.framework.visitor.Visitor;

import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;

/**
 * The ShodroneUserPrinter class implements the Visitor interface to provide
 * a functionality to print the information of a ShodroneUser in a consistent display format.
 *
 * The class overrides the visit method to handle the logic of extracting and
 * formatting the user's VAT number and email address for display purposes.
 */
public class ShodroneUserPrinter implements Visitor<ShodroneUser> {

    @Override
    public void visit(final ShodroneUser visitee) {
        String vatStr = "[No VAT]";
        if (visitee.identity() != null) {
            vatStr = visitee.identity().toString();
        }

        String emailStr = "[No Email]";
        if (visitee.systemUser() != null && visitee.systemUser().email() != null) {
            emailStr = visitee.systemUser().email().toString();
        }
        // Consistent display format: (Email) [VAT]
        System.out.printf("(%s) [%s]", emailStr, vatStr);
    }
}