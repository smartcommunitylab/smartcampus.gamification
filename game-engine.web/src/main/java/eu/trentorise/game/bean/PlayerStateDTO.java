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

package eu.trentorise.game.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Inventory;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.repo.ChallengeConceptPersistence;

@JsonInclude(Include.NON_NULL)
public class PlayerStateDTO {
	private String playerId;
	private String gameId;

	private Map<String, Set<GameConcept>> state = new HashMap<String, Set<GameConcept>>();
    private List<PlayerLevel> levels = new ArrayList<>();

    private Inventory inventory;

	private CustomData customData = new CustomData();

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

	public Map<String, Set<GameConcept>> getState() {
		return state;
	}

	public void setState(Map<String, Set<GameConcept>> state) {
		this.state = state;
	}

	public CustomData getCustomData() {
		return customData;
	}

	public void setCustomData(CustomData customData) {
		this.customData = customData;
	}

    public List<PlayerLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<PlayerLevel> levels) {
        this.levels = levels;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

	public void loadChallengeConcepts(List<ChallengeConceptPersistence> listCcps) {
		listCcps.forEach(ccp -> {
			String conceptType = ccp.getConcept().getClass().getSimpleName();
			Set<GameConcept> gcSet = state.get(conceptType);
			if (gcSet == null) {
				gcSet = new HashSet<GameConcept>();
				state.put(conceptType, gcSet);
			}
			gcSet.add(ccp.getConcept());
		});
	}

}
