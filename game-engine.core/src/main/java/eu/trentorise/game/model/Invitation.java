package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.trentorise.game.model.GroupChallenge.PointConceptRef;

public class Invitation {

    private String gameId;
    private Player proposer;
    private List<Player> guests = new ArrayList<>();

    private Date challengeStart;
    private Date challengeEnd;
    private PointConceptRef challengePointConcept;
    private Reward reward;

    public static class Player {
        private String playerId;

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Player getProposer() {
        return proposer;
    }

    public void setProposer(Player proposer) {
        this.proposer = proposer;
    }

    public List<Player> getGuests() {
        return guests;
    }

    public void setGuests(List<Player> guests) {
        this.guests = guests;
    }

    public PointConceptRef getChallengePointConcept() {
        return challengePointConcept;
    }

    public void setChallengePointConcept(PointConceptRef challengePointConcept) {
        this.challengePointConcept = challengePointConcept;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Date getChallengeStart() {
        return challengeStart;
    }

    public void setChallengeStart(Date challengeStart) {
        this.challengeStart = challengeStart;
    }

    public Date getChallengeEnd() {
        return challengeEnd;
    }

    public void setChallengeEnd(Date challengeEnd) {
        this.challengeEnd = challengeEnd;
    }

}
