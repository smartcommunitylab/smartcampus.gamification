package eu.trentorise.game.repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import eu.trentorise.game.model.core.SearchCriteria;

public interface ExtendPlayerRepo {

	public Page<StatePersistence> search(SearchCriteria searchCriteria, Pageable pageable);
}
