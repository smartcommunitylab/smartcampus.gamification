package it.smartcommunitylab.model.ext;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GroupChallenge {

    @JsonIgnore
    public static final String MODEL_NAME_COMPETITIVE_PERFORMANCE = "groupCompetitivePerformance";

    @JsonIgnore
    public static final String MODEL_NAME_COMPETITIVE_TIME = "groupCompetitiveTime";

    @JsonIgnore
    public static final String MODEL_NAME_COOPERATIVE = "groupCooperative";

    @JsonIgnore
    public static final List<String> MODELS =
            Arrays.asList(MODEL_NAME_COMPETITIVE_PERFORMANCE, MODEL_NAME_COMPETITIVE_TIME,
                    MODEL_NAME_COOPERATIVE);


}
