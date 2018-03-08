package it.smartcommunitylab.gamification.log2stats;

import org.junit.Assert;
import org.junit.Test;

public class BadgeCollectionConceptTransformerTest {
    @Test
    public void when_row_is_null_return_null() {
        String row = null;
        Transformer transformer =
                new BadgeCollectionConceptTransformer("395b69a3-9e89-44fe-b12c-129eb12bfd80",
                        1519156629000L, "59a91478e4b0c9db6800afaf", "25631");
        String expected = null;
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void when_row_is_empty_return_empty_string() {
        String row = "";
        Transformer transformer =
                new BadgeCollectionConceptTransformer("395b69a3-9e89-44fe-b12c-129eb12bfd80",
                        1519156629000L, "59a91478e4b0c9db6800afaf", "25631");
        String expected = "";
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void transform() {
        String row =
                "2018-02-21 07:56:34,902 INFO [LogHub-pool-1-thread-1] - 59a91478e4b0c9db6800afaf - rule '100 trip impact0' updated BadgeCollectionConcept 'sustainable life' with '100_zero_impact_trip' of player 26751";
        Transformer transformer =
                new BadgeCollectionConceptTransformer("6ed4a2af-1387-45ce-bb7a-7fabf6d771cb",
                        1519156629000L, "59a91478e4b0c9db6800afaf", "26751");
        String expected =
                "INFO - \"59a91478e4b0c9db6800afaf\" \"26751\" 6ed4a2af-1387-45ce-bb7a-7fabf6d771cb 1519156629000 1519196194902 type=BadgeCollectionConcept ruleName=\"100 trip impact0\" name=\"sustainable life\" new_badge=\"100_zero_impact_trip\"";

        Assert.assertEquals(expected, transformer.transform(row));

    }
}
