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

package eu.trentorise.game.repo;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.task.ClassificationTask;
import eu.trentorise.game.task.GeneralClassificationTask;

@Document(collection = "game")
public class GamePersistence {

	@Transient
	private final Logger logger = LoggerFactory.getLogger(GamePersistence.class);

	@Id
	private String id;

	private String name;

	private String owner;

	private String domain;

	private Set<String> actions = new HashSet<String>();

	private Set<GenericObjectPersistence> tasks = new HashSet<GenericObjectPersistence>();

	private Set<String> rules = new HashSet<String>();

	private Set<GenericObjectPersistence> concepts = new HashSet<GenericObjectPersistence>();

	private long expiration;
	private boolean terminated;

	public GamePersistence() {

	}

	public GamePersistence(Game game) {
		id = game.getId();
		name = game.getName();
		owner = game.getOwner();
		domain = game.getDomain();
		actions = game.getActions();
		rules = game.getRules();
		if (game.getTasks() != null) {
			for (GameTask gt : game.getTasks()) {
				tasks.add(new GenericObjectPersistence(gt));
			}
		}

		if (game.getConcepts() != null) {
			for (GameConcept gc : game.getConcepts()) {
				concepts.add(new GenericObjectPersistence(gc));
			}
		}
		expiration = game.getExpiration();
		terminated = game.isTerminated();
	}

	public Game toGame() {
		Game game = new Game();
		game.setId(id);
		game.setName(name);
		game.setOwner(owner);
		game.setDomain(domain);
		game.setActions(actions);
		game.setRules(rules);
		Set<GameTask> t = new HashSet<GameTask>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
		for (GenericObjectPersistence obj : tasks) {
			// fix: Use @JsonDeserialize to maintain compatibility with
			// databases previous of version 2.0.0 in which
			// classificationTask was a concrete class representing general
			// classifications
			if (obj.getType().equals(ClassificationTask.class.getCanonicalName())) {
				obj.setType(GeneralClassificationTask.class.getCanonicalName());
			}
			try {
				t.add(mapper.convertValue(obj.getObj(), (Class<? extends GameTask>) Thread.currentThread()
						.getContextClassLoader().loadClass(obj.getType())));
			} catch (Exception e) {
				LogHub.error(id, logger, "Problem to load class {}", obj.getType(), e);
			}
		}
		game.setTasks(t);

		Set<GameConcept> gc = new HashSet<GameConcept>();
		for (GenericObjectPersistence obj : concepts) {
			try {
				gc.add(mapper.convertValue(obj.getObj(), (Class<? extends GameConcept>) Thread.currentThread()
						.getContextClassLoader().loadClass(obj.getType())));
			} catch (Exception e) {
				logger.error("Problem to load class {}", obj.getType());
			}
		}
		game.setConcepts(gc);
		game.setExpiration(expiration);
		game.setTerminated(terminated);
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Set<String> getRules() {
		return rules;
	}

	public void setRules(Set<String> rules) {
		this.rules = rules;
	}

	public Set<GenericObjectPersistence> getConcepts() {
		return concepts;
	}

	public void setConcepts(Set<GenericObjectPersistence> concepts) {
		this.concepts = concepts;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
