package it.smartcommunitylab.aac.model.authorization;

public class RequestedAuthorizationDTO {
	private AuthorizationResourceDTO resource;
	private AuthorizationUserDTO entity;
	private String action;

	public AuthorizationResourceDTO getResource() {
		return resource;
	}

	public void setResource(AuthorizationResourceDTO resource) {
		this.resource = resource;
	}

	public AuthorizationUserDTO getEntity() {
		return entity;
	}

	public void setEntity(AuthorizationUserDTO entity) {
		this.entity = entity;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
