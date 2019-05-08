package eu.trentorise.game.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.EngineTask;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.TaskService;

public class JobDestroyerTask extends EngineTask {

    private static final Logger logger = LoggerFactory.getLogger(JobDestroyerTask.class);

    private GameService gameSrv;
    private TaskService taskSrv;

    public JobDestroyerTask(TaskService taskSrv, GameService gameSrv) {
        this.gameSrv = gameSrv;
        this.taskSrv = taskSrv;
    }

    public JobDestroyerTask(TaskService taskSrv, GameService gameSrv, String name,
            TaskSchedule schedule) {
        super(name, schedule);
        this.gameSrv = gameSrv;
        this.taskSrv = taskSrv;
    }

    @Override
    public void execute() {
        LogHub.info(null, logger, "task destroyer invocation");
        long deadline = System.currentTimeMillis();

        List<Game> games = gameSrv.loadGames(true);
        for (Game game : games) {
            if (game.getExpiration() > 0 && game.getExpiration() < deadline) {
                for (GameTask task : game.getTasks()) {
                    if (taskSrv.destroyTask(task, game.getId())) {
                        LogHub.info(null, logger, "Destroy task - {} - of game {}",
                                task.getName(), game.getId());
                    }
                }
                game.setTerminated(true);
                gameSrv.saveGameDefinition(game);
            }
        }
    }

}
