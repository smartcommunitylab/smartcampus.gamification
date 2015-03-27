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

package eu.trentorise.game.managers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.model.TaskData;
import eu.trentorise.game.repo.TaskDataRepo;
import eu.trentorise.game.services.TaskService;

public abstract class TaskDataManager implements TaskService {

	@Autowired
	protected TaskDataRepo taskDataRepo;

	public abstract void createTask(GameTask task, GameContext ctx);

	public abstract boolean destroyTask(GameTask task, String gameId);

	public String saveData(String gameId, String taskName, Object data) {
		TaskData taskData = new TaskData();
		taskData.setGameId(gameId);
		taskData.setTaskName(taskName);
		taskData.setTimestamp(System.currentTimeMillis());
		taskData.setData(data);
		taskData.setDataClassname(data.getClass().getCanonicalName());

		taskData.setId(generateId(gameId, taskName));

		taskDataRepo.save(taskData);
		return taskData.getId();
	}

	public List<Object> readData(String gameId, String taskName) {
		TaskData data = taskDataRepo.findOne(generateId(gameId, taskName));
		if (data.getGameId().equals(gameId)
				&& data.getTaskName().equals(taskName)) {
			return Arrays.asList(data.getData());
		} else {
			return Collections.<Object> emptyList();
		}
	}

	private String generateId(String gameId, String taskName) {
		return gameId + ":" + taskName;
	}

}
