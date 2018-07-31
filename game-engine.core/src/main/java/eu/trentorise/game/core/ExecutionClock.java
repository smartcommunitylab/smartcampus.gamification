package eu.trentorise.game.core;

import java.util.Date;

public class ExecutionClock implements Clock {

    private long moment;

    public ExecutionClock(long executionMoment) {
        moment = executionMoment;

    }
    @Override
    public Date now() {
        return new Date(moment);
    }

    @Override
    public long nowAsMillis() {
        return moment;
    }

}
