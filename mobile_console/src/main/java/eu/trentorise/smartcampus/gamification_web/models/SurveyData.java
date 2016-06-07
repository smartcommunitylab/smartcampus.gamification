package eu.trentorise.smartcampus.gamification_web.models;

public class SurveyData {

	private String gamimg_experience;
	private String change_of_habits;
	private String new_habits_maintaining;
	private String job_tranport_mode;
	private String free_time_tranport_mode;
	private String trip_type;
	private String new_mode_type;
	private String point_interest_in_game;
	private String badges_interest_in_game;
	private String challenges_interest_in_game;
	private String prize_interest_in_game;
	private String game_improve_suggestion;
	private String app_improve_suggestion;
	private Long timestamp;
	
	public SurveyData() {
		super();
	}

	public SurveyData(String gamimg_experience, String change_of_habits, String new_habits_maintaining,
			String job_tranport_mode, String free_time_tranport_mode, String trip_type, String new_mode_type,
			String point_interest_in_game, String badges_interest_in_game, String challenges_interest_in_game,
			String prize_interest_in_game, String game_improve_suggestion, String app_improve_suggestion,
			Long timestamp) {
		super();
		this.gamimg_experience = gamimg_experience;
		this.change_of_habits = change_of_habits;
		this.new_habits_maintaining = new_habits_maintaining;
		this.job_tranport_mode = job_tranport_mode;
		this.free_time_tranport_mode = free_time_tranport_mode;
		this.trip_type = trip_type;
		this.new_mode_type = new_mode_type;
		this.point_interest_in_game = point_interest_in_game;
		this.badges_interest_in_game = badges_interest_in_game;
		this.challenges_interest_in_game = challenges_interest_in_game;
		this.prize_interest_in_game = prize_interest_in_game;
		this.game_improve_suggestion = game_improve_suggestion;
		this.app_improve_suggestion = app_improve_suggestion;
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

	public String getJob_tranport_mode() {
		return job_tranport_mode;
	}

	public String getFree_time_tranport_mode() {
		return free_time_tranport_mode;
	}

	public String getTrip_type() {
		return trip_type;
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

	public String getGame_improve_suggestion() {
		return game_improve_suggestion;
	}

	public String getApp_improve_suggestion() {
		return app_improve_suggestion;
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

	public void setJob_tranport_mode(String job_tranport_mode) {
		this.job_tranport_mode = job_tranport_mode;
	}

	public void setFree_time_tranport_mode(String free_time_tranport_mode) {
		this.free_time_tranport_mode = free_time_tranport_mode;
	}

	public void setTrip_type(String trip_type) {
		this.trip_type = trip_type;
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

	public void setGame_improve_suggestion(String game_improve_suggestion) {
		this.game_improve_suggestion = game_improve_suggestion;
	}

	public void setApp_improve_suggestion(String app_improve_suggestion) {
		this.app_improve_suggestion = app_improve_suggestion;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "SurveyData [gamimg_experience=" + gamimg_experience + ", change_of_habits=" + change_of_habits
				+ ", new_habits_maintaining=" + new_habits_maintaining + ", job_tranport_mode=" + job_tranport_mode
				+ ", free_time_tranport_mode=" + free_time_tranport_mode + ", trip_type=" + trip_type
				+ ", new_mode_type=" + new_mode_type + ", point_interest_in_game=" + point_interest_in_game
				+ ", badges_interest_in_game=" + badges_interest_in_game + ", challenges_interest_in_game="
				+ challenges_interest_in_game + ", prize_interest_in_game=" + prize_interest_in_game
				+ ", game_improve_suggestion=" + game_improve_suggestion + ", app_improve_suggestion="
				+ app_improve_suggestion + ", timestamp=" + timestamp + "]";
	}
	
	public String toJSONString() {
		return "{\"gamimg_experience\":\"" + gamimg_experience + "\", \"change_of_habits\":\"" + change_of_habits
				+ "\", \"new_habits_maintaining\":\"" + new_habits_maintaining + "\", \"job_tranport_mode\":\"" + job_tranport_mode
				+ "\", \"free_time_tranport_mode\":\"" + free_time_tranport_mode + "\", \"trip_type\":\"" + trip_type
				+ "\", \"new_mode_type\":\"" + new_mode_type + "\", \"point_interest_in_game\":\"" + point_interest_in_game
				+ "\", \"badges_interest_in_game\":\"" + badges_interest_in_game + "\", \"challenges_interest_in_game\":\""
				+ challenges_interest_in_game + "\", \"prize_interest_in_game\":\"" + prize_interest_in_game
				+ "\", \"game_improve_suggestion\":\"" + game_improve_suggestion + "\", \"app_improve_suggestion\":\""
				+ app_improve_suggestion + "\", \"timestamp\":" + timestamp + "}";
	}

}
