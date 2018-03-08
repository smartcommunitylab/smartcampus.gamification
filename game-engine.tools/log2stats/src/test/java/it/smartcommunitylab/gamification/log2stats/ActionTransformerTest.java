package it.smartcommunitylab.gamification.log2stats;

import org.junit.Assert;
import org.junit.Test;

public class ActionTransformerTest {


    @Test
    public void when_row_is_null_return_null() {
        String row = null;
        Transformer transformer = new ActionTransformer("6ed4a2af-1387-45ce-bb7a-7fabf6d771cb");
        String expected = null;
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void when_row_is_empty_return_empty_string() {
        String row = "";
        Transformer transformer = new ActionTransformer("6ed4a2af-1387-45ce-bb7a-7fabf6d771cb");
        String expected = "";
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void transform() {
        String row =
                "2018-02-21 00:29:42,807 INFO [LogHub-pool-1-thread-1] - 59a91478e4b0c9db6800afaf - gameId:59a91478e4b0c9db6800afaf, actionId: save_itinerary, playerId: 25631, executionMoment: 20-02-2018 20:57:09, data: {travelId=5a8c7d9e9045ea330453b661, walkDistance=0.2728388418605312}, factObjs: null";
        Transformer transformer = new ActionTransformer("6ed4a2af-1387-45ce-bb7a-7fabf6d771cb");
        String expected =
                "INFO - \"59a91478e4b0c9db6800afaf\" \"25631\" 6ed4a2af-1387-45ce-bb7a-7fabf6d771cb 1519156629000 1519169382807 type=Action actionName=\"save_itinerary\"";

        Assert.assertEquals(expected, transformer.transform(row));

    }
}
