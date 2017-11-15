package eu.trentorise.game.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eu.trentorise.game.model.core.GameConcept;

public class ChallengeConcept extends GameConcept {
    private String modelName;
    private Map<String, Object> fields = new HashMap<String, Object>();
    private Date start;
    private Date end;

    // metadata fields
    private boolean completed = false;
    private Date dateCompleted;

    private static final DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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
        return dateCompleted;
    }


    @Override
    public String toString() {
        return String.format("{modelName=%s, instance=%s, fields=%s, start=%s, end=%s}", modelName,
                name, fields, start != null ? dateFormatter.format(start) : null,
                end != null ? dateFormatter.format(end) : null);
    }

    /**
     * Helper method of challenge
     * 
     * @return true if challenge is completed
     */
    public boolean completed() {
        completed = true;
        dateCompleted = new Date();
        return true;
    }

    public boolean isCompleted() {
        return completed;
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

}
