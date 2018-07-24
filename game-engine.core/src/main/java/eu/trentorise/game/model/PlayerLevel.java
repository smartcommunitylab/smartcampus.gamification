package eu.trentorise.game.model;

import org.springframework.data.annotation.PersistenceConstructor;

public class PlayerLevel {
    private String levelName;
    private String levelValue;
    private String pointConcept;
    private double toNextLevel;

    public PlayerLevel(Level levelDefinition, String levelValue, double toNextLevel) {
        this.levelName = levelDefinition.getName();
        this.pointConcept = levelDefinition.getPointConceptName();
        this.levelValue = levelValue;
        this.toNextLevel = toNextLevel;
    }


    @PersistenceConstructor
    private PlayerLevel(String levelName, String pointConcept, String levelValue,
            double toNextLevel) {
        this.levelName = levelName;
        this.pointConcept = pointConcept;
        this.levelValue = levelValue;
        this.toNextLevel = toNextLevel;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getPointConcept() {
        return pointConcept;
    }

    public double getToNextLevel() {
        return toNextLevel;
    }

    public String getLevelValue() {
        return levelValue;
    }


}
