package eu.trentorise.smartcampus.gamification_web.models.classification;

public class ClassificationData implements Comparable<ClassificationData> {

	private String playerId;
	private String nickName;
	private Integer score;
	private Integer position;
	
	public String getPlayerId() {
		return playerId;
	}

	public String getNickName() {
		return nickName;
	}

	public int getScore() {
		return score;
	}

	public int getPosition() {
		return position;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ClassificationData() {
		super();
	}

	public ClassificationData(String playerId, String nickName, Integer score, Integer position) {
		super();
		this.playerId = playerId;
		this.nickName = nickName;
		this.score = score;
		this.position = position;
	}

	/*public int compare(ClassificationData player2, ClassificationData player1)
    {
        return  player1.score.compareTo(player2.score);
    }*/

	@Override
	public int compareTo(ClassificationData player) {
		return (this.score).compareTo(player.score);
	}

}
