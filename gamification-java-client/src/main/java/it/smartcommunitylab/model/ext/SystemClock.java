package it.smartcommunitylab.model.ext;

import java.util.Date;

public class SystemClock implements Clock {

    @Override
    public Date now() {
        return new Date();
    }

    @Override
    public long nowAsMillis() {
        return System.currentTimeMillis();
    }

}
