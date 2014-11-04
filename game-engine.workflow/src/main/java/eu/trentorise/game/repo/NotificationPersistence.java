package eu.trentorise.game.repo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification")
public class NotificationPersistence extends GenericObjectPersistence {

	@Id
	private String id;

	public NotificationPersistence(Object obj) {
		super(obj);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
