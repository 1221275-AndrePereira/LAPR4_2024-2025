package eapli.shodrone.daemon.customer;

import eapli.shodrone.daemon.customer.presentation.CustomerServiceProtocolServer;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodronePasswordPolicy;

import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;

public class ShodroneDaemon {

    private static final int PORT = 9000;

    public static void main(String[] args) {
        System.out.println("Configuring the daemon...");
        AuthzRegistry.configure(PersistenceContext.repositories().users(), new ShodronePasswordPolicy(), new PlainTextEncoder());

        System.out.println("Starting the server on port " + PORT);
        final var server = new CustomerServiceProtocolServer();
        server.start(PORT, true);

        System.out.println("Exiting the daemon.");
        System.exit(0);
    }
}