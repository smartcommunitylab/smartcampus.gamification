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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.ResourceNotFoundException;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.Inventory;
import eu.trentorise.game.model.Inventory.ItemChoice;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.PlayerBlackList;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.ClassificationBoard;
import eu.trentorise.game.model.core.ClassificationPosition;
import eu.trentorise.game.model.core.ClassificationType;
import eu.trentorise.game.model.core.ComplexSearchQuery;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.model.core.RawSearchQuery;
import eu.trentorise.game.model.core.StringSearchQuery;
import eu.trentorise.game.notification.ChallengeAssignedNotification;
import eu.trentorise.game.notification.ChallengeInvitationCanceledNotification;
import eu.trentorise.game.notification.ChallengeInvitationRefusedNotification;
import eu.trentorise.game.notification.ChallengeProposedNotification;
import eu.trentorise.game.repo.ChallengeConceptPersistence;
import eu.trentorise.game.repo.ChallengeConceptRepo;
import eu.trentorise.game.repo.GenericObjectPersistence;
import eu.trentorise.game.repo.GroupChallengeRepo;
import eu.trentorise.game.repo.PlayerRepo;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.repo.TeamPersistence;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@Component
public class DBPlayerManager implements PlayerService {

    private final static Logger logger = LoggerFactory.getLogger(DBPlayerManager.class);

    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GameService gameSrv;

    @Autowired
	private ChallengeConceptRepo challengeConceptRepo;

    @Autowired
    private GroupChallengeRepo groupChallengeRepo;

    @Autowired
    private NotificationManager notificationSrv;

    @Autowired
    private ArchiveManager archiveSrv;
    
    private static final int PROPOSER_RANGE = 2;

    public PlayerState loadState(String gameId, String playerId, boolean upsert, boolean mergeGroupChallenges) {
        return loadState(gameId, playerId, upsert, mergeGroupChallenges, false);
    }

    public PlayerState loadState(String gameId, String playerId, boolean upsert,
            boolean mergeChallenges, boolean filterHiddenChallenges) {
        eu.trentorise.game.repo.StatePersistence state =
                playerRepo.findByGameIdAndPlayerId(gameId, playerId);
        PlayerState res = state == null ? (upsert ? new PlayerState(gameId, playerId) : null)
                : isTeam(state) ? new TeamState(state) : new PlayerState(state);
              
        res = initDefaultLevels(initConceptsStructure(res, gameId), gameId);
        if (mergeChallenges) {
        	List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, playerId);
    		res.loadChallengeConcepts(listCcs);
            res = mergeGroupChallenges(res, gameId);
            
            if (filterHiddenChallenges) {
                res = filterHiddenChallenges(res);
            }
        }       

