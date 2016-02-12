/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.core.Notification;

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
