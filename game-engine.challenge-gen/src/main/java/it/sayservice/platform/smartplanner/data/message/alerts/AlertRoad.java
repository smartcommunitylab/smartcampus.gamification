package it.sayservice.platform.smartplanner.data.message.alerts;

import it.sayservice.platform.smartplanner.data.message.RoadElement;

public class AlertRoad extends Alert {
	private static final long serialVersionUID = -6928706057154015012L;

	/**
	 * agency managing the roads
	 */
	private String agencyId;
	/**
	 * Road affected
	 */
	private RoadElement road;
	
	/**
	 * Types of changes
	 */
	private AlertRoadType[] changeTypes;

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public RoadElement getRoad() {
		return road;
	}

	public void setRoad(RoadElement road) {
		this.road = road;
	}

	public AlertRoadType[] getChangeTypes() {
		return changeTypes;
	}

	public void setChangeTypes(AlertRoadType[] changeTypes) {
		this.changeTypes = changeTypes;
	}
}
