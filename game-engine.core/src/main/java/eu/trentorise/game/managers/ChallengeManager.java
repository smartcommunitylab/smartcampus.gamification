package eu.trentorise.game.managers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.trentorise.game.core.Clock;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeInvitation;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.ChallengeReportCSVStub;
import eu.trentorise.game.model.ChallengeReportJSONStub;
import eu.trentorise.game.model.ChallengeUpdate;
import eu.trentorise.game.model.ExportCsv;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.ArchivedConcept;
import eu.trentorise.game.notification.ChallengeCompletedNotication;
import eu.trentorise.game.notification.ChallengeFailedNotication;
import eu.trentorise.game.notification.ChallengeInvitationAcceptedNotification;
import eu.trentorise.game.notification.ChallengeInvitationCanceledNotification;
import eu.trentorise.game.notification.ChallengeInvitationNotification;
import eu.trentorise.game.notification.ChallengeInvitationRefusedNotification;
import eu.trentorise.game.repo.ChallengeConceptPersistence;
import eu.trentorise.game.repo.ChallengeConceptRepo;
import eu.trentorise.game.repo.GroupChallengeRepo;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@Service
public class ChallengeManager {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ChallengeManager.class);
	private static final String INVITATION_CHALLENGE_ORIGIN = "player";
	private static final String SINGLE_CHALLENGE = "SingleChallenge";
	private static final String SINGLE = "single";
	private static final String GROUP = "group";

	private static final int LIMIT_INVITATIONS_AS_PROPOSER = 1;
	private static final int LIMIT_INVITATIONS_AS_GUEST = 3;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private GroupChallengeRepo groupChallengeRepo;

	@Autowired
	private ChallengeConceptRepo challengeConceptRepo;

	@Autowired
	private Clock clock;

	@Autowired
	private GameService gameSrv;

	@Autowired
	private NotificationManager notificationSrv;

	@Autowired
	private ArchiveManager archiveSrv;

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<String> conditionCheck(GroupChallenge groupChallenge) {
		if (groupChallenge.getChallengeModel().equals(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE)) { // check if
																											// I need
																											// this
																											// behavior
																											// in
																											// best
																											// performance
			List<Attendee> attendees = groupChallenge.getAttendees();
			List<String> playerIds = attendees.stream().map(attendee -> attendee.getPlayerId())
					.collect(Collectors.toList());
			final String gameId = groupChallenge.getGameId();
			List<PlayerState> playerStates = playerIds.stream().map(id -> playerSrv.loadState(gameId, id, false, false))
					.filter(state -> state != null).collect(Collectors.toList());

			groupChallenge = groupChallenge.update(playerStates);
		}
		return groupChallenge.winners().stream().map(winner -> winner.getPlayerId()).collect(Collectors.toList());
	}

	public GroupChallenge save(GroupChallenge challenge) {
		if (challenge != null) {
			// ATTENTION: some issues with tests
			Game game = gameSrv.loadGameDefinitionById(challenge.getGameId());
			challenge.validate(game);
			if (StringUtils.isBlank(challenge.getInstanceName())) {
				Attendee proposer = challenge.proposer();
				challenge.setInstanceName(String.format("p_%s_%s", proposer != null ? proposer.getPlayerId() : null,
						UUID.randomUUID().toString()));
			}
			return groupChallengeRepo.save(challenge);
		} else {
			return null;
		}
	}

	public void logStatsEvents(Game game, GroupChallenge challenge) {
		if (challenge != null && game != null) {
			List<Attendee> attendees = challenge.getAttendees();
			long timestamp = System.currentTimeMillis();
			String executionId = UUID.randomUUID().toString();
			attendees.forEach(a -> {
				if (a.isWinner()) {
					StatsLogger.logChallengeCompleted(game.getDomain(), game.getId(), a.getPlayerId(), executionId,
							challenge.getEnd().getTime(), timestamp, challenge.getInstanceName());
				} else {
					StatsLogger.logChallengeFailed(game.getDomain(), game.getId(), a.getPlayerId(), executionId,
							challenge.getEnd().getTime(), timestamp, challenge.getInstanceName());
				}
			});
		}
	}

	public void sendChallengeNotification(GroupChallenge challenge) {
		if (challenge != null) {
			List<Attendee> attendees = challenge.getAttendees();
			attendees.stream().forEach(a -> {
				if (a.isWinner()) {
					ChallengeCompletedNotication notification = new ChallengeCompletedNotication();
					notification.setChallengeName(challenge.getInstanceName());
					notification.setGameId(challenge.getGameId());
					notification.setPlayerId(a.getPlayerId());
					notification.setModel(challenge.getChallengeModel());
					notification.setPointConcept(challenge.getChallengePointConcept().getName());
					notificationSrv.notificate(notification);
				} else {
					ChallengeFailedNotication notification = new ChallengeFailedNotication();
					notification.setChallengeName(challenge.getInstanceName());
					notification.setGameId(challenge.getGameId());
					notification.setPlayerId(a.getPlayerId());
					notification.setModel(challenge.getChallengeModel());
					notification.setPointConcept(challenge.getChallengePointConcept().getName());
					notificationSrv.notificate(notification);
				}
			});
		}
	}

	public ChallengeConcept update(String gameId, String playerId, ChallengeUpdate changes) {
		PlayerState state = playerSrv.loadState(gameId, playerId, false, false);
		List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, playerId);
		state.loadChallengeConcepts(listCcs);
		Optional<ChallengeConcept> challenge = state.challenge(changes.getName());
		challenge.ifPresent(c -> {
			final String modelName = c.getModelName();
			Optional<ChallengeModel> model = gameSrv.readChallengeModelByName(gameId, modelName);
			List<String> invalidFields = model.map(m -> m.invalidFields(changes)).orElse(Collections.emptyList());
			if (!invalidFields.isEmpty()) {
				throw new IllegalArgumentException(
						String.format("field %s not present in model %s", invalidFields.get(0), modelName));
			}
		});
		ChallengeConcept challengeUpdated = challenge.flatMap(c -> {
			state.upateChallenge(changes);
			playerSrv.saveState(state);
			return state.challenge(changes.getName());
		}).orElseThrow(() -> new IllegalArgumentException(
				String.format("challenges %s not found in player %s", changes.getName(), playerId)));
		LogHub.info(gameId, logger, String.format("updated challenge %s of player %s", changes.getName(), playerId));
		return challengeUpdated;
	}

	public List<GroupChallenge> completedPerformanceGroupChallenges(String gameId) {
		return groupChallengeRepo.findByGameIdAndStateAndEndBeforeAndChallengeModel(gameId, ChallengeState.ASSIGNED,
				clock.now(), GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
	}

	public List<GroupChallenge> activeGroupChallengesByDate(String gameId, String playerId, Date atDate) {
		return groupChallengeRepo.activeGroupChallenges(gameId, playerId, atDate);
	}

	public List<GroupChallenge> groupChallengeToFail(String gameId, Date atDate) {
		return groupChallengeRepo.groupChallengesToFail(gameId, atDate);
	}

	public GroupChallenge inviteToChallenge(ChallengeInvitation invitation) {
		if (invitation != null) {

			invitation.validate();
			// check if player has sent other invitations
			List<GroupChallenge> proposerInvitations = groupChallengeRepo.proposerInvitations(invitation.getGameId(),
					invitation.getProposer().getPlayerId());
			if (proposerInvitations.size() >= LIMIT_INVITATIONS_AS_PROPOSER) {
				throw new IllegalArgumentException(
						String.format("player %s already has %s pending invitations as proposer",
								invitation.getProposer().getPlayerId(), LIMIT_INVITATIONS_AS_PROPOSER));
			}

			// check if player has received max 3 invitations
			invitation.getGuests().forEach(guest -> {
				List<GroupChallenge> guestInvitations = groupChallengeRepo.guestInvitations(invitation.getGameId(),
						guest.getPlayerId());
				if (guestInvitations.size() >= LIMIT_INVITATIONS_AS_GUEST) {
					throw new IllegalArgumentException(
							String.format("player %s already has %s pending invitations as guest", guest.getPlayerId(),
									LIMIT_INVITATIONS_AS_GUEST));
				}
			});

			GroupChallenge groupChallenge = convert(invitation);
			save(groupChallenge);

			// produce notifications
			final String proposerId = invitation.getProposer().getPlayerId();
			final Game game = gameSrv.loadGameDefinitionById(invitation.getGameId());
			final String inviteExecutionId = UUID.randomUUID().toString();
			final long executionTime = clock.nowAsMillis();
			invitation.getGuests().forEach(guest -> {
				ChallengeInvitationNotification invitationNotification = new ChallengeInvitationNotification();
				invitationNotification.setGameId(invitation.getGameId());
				invitationNotification.setPlayerId(guest.getPlayerId());
				invitationNotification.setProposerId(proposerId);
				notificationSrv.notificate(invitationNotification);
				LogHub.info(invitation.getGameId(), logger,
						String.format("Player %s invites player %s to a challenge", proposerId, guest.getPlayerId()));
				StatsLogger.logInviteToChallenge(game.getDomain(), game.getId(), proposerId, inviteExecutionId,
						executionTime, executionTime, guest.getPlayerId(), groupChallenge.getInstanceName(),
						groupChallenge.getChallengeModel());
			});
			return groupChallenge;
		}
		return null;
	}

	private GroupChallenge convert(ChallengeInvitation invitation) {
		GroupChallenge challenge = null;
		if (invitation != null) {
			challenge = new GroupChallenge(ChallengeState.PROPOSED);
			challenge.setChallengePointConcept(invitation.getChallengePointConcept());
			challenge.setChallengeModel(invitation.getChallengeModelName());
			challenge.setOrigin(INVITATION_CHALLENGE_ORIGIN);
			challenge.setStart(invitation.getChallengeStart());
			challenge.setEnd(invitation.getChallengeEnd());
			challenge.setAttendees(attendees(invitation));
			challenge.setGameId(invitation.getGameId());
			challenge.setReward(invitation.getReward());
			challenge.setInstanceName(invitation.getChallengeName());
			challenge.setChallengeTarget(invitation.getChallengeTarget());
			if (StringUtils.isBlank(challenge.getInstanceName())) {
				challenge.setInstanceName(
						String.format("p_%s_%s", invitation.getProposer().getPlayerId(), UUID.randomUUID().toString()));
			}
		}

		return challenge;
	}

	private List<Attendee> attendees(ChallengeInvitation invitation) {
		List<Attendee> attendees = new ArrayList<GroupChallenge.Attendee>();
		if (invitation != null) {
			if (invitation.getProposer() != null) {
				Attendee proposer = new Attendee();
				proposer.setPlayerId(invitation.getProposer().getPlayerId());
				proposer.setRole(Role.PROPOSER);
				attendees.add(proposer);
			}
			attendees.addAll(invitation.getGuests().stream().map(p -> {
				Attendee guest = new Attendee();
				guest.setPlayerId(p.getPlayerId());
				guest.setRole(Role.GUEST);
				return guest;
			}).collect(Collectors.toList()));

		}
		return attendees;
	}

	public GroupChallenge acceptInvitation(String gameId, String playerId, String challengeName) {
		PlayerState guestState = playerSrv.loadState(gameId, playerId, true, false);
		List<GroupChallenge> guestInvitations = groupChallengeRepo.guestInvitations(gameId, playerId);
		GroupChallenge pendingInvitation = guestInvitations.stream()
				.filter(invitation -> invitation.getInstanceName().equals(challengeName)).findFirst().orElse(null);
		if (pendingInvitation != null) {
			pendingInvitation.updateState(ChallengeState.ASSIGNED);
			save(pendingInvitation);
			LogHub.info(gameId, logger,
					String.format("Player %s has accepted invitation for challenge %s", playerId, challengeName));
			Attendee challengeProposer = pendingInvitation.proposer();
			if (challengeProposer != null) {
				ChallengeInvitationAcceptedNotification acceptedNotification = new ChallengeInvitationAcceptedNotification();
				acceptedNotification.setGameId(gameId);
				acceptedNotification.setPlayerId(challengeProposer.getPlayerId());
				acceptedNotification.setChallengeName(pendingInvitation.getInstanceName());
				acceptedNotification.setGuestId(playerId);
				notificationSrv.notificate(acceptedNotification);
				Game game = gameSrv.loadGameDefinitionById(gameId);
				final String executionId = UUID.randomUUID().toString();
				final long executionTime = clock.nowAsMillis();
				StatsLogger.logChallengeInvitationAccepted(game.getDomain(), gameId, playerId, executionId,
						executionTime, executionTime, pendingInvitation.getInstanceName(),
						pendingInvitation.getChallengeModel());
			}

			triggerOnProposedGroupChallenges(guestState);
			if (challengeProposer != null) {
				PlayerState proposerState = playerSrv.loadState(gameId, challengeProposer.getPlayerId(), true, false);
				triggerOnProposedGroupChallenges(proposerState);
			}

			return pendingInvitation;
		} else {
			throw new IllegalArgumentException(
					String.format("Challenge %s is not PROPOSED for guest player %s", challengeName, playerId));
		}
	}

	private void triggerOnProposedGroupChallenges(PlayerState playerState) {
		final String gameId = playerState.getGameId();
		final String playerId = playerState.getPlayerId();
		final Game game = gameSrv.loadGameDefinitionById(gameId);

		List<GroupChallenge> otherProposedchallenges = groupChallengeRepo.playerGroupChallenges(gameId, playerId,
				ChallengeState.PROPOSED);
		groupChallengeRepo.deleteAll(otherProposedchallenges);
		otherProposedchallenges.forEach(challenge -> {
			final Attendee proposer = challenge.proposer();
			if (proposer != null && proposer.getPlayerId().equals(playerId)) {
				challenge.updateState(ChallengeState.CANCELED);
				sendCanceledNotifications(challenge);
			} else {
				challenge.updateState(ChallengeState.REFUSED);
			}
			archiveSrv.moveToArchive(gameId, challenge);

			// send notifications to other participants
			final String executionId = UUID.randomUUID().toString();
			final long executionTime = System.currentTimeMillis();
			challenge.guests().stream().filter(g -> g.getPlayerId().equals(playerId)).findFirst().ifPresent(guest -> {
				sendRefusedNotification(guest.getPlayerId(), challenge);
				StatsLogger.logChallengeInvitationRefused(game.getDomain(), gameId, playerId, executionId,
						executionTime, executionTime, challenge.getInstanceName(), challenge.getChallengeModel());
			});

		});
	}

	public GroupChallenge refuseInvitation(String gameId, String playerId, String challengeName) {
		GroupChallenge refused = groupChallengeRepo.deleteProposedChallengeByGuest(gameId, playerId, challengeName);

		if (refused == null) {
			throw new IllegalArgumentException(
					String.format("Challenge %s is not PROPOSED for guest player %s", challengeName, playerId));
		}
		Game game = gameSrv.loadGameDefinitionById(gameId);
		refused.updateState(ChallengeState.REFUSED);
		archiveSrv.moveToArchive(gameId, refused);
		sendRefusedNotification(playerId, refused);
		final String executionId = UUID.randomUUID().toString();
		final long executionTime = System.currentTimeMillis();
		StatsLogger.logChallengeInvitationRefused(game.getDomain(), gameId, playerId, executionId, executionTime,
				executionTime, challengeName, refused.getChallengeModel());
		LogHub.info(gameId, logger,
				String.format("Invitation to challenge %s is refused by player %s", challengeName, playerId));
		return refused;
	}

	private ChallengeInvitationRefusedNotification sendRefusedNotification(String guestId, GroupChallenge refused) {
		ChallengeInvitationRefusedNotification notification = new ChallengeInvitationRefusedNotification();
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
			LogHub.warn(gameId, logger,
					String.format("Invitation without proposer, no refuse notification will be send"));
		}
		return notification;
	}

	public GroupChallenge cancelInvitation(String gameId, String playerId, String challengeName) {
		GroupChallenge canceled = groupChallengeRepo.deleteProposedChallengeByProposer(gameId, playerId, challengeName);

		if (canceled == null) {
			throw new IllegalArgumentException(
					String.format("Challenge %s is not PROPOSED by proposer player %s", challengeName, playerId));
		}
		sendCanceledNotifications(canceled);
		canceled.updateState(ChallengeState.CANCELED);
		archiveSrv.moveToArchive(gameId, canceled);
		final Game game = gameSrv.loadGameDefinitionById(gameId);
		LogHub.info(gameId, logger,
				String.format("Invitation to challenge %s canceled by player %s", challengeName, playerId));
		final String executionId = UUID.randomUUID().toString();
		final long executionTime = System.currentTimeMillis();
		StatsLogger.logChallengeInvitationCanceled(game.getDomain(), gameId, playerId, executionId, executionTime,
				executionTime, challengeName, canceled.getChallengeModel());
		return canceled;
	}

	private void sendCanceledNotifications(GroupChallenge canceled) {
		final Attendee proposer = canceled.proposer();
		final String gameId = canceled.getGameId();
		final String challengeName = canceled.getInstanceName();
		canceled.guests().forEach(guest -> {
			ChallengeInvitationCanceledNotification notification = new ChallengeInvitationCanceledNotification();
			notification.setGameId(gameId);
			notification.setPlayerId(guest.getPlayerId());
			notification.setChallengeName(challengeName);
			if (proposer != null) {
				notification.setProposerId(proposer.getPlayerId());
			}
			notificationSrv.notificate(notification);
		});
	}

	public String readChallengeReportJSON(String gameId, Date from, Date to, String challengeType, String modelName,
			Boolean hidden) throws JsonProcessingException {

		String result;
		List<ChallengeReportJSONStub> challengeReportList = new ArrayList<>();

		if (challengeType == null) {
			List<ChallengeReportJSONStub> singleChallenges = readSingleChallengesJSONStub(gameId, from, to, modelName,
					hidden);
			List<ChallengeReportJSONStub> groupChallenges = readGroupChallengesJSONStub(gameId, from, to, modelName);
			challengeReportList.addAll(singleChallenges);
			challengeReportList.addAll(groupChallenges);
		} else if (challengeType.equalsIgnoreCase(SINGLE)) {
			List<ChallengeReportJSONStub> singleChallenges = readSingleChallengesJSONStub(gameId, from, to, modelName,
					hidden);
			challengeReportList.addAll(singleChallenges);
		} else if (challengeType.equalsIgnoreCase(GROUP)) {
			List<ChallengeReportJSONStub> groupChallenges = readGroupChallengesJSONStub(gameId, from, to, modelName);
			challengeReportList.addAll(groupChallenges);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		result = mapper.writeValueAsString(challengeReportList);

		return result;

	}

	private List<ChallengeReportJSONStub> readSingleChallengesJSONStub(String gameId, Date from, Date to,
			String modelName, Boolean hidden) {
		List<ChallengeReportJSONStub> singleChallengeReport = new ArrayList<>();

		List<ChallengeConceptPersistence> singleChallenges = readSingleChallenges(gameId, from, to, modelName, hidden);

		for (ChallengeConceptPersistence sc : singleChallenges) {
			ChallengeReportJSONStub tmp = new ChallengeReportJSONStub();
			tmp.setSingleChallenge(sc.getConcept());
			tmp.setChallengeType(SINGLE_CHALLENGE);
			singleChallengeReport.add(tmp);
		}

		List<ArchivedConcept> archivedSingleChallenges = readSingleArchives(gameId, from, to, modelName, hidden);

		for (ArchivedConcept asc : archivedSingleChallenges) {
			ChallengeReportJSONStub tmp = new ChallengeReportJSONStub();
			tmp.setSingleChallenge(asc.getChallenge());
			tmp.setChallengeType(SINGLE_CHALLENGE);
			singleChallengeReport.add(tmp);
		}

		return singleChallengeReport;

	}

	private List<ChallengeReportJSONStub> readGroupChallengesJSONStub(String gameId, Date from, Date to,
			String modelName) {
		List<ChallengeReportJSONStub> groupChallengeReport = new ArrayList<>();

		List<GroupChallenge> groupChallenges = readGroupChallenges(gameId, from, to, modelName);

		for (GroupChallenge gc : groupChallenges) {
			ChallengeReportJSONStub tmp = new ChallengeReportJSONStub();
			tmp.setGroupChallenge(gc);
			tmp.setChallengeType(gc.getClass().getSimpleName());
			groupChallengeReport.add(tmp);
		}

		List<ArchivedConcept> groupChallengeArchive = readGroupArchives(gameId, from, to, modelName);

		for (ArchivedConcept agc : groupChallengeArchive) {
			ChallengeReportJSONStub tmp = new ChallengeReportJSONStub();
			tmp.setGroupChallenge(agc.getGroupChallenge());
			tmp.setChallengeType(agc.getGroupChallenge().getClass().getSimpleName());
			groupChallengeReport.add(tmp);
		}

		return groupChallengeReport;
	}

	public List<ChallengeReportCSVStub> readChallengeReportCSV(String gameId, Date from, Date to, String challengeType,
			String modelName, Boolean hidden) {

		List<ChallengeReportCSVStub> result = new ArrayList<>();

		if (challengeType == null ) {
			List<ChallengeReportCSVStub> singleChallenges = readSingleChallengesCSVStub(gameId, from, to, modelName,
					hidden);
			List<ChallengeReportCSVStub> groupChallenges = readGroupChallengesCSVStub(gameId, from, to, modelName);
			result.addAll(singleChallenges);
			result.addAll(groupChallenges);
		} else if (challengeType.equalsIgnoreCase(SINGLE)) {
			List<ChallengeReportCSVStub> singleChallenges = readSingleChallengesCSVStub(gameId, from, to, modelName,
					hidden);
			result.addAll(singleChallenges);
		} else if (challengeType.equalsIgnoreCase(GROUP)) {
			List<ChallengeReportCSVStub> groupChallenges = readGroupChallengesCSVStub(gameId, from, to, modelName);
			result.addAll(groupChallenges);
		} 
		
		return result;
	}

	private List<ChallengeReportCSVStub> readSingleChallengesCSVStub(String gameId, Date from, Date to,
			String modelName, Boolean hidden) {

		List<ChallengeReportCSVStub> singleChallengeReport = new ArrayList<>();

		List<ChallengeConceptPersistence> singleChallenges = readSingleChallenges(gameId, from, to, modelName, hidden);

		for (ChallengeConceptPersistence sc : singleChallenges) {
			ChallengeReportCSVStub tmp = new ChallengeReportCSVStub();
			tmp.setChallengeType(SINGLE_CHALLENGE);
			tmp.setId(sc.getId());
			tmp.setName(sc.getName());
			tmp.setModelName(sc.getConcept().getModelName());
			tmp.setGameId(sc.getGameId());
			tmp.setPlayerId(sc.getPlayerId());
			tmp.setStart(sc.getConcept().getStart());
			tmp.setEnd(sc.getConcept().getEnd());
			tmp.setStateDate(Map.copyOf(sc.getConcept().getStateDate()));
			tmp.setHide(sc.getConcept().getVisibility().isHidden());
			singleChallengeReport.add(tmp);
		}

		List<ArchivedConcept> archivedSingleChallenges = readSingleArchives(gameId, from, to, modelName, hidden);

		for (ArchivedConcept asc : archivedSingleChallenges) {
			ChallengeReportCSVStub tmp = new ChallengeReportCSVStub();
			tmp.setChallengeType(SINGLE_CHALLENGE);
			tmp.setId(asc.getId());
			tmp.setName(asc.getChallenge().getName());
			tmp.setModelName(asc.getChallenge().getModelName());
			tmp.setGameId(asc.getGameId());
			tmp.setPlayerId(asc.getPlayerId());
			tmp.setStart(asc.getChallenge().getStart());
			tmp.setEnd(asc.getChallenge().getEnd());
			tmp.setStateDate(Map.copyOf(asc.getChallenge().getStateDate()));
			tmp.setHide(asc.getChallenge().isHidden());
			singleChallengeReport.add(tmp);
		}

		return singleChallengeReport;
	}

	private List<ChallengeConceptPersistence> readSingleChallenges(String gameId, Date from, Date to, String modelName,
			Boolean hidden) {

		Query query = new Query();
		Criteria criteria = Criteria.where("gameId").is(gameId);

		if (from != null && to != null) {
			criteria = criteria.and("concept.start").gte(from).and(("concept.end")).lte(to);
		} else if (from != null) {
			criteria = criteria.and("concept.start").gte(from);
		} else if (to != null) {
			criteria = criteria.and("concept.end").lte(to);
		}

		if (modelName != null) {
			criteria = criteria.and("concept.modelName").is(modelName);
		}

		if (hidden != null) {
			criteria = criteria.and("concept.visibility.hidden").is(hidden);
		}

		query.addCriteria(criteria);

		return mongoTemplate.find(query, ChallengeConceptPersistence.class);

	}

	private List<ChallengeReportCSVStub> readGroupChallengesCSVStub(String gameId, Date from, Date to,
			String modelName) {

		List<ChallengeReportCSVStub> groupChallengeReport = new ArrayList<>();

		List<GroupChallenge> groupChallenges = readGroupChallenges(gameId, from, to, modelName);

		for (GroupChallenge gc : groupChallenges) {
			ChallengeReportCSVStub tmp = new ChallengeReportCSVStub();
			tmp.setChallengeType(gc.getClass().getSimpleName());
			tmp.setId(gc.getId());
			tmp.setName(gc.getInstanceName());
			tmp.setModelName(gc.getChallengeModel());
			tmp.setGameId(gc.getGameId());
			tmp.setPlayerId(
					gc.getAttendees().stream().map(foo -> foo.getPlayerId()).collect(Collectors.toList()).toString());
			tmp.setStart(gc.getStart());
			tmp.setEnd(gc.getEnd());
			tmp.setStateDate(Map.copyOf(gc.getStateDate()));
			groupChallengeReport.add(tmp);
		}

		List<ArchivedConcept> archivedGroupChallenges = readGroupArchives(gameId, from, to, modelName);

		for (ArchivedConcept agc : archivedGroupChallenges) {
			ChallengeReportCSVStub tmp = new ChallengeReportCSVStub();
			tmp.setChallengeType(agc.getGroupChallenge().getClass().getSimpleName());
			tmp.setId(agc.getId());
			tmp.setName(agc.getGroupChallenge().getInstanceName());
			tmp.setModelName(agc.getGroupChallenge().getChallengeModel());
			tmp.setGameId(agc.getGameId());
			tmp.setPlayerId(agc.getGroupChallenge().getAttendees().stream().map(foo -> foo.getPlayerId())
					.collect(Collectors.toList()).toString());
			tmp.setStart(agc.getGroupChallenge().getStart());
			tmp.setEnd(agc.getGroupChallenge().getEnd());
			tmp.setStateDate(Map.copyOf(agc.getGroupChallenge().getStateDate()));
			groupChallengeReport.add(tmp);
		}

		return groupChallengeReport;

	}

	private List<GroupChallenge> readGroupChallenges(String gameId, Date from, Date to, String modelName) {
		Query query = new Query();
		Criteria criteria = Criteria.where("gameId").is(gameId);

		if (from != null && to != null) {
			criteria = criteria.and("start").gte(from).and(("end")).lte(to);
		} else if (from != null) {
			criteria = criteria.and("start").gte(from);
		} else if (to != null) {
			criteria = criteria.and("end").lte(to);
		}
		if (modelName != null) {
			criteria = criteria.and("challengeModel").is(modelName);
		}

		query.addCriteria(criteria);
		return mongoTemplate.find(query, GroupChallenge.class);

	}

	public ExportCsv getChallengeReportCSV(List<ChallengeReportCSVStub> result, String gameId) {
		DateTimeFormatter ldf = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		LocalDate today = LocalDate.now();
		String filename = "report_challenges" + "_" + gameId + "_" + today.format(ldf) + ".csv";
		StringBuffer sb = new StringBuffer(
				"\"type\",\"challengeId\",\"name\",\"modelName\",\"playerId\",\"start\",\"end\",\"hide\","
						+ ChallengeState.PROPOSED + "," + ChallengeState.ASSIGNED + "," + ChallengeState.ACTIVE + ","
						+ ChallengeState.COMPLETED + "," + ChallengeState.FAILED + "," + ChallengeState.REFUSED + ","
						+ ChallengeState.AUTO_DISCARDED + "," + ChallengeState.CANCELED + "\n");
		result.forEach(challenge -> {
			Date proposed = challenge.getStateDate().containsKey(ChallengeState.PROPOSED)
					? challenge.getStateDate().get(ChallengeState.PROPOSED)
					: null;
			Date assigned = challenge.getStateDate().containsKey(ChallengeState.ASSIGNED)
					? challenge.getStateDate().get(ChallengeState.ASSIGNED)
					: null;
			Date active = challenge.getStateDate().containsKey(ChallengeState.ACTIVE)
					? challenge.getStateDate().get(ChallengeState.ACTIVE)
					: null;
			Date completed = challenge.getStateDate().containsKey(ChallengeState.COMPLETED)
					? challenge.getStateDate().get(ChallengeState.COMPLETED)
					: null;
			Date failed = challenge.getStateDate().containsKey(ChallengeState.FAILED)
					? challenge.getStateDate().get(ChallengeState.FAILED)
					: null;
			Date refused = challenge.getStateDate().containsKey(ChallengeState.REFUSED)
					? challenge.getStateDate().get(ChallengeState.REFUSED)
					: null;
			Date autoDiscarded = challenge.getStateDate().containsKey(ChallengeState.AUTO_DISCARDED)
					? challenge.getStateDate().get(ChallengeState.AUTO_DISCARDED)
					: null;
			Date cancelled = challenge.getStateDate().containsKey(ChallengeState.CANCELED)
					? challenge.getStateDate().get(ChallengeState.CANCELED)
					: null;
			sb.append("\"" + challenge.getChallengeType() + "\",");
			sb.append("\"" + challenge.getId() + "\",");
			sb.append("\"" + challenge.getName() + "\",");
			sb.append("\"" + challenge.getModelName() + "\",");
			sb.append("\"" + challenge.getPlayerId() + "\",");
			sb.append("\"" + challenge.getStart() + "\",");
			sb.append("\"" + challenge.getEnd() + "\",");
			sb.append("\"" + challenge.isHide() + "\",");
			sb.append("\"" + proposed + "\",");
			sb.append("\"" + assigned + "\",");
			sb.append("\"" + active + "\",");
			sb.append("\"" + completed + "\",");
			sb.append("\"" + failed + "\",");
			sb.append("\"" + refused + "\",");
			sb.append("\"" + autoDiscarded + "\",");
			sb.append("\"" + cancelled + "\"\n");
		});
		ExportCsv exportCsv = new ExportCsv();
		exportCsv.setFilename(filename);
		exportCsv.setContentType("text/csv");
		exportCsv.setContent(sb);
		return exportCsv;
	}

	public List<ArchivedConcept> readGroupArchives(String gameId, Date from, Date to, String modelName) {
		Query query = new Query();
		Criteria criteria = Criteria.where("gameId").is(gameId).and("groupChallenge").exists(true);

		if (from != null && to != null) {
			criteria = criteria.and("challenge.start").gte(from).and("challenge.end").lte(to);
		} else if (from != null) {
			criteria = criteria.and("challenge.start").gte(from);
		} else if (to != null) {
			criteria = criteria.and("challenge.end").lte(to);
		}
		if (modelName != null) {
			criteria = criteria.and("challengeModel").is(modelName);
		}

		query.addCriteria(criteria);
		return mongoTemplate.find(query, ArchivedConcept.class);

	}

	private List<ArchivedConcept> readSingleArchives(String gameId, Date from, Date to, String modelName,
			Boolean hidden) {
		Query query = new Query();
		Criteria criteria = Criteria.where("gameId").is(gameId).and("challenge").exists(true);

		if (from != null && to != null) {
			criteria = criteria.and("challenge.start").gte(from).and("challenge.end").lte(to);
		} else if (from != null) {
			criteria = criteria.and("challenge.start").gte(from);
		} else if (to != null) {
			criteria = criteria.and("challenge.end").lte(to);
		}

		if (modelName != null) {
			criteria = criteria.and("challenge.modelName").is(modelName);
		}

		if (hidden != null) {
			criteria = criteria.and("challenge.visibility.hidden").is(hidden);
		}

		query.addCriteria(criteria);
		return mongoTemplate.find(query, ArchivedConcept.class);

	}

	public List<ChallengeConcept> readChallenges(String gameId, String playerId, Boolean inprogress) {

		List<ChallengeConcept> result = new ArrayList<>();
		Query query = new Query();
		Criteria criteria = Criteria.where("gameId").is(gameId).and("playerId").is(playerId);
		if (inprogress) {
			criteria = criteria.and("concept.end").gt(new Date());
		}
		query.addCriteria(criteria);
		List<ChallengeConceptPersistence> singleChallenges = mongoTemplate.find(query,
				ChallengeConceptPersistence.class);

		Query query2 = new Query();
		Criteria criteria2 = Criteria.where("gameId").is(gameId).and("attendees")
				.elemMatch(Criteria.where("playerId").is(playerId));
		if (inprogress) {
			criteria2 = criteria2.and("concept.end").gt(new Date());
		}
		query2.addCriteria(criteria2);
		List<GroupChallenge> groupChallenge = mongoTemplate.find(query2, GroupChallenge.class);

		for (ChallengeConceptPersistence cp : singleChallenges) {
			result.add(cp.getConcept());
		}

		for (GroupChallenge gc : groupChallenge) {
			result.add(gc.toChallengeConcept(playerId));
		}

		return result;
	}
	
}
