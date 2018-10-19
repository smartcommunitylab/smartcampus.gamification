package eu.trentorise.game.model;

import java.util.List;

import eu.trentorise.game.model.GroupChallenge.PointConceptRef;

public class Invitation {

    private String gameId;
    private Player proposer;
    private List<Player> guests;

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

}
