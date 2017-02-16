package eu.trentorise.game.model.core;

import java.util.List;
import java.util.Map;

public class RawSearchQuery extends SearchQuery {
	private Map<String, Object> rawQuery;

	public RawSearchQuery(Map<String, Object> rawQuery,
			List<SortItem> sortItems, Projection projection) {
		super(sortItems, projection);
		this.rawQuery = rawQuery;
	}

	public Map<String, Object> getRawQuery() {
		return rawQuery;
	}

}
