package eu.trentorise.game.model;

import eu.trentorise.game.model.GroupChallenge.PointConceptRef;

public class Reward {
    private double percentage;
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

}