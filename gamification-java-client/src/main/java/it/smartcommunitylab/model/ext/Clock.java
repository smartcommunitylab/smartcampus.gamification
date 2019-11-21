package it.smartcommunitylab.model.ext;

import java.util.Date;

public interface Clock {
    Date now();

    long nowAsMillis();
}
