package eu.trentorise.game.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rule")
public class DBRule extends Rule {
	@Id
	private String id;
	private String content;

	public DBRule(String gameId, String content) {
		super(gameId);
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
