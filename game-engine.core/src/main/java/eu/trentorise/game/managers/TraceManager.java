package eu.trentorise.game.managers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.PlayerMoveLog;
import eu.trentorise.game.repo.PlayerMoveLogRepo;
import eu.trentorise.game.services.TraceService;

@Component
public class TraceManager implements TraceService {

	@Autowired
	private PlayerMoveLogRepo playerMoveRepo;

	@Override
	public void tracePlayerMove(PlayerState old, PlayerState newOne,
			Map<String, Object> inputData, long executionTime) {
		Map<String, PointConcept> points = new HashMap<>();
		Map<String, BadgeCollectionConcept> badgeCollections = new HashMap<>();

		for (GameConcept gc : newOne.getState()) {
			if (gc instanceof PointConcept) {
				points.put(gc.getName(), (PointConcept) gc);
			}

			if (gc instanceof BadgeCollectionConcept) {
				badgeCollections.put(gc.getName(), (BadgeCollectionConcept) gc);
			}
		}

		Map<String, Double> tracePoints = new HashMap<>();
		Map<String, String[]> traceBadgeCollections = new HashMap<>();

		for (GameConcept gc : old.getState()) {
			if (gc instanceof PointConcept) {
				PointConcept oldValue = (PointConcept) gc;
				PointConcept newValue = points.get(oldValue.getName());
				tracePoints.put(gc.getName(),
						newValue.getScore() - oldValue.getScore());
			}

			if (gc instanceof BadgeCollectionConcept) {
				BadgeCollectionConcept oldValue = (BadgeCollectionConcept) gc;
				BadgeCollectionConcept newValue = badgeCollections.get(oldValue
						.getName());
				traceBadgeCollections.put(
						gc.getName(),
						CollectionUtils.subtract(newValue.getBadgeEarned(),
								oldValue.getBadgeEarned()).toArray(
								new String[0]));
			}
		}

		PlayerMoveLog tracePlayerMove = new PlayerMoveLog(old.getGameId(),
				old.getPlayerId(), new Date(executionTime), null, tracePoints,
				traceBadgeCollections);
		playerMoveRepo.save(tracePlayerMove);

	}
}
