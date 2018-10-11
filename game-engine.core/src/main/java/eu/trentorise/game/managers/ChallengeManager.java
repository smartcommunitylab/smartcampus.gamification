package eu.trentorise.game.managers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.trentorise.game.core.Clock;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.repo.GroupChallengeRepo;
import eu.trentorise.game.services.PlayerService;

@Service
public class ChallengeManager {

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

}
