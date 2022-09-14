package eu.trentorise.game.stats;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

public class Record {

	private static final Logger logger = Logger.getLogger(Record.class);

	public static final String TYPE_FIELD_NAME = "type=";
	private RecordType type;
	private String content;
	private int indexType;

	public int getIndexType() {
		return indexType;
	}

	public Record(String content) {
		Preconditions.checkNotNull(content, "content cannot be null");
		this.content = content;
		indexType = content.indexOf(TYPE_FIELD_NAME);
		String recordType = null;
		if (indexType > -1) {
			int indexTypeValue = indexType + TYPE_FIELD_NAME.length();
			if (content.indexOf(" ", indexTypeValue) >= 0) {
				recordType = content.substring(indexTypeValue, content.indexOf(" ", indexTypeValue));
			} else {
				recordType = content.substring(indexTypeValue, content.length());
			}
		}
		logger.debug("type: " + recordType);
		type = RecordType.of(recordType);
	}

	public RecordType getType() {
		return type;
	}

	public String getContent() {
		return content;
	}

}