        return res;
    }

    private PlayerState filterHiddenChallenges(PlayerState state) {
        Stream<GameConcept> conceptNotChallenges = state.getState().stream()
                .filter(concept -> concept.getClass() != ChallengeConcept.class);
        Stream<ChallengeConcept> publicChallenges = state.getState().stream()
                .filter(concept -> concept.getClass() == ChallengeConcept.class)
                .map(concept -> (ChallengeConcept) concept)
                .filter(challenge -> !challenge.isHidden());

        state.setState(
                Stream.concat(conceptNotChallenges, publicChallenges).collect(Collectors.toSet()));

        return state;
    }

    public PlayerState saveState(PlayerState state) {
        PlayerState saved = null;
        if (state != null) {
            StatePersistence toSave = null;
            state = removeGroupChallenges(state);
            if (state instanceof TeamState) {
                toSave = new TeamPersistence((TeamState) state);
                saved = new TeamState(persist(toSave));
            } else {
                toSave = new StatePersistence(state);
                saved = new PlayerState(persist(toSave));
            }
        }
        return saved;
    }

    private PlayerState removeGroupChallenges(PlayerState state) {
        if (state != null) {
            Iterator<GameConcept> iter = state.getState().iterator();
            while (iter.hasNext()) {
                GameConcept elem = iter.next();
                if (elem.getClass() == ChallengeConcept.class
                        && ((ChallengeConcept) elem).isGroupChallenge()) {
                    iter.remove();
                }


            }
        }
        return state;
    }

    private PlayerState mergeGroupChallenges(PlayerState state, String gameId) {
        if (state != null) {
            List<GroupChallenge> groupChallenges =
                    groupChallengeRepo.playerGroupChallenges(gameId, state.getPlayerId());
            state.getState()
                    .addAll(groupChallenges.stream()
                            .map(groupChallenge -> convertToChallengeConcept(state.getPlayerId(),
                                    groupChallenge))
                            .collect(Collectors.toList()));
        }
        return state;
    }

    private ChallengeConcept convertToChallengeConcept(String playerId,
            GroupChallenge groupChallenge) {
        return groupChallenge != null ? groupChallenge.toChallengeConcept(playerId) : null;
    }
    private boolean isTeam(StatePersistence state) {
        return state != null && state.getMetadata().get(TeamState.NAME_METADATA) != null
                && state.getMetadata().get(TeamState.MEMBERS_METADATA) != null;
    }

    private StatePersistence persist(StatePersistence state) {
        return persist(state.getGameId(), state.getPlayerId(), state.getConcepts(),
                state.getLevels(), state.getInventory(), state.getCustomData(),
                state.getMetadata());
    }

    private StatePersistence persistConcepts(String gameId, String playerId,
            Map<String, Map<String, GenericObjectPersistence>> concepts) {
        return persist(gameId, playerId, concepts, null, null, null, null);
    }

    @SuppressWarnings("unused")
    private StatePersistence persistCustomData(String gameId, String playerId, CustomData data) {
        return persist(gameId, playerId, null, null, null, data, null);
    }

    private StatePersistence persistCustomData(String gameId, String playerId,
            Map<String, Object> customData) {
        CustomData c = new CustomData();
        c.putAll(customData);
        return persist(gameId, playerId, null, null, null, c, null);
    }

    @SuppressWarnings("unused")
    private StatePersistence persistMetadata(String gameId, String playerId,
            Map<String, Object> metadata) {
        return persist(gameId, playerId, null, null, null, null, metadata);
    }

    private StatePersistence persist(String gameId, String playerId,
            Map<String, Map<String, GenericObjectPersistence>> concepts, List<PlayerLevel> levels,
            Inventory inventory, CustomData customData, Map<String, Object> metadata) {
        if (StringUtils.isBlank(gameId) || StringUtils.isBlank(playerId)) {
            throw new IllegalArgumentException(
                    "field gameId and playerId of PlayerState MUST be set");
        }

        Criteria criteria = new Criteria();
        criteria = criteria.and("gameId").is(gameId).and("playerId").is(playerId);
        Query query = new Query(criteria);
        Update update = new Update();
        if (concepts != null) {
        	concepts = persistChallengeConcept(concepts, gameId, playerId);
            update.set("concepts", concepts);
        }
        if (levels != null) {
            update.set("levels", levels);
        }
        if (inventory != null) {
            update.set("inventory", inventory);
        }
        if (customData != null) {
            update.set("customData", customData);
        }
        if (metadata != null) {
            update.set("metadata", metadata);
        }
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        return mongoTemplate.findAndModify(query, update, options, StatePersistence.class);
    }

    public List<String> readPlayers(String gameId) {
        List<StatePersistence> states = playerRepo.findByGameId(gameId);
        List<String> result = new ArrayList<String>();
        for (StatePersistence state : states) {
            result.add(state.getPlayerId());
        }

        return result;
    }

    @Override
    public Page<String> readPlayers(String gameId, Pageable pageable) {
        Page<StatePersistence> states = playerRepo.findByGameId(gameId, pageable);
        List<String> result = new ArrayList<String>();
        for (StatePersistence state : states) {
            result.add(state.getPlayerId());
        }
        PageImpl<String> res = new PageImpl<String>(result, pageable, states.getTotalElements());
        return res;
    }

    public Page<PlayerState> loadStates(String gameId, Pageable pageable,
            boolean mergeGroupChallenges, boolean filterHiddenChallenges) {
        StopWatch stopWatch =
                LogManager.getLogger(StopWatch.DEFAULT_LOGGER_NAME).getAppender("perf-file") != null
                        ? new Log4JStopWatch() : null;
        if (stopWatch != null) {
            stopWatch.start("loadStates");
        }
        Page<StatePersistence> states = playerRepo.findByGameId(gameId, pageable);
        List<PlayerState> result = new ArrayList<PlayerState>();
        for (StatePersistence state : states) {
            PlayerState playerState = initDefaultLevels(
                    initConceptsStructure(new PlayerState(state), gameId), gameId);
            if (mergeGroupChallenges) {
            	playerState = mergeGroupChallenges(playerState, gameId);
            	List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, playerState.getPlayerId());
            	playerState.loadChallengeConcepts(listCcs);
            }
            if (filterHiddenChallenges) {
                playerState = filterHiddenChallenges(playerState);
            }
            result.add(playerState);
        }
        PageImpl<PlayerState> res =
                new PageImpl<PlayerState>(result, pageable, states.getTotalElements());
        if (stopWatch != null) {
            stopWatch.stop("loadStates", "Loaded states of game " + gameId);
        }
        return res;
    }

    public Page<PlayerState> loadStates(String gameId, Pageable pageable,
            boolean mergeGroupChallenges) {
        return loadStates(gameId, pageable, mergeGroupChallenges, false);
    }

    // TODO: method use only by a test, investigate
    @Override
    public List<PlayerState> loadStates(String gameId) {
        List<StatePersistence> states = playerRepo.findByGameId(gameId);
        List<PlayerState> result = new ArrayList<PlayerState>();
        for (StatePersistence state : states) {
            result.add(initDefaultLevels(initConceptsStructure(new PlayerState(state), gameId),
                    gameId));
        }

        return result;
    }

    public Page<PlayerState> loadStates(String gameId, String playerId, Pageable pageable,
            boolean mergeGroupChallenges, boolean filterHiddenChallenges) {
        Page<StatePersistence> states =
                playerRepo.findByGameIdAndPlayerIdLike(gameId, playerId, pageable);
        List<PlayerState> result = new ArrayList<PlayerState>();
        for (StatePersistence state : states) {
            PlayerState playerState = initDefaultLevels(
                    initConceptsStructure(new PlayerState(state), gameId), gameId);
            if (mergeGroupChallenges) {
            	playerState = mergeGroupChallenges(playerState, gameId);
            	List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, playerId);
            	playerState.loadChallengeConcepts(listCcs); 
                
            }
            if (filterHiddenChallenges) {
                playerState = filterHiddenChallenges(playerState);
            }
            result.add(playerState);
        }
        PageImpl<PlayerState> res =
                new PageImpl<PlayerState>(result, pageable, states.getTotalElements());
        return res;
    }

    @Override
    public Page<PlayerState> loadStates(String gameId, String playerId, Pageable pageable,
            boolean mergeGroupChallenges) {
        return loadStates(gameId, playerId, pageable, mergeGroupChallenges, false);
    }

    private PlayerState initConceptsStructure(PlayerState ps, String gameId) {
        if (ps != null) {
            Game g = gameSrv.loadGameDefinitionById(gameId);
            if (ps.getState() == null) {
                ps.setState(new HashSet<GameConcept>());
            }
            if (g != null) {
                List<GameConcept> toAppend = new ArrayList<GameConcept>();
                if (g.getConcepts() != null) {
                    for (GameConcept gc : g.getConcepts()) {
                        boolean found = false;
                        for (GameConcept pgc : ps.getState()) {
                            found = gc.getName().equals(pgc.getName())
                                    && gc.getClass().equals(pgc.getClass());
                            if (found) {
                                break;
                            }
                        }
                        if (!found) {
                            toAppend.add(gc);
                        }
                    }
                }
                ps.getState().addAll(toAppend);
            }
        }
        return ps;
    }

    private PlayerState initDefaultLevels(PlayerState ps, String gameId) {
        if (ps != null) {
            Game g = gameSrv.loadGameDefinitionById(gameId);
            if (g != null && ps.getLevels().isEmpty()) {
                List<Level> levelDefinitions = g.getLevels();
                levelDefinitions.stream().forEach(definition -> {
                    List<Threshold> levelThresholds = g.getLevelThresholds(definition.getName());
                    if (!levelThresholds.isEmpty() && levelThresholds.get(0).getValue() == 0) {
                        ps.getLevels().add(new PlayerLevel(definition, 0));
                    }
                });
            }
        }
        return ps;
    }

    @Override
    public TeamState saveTeam(TeamState team) {
        return (TeamState) saveState(team);
    }

    @Override
    public List<TeamState> readTeams(String gameId) {
        List<StatePersistence> result = playerRepo.findTeamsByGameId(gameId);
        List<TeamState> converted = new ArrayList<>();
        for (StatePersistence sp : result) {
            converted.add(new TeamState(sp));
        }

        return converted;
    }

    @Override
    public void deleteState(String gameId, String playerId) {
        playerRepo.deleteByGameIdAndPlayerId(gameId, playerId);
    }

    @Override
    public List<TeamState> readTeams(String gameId, String playerId) {
        List<StatePersistence> result = playerRepo.findTeamByMemberId(gameId, playerId);
        List<TeamState> converted = new ArrayList<>();
        for (StatePersistence sp : result) {
            converted.add(new TeamState(sp));
        }

        return converted;
    }

    @Override
    public TeamState addToTeam(String gameId, String teamId, String playerId) {
        StatePersistence state = playerRepo.findByGameIdAndPlayerId(gameId, teamId);
        if (state != null) {
            List<String> members =
                    (List<String>) state.getMetadata().get(TeamState.MEMBERS_METADATA);
            if (members != null) {
                members.add(playerId);
                state.getMetadata().put(TeamState.MEMBERS_METADATA, members);
                playerRepo.save(state);
            }
            return new TeamState(state);
        }

        return null;

    }

    @Override
    public TeamState removeFromTeam(String gameId, String teamId, String playerId) {
        StatePersistence state = playerRepo.findByGameIdAndPlayerId(gameId, teamId);
        if (state != null) {
            List<String> members =
                    (List<String>) state.getMetadata().get(TeamState.MEMBERS_METADATA);
            if (members != null) {
                members.remove(playerId);
                state.getMetadata().put(TeamState.MEMBERS_METADATA, members);
                playerRepo.save(state);
            }
        }

        return new TeamState(state);
    }

    @Override
    public TeamState readTeam(String gameId, String teamId) {
        return (TeamState) loadState(gameId, teamId, false, false);
    }

    @Override
    public PlayerState updateCustomData(String gameId, String playerId, Map<String, Object> data) {
        // findAndModify only customdata to avoid concurrent accesses on same
        // data
        StatePersistence state = persistCustomData(gameId, playerId, data);
        return new PlayerState(state);

    }

    @Override
    public ChallengeConcept assignChallenge(String gameId, String playerId,
            ChallengeAssignment challengeAssignment) {

        Map<String, Object> data = challengeAssignment.getData();
        if (playerId == null) {
            throw new IllegalArgumentException("playerId cannot be null");
        }

        if (challengeAssignment.getModelName() == null) {
            throw new IllegalArgumentException("modelName cannot be null");
        }

        Optional<ChallengeModel> model = gameSrv.readChallengeModelByName(gameId, challengeAssignment.getModelName());

        if (data == null) {
            data = new HashMap<String, Object>();
        } else {
            List<String> invalidFields = model.map(m -> m.invalidFields(challengeAssignment))
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("model %s not exist in game %s",
                                    challengeAssignment.getModelName(), gameId)));
            if (!invalidFields.isEmpty()) {
                throw new IllegalArgumentException(String.format("field %s not present in model %s",
                        invalidFields.get(0), challengeAssignment.getModelName()));
            }
        }


        ChallengeConcept challenge = null;
        try {
            challenge = new ChallengeConcept(
                    convertToChallengeState(challengeAssignment.getChallengeType()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    String.format("You cannot create a challenge with state %s",
                            challengeAssignment.getChallengeType()));
        }
        challenge.setModelName(challengeAssignment.getModelName());
        challenge.setFields(data);
        challenge.setStart(challengeAssignment.getStart());
        challenge.setEnd(challengeAssignment.getEnd());
        // needed since v2.2.0, gameConcept name is mandatory because it is used
        // as key in persistence structure
        challenge.setName(challengeAssignment.getInstanceName() != null
                ? challengeAssignment.getInstanceName() : UUID.randomUUID().toString());
        challenge.setOrigin(challengeAssignment.getOrigin());
        challenge.setPriority(challengeAssignment.getPriority());

        challenge.getVisibility().setHidden(challengeAssignment.isHide());

        if (challenge.getVisibility().isHidden()) {
            Game game = gameSrv.loadGameDefinitionById(gameId);
            Date now = new Date();
            Date disclosureDate = game.nextChallengeDisclosureDate(now);
            challenge.getVisibility().setDisclosureDate(disclosureDate);
        }

        // save in playerState
        PlayerState state = loadState(gameId, playerId, true, false);

        state.getState().add(challenge);
        persistConcepts(gameId, playerId, new StatePersistence(state).getConcepts());

        if (challenge.getVisibility().isHidden()) {
            LogHub.info(gameId, logger,
                    "challenge {} is hidden [disclosure date: {}], notification will be not send",
                    challenge.getName(),
                    LogHub.logDate(challenge.getVisibility().getDisclosureDate()).orElse("-"));
        } else {
            Notification challengeNotification = null;
            if (challenge.getState() == ChallengeState.ASSIGNED) {
                ChallengeAssignedNotification challengeAssignedNotification =
                        new ChallengeAssignedNotification();
                challengeAssignedNotification.setChallengeName(challenge.getName());
                challengeAssignedNotification.setGameId(gameId);
                challengeAssignedNotification.setPlayerId(playerId);
                challengeAssignedNotification.setStartDate(challengeAssignment.getStart());
                challengeAssignedNotification.setEndDate(challengeAssignment.getEnd());
                challengeNotification = challengeAssignedNotification;
            } else {
                ChallengeProposedNotification challengeProposedNotification =
                        new ChallengeProposedNotification();
                challengeProposedNotification.setChallengeName(challenge.getName());
                challengeProposedNotification.setGameId(gameId);
                challengeProposedNotification.setPlayerId(playerId);
                challengeProposedNotification.setStartDate(challengeAssignment.getStart());
                challengeProposedNotification.setEndDate(challengeAssignment.getEnd());
                challengeNotification = challengeProposedNotification;
            }
            notificationSrv.notificate(challengeNotification);
            LogHub.info(gameId, logger, "send notification: {}", challengeNotification.toString());
        }
        Game game = gameSrv.loadGameDefinitionById(gameId);
        if (challenge.getState() == ChallengeState.ASSIGNED) {
            StatsLogger.logChallengeAssignment(game.getDomain(), gameId, playerId,
                    UUID.randomUUID().toString(), System.currentTimeMillis(), challenge.getName(),
                    challengeAssignment.getStart(), challengeAssignment.getEnd());
        } else if (challenge.getState() == ChallengeState.PROPOSED) {
            StatsLogger.logChallengeProposed(game.getDomain(), gameId, playerId,
                    UUID.randomUUID().toString(), System.currentTimeMillis(),
                    System.currentTimeMillis(), challenge.getName());
        }

        return challenge;

    }

    private ChallengeState convertToChallengeState(String challengeType) {
        if (challengeType == null) {
            return null;
        } else {
            return ChallengeState.valueOf(challengeType.toUpperCase());
        }
    }

    // public ClassificationBoard classifyPlayerStatesWithKey(long timestamp,
    // String pointConceptName, String periodName,
    // String key, String gameId, int pageNum, int pageSize) {
    @Override
    public ClassificationBoard classifyPlayerStatesWithKey(long timestamp, String pointConceptName,
            String periodName, String key, String gameId, Pageable pageable) {

        ClassificationBoard classificationBoard = new ClassificationBoard();
        String field = "concepts.PointConcept." + pointConceptName
                + ".obj.periods." + periodName + ".instances." + key + ".score";
        Criteria criteria = Criteria
        		.where("gameId").is(gameId).and(field).gt(0);

        Query query = new Query();
        // criteria.
        query.addCriteria(criteria);
//        query.with(new Sort(Sort.Direction.DESC, field));
        query.with(Sort.by(Sort.Order.desc(field)));
        // fields in response.
        query.fields().include(field);
        query.fields().include("playerId");
        // pagination.
        query.with(pageable);
        logger.info("Classification query " + query);

        List<StatePersistence> pStates = mongoTemplate.find(query, StatePersistence.class);

        List<ClassificationPosition> classification = new ArrayList<ClassificationPosition>();
        for (StatePersistence state : pStates) {
            classification.add(new ClassificationPosition(
                    state.getIncrementalScore(pointConceptName, periodName, key),
                    state.getPlayerId()));
        }
        classificationBoard.setBoard(classification);
        classificationBoard.setType(ClassificationType.INCREMENTAL);
        classificationBoard.setPointConceptName(pointConceptName);

        return classificationBoard;
    }

    @Override
    public ClassificationBoard classifyAllPlayerStates(Game g, String itemType, Pageable pageable) {

        ClassificationBoard classificationBoard = new ClassificationBoard();
        List<ClassificationPosition> classification = new ArrayList<ClassificationPosition>();

        Criteria general = Criteria.where("gameId").is(g.getId());

        Query query = new Query();
        query.addCriteria(general);
//        query.with(
//                new Sort(Sort.Direction.DESC, "concepts.PointConcept." + itemType + ".obj.score"));
        query.with(Sort.by(Sort.Order.desc("concepts.PointConcept." + itemType + ".obj.score")));
        
        query.fields().include("concepts.PointConcept." + itemType + ".obj.score");
        query.fields().include("playerId");
        // pagination.
        query.with(pageable);

        List<StatePersistence> pStates = mongoTemplate.find(query, StatePersistence.class);

        for (StatePersistence state : pStates) {
            classification.add(new ClassificationPosition(state.getGeneralItemScore(itemType),
                    state.getPlayerId()));
        }
        classificationBoard.setPointConceptName(itemType);
        classificationBoard.setBoard(classification);
        classificationBoard.setType(ClassificationType.GENERAL);

        return classificationBoard;
    }

    private Page<PlayerState> convertToPlayerState(Page<StatePersistence> states,
            Pageable pageable) {
        List<PlayerState> contents = new ArrayList<>();
        for (StatePersistence state : states) {
            contents.add(new PlayerState(state));
        }
        Page<PlayerState> result = new PageImpl<>(contents, pageable, states.getTotalElements());

        return result;
    }

    @Override
    public Page<PlayerState> search(String gameId, RawSearchQuery query, Pageable pageable) {
        Page<StatePersistence> states = playerRepo.search(gameId, query, pageable);
        return convertToPlayerState(states, pageable);

    }

    @Override
    public Page<PlayerState> search(String gameId, ComplexSearchQuery query, Pageable pageable) {
        Page<StatePersistence> states = playerRepo.search(gameId, query, pageable);
        return convertToPlayerState(states, pageable);
    }

    @Override
    public Page<PlayerState> search(String gameId, StringSearchQuery query, Pageable pageable) {
        Page<StatePersistence> states = playerRepo.search(gameId, query, pageable);
        return convertToPlayerState(states, pageable);
    }

    @Override
    public ChallengeConcept acceptChallenge(String gameId, String playerId, String challengeName) {
        Game game = gameSrv.loadGameDefinitionById(gameId);
        PlayerState state = loadState(gameId, playerId, false, false);
        List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, playerId); 
        state.loadChallengeConcepts(listCcs);
        boolean found = false;
        ChallengeConcept accepted = null;
        for (ChallengeConcept challenge : state.challenges()) {
            if (challenge.getName().equals(challengeName)) {
                if (challenge.getState() == ChallengeState.PROPOSED) {
                    accepted = challenge.updateState(ChallengeState.ASSIGNED);
                    found = true;
                    break;
                } else {
                    throw new IllegalArgumentException(
                            String.format("challenge %s is not in state proposed", challengeName));
                }
            }
        }

        if (found) {
            long executionTime = System.currentTimeMillis();
            String executionId = UUID.randomUUID().toString();
            StatsLogger.logChallengeAccepted(game.getDomain(), gameId, playerId, executionId,
                    executionTime, executionTime, challengeName);
            java.util.Iterator<ChallengeConcept> iterator = state.challenges().iterator();

            // refuse all SINGLE PROPOSED
            while (iterator.hasNext()) {
                ChallengeConcept ch = iterator.next();
                if (ch.getState() == ChallengeState.PROPOSED) {
                    ChallengeConcept removedChallenge =
                            state.removeConcept(ch.getName(), ChallengeConcept.class);
                    removedChallenge.updateState(ChallengeState.REFUSED);
                    ChallengeConceptPersistence saved = challengeConceptRepo.findByGameIdAndPlayerIdAndName(gameId,
							playerId, ch.getName());
                    challengeConceptRepo.delete(saved);
                    archiveSrv.moveToArchive(gameId, playerId, removedChallenge);
                    StatsLogger.logChallengeRefused(game.getDomain(), gameId, playerId,
                            executionId, executionTime, executionTime, ch.getName());
                }
            }
            saveState(state);
        }

        if (!found) {
            throw new IllegalArgumentException(
                    String.format("challenge %s not exist", challengeName));
        }

        return accepted;
    }

    @SuppressWarnings("unused")
    private ChallengeInvitationRefusedNotification sendRefusedNotification(String guestId,
            GroupChallenge refused) {
        ChallengeInvitationRefusedNotification notification =
                new ChallengeInvitationRefusedNotification();
        Attendee proposer = refused.proposer();
        final String gameId = refused.getGameId();
        final String challengeName = refused.getInstanceName();

        if (proposer != null) {
            notification.setGameId(gameId);
            notification.setPlayerId(proposer.getPlayerId());
            notification.setChallengeName(challengeName);
            notification.setGuestId(guestId);
            notificationSrv.notificate(notification);
        } else {
            LogHub.warn(gameId, logger, String
                    .format("Invitation without proposer, no refuse notification will be send"));
        }
        return notification;
    }

    @SuppressWarnings("unused")
    private void sendCanceledNotifications(GroupChallenge canceled) {
        final Attendee proposer = canceled.proposer();
        final String gameId = canceled.getGameId();
        final String challengeName = canceled.getInstanceName();
        canceled.guests().forEach(guest -> {
            ChallengeInvitationCanceledNotification notification =
                    new ChallengeInvitationCanceledNotification();
            notification.setGameId(gameId);
            notification.setPlayerId(guest.getPlayerId());
            notification.setChallengeName(challengeName);
            if (proposer != null) {
                notification.setProposerId(proposer.getPlayerId());
            }
            notificationSrv.notificate(notification);
        });
    }

    @Override
    public ChallengeConcept forceChallengeChoice(String gameId, String playerId) {
        PlayerState state = loadState(gameId, playerId, false, false);
        List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, state.getPlayerId()); 
        state.loadChallengeConcepts(listCcs);
        Optional<ChallengeConcept> maxPriorityChallenge = state.challenges().stream()
                .filter(challenge -> challenge.getState() == ChallengeState.PROPOSED)
                .max(new PriorityComparator());

        // assign PROPOSED SINGLE challenge with max priority and flag as forced
        ChallengeConcept forcedChallenge = maxPriorityChallenge
                .map(challenge -> challenge.updateState(ChallengeState.ASSIGNED).forced())
                .orElse(null);

        // auto discarded all PROPOSED SINGLE challenges
        state.challenges().stream()
                .filter(challenge -> challenge.getState() == ChallengeState.PROPOSED)
                .forEach(proposed -> {
                    ChallengeConcept removedChallenge =
                            state.removeConcept(proposed.getName(), ChallengeConcept.class);
					ChallengeConceptPersistence saved = challengeConceptRepo.findByGameIdAndPlayerIdAndName(gameId,
							playerId, proposed.getName());
					challengeConceptRepo.delete(saved);
                    removedChallenge.updateState(ChallengeState.AUTO_DISCARDED);
                    archiveSrv.moveToArchive(gameId, playerId, removedChallenge);
                });

        // auto discarded all PROPOSED GROUP challenges
        List<GroupChallenge> otherProposedhallenges =
                groupChallengeRepo.playerGroupChallenges(gameId, playerId, ChallengeState.PROPOSED);
        groupChallengeRepo.deleteAll(otherProposedhallenges);
        otherProposedhallenges.forEach(challenge -> {
            challenge.updateState(ChallengeState.AUTO_DISCARDED);
            archiveSrv.moveToArchive(gameId, challenge);
        });

        saveState(state);
        return forcedChallenge;
    }
    


    private class PriorityComparator implements Comparator<ChallengeConcept> {

        @Override
        public int compare(ChallengeConcept o1, ChallengeConcept o2) {
            return o1.getPriority() - o2.getPriority();
        }

    }

	@Override
    public PlayerBlackList blockPlayer(String gameId, String playerId, String otherPlayerId) {
		Criteria criteria = new Criteria("gameId").is(gameId).and("playerId").is(playerId);

		Query q = new Query();
		q.addCriteria(criteria);

		PlayerBlackList pbListObj = mongoTemplate.findOne(q, PlayerBlackList.class);

		if (pbListObj != null) {
			if (pbListObj.getBlockedPlayers().indexOf(otherPlayerId) < 0) {
				pbListObj.getBlockedPlayers().add(otherPlayerId);	
			}			
		} else {
			pbListObj = new PlayerBlackList();
			pbListObj.setPlayerId(playerId);
			pbListObj.setGameId(gameId);
			pbListObj.getBlockedPlayers().add(otherPlayerId);			
		}
		
		mongoTemplate.save(pbListObj);
        final String executionId = UUID.randomUUID().toString();
        final long timestamp = System.currentTimeMillis();
        Game game = gameSrv.loadGameDefinitionById(gameId);
        StatsLogger.logBlacklist(game.getDomain(), gameId, playerId, executionId, timestamp,
                otherPlayerId);
        return pbListObj;

	}

	@Override
    public PlayerBlackList unblockPlayer(String gameId, String playerId, String otherPlayerId) {
		Criteria criteria = new Criteria("gameId").is(gameId).and("playerId").is(playerId);

		Query q = new Query();
		q.addCriteria(criteria);

		PlayerBlackList pbListObj = mongoTemplate.findOne(q, PlayerBlackList.class);

        if (pbListObj == null) {
            pbListObj = new PlayerBlackList();
        }

        pbListObj.getBlockedPlayers().remove(otherPlayerId);
		mongoTemplate.save(pbListObj);
        final String executionId = UUID.randomUUID().toString();
        final long timestamp = System.currentTimeMillis();
        Game game = gameSrv.loadGameDefinitionById(gameId);
        StatsLogger.logUnblacklist(game.getDomain(), gameId, playerId, executionId, timestamp,
                otherPlayerId);
        return pbListObj;
	}

	@Override
	public PlayerBlackList readBlackList(String gameId, String playerId) {
		Criteria criteria = new Criteria("gameId").is(gameId).and("playerId").is(playerId);

		Query q = new Query();
		q.addCriteria(criteria);

        PlayerBlackList response = mongoTemplate.findOne(q, PlayerBlackList.class);
        return response != null ? response : new PlayerBlackList();
	}
	
	@Override
	public List<String> readSystemPlayerState(String gameId, String playerId, String conceptName) {

		List<String> sps = new ArrayList<String>();
		List<String> filterPlayerList = new ArrayList<String>();
		
		filterPlayerList.add(playerId);

		// 1. Read the level of proposer player.
		StatePersistence callerState = playerRepo.findByGameIdAndPlayerId(gameId, playerId);

		if (callerState != null && !callerState.getLevels().isEmpty()) {

			PlayerLevel referenceLevel = null;

			if (conceptName != null && !conceptName.isEmpty()) {
				for (PlayerLevel pLevel : callerState.getLevels()) {
					if (pLevel.getPointConcept().equalsIgnoreCase(conceptName)) {
						referenceLevel = pLevel;
						break;
					}
				}
			} else {
				referenceLevel = callerState.getLevels().get(0);
			}

			if (referenceLevel != null) {

				// 2.level is in the proposer's range +-2
				int levelMax = referenceLevel.getLevelIndex() + PROPOSER_RANGE;
				int levelMin = referenceLevel.getLevelIndex() - PROPOSER_RANGE;

				Criteria criteria = new Criteria("playerId").nin(filterPlayerList).and("gameId").is(gameId);

				if (conceptName != null && !conceptName.isEmpty()) {
					criteria = criteria.and("levels").elemMatch(Criteria.where("levelIndex").gte(levelMin).lte(levelMax)
							.and("pointConcept").is(conceptName));
				} else {
					criteria = criteria.and("levels.0").exists(true).and("levels.0.levelIndex").gte(levelMin)
							.lte(levelMax);
				}

				// 3.player is not in proposer's blacklist
				PlayerBlackList pbList = readBlackList(gameId, playerId);
				if (pbList != null && !pbList.getBlockedPlayers().isEmpty()) {
					Criteria blistCriteria = new Criteria("playerId").nin(pbList.getBlockedPlayers());
					criteria = criteria.andOperator(blistCriteria);
				}
				
				// 4.read players who have blocked proposer(NEW!!)
				Criteria criteria2 = new Criteria("blockedPlayers").in(playerId);
				Query q1 = new Query();
				q1.addCriteria(criteria2);
				q1.fields().include("playerId");
				for (PlayerBlackList blocker : mongoTemplate.find(q1, PlayerBlackList.class)) {
					filterPlayerList.add(blocker.getPlayerId());
				}
				
				Query q2 = new Query();
				q2.addCriteria(criteria);
				List<PlayerState> filerList = mongoTemplate.find(q2, PlayerState.class);

				Date now = new Date();
				for (PlayerState ps : filerList) {
					// 4. player has received less than 3 invitations to challenge
					if (groupChallengeRepo.guestInvitations(gameId, ps.getPlayerId()).size() < 3) {

						// 5.1 check for group challenge assignment.
						Criteria groupChallengeCheck = new Criteria("gameId").is(gameId).and("state").is(ChallengeState.ASSIGNED)
								.and("start").gt(now).and("attendees.playerId").is(ps.getPlayerId());
						Query q4 = new Query();
						q4.addCriteria(groupChallengeCheck);
						
						if (mongoTemplate.find(q4, GroupChallenge.class).size() > 0) {
							continue;
						}
						
						// 5.2 check for single challenge assignment.
                        // boolean isChallengeAssignedInFuture = false;
                        // for (ChallengeConcept cp : loadState(gameId, ps.getPlayerId(), false,
                        // false)
                        // .challenges()) {
                        // if (cp.getState().equals(ChallengeState.ASSIGNED)
                        // && cp.getStart().after(now)) {
                        // isChallengeAssignedInFuture = true;
                        // break;
                        // }
                        // }
                        // if (!isChallengeAssignedInFuture) {
                            sps.add(ps.getPlayerId());
                        // }
					}
				}
				
			} else {
				LogHub.error(gameId, logger,
						"readSystemPlayerState: no reference level found for player {} for this game {} for conceptName {}",
						playerId, gameId, conceptName);
				throw new ResourceNotFoundException("readSystemPlayerState: no reference level found for player " + playerId
						+ " for game " + gameId + " for conceptName " + conceptName);
			}

		} else {
			LogHub.error(gameId, logger,
					"readSystemPlayerState: no player state | empty level found for player {} for this game {}",
					playerId, gameId);
			throw new ResourceNotFoundException("readSystemPlayerState: no player state | empty level found for player "
					+ playerId + " for game " + gameId);
		}

		return sps;

	}

    @Override
    public Inventory choiceActivation(String gameId, String playerId, ItemChoice choice) {
        PlayerState state = loadState(gameId, playerId, false, false);
        Game game = gameSrv.loadGameDefinitionById(gameId);
        final String executionId = UUID.randomUUID().toString();
        final long executionTime = System.currentTimeMillis();
        if (state != null) {
            Inventory result = state.getInventory().activateChoice(choice);
            saveState(state);
            StatsLogger.logChoiceActived(game.getDomain(), gameId, playerId, executionId,
                    executionTime,
                    executionTime, choice.getName());
            return result;
        } else {
            throw new IllegalArgumentException(String
                    .format("state for player %s in game %s doesn't exist", playerId, gameId));
        }
    }
    
	private Map<String, Map<String, GenericObjectPersistence>> persistChallengeConcept(
			Map<String, Map<String, GenericObjectPersistence>> concepts, String gameId, String playerId) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, GenericObjectPersistence>> challengeConcepts = concepts.entrySet().stream()
				.filter(x -> x.getKey().equals(ChallengeConcept.class.getSimpleName()))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
		Map<String, Map<String, GenericObjectPersistence>> otherConcepts = concepts.entrySet().stream()
				.filter(x -> !(x.getKey().equals(ChallengeConcept.class.getSimpleName())))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
		// UPDATE, INSERT
		for (Map<String, GenericObjectPersistence> entry : challengeConcepts.values()) {
			for (GenericObjectPersistence gpo : entry.values()) {
				ChallengeConcept cc = mapper.convertValue(gpo.getObj(), ChallengeConcept.class);
				// update, insert
				ChallengeConceptPersistence persist = challengeConceptRepo.findByGameIdAndPlayerIdAndName(gameId, playerId, cc.getName());
				if (persist != null) {
					persist.setConcept(cc);
				} else {
					persist = new ChallengeConceptPersistence(cc, gameId, playerId, cc.getName());
				}
				challengeConceptRepo.save(persist);
			}
		}
		
		return otherConcepts;
	}
	
}
