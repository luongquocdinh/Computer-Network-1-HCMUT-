package data;

import java.io.Serializable;

import tags.Tags;

@SuppressWarnings("serial")
public class dataFile implements Serializable{

	@SuppressWarnings("unused")
	private String openTags = Tags.FILE_DATA_OPEN_TAG;
	@SuppressWarnings("unused")
	private String closeTags = Tags.FILE_DATA_CLOSE_TAG;
	public byte[] data;

	public dataFile(int size) {
		data = new byte[size];
	}
	
	public dataFile() {
		data = new byte[Tags.MAX_MSG_SIZE];
	}
}
