package it.smartcommunitylab.gamification.log2elastic;

public enum RecordType {
    ACTION("Action"), RULE_POINTCONCEPT("PointConcept"),

    RULE_BADGECOLLECTIONCONCEPT("BadgeCollectionConcept"), CLASSIFICATION("Classification"),

    CHALLENGE_ASSIGNED("ChallengeAssigned"), CHALLENGE_COMPLETED("ChallengeCompleted"),

    USER_CREATION("UserCreation"), END_GAME_ACTION("EndGameAction"),

    LEVEL_GAINED("LevelGained"), BLACKLIST("Blacklist"),

    UNBLACKLIST("Unblacklist"), CHALLENGE_PROPOSED("ChallengeProposed"),

    CHALLENGE_ACCEPTED("ChallengeAccepted"), CHOICE_ACTIVATED("ChoiceActivated"),

    CHALLENGE_REFUSED("ChallengeRefused"), CHALLENGE_FAILED("ChallengeFailed"),

    CHALLENGE_INVITATION("ChallengeInvitation"), CHALLENGE_INVITATION_ACCEPTED(
            "ChallengeInvitationAccepted"),

    CHALLENGE_INVITATION_REFUSED("ChallengeInvitationRefused"), CHALLENGE_INVITATION_CANCELED(
            "ChallengeInvitationCanceled");

    private String representation;

    private RecordType(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

    public static RecordType of(String recordType) {
        switch (recordType) {
            case "Action":
                return RecordType.ACTION;
            case "PointConcept":
                return RecordType.RULE_POINTCONCEPT;
            case "BadgeCollectionConcept":
                return RecordType.RULE_BADGECOLLECTIONCONCEPT;
            case "Classification":
                return RecordType.CLASSIFICATION;
            case "ChallengeAssigned":
                return RecordType.CHALLENGE_ASSIGNED;
            case "ChallengeCompleted":
                return RecordType.CHALLENGE_COMPLETED;
            case "UserCreation":
                return RecordType.USER_CREATION;
            case "EndGameAction":
                return RecordType.END_GAME_ACTION;
            case "LevelGained":
                return RecordType.LEVEL_GAINED;
            case "Blacklist":
                return RecordType.BLACKLIST;
            case "Unblacklist":
                return RecordType.UNBLACKLIST;
            case "ChallengeProposed":
                return RecordType.CHALLENGE_PROPOSED;
            case "ChallengeAccepted":
                return RecordType.CHALLENGE_ACCEPTED;
            case "ChoiceActivated":
                return RecordType.CHOICE_ACTIVATED;
            case "ChallengeRefused":
                return RecordType.CHALLENGE_REFUSED;
            case "ChallengeFailed":
                return RecordType.CHALLENGE_FAILED;
            case "ChallengeInvitation":
                return RecordType.CHALLENGE_INVITATION;
            case "ChallengeInvitationAccepted":
                return RecordType.CHALLENGE_INVITATION_ACCEPTED;
            case "ChallengeInvitationRefused":
                return RecordType.CHALLENGE_INVITATION_REFUSED;
            case "ChallengeInvitationCanceled":
                return RecordType.CHALLENGE_INVITATION_CANCELED;
            default:
                throw new IllegalArgumentException(recordType + " non e' supportato");
        }
    }
}
