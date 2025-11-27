package eapli.shodrone.infrastructure.boostrapers;

import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.framework.infrastructure.authz.application.AuthenticationService;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;

import eapli.shodrone.infrastructure.boostrapers.demo.*;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.validations.Invariants;
import eapli.framework.strings.util.Strings;
import eapli.framework.actions.Action;

import eapli.shodrone.usermanagement.domain.UserBuilderHelper;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Base Bootstrapping data app
 *
 */
@SuppressWarnings("squid:S106")
public class ShodroneBootstrapper implements Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ShodroneBootstrapper.class);

    private static final String POWERUSER_PWD = "poweruserA1";
    private static final String POWERUSER = "poweruser";

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final AuthenticationService authenticationService = AuthzRegistry.authenticationService();
    private final UserRepository userRepository = PersistenceContext.repositories().users();

    @Override
    public boolean execute() {
        // declare bootstrap actions
        final Action[] actions = {
                new MasterUsersBootstrapper(),
                new ShodroneUserBootstrapper(),
                new FigureBootstrapper(),
                new DroneBootstrapper(),
                new ShowRequestBootstrapper(),
                new ShowProposalBootstrapper(),
                new ProposalDocumentBootstrapper()
        };

        registerPowerUser();
        authenticateForBootstrapping();

        // execute all bootstrapping
        boolean ret = true;
        for (final Action boot : actions) {
            System.out.println("Bootstrapping " + nameOfEntity(boot) + "...");
            ret &= boot.execute();
        }
        return ret;
    }

    private boolean registerPowerUser() {
        LOGGER.info("Attempting to register power user...");
        try {
            // Test database connection
            if (!userRepository.findAll().iterator().hasNext()) {
                LOGGER.info("User table is empty, proceeding with power user creation");
            }

            final SystemUserBuilder userBuilder = UserBuilderHelper.builder();
            userBuilder.withUsername(POWERUSER)
                    .withPassword(POWERUSER_PWD)
                    .withName("joão", "power")
                    .withEmail("joao@shodrone.com")
                    .withRoles(ShodroneRoles.POWER_USER);
            final SystemUser newUser = userBuilder.build();

            SystemUser poweruser = userRepository.save(newUser);
            LOGGER.info("Power user successfully created");
            assert poweruser != null;
            return true;
        } catch (ConcurrencyException | IntegrityViolationException e) {
            LOGGER.warn("Assuming {} already exists (activate trace log for details)", POWERUSER);
            LOGGER.trace("Assuming existing record", e);
            return false;
        } catch (Exception e) {
            LOGGER.error("Failed to create power user", e);
            throw e;
        }
    }

    /**
     * authenticate a super user to be able to register new users
     *
     */
    protected void authenticateForBootstrapping() {
        authenticationService.authenticate(POWERUSER, POWERUSER_PWD);
        Invariants.ensure(authz.hasSession());
    }

    private String nameOfEntity(final Action boot) {
        final String name = boot.getClass().getSimpleName();
        return Strings.left(name, name.length() - "Bootstrapper".length());
    }
}
