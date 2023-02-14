package eu.trentorise.game.bean;

import eu.trentorise.game.model.PointConcept;

public class PointConceptDTO {

	private String id;
	private PointConcept pc;

	public PointConceptDTO(String id, PointConcept pc) {
		super();
		this.id = id;
		this.pc = pc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PointConcept getPc() {
		return pc;
	}

	public void setPc(PointConcept pc) {
		this.pc = pc;
	}

}
