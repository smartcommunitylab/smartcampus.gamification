package it.smartcommunitylab.gamification.log2stats;

import org.junit.Assert;
import org.junit.Test;


public class PointConceptTransformerTest {

    @Test
    public void when_row_is_null_return_null() {
        String row = null;
        Transformer transformer = new PointConceptTransformer(
                "6ed4a2af-1387-45ce-bb7a-7fabf6d771cb", 1519156629000L, "59a91478e4b0c9db6800afaf");
        String expected = null;
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void when_row_is_empty_return_empty_string() {
        String row = "";
        Transformer transformer = new PointConceptTransformer(
                "6ed4a2af-1387-45ce-bb7a-7fabf6d771cb", 1519156629000L, "59a91478e4b0c9db6800afaf");
        String expected = "";
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void transform() {
        String row =
                "2018-02-21 00:29:42,829 INFO [LogHub-pool-1-thread-1] - 59a91478e4b0c9db6800afaf - rule \'all modes - update green points\' updated PointConcept \'green leaves\' of 4.0 (total: 13440.0) of player 25631";
        Transformer transformer = new PointConceptTransformer(
                "6ed4a2af-1387-45ce-bb7a-7fabf6d771cb", 1519156629000L, "59a91478e4b0c9db6800afaf");
        String expected =
                "INFO - \"59a91478e4b0c9db6800afaf\" \"25631\" 6ed4a2af-1387-45ce-bb7a-7fabf6d771cb 1519156629000 1519169382829 type=PointConcept ruleName=\"all modes - update green points\" name=\"green leaves\" deltaScore=4.0 score=13440.0";
        Assert.assertEquals(expected, transformer.transform(row));

    }
}
