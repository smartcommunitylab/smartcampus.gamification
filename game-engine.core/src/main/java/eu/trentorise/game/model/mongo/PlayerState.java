package eu.trentorise.game.model.mongo;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PlayerState {

	@Id
	private String id;
	/** game id. **/
	private String gameId;
	/** player id. (reqd). **/
	private String playerId;
	/** concepts. **/
	private Map<Object, Map<Object, Object>> concepts;
	/** custom data. **/
	private Map<Object, Map<Object, Object>> customData;
	/** meta data. **/
	private Map<Object, Map<Object, Object>> metaData;

	public PlayerState() {

	}

	public PlayerState(String id, String gameId, String playerId, Map<Object, Map<Object, Object>> concepts,
			Map<Object, Map<Object, Object>> customData, Map<Object, Map<Object, Object>> metaData) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.playerId = playerId;
		this.concepts = concepts;
		this.customData = customData;
		this.metaData = metaData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public Map<Object, Map<Object, Object>> getConcepts() {
		return concepts;
	}

	public void setConcepts(Map<Object, Map<Object, Object>> concepts) {
		this.concepts = concepts;
	}

	public Map<Object, Map<Object, Object>> getCustomData() {
		return customData;
	}

	public void setCustomData(Map<Object, Map<Object, Object>> customData) {
		this.customData = customData;
	}

	public Map<Object, Map<Object, Object>> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<Object, Map<Object, Object>> metaData) {
		this.metaData = metaData;
	}

	public double getIncrementalScore(String pointConceptName, String periodName, String key) {
		double score = 0;

		if (this.getConcepts() != null && this.getConcepts().containsKey("PointConcept")) {

				Map<Object, Object> t0 = (Map<Object, Object>) this.getConcepts().get("PointConcept");

				if (t0 != null && t0.containsKey(pointConceptName)
						&& t0.get(pointConceptName) instanceof Map)  {
					Map<Object, Object> t = (Map<Object, Object>) t0.get(pointConceptName);

					if (t != null && t.containsKey("obj")) {
						Map<Object, Object> t1 = (Map<Object, Object>) t.get("obj");

						if (t1 != null && t1.containsKey("periods")) {
							Map<Object, Object> t2 = (Map<Object, Object>) t1.get("periods");

							if (t2 != null && t2.containsKey(periodName)) {
								Map<Object, Object> t3 = (Map<Object, Object>) t2.get(periodName);

								if (t3 != null && t3.containsKey("instances")) {
									Map<Object, Object> t4 = (Map<Object, Object>) t3.get("instances");

									if (t4 != null && t4.containsKey(key)) {
										Map<Object, Object> t5 = (Map<Object, Object>) t4.get(key);

										if (t5 != null && t5.containsKey("score"))
											score = (Double) t5.get("score");
									}
								}
							}
						}
					}
				}
			}
		
		return score;
	}

	public double getGeneralItemScore(String itemType) {
		double score = 0;

		if (this.getConcepts() != null && this.getConcepts().containsKey("PointConcept")) {

			Map<Object, Object> t0 = (Map<Object, Object>) this.getConcepts().get("PointConcept");

			if (t0 != null && t0.containsKey(itemType) && t0.get(itemType) instanceof Map) {
				Map<Object, Object> t = (Map<Object, Object>) t0.get(itemType);

				if (t != null && t.containsKey("obj")) {
					Map<Object, Object> t1 = (Map<Object, Object>) t.get("obj");

					if (t1 != null && t1.containsKey("score"))
						score = (Double) t1.get("score");
				}
			}
		}

		return score;
	}

}
