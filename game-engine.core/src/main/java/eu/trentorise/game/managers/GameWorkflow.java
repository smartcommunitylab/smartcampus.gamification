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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TraceService;
import eu.trentorise.game.services.Workflow;

@Component
public class GameWorkflow implements Workflow {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(GameWorkflow.class);

    @Autowired
    protected GameEngine gameEngine;

    @Autowired
    protected PlayerService playerSrv;

    @Autowired
    protected GameService gameSrv;

    @Autowired
    private TraceService traceSrv;

    @Autowired
    private ChallengeManager challengeSrv;

    @Autowired
    private Environment env;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    protected void workflowExec(String gameId, String actionId, String userId, String executionId,
            long executionMoment, Map<String, Object> data, List<Object> factObjects) {
        final Date executionDate = new Date(executionMoment);
        LogHub.info(gameId, logger,
                "gameId:{}, actionId: {}, playerId: {}, executionMoment: {}, data: {}, factObjs: {}",
                gameId, actionId, userId, dateFormat.format(executionDate), data,
                factObjects);
        Game g = gameSrv.loadGameDefinitionById(gameId);
        if (g == null || g.getActions() == null
                || (!actionId.startsWith(GameManager.INTERNAL_ACTION_PREFIX)
                        && !g.getActions().contains(actionId))) {
            throw new IllegalArgumentException(String
                    .format("game %s not exist or action %s not belong to it", gameId, actionId));
        }

        PlayerState playerState = playerSrv.loadState(gameId, userId, true, false);

        // Actually GameService.execute modifies playerState passed as parameter
        PlayerState oldState = playerState.clone();

        if (isClassificationAction(actionId)) {
            StatsLogger.logClassification(g.getDomain(), gameId, userId, executionId,
                    executionMoment, data, factObjects);
        } else {
            StatsLogger.logAction(g.getDomain(), gameId, userId, executionId, executionMoment,
                    actionId, data, factObjects, playerState);
        }
        PlayerState newState = gameEngine.execute(gameId, playerState, actionId, data, executionId,
                executionMoment, factObjects);

        boolean result = playerSrv.saveState(newState) != null;

        // update score of all player active groupChallenges

        List<GroupChallenge> playerActiveGroupChallenges =
                challengeSrv.activeGroupChallengesByDate(gameId, userId, executionDate);
        if (playerActiveGroupChallenges.size() > 0) {
            LogHub.info(gameId, logger, String.format("Player %s has %s active group challenges",
                    userId, playerActiveGroupChallenges.size()));
        } else {
            LogHub.info(gameId, logger, String.format("Player %s has no active group challenges",
                    userId, playerActiveGroupChallenges.size()));
        }
        playerActiveGroupChallenges.forEach(groupChallenge -> {
            List<PlayerState> participantStates = groupChallenge.getAttendees().stream()
                    .filter(a -> !a.getPlayerId().equals(userId)).map(participant -> playerSrv
                            .loadState(gameId, participant.getPlayerId(), false, false))
                    .collect(Collectors.toList());
            participantStates.add(newState);
            groupChallenge.update(participantStates, executionMoment);
            challengeSrv.save(groupChallenge);
            if (groupChallenge.getChallengeModel()
                    .equals(GroupChallenge.MODEL_NAME_COMPETITIVE_TIME)
                    || groupChallenge.getChallengeModel()
                            .equals(GroupChallenge.MODEL_NAME_COOPERATIVE)) {
                List<String> winners = challengeSrv.conditionCheck(groupChallenge);
                if (!winners.isEmpty()) {
                    groupChallenge.updateState(ChallengeState.COMPLETED, executionDate);
                    challengeSrv.save(groupChallenge);
                    challengeSrv.sendChallengeNotification(groupChallenge);
                    challengeSrv.logStatsEvents(g, groupChallenge);
                    LogHub.info(gameId, logger,
                            String.format(
                                    "Player %s wins group challenge %s of type %s, he will be rewarded",
                                    userId, groupChallenge.getInstanceName(),
                                    groupChallenge.getChallengeModel()));
                    winners.stream().forEach(w -> {
                        apply(gameId, GameManager.INTERNAL_ACTION_PREFIX + "reward", w,
                                executionMoment, null, Arrays.asList(groupChallenge.getReward()));
                    });
                }
            }
        });


        if (env.getProperty("trace.playerMove", Boolean.class, false)) {
            traceSrv.tracePlayerMove(oldState, newState, data, executionMoment);
            LogHub.info(gameId, logger, "Traced player {} move", userId);
        }
        LogHub.info(gameId, logger, "Process terminated: {}", result);
        StatsLogger.logEndGameAction(g.getDomain(), gameId, userId, executionId, executionMoment,
                System.currentTimeMillis());
    }

    private boolean isClassificationAction(String actionId) {
        return actionId != null && "scogei_classification".equals(actionId);
    }

    public void apply(String gameId, String actionId, String userId, Map<String, Object> data,
            List<Object> factObjects) {
        String executionId = generateExecutionId();
        long executionMoment = System.currentTimeMillis();
        workflowExec(gameId, actionId, userId, executionId, executionMoment, data, factObjects);

    }

    @Override
    public void apply(String gameId, String actionId, String playerId, long executionMoment,
            Map<String, Object> data, List<Object> factObjects) {
        String executionId = generateExecutionId();
        workflowExec(gameId, actionId, playerId, executionId, executionMoment, data, factObjects);
    }

    private String generateExecutionId() {
        return UUID.randomUUID().toString();
    }

}
