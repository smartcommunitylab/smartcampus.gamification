package eu.trentorise.game.bean;

import eu.trentorise.game.model.core.ComplexSearchQuery;
import eu.trentorise.game.model.core.RawSearchQuery;

public class WrapperQuery {
	private ComplexSearchQuery complexQuery;
	private RawSearchQuery rawQuery;

	/**
	 * @return the complexQuery
	 */
	public ComplexSearchQuery getComplexQuery() {
		return complexQuery;
	}

	/**
	 * @param complexQuery
	 *            the complexQuery to set
	 */
	public void setComplexQuery(ComplexSearchQuery complexQuery) {
		this.complexQuery = complexQuery;
	}

	/**
	 * @return the rawQuery
	 */
	public RawSearchQuery getRawQuery() {
		return rawQuery;
	}

	/**
	 * @param rawQuery
	 *            the rawQuery to set
	 */
	public void setRawQuery(RawSearchQuery rawQuery) {
		this.rawQuery = rawQuery;
	}

}
