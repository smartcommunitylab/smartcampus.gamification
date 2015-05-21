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

package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerState {

	private String playerId;
	private String gameId;

	private Set<GameConcept> state = new HashSet<GameConcept>();

	private List<CustomData> customData = new ArrayList<CustomData>();

	public PlayerState() {
	}

	public PlayerState(String playerId, String gameId) {
		this.playerId = playerId;
		this.gameId = gameId;
	}

	public Set<GameConcept> getState() {
		return state;
	}

	public void setState(Set<GameConcept> state) {
		this.state = state;
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

	public List<CustomData> getCustomData() {
		return customData;
	}

	public void setCustomData(List<CustomData> customData) {
		this.customData = customData;
	}

}