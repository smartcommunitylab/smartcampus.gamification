package it.smartcommunitylab.gamification.log2stats;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionTransformer extends AbstractTransformer {

    protected String gameId;
    protected String playerId;
    protected long executionTime;

    public ActionTransformer(String executionId) {
        super(executionId);
    }

    @Override
    public String transform(String row) {
        String result = null;
        if (row != null) {
            if (row.isEmpty()) {
                result = row;
            } else {
                Pattern searchFields = Pattern.compile(
                        "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) INFO \\[.+\\] - .+ - gameId:(\\w+), actionId: (.+), playerId: (.+), executionMoment: (\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}), data: .*, factObjs: .*");
                Matcher matcher = searchFields.matcher(row);
                if (matcher.matches()) {
                    gameId = matcher.group(2);
                    playerId = matcher.group(4);
                    executionTime = executionTime(matcher.group(5));
                    long timestamp = timestamp(matcher.group(1));
                    String actionName = matcher.group(3);
                    result = String.format(
                            "INFO - \"%s\" \"%s\" %s %s %s type=Action actionName=\"%s\"", gameId,
                            playerId, getExecutionId(), executionTime, timestamp, actionName);

                }
            }
        }
        return result;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public long getExecutionTime() {
        return executionTime;
    }



}
