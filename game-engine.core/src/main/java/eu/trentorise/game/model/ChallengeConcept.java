package eu.trentorise.game.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.trentorise.game.core.Clock;
import eu.trentorise.game.core.SystemClock;
import eu.trentorise.game.model.core.GameConcept;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ChallengeConcept extends GameConcept {
    private String modelName;
    private Map<String, Object> fields = new HashMap<String, Object>();
    private Date start;
    private Date end;

    // metadata fields

    // field use in engine version <= 2.4.0
    private boolean completed = false;
    // field use in engine version <= 2.4.0
    private Date dateCompleted;

    private ChallengeState state;
    private Map<ChallengeState, Date> stateDate = new HashMap<>();
    private String origin;

    @JsonIgnore
    private Date objectCreationDate;

    @JsonIgnore
    private Clock clock;

    @JsonIgnore
    private static final DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @JsonIgnore
    private static final ChallengeState DEFAULT_STATE = ChallengeState.ASSIGNED;

    @JsonIgnore
    private static final Clock DEFAULT_CLOCK = new SystemClock();

    public static enum ChallengeState {
        PROPOSED, ASSIGNED, ACTIVE, COMPLETED, FAILED
    }


    public ChallengeConcept() {
        this.clock = DEFAULT_CLOCK;
    }

    public ChallengeConcept(Clock clock) {
        this(DEFAULT_STATE, clock);
    }

    public ChallengeConcept(ChallengeState state) {
        this(state, DEFAULT_CLOCK);
    }

    public ChallengeConcept(ChallengeState state, Clock clock) {
        this.clock = clock;
        updateState(state == null ? ChallengeState.ASSIGNED : state);
        this.objectCreationDate = clock.now();
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
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

    public Date getDateCompleted() {
        return dateCompleted != null ? dateCompleted : stateDate.get(ChallengeState.COMPLETED);
    }


    @Override
    public String toString() {
        return String.format("{modelName=%s, instance=%s, fields=%s, start=%s, end=%s}", modelName,
                name, fields, start != null ? dateFormatter.format(start) : null,
                end != null ? dateFormatter.format(end) : null);
    }

    public ChallengeState getState() {
        if (state == null) {
            if (isCompleted()) { // loaded a completed ChallengeConcept <= 2.4.0
                state = ChallengeState.COMPLETED;
                stateDate.put(ChallengeState.COMPLETED, dateCompleted);
            } else {
                state = DEFAULT_STATE;
                stateDate.put(DEFAULT_STATE, objectCreationDate);
            }
        }
        return state;
    }

    /**
     * Helper method of challenge
     * 
     * @return true if challenge is completed
     */
    public boolean completed() {
        updateState(ChallengeState.COMPLETED);
        return true;
    }

    public boolean isCompleted() {
        return completed || state == ChallengeState.COMPLETED;
    }

    /**
     * Check if challenge is active in the specified date
     * 
     * @param when a date
     * @return true only if the date is in the range of challenge validity.
     * 
     *         <strong>NOTE</strong> If date is null or invalid the challenge is considered active
     */
    public boolean isActive(Date when) {
        boolean startCondition = start == null || when == null || start.before(when);
        boolean endCondition = end == null || when == null || end.after(when);
        return startCondition && endCondition;
    }

    /**
     * Check if challenge is active in the specified timestamp
     * 
     * @param when a timestamp in millis
     * @return true only if the timestamp is in the range of challenge validity.
     * 
     *         <strong>NOTE</strong> If timestamp is less than zero the challenge is considered
     *         active
     */
    public boolean isActive(long when) {
        return when < 0 || isActive(new Date(when));

    }

    public ChallengeConcept updateState(ChallengeState state) {
        if (state == null) {
            throw new IllegalArgumentException("Cannot update to a null state");
        }
        this.state = state;
        stateDate.put(state, clock.now());
        return this;
    }

    public Date getDate(ChallengeState state) {
        return stateDate.get(state);
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

}
