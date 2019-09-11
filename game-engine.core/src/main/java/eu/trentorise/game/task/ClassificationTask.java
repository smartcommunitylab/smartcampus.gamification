package eu.trentorise.game.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.managers.ClassificationFactory.ClassificationBuilder;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.ClassificationPosition;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.notification.ClassificationNotification;

public abstract class ClassificationTask extends GameTask {

	private final Logger logger = LoggerFactory.getLogger(ClassificationTask.class);

	private static final int DEFAULT_VALUE = 3;

	private static final String ACTION_CLASSIFICATION = "classification";

	private Integer itemsToNotificate = DEFAULT_VALUE;
	private String classificationName;

	public ClassificationTask(String classificationName, TaskSchedule schedule) {
		super(classificationName, schedule);
		this.classificationName = classificationName;

	}

	public ClassificationTask(Integer itemsToNotificate, String classificationName, TaskSchedule schedule) {
		super(classificationName, schedule);
		this.classificationName = classificationName;
		this.itemsToNotificate = itemsToNotificate;
	}

	@JsonCreator
	public ClassificationTask() {
		super();
	}

	@Override
	public void execute(GameContext ctx) {
		if (ctx == null) {
			logger.warn("gameContext null");
			return;
		}

		LogHub.info(ctx.getGameRefId(), logger, "run task {} of group {}", getName(), ctx.getGameRefId());

		List<String> players = ctx.readPlayers();

		List<PlayerState> states = new ArrayList<PlayerState>();
		for (String p : players) {
			states.add(ctx.readStatus(p));
		}

		ClassificationBuilder builder = createBuilder(states);

		List<ClassificationPosition> classification = builder.getClassificationBoard().getBoard();

		if (logger.isDebugEnabled()) {
			for (ClassificationPosition position : classification) {
                LogHub.debug(ctx.getGameRefId(), logger, "{}: player {} score {}",
                        classificationName, position.getPlayerId(), position.getScore());
			}
		}

        // nobody gained score in this classification, avoid to run reward actions in engine
        if (classification.size() == 0 || classification.get(0).getScore() == 0) {
            LogHub.info(ctx.getGameRefId(), logger,
                    "No scores for the classification {}, avoid to send reward actions to the engine",
                    classificationName);
        } else {
            int position = 1, nextPosition = 1, index;
            Double lastScore = null;
            boolean sameScore = false;
            for (ClassificationPosition item : classification) {

                sameScore = lastScore != null && lastScore == item.getScore();
                index = nextPosition - 1;

                if (index >= itemsToNotificate && !sameScore) {
                    break;
                }

                if (!sameScore) {
                    position = nextPosition;
                }
                lastScore = item.getScore();
                nextPosition++;

                Classification c =
                        createClassificationObject(ctx, item.getScore(), getScoreType(), position);

                List<Object> factObjs = new ArrayList<Object>();
                factObjs.add(c);
                ctx.sendAction(ACTION_CLASSIFICATION, item.getPlayerId(), null, factObjs);

                ClassificationNotification classificationNotification =
                        new ClassificationNotification();
                classificationNotification.setGameId(ctx.getGameRefId());
                classificationNotification.setPlayerId(item.getPlayerId());
                classificationNotification.setClassificationName(classificationName);
                classificationNotification.setClassificationPosition(position);

                ctx.sendNotification(classificationNotification);
                LogHub.info(ctx.getGameRefId(), logger, "send notification: {}",
                        classificationNotification.toString());
            }
        }
	}

	@Override
	@JsonIgnore
	public List<String> getExecutionActions() {
		return Arrays.asList(ACTION_CLASSIFICATION);
	}

	protected abstract ClassificationBuilder createBuilder(List<PlayerState> states);

	protected abstract String getScoreType();

	protected abstract Classification createClassificationObject(GameContext ctx, double score, String scoreType,
			int position);


	public Integer getItemsToNotificate() {
		return itemsToNotificate;
	}

	public void setItemsToNotificate(Integer itemsToNotificate) {
		this.itemsToNotificate = itemsToNotificate;
	}

	public String getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}

}
