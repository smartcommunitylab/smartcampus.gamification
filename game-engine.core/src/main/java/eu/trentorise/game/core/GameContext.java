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

package eu.trentorise.game.core;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.trentorise.game.managers.GameManager;
import eu.trentorise.game.managers.NotificationManager;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.TaskDataConverter;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.services.Workflow;

@Component("gameCtx")
@Scope("prototype")
public class GameContext {

	private String gameRefId;
	private GameTask task;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private Workflow workflow;

	@Autowired
	private TaskService taskSrv;

	@Autowired
	private NotificationManager notificationSrv;

	private TaskDataConverter converter = new TaskDataConverter();

	public enum Order {
		ASC, DESC
	}

	public GameContext(String gameRefId, GameTask task) {
		this.gameRefId = gameRefId;
		this.task = task;
	}

	public GameContext() {

	}

	public synchronized void sendAction(String action, String playerId, Map<String, Object> params) {
		// transform in internalAction
		action = action.startsWith(GameManager.INTERNAL_ACTION_PREFIX) ? action
				: GameManager.INTERNAL_ACTION_PREFIX + action;
		workflow.apply(gameRefId, action, playerId, params, null);
	}

	public synchronized void sendAction(String action, String playerId, Map<String, Object> params,
			List<Object> workingMemoryObjects) {
		// transform in internalAction
		action = action.startsWith(GameManager.INTERNAL_ACTION_PREFIX) ? action
				: GameManager.INTERNAL_ACTION_PREFIX + action;
		workflow.apply(gameRefId, action, playerId, params, workingMemoryObjects);
	}

	public synchronized void sendNotification(Notification notification) {
		notificationSrv.notificate(notification);
	}

	public PlayerState readStatus(String playerId) {
		return playerSrv.loadState(gameRefId, playerId, true);
	}

	public List<String> readPlayers() {
		return playerSrv.readPlayers(gameRefId);
	}

	public String getGameRefId() {
		return gameRefId;
	}

	public GameTask getTask() {
		return task;
	}

	public String writeTaskData(Object data) {
		return taskSrv.saveData(gameRefId, task.getName(), data);
	}

	public Object readTaskData() {
		List<Object> res = taskSrv.readData(gameRefId, task.getName());
		return res.isEmpty() ? null : res.get(0);
	}

	public <T> T readTaskData(Class<T> classType) {
		return converter.convert(readTaskData(), classType);
	}

}
