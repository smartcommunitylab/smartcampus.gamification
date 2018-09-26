package eu.trentorise.game.bean;

import java.util.Date;
import java.util.List;

public class GroupChallengeDTO {
    private String instanceName;
    // private String modelName; ??
    private List<AttendeeDTO> attendees;

    private PointConceptDTO challengePointConcept;
    private RewardDTO reward;


    private String state;
    private String origin;
    private Date start;
    private Date end;
    private int priority;


    public static class RewardDTO {
        private double percentage;
        private PointConceptDTO calculationPointConcept;
        private PointConceptDTO targetPointConcept;
    }

    public static class AttendeeDTO {
        private String playerId;
        private Role role;

        public enum Role {
            PROPOSER, GUEST
        }
    }

    public static class PointConceptDTO {
        private String name;
        private String period;


    }

}
