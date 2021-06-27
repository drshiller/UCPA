package org.ucnj.paccess;

/**
 * This class represents one record of the name search result data
 * queried from the C573WEB/WAPF01 and C573WEB/WAPF11 files.
 * This is the main information for a registered document.
 * @author Steve Hiller
 * @see org.ucnj.paccess.servlets.NameResults
 * @see org.ucnj.paccess.UCPA
 * @see org.ucnj.paccess.request.Request
 * @see org.ucnj.paccess.request.RequestList
 * @see org.ucnj.paccess.request.Item
 */

public class Result {
	private Integer docType = null;
	private String docTypeDesc = "";
	private String lastName = "";
	private String firstName = "";
	private String stampDate = "";
	private String instrPrefix = "";
	private String instrMiddle = "";
	private String instrSuffix = "";
	private String book = "";
	private String page = "";
	private String corpType = "";
	private String partyType = "";
	private String party1Name = "";
	
	private String lastNameRev = "";
	private String firstNameRev = "";
	private String corpTypeRev = "";
	private String partyTypeRev = "";
	private String party2Name = "";

	private String imageID = "";
	private boolean imageAnnotated = false;
	private Integer numPages = null;

	/**
	 * Result constructor.
	 */
	public Result() {
		super();
	}
	
	/**
	 * Transforms the shorthand single char code of a corporation type
	 * into a full text version.
	 * @return The full text for a corporation type.
	 * @param type - The shorthand single char code of a corporation type. 
	 */
	private String corporationType(String type) {
	
		String corpStr = "";
	
		if (type.equals("C")) {
			corpStr = "CORP";
		}
		else if (type.equals("N")) {
			corpStr = "NUM";
		}
		else if (type.equals("R")) {
			corpStr = "CHURCH";
		}
		else if (type.equals("U")) {
			corpStr = "USA";
		}
			
		return corpStr;
	}
	
	/**
	 * @return The full text for the corpType setting for this registered document result.
	 */
	public String corpTypeFull() {
		return corporationType(corpType);
	}
	
	/**
	 * @return The full text for the corpTypeRev setting for this registered document result.
	 */
	public String corpTypeFullRev() {
		return corporationType(corpTypeRev);
	}
	
	/**
	 * @return The book setting for this registered document result.
	 */
	public String getBook() {
		return book;
	}
	
	/**
	 * @return The corporation type setting for this registered document result.
	 */
	public String getCorpType() {
		return corpType;
	}
	
	/**
	 * @return The reverse party coporation type setting for this registered document result.
	 */
	public String getCorpTypeRev() {
		return corpTypeRev;
	}

	public Integer getDocType() {
		return docType;
	}

	public String getDocTypeDesc() {
		return docTypeDesc;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFirstNameRev() {
		return firstNameRev;
	}

	public String getImageID() {
		return imageID;
	}

	public String getInstrMiddle() {
		return instrMiddle;
	}

	public String getInstrPrefix() {
		return instrPrefix;
	}

	public String getInstrSuffix() {
		return instrSuffix;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLastNameRev() {
		return lastNameRev;
	}

	public Integer getNumPages() {
		return numPages;
	}

	public String getPage() {
		return page;
	}

	public String getParty1Name() {
		return party1Name;
	}

	public String getParty2Name() {
		return party2Name;
	}

	public String getPartyType() {
		return partyType;
	}

	public String getPartyTypeRev() {
		return partyTypeRev;
	}

	public String getStampDate() {
		return stampDate;
	}

	public boolean isImageAnnotated() {
		return imageAnnotated;
	}

	public void setBook(java.lang.String newBook) {
		book = newBook;
	}

	public void setCorpType(String newCorpType) {
		corpType = newCorpType;
	}

	public void setCorpTypeRev(String newCorpTypeRev) {
		corpTypeRev = newCorpTypeRev;
	}

	public void setDocType(Integer newDocType) {
		docType = newDocType;
	}

	public void setDocTypeDesc(String newDocTypeDesc) {
		docTypeDesc = newDocTypeDesc;
	}

	public void setFirstName(String newFirstName) {
		firstName = newFirstName;
	}

	public void setFirstNameRev(String newFirstNameRev) {
		firstNameRev = newFirstNameRev;
	}

	public void setImageAnnotated(boolean newImageAnnotated) {
		imageAnnotated = newImageAnnotated;
	}

	public void setImageID(String newImageID) {
		imageID = newImageID;
	}

	public void setInstrMiddle(String newInstrMiddle) {
		instrMiddle = newInstrMiddle;
	}

	public void setInstrPrefix(String newInstrPrefix) {
		instrPrefix = newInstrPrefix;
	}

	public void setInstrSuffix(String newInstrSuffix) {
		instrSuffix = newInstrSuffix;
	}

	public void setLastName(String newLastName) {
		lastName = newLastName;
	}

	public void setLastNameRev(String newLastNameRev) {
		lastNameRev = newLastNameRev;
	}

	public void setNumPages(Integer newNumPages) {
		numPages = newNumPages;
	}

	public void setPage(String newPage) {
		page = newPage;
	}

	public void setParty1Name(String newParty1Name) {
		party1Name = newParty1Name;
	}

	public void setParty2Name(String newParty2Name) {
		party2Name = newParty2Name;
	}

	public void setPartyType(String newPartyType) {
		partyType = newPartyType;
	}

	public void setPartyTypeRev(String newPartyTypeRev) {
		partyTypeRev = newPartyTypeRev;
	}

	public void setStampDate(String newStampDate) {
		stampDate = newStampDate;
	}
	
	/**
	 * Transform a date string.
	 * @return The transformed date string.
	 * @param inDate - The date string to be transformed.
	 * @param asMDY - If true then transform from MM/DD/YYYY to YYYYMMDD; if false then YYYYMMDD to MM/DD/YYYY.
	 */
	public static String transformDate(String inDate, boolean asMDY) {
		
		String day, month, year;
	
		// assume incoming date formatted as MM/DD/YYYY
		if (asMDY == true) {
			day = inDate.substring(3, 5);
			month = inDate.substring(0, 2);
			year = inDate.substring(6, 10);
	
			// output as YYYYMMDD
			return year + month + day;
		}
	
		// assume incoming date formatted as YYYYMMDD
		else {
			year = inDate.substring(0,4);
			month = inDate.substring(4, 6);
			day = inDate.substring(6,8);
			
			// output as MM/DD/YYYY
			return month + "/" + day + "/" + year;
		}
	}
}
