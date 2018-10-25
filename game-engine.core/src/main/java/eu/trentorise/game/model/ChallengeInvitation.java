package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import eu.trentorise.game.model.GroupChallenge.PointConceptRef;

public class ChallengeInvitation {

    private String gameId;
    private Player proposer;
    private List<Player> guests = new ArrayList<>();

    private String challengeModelName;
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

    public String getChallengeModelName() {
        return challengeModelName;
    }

    public void setChallengeModelName(String challengeModelName) {
        this.challengeModelName = challengeModelName;
    }

    public void validate() {
        if (StringUtils.isBlank(challengeModelName)) {
            throw new IllegalArgumentException(String.format("challengeModel cannot be blank"));
        } else if (!GroupChallenge.MODELS.contains(challengeModelName)) {
            throw new IllegalArgumentException(String
                    .format("challengeModel %s not supported for invitation", challengeModelName));
        }
        if (StringUtils.isBlank(gameId)) {
            throw new IllegalArgumentException(String.format("gameId cannot be blank"));
        }
        if (proposer == null || StringUtils.isBlank(proposer.getPlayerId())) {
            throw new IllegalArgumentException(
                    String.format("proposer is null or playerId is unset"));
        }

        if (CollectionUtils.isEmpty(guests)) {
            throw new IllegalArgumentException(
                    String.format("guests should contain at least one element"));
        }
    }

}
