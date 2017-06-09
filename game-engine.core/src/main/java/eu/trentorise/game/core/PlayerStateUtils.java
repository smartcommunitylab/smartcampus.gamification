package eu.trentorise.game.core;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;

public class PlayerStateUtils {

	public static PlayerState incrementPointConcept(PlayerState state, String name, double score) {
		Set<GameConcept> concepts = state.getState();
		concepts.stream().filter(gc -> gc instanceof PointConcept && gc.getName().equals(name)).findFirst()
				.ifPresent(concept -> {
					((PointConcept) concept).setScore(((PointConcept) concept).getScore() + score);
				});
		state.setState(concepts);
		return state;
	}

	public static PlayerState incrementBadgeCollectionConcept(PlayerState state, String name, String badge) {
		Set<GameConcept> concepts = state.getState();
		concepts.stream().filter(gc -> gc instanceof BadgeCollectionConcept && gc.getName().equals(name)).findFirst()
				.ifPresent(concept -> {
					((BadgeCollectionConcept) concept).getBadgeEarned().add(badge);
				});
		state.setState(concepts);
		return state;
	}

	public static double getDeltaScore(PlayerState state, String name, double actualScore) {
		Set<GameConcept> concepts = state.getState();
		Optional<GameConcept> scoreConcept = concepts.stream()
				.filter(gc -> gc instanceof PointConcept && gc.getName().equals(name)).findFirst();
		double delta = 0d;
		try {
			GameConcept concept = scoreConcept.get();
			delta = actualScore - ((PointConcept) concept).getScore();
		} catch (NoSuchElementException e) {
			delta = actualScore;
		}
		return delta;
	}

	public static List<String> getDeltaBadges(PlayerState state, String name, List<String> actualBadges) {
		Set<GameConcept> concepts = state.getState();
		Optional<GameConcept> scoreConcept = concepts.stream()
				.filter(gc -> gc instanceof BadgeCollectionConcept && gc.getName().equals(name)).findFirst();
		List<String> delta = new ArrayList<>();
		try {
			GameConcept concept = scoreConcept.get();
			List<String> previousCollection = ((BadgeCollectionConcept) concept).getBadgeEarned();
			delta.addAll(CollectionUtils.subtract(actualBadges, previousCollection));
		} catch (NoSuchElementException e) {
			delta.addAll(actualBadges);
		}
		return delta;
	}

}
