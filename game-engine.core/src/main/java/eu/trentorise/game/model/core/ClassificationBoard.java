package eu.trentorise.game.model.core;

import java.util.List;

public class ClassificationBoard {
	private String pointConceptName;
	private ClassificationType type;
	private List<ClassificationPosition> board;

	public String getPointConceptName() {
		return pointConceptName;
	}

	public void setPointConceptName(String pointConceptName) {
		this.pointConceptName = pointConceptName;
	}

	public ClassificationType getType() {
		return type;
	}

	public void setType(ClassificationType type) {
		this.type = type;
	}

	public List<ClassificationPosition> getBoard() {
		return board;
	}

	public void setBoard(List<ClassificationPosition> board) {
		this.board = board;
	}

}
