package eapli.shodrone.persistence.impl.inmemory;

import eapli.shodrone.figure.domain.Description;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.figure.domain.Version;
import eapli.shodrone.figure.repository.FigureRepository;
import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;

import java.util.List;
import java.util.Optional;


public class InMemoryFigureRepository extends InMemoryDomainRepository<Figure, Long> implements FigureRepository {

    static {
        InMemoryInitializer.init();
    }

    @Override
    public List<Figure> allFigures() {
        return List.of();
    }

    @Override
    public Optional<Figure> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Figure> findByDescription(Description description) {
        return Optional.empty();
    }

    @Override
    public Optional<Figure> findFigureByDescriptionAndVersion(Description description, Version version) {
        return Optional.empty();
    }

    @Override
    public List<Figure> allActivePublicFigures() {
        return List.of();
    }

    @Override
    public List<Figure> listExclusiveFigureOfUser(ShodroneUser user) {
        return List.of();
    }

    @Override
    public Optional<Figure> ofIdentity(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteOfIdentity(Long entityId) {

    }
}
