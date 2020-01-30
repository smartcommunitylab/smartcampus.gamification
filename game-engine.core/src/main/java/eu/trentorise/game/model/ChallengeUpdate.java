package eu.trentorise.game.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeConcept.Visibility;

public class ChallengeUpdate {
    private String name;
    private Map<String, Object> fields = new HashMap<String, Object>();
    private Date start;
    private Date end;
    // private ChallengeState stateUpdate;
    // private Map<ChallengeState, Date> stateDate = new HashMap<>();
    private StateUpdate stateUpdate;
    private int priority = 0;
    private Visibility visibility = new Visibility();



    public static class StateUpdate {
        private ChallengeState state;
        private Date stateDate;

        public ChallengeState getState() {
            return state;
        }

        public void setState(ChallengeState state) {
            this.state = state;
        }

        public Date getStateDate() {
            return stateDate;
        }

        public void setStateDate(Date stateDate) {
            this.stateDate = stateDate;
        }


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

    // public ChallengeState getState() {
    // return stateUpdate;
    // }
    //
    // public void setState(ChallengeState stateUpdate) {
    // this.state = stateUpdate;
    // }
    //
    // public Map<ChallengeState, Date> getStateDate() {
    // return stateDate;
    // }
    //
    // public void setStateDate(Map<ChallengeState, Date> stateDate) {
    // this.stateDate = stateDate;
    // }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StateUpdate getStateUpdate() {
        return stateUpdate;
    }

    public void setStateUpdate(StateUpdate stateUpdate) {
        this.stateUpdate = stateUpdate;
    }


}
