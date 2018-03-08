package it.smartcommunitylab.gamification.log2stats;

import java.util.UUID;

import org.apache.log4j.Logger;

public class Analyzer {

    private static final Logger logger = Logger.getLogger(Analyzer.class);

    private ActionTransformer lastAction = null;

    public String analyze(String row) {
        Transformer transformer = getTransformer(row);
        if (transformer != null) {
            return transformer.transform(row);
        } else {
            return "";
        }
    }


    private Transformer getTransformer(String row) {
        Transformer transformer = null;
        if (isAction(row)) {
            lastAction = new ActionTransformer(UUID.randomUUID().toString());
            return lastAction;
        } else if (isEndGameAction(row)) {
            if (lastAction == null) {
                logger.warn(String.format("not found relative Action row: %s", row));
                return null;
            }
            return new EndGameTransformer(lastAction.getExecutionId(),
                    lastAction.getExecutionTime(), lastAction.getGameId(),
                    lastAction.getPlayerId());
        } else if (isBadgeCollectionConcept(row)) {
            if (lastAction == null) {
                logger.warn(String.format("not found relative Action row: %s", row));
                return null;
            }
            return new BadgeCollectionConceptTransformer(lastAction.getExecutionId(),
                    lastAction.getExecutionTime(), lastAction.getGameId(),
                    lastAction.getPlayerId());
        } else if (isPointConcept(row)) {
            if (lastAction == null) {
                logger.warn(String.format("not found relative Action row: %s", row));
                return null;
            }
            return new PointConceptTransformer(lastAction.getExecutionId(),
                    lastAction.getExecutionTime(), lastAction.getGameId());
        } else if (isClassification(row)) {
            ClassificationTransformer classificationTransformer =
                    new ClassificationTransformer(UUID.randomUUID().toString());
            lastAction = classificationTransformer;
            return classificationTransformer;
        }
        return transformer;

    }


    private boolean isEndGameAction(String row) {
        return row != null && row.contains("Process terminated: true");
    }

    private boolean isBadgeCollectionConcept(String row) {
        return row != null && row.contains("updated BadgeCollectionConcept");
    }

    private boolean isPointConcept(String row) {
        return row != null && row.contains("updated PointConcept");
    }

    private boolean isClassification(String row) {
        return row != null && row.contains("scogei_classification");
    }

    private boolean isAction(String row) {
        return row != null && row.contains("actionId:") && !row.contains("scogei_classification");
    }
}
