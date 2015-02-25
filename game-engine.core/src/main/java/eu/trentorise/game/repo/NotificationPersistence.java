package eu.trentorise.game.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.Notification;

@Document(collection = "notification")
public class NotificationPersistence extends GenericObjectPersistence {

	@Transient
	private final Logger logger = LoggerFactory
			.getLogger(NotificationPersistence.class);
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

	public Notification toNotification() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(getObj(),
					(Class<? extends Notification>) Thread.currentThread()
							.getContextClassLoader().loadClass(getType()));
		} catch (Exception e) {
			logger.error("Problem to load class {}", getType());
			return null;
		}
	}
}
