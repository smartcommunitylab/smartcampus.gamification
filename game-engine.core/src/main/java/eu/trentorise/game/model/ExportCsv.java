package eu.trentorise.game.model;

public class ExportCsv {
	private String filename;
	private String contentType;
	private StringBuffer content;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public StringBuffer getContent() {
		return content;
	}
	public void setContent(StringBuffer content) {
		this.content = content;
	}
}