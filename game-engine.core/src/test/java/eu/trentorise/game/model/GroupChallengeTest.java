package eu.trentorise.game.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.test_utils.Utils;

public class GroupChallengeTest {

    @Test
    public void assigned() {
        GroupChallenge challenge = new GroupChallenge();
        Map<ChallengeState, Date> testStateDate = new HashMap<>();
        testStateDate.put(ChallengeState.ASSIGNED, Utils.date("2018-10-01T10:50:00"));
        challenge.setStateDate(testStateDate);
        
        Attendee guest1 = new Attendee();
        guest1.setPlayerId("p1");
        challenge.getAttendees().add(guest1);
        
        ChallengeConcept result = challenge.toChallengeConcept("p1");
        assertThat(result.getState(), is(ChallengeState.ASSIGNED));
        assertThat(result.getDate(ChallengeState.ASSIGNED), is(Utils.date("2018-10-01T10:50:00")));
    }

    @Test
    public void proposed() {
        GroupChallenge challenge = new GroupChallenge(ChallengeState.PROPOSED);
        Map<ChallengeState, Date> testStateDate = new HashMap<>();
        testStateDate.put(ChallengeState.PROPOSED, Utils.date("2018-10-01T10:50:00"));
        challenge.setStateDate(testStateDate);

        Attendee guest1 = new Attendee();
        guest1.setPlayerId("p1");
        challenge.getAttendees().add(guest1);

        ChallengeConcept result = challenge.toChallengeConcept("p1");
        assertThat(result.getState(), is(ChallengeState.PROPOSED));
        assertThat(result.getDate(ChallengeState.PROPOSED), is(Utils.date("2018-10-01T10:50:00")));
    }

    @Test
    public void proposedAndAssigned() {
        GroupChallenge challenge = new GroupChallenge(ChallengeState.ASSIGNED);
        Map<ChallengeState, Date> testStateDate = new HashMap<>();
        testStateDate.put(ChallengeState.PROPOSED, Utils.date("2018-10-01T10:50:00"));
        testStateDate.put(ChallengeState.ASSIGNED, Utils.date("2018-10-01T11:00:00"));
        challenge.setStateDate(testStateDate);

        Attendee guest1 = new Attendee();
        guest1.setPlayerId("p1");
        challenge.getAttendees().add(guest1);

        ChallengeConcept result = challenge.toChallengeConcept("p1");
        assertThat(result.getState(), is(ChallengeState.ASSIGNED));
        assertThat(result.getDate(ChallengeState.PROPOSED), is(Utils.date("2018-10-01T10:50:00")));
        assertThat(result.getDate(ChallengeState.ASSIGNED), is(Utils.date("2018-10-01T11:00:00")));
    }

    @Test
    public void finishedChallengeAndPlayerIsWinner() {
        GroupChallenge challenge = new GroupChallenge(ChallengeState.COMPLETED);
        Map<ChallengeState, Date> testStateDate = new HashMap<>();
        testStateDate.put(ChallengeState.PROPOSED, Utils.date("2018-10-01T10:50:00"));
        testStateDate.put(ChallengeState.ASSIGNED, Utils.date("2018-10-01T11:00:00"));
        testStateDate.put(ChallengeState.COMPLETED, Utils.date("2018-10-02T15:00:00"));
        challenge.setStateDate(testStateDate);

        Attendee guest1 = new Attendee();
        guest1.setPlayerId("p1");
        guest1.setWinner(true);
        challenge.getAttendees().add(guest1);

        ChallengeConcept result = challenge.toChallengeConcept("p1");
        assertThat(result.getState(), is(ChallengeState.COMPLETED));
        assertThat(result.getDate(ChallengeState.PROPOSED), is(Utils.date("2018-10-01T10:50:00")));
        assertThat(result.getDate(ChallengeState.ASSIGNED), is(Utils.date("2018-10-01T11:00:00")));
        assertThat(result.getDate(ChallengeState.COMPLETED), is(Utils.date("2018-10-02T15:00:00")));
    }

    @Test
    public void finishedChallengeAndPlayerIsLooser() {
        GroupChallenge challenge = new GroupChallenge(ChallengeState.COMPLETED);
        Map<ChallengeState, Date> testStateDate = new HashMap<>();
        testStateDate.put(ChallengeState.PROPOSED, Utils.date("2018-10-01T10:50:00"));
        testStateDate.put(ChallengeState.ASSIGNED, Utils.date("2018-10-01T11:00:00"));
        testStateDate.put(ChallengeState.COMPLETED, Utils.date("2018-10-02T15:00:00"));
        challenge.setStateDate(testStateDate);

        Attendee guest1 = new Attendee();
        guest1.setPlayerId("p1");
        guest1.setWinner(false);
        challenge.getAttendees().add(guest1);

        ChallengeConcept result = challenge.toChallengeConcept("p1");
        assertThat(result.getState(), is(ChallengeState.FAILED));
        assertThat(result.getDate(ChallengeState.PROPOSED), is(Utils.date("2018-10-01T10:50:00")));
        assertThat(result.getDate(ChallengeState.ASSIGNED), is(Utils.date("2018-10-01T11:00:00")));
        assertThat(result.getDate(ChallengeState.FAILED), is(Utils.date("2018-10-02T15:00:00")));
        assertThat(result.getDate(ChallengeState.COMPLETED), nullValue());
    }


}
