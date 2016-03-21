package it.sayservice.platform.smartplanner.data.message.otpbeans;

import java.util.List;
import java.util.Map;

public class GeolocalizedStopRequest {

	private String agencyId;
	private double[] coordinates;
	private double radius;
	private int pageNumber;
	private int pageSize = 50;

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[] latlon) {
		this.coordinates = latlon;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
