package eu.trentorise.game.model;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Predicate;

import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.core.GameConcept;

public class Reward {
    private double percentage;
    private double threshold;
    private Map<String, Double> bonusScore = new HashMap<>();
    private PointConceptRef calculationPointConcept;
    private PointConceptRef targetPointConcept;

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public PointConceptRef getCalculationPointConcept() {
        return calculationPointConcept;
    }

    public void setCalculationPointConcept(PointConceptRef calculationPointConcept) {
        this.calculationPointConcept = calculationPointConcept;
    }

    public PointConceptRef getTargetPointConcept() {
        return targetPointConcept;
    }

    public void setTargetPointConcept(PointConceptRef targetPointConcept) {
        this.targetPointConcept = targetPointConcept;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return String.format(
                "{bonusScore=%s, percentage=%s, threshold=%s, calculationPointConcept=%s, targetPointConcept=%s}",
                bonusScore,
                percentage, threshold, calculationPointConcept, targetPointConcept);
    }

    public Map<String, Double> getBonusScore() {
        return bonusScore;
    }

    public void validate(Game game) {
        if (calculationPointConcept == null) {
            throw new IllegalArgumentException("calculationPointConcept is required");
        }

        if (targetPointConcept == null) {
            throw new IllegalArgumentException("targetPointConcept is required");
        }
        if (game != null) {
            validatePointConcept(game, calculationPointConcept);
            validatePointConcept(game, targetPointConcept);
        }
    }

    private void validatePointConcept(Game game, PointConceptRef pointConceptRef) {
        PointConcept pointConcept = (PointConcept) game.getConcepts().stream()
                .filter(foundPointConcept(pointConceptRef.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("pointconcept %s not defined in game %s",
                                pointConceptRef.getName(), game.getId())));

        if(pointConceptRef.getPeriod() != null && pointConcept.getPeriod(pointConceptRef.getPeriod()) == null) {
            throw new IllegalArgumentException(
                    String.format("period %s not existent in pointConcept %s",
                            pointConceptRef.getPeriod(), pointConceptRef.getName()));
        }
    }

    private Predicate<GameConcept> foundPointConcept(String name) {
        return concept -> concept.getClass() == PointConcept.class
                && concept.getName().equals(name);
    }
}