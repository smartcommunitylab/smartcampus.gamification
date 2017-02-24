package eu.trentorise.game.model.core;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchQuery {
	private List<SortItem> sortItems;
	private Projection projection;

	public SearchQuery(List<SortItem> sortItems, Projection projection) {
		this.sortItems = sortItems;
		this.projection = projection;
	}

	public List<SortItem> getSortItems() {
		return sortItems;
	}

	public Projection getProjection() {
		return projection;
	}

	public static class Projection {
		private List<String> include;
		private List<String> exclude;

		@JsonCreator
		public Projection(@JsonProperty("include") List<String> include,
				@JsonProperty("exclude") List<String> exclude) {
			this.include = include;
			this.exclude = exclude;
		}

		public List<String> getIncludeFields() {
			return include;
		}

		public List<String> getExcludeFields() {
			return exclude;
		}
	}
}
