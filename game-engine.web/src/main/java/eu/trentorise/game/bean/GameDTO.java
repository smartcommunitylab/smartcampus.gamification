package eu.trentorise.game.bean;

import java.util.HashSet;
import java.util.Set;

import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.task.ClassificationTask;

public class GameDTO {
	private String id;
	private String name;
	private String owner;
	private Set<String> actions;
	private Set<String> rules;
	private long expiration;
	private boolean terminated;

	private Set<ClassificationTask> classificationTask;
	private Set<PointConcept> pointConcept;
	private Set<BadgeCollectionConcept> badgeCollectionConcept;

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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Set<String> getActions() {
		return actions;
	}

	public void setActions(Set<String> actions) {
		this.actions = actions;
	}

	public Set<String> getRules() {
		return rules;
	}

	public void setRules(Set<String> rules) {
		this.rules = rules;
	}

	public Set<ClassificationTask> getClassificationTask() {
		return classificationTask;
	}

	public void setClassificationTask(Set<ClassificationTask> classificationTask) {
		this.classificationTask = classificationTask;
	}

	public Set<PointConcept> getPointConcept() {
		return pointConcept;
	}

	public void setPointConcept(Set<PointConcept> pointConcept) {
		this.pointConcept = pointConcept;
	}

	public Set<BadgeCollectionConcept> getBadgeCollectionConcept() {
		return badgeCollectionConcept;
	}

	public void setBadgeCollectionConcept(
			Set<BadgeCollectionConcept> badgeCollectionConcept) {
		this.badgeCollectionConcept = badgeCollectionConcept;
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

	public GameDTO() {

	}

	public GameDTO(Game g) {
		if (g != null) {
			id = g.getId();
			actions = g.getActions();
			expiration = g.getExpiration();
			name = g.getName();
			owner = g.getOwner();
			rules = g.getRules();
			terminated = g.isTerminated();

			if (g.getConcepts() != null) {
				pointConcept = new HashSet<PointConcept>();
				badgeCollectionConcept = new HashSet<BadgeCollectionConcept>();
				for (GameConcept gc : g.getConcepts()) {
					if (gc instanceof PointConcept) {
						pointConcept.add((PointConcept) gc);
					}

					if (gc instanceof BadgeCollectionConcept) {
						badgeCollectionConcept.add((BadgeCollectionConcept) gc);
					}
				}
			}

			if (g.getTasks() != null) {
				classificationTask = new HashSet<ClassificationTask>();
				for (GameTask gt : g.getTasks()) {
					classificationTask.add((ClassificationTask) gt);
				}
			}
		}
	}

	public Game toGame() {
		Game g = new Game();
		g.setId(id);
		g.setActions(actions);
		g.setExpiration(expiration);
		g.setName(name);
		g.setOwner(owner);
		g.setRules(rules);
		g.setTerminated(terminated);

		if (pointConcept != null) {
			g.setConcepts(new HashSet<GameConcept>());
			g.getConcepts().addAll(pointConcept);
		}

		if (badgeCollectionConcept != null) {
			if (g.getConcepts() == null) {
				g.setConcepts(new HashSet<GameConcept>());
			}
			g.getConcepts().addAll(badgeCollectionConcept);

		}

		if (classificationTask != null) {
			g.setTasks(new HashSet<GameTask>());
			g.getTasks().addAll(classificationTask);
		}

		return g;
	}
}
