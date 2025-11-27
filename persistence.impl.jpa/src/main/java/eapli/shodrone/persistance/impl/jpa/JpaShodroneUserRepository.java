package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.customerRepresentative.domain.CustomerRepresentative;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class JpaShodroneUserRepository extends JpaAutoTxRepository<ShodroneUser, VATNumber, VATNumber>
        implements ShodroneUserRepository {

    public JpaShodroneUserRepository(final TransactionalContext autoTx) {
        super(autoTx, "vatNumber");
    }

    public JpaShodroneUserRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "vatNumber");
    }

    @Override
    public Optional<ShodroneUser> findByUsername(final Username name) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return matchOne("e.systemUser.username=:name", params);
    }

    @Override
    public Optional<ShodroneUser> findByVatNumber(final VATNumber number) {
        final Map<String, Object> params = new HashMap<>();
        params.put("number", number);
        return matchOne("e.vatNumber=:number", params);
    }
    @Override
    public Iterable<ShodroneUser> findAllActive() {
        return match("e.systemUser.active = true");
    }

    public Optional<ShodroneUser> findCustomerByRepresentative(final Username username) {
        final Map<String, Object> params = new HashMap<>();
        params.put("username", username);

        return matchOne("EXISTS (SELECT rep FROM e.customerRepresentatives rep WHERE rep.username = :username)", params);
    }
}
