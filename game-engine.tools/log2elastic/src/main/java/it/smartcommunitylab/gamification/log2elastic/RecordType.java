package it.smartcommunitylab.gamification.log2elastic;

public enum RecordType {
    ACTION("Action"), RULE_POINTCONCEPT("PointConcept"), RULE_BADGECOLLECTIONCONCEPT(
            "BadgeCollectionConcept"), CLASSIFICATION("Classification"), CHALLENGEASSIGNED(
                    "ChallengeAssigned"), CHALLENGECOMPLETED("ChallengeCompleted"), USERCREATION(
                            "UserCreation"), ENDGAMEACTION("EndGameAction");

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
                return RecordType.CHALLENGEASSIGNED;
            case "ChallengeCompleted":
                return RecordType.CHALLENGECOMPLETED;
            case "UserCreation":
                return RecordType.USERCREATION;
            case "EndGameAction":
                return RecordType.ENDGAMEACTION;
            default:
                throw new IllegalArgumentException(recordType + " non e' supportato");
        }
    }
}
