package eu.trentorise.game.repo;

import java.util.List;

public class NotificationQuery {

	private long fromTs = -1;
	private long toTs = -1;
	private List<String> includeTypes;
	private List<String> excludeTypes;

	public long getFromTs() {
		return fromTs;
	}

	public void setFromTs(long fromTs) {
		this.fromTs = fromTs;
	}

	public long getToTs() {
		return toTs;
	}

	public void setToTs(long toTs) {
		this.toTs = toTs;
	}

	public List<String> getIncludeTypes() {
		return includeTypes;
	}

	public void setIncludeTypes(List<String> includeTypes) {
		this.includeTypes = includeTypes;
	}

	public List<String> getExcludeTypes() {
		return excludeTypes;
	}

	public void setExcludeTypes(List<String> excludeTypes) {
		this.excludeTypes = excludeTypes;
	}

}
