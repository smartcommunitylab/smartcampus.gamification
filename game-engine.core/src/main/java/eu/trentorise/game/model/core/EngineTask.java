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

package eu.trentorise.game.model.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import eu.trentorise.game.core.TaskSchedule;

public abstract class EngineTask {

	private String name;

	private TaskSchedule schedule;

    public abstract void execute();

	public EngineTask(String name, TaskSchedule schedule) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException(
					"task name cannot be null or empty");
		}
		this.name = name;
		this.setSchedule(schedule);
	}

	public EngineTask() {

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
        return new HashCodeBuilder(31, 21).append(name).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EngineTask other = (EngineTask) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
