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

import java.util.Set;

import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;

public class Game {
	private String id;
	private String name;
	private String owner;
	private Set<String> actions;
	private Set<GameTask> tasks;
	private Set<String> rules;
	private Set<GameConcept> concepts;

	/**
	 * game expiration time. If game must live forever set a negative value
	 */
	private long expiration;

	private boolean terminated;

	public Game() {
	}

	public Game(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getActions() {
		return actions;
	}

	public void setActions(Set<String> actions) {
		this.actions = actions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<GameTask> getTasks() {
		return tasks;
	}

	public void setTasks(Set<GameTask> tasks) {
		this.tasks = tasks;
	}

	public Set<String> getRules() {
		return rules;
	}

	public void setRules(Set<String> rules) {
		this.rules = rules;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public Set<GameConcept> getConcepts() {
		return concepts;
	}

	public void setConcepts(Set<GameConcept> concepts) {
		this.concepts = concepts;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
