package eu.trentorise.game.model.core;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RawSearchQuery extends SearchQuery {
	private Map<String, Object> rawQuery;

	@JsonCreator
	public RawSearchQuery(
			@JsonProperty("rawQuery") Map<String, Object> rawQuery,
			@JsonProperty("projection") Projection projection,
			@JsonProperty("sortItems") List<SortItem> sortItems) {
		super(sortItems, projection);
		this.rawQuery = rawQuery;
	}

	public Map<String, Object> getRawQuery() {
		return rawQuery;
	}

}
