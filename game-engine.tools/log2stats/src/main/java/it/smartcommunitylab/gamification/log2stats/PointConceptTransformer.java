package it.smartcommunitylab.gamification.log2stats;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointConceptTransformer extends AbstractTransformer {
    private long executionTime;
    private String gameId;

    public PointConceptTransformer(String executionId, long executionTime, String gameId) {
        super(executionId);
        this.executionTime = executionTime;
        this.gameId = gameId;
    }

    @Override
    public String transform(String row) {
        String result = null;
        if (row != null) {
            if (row.isEmpty()) {
                result = row;
            } else {
                Pattern searchFields = Pattern.compile(
                        "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) INFO \\[.+\\] - .+ - rule \'(.+)\' updated PointConcept \'(.+)\' of (.+) \\(total: (.+)\\) of player (.+)");
                Matcher matcher = searchFields.matcher(row);
                if (matcher.matches()) {
                    String deltaScore = matcher.group(4);
                    String score = matcher.group(5);
                    String playerId = matcher.group(6);
                    String ruleName = matcher.group(2);
                    String conceptName = matcher.group(3);
                    long timestamp = timestamp(matcher.group(1));
                    result = String.format(
                            "INFO - \"%s\" \"%s\" %s %s %s type=PointConcept ruleName=\"%s\" name=\"%s\" deltaScore=%s score=%s",
                            gameId, playerId, getExecutionId(), executionTime, timestamp, ruleName,
                            conceptName, deltaScore, score);
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
