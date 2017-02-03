/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.repo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.GameConcept;

@Document(collection = "playerState")
public class StatePersistence {

	@Id
	private String id;

	private String playerId;
	private String gameId;

	protected Map<String, Object> metadata = new HashMap<String, Object>();

	private Map<String, Map<String, GenericObjectPersistence>> concepts = new HashMap<>();
	// private List<GenericObjectPersistence> concepts = new
	// ArrayList<GenericObjectPersistence>();

	private CustomData customData;

	public StatePersistence() {

	}

	public StatePersistence(PlayerState state) {
		playerId = state.getPlayerId();
		gameId = state.getGameId();

		for (GameConcept gc : state.getState()) {
			Map<String, GenericObjectPersistence> res = getConceptMap(concepts,
					gc.getClass());
			res.put(gc.getName(), new GenericObjectPersistence(gc));
			concepts.put(gc.getClass().getSimpleName(), res);
		}

		customData = state.getCustomData();
	}

	private <T extends GameConcept> Map<String, GenericObjectPersistence> getConceptMap(
			Map<String, Map<String, GenericObjectPersistence>> concepts,
			Class<T> key) {
		Map<String, GenericObjectPersistence> map = concepts.get(key
				.getSimpleName());
		return map == null ? new HashMap<String, GenericObjectPersistence>()
				: map;

	}

	public StatePersistence(String gameId, String playerId) {
		this.gameId = gameId;
		this.playerId = playerId;
	}

	public Map<String, Map<String, GenericObjectPersistence>> getConcepts() {
		return concepts;
	}

	public void setConcepts(
			Map<String, Map<String, GenericObjectPersistence>> concepts) {
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

	public CustomData getCustomData() {
		return customData;
	}

	public void setCustomData(CustomData customData) {
		this.customData = customData;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
	
	public double getIncrementalScore(String pointConceptName, String periodName, String key) {
		double score = 0;

		if (this.getConcepts() != null && this.getConcepts().containsKey("PointConcept")) {

			Map<String, GenericObjectPersistence> t0 = (Map<String, GenericObjectPersistence>) this.getConcepts()
					.get("PointConcept");

			if (t0 != null && t0.containsKey(pointConceptName)) {

				GenericObjectPersistence t = t0.get(pointConceptName);

				if (t.getObj() != null && t.getObj().containsKey("periods")) {

					Map<Object, Object> t2 = (Map<Object, Object>) t.getObj().get("periods");

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

		return score;
	}

	public double getGeneralItemScore(String itemType) {
		double score = 0;

		if (this.getConcepts() != null && this.getConcepts().containsKey("PointConcept")) {

			Map<String, GenericObjectPersistence> t0 = (Map<String, GenericObjectPersistence>) this.getConcepts()
					.get("PointConcept");

			if (t0 != null && t0.containsKey(itemType)) {
				GenericObjectPersistence t = t0.get(itemType);

				if (t.getObj() != null && t.getObj().containsKey("score")) {
					score = (Double) t.getObj().get("score");
				}
			}
		}

		return score;
	}
}
