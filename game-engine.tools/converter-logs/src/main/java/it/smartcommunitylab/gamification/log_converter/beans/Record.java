package it.smartcommunitylab.gamification.log_converter.beans;

public class Record {
	private RecordType type;
	private String content;
	private int indexType;

	public int getIndexType() {
		return indexType;
	}

	public void setIndexType(int indexType) {
		this.indexType = indexType;
	}

	public Record() {
	}

	public RecordType getType() {
		return type;
	}

	public void setType(RecordType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
