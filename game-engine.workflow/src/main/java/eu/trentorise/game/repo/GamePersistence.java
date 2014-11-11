package eu.trentorise.game.repo;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.model.Game;

@Document(collection = "game")
public class GamePersistence {

	@Transient
	private final Logger logger = LoggerFactory
			.getLogger(GamePersistence.class);

	@Id
	private String id;

	private String name;

	private Set<String> actions = new HashSet<String>();

	private Set<GenericObjectPersistence> tasks = new HashSet<GenericObjectPersistence>();

	private Set<String> rules = new HashSet<String>();

	public GamePersistence() {

	}

	public GamePersistence(Game game) {
		id = game.getId();
		name = game.getName();
		actions = game.getActions();
		rules = game.getRules();
		for (GameTask gt : game.getTasks()) {
			tasks.add(new GenericObjectPersistence(gt));
		}
	}

	public Game toGame() {
		Game game = new Game();
		game.setId(id);
		game.setName(name);
		game.setActions(actions);
		game.setRules(rules);
		Set<GameTask> t = new HashSet<GameTask>();
		ObjectMapper mapper = new ObjectMapper();
		for (GenericObjectPersistence obj : tasks) {
			try {
				t.add(mapper.convertValue(
						obj.getObj(),
						(Class<? extends GameTask>) Thread.currentThread()
								.getContextClassLoader()
								.loadClass(obj.getType())));
			} catch (Exception e) {
				logger.error("Problem to load class {}", obj.getType());
			}
		}
		game.setTasks(t);
		return game;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getActions() {
		return actions;
	}

	public void setActions(Set<String> actions) {
		this.actions = actions;
	}

	public Set<GenericObjectPersistence> getTasks() {
		return tasks;
	}

	public void setTasks(Set<GenericObjectPersistence> tasks) {
		this.tasks = tasks;
	}

}
