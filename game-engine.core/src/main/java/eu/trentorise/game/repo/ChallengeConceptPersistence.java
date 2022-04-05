package eu.trentorise.game.repo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.trentorise.game.model.ChallengeConcept;

@Document(collection = "challengeConcept")
public class ChallengeConceptPersistence {
	@Id
	private String id;
	private String name;
	private String playerId;
	private String gameId;
	private ChallengeConcept concept;

	public ChallengeConceptPersistence(ChallengeConcept obj) {
		this.concept = obj;
	}

	public ChallengeConceptPersistence() {
		super();
	}

	public ChallengeConceptPersistence(String gameId, String playerId) {
		this.gameId = gameId;
		this.playerId = playerId;
	}

	public ChallengeConceptPersistence(ChallengeConcept obj, String gameId, String playerId, String name) {
		this.concept = obj;
		this.gameId = gameId;
		this.playerId = playerId;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public ChallengeConcept getConcept() {
		return concept;
	}

	public void setConcept(ChallengeConcept concept) {
		this.concept = concept;
	}

}
