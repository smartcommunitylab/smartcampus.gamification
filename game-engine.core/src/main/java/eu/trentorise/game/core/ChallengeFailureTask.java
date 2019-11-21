package eu.trentorise.game.core;

import eu.trentorise.game.model.core.EngineTask;
import eu.trentorise.game.services.GameService;

public class ChallengeFailureTask extends EngineTask {

    private GameService gameSrv;

    public ChallengeFailureTask(GameService gameSrv, String name,
            TaskSchedule schedule) {
        super(name, schedule);
        this.gameSrv = gameSrv;
    }

    @Override
    public void execute() {
        gameSrv.taskChallengeFailure();
    }

}
