package eu.trentorise.game.model;

public class ClasspathRule extends Rule {

	private String url;

	public ClasspathRule(String gameId, String url) {
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
