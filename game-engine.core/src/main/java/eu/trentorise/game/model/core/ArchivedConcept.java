package eu.trentorise.game.model.core;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.GroupChallenge;

@Document(collection="challengeArchive")
public class ArchivedConcept {
    private String id;
    private String gameId;
    private String playerId;
    private ChallengeConcept challenge;
    private GroupChallenge groupChallenge;
    private Date archivingDate = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public ChallengeConcept getChallenge() {
        return challenge;
    }

    public void setChallenge(ChallengeConcept challenge) {
        this.challenge = challenge;
    }

    public GroupChallenge getGroupChallenge() {
        return groupChallenge;
    }

    public void setGroupChallenge(GroupChallenge groupChallenge) {
        this.groupChallenge = groupChallenge;
    }

    public Date getArchivingDate() {
        return archivingDate;
    }

    public void setArchivingDate(Date archivingDate) {
        this.archivingDate = archivingDate;
    }



}
