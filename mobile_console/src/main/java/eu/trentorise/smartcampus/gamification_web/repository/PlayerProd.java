package eu.trentorise.smartcampus.gamification_web.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.trentorise.smartcampus.gamification_web.models.PersonalData;
import eu.trentorise.smartcampus.gamification_web.models.SurveyData;

@Document(collection="player_prod")
public class PlayerProd {
	
	@Id
	private String pid;
	private String socialId;
	
	private String name;
	private String surname;
	private String nikName;
	private String mail;
	private PersonalData personalData;
	private SurveyData surveyData;
	
	public PlayerProd() {
		super();
	}

	public PlayerProd(String pid, String socialId, String name, String surname, String nikName,
			String mail, PersonalData personalData, SurveyData surveyData) {
		super();
		this.pid = pid;
		this.socialId = socialId;
		this.name = name;
		this.surname = surname;
		this.nikName = nikName;
		this.mail = mail;
		this.personalData = personalData;
		this.surveyData = surveyData;
	}

	public String getPId() {
		return pid;
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

	public void setPId(String pid) {
		this.pid = pid;
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

	public String toJSONString() {
		String pdata = null;
		if(personalData!=null){
			pdata = personalData.toJSONString();
		}
		String sdata = null;
		if(surveyData!=null){
			sdata = surveyData.toJSONString();
		}
		return "{\"pid\":\"" + pid + "\", \"socialId\":\"" + socialId + "\", \"name\":\""
				+ name + "\", \"surname\":\"" + surname + "\", \"nikName\":\"" + nikName
				+ "\", \"mail\":\"" + mail + "\", \"personalData\":" + pdata + ", \"surveyData\":" + sdata +"}";
	}

}
