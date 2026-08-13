package it.smartcommunitylab.aac.model.authorization;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationNodeDTO {
	private String qname;
	private List<AuthorizationNodeParamDTO> parameters = new ArrayList<>();

	public String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

	public List<AuthorizationNodeParamDTO> getParameters() {
		return parameters;
	}

	public void setParameters(List<AuthorizationNodeParamDTO> parameters) {
		this.parameters = parameters;
	}

}
