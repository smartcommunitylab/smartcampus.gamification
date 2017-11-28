package eu.trentorise.game.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.trentorise.game.managers.GameManager;
import eu.trentorise.game.model.core.GameConcept;

@Component
@Scope("prototype")
public class PointConceptStateHelper {

    @Autowired
    private GameManager gameManager;

    private PointConcept pointConcept = null;

    public PointConceptStateHelper instanceOf(Game game, String pointConceptName) {
        return this;
    }


    private PointConcept getPointConceptDefinition(Game game, String pointName) {
        if (game == null) {
            throw new IllegalArgumentException("game cannot be null");
        }

        if (pointName == null) {
            throw new IllegalArgumentException("pointName cannot be null");
        }

        PointConcept pointDefinition = null;
        for (GameConcept gc : game.getConcepts()) {
            if (gc.getClass() == PointConcept.class && gc.getName().equals(pointName)) {
                pointDefinition = (PointConcept) gc;
            }
        }

        return pointDefinition;
    }

    public PointConceptStateHelper instanceOf(String gameId, String pointConceptName) {
        Game game = gameManager.loadGameDefinitionById(gameId);
        pointConcept = getPointConceptDefinition(game, pointConceptName);
        return this;
    }

    public void setScoreInTime(long moment, double score) {
        if (pointConcept != null) {
            pointConcept.executionMoment = moment;
            pointConcept.setScore(score);
        }
    }

    public void setScoreInTime(Date moment, double score) {
        if (moment == null) {
            throw new IllegalArgumentException("moment date is null");
        }
        setScoreInTime(moment.getTime(), score);
    }



}
