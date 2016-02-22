package eu.trentorise.game.model.core;

public abstract class UrlRule extends Rule {

	private String url;

	public UrlRule(String gameId) {
		super(gameId);
	}

	public UrlRule(String gameId, String url) {
		super(gameId);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
