package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.shodrone.figure.domain.Description;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.figure.domain.Version;
import eapli.shodrone.figure.repository.FigureRepository;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JpaFigureRepository extends JpaAutoTxRepository<Figure, Long, Long> implements FigureRepository {

    public JpaFigureRepository(final TransactionalContext autoTx) {
        super(autoTx, "id");
    }

    public JpaFigureRepository(final String puname) {
        super(puname, "id");
    }

    @Override
    public List<Figure> allFigures() {
        return List.of();
    }

    @Override
    public Optional<Figure> findById(Long id) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return matchOne(" e.id = :id", params);
    }

    @Override
    public Optional<Figure> findByDescription(Description description) {
        final Map<String, Object> params = new HashMap<>();
        params.put("desc", description);

        return matchOne(" e.description = :desc", params);
    }

    @Override
    public Optional<Figure> findFigureByDescriptionAndVersion(Description description, Version version) {
        final Map<String, Object> params = new HashMap<>();
        params.put("desc", description);
        params.put("figVer", version);

        return matchOne(" e.description = :desc AND e.figureVersion = :figVer", params);

    }

    @Override
    public List<Figure> allActivePublicFigures() {
        return match("e.isActive = true AND e.exclusiveCustomer IS NULL");
    }

    @Override
    public List<Figure> listExclusiveFigureOfUser(ShodroneUser user) {
        final Map<String, Object> params = new HashMap<>();
        params.put("user", user);

        return match("e.exclusiveCustomer = :user", params);
    }


    @Override
    public Optional<Figure> ofIdentity(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteOfIdentity(Long entityId) {

    }
}
