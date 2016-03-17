package eu.trentorise.game.challenges.util;

public class ChallengeRuleRow {

    private String type;
    private String goalType;
    private Object target;
    private String pointType;
    private String selectionCriteria;
    private Integer bonus;

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getGoalType() {
	return goalType;
    }

    public void setGoalType(String goalType) {
	this.goalType = goalType;
    }

    public Object getTarget() {
	return target;
    }

    public void setTarget(Object target) {
	this.target = target;
    }

    public String getPointType() {
	return pointType;
    }

    public void setPointType(String pointType) {
	this.pointType = pointType;
    }

    public String getSelectionCriteria() {
	return selectionCriteria;
    }

    public void setSelectionCriteria(String selectionCriteria) {
	this.selectionCriteria = selectionCriteria;
    }

    public void setBonus(Integer bonus) {
	this.bonus = bonus;
    }

    public Integer getBonus() {
	return bonus;
    }

}
