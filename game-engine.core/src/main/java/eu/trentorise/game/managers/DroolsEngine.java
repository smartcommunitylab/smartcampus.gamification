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
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.command.Command;
import org.kie.api.io.Resource;
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
import eu.trentorise.game.core.Utility;
import eu.trentorise.game.model.Action;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.InputData;
import eu.trentorise.game.model.Member;
import eu.trentorise.game.model.Player;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.Propagation;
import eu.trentorise.game.model.StatsChallengeConcept;
import eu.trentorise.game.model.Team;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.UpdateMembers;
import eu.trentorise.game.model.UpdateTeams;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.FSRule;
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

    private KieServices kieServices = KieServices.Factory.get();

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

        loadGameRules(gameId);

        KieContainer kieContainer =
                kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

        StatelessKieSession kSession = kieContainer.newStatelessKieSession();
        kSession.addEventListener(new LoggingRuleListener(gameId, state.getPlayerId(),
                state.clone(), executionId, executionMoment));

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

        Player player = null;
        if (state instanceof TeamState) {
            player = new Player(state.getPlayerId(), true, ((TeamState) state).getMembers().size());
        } else {
            player = new Player(state.getPlayerId(), false);
        }
        cmds.add(CommandFactory.newInsert(player));

        // filter state removing all ended or completed challenges for the
        // player
        Set<GameConcept> filteredByChallenge = new HashSet<>(state.getState());
        Set<GameConcept> archivedChallenges = new HashSet<>();
        Date now = new Date();
        for (Iterator<GameConcept> iter = filteredByChallenge.iterator(); iter.hasNext();) {
            GameConcept gc = iter.next();
            if (gc instanceof ChallengeConcept) {
                ChallengeConcept challenge = (ChallengeConcept) gc;
                challenge = enrichWithStatsRequiredInfo(challenge, gameId, state.getPlayerId(),
                        executionId);
                if (challenge.isCompleted()
                        || (challenge.getStart() != null && challenge.getStart().after(now))
                        || (challenge.getEnd() != null && challenge.getEnd().before(now))) {
                    iter.remove();
                    archivedChallenges.add(gc);
                }
            }

            // FIXME: inject correct executionMoment for game action, TO IMPROVE
            if (gc instanceof PointConcept) {
                PointConcept pointConcept = (PointConcept) gc;
                pointConcept.setExecutionMoment(executionMoment);
            }
        }

        // ATTENTION: Drools modifies objects inserted in working memory by
        // reference
        cmds.add(CommandFactory.newInsertElements(filteredByChallenge));
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
        Set<GameConcept> newState = new HashSet<GameConcept>(archivedChallenges);

        Iterator<QueryResultsRow> iter =
                ((QueryResults) results.getValue("retrieveState")).iterator();
        while (iter.hasNext()) {
            GameConcept stateElement = (GameConcept) iter.next().get("$result");
            newState.add(stateElement);
            if (stateElement instanceof ChallengeConcept) {
                sendChallengeCompletedNotifications((ChallengeConcept) stateElement, gameId,
                        player.getId(), executionMoment);
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
        // fix for dataset prior than 0.9 version
        state.setCustomData(customData.isEmpty() ? new CustomData() : customData.get(0));

        if (stopWatch != null) {
            stopWatch.stop("game execution", String.format("execution for game %s of player %s",
                    gameId, state.getPlayerId()));
        }
        return state;
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

    private ChallengeConcept enrichWithStatsRequiredInfo(ChallengeConcept challenge, String gameId,
            String playerId, String executionId) {
        ChallengeConcept converted = null;
        if (challenge != null) {
            converted = new StatsChallengeConcept(gameId, playerId, executionId);
            converted.setEnd(challenge.getEnd());
            converted.setStart(challenge.getStart());
            converted.setFields(challenge.getFields());
            converted.setId(challenge.getId());
            converted.setModelName(challenge.getModelName());
            converted.setName(challenge.getName());
        }

        return converted;
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

    private void loadGameRules(String gameId) {
        KieFileSystem kfs = kieServices.newKieFileSystem();
        RuleLoader ruleLoader = new RuleLoader(gameId);
        // load core.drl

        Resource coreRes;
        try {
            coreRes = ruleLoader.load("classpath://rules/core.drl");
            kfs.write(coreRes);
            LogHub.info(gameId, logger, "Core rules loaded");
        } catch (MalformedURLException e) {
            LogHub.info(gameId, logger, "Exception loading core.drl");
        }

        // load rules

        Game game = gameSrv.loadGameDefinitionById(gameId);

        if (game != null && game.getRules() != null) {
            for (String rule : game.getRules()) {
                Resource r1;
                try {
                    r1 = ruleLoader.load(rule);
                    // fix to not load constant file
                    if (r1 != null) {
                        kfs.write(r1);
                        LogHub.debug(gameId, logger, "{} loaded", rule);
                    }
                } catch (MalformedURLException e) {
                    LogHub.error(gameId, logger, "Malformed URL loading rule {}, rule not loaded",
                            rule);
                } catch (RuntimeException e) {
                    LogHub.error(gameId, logger, "Exception loading rule {}", rule);
                }
            }
        }
        kieServices.newKieBuilder(kfs).buildAll();
        LogHub.info(gameId, logger, "Rules repository built");

    }

    private class RuleLoader {
        private String gameId;

        public RuleLoader(String gameId) {
            this.gameId = gameId;
        }

        public boolean isConstantsRule(String ruleUrl) {
            boolean classpathCheck = ruleUrl.startsWith(ClasspathRule.URL_PROTOCOL)
                    && ruleUrl.contains("/constants");
            boolean fsCheck =
                    ruleUrl.startsWith(FSRule.URL_PROTOCOL) && ruleUrl.contains("/constants");
            boolean dbCheck = ruleUrl.startsWith(DBRule.URL_PROTOCOL);
            if (dbCheck) {
                Rule r = gameSrv.loadRule(gameId, ruleUrl);
                dbCheck = r != null && r.getName() != null && r.getName().equals("constants");
            }

            return classpathCheck || fsCheck || dbCheck;
        }

        public Resource load(String ruleUrl) throws MalformedURLException {
            Resource res = null;
            String url = null;
            if (isConstantsRule(ruleUrl)) {
                return null;
            }
            if (ruleUrl.startsWith(ClasspathRule.URL_PROTOCOL)) {
                url = ruleUrl.substring(ClasspathRule.URL_PROTOCOL.length());
                res = kieServices.getResources().newClassPathResource(url);
            } else if (ruleUrl.startsWith(FSRule.URL_PROTOCOL)) {
                url = ruleUrl.substring(FSRule.URL_PROTOCOL.length());
                res = kieServices.getResources().newFileSystemResource(url);
            } else if (ruleUrl.startsWith(DBRule.URL_PROTOCOL)) {
                Rule r = gameSrv.loadRule(gameId, ruleUrl);
                if (r != null) {
                    res = kieServices.getResources()
                            .newReaderResource(new StringReader(((DBRule) r).getContent()));
                    res.setSourcePath(
                            "rules/" + r.getGameId() + "/" + ((DBRule) r).getId() + ".drl");
                } else {
                    LogHub.error(ruleUrl, logger, "DBRule {} not exist", url);
                }
            } else {
                throw new MalformedURLException("resource URL not supported");
            }
            return res;
        }
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
