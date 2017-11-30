package eu.trentorise.game.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.managers.GameManager;
import eu.trentorise.game.model.core.GameConcept;

@Component
public class PointConceptStateHelperFactory {

    @Autowired
    private GameManager gameManager;

    /**
     * Obtain a instance of {@link PointConceptStateHelper} Throw {@link IllegalArgumentException}
     * if the name of PointConcept is not defined in the game
     * 
     * @param game the {@link Game}
     * @param pointConceptName
     * @return
     */
    public PointConceptStateHelper instanceOf(Game game, String pointConceptName) {
        PointConcept pointConceptDefinition = getPointConceptDefinition(game, pointConceptName);
        PointConceptStateHelper helperInstance =
                new PointConceptStateHelperImpl(pointConceptDefinition);
        return helperInstance;
    }

    /**
     * Obtain a instance of {@link PointConceptStateHelper} Throw {@link IllegalArgumentException}
     * if the name of PointConcept is not defined in the game
     * 
     * @param gameId game id
     * @param pointConceptName name of the point concept
     * @return
     */


    public PointConceptStateHelper instanceOf(String gameId, String pointConceptName) {
        Game game = gameManager.loadGameDefinitionById(gameId);
        return instanceOf(game, pointConceptName);
    }

    private PointConcept getPointConceptDefinition(Game game, String pointName) {
        if (game == null) {
            throw new IllegalArgumentException("game cannot be null");
        }

        if (pointName == null) {
            throw new IllegalArgumentException("pointName cannot be null");
        }

        PointConcept pointDefinition = null;
        if (game != null && game.getConcepts() != null) {
            for (GameConcept gc : game.getConcepts()) {
                if (gc.getClass() == PointConcept.class && gc.getName().equals(pointName)) {
                    pointDefinition = (PointConcept) gc;
                    break;
                }
            }
        }
        if (pointDefinition == null) {
            throw new IllegalArgumentException(
                    String.format("pointConcept %s doesn't exist in game definition %s", pointName,
                            game.getId()));
        }
        return pointDefinition;
    }

    /**
     * Helper class to instantiate {@link PointConcept} definitions with a specific state
     * 
     * @author mirko
     *
     */
    public interface PointConceptStateHelper {

        /**
         * Set the total score considering a moment in time
         * 
         * @param moment date in which consider the score setting as timestamp in milliseconds
         * @param score the total score
         * @return reference to {@link PointConceptStateHelper}
         */
        PointConceptStateHelper setScoreInTime(long moment, double score);

        /**
         * Set the total score considering a moment in time
         * 
         * @param moment date in which consider the score setting
         * @param score the total score
         * @return reference to {@link PointConceptStateHelper}
         */
        PointConceptStateHelper setScoreInTime(Date moment, double score);

        /**
         * Create the instance for the {@link PointConcept}
         * 
         * @return {@link PointConcept} instance
         */
        PointConcept build();
    }



    private class PointConceptStateHelperImpl implements PointConceptStateHelper {

        private PointConcept pointConcept = null;

        private PointConceptStateHelperImpl(PointConcept definition) {
            pointConcept = definition;
        }

        public PointConceptStateHelper setScoreInTime(long moment, double score) {
            if (pointConcept != null) {
                pointConcept.executionMoment = moment;
                pointConcept.setScore(score);
            }
            return this;
        }

        public PointConceptStateHelper setScoreInTime(Date moment, double score) {
            if (moment == null) {
                throw new IllegalArgumentException("moment date is null");
            }
            setScoreInTime(moment.getTime(), score);
            return this;
        }

        public PointConcept build() {
            return pointConcept;
        }
    }

}
