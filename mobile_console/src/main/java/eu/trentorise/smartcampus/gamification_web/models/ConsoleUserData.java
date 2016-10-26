package eu.trentorise.smartcampus.gamification_web.models;
import java.io.Serializable;

public class ConsoleUserData implements Serializable {

	private static final long serialVersionUID = -6595862532036311268L;
	private String id;
	private String username;
    private String password;
    private String appId;
    private String name;
    private String surname;
    private String mail;
    
	public String getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getAppId() {
		return appId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	

}
