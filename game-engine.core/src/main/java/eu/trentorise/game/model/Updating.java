package eu.trentorise.game.model;

/**
 * Game Engine Context Object, it is injected in game context when there is a
 * propagation of action from player to its teams and viceversa
 * 
 * @author mirko perillo
 * 
 */
public class Updating {
	private String tag;

	public Updating() {
	}

	public Updating(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
