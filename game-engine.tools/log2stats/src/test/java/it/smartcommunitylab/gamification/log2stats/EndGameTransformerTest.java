package it.smartcommunitylab.gamification.log2stats;

import org.junit.Assert;
import org.junit.Test;

public class EndGameTransformerTest {
    @Test
    public void when_row_is_null_return_null() {
        String row = null;
        Transformer transformer = new EndGameTransformer("395b69a3-9e89-44fe-b12c-129eb12bfd80",
                1519156629000L, "59a91478e4b0c9db6800afaf", "25631");
        String expected = null;
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void when_row_is_empty_return_empty_string() {
        String row = "";
        Transformer transformer = new EndGameTransformer("395b69a3-9e89-44fe-b12c-129eb12bfd80",
                1519156629000L, "59a91478e4b0c9db6800afaf", "25631");
        String expected = "";
        Assert.assertEquals(expected, transformer.transform(row));
    }

    @Test
    public void transform() {
        String row =
                "2018-02-21 00:29:42,866 INFO [LogHub-pool-1-thread-1] - 59a91478e4b0c9db6800afaf - Process terminated: true";
        Transformer transformer = new EndGameTransformer("395b69a3-9e89-44fe-b12c-129eb12bfd80",
                1519156629000L, "59a91478e4b0c9db6800afaf", "25424");
        String expected =
                "INFO - \"59a91478e4b0c9db6800afaf\" \"25424\" 395b69a3-9e89-44fe-b12c-129eb12bfd80 1519156629000 1519169382866 type=EndGameAction";
        Assert.assertEquals(expected, transformer.transform(row));

    }
}
