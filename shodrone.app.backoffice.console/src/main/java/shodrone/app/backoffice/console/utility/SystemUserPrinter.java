package shodrone.app.backoffice.console.utility;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.visitor.Visitor;

/**
 * SystemUserPrinter is a visitor that prints the details of a SystemUser.
 * It implements the Visitor interface and overrides the visit method to
 * print the user's username, first name, last name, and active status.
 */
@SuppressWarnings({ "squid:S106" })
public class SystemUserPrinter implements Visitor<SystemUser> {

    /**
     * Prints the details of a SystemUser.
     *
     * @param visitee the SystemUser to print
     */
    @Override
    public void visit(final SystemUser visitee) {
        System.out.printf("%-30s%-30s%-30s%-30s", visitee.username(), visitee.name().firstName(),
                visitee.name().lastName(), visitee.isActive() ? "ACTIVE" : "INACTIVE");
    }
}
