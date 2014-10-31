package eu.trentorise.game.repo;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericObjectPersistence {
	private Map<String, Object> obj;
	private String type;

	public GenericObjectPersistence(Object obj) {
		ObjectMapper mapper = new ObjectMapper();

		this.obj = mapper.convertValue(obj, Map.class);
		this.type = obj.getClass().getCanonicalName();
	}

	public GenericObjectPersistence() {
	}

	public Map<String, Object> getObj() {
		return obj;
	}

	public void setObj(Map<String, Object> concept) {
		this.obj = concept;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
