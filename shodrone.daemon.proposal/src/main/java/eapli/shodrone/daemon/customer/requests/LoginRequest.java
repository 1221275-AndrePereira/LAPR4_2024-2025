package eapli.shodrone.daemon.customer.requests;

import eapli.framework.infrastructure.authz.application.AuthenticationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;

import eapli.shodrone.customerRepresentative.repository.CustomerRepresentativeRepository;
import eapli.shodrone.shodroneusermanagement.application.ListShodroneUsersController;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.usermanagement.application.ListSystemUsersController;

import java.util.Optional;

/**
 * Handles the login request from a customer.
 * Authenticates the user and returns their VAT number upon success.
 */
public class LoginRequest extends ProtocolRequest {

    private final String username;
    private final String password;
    private final AuthenticationService authSvc;
    private final ListShodroneUsersController listShodroneUsersController;
    private final ListSystemUsersController listSystemUsersController;


    public LoginRequest(final String request, final String username, final String password) {
        super(request);
        this.username = username;
        this.password = password;
        this.authSvc = AuthzRegistry.authenticationService();
        this.listSystemUsersController = new ListSystemUsersController();
        this.listShodroneUsersController = new ListShodroneUsersController();
    }

    @Override
    public String execute() {
        VATNumber vatNumber = null;

        try {
            if (authSvc.authenticate(username, password).isPresent()) {

                Optional<SystemUser> user = listSystemUsersController.find(Username.valueOf(username));

                if (user.isPresent()) {
                    if (user.get().roleTypes().contains(Role.valueOf("CUSTOMER"))) {
                        Optional<ShodroneUser> customer = listShodroneUsersController.findShodroneUserByUsername(user.get().username());
                        if (customer.isPresent()) {
                            vatNumber = customer.get().vatNumber();
                        }
                    } else if (user.get().roleTypes().contains(Role.valueOf("CUSTOMER_REPRESENTATIVE"))) {
                        Optional<ShodroneUser> customer = listShodroneUsersController.findCustomerByRepresentative(user.get().username());
                        if (customer.isPresent()) {
                            vatNumber = customer.get().vatNumber();
                        }
                    }
                    System.out.println("User inst a Customer neither a Customer Representative");
                }

                return "LOGGED_IN,\"" + vatNumber + "\"\n";
            } else {
                return buildBadRequest("Invalid credentials.");
            }
        } catch (Exception e) {
            return buildServerError(e.getMessage());
        }


    }
}

