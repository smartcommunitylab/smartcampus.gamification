package it.sayservice.platform.smartplanner.data.message;

import java.io.Serializable;

public class RoadElement implements Serializable {
	private static final long serialVersionUID = -2806074797696111050L;
	
	private String lat;
	private String lon;
	
	private String streetCode;
	private String street;

	private String fromNumber;
	private String toNumber;
	
	private String fromIntersection;
	private String toIntersection;

	private String note;

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getStreetCode() {
		return streetCode;
	}

	public void setStreetCode(String streetCode) {
		this.streetCode = streetCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getFromNumber() {
		return fromNumber;
	}

	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}

	public String getToNumber() {
		return toNumber;
	}

	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	public String getFromIntersection() {
		return fromIntersection;
	}

	public void setFromIntersection(String fromIntersection) {
		this.fromIntersection = fromIntersection;
	}

	public String getToIntersection() {
		return toIntersection;
	}

	public void setToIntersection(String toIntersection) {
		this.toIntersection = toIntersection;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
}
