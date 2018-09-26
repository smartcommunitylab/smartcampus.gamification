package eu.trentorise.game.test_utils;

import java.util.Date;

import org.joda.time.LocalDateTime;

public final class Utils {

    // static class, no needs to instantiate
    private Utils() {}

    public static Date date(String isoDate) {
        return LocalDateTime.parse(isoDate).toDate();
    }
}
