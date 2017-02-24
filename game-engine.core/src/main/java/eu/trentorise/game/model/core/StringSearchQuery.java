package eu.trentorise.game.model.core;

import java.util.List;

public class StringSearchQuery extends SearchQuery {
	private String query;

	public StringSearchQuery(String rawQuery, Projection projection,
			List<SortItem> sortItems) {
		super(sortItems, projection);
		this.query = rawQuery;
	}

	public String getQuery() {
		return query;
	}

}
