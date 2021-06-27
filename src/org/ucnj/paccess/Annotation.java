package org.ucnj.paccess;

/**
 * Class that represents one annotation note from a possible
 * larger set of associated notes
 */
public class Annotation {
	
	private String note = "";
	private String openDate = "";
	
	public Annotation(String note, String openDate)
	{
		this.note = note;
		this.openDate = openDate;
	}

	public java.lang.String getNote() {
		return note;
	}

	public java.lang.String getOpenDate() {
		return openDate;
	}

}
