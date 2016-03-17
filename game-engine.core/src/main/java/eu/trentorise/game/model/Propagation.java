package eu.trentorise.game.model;

/**
 * Game Engine Context Object, it is injected in game context when there is a
 * propagation of action from player to its teams and viceversa
 * 
 * @author mirko perillo
 * 
 */
public class Propagation {
	private String action;

	public Propagation() {
	}

	public Propagation(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Propagation) {
			Propagation o2 = (Propagation) obj;
			return o2.getAction() == null && action == null
					|| o2.getAction().equals(action);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		if (action != null) {
			hash = hash * 17 + action.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		return String.format("%s(action=%s)", getClass().getSimpleName(),
				action);
	}
}
