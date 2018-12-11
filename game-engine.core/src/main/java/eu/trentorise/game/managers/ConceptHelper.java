package eu.trentorise.game.managers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import eu.trentorise.game.core.ExecutionClock;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;

class ConceptHelper {

    public Set<GameConcept> injectExecutionMoment(Set<GameConcept> concepts,
            long executionMoment) {
        for (Iterator<GameConcept> iter = concepts.iterator(); iter.hasNext();) {
            GameConcept gc = iter.next();
            if (gc instanceof ChallengeConcept) {
                ChallengeConcept challenge = (ChallengeConcept) gc;
                challenge.setClock(new ExecutionClock(executionMoment));
            }
            if (gc instanceof PointConcept) {
                PointConcept pointConcept = (PointConcept) gc;
                pointConcept.setExecutionMoment(executionMoment);
            }
        }

        return concepts;
    }

    public Set<GameConcept> activateConcepts(Set<GameConcept> concepts) {
        for (Iterator<GameConcept> iter = concepts.iterator(); iter.hasNext();) {
            GameConcept gc = iter.next();
            if (gc instanceof ChallengeConcept) {
                ChallengeConcept challenge = (ChallengeConcept) gc;
                challenge.activate();
            }
        }

        return concepts;
    }

    public Set<GameConcept> findActiveConcepts(Set<GameConcept> concepts) {
        Set<GameConcept> activeConcepts = new HashSet<>();
        for (Iterator<GameConcept> iter = concepts.iterator(); iter.hasNext();) {
            GameConcept gc = iter.next();
            if (gc instanceof ChallengeConcept) {
                ChallengeConcept challenge = (ChallengeConcept) gc;
                if (!challenge.isActive() || !challenge.isActivable()) {
                    continue;
                }
            }
            activeConcepts.add(gc);
        }

        return activeConcepts;
    }
}
