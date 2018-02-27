package it.smartcommunitylab.gamification.log2stats;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BadgeCollectionConceptTransformer extends AbstractTransformer {
    private long executionTime;
    private String gameId;
    private String playerId;

    public BadgeCollectionConceptTransformer(String executionId, long executionTime, String gameId,
            String playerId) {
        super(executionId);
        this.executionTime = executionTime;
        this.gameId = gameId;
        this.playerId = playerId;
    }


    @Override
    public String transform(String row) {
        String result = null;
        if (row != null) {
            if (row.isEmpty()) {
                result = row;
            } else {
                Pattern searchFields = Pattern.compile(
                        "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) INFO \\[.+\\] - .+ - rule \\'(.+)\\' updated BadgeCollectionConcept \\'(.+)\\' with \\'(.+)\\' of player (.+)");
                Matcher matcher = searchFields.matcher(row);
                if (matcher.matches()) {
                    long timestamp = timestamp(matcher.group(1));
                    String ruleName = matcher.group(2);
                    String badgeName = matcher.group(4);
                    String conceptName = matcher.group(3);
                    result = String.format(
                            "INFO - \"%s\" \"%s\" %s %s %s type=BadgeCollectionConcept ruleName=\"%s\" name=\"%s\" new_badge=\"%s\"",
                            gameId, playerId, getExecutionId(), executionTime, timestamp, ruleName,
                            conceptName, badgeName);
                }
            }
        }
        return result;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public String getGameId() {
        return gameId;
    }

}
