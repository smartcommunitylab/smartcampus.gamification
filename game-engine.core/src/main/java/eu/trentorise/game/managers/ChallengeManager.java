package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.trentorise.game.core.Clock;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeInvitation;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.notification.ChallengeCompletedNotication;
import eu.trentorise.game.notification.ChallengeFailedNotication;
import eu.trentorise.game.notification.ChallengeInvitationAcceptedNotification;
import eu.trentorise.game.notification.ChallengeInvitationCanceledNotification;
import eu.trentorise.game.notification.ChallengeInvitationNotification;
import eu.trentorise.game.notification.ChallengeInvitationRefusedNotification;
import eu.trentorise.game.repo.GroupChallengeRepo;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@Service
public class ChallengeManager {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(ChallengeManager.class);
    private static final String INVITATION_CHALLENGE_ORIGIN = "player";

    private static final int LIMIT_INVITATIONS_AS_PROPOSER = 1;
    private static final int LIMIT_INVITATIONS_AS_GUEST = 3;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private GroupChallengeRepo groupChallengeRepo;

    @Autowired
    private Clock clock;

    @Autowired
    private GameService gameSrv;

    @Autowired
    private NotificationManager notificationSrv;

    @Autowired
    private ArchiveManager archiveSrv;

    public List<String> conditionCheck(GroupChallenge groupChallenge) {
        if (groupChallenge.getChallengeModel()
                .equals(GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE)) { // check if I need
                                                                              // this behavior in
                                                                              // best performance
        List<Attendee> attendees = groupChallenge.getAttendees();
        List<String> playerIds = attendees.stream().map(attendee -> attendee.getPlayerId())
                .collect(Collectors.toList());
        final String gameId = groupChallenge.getGameId();
        List<PlayerState> playerStates =
                playerIds.stream().map(id -> playerSrv.loadState(gameId, id, false, false))
                        .filter(state -> state != null).collect(Collectors.toList());

        groupChallenge = groupChallenge.update(playerStates);
        }
        return groupChallenge.winners().stream().map(winner -> winner.getPlayerId())
                .collect(Collectors.toList());
    }

    public GroupChallenge save(GroupChallenge challenge) {
        if (challenge != null) {
            // ATTENTION: some issues with tests
            Game game = gameSrv.loadGameDefinitionById(challenge.getGameId());
            challenge.validate(game);
            if (StringUtils.isBlank(challenge.getInstanceName())) {
                Attendee proposer = challenge.proposer();
                challenge.setInstanceName(
                        String.format("p_%s_%s", proposer != null ? proposer.getPlayerId() : null,
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
                    StatsLogger.logChallengeCompleted(game.getDomain(), game.getId(),
                            a.getPlayerId(), executionId, challenge.getEnd().getTime(), timestamp,
                            challenge.getInstanceName());
                } else {
                    StatsLogger.logChallengeFailed(game.getDomain(), game.getId(), a.getPlayerId(),
                            executionId, challenge.getEnd().getTime(), timestamp,
                            challenge.getInstanceName());
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
                    notificationSrv.notificate(notification);
                } else {
                    ChallengeFailedNotication notification = new ChallengeFailedNotication();
                    notification.setChallengeName(challenge.getInstanceName());
                    notification.setGameId(challenge.getGameId());
                    notification.setPlayerId(a.getPlayerId());
                    notificationSrv.notificate(notification);
                }
            });
        }
    }


    public List<GroupChallenge> completedPerformanceGroupChallenges(String gameId) {
        return groupChallengeRepo.findByGameIdAndStateAndEndBeforeAndChallengeModel(gameId,
                ChallengeState.ASSIGNED, clock.now(),
                GroupChallenge.MODEL_NAME_COMPETITIVE_PERFORMANCE);
    }

    public List<GroupChallenge> activeGroupChallengesByDate(String gameId, String playerId,
            Date atDate) {
        return groupChallengeRepo.activeGroupChallenges(gameId, playerId, atDate);
    }

    public List<GroupChallenge> groupChallengeToFail(String gameId, Date atDate) {
        return groupChallengeRepo.groupChallengesToFail(gameId, atDate);
    }

