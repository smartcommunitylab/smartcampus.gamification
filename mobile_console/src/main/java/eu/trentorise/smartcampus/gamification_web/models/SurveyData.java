package eu.trentorise.smartcampus.gamification_web.models;

public class SurveyData {

	private String user_residence_tn;
	private String user_commute_tn;
	private String gamimg_experience;
	private String change_of_habits;
	private String new_habits_maintaining;
	private String new_mode_type;
	private String point_interest_in_game;
	private String badges_interest_in_game;
	private String challenges_interest_in_game;
	private String prize_interest_in_game;
	private String game_liked_features;
	private String game_not_liked_features;
	private String improve_suggestion;
	private Long timestamp;
	
	public SurveyData() {
		super();
	}

	public SurveyData(String user_residence_tn, String user_commute_tn, String gamimg_experience,
			String change_of_habits, String new_habits_maintaining, String new_mode_type, String point_interest_in_game,
			String badges_interest_in_game, String challenges_interest_in_game, String prize_interest_in_game,
			String game_liked_features, String game_not_liked_features, String improve_suggestion, Long timestamp) {
		super();
		this.user_residence_tn = user_residence_tn;
		this.user_commute_tn = user_commute_tn;
		this.gamimg_experience = gamimg_experience;
		this.change_of_habits = change_of_habits;
		this.new_habits_maintaining = new_habits_maintaining;
		this.new_mode_type = new_mode_type;
		this.point_interest_in_game = point_interest_in_game;
		this.badges_interest_in_game = badges_interest_in_game;
		this.challenges_interest_in_game = challenges_interest_in_game;
		this.prize_interest_in_game = prize_interest_in_game;
		this.game_liked_features = game_liked_features;
		this.game_not_liked_features = game_not_liked_features;
		this.improve_suggestion = improve_suggestion;
		this.timestamp = timestamp;
	}



	public String getGamimg_experience() {
		return gamimg_experience;
	}

	public String getChange_of_habits() {
		return change_of_habits;
	}

	public String getNew_habits_maintaining() {
		return new_habits_maintaining;
	}

	public String getNew_mode_type() {
		return new_mode_type;
	}

	public String getPoint_interest_in_game() {
		return point_interest_in_game;
	}

	public String getBadges_interest_in_game() {
		return badges_interest_in_game;
	}

	public String getChallenges_interest_in_game() {
		return challenges_interest_in_game;
	}

	public String getPrize_interest_in_game() {
		return prize_interest_in_game;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setGamimg_experience(String gamimg_experience) {
		this.gamimg_experience = gamimg_experience;
	}

	public void setChange_of_habits(String change_of_habits) {
		this.change_of_habits = change_of_habits;
	}

	public void setNew_habits_maintaining(String new_habits_maintaining) {
		this.new_habits_maintaining = new_habits_maintaining;
	}

	public void setNew_mode_type(String new_mode_type) {
		this.new_mode_type = new_mode_type;
	}

	public void setPoint_interest_in_game(String point_interest_in_game) {
		this.point_interest_in_game = point_interest_in_game;
	}

	public void setBadges_interest_in_game(String badges_interest_in_game) {
		this.badges_interest_in_game = badges_interest_in_game;
	}

	public void setChallenges_interest_in_game(String challenges_interest_in_game) {
		this.challenges_interest_in_game = challenges_interest_in_game;
	}

	public void setPrize_interest_in_game(String prize_interest_in_game) {
		this.prize_interest_in_game = prize_interest_in_game;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getUser_residence_tn() {
		return user_residence_tn;
	}

	public String getUser_commute_tn() {
		return user_commute_tn;
	}

	public String getGame_liked_features() {
		return game_liked_features;
	}

	public String getGame_not_liked_features() {
		return game_not_liked_features;
	}

	public String getImprove_suggestion() {
		return improve_suggestion;
	}

	public void setUser_residence_tn(String user_residence_tn) {
		this.user_residence_tn = user_residence_tn;
	}

	public void setUser_commute_tn(String user_commute_tn) {
		this.user_commute_tn = user_commute_tn;
	}

	public void setGame_liked_features(String game_liked_features) {
		this.game_liked_features = game_liked_features;
	}

	public void setGame_not_liked_features(String game_not_liked_features) {
		this.game_not_liked_features = game_not_liked_features;
	}

	public void setImprove_suggestion(String improve_suggestion) {
		this.improve_suggestion = improve_suggestion;
	}

	/*@Override
	public String toString() {
		return "SurveyData [gamimg_experience=" + gamimg_experience + ", change_of_habits=" + change_of_habits
				+ ", new_habits_maintaining=" + new_habits_maintaining + ", job_transport_mode=" + job_transport_mode
				+ ", free_time_transport_mode=" + free_time_transport_mode + ", trip_type=" + trip_type
				+ ", new_mode_type=" + new_mode_type + ", point_interest_in_game=" + point_interest_in_game
				+ ", badges_interest_in_game=" + badges_interest_in_game + ", challenges_interest_in_game="
				+ challenges_interest_in_game + ", prize_interest_in_game=" + prize_interest_in_game
				+ ", game_improve_suggestion=" + game_improve_suggestion + ", app_improve_suggestion="
				+ app_improve_suggestion + ", timestamp=" + timestamp + "]";
	}*/
	
	/*public String toJSONString() {
		String gameSuggestion = "";
		if(game_improve_suggestion != null){
			gameSuggestion = game_improve_suggestion.replaceAll("[-+.^:,\\n\\t]","");
		}
		String appSuggestion = "";
		if(app_improve_suggestion != null){
			appSuggestion = app_improve_suggestion.replaceAll("[-+.^:,\\n\\t]","");
		}
		
		return "{\"gamimg_experience\":\"" + gamimg_experience + "\", \"change_of_habits\":\"" + change_of_habits
				+ "\", \"new_habits_maintaining\":\"" + new_habits_maintaining + "\", \"job_transport_mode\":\"" + job_transport_mode
				+ "\", \"free_time_transport_mode\":\"" + free_time_transport_mode + "\", \"trip_type\":\"" + trip_type
				+ "\", \"new_mode_type\":\"" + new_mode_type + "\", \"point_interest_in_game\":\"" + point_interest_in_game
				+ "\", \"badges_interest_in_game\":\"" + badges_interest_in_game + "\", \"challenges_interest_in_game\":\""
				+ challenges_interest_in_game + "\", \"prize_interest_in_game\":\"" + prize_interest_in_game
				+ "\", \"game_improve_suggestion\":\"" + gameSuggestion + "\", \"app_improve_suggestion\":\""
				+ appSuggestion + "\", \"timestamp\":" + timestamp + "}";
	}*/

}
