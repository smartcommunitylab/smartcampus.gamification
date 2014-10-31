package eu.trentorise.game.repo;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericObjectPersistence {
	private Map<String, Object> concept;
	private String type;

	public GenericObjectPersistence(Object obj) {
		ObjectMapper mapper = new ObjectMapper();

		this.concept = mapper.convertValue(obj, Map.class);
		this.type = obj.getClass().getCanonicalName();
	}

	public GenericObjectPersistence() {
	}

	public Map<String, Object> getConcept() {
		return concept;
	}

	public void setConcept(Map<String, Object> concept) {
		this.concept = concept;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
