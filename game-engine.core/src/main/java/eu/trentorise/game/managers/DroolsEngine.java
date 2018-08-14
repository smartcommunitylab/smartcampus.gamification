/**
 * Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package eu.trentorise.game.managers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.LogManager;
import org.drools.core.io.impl.ByteArrayResource;
import org.drools.verifier.Verifier;
import org.drools.verifier.VerifierError;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.kie.api.command.Command;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.command.CommandFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.LoggingRuleListener;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.core.Utility;
import eu.trentorise.game.managers.drools.KieContainerFactory;
import eu.trentorise.game.model.Action;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.InputData;
import eu.trentorise.game.model.Member;
import eu.trentorise.game.model.Player;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.Propagation;
import eu.trentorise.game.model.Team;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.UpdateMembers;
import eu.trentorise.game.model.UpdateTeams;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.model.core.Rule;
import eu.trentorise.game.model.core.UrlRule;
import eu.trentorise.game.notification.ChallengeCompletedNotication;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;

@Component
public class DroolsEngine implements GameEngine {

    private final Logger logger = LoggerFactory.getLogger(DroolsEngine.class);

    @Autowired
    private NotificationManager notificationSrv;

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private Workflow workflow;

    @Autowired
    private KieContainerFactory kieContainerFactory;

    public PlayerState execute(String gameId, PlayerState state, String action,
            Map<String, Object> data, String executionId, long executionMoment,
            List<Object> factObjects) {

        StopWatch stopWatch =
                LogManager.getLogger(StopWatch.DEFAULT_LOGGER_NAME).getAppender("perf-file") != null
                        ? new Log4JStopWatch() : null;
        if (stopWatch != null) {
            stopWatch.start("game execution");
        }

        Game game = gameSrv.loadGameDefinitionById(gameId);
        if (game != null && game.isTerminated()) {
            throw new IllegalArgumentException(String.format("game %s is expired", gameId));
        }

        ConceptHelper conceptHelper = new ConceptHelper();

        KieContainer kieContainer = kieContainerFactory.getContainer(gameId);

        PlayerState stateBeforePlay = state.clone();
        StatelessKieSession kSession = kieContainer.newStatelessKieSession();
        kSession.addEventListener(new LoggingRuleListener(game.getDomain(), gameId,
                state.getPlayerId(), stateBeforePlay, executionId, executionMoment));

        List<Command> cmds = new ArrayList<Command>();

        if (data == null) {
            data = new HashMap<String, Object>();
        }
        cmds.add(CommandFactory.newInsert(new InputData(data)));

        if (!StringUtils.isBlank(action)) {
            cmds.add(CommandFactory.newInsert(new Action(action)));
        }

        if (factObjects != null) {
            cmds.add(CommandFactory.newInsertElements(factObjects));
        }

        cmds.add(CommandFactory.newInsert(new Game(gameId)));

        Player player = new Player(state);
        cmds.add(CommandFactory.newInsert(player));

        // filter state removing all ended or completed challenges for the
        // player
        Set<GameConcept> concepts = new HashSet<>(state.getState());

        concepts = conceptHelper.injectExecutionMoment(concepts, executionMoment);
        concepts = conceptHelper.activateConcepts(concepts);

        Set<GameConcept> activeConcepts = conceptHelper.findActiveConcepts(concepts);
        
        Set<GameConcept> inactiveConcepts =
                new HashSet<>(CollectionUtils.subtract(concepts, activeConcepts));
        
        
        // ATTENTION: Drools modifies objects inserted in working memory by
        // reference
        cmds.add(CommandFactory.newInsertElements(activeConcepts));
        cmds.add(CommandFactory.newInsert(state.getCustomData()));
        cmds.add(CommandFactory.newFireAllRules());

        // queries
        cmds.add(CommandFactory.newQuery("retrieveState", "getGameConcepts"));
        cmds.add(CommandFactory.newQuery("retrieveNotifications", "getNotifications"));
        cmds.add(CommandFactory.newQuery("retrieveCustomData", "getCustomData"));
        cmds.add(CommandFactory.newQuery("retrieveUpdateTeams", "getUpdateTeams"));
        cmds.add(CommandFactory.newQuery("retrieveUpdateMembers", "getUpdateMembers"));
        cmds.add(CommandFactory.newQuery("retrieveLevel", "getLevel"));
        cmds.add(CommandFactory.newQuery("retrieveMember", "getMember"));

        // set gameId as constant
        kSession.setGlobal("utils", new Utility(gameId));

        kSession = loadGameConstants(kSession, gameId);

        ExecutionResults results = kSession.execute(CommandFactory.newBatchExecution(cmds));

        // new state contains archived challenges and all GameConcept
        // loaded in engine session
        Set<GameConcept> newState = new HashSet<GameConcept>(inactiveConcepts);

        Iterator<QueryResultsRow> iter =
                ((QueryResults) results.getValue("retrieveState")).iterator();
        while (iter.hasNext()) {
            GameConcept stateElement = (GameConcept) iter.next().get("$result");
            newState.add(stateElement);
            if (stateElement instanceof ChallengeConcept) {
                ChallengeConcept challenge = (ChallengeConcept) stateElement;

                // normalize state, useful when action come from the past
                challenge.normalizeState();

                sendChallengeCompletedNotifications(challenge, gameId, player.getId(),
                        executionMoment);
                logCompletedChallenge(game.getDomain(), gameId, executionId, executionMoment,
                        player, challenge);
            }
        }

        List<CustomData> customData = new ArrayList<CustomData>();

        iter = ((QueryResults) results.getValue("retrieveCustomData")).iterator();
        while (iter.hasNext()) {
            CustomData stateCustomData = (CustomData) iter.next().get("$data");
            customData.add(stateCustomData);
        }

        iter = ((QueryResults) results.getValue("retrieveNotifications")).iterator();
        while (iter.hasNext()) {
            Notification note = (Notification) iter.next().get("$notifications");
            notificationSrv.notificate(note);
            LogHub.info(gameId, logger, "send notification: {}", note.toString());
        }

        iter = ((QueryResults) results.getValue("retrieveUpdateTeams")).iterator();

        if (iter.hasNext()) {
            Set<Object> facts = new HashSet<>();
            Iterator<QueryResultsRow> iter1 = null;
            while (iter.hasNext()) {
                UpdateTeams updateCalls = (UpdateTeams) iter.next().get("$data");
                iter1 = ((QueryResults) results.getValue("retrieveLevel")).iterator();
                int level = 1;
                if (iter1.hasNext()) {
                    level = (int) iter1.next().get("$data");
                    level++;
                }
                facts.add(new Propagation(updateCalls.getPropagationAction(), level));
            }

            List<TeamState> playerTeams = playerSrv.readTeams(gameId, state.getPlayerId());
            LogHub.info(gameId, logger, "Player {} belongs to {} teams", state.getPlayerId(),
                    playerTeams.size());
            if (playerTeams.size() > 0) {
                LogHub.info(gameId, logger, "call for update with data {}", data);
            }

            iter1 = ((QueryResults) results.getValue("retrieveMember")).iterator();
            Member fromPropagation = null;
            if (iter1.hasNext()) {
                fromPropagation = (Member) iter1.next().get("$data");
            }
            Map<String, Object> payloadData = new HashMap<>(data);
            if (fromPropagation != null && fromPropagation.getInputData() != null) {
                payloadData.putAll(fromPropagation.getInputData());
            }
            facts.add(new Member(state.getPlayerId(), payloadData));
            for (TeamState team : playerTeams) {
                workflow.apply(gameId, action, team.getPlayerId(), executionMoment, null,
                        new ArrayList<>(facts));
            }
        }
        iter = ((QueryResults) results.getValue("retrieveUpdateMembers")).iterator();
        if (iter.hasNext()) {
            Set<Object> facts = new HashSet<>();
            while (iter.hasNext()) {
                UpdateMembers updateCalls = (UpdateMembers) iter.next().get("$data");
                facts.add(new Propagation(updateCalls.getPropagationAction()));
            }
            // check if a propagation to team members is needed
            try {
                TeamState team = playerSrv.readTeam(gameId, state.getPlayerId());
                List<String> members = team.getMembers();
                facts.add(new Team(state.getPlayerId(), data));
                LogHub.info(gameId, logger, "Team {} has {} members", state.getPlayerId(),
                        members.size());
                for (String member : members) {
                    workflow.apply(gameId, action, member, executionMoment, null,
                            new ArrayList<>(facts));
                }
            } catch (ClassCastException e) {
                LogHub.info(gameId, logger,
                        "{} is not a team, there is no propagation to team members",
                        state.getPlayerId());
            }

        }

        state.setState(newState);

        List<PlayerLevel> playerLevels = gameSrv.calculateLevels(gameId, state);
        state = state.updateLevels(playerLevels);
        logLevelStatus(gameId, playerLevels);

        state.updateInventory(game);

        // fix for dataset prior than 0.9 version
        state.setCustomData(customData.isEmpty() ? new CustomData() : customData.get(0));

        if (stopWatch != null) {
            stopWatch.stop("game execution", String.format("execution for game %s of player %s",
                    gameId, state.getPlayerId()));
        }
        return state;
    }

    private void logLevelStatus(String gameId, List<PlayerLevel> levels) {
        if (levels != null && !levels.isEmpty()) {
        StringBuffer levelStatus = new StringBuffer();
        for (PlayerLevel lev : levels) {
                if (levelStatus.length() != 0) {
                    levelStatus.append(",");
                }
                levelStatus.append(String.format("{levelName=%s,levelValue=%s,toNextLevel=%s}",
                    lev.getLevelName(), lev.getLevelValue(), lev.getToNextLevel()));
        }
            LogHub.info(gameId, logger, "Level status: " + levelStatus.toString());
        }


    }
    private void logCompletedChallenge(String domain, String gameId, String executionId,
            long executionMoment, Player player, ChallengeConcept challenge) {
        if (challenge != null && challenge.isCompleted()) {
            StatsLogger.logChallengeCompleted(domain, gameId, player.getId(), executionId,
                    executionMoment, System.currentTimeMillis(), challenge.getName());
        }
    }

    private void sendChallengeCompletedNotifications(ChallengeConcept stateElement, String gameId,
            String playerId, long executionMoment) {
        if (stateElement.isCompleted()) {
            ChallengeCompletedNotication challengeNotification = new ChallengeCompletedNotication();
            challengeNotification.setGameId(gameId);
            challengeNotification.setPlayerId(playerId);
            challengeNotification.setChallengeName(stateElement.getName());
            challengeNotification.setTimestamp(executionMoment);
            notificationSrv.notificate(challengeNotification);
            LogHub.info(gameId, logger, "send notification: {}", challengeNotification.toString());
        }
    }


    private StatelessKieSession loadGameConstants(StatelessKieSession kSession, String gameId) {

        // load game constants
        InputStream constantsFileStream = null;
        Game g = gameSrv.loadGameDefinitionById(gameId);
        if (g != null && g.getRules() != null) {
            for (String ruleUrl : g.getRules()) {
                Rule r = gameSrv.loadRule(gameId, ruleUrl);
                if ((r != null && r.getName() != null && r.getName().equals("constants"))
                        || r instanceof UrlRule && ((UrlRule) r).getUrl().contains("constants")) {
                    try {
                        constantsFileStream = r.getInputStream();
                    } catch (IOException e) {
                        LogHub.error(gameId, logger, "Exception loading constants file", e);
                    }
                }
            }
        }

        if (constantsFileStream != null) {
            try {
                PropertiesConfiguration constants = new PropertiesConfiguration();
                constants.load(constantsFileStream);
                constants.setListDelimiter(',');
                LogHub.debug(gameId, logger, "constants file loaded for game {}", gameId);
                Iterator<String> constantsIter = constants.getKeys();
                while (constantsIter.hasNext()) {
                    String constant = constantsIter.next();
                    Object value = numberConversion(constants.getProperty(constant));
                    kSession.setGlobal(constant, value);
                    if (logger.isDebugEnabled()) {
                        List<Object> listValue = constants.getList(constant);
                        if (listValue.isEmpty()) {
                            LogHub.debug(gameId, logger, "constant {} loaded: {}", constant, value);
                        } else {
                            LogHub.debug(gameId, logger, "constant {} loaded: {}, size: {}",
                                    constant, listValue, listValue.size());
                        }
                    }
                }
            } catch (ConfigurationException e) {
                LogHub.error(gameId, logger, "constants loading exception");
            }
        } else {
            LogHub.info(gameId, logger, "Rule constants file not found");
        }
        return kSession;
    }

    private Object numberConversion(Object value) {

        if (value instanceof String) {
            String converted = (String) value;
            if (NumberUtils.isNumber(converted) && converted.toLowerCase().contains("l")) {
                return new Long(converted.substring(0, converted.length() - 1));
            }
            if (NumberUtils.isNumber(converted) && !converted.contains(".")) {
                return new Integer(converted);
            }
            if (NumberUtils.isNumber(converted) && converted.contains(".")) {
                return new Double(converted);
            }
        }

        return value;
    }


    @Override
    public List<String> validateRule(String gameId, String content) {
        List<String> result = new ArrayList<String>();
        if (content != null) {
            VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
            // Check that the builder works.
            if (!vBuilder.hasErrors()) {
                Verifier verifier = vBuilder.newVerifier();
                verifier.addResourcesToVerify(
                        new ByteArrayResource(content.getBytes()).setTargetPath("/t.drl"),
                        ResourceType.DRL);
                for (VerifierError err : verifier.getErrors()) {
                    result.add(err.getMessage());
                }
            } else {
                LogHub.error(gameId, logger, "Drools verifier instantiation exception");
            }
        }
        return result;
    }
}
