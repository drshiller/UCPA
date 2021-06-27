package org.ucnj.paccess;

/**
 * This class represents one record of the party details data
 * queried from the C573WEB/WAPF01 and C573WEB/WAPF11 files.
 */
public class PartyName {
	private String lastName = "";
	private String firstName = "";
	private String corpType = "";

	public PartyName() {
		super();
	}

	public String getCorpType() {
		return corpType;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setCorpType(String newCorpType) {
		corpType = newCorpType;
	}

	public void setFirstName(String newFirstName) {
		firstName = newFirstName;
	}

	public void setLastName(String newLastName) {
		lastName = newLastName;
	}
}
