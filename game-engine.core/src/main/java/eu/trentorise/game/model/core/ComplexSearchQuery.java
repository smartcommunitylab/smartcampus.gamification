package eu.trentorise.game.model.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplexSearchQuery extends SearchQuery {
	private Map<String, List<QueryElement>> query;

	@JsonCreator
	public ComplexSearchQuery(
			@JsonProperty("query") Map<String, List<QueryElement>> query,
			@JsonProperty("projection") Projection projection,
			@JsonProperty("sortItems") List<SortItem> sortItems) {
		super(sortItems, projection);
		this.query = query;
	}

	public Map<String, List<QueryElement>> getQuery() {
		return query;
	}

	public static class QueryElement {
		private String conceptName;
		private String periodName;
		private Date instanceDate;
		private String clause;
		private String field;

		public QueryElement(String conceptName, String periodName,
				Date instanceDate, String clause) {
			this.conceptName = conceptName;
			this.periodName = periodName;
			this.instanceDate = instanceDate;
			this.clause = clause;
		}

		/*
		 * Jackson permits to have only one annotation JsonCreator per Class so
		 * this private Constructor is used by jackson deserializer
		 */
		@JsonCreator
		@SuppressWarnings("unused")
		private QueryElement(@JsonProperty("conceptName") String conceptName,
				@JsonProperty("periodName") String periodName,
				@JsonProperty("instanceDate") Date instanceDate,
				@JsonProperty("clause") String clause,
				@JsonProperty("field") String field) {
			this.conceptName = conceptName;
			this.periodName = periodName;
			this.instanceDate = instanceDate;
			this.clause = clause;
			this.field = field;
		}

		public QueryElement(String conceptName, String clause) {
			this.conceptName = conceptName;
			this.clause = clause;
		}

		public QueryElement(String conceptName, String field, String clause) {
			this.conceptName = conceptName;
			this.clause = clause;
			this.field = field;
		}

		public String getConceptName() {
			return conceptName;
		}

		public String getClause() {
			return clause;
		}

		public String getPeriodName() {
			return periodName;
		}

		public Date getInstanceDate() {
			return instanceDate;
		}

		public String getField() {
			return field;
		}
	}

	public enum SearchElement {
		POINT_CONCEPT("pointConcept"), PERIODIC_POINT_CONCEPT(
				"periodicPointConcept"), BADGE_COLLECTION_CONCEPT(
				"badgeCollectionConcept"), CHALLENGE_CONCEPT("challengeConcept"), CUSTOM_DATA(
				"customData");

		private String displayName;

		private SearchElement(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

}
