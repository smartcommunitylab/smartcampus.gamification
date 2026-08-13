package it.smartcommunitylab.aac.model.authorization;

import java.util.List;

public class AuthorizationResourceDTO {
	private String qnameRef;
	private List<AuthorizationNodeValueDTO> values;

	public String getQnameRef() {
		return qnameRef;
	}

	public void setQnameRef(String qnameRef) {
		this.qnameRef = qnameRef;
	}

	public List<AuthorizationNodeValueDTO> getValues() {
		return values;
	}

	public void setValues(List<AuthorizationNodeValueDTO> values) {
		this.values = values;
	}

}
