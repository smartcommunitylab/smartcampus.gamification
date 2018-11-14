package eu.trentorise.game.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupChallengeDTO {
    private String gameId;
    private String instanceName;
    private List<AttendeeDTO> attendees;

    private PointConceptDTO challengePointConcept;
    private double challengeTarget = -1;
    private RewardDTO reward;
    private String challengeModelName;

    private String state;
    private String origin;
    private Date start;
    private Date end;
    private int priority;


    public static class RewardDTO {
        private double percentage;
        private double threshold;
        private Map<String, Double> bonusScore = new HashMap<>();
        private PointConceptDTO calculationPointConcept;
        private PointConceptDTO targetPointConcept;

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

        public PointConceptDTO getCalculationPointConcept() {
            return calculationPointConcept;
        }

        public void setCalculationPointConcept(PointConceptDTO calculationPointConcept) {
            this.calculationPointConcept = calculationPointConcept;
        }

        public PointConceptDTO getTargetPointConcept() {
            return targetPointConcept;
        }

        public void setTargetPointConcept(PointConceptDTO targetPointConcept) {
            this.targetPointConcept = targetPointConcept;
        }

        public double getThreshold() {
            return threshold;
        }

        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }

        public Map<String, Double> getBonusScore() {
            return bonusScore;
        }

        public void setBonusScore(Map<String, Double> bonusScore) {
            this.bonusScore = bonusScore;
        }


    }

    public static class AttendeeDTO {
        private String playerId;
        private String role;


        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }


    }

    public static class PointConceptDTO {
        private String name;
        private String period;

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

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public List<AttendeeDTO> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<AttendeeDTO> attendees) {
        this.attendees = attendees;
    }

    public PointConceptDTO getChallengePointConcept() {
        return challengePointConcept;
    }

    public void setChallengePointConcept(PointConceptDTO challengePointConcept) {
        this.challengePointConcept = challengePointConcept;
    }

    public RewardDTO getReward() {
        return reward;
    }

    public void setReward(RewardDTO reward) {
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getChallengeModelName() {
        return challengeModelName;
    }

    public void setChallengeModelName(String challengeModelName) {
        this.challengeModelName = challengeModelName;
    }

    public double getChallengeTarget() {
        return challengeTarget;
    }

    public void setChallengeTarget(double challengeTarget) {
        this.challengeTarget = challengeTarget;
    }

}
