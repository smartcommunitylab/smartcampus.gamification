package eu.trentorise.game.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.trentorise.game.core.Clock;
import eu.trentorise.game.core.SystemClock;
import eu.trentorise.game.model.ChallengeUpdate.StateUpdate;
import eu.trentorise.game.model.core.GameConcept;

@JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
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



    /**
     * An higher value refers to a higher priority
     */
    private int priority = 0;

    private boolean forced;
    private Visibility visibility = new Visibility();

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
        PROPOSED, ASSIGNED, ACTIVE, COMPLETED, FAILED, REFUSED, AUTO_DISCARDED, CANCELED
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


    public static class Visibility {
        private boolean hidden = false;
        private Date disclosureDate;

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public Date getDisclosureDate() {
            return disclosureDate;
        }

        public void setDisclosureDate(Date disclosureDate) {
            this.disclosureDate = disclosureDate;
        }


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
        return String.format("{modelName=%s, instance=%s, fields=%s, start=%s, end=%s, state=%s}",
                modelName,
                name, fields, start != null ? dateFormatter.format(start) : null,
                end != null ? dateFormatter.format(end) : null, getState());
    }

    public ChallengeState getState() {
        if (state == null) {
            if (isCompleted()) { // loaded a completed ChallengeConcept <= 2.4.0
                state = ChallengeState.COMPLETED;
                stateDate.put(ChallengeState.COMPLETED, dateCompleted);
            } else if (isFailed()) {
                state = ChallengeState.FAILED;
                stateDate.put(ChallengeState.FAILED, end);
            } else {
                state = DEFAULT_STATE;
                stateDate.put(DEFAULT_STATE, objectCreationDate);
            }
        }
        return state;
    }

    @JsonIgnore
    // FIXME : bad method, remove it, used only to fix a issue in challenge failure schedule
    public ChallengeState persistedState() {
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

        // scenario when play into the past, If a complete a challenge previously failed
        // failed state MUST be removed from history
        resetHistory(ChallengeState.FAILED);
        return true;
    }

    public ChallengeConcept activate() {
        boolean regularCondition = getState() == ChallengeState.ASSIGNED && isActivable();
        boolean playIntoThePast = stateDate.get(ChallengeState.FAILED) != null
                && clock.now().before(stateDate.get(ChallengeState.FAILED)) && isActivable();
        if (regularCondition || playIntoThePast) {
            updateState(ChallengeState.ACTIVE);
        }

        return this;
    }

    private boolean isFailed() {
        return end != null && end.before(clock.now());
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

    /**
     * Check if challenge is activable using configured {@link Clock}
     * 
     * @return true is challenge is activable
     */
    @JsonIgnore
    public boolean isActivable() {
        return isActive(clock.now());
    }

    /**
     * Check if challenge is in {@link ChallengeState} ACTIVE
     * 
     * @return true if challenge is ACTIVE
     */
    @JsonIgnore
    public boolean isActive() {
        return state == ChallengeState.ACTIVE;
    }

    public ChallengeConcept updateState(ChallengeState state) {
        return updateState(state, clock.now());
    }

    public ChallengeConcept updateState(ChallengeState state, Date atDate) {
        if (state == null) {
            throw new IllegalArgumentException("Cannot update to a null state");
        }
        this.state = state;
        stateDate.put(state, atDate);
        return this;
    }

    public ChallengeConcept normalizeState() {
        if (state != ChallengeState.FAILED && stateDate.containsKey(ChallengeState.FAILED)) {
            state = ChallengeState.FAILED;
        }

        return this;
    }

    @JsonIgnore
    public boolean isGroupChallenge() {
        return GroupChallenge.MODELS.contains(modelName);
    }

    public ChallengeConcept forced() {
        this.forced = true;
        return this;
    }

    private void resetHistory(ChallengeState state) {
        stateDate.remove(state);
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStateDate(Map<ChallengeState, Date> stateDate) {
        this.stateDate = stateDate;
    }

    public void setState(ChallengeState state) {
        this.state = state;
    }

    public Map<ChallengeState, Date> getStateDate() {
        return stateDate;
    }

    public boolean isForced() {
        return forced;
    }

    @JsonIgnore
    public boolean isHidden() {
        return visibility.hidden && (visibility.disclosureDate == null
                || clock.now().before(visibility.disclosureDate));
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public ChallengeConcept update(ChallengeUpdate changes) {
        if (name.equals(changes.getName())) {
            if(changes.getStateUpdate() != null) {
                final StateUpdate stateUpdate = changes.getStateUpdate();
                state = stateUpdate.getState();
                stateDate.put(state, stateUpdate.getStateDate() != null ? stateUpdate.getStateDate() : new Date());
            }
            priority = changes.getPriority();
            visibility = changes.getVisibility();
            fields = changes.getFields();
        }
        return this;
    }

}
