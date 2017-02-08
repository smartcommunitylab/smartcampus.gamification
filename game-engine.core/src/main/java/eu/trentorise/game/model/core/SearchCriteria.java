package eu.trentorise.game.model.core;

import java.util.List;

public class SearchCriteria {
	private List<SortItem> sortItems;
	private Projection projection;

	public SearchCriteria(List<SortItem> sortItems, Projection projection) {
		this.sortItems = sortItems;
		this.projection = projection;
	}

	public SearchCriteria() {
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

		public Projection(List<String> include, List<String> exclude) {
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
