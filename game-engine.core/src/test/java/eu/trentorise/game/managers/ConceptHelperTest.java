package eu.trentorise.game.managers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import eu.trentorise.game.core.ExecutionClock;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;

@RunWith(JUnit4.class)
public class ConceptHelperTest {

    @Test
    public void inject_execution_moment_in_point_concept() {
        ConceptHelper helper = new ConceptHelper();
        long moment = System.currentTimeMillis();
        Set<GameConcept> concepts = new HashSet<>();
        concepts.add(new PointConcept("green"));
        concepts = helper.injectExecutionMoment(concepts, moment);
        PointConcept injected = (PointConcept) concepts.stream().findFirst().get();
        assertThat(injected.getExecutionMoment(), equalTo(moment));
    }

    @Test
    public void inject_execution_moment_in_challenge() {
        ConceptHelper helper = new ConceptHelper();
        long twoHoursAgoAsMillis =
                LocalDateTime.now().minusHours(2).atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli();

        Set<GameConcept> concepts = new HashSet<>();
        concepts.add(new ChallengeConcept());
        
        concepts = helper.injectExecutionMoment(concepts, twoHoursAgoAsMillis);
        ChallengeConcept injected = (ChallengeConcept) concepts.stream().findFirst().get();
        assertThat(injected.getClock().nowAsMillis(), equalTo(twoHoursAgoAsMillis));
    }

    @Test
    public void concepts_size_preservation() {
        ConceptHelper helper = new ConceptHelper();

        Set<GameConcept> concepts = new HashSet<>();
        concepts.add(new ChallengeConcept());
        concepts.add(new ChallengeConcept());
        concepts.add(new BadgeCollectionConcept("my-collection"));

        long twoHoursAgoAsMillis = LocalDateTime.now().minusHours(2).atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        concepts = helper.injectExecutionMoment(concepts, twoHoursAgoAsMillis);
        assertThat(concepts, hasSize(3));
    }

    @Test
    public void activate_assigned_challenge() {
        ConceptHelper helper = new ConceptHelper();
        Set<GameConcept> concepts = new HashSet<>();
        ChallengeConcept challenge = new ChallengeConcept(ChallengeState.ASSIGNED);

        concepts.add(challenge);

        concepts = helper.activateConcepts(concepts);
        ChallengeConcept activated = (ChallengeConcept) concepts.stream().findFirst().get();
        assertThat(activated.isActive(), equalTo(true));
    }

    @Test
    public void should_not_activate_proposed_challenge() {
        ConceptHelper helper = new ConceptHelper();
        Set<GameConcept> concepts = new HashSet<>();
        ChallengeConcept challenge = new ChallengeConcept(ChallengeState.PROPOSED);

        concepts.add(challenge);

        concepts = helper.activateConcepts(concepts);
        ChallengeConcept activated = (ChallengeConcept) concepts.stream().findFirst().get();
        assertThat(activated.isActive(), equalTo(false));
    }

    @Test
    public void should_not_activate_past_failed_challenge() {
        ConceptHelper helper = new ConceptHelper();
        Set<GameConcept> concepts = new HashSet<>();
        long yesterdayAsMillis = LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        ChallengeConcept challenge =
                new ChallengeConcept(ChallengeState.ASSIGNED,
                        new ExecutionClock(yesterdayAsMillis));
        challenge.updateState(ChallengeState.FAILED);
        concepts.add(challenge);
        long todayAsMillis =
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        challenge.setClock(new ExecutionClock(todayAsMillis));
        concepts = helper.activateConcepts(concepts);
        ChallengeConcept activated = (ChallengeConcept) concepts.stream().findFirst().get();
        assertThat(activated.isActive(), equalTo(false));
    }

    @Test
    public void should_activate_challenge_failed_after_activation_clock() {
        ConceptHelper helper = new ConceptHelper();
        Set<GameConcept> concepts = new HashSet<>();
        long yesterdayAsMillis = LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        ChallengeConcept challenge = new ChallengeConcept(ChallengeState.ASSIGNED,
                new ExecutionClock(yesterdayAsMillis));
        challenge.updateState(ChallengeState.FAILED);
        concepts.add(challenge);
        long twoDaysAgoAsMills = LocalDateTime.now().minusDays(2).atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        challenge.setClock(new ExecutionClock(twoDaysAgoAsMills));
        concepts = helper.activateConcepts(concepts);
        ChallengeConcept activated = (ChallengeConcept) concepts.stream().findFirst().get();
        assertThat(activated.isActive(), equalTo(true));
    }

    @Test
    public void should_find_two_active_challenges() {
        ConceptHelper helper = new ConceptHelper();
        Set<GameConcept> concepts = new HashSet<>();
        ChallengeConcept challenge1 = new ChallengeConcept(ChallengeState.PROPOSED);
        ChallengeConcept challenge2 = new ChallengeConcept();
        ChallengeConcept challenge3 = new ChallengeConcept();

        challenge2.activate();
        challenge3.activate();

        concepts.add(challenge1);
        concepts.add(challenge2);
        concepts.add(challenge3);

        Set<GameConcept> activated = helper.findActiveConcepts(concepts);
        assertThat(activated, hasSize(2));


    }

}
