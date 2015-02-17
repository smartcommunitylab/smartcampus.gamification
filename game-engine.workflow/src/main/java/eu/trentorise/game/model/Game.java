package eu.trentorise.game.model;

import java.util.Set;

import eu.trentorise.game.core.GameTask;

public class Game {
	private String id;
	private String name;
	private Set<String> actions;
	private Set<GameTask> tasks;
	private Set<String> rules;

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

}
