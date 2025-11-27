package eapli.shodrone.persistance.impl.jpa;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.figurecategory.repositories.FigureCategoryCatalogue;
import shodrone.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JpaFigureCategoryCatalogue
        extends JpaAutoTxRepository<FigureCategory, Long, Long> implements FigureCategoryCatalogue{

    public JpaFigureCategoryCatalogue(final TransactionalContext autoTx) {
        super(autoTx, "figureCategoryId");
    }

    public JpaFigureCategoryCatalogue(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "figureCategoryId");
    }

    @Override
    public Optional<FigureCategory> ofIdentity(Long id) {
        return findById(id);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public Iterable<FigureCategory> obtainAllFigureCategories() {
        return super.findAll();
    }

    @Override
    public Optional<FigureCategory> searchByFigureCategoryName(Designation name) {
        final Map<String, Object> params = new HashMap<>();
        params.put("figureCategoryName", name);
        return matchOne("e.figureCategoryName = :figureCategoryName", params);
    }
}
