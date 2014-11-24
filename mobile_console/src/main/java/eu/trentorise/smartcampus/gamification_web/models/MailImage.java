package eu.trentorise.smartcampus.gamification_web.models;

import java.util.Arrays;

public class MailImage {
	
	private String imageName = "";
	private byte[] imageByte = null;
	private String imageType = "";
	
	public MailImage(){
		super();
	};
	
	public MailImage(String imageName, byte[] imageByte, String imageType) {
		super();
		this.imageName = imageName;
		this.imageByte = imageByte;
		this.imageType = imageType;
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

	@Override
	public String toString() {
		return "MailImage [imageName=" + imageName + ", imageByte="
				+ Arrays.toString(imageByte) + ", imageType=" + imageType + "]";
	}

}
