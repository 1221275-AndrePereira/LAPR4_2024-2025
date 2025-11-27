package eapli.shodrone.figurecategory.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.general.domain.model.Designation;
import eapli.shodrone.figurecategory.domain.FigureCategory;

import java.util.Optional;

public interface FigureCategoryCatalogue extends DomainRepository<Long, FigureCategory> {

    Optional<FigureCategory> searchByFigureCategoryName(Designation name);

    Iterable<FigureCategory> obtainAllFigureCategories();

}
