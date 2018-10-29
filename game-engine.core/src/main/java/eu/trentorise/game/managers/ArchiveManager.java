package eu.trentorise.game.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.core.ArchivedConcept;

@Service
public class ArchiveManager {

    private static final String CHALLENGE_ARCHIVE_COLLECTION = "challengeArchive";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void moveToArchive(String gameId, String playerId, ChallengeConcept challenge) {
        ArchivedConcept archived = new ArchivedConcept();
        archived.setChallenge(challenge);
        archived.setGameId(gameId);
        archived.setPlayerId(playerId);
        mongoTemplate.save(archived, CHALLENGE_ARCHIVE_COLLECTION);
    }

    public void moveToArchive(String gameId, GroupChallenge challenge) {
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGroupChallenge(challenge);
        archived.setGameId(gameId);
        mongoTemplate.save(archived, CHALLENGE_ARCHIVE_COLLECTION);
    }
}
