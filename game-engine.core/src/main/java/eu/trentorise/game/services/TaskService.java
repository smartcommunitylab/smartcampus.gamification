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

package eu.trentorise.game.services;

import java.util.List;

import org.springframework.stereotype.Service;

import eu.trentorise.game.model.core.EngineTask;
import eu.trentorise.game.model.core.GameTask;

@Service
public interface TaskService {

    void createEngineTask(EngineTask engineTask);

	public void createTask(GameTask task, String gameId);

	public boolean destroyTask(GameTask task, String gameId);

	public void updateTask(GameTask task, String gameId);

	public String saveData(String gameId, String taskName, Object data);

	public List<Object> readData(String gameId, String taskName);

	public void deleteData(String gameId, String taskName);
}
