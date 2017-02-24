package eu.trentorise.game.model.core;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RawSearchQuery extends SearchQuery {
	private Map<String, Object> query;

	@JsonCreator
	public RawSearchQuery(
			@JsonProperty("query") Map<String, Object> rawQuery,
			@JsonProperty("projection") Projection projection,
			@JsonProperty("sortItems") List<SortItem> sortItems) {
		super(sortItems, projection);
		this.query = rawQuery;
	}

	public Map<String, Object> getQuery() {
		return query;
	}

}
