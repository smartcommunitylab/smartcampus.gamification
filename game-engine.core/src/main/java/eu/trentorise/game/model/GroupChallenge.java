package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Predicate;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;
import eu.trentorise.game.model.core.GameConcept;

public class GroupChallenge {

    @JsonIgnore
    public static final String MODEL_NAME_COMPETITIVE_PERFORMANCE = "groupCompetitivePerformance";

    @JsonIgnore
    public static final String MODEL_NAME_COMPETITIVE_TIME = "groupCompetitiveTime";

    @JsonIgnore
    public static final String MODEL_NAME_COOPERATIVE = "groupCooperative";

    @JsonIgnore
    public static final List<String> MODELS =
            Arrays.asList(MODEL_NAME_COMPETITIVE_PERFORMANCE, MODEL_NAME_COMPETITIVE_TIME,
                    MODEL_NAME_COOPERATIVE);

    private String id;

    private String gameId;
    private String instanceName;
    private List<Attendee> attendees = new ArrayList<>();

    private String challengeModel;
    private PointConceptRef challengePointConcept;
    private double challengeTarget = -1;
    private Reward reward;

    private ChallengeState state;
    private Map<ChallengeState, Date> stateDate = new HashMap<>();
    private String origin;
    private Date start;
    private Date end;
    private int priority;

    public GroupChallenge() {
        init(null);
    }
    public GroupChallenge(ChallengeState initialState) {
        init(initialState);
    }

    private void init(ChallengeState initialState) {
        if (initialState == null) {
            initialState = ChallengeState.ASSIGNED;
        }

        state = initialState;
        stateDate.put(state, new Date());

    }


    public GroupChallenge update(PlayerState attendeeState, long executionMoment) {
        Optional<Attendee> player = attendees.stream()
                .filter(a -> attendeeState.getPlayerId().equals(a.getPlayerId())).findFirst();
        player.ifPresent(a -> {
            double score = challengeScore(attendeeState, challengePointConcept, executionMoment);
            double scoreToAssign = score;
            double howMuchToWin = challengeTarget;
            if (MODEL_NAME_COOPERATIVE.equalsIgnoreCase(challengeModel)) {
                double othersScore = attendees.stream().filter(
                        attendee -> !attendee.getPlayerId().equals(attendeeState.getPlayerId()))
                        .mapToDouble(attendee -> attendee.getChallengeScore()).sum();
                howMuchToWin = challengeTarget - othersScore;
            }
            if (scoreToAssign > howMuchToWin) {
                scoreToAssign = howMuchToWin;
            }
            a.setChallengeScore(scoreToAssign);

        });
        return this;
    }

    public GroupChallenge update(List<PlayerState> attendeeStates, long executionMoment) {
        attendees.forEach(attendee -> {
            attendee.setChallengeScore(
                    challengeScore(attendee.getPlayerId(), challengePointConcept, attendeeStates,
                            executionMoment));
        });
        return this;
    }

    public GroupChallenge update(List<PlayerState> attendeeStates) {
        attendees.forEach(attendee -> {
            attendee.setChallengeScore(challengeScore(attendee.getPlayerId(), challengePointConcept,
                    attendeeStates, instantInChallenge(end)));
        });
        return this;
    }

    public List<Attendee> winners() {
        List<String> winnerIds = new ArrayList<>();
        switch (challengeModel) {
            case MODEL_NAME_COMPETITIVE_PERFORMANCE:
                double max = 0;
                for (Attendee attendee : attendees) {
                    if (max < attendee.getChallengeScore()) {
                        max = attendee.getChallengeScore();
                        winnerIds.clear();
                        winnerIds.add(attendee.getPlayerId());
                    } else if (max == attendee.getChallengeScore()) {
                        winnerIds.add(attendee.getPlayerId());
                    }
                }
                break;
            case MODEL_NAME_COMPETITIVE_TIME:
                for (Attendee attendee : attendees) {
                    if (attendee.getChallengeScore() >= challengeTarget) {
                        winnerIds.add(attendee.getPlayerId());
                    }
                }
                break;
            case MODEL_NAME_COOPERATIVE:
                double teamScore =
                        attendees.stream().map(a -> a.getChallengeScore()).reduce(0D, Double::sum);
                if (teamScore >= challengeTarget) {
                    winnerIds = attendees.stream().map(Attendee::getPlayerId)
                            .collect(Collectors.toList());
                }
                break;
            default:
                break;
        }

        winnerIds.forEach(id -> {
            attendees.stream().filter(a -> a.getPlayerId().equals(id)).findFirst()
                    .ifPresent(a -> a.setWinner(true));
        });
        return attendees.stream().filter(a -> a.isWinner()).collect(Collectors.toList());
    }

