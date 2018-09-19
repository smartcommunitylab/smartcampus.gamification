package eu.trentorise.game.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.services.PlayerService;

public class AutoChallengeChoiceTask extends GameTask {

    private Logger logger = LoggerFactory.getLogger(AutoChallengeChoiceTask.class);

    private PlayerService playerSrv;

    public AutoChallengeChoiceTask() {

    }

    public AutoChallengeChoiceTask(PlayerService playerSrv) {
        this.playerSrv = playerSrv;
    }

    @Override
    public void execute(GameContext ctx) {
        long start = System.currentTimeMillis();
        List<String> playerIds = ctx.readPlayers();
        LogHub.info(ctx.getGameRefId(), logger,
                "AutoChallengeChoiceTask started");
        if (playerSrv != null) {
        playerIds.forEach(id -> {
            playerSrv.forceChallengeChoice(ctx.getGameRefId(), id);
        });
            LogHub.info(ctx.getGameRefId(), logger, "Completed AutoChallengeChoiceTask, ms: "
                    + (System.currentTimeMillis() - start));
        } else {
            LogHub.warn(ctx.getGameRefId(), logger,
                    "PlayerSrv null running AutoChallengeChoiceTask ");
        }

    }

    @Override
    public List<String> getExecutionActions() {
        return null;
    }

    public PlayerService getPlayerSrv() {
        return playerSrv;
    }

    public void setPlayerSrv(PlayerService playerSrv) {
        this.playerSrv = playerSrv;
    }
}
