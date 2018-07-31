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
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.bean.ChallengeAssignmentDTO;
import eu.trentorise.game.bean.ClassificationDTO;
import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.bean.GeneralClassificationDTO;
import eu.trentorise.game.bean.IncrementalClassificationDTO;
import eu.trentorise.game.bean.LevelDTO;
import eu.trentorise.game.bean.LevelDTO.ThresholdDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.bean.RuleDTO;
import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.managers.GameManager;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.Rule;
import eu.trentorise.game.model.core.TimeInterval;
import eu.trentorise.game.model.core.TimeUnit;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.task.GeneralClassificationTask;
import eu.trentorise.game.task.IncrementalClassificationTask;

@Component
public class Converter {

	@Autowired
	private GameService gameSrv;

	private static final Logger logger = LoggerFactory.getLogger(Converter.class);

	public GameDTO convertGame(Game game) {
		GameDTO gDTO = null;
		if (game != null) {
			gDTO = new GameDTO();
			gDTO.setId(game.getId());
			gDTO.setActions(game.getActions());
			gDTO.setExpiration(game.getExpiration());
			gDTO.setName(game.getName());
			gDTO.setOwner(game.getOwner());
			gDTO.setDomain(game.getDomain());
            gDTO.setLevels(game.getLevels().stream().map(level -> convert(level))
                    .collect(Collectors.toList()));

			// remove internal actions
			Iterator<String> iter = gDTO.getActions() != null ? gDTO.getActions().iterator() : null;

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
						gDTO.getBadgeCollectionConcept().add((BadgeCollectionConcept) gc);
					}
				}
			}

			if (game.getTasks() != null) {
				gDTO.setClassificationTask(new HashSet<ClassificationDTO>());
				for (GameTask gt : game.getTasks()) {
					ClassificationDTO c = null;
					if (gt instanceof GeneralClassificationTask) {
						c = convertClassificationTask((GeneralClassificationTask) gt);
					} else {
						c = convertClassificationTask(game.getId(), (IncrementalClassificationTask) gt);
					}
					gDTO.getClassificationTask().add(c);
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
		g.setDomain(game.getDomain());
		if (game.getRules() != null) {
			g.setRules(new HashSet<String>());
			for (RuleDTO r : game.getRules()) {
				g.getRules().add(r.getId());
			}
		}
		g.setTerminated(game.isTerminated());

        g.getLevels().addAll(
                game.getLevels().stream().map(dto -> convert(dto)).collect(Collectors.toList()));

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
			for (ClassificationDTO c : game.getClassificationTask()) {
				if (c instanceof GeneralClassificationDTO) {
					g.getTasks().add(convertClassificationTask((GeneralClassificationDTO) c));
				} else {
					g.getTasks().add(convertClassificationTask((IncrementalClassificationDTO) c));

				}
			}
		}

		return g;
	}

	public GeneralClassificationDTO convertClassificationTask(GeneralClassificationTask t) {
		GeneralClassificationDTO task = null;
		if (t != null) {
			task = new GeneralClassificationDTO();
			task.setClassificationName(t.getClassificationName());
			task.setCronExpression(t.getSchedule() != null ? t.getSchedule().getCronExpression() : null);
			task.setItemsToNotificate(t.getItemsToNotificate());
			task.setItemType(t.getItemType());
			task.setName(t.getName());
		}
		return task;
	}

	public GeneralClassificationTask convertClassificationTask(GeneralClassificationDTO t) {
		GeneralClassificationTask task = null;
		if (t != null) {
			task = new GeneralClassificationTask();
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

	public IncrementalClassificationTask convertClassificationTask(IncrementalClassificationDTO t) {
		IncrementalClassificationTask task = null;
		if (t != null) {
			Set<GameConcept> concepts = null;
			if (t.getGameId() != null) {
				concepts = gameSrv.readConceptInstances(t.getGameId());
				PointConcept pc = null;
				for (GameConcept gc : concepts) {
					if (gc instanceof PointConcept && gc.getName().equals(t.getItemType())) {
						pc = (PointConcept) gc;
					}
				}

				if (StringUtils.isBlank(t.getDelayUnit())) {
					task = new IncrementalClassificationTask(pc, t.getPeriodName(), t.getClassificationName());
				} else {
					task = new IncrementalClassificationTask(pc, t.getPeriodName(), t.getClassificationName(),
							new TimeInterval(t.getDelayValue(), TimeUnit.valueOf(t.getDelayUnit())));
				}
				task.setItemsToNotificate(t.getItemsToNotificate());
				task.setName(t.getName());
			} else {
				LogHub.warn(null, logger, "Try to convert IncrementalClassificationDTO with null gameId field");
				throw new IllegalArgumentException("gameId is a required field");
			}
		}
		return task;
	}

	public IncrementalClassificationDTO convertClassificationTask(String gameId,
			IncrementalClassificationTask classification) {
		IncrementalClassificationDTO result = null;
		if (classification != null) {
			result = new IncrementalClassificationDTO();
			result.setClassificationName(classification.getClassificationName());
			result.setItemType(classification.getPointConceptName());
			result.setPeriodName(classification.getPeriodName());
			result.setItemsToNotificate(classification.getItemsToNotificate());
			result.setName(classification.getName());
			result.setGameId(gameId);
			if (classification.getSchedule() != null && classification.getSchedule().getDelay() != null) {
				result.setDelayValue(classification.getSchedule().getDelay().getValue());
				result.setDelayUnit(classification.getSchedule().getDelay().getUnit().toString());
			}
		}

		return result;
	}

	public PlayerState convertPlayerState(PlayerStateDTO ps) {
		PlayerState res = null;
		if (ps != null) {
			res = new PlayerState(ps.getGameId(), ps.getPlayerId());
			res.setCustomData(ps.getCustomData());
			Collection<Set<GameConcept>> concepts = ps.getState().values();
			for (Set<GameConcept> s : concepts) {
				res.getState().addAll(s);
			}
            res.getLevels().addAll(ps.getLevels());
		}

		return res;
	}

	public PlayerStateDTO convertPlayerState(PlayerState ps) {
		PlayerStateDTO res = null;
		if (ps != null) {
			res = new PlayerStateDTO();
			res.setGameId(ps.getGameId());
			res.setPlayerId(ps.getPlayerId());
			res.setCustomData(ps.getCustomData());

			res.setState(new HashMap<String, Set<GameConcept>>());
            // FIXME state is never null in PlayerState by design
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

            res.getLevels().addAll(ps.getLevels());

		}

		return res;
	}

	public TeamState convertTeam(TeamDTO t) {
		TeamState team = null;
		if (t != null) {
			team = new TeamState(t.getGameId(), t.getPlayerId());
			team.setName(t.getName());
			team.setCustomData(t.getCustomData());
			team.setMembers(t.getMembers());
			Collection<Set<GameConcept>> concepts = t.getState().values();
			for (Set<GameConcept> s : concepts) {
				team.getState().addAll(s);
			}
		}

		return team;
	}

	public TeamDTO convertTeam(TeamState t) {
		TeamDTO team = null;
		if (t != null) {
			team = new TeamDTO();
			team.setName(t.getName());
			team.setGameId(t.getGameId());
			team.setCustomData(t.getCustomData());
			team.setMembers(t.getMembers());
			team.setPlayerId(t.getPlayerId());
			if (t.getState() != null) {
				for (GameConcept gc : t.getState()) {
					String conceptType = gc.getClass().getSimpleName();
					Set<GameConcept> gcSet = team.getState().get(conceptType);
					if (gcSet == null) {
						gcSet = new HashSet<GameConcept>();
						team.getState().put(conceptType, gcSet);
					}
					gcSet.add(gc);
				}
			}
		}

		return team;
	}


    public Level convert(LevelDTO level) {
	    Level lev = null;
	    if(level != null) {
	        lev = new Level(level.getName(),level.getPointConceptName());
            lev.getThresholds()
                    .addAll(level.getThresholds().stream()
                            .map(dto -> new Threshold(dto.getName(), dto.getValue())
	        ).collect(Collectors.toList()));
	    }
        return lev;
	}

    public LevelDTO convert(Level level) {
	    LevelDTO levelDTO = null;
        if (level != null) {

            levelDTO = new LevelDTO();
            levelDTO.setName(level.getName());
            levelDTO.setPointConceptName(level.getPointConceptName());

            levelDTO.setThresholds(level.getThresholds().stream().map(thres -> {

                ThresholdDTO dto = new ThresholdDTO();
                dto.setName(thres.getName());
                dto.setValue(thres.getValue());
                return dto;
            }).collect(Collectors.toList()));
        }
        return levelDTO;
	}

    public ChallengeAssignmentDTO convert(ChallengeAssignment challengeAssignment) {
        ChallengeAssignmentDTO dto = null;
        if (challengeAssignment != null) {
            dto = new ChallengeAssignmentDTO();
            dto.setInstanceName(challengeAssignment.getInstanceName());
            dto.setModelName(challengeAssignment.getModelName());
            dto.setData(challengeAssignment.getData());
            dto.setStart(challengeAssignment.getStart());
            dto.setEnd(challengeAssignment.getEnd());
            dto.setState(challengeAssignment.getChallengeType());
            dto.setOrigin(challengeAssignment.getOrigin());
        }
        return dto;
    }

    public ChallengeAssignment convert(ChallengeAssignmentDTO dto) {
        ChallengeAssignment challengeAssignment = null;
        if(dto != null) {
            challengeAssignment = new ChallengeAssignment();
            challengeAssignment.setChallengeType(dto.getState());
            challengeAssignment.setData(dto.getData());
            challengeAssignment.setStart(dto.getStart());
            challengeAssignment.setEnd(dto.getEnd());
            challengeAssignment.setInstanceName(dto.getInstanceName());
            challengeAssignment.setModelName(dto.getModelName());
            challengeAssignment.setOrigin(dto.getOrigin());
        }
        return challengeAssignment;
    }
}
