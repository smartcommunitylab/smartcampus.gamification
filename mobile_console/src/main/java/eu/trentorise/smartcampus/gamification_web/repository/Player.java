package eu.trentorise.smartcampus.gamification_web.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="player")
public class Player {
	
	@Id
	private String id;
	
	private String name;
	private String surname;
	private String nikName;
	private String mail;
	
	public Player() {
		super();
	}

	public Player(String id, String name, String surname, String nikName,
			String mail) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.nikName = nikName;
		this.mail = mail;
	}

	public String getId() {
		return id;
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

	public void setId(String id) {
		this.id = id;
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
	

}
