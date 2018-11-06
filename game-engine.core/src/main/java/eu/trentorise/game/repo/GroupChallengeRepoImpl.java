package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.GroupChallenge.Attendee;
import eu.trentorise.game.model.GroupChallenge.Attendee.Role;

public class GroupChallengeRepoImpl implements ExtendedGroupChallengeRepo {

    @Autowired
    private MongoTemplate mongo;

    @Override
    public List<GroupChallenge> playerGroupChallenges(String gameId, String playerId) {
        Criteria crit = new Criteria("gameId").is(gameId).and("attendees.playerId").is(playerId);
        return mongo.find(new Query(crit), GroupChallenge.class);

    }

    @Override
    public List<GroupChallenge> playerGroupChallenges(String gameId, String playerId,
            ChallengeState challengeState) {
        Criteria crit = new Criteria("gameId").is(gameId).and("attendees.playerId").is(playerId)
                .and("state").is(challengeState);
        return mongo.find(new Query(crit), GroupChallenge.class);
    }

    @Override
    public void deletePlayerGroupChallenges(String gameId, String playerId,
            ChallengeState challengeState) {
        Criteria crit = new Criteria("gameId").is(gameId).and("attendees.playerId").is(playerId)
                .and("state").is(challengeState);
        mongo.remove(new Query(crit), GroupChallenge.class);
    }

    @Override
    public List<GroupChallenge> proposerInvitations(String gameId, String playerId) {
        Attendee proposer = new Attendee();
        proposer.setPlayerId(playerId);
        proposer.setRole(Role.PROPOSER);
        Criteria crit = new Criteria("gameId").is(gameId).and("attendees").is(proposer)
                .and("state").is(ChallengeState.PROPOSED);
        return mongo.find(new Query(crit), GroupChallenge.class);
    }

    @Override
    public List<GroupChallenge> guestInvitations(String gameId, String playerId) {
        Attendee guest = new Attendee();
        guest.setPlayerId(playerId);
        guest.setRole(Role.GUEST);
        Criteria crit = new Criteria("gameId").is(gameId).and("attendees").is(guest).and("state")
                .is(ChallengeState.PROPOSED);
        return mongo.find(new Query(crit), GroupChallenge.class);
    }

    @Override
    public GroupChallenge deleteProposedChallengeByGuest(String gameId, String playerId,
            String instanceName) {
        Attendee guest = new Attendee();
        guest.setPlayerId(playerId);
        guest.setRole(Role.GUEST);
        Criteria crit = new Criteria("gameId").is(gameId).and("attendees").is(guest).and("state")
                .is(ChallengeState.PROPOSED)
                .and("instanceName").is(instanceName);
        return mongo.findAndRemove(new Query(crit), GroupChallenge.class);
    }

    @Override
    public GroupChallenge deleteProposedChallengeByProposer(String gameId, String playerId,
            String instanceName) {
        Attendee proposer = new Attendee();
        proposer.setPlayerId(playerId);
        proposer.setRole(Role.PROPOSER);
        Criteria crit = new Criteria("gameId").is(gameId).and("attendees").is(proposer).and("state")
                .is(ChallengeState.PROPOSED)
                .and("instanceName").is(instanceName);
        return mongo.findAndRemove(new Query(crit), GroupChallenge.class);
    }

}
