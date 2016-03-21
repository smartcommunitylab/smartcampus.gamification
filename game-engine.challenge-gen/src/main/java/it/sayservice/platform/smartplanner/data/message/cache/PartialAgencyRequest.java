package it.sayservice.platform.smartplanner.data.message.cache;

import java.util.List;

public class PartialAgencyRequest {

	private String version;
	private List<String> routes;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getRoutes() {
		return routes;
	}

	public void setRoutes(List<String> routes) {
		this.routes = routes;
	}

}
