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

package eu.trentorise.game.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.bean.RuleDTO;
import eu.trentorise.game.bean.TaskDTO;
import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.managers.GameManager;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.Rule;
import eu.trentorise.game.model.Team;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.task.ClassificationTask;

@Component
public class Converter {

	@Autowired
	private GameService gameSrv;

	public GameDTO convertGame(Game game) {
		GameDTO gDTO = null;
		if (game != null) {
			gDTO = new GameDTO();
			gDTO.setId(game.getId());
			gDTO.setActions(game.getActions());
			gDTO.setExpiration(game.getExpiration());
			gDTO.setName(game.getName());
			gDTO.setOwner(game.getOwner());

			// remove internal actions
			Iterator<String> iter = gDTO.getActions() != null ? gDTO
					.getActions().iterator() : null;

			while (iter != null && iter.hasNext()) {
				if (iter.next().startsWith(GameManager.INTERNAL_ACTION_PREFIX)) {
					iter.remove();
				}
			}

			if (game.getRules() != null) {
				gDTO.setRules(new HashSet<RuleDTO>());
				for (String ruleUrl : game.getRules()) {
					RuleDTO r = new RuleDTO();
					r.setId(ruleUrl);
					Rule rule = gameSrv.loadRule(game.getId(), ruleUrl);
					if (rule != null) {
						r.setName(rule.getName());
					}
					gDTO.getRules().add(r);
				}
			}

			gDTO.setTerminated(game.isTerminated());

			if (game.getConcepts() != null) {
				gDTO.setPointConcept(new HashSet<PointConcept>());
				gDTO.setBadgeCollectionConcept(new HashSet<BadgeCollectionConcept>());
				for (GameConcept gc : game.getConcepts()) {
					if (gc instanceof PointConcept) {
						gDTO.getPointConcept().add((PointConcept) gc);
					}

					if (gc instanceof BadgeCollectionConcept) {
						gDTO.getBadgeCollectionConcept().add(
								(BadgeCollectionConcept) gc);
					}
				}
			}

			if (game.getTasks() != null) {
				gDTO.setClassificationTask(new HashSet<ClassificationTask>());
				for (GameTask gt : game.getTasks()) {
					gDTO.getClassificationTask().add((ClassificationTask) gt);
				}
			}
		}
		return gDTO;
	}

	public Game convertGame(GameDTO game) {
		Game g = new Game();
		g.setId(game.getId());
		g.setActions(game.getActions());
		g.setExpiration(game.getExpiration());
		g.setName(game.getName());
		g.setOwner(game.getOwner());
		if (game.getRules() != null) {
			g.setRules(new HashSet<String>());
			for (RuleDTO r : game.getRules()) {
				g.getRules().add(r.getId());
			}
		}
		g.setTerminated(game.isTerminated());

		if (game.getPointConcept() != null) {
			g.setConcepts(new HashSet<GameConcept>());
			g.getConcepts().addAll(game.getPointConcept());
		}

		if (game.getBadgeCollectionConcept() != null) {
			if (g.getConcepts() == null) {
				g.setConcepts(new HashSet<GameConcept>());
			}
			g.getConcepts().addAll(game.getBadgeCollectionConcept());

		}

		if (game.getClassificationTask() != null) {
			g.setTasks(new HashSet<GameTask>());
			g.getTasks().addAll(game.getClassificationTask());
		}

		return g;
	}

	public TaskDTO convertClassificationTask(ClassificationTask t) {
		TaskDTO task = null;
		if (t != null) {
			task = new TaskDTO();
			task.setClassificationName(t.getClassificationName());
			task.setCronExpression(t.getSchedule() != null ? t.getSchedule()
					.getCronExpression() : null);
			task.setItemsToNotificate(t.getItemsToNotificate());
			task.setItemType(t.getItemType());
			task.setName(t.getName());
		}
		return task;
	}

	public ClassificationTask convertClassificationTask(TaskDTO t) {
		ClassificationTask task = null;
		if (t != null) {
			task = new ClassificationTask();
			task.setClassificationName(t.getClassificationName());
			if (t.getCronExpression() != null) {
				TaskSchedule sched = new TaskSchedule();
				sched.setCronExpression(t.getCronExpression());
				task.setSchedule(sched);
			}
			task.setItemsToNotificate(t.getItemsToNotificate());
			task.setItemType(t.getItemType());
			task.setName(t.getName());
		}
		return task;
	}

	public PlayerState convertPlayerState(PlayerStateDTO ps) {
		PlayerState res = null;
		if (ps != null) {
			res = new PlayerState();
			res.setPlayerId(ps.getPlayerId());
			res.setGameId(ps.getGameId());
			res.setCustomData(ps.getCustomData());
			Collection<Set<GameConcept>> concepts = ps.getState().values();
			for (Set<GameConcept> s : concepts) {
				res.getState().addAll(s);
			}
		}

		return res;
	}

	public PlayerStateDTO convertPlayerState(PlayerState ps) {
		PlayerStateDTO res = null;
		if (ps != null) {
			res = new PlayerStateDTO();
			res.setGameId(ps.getGameId());
			res.setPlayerId(ps.getPlayerId());

			res.setState(new HashMap<String, Set<GameConcept>>());
			if (ps.getState() != null) {
				for (GameConcept gc : ps.getState()) {
					String conceptType = gc.getClass().getSimpleName();
					Set<GameConcept> gcSet = res.getState().get(conceptType);
					if (gcSet == null) {
						gcSet = new HashSet<GameConcept>();
						res.getState().put(conceptType, gcSet);
					}
					gcSet.add(gc);
				}
			}
		}

		return res;
	}

	public Team convertTeam(TeamDTO t) {
		Team team = null;
		if (t != null) {
			team = new Team();
			team.setName(t.getName());
			team.setGameId(t.getGameId());
			team.setCustomData(t.getCustomData());
			team.setMembers(t.getMembers());
			team.setPlayerId(t.getPlayerId());
			Collection<Set<GameConcept>> concepts = t.getState().values();
			for (Set<GameConcept> s : concepts) {
				team.getState().addAll(s);
			}
		}

		return team;
	}
}
