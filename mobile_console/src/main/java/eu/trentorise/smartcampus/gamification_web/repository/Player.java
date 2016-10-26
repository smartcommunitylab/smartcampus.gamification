package eu.trentorise.smartcampus.gamification_web.repository;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.trentorise.smartcampus.gamification_web.models.Event;
import eu.trentorise.smartcampus.gamification_web.models.PersonalData;
import eu.trentorise.smartcampus.gamification_web.models.SurveyData;

@Document(collection="player")
public class Player {
	
	@Id
	private String pid;
	private String type;	// test or prod
	private String socialId;
	
	private String name;
	private String surname;
	private String nikName;
	private String mail;
	private String language;
	private boolean sendMail;
	private PersonalData personalData;
	private SurveyData surveyData;
	private boolean checkedRecommendation;
	private List<Event> eventsCheckIn;
	
	public Player() {
		super();
	}

	public Player(String pid, String socialId, String name, String surname, String nikName,
			//String mail, String language, boolean sendMail, PersonalData personalData, SurveyData surveyData, String type) {
			String mail, String language, boolean sendMail, PersonalData personalData, SurveyData surveyData, boolean checkRecommendation, String type) {
		super();
		this.pid = pid;
		this.socialId = socialId;
		this.name = name;
		this.surname = surname;
		this.nikName = nikName;
		this.mail = mail;
		this.language = language;
		this.sendMail = sendMail;
		this.personalData = personalData;
		this.surveyData = surveyData;
		this.checkedRecommendation = checkRecommendation;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getNikName() {
		return nikName;
	}

	public String getMail() {
		return mail;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setNikName(String nikName) {
		this.nikName = nikName;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPid() {
		return pid;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public PersonalData getPersonalData() {
		return personalData;
	}

	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}
	
	public SurveyData getSurveyData() {
		return surveyData;
	}

	public void setSurveyData(SurveyData surveyData) {
		this.surveyData = surveyData;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isCheckedRecommendation() {
		return checkedRecommendation;
	}

	public void setCheckedRecommendation(boolean checkedRecommendation) {
		this.checkedRecommendation = checkedRecommendation;
	}

	public List<Event> getEventsCheckIn() {
		return eventsCheckIn;
	}

	public void setEventsCheckIn(List<Event> eventsCheckIn) {
		this.eventsCheckIn = eventsCheckIn;
	}

	public String toJSONString() {
		String pdata = null;
		if(personalData!=null){
			pdata = personalData.toJSONString();
		}
		String sdata = null;
		if(surveyData!=null){
			sdata = surveyData.toJSONString();
		}
		return "{\"pid\":\"" + pid + "\", \"socialId\":\"" + socialId + "\", \"type\":\"" + type + "\", \"name\":\""
				+ name + "\", \"surname\":\"" + surname + "\", \"nikName\":\"" + nikName
				+ "\", \"mail\":\"" + mail + "\", \"personalData\":" + pdata + ", \"surveyData\":" + sdata +"}";
	}
	
}
