package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.trentorise.game.core.Clock;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.Invitation;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.repo.GroupChallengeRepo;
import eu.trentorise.game.services.PlayerService;

@Service
public class ChallengeManager {

    private static final String INVITATION_CHALLENGE_ORIGIN = "player";

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private GroupChallengeRepo groupChallengeRepo;

    @Autowired
    private Clock clock;

    public List<String> conditionCheck(GroupChallenge groupChallenge) {
        List<Attendee> attendees = groupChallenge.getAttendees();
        List<String> playerIds = attendees.stream().map(attendee -> attendee.getPlayerId())
                .collect(Collectors.toList());
        final String gameId = groupChallenge.getGameId();
        List<PlayerState> playerStates = playerIds.stream()
                .map(id -> playerSrv.loadState(gameId, id, false, false))
                .filter(state -> state != null)
                .collect(Collectors.toList());

        groupChallenge = groupChallenge.update(playerStates);
        return groupChallenge.winners().stream().map(winner -> winner.getPlayerId())
                .collect(Collectors.toList());
    }

    public GroupChallenge save(GroupChallenge challenge) {
        return groupChallengeRepo.save(challenge);
    }


    public List<GroupChallenge> completedPerformanceGroupChallenges(String gameId) {
        return groupChallengeRepo.findByGameIdAndStateAndEndBefore(gameId, ChallengeState.ASSIGNED,
                clock.now());
    }

    public void inviteToChallenge(Invitation invitation) {
        // save GroupChallenge
        GroupChallenge groupChallenge = convert(invitation);

        // produce notification to other attendees
        // stats notification
    }

    private GroupChallenge convert(Invitation invitation) {
        GroupChallenge challenge = null;
        if (invitation != null) {
            challenge = new GroupChallenge(ChallengeState.PROPOSED);
            challenge.setChallengePointConcept(invitation.getChallengePointConcept());
            challenge.setOrigin(INVITATION_CHALLENGE_ORIGIN);
            challenge.setStart(invitation.getChallengeStart());
            challenge.setEnd(invitation.getChallengeEnd());
            challenge.setAttendees(attendees(invitation));
            challenge.setGameId(invitation.getGameId());
            challenge.setReward(invitation.getReward());
            challenge.setInstanceName(String.format("p%s_%s",
                    invitation.getProposer().getPlayerId(), UUID.randomUUID().toString()));
        }

        return challenge;
    }

    private List<Attendee> attendees(Invitation invitation) {
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
}
