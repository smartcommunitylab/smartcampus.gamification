package it.smartcommunitylab.gamification.log2stats;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassificationTransformer extends ActionTransformer {

    public ClassificationTransformer(String executionId) {
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
                        "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) INFO \\[.+\\] - .+ - gameId:(.+), actionId: scogei_classification, playerId: (.+), executionMoment: (\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}), data: null, factObjs: \\[\\{name: (.+), classificationType: INCREMENTAL, scoreType: .+, position: (.+), executionTime: .*");
                Matcher matcher = searchFields.matcher(row);
                if (matcher.matches()) {
                    long timestamp = timestamp(matcher.group(1));
                    String classificationName = matcher.group(5);
                    String position = matcher.group(6);
                    gameId = matcher.group(2);
                    playerId = matcher.group(3);
                    executionTime = executionTime(matcher.group(4));
                    result = String.format(
                            "INFO - \"%s\" \"%s\" %s %s %s type=Classification classificationName=\"%s\" classificationPosition=%s",
                            gameId, playerId, getExecutionId(), executionTime, timestamp,
                            classificationName, position);
                }
            }
        }
        return result;
    }

}
