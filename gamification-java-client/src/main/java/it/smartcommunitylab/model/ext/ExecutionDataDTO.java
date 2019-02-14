package it.smartcommunitylab.model.ext;

/**
 * Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.util.Date;
import java.util.Map;

public class ExecutionDataDTO {
	public String gameId;
	public String actionId;
	public String playerId;
	public Map<String, Object> data;
	/*
	 * date can be serialized either as millis timestamp or as ISO 8601 date
	 * string representation
	 */
	private Date executionMoment;

	public ExecutionDataDTO() {
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Date getExecutionMoment() {
		return executionMoment;
	}

	public void setExecutionMoment(Date executionMoment) {
		this.executionMoment = executionMoment;
	}

}
