package org.ucnj.paccess;

/**
 * This class represents one record of the miscellaneous details data
 * queried from the C573WEB/WAPF03 and C573WEB/WAPF10 files.
 */
public class MiscDetail {
	private int seqNumber = 0;
	private String description = "";
	private String data = "";
	private int column = 0;
	public MiscDetail() {
		super();
	}
	public int getColumn() {
		return column;
	}
	public String getData() {
		return data;
	}
	public String getDescription() {
		return description;
	}
	public int getSeqNumber() {
		return seqNumber;
	}
	public void setColumn(int newColumn) {
		column = newColumn;
	}
	public void setData(String newData) {
		data = newData;
	}
	public void setDescription(String newDescription) {
		description = newDescription;
	}
	public void setSeqNumber(int newSeqNumber) {
		seqNumber = newSeqNumber;
	}
}
