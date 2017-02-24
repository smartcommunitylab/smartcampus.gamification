package eu.trentorise.game.model.core;

import java.util.List;

public class StringSearchQuery extends SearchQuery {
	private String rawQuery;

	public StringSearchQuery(String rawQuery, Projection projection,
			List<SortItem> sortItems) {
		super(sortItems, projection);
		this.rawQuery = rawQuery;
	}

	public String getRawQuery() {
		return rawQuery;
	}

}
