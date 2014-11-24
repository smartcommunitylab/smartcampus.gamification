package eu.trentorise.smartcampus.gamification_web.models;

public class State {
	
	private String id = "";
	private String name = "";
	private String score = "";
	
	public State(){
		super();
	};
	
	public State(String id, String name, String score) {
		super();
		this.id = id;
		this.name = name;
		this.score = score;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getScore() {
		return score;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "\n State [id=" + id + ", \nname=" + name + ", \nscore=" + score + "]";
	}
	

}