    public GroupChallenge inviteToChallenge(ChallengeInvitation invitation) {
        if (invitation != null) {

            invitation.validate();
            // check if player has sent other invitations
            List<GroupChallenge> proposerInvitations = groupChallengeRepo.proposerInvitations(
                    invitation.getGameId(), invitation.getProposer().getPlayerId());
            if (proposerInvitations.size() >= LIMIT_INVITATIONS_AS_PROPOSER) {
                throw new IllegalArgumentException(String.format(
                        "player %s already has %s pending invitations as proposer",
                        invitation.getProposer().getPlayerId(), LIMIT_INVITATIONS_AS_PROPOSER));
            }

            // check if player has received max 3 invitations
            invitation.getGuests().forEach(guest -> {
                List<GroupChallenge> guestInvitations = groupChallengeRepo
                        .guestInvitations(invitation.getGameId(), guest.getPlayerId());
                if (guestInvitations.size() >= LIMIT_INVITATIONS_AS_GUEST) {
                    throw new IllegalArgumentException(
                            String.format("player %s already has %s pending invitations as guest",
                                    guest.getPlayerId(), LIMIT_INVITATIONS_AS_GUEST));
                }
                PlayerState state = playerSrv.loadState(invitation.getGameId(), guest.getPlayerId(),
                        true, false);
                long assignedCounter =
                        state.challenges().stream()
                                .filter(c -> c.getState() == ChallengeState.ASSIGNED
                                        && insideChallengeTime(invitation.getChallengeStart(), c))
                                .count();
                if (assignedCounter > 0) {
                    throw new IllegalArgumentException(String.format(
                            "player %s already has ASSIGNED challenge in the invitation period",
                            guest.getPlayerId()));
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
                ChallengeInvitationNotification invitationNotification =
                        new ChallengeInvitationNotification();
                invitationNotification.setGameId(invitation.getGameId());
                invitationNotification.setPlayerId(guest.getPlayerId());
                invitationNotification.setProposerId(proposerId);
                notificationSrv.notificate(invitationNotification);
                LogHub.info(invitation.getGameId(), logger,
                        String.format("Player %s invites player %s to a challenge", proposerId,
                                guest.getPlayerId()));
                StatsLogger.logInviteToChallenge(game.getDomain(), game.getId(), proposerId,
                        inviteExecutionId, executionTime, executionTime, guest.getPlayerId(),
                        groupChallenge.getInstanceName(), groupChallenge.getChallengeModel());
            });
            return groupChallenge;
        }
        return null;
    }

    private boolean insideChallengeTime(Date startInvitationChallenge, ChallengeConcept assigned) {
        final Date start = assigned.getStart();
        final Date end = assigned.getEnd();

        if (start != null && end != null) {
            Interval validityTimeChallenge = new Interval(start.getTime(), end.getTime());
            return validityTimeChallenge.contains(startInvitationChallenge.getTime());
        } else if (start == null) {
            return startInvitationChallenge.before(end);
        } else if (end == null) {
            return startInvitationChallenge.after(start);
        }
        return false;
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
                challenge.setInstanceName(String.format("p_%s_%s",
                        invitation.getProposer().getPlayerId(), UUID.randomUUID().toString()));
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
        List<GroupChallenge> guestInvitations =
                groupChallengeRepo.guestInvitations(gameId, playerId);
        GroupChallenge pendingInvitation = guestInvitations.stream()
                .filter(invitation -> invitation.getInstanceName().equals(challengeName))
                .findFirst().orElse(null);
        if (pendingInvitation != null) {
            pendingInvitation.updateState(ChallengeState.ASSIGNED);
            save(pendingInvitation);
            LogHub.info(gameId, logger, String.format(
                    "Player %s has accepted invitation for challenge %s", playerId, challengeName));
            Attendee challengeProposer = pendingInvitation.proposer();
            if (challengeProposer != null) {
                ChallengeInvitationAcceptedNotification acceptedNotification =
                        new ChallengeInvitationAcceptedNotification();
                acceptedNotification.setGameId(gameId);
                acceptedNotification.setPlayerId(challengeProposer.getPlayerId());
                acceptedNotification.setChallengeName(pendingInvitation.getInstanceName());
                acceptedNotification.setGuestId(playerId);
                notificationSrv.notificate(acceptedNotification);
                Game game = gameSrv.loadGameDefinitionById(gameId);
                final String executionId = UUID.randomUUID().toString();
                final long executionTime = clock.nowAsMillis();
                StatsLogger.logChallengeInvitationAccepted(game.getDomain(), gameId, playerId,
                        executionId, executionTime, executionTime,
                        pendingInvitation.getInstanceName(), pendingInvitation.getChallengeModel());
            }

            triggerOnProposedChallenges(guestState);
            if (challengeProposer != null) {
                PlayerState proposerState =
                        playerSrv.loadState(gameId, challengeProposer.getPlayerId(), true, false);
                triggerOnProposedChallenges(proposerState);
            }

            return pendingInvitation;
        } else {
            throw new IllegalArgumentException(String.format(
                    "Challenge %s is not PROPOSED for guest player %s", challengeName, playerId));
        }
    }

    private void triggerOnProposedChallenges(PlayerState playerState) {
        final String gameId = playerState.getGameId();
        final String playerId = playerState.getPlayerId();
        final Game game = gameSrv.loadGameDefinitionById(gameId);
        // trigger archiving of other PROPOSED challenges
        java.util.Iterator<ChallengeConcept> iterator = playerState.challenges().iterator();
        while (iterator.hasNext()) {
            ChallengeConcept ch = iterator.next();
            if (ch.getState() == ChallengeState.PROPOSED) {
                ChallengeConcept removedChallenge =
                        playerState.removeConcept(ch.getName(), ChallengeConcept.class);
                removedChallenge.updateState(ChallengeState.REFUSED);
                archiveSrv.moveToArchive(gameId, playerId, removedChallenge);
            }
        }
        playerSrv.saveState(playerState);

        List<GroupChallenge> otherProposedchallenges =
                groupChallengeRepo.playerGroupChallenges(gameId, playerId, ChallengeState.PROPOSED);
        groupChallengeRepo.delete(otherProposedchallenges);
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
            challenge.guests().stream()
                    .filter(g -> g.getPlayerId().equals(playerId)).findFirst()
                    .ifPresent(guest -> {
                        sendRefusedNotification(guest.getPlayerId(), challenge);
                        StatsLogger.logChallengeInvitationRefused(game.getDomain(), gameId,
                                playerId, executionId, executionTime, executionTime,
                                challenge.getInstanceName(), challenge.getChallengeModel());
                    });

        });
    }

    public GroupChallenge refuseInvitation(String gameId, String playerId, String challengeName) {
        GroupChallenge refused =
                groupChallengeRepo.deleteProposedChallengeByGuest(gameId, playerId, challengeName);

        if (refused == null) {
            throw new IllegalArgumentException(String.format(
                    "Challenge %s is not PROPOSED for guest player %s", challengeName, playerId));
        }
        Game game = gameSrv.loadGameDefinitionById(gameId);
        refused.updateState(ChallengeState.REFUSED);
        archiveSrv.moveToArchive(gameId, refused);
        sendRefusedNotification(playerId, refused);
        final String executionId = UUID.randomUUID().toString();
        final long executionTime = System.currentTimeMillis();
        StatsLogger.logChallengeInvitationRefused(game.getDomain(), gameId, playerId, executionId,
                executionTime, executionTime, challengeName, refused.getChallengeModel());
        LogHub.info(gameId, logger, String.format(
                "Invitation to challenge %s is refused by player %s", challengeName, playerId));
        return refused;
    }

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

    public GroupChallenge cancelInvitation(String gameId, String playerId, String challengeName) {
        GroupChallenge canceled = groupChallengeRepo.deleteProposedChallengeByProposer(gameId,
                playerId, challengeName);

        if (canceled == null) {
            throw new IllegalArgumentException(String.format(
                    "Challenge %s is not PROPOSED by proposer player %s", challengeName, playerId));
        }
        sendCanceledNotifications(canceled);
        canceled.updateState(ChallengeState.CANCELED);
        archiveSrv.moveToArchive(gameId, canceled);
        final Game game = gameSrv.loadGameDefinitionById(gameId);
        LogHub.info(gameId, logger, String.format(
                "Invitation to challenge %s canceled by player %s", challengeName, playerId));
        final String executionId = UUID.randomUUID().toString();
        final long executionTime = System.currentTimeMillis();
        StatsLogger.logChallengeInvitationCanceled(game.getDomain(), gameId, playerId, executionId,
                executionTime, executionTime, challengeName, canceled.getChallengeModel());
        return canceled;
    }

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
}
