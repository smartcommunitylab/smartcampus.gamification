package it.smartcommunitylab.gamification.log2stats;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public abstract class AbstractTransformer implements Transformer {
    private String executionId;

    public AbstractTransformer(String executionId) {
        this.executionId = executionId != null ? executionId : UUID.randomUUID().toString();
    }

    public String getExecutionId() {
        return executionId;
    }


    final static long timestamp(String representation) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        try {
            return dateFormat.parse(representation).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    final static long executionTime(String representation) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            return dateFormat.parse(representation).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
