package org.ucnj.paccess;

/**
 * This class represents one record of the board date data
 * as held in the C573WEB/WAPF11 document type file.
 */
public class BoardDate {
	private String docType = "";
	private String startDate = "";
	private String boardDate = "";

	public BoardDate() {
		super();
	}
	
	public String getBoardDate() {
		return boardDate;
	}
	
	public String getDocType() {
		return docType;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setBoardDate(String newBoardDate) {
		boardDate = newBoardDate;
	}
	
	public void setDocType(String newDocType) {
		docType = newDocType;
	}
	
	public void setStartDate(String newStartDate) {
		startDate = newStartDate;
	}
}
