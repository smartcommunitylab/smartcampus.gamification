package eu.trentorise.game.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "playerState")
public class StatePersistence {
	@Id
	private String id;

	private String playerId;
	private String gameId;

	private List<ConceptPersistence> concepts = new ArrayList<ConceptPersistence>();

	public List<ConceptPersistence> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<ConceptPersistence> concepts) {
		this.concepts = concepts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
