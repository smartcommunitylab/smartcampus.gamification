package eu.trentorise.smartcampus.gamification_web.models;

import java.util.Arrays;

public class BagesData {
	
	private String imageName = "";
	private byte[] imageByte = null;
	private String imageType = "";
	private String textId = "";
	private String showedText = "";
	private String showedTextEng = "";
	
	public BagesData(){
		super();
	};
	
	public BagesData(String imageName, byte[] imageByte, String imageType,
			String textId, String showedText, String showedTextEng) {
		super();
		this.imageName = imageName;
		this.imageByte = imageByte;
		this.imageType = imageType;
		this.textId = textId;
		this.showedText = showedText;
		this.showedTextEng = showedTextEng;
	}

	public String getTextId() {
		return textId;
	}

	public String getShowedText() {
		return showedText;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

	public void setShowedText(String showedText) {
		this.showedText = showedText;
	}

	public String getImageName() {
		return imageName;
	}

	public byte[] getImageByte() {
		return imageByte;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setImageByte(byte[] imageByte) {
		this.imageByte = imageByte;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getShowedTextEng() {
		return showedTextEng;
	}

	public void setShowedTextEng(String showedTextEng) {
		this.showedTextEng = showedTextEng;
	}

	@Override
	public String toString() {
		return "BagesData [imageName=" + imageName + ", imageByte="
				+ Arrays.toString(imageByte) + ", imageType=" + imageType
				+ ", textId=" + textId + ", showedText=" + showedText + "]";
	}

}
