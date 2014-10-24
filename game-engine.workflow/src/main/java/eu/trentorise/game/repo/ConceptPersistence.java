package eu.trentorise.game.repo;

import java.util.Map;

public class ConceptPersistence {
	private Map<String, Object> concept;
	private String type;

	public ConceptPersistence(Map<String, Object> concept, String type) {
		this.concept = concept;
		this.type = type;
	}

	public ConceptPersistence() {
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
