package eu.trentorise.game.model.core;

import java.util.List;

public class StringSearchQuery extends SearchQuery {
	private String rawQuery;

	public StringSearchQuery(List<SortItem> sortItems, Projection projection,
			String rawQuery) {
		super(sortItems, projection);
		this.rawQuery = rawQuery;
	}

	public String getRawQuery() {
		return rawQuery;
	}

}
