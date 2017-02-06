package eu.trentorise.game.repo;

import java.util.List;

public interface ExtendPlayerRepo {

	public List<StatePersistence> search(List<String> projectionIncludeFields, List<String> projectionExcludeFields);
}