    public GroupChallenge updateState(ChallengeState state) {
        return updateState(state, new Date());
    }

    public GroupChallenge updateState(ChallengeState state, Date atDate) {
        this.state = state;
        stateDate.put(state, atDate);
        return this;
    }

    private double challengeScore(String playerId, PointConceptRef pointConcept,
            List<PlayerState> attendeeStates, long executionMoment) {
        Optional<PlayerState> playerState = attendeeStates.stream().filter(state -> state.getPlayerId().equals(playerId)).findFirst();
        
        return playerState.map(state -> challengeScore(state, pointConcept, executionMoment))
                .orElseThrow(() -> new IllegalArgumentException(
                String.format("attendeeStates doesn't contain player %s", playerId)));

    }

    private double challengeScore(PlayerState state, PointConceptRef pointConcept,
            long executionMoment) {
        PointConcept challengePointConceptState = state.pointConcept(pointConcept.getName());
        return challengePointConceptState.getPeriodScore(pointConcept.getPeriod(), executionMoment);
    }

    private long instantInChallenge(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -1);
        return cal.getTime().getTime();

    }

    public ChallengeConcept toChallengeConcept(String playerId) {
        ChallengeConcept ch = new ChallengeConcept();
        Optional<Attendee> player =
                attendees.stream().filter(a -> a.getPlayerId().equals(playerId)).findFirst();
        player.ifPresent(p -> {
            ch.setModelName(challengeModel);
            ch.setName(instanceName);
            ch.setStart(start);
            ch.setEnd(end);
            ch.setOrigin(origin);
            ch.setPriority(priority);
            setChallengeState(ch, p);
            setFields(ch, p);
        });
        return ch;
    }

    public Attendee proposer() {
        return attendees.stream().filter(a -> a.getRole() == Role.PROPOSER).findFirst()
                .orElse(null);
    }

    public List<Attendee> guests() {
        return attendees.stream().filter(a -> a.getRole() == Role.GUEST)
                .collect(Collectors.toList());

    }

    public void validate(Game game) {
        if (StringUtils.isBlank(challengeModel)) {
            throw new IllegalArgumentException(String.format("challengeModel cannot be blank"));
        } else if (!GroupChallenge.MODELS.contains(challengeModel)) {
            throw new IllegalArgumentException(String
                    .format("challengeModel %s not supported for invitation", challengeModel));
        }
        if (StringUtils.isBlank(gameId)) {
            throw new IllegalArgumentException(String.format("gameId cannot be blank"));
        }
        if (attendees.isEmpty()) {
            throw new IllegalArgumentException(String.format("attendees couldn't be empty"));
        }

        validateChallengePointConcept(game, challengePointConcept);

        if (reward != null) {
            reward.validate(game);
        }
    }

    private static void validateChallengePointConcept(Game game,
            PointConceptRef challengePointConcept) {
        if (challengePointConcept == null) {
            throw new IllegalArgumentException("challengePointConcept is required");
        }
        validatePointConcept(game, challengePointConcept);
    }

    private static void validatePointConcept(Game game, PointConceptRef pointConceptRef) {
        PointConcept pointConcept = (PointConcept) game.getConcepts().stream()
                .filter(foundPointConcept(pointConceptRef.getName())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("pointconcept %s not defined in game %s",
                                pointConceptRef.getName(), game.getId())));

        if (pointConceptRef.getPeriod() != null
                && pointConcept.getPeriod(pointConceptRef.getPeriod()) == null) {
            throw new IllegalArgumentException(
                    String.format("period %s not existent in pointConcept %s",
                            pointConceptRef.getPeriod(), pointConceptRef.getName()));
        }
    }

    private static Predicate<GameConcept> foundPointConcept(String name) {
        return concept -> concept.getClass() == PointConcept.class
                && concept.getName().equals(name);
    }

    private ChallengeConcept setFields(ChallengeConcept challenge, Attendee player) {
        challenge.getFields().put("challengeScoreName",
                challengePointConcept != null ? challengePointConcept.getName() : null);
        challenge.getFields().put("challengeScore", player.getChallengeScore());
        challenge.getFields().put("isWinner", player.isWinner());
        List<Map<String, Object>> otherAttendeeScores = new ArrayList<>();
        attendees.stream().filter(a -> !a.getPlayerId().equals(player.getPlayerId())).forEach(a -> {
            Map<String, Object> attendeeScore = new HashMap<>();
            attendeeScore.put("playerId", a.getPlayerId());
            attendeeScore.put("challengeScore", a.getChallengeScore());
            otherAttendeeScores.add(attendeeScore);
        });
        if (reward != null) {
            challenge.getFields().put("rewardPercentage", reward.getPercentage());
            challenge.getFields().put("rewardThreshold", reward.getThreshold());
            Double attendeeReward = reward.getBonusScore().get(player.getPlayerId());
            challenge.getFields().put("rewardBonusScore",
                    attendeeReward != null ? attendeeReward : 0d);
            List<Map<String, Object>> othersBonusScore = new ArrayList<>();
            attendees.stream().filter(a -> !a.getPlayerId().equals(player.getPlayerId()))
                    .forEach(a -> {
                Map<String, Object> attendeeBonus = new HashMap<>();
                Double bonusScore = reward.getBonusScore().get(a.getPlayerId());
                if (bonusScore != null) {
                    attendeeBonus.put(a.getPlayerId(), bonusScore);
                    othersBonusScore.add(attendeeBonus);
                            challenge.getFields().put("challengerBonusScore", bonusScore);
                }
            });
            challenge.getFields().put("othersBonusScore", othersBonusScore);
        }

        if (challengePointConcept != null) {
            challenge.getFields().put("challengePointConceptName", challengePointConcept.getName());
            challenge.getFields().put("challengePointConceptPeriod",
                    challengePointConcept.getPeriod());
        }
        challenge.getFields().put("challengeTarget", challengeTarget);
        challenge.getFields().put("otherAttendeeScores", otherAttendeeScores);
        Attendee proposer = proposer();
        if(proposer != null) {
            challenge.getFields().put("proposer", proposer.getPlayerId());
        }
        return challenge;

    }
    private ChallengeConcept setChallengeState(ChallengeConcept challenge, Attendee attendee) {
        challenge.setState(state);
        copyStateStoryIntoChallenge(ChallengeState.PROPOSED, challenge);
        copyStateStoryIntoChallenge(ChallengeState.ASSIGNED, challenge);
        copyStateStoryIntoChallenge(ChallengeState.FAILED, challenge);
        
        if (challenge.getState() == ChallengeState.COMPLETED) {
            if(attendee.isWinner){
                Date completedDate = stateDate.get(ChallengeState.COMPLETED);
                challenge.updateState(ChallengeState.COMPLETED, completedDate);
            } else {
                Date completedDate = stateDate.get(ChallengeState.COMPLETED);
                // failure date for player is set to completed date for groupChallenge object
                // completed date for groupChallenge is set to completionCheck of challenge
                challenge.updateState(ChallengeState.FAILED, completedDate);
            }
        }

        return challenge;

    }

    private void copyStateStoryIntoChallenge(ChallengeState state, ChallengeConcept challenge) {
        final Date dateOfTheState = stateDate.get(state);
        if (dateOfTheState != null) {
            challenge.getStateDate().put(state, dateOfTheState);
        }
    }

    public static class Attendee {
        private String playerId;
        private Role role;
        private boolean isWinner;
        private double challengeScore;
        private Date valuationTime;

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

        public Date getValuationTime() {
            return valuationTime;
        }

        public void setValuationTime(Date valuationTime) {
            this.valuationTime = valuationTime;
        }

    }

    public static class PointConceptRef {
        private String name;
        private String period;

        public PointConceptRef() {
        }

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

        @Override
        public String toString() {
            return String.format("{name=%s, period=%s}", name, period);
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

    public ChallengeState getState() {
        return state;
    }

    public void setState(ChallengeState state) {
        this.state = state;
    }

    public Map<ChallengeState, Date> getStateDate() {
        return stateDate;
    }

    public void setStateDate(Map<ChallengeState, Date> stateDate) {
        this.stateDate = stateDate;
    }

    public String getChallengeModel() {
        return challengeModel;
    }

    public void setChallengeModel(String challengeModel) {
        this.challengeModel = challengeModel;
    }

    public double getChallengeTarget() {
        return challengeTarget;
    }

    public void setChallengeTarget(double challengeTarget) {
        this.challengeTarget = challengeTarget;
    }
}
