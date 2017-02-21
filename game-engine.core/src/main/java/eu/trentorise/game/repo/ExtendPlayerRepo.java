package eu.trentorise.game.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import eu.trentorise.game.model.core.ComplexSearchQuery;
import eu.trentorise.game.model.core.RawSearchQuery;
import eu.trentorise.game.model.core.SearchCriteria;
import eu.trentorise.game.model.core.StringSearchQuery;

public interface ExtendPlayerRepo {

	public Page<StatePersistence> search(SearchCriteria searchCriteria, Pageable pageable);

	public Page<StatePersistence> search(RawSearchQuery query, Pageable pageable);

	public Page<StatePersistence> search(ComplexSearchQuery query, Pageable pageable);

	public Page<StatePersistence> search(StringSearchQuery query, Pageable pageable);

}
