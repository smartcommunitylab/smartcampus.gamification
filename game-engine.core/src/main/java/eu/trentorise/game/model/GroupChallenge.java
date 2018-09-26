package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupChallenge {

    private String id;

    private String gameId;
    private String instanceName;
    private List<Attendee> attendees = new ArrayList<>();

    private PointConceptRef challengePointConcept;
    private Reward reward;


    private String state;
    private String origin;
    private Date start;
    private Date end;
    private int priority;


    public static class Reward {
        private double percentage;
        private PointConceptRef calculationPointConcept;
        private PointConceptRef targetPointConcept;

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

        public PointConceptRef getCalculationPointConcept() {
            return calculationPointConcept;
        }

        public void setCalculationPointConcept(PointConceptRef calculationPointConcept) {
            this.calculationPointConcept = calculationPointConcept;
        }

        public PointConceptRef getTargetPointConcept() {
            return targetPointConcept;
        }

        public void setTargetPointConcept(PointConceptRef targetPointConcept) {
            this.targetPointConcept = targetPointConcept;
        }

    }

    public static class Attendee {
        private String playerId;
        private Role role;
        private boolean isWinner;
        private double challengeScore;

        public enum Role {
            PROPOSER, GUEST
        }

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public boolean isWinner() {
            return isWinner;
        }

        public void setWinner(boolean isWinner) {
            this.isWinner = isWinner;
        }

        public double getChallengeScore() {
            return challengeScore;
        }

        public void setChallengeScore(double challengeScore) {
            this.challengeScore = challengeScore;
        }

    }

    public static class PointConceptRef {
        private String name;
        private String period;



        public PointConceptRef(String name, String period) {
            this.name = name;
            this.period = period;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
