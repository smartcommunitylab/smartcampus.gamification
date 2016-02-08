package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.List;

import eu.trentorise.game.repo.StatePersistence;

public class Team extends PlayerState {

	public static enum MemberType {
		PLAYER, TEAM
	};

	public Team() {

	}

	public Team(StatePersistence state) {
		super(state);
		name = (String) state.getMetadata().get("name");
		members = (List<String>) state.getMetadata().get("members");
	}

	private String name;

	private List<String> members = new ArrayList<>();

	private MemberType memberType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

}
