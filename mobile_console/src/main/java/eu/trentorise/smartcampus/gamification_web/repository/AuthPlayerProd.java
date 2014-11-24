package eu.trentorise.smartcampus.gamification_web.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="auth_player_prod")
public class AuthPlayerProd {
	
	@Id
	private String pid;
	
	private String name;
	private String surname;
	private String nikName;
	private String mail;
	
	public AuthPlayerProd() {
		super();
	}

	public AuthPlayerProd(String pid, String name, String surname, String nikName,
			String mail) {
		super();
		this.pid = pid;
		this.name = name;
		this.surname = surname;
		this.nikName = nikName;
		this.mail = mail;
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

	public void setPid(String pid) {
		this.pid = pid;
	}


	public String toJSONString() {
		return "{\"pid\":\"" + pid + "\", \"name\":\""
				+ name + "\", \"surname\":\"" + surname + "\", \"nikName\":\"" + nikName
				+ "\", \"mail\":\"" + mail + "\"}";
	}
	
}
