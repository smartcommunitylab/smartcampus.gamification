package it.smartcommunitylab.aac.model.authorization;

public class AccountAttributeDTO {

	private String accountName;
	private String attributeName;
	private String attributeValue;

	public AccountAttributeDTO() {

	}

	public AccountAttributeDTO(String accountName, String attributeName, String attributeValue) {
		super();
		this.accountName = accountName;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
}
