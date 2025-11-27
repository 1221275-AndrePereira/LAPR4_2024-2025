package eapli.shodrone.figure.repository;

import eapli.shodrone.figure.domain.Description;
import eapli.shodrone.figure.domain.Figure;
import eapli.framework.domain.repositories.DomainRepository;
import eapli.shodrone.figure.domain.Version;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;

import java.util.List;
import java.util.Optional;

public interface FigureRepository extends DomainRepository<Long, Figure> {
    List<Figure> allFigures();

    Optional<Figure> findById(Long id);

    Optional<Figure> findByDescription(Description description);

    Optional<Figure> findFigureByDescriptionAndVersion(Description description, Version version);

    List<Figure> allActivePublicFigures();

    List<Figure> listExclusiveFigureOfUser(ShodroneUser user);


}
