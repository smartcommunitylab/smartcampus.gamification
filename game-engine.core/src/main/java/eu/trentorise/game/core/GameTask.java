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

import java.util.ArrayList;
import java.util.List;

import eu.trentorise.game.managers.GameManager;

public abstract class GameTask {

	private String name;

	private TaskSchedule schedule;

	public abstract void execute(GameContext ctx);

	protected abstract List<String> getExecutionActions();

	public List<String> retrieveActions() {
		List<String> list = getExecutionActions();
		List<String> res = new ArrayList<String>();
		if (list != null) {
			for (String a : list) {
				res.add(GameManager.INTERNAL_ACTION_PREFIX + a);
			}
		}

		return res;
	}

	public GameTask(String name, TaskSchedule schedule) {
		this.name = name;
		this.setSchedule(schedule);
	}

	public GameTask() {

	}

	public TaskSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(TaskSchedule schedule) {
		this.schedule = schedule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameTask other = (GameTask) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
