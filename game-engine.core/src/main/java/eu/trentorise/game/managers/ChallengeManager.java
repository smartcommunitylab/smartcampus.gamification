package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.PlayerService;

@Service
public class ChallengeManager {

    @Autowired
    private PlayerService playerSrv;

    public List<String> conditionCheck(GroupChallenge groupChallenge) {
        List<Attendee> attendees = groupChallenge.getAttendees();
        List<String> playerIds = attendees.stream().map(attendee -> attendee.getPlayerId())
                .collect(Collectors.toList());
        List<PlayerState> playerStates = playerIds.stream()
                .map(id -> playerSrv.loadState(groupChallenge.getGameId(), id, false))
                .filter(state -> state != null)
                .collect(Collectors.toList());
        List<String> winners = winners(playerStates, groupChallenge);

        return winners;

    }


    private List<String> winners(List<PlayerState> states, GroupChallenge groupChallenge) {
        PointConceptRef challengePointConceptRef = groupChallenge.getChallengePointConcept();
        List<String> winnerIds = new ArrayList<>();
        double max = 0;
        for (PlayerState state : states) {
            PointConcept challengePointConcept =
                    state.pointConcept(challengePointConceptRef.getName());
            double challengeScore = challengePointConcept.getPeriodScore(
                    challengePointConceptRef.getPeriod(),
                    instantInChallenge(groupChallenge.getEnd()));
            if (max < challengeScore) {
                max = challengeScore;
                winnerIds.clear();
                winnerIds.add(state.getPlayerId());
            } else if (max == challengeScore) {
                winnerIds.add(state.getPlayerId());
            }
        }
        return winnerIds;
    }

    private long instantInChallenge(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -1);
        return cal.getTime().getTime();
        
    }

}
