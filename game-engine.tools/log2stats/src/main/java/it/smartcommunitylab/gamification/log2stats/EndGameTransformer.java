package it.smartcommunitylab.gamification.log2stats;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndGameTransformer extends AbstractTransformer {
    private long executionTime;
    private String gameId;
    private String playerId;

    public EndGameTransformer(String executionId, long executionTime, String gameId,
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
                        "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) INFO \\[.+\\] - .+ - Process terminated: true");
                Matcher matcher = searchFields.matcher(row);
                if (matcher.matches()) {
                    long timestamp = timestamp(matcher.group(1));
                    result = String.format("INFO - \"%s\" \"%s\" %s %s %s type=EndGameAction",
                            gameId, playerId, getExecutionId(), executionTime, timestamp);
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
