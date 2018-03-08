package it.smartcommunitylab.gamification.log2stats;

import org.junit.Assert;
import org.junit.Test;

public class ClassificationTransformerTest {
    @Test
    public void when_row_is_null_return_null() {
        String row = null;
        Transformer transformer =
                new ClassificationTransformer("395b69a3-9e89-44fe-b12c-129eb12bfd80");
        String expected = null;
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void when_row_is_empty_return_empty_string() {
        String row = "";
        Transformer transformer =
                new ClassificationTransformer("395b69a3-9e89-44fe-b12c-129eb12bfd80");
        String expected = "";
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void transform() {
        String row =
                "2018-02-20 00:00:18,139 INFO [LogHub-pool-1-thread-1] - 59a91478e4b0c9db6800afaf - gameId:59a91478e4b0c9db6800afaf, actionId: scogei_classification, playerId: 25706, executionMoment: 20-02-2018 00:00:18, data: null, factObjs: [{name: week classification green, classificationType: INCREMENTAL, scoreType: green leaves, score: 2549.0, position: 1, executionTime: 22, periodReference: [instanceIndex: 21, start: Sat Feb 03 00:00:00 CET 2018, end: Sat Feb 10 00:00:00 CET 2018, periodRepresentation: 03/02/2018T00:00:00-10/02/2018T00:00:00]}]";
        Transformer transformer =
                new ClassificationTransformer("870a0514-b66b-4375-b028-789c5ac2a838");
        String expected =
                "INFO - \"59a91478e4b0c9db6800afaf\" \"25706\" 870a0514-b66b-4375-b028-789c5ac2a838 1519081218000 1519081218139 type=Classification classificationName=\"week classification green\" classificationPosition=1";

        Assert.assertEquals(expected, transformer.transform(row));

    }
}
