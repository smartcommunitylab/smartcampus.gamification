package it.smartcommunitylab.aac.model.authorization;

public class AuthorizationUserDTO {
	private AccountAttributeDTO accountAttribute;
	private String type;

	public AuthorizationUserDTO() {

	}

	public AuthorizationUserDTO(AccountAttributeDTO accountAttribute, String type) {
		super();
		this.accountAttribute = accountAttribute;
		this.type = type;
	}

	public AccountAttributeDTO getAccountAttribute() {
		return accountAttribute;
	}

	public void setAccountAttribute(AccountAttributeDTO accountAttribute) {
		this.accountAttribute = accountAttribute;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
