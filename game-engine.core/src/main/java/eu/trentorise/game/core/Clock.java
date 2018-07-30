package eu.trentorise.game.core;

import java.util.Date;

public interface Clock {
    Date now();

    long nowAsMillis();
}
