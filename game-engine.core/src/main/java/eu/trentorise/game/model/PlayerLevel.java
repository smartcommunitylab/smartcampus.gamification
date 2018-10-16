package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.PersistenceConstructor;

import eu.trentorise.game.model.Level.Threshold;

public class PlayerLevel {
    private String levelName;
    private String levelValue;
    private int levelIndex;
    private String pointConcept;
    private double startLevelScore;
    private double endLevelScore;
    private double toNextLevel;


    private static final double INFINITE_LEVEL_FLAG = -1;

    public PlayerLevel(Level levelDefinition, double actualScore) {
        this.levelName = levelDefinition.getName();
        this.pointConcept = levelDefinition.getPointConceptName();

        final List<Threshold> levelRange = levelRange(levelDefinition, actualScore);
        this.levelValue = levelRange.get(0).getName();
        this.levelIndex = levelRange.get(0).getIndex();
        this.startLevelScore = levelRange.get(0).getValue();
        this.endLevelScore =
                isNeverEndingLevel(levelRange) ? INFINITE_LEVEL_FLAG : levelRange.get(1).getValue();
        this.toNextLevel = isNeverEndingLevel(levelRange) ? 0 : (endLevelScore - actualScore);
    }

    private boolean isNeverEndingLevel(List<Threshold> levelRange) {
        return levelRange.size() == 1;
    }

    private List<Threshold> levelRange(Level levelDefinition, double actualScore) {
        final List<Threshold> thresholds = levelDefinition.getThresholds();
        List<Threshold> levelRange = new ArrayList<>();

        Threshold actualThreshold = thresholds.stream()
                .filter(thres -> thres.getValue() <= actualScore)
                .collect(Collectors.toCollection(java.util.LinkedList::new)).getLast();

        if (actualThreshold != null) {
            int thresholdIdx = thresholds.indexOf(actualThreshold);
            actualThreshold.setIndex(thresholdIdx);
            levelRange.add(actualThreshold);
            int nextThresholdIdx = thresholdIdx + 1;
            if (nextThresholdIdx < thresholds.size()) {
                Threshold nextThreshold = thresholds.get(nextThresholdIdx);
                nextThreshold.setIndex(nextThresholdIdx);
                levelRange.add(nextThreshold);
            }
        }
        return levelRange;
    }

    @PersistenceConstructor
    private PlayerLevel(String levelName, String pointConcept, String levelValue,
            Double toNextLevel, Double startLevelScore, Double endLevelScore) {
        this.levelName = levelName;
        this.pointConcept = pointConcept;
        this.levelValue = levelValue;

        // check if value is persisted or give a valid one
        this.toNextLevel = toNextLevel != null ? toNextLevel : 0;
        this.startLevelScore = startLevelScore != null ? startLevelScore : 0;
        this.endLevelScore = endLevelScore != null ? endLevelScore : 0;
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

    public double getStartLevelScore() {
        return startLevelScore;
    }

    public double getEndLevelScore() {
        return endLevelScore;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }
}
