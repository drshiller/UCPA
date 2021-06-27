package org.ucnj.paccess;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.ucnj.utilities.DbUtil;

public class SearchOptions {

	// the form that just stored search options
	private String formLink;

	// search by name options
	private String lastName;
	private String lastNameOpt;
	private String firstName;
	private String firstNameOpt;
	private String[] docTypes;
	private String fromDate;
	private String toDate;
	private String resultsPerPage;
	private String partyOpt;
	
	// search by date range options 
	private String docTypeDR;
	private String fromDateDR;
	private String toDateDR;
	private String resultsPerPageDR;
	private String partyOptDR;

	// search by book & page options
	private String docTypeBP;
	private String book;
	private String page;

	// search by instrument options
	private String docTypeI;
	private String year;
	private String prefix;
	private String middle;
	private String suffix;
	
	/**
	 * SearchOptions constructor comment.
	 */
	public SearchOptions() {
		
		super();
	
		setLastName("");
		setLastNameOpt("e");
		setFirstName("");
		setFirstNameOpt("s");
		setDocTypes(null);
		setFromDate("");
		setToDate("");
		setResultsPerPage("");
		setPartyOpt("3");
		
		setDocTypeDR("");
		setFromDateDR("");
		setToDateDR("");
		setResultsPerPageDR("");
	
		setDocTypeBP("");
		setBook("");
		setPage("");
	
		setDocTypeI("");
		setYear("");
		setPrefix("");
		setMiddle("");
		setSuffix("");
		
	}

	public String getBook() {
		return book;
	}

	public String getDocTypeBP() {
		return docTypeBP;
	}

	public String getDocTypeI() {
		return docTypeI;
	}

	public String getDocTypeDR() {
		return docTypeDR;
	}

	public String[] getDocTypes() {
		return docTypes;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFirstNameOpt() {
		return firstNameOpt;
	}

	public String getFormLink() {
		return formLink;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLastNameOpt() {
		return lastNameOpt;
	}

	public String getMiddle() {
		return middle;
	}

	public String getPage() {
		return page;
	}

	public String getPartyOpt() {
		return partyOpt;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getResultsPerPage() {
		return resultsPerPage;
	}

	public String getSuffix() {
		return suffix;
	}

	private String getTime() {
		Calendar rightNow = Calendar.getInstance();
		String hour = String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
		String minute = String.valueOf(rightNow.get(Calendar.MINUTE));
		if (minute.length() < 2) minute = "0" + minute;
		String second = String.valueOf(rightNow.get(Calendar.SECOND));
		if (second.length() < 2) second = "0" + second;
	
		return hour + minute + second;
	}

	public String getToDate() {
		return toDate;
	}

	public String getYear() {
		return year;
	}

	public String getFromDateDR() {
		return fromDateDR;
	}

	public String getPartyOptDR() {
		return partyOptDR;
	}

	public String getResultsPerPageDR() {
		return resultsPerPageDR;
	}

	public String getToDateDR() {
		return toDateDR;
	}

	public void save(UCPA userUCPA,
					 String addressIP)
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		String qryStr = "";
	
		// init 0ptional output fields
		String lastName      = UCPA.repeatChar(50, ' ');
		String lastType      = UCPA.repeatChar(1,  ' ');
		String firstName     = UCPA.repeatChar(50, ' ');
		String firstType     = UCPA.repeatChar(1,  ' ');
		String startDate     = UCPA.repeatChar(10, ' ');
		String endDate       = UCPA.repeatChar(10, ' ');
		String partyType     = UCPA.repeatChar(1,  ' ');
		String nameDocTypes  = UCPA.repeatChar(50, ' ');
		String resPerPage    = UCPA.repeatChar(3,  ' ');
		String bookDocType   = UCPA.repeatChar(3,  ' ');
		String book          = UCPA.repeatChar(5,  ' ');
		String page          = UCPA.repeatChar(4,  ' ');
		String instDocType   = UCPA.repeatChar(3,  ' ');
		String year          = UCPA.repeatChar(4,  ' ');
		String prefix        = UCPA.repeatChar(2,  ' ');
		String middle        = UCPA.repeatChar(6,  ' ');
		String suffix        = UCPA.repeatChar(2,  ' ');
		String dateDocType   = UCPA.repeatChar(3,  ' ');
		String dateStartDate = UCPA.repeatChar(10, ' ');
		String dateEndDate   = UCPA.repeatChar(10, ' ');
		String datePartyType = UCPA.repeatChar(1,  ' ');
		
		try {
			Context initCtx = new InitialContext();
//			String servletInstance = (String) initCtx.lookup("java:comp/env/servletInstance");
String servletInstance = "o1";
			
			// query string starts here
			qryStr = "INSERT INTO C573WEB.WAPF80 " +
		  		     "(LTYPE, LDATE, LTIME, LIP," +
		  		     " L1SNME, L1SNTP, L1GNME, L1GNTP, L1SDTE, L1EDTE, L1PRTY, L1DOCS, L1LINE," +
		  		     " L2DOCT, L2BOOK, L2PAGE," +
		  		     " L3DOCT, L3YEAR, L3PRE, L3MID, L3SUF," +
		  		     " L4DOCT, L4SDTE, L4EDTE, L4PRTY, LSRV) ";
	
			// Date/time stamp stored in all recs
			String logDate = Result.transformDate(UCPA.todaysDate(), true);
			String logTime = getTime();
	
			// store options for given search type
			String logType = "";
			if (getFormLink().equals("name")) {
				logType = "1";
				lastName = UCPA.removeSpecialChars(getLastName(), true); // remove chars that SQL doesn't like
				lastType = getLastNameOpt();
				firstName = UCPA.removeSpecialChars(getFirstName(), false); // remove chars that SQL doesn't like
				firstType = getFirstNameOpt();
				startDate = Result.transformDate(getFromDate(), true);
				endDate = Result.transformDate(getToDate(), true);
				partyType = getPartyOpt();
				resPerPage = UCPA.formatSpace(getResultsPerPage(), 3, true);
	
				// doc types held as bit settings in long string
				String types[] = getDocTypes();
				int nTypes = types.length;
				StringBuffer sbTypes = new StringBuffer(UCPA.repeatChar(50, '0')); // init all bits
				for (int i = 0; i < nTypes; i++) {
					int idx = new Integer(types[i]).intValue() - 1;
					if (idx < 50) {
						sbTypes.setCharAt(idx, '1');
					}
				}
				nameDocTypes = sbTypes.toString();
			}
			else if (getFormLink().equals("book")) {
				logType = "2";
				bookDocType = getDocTypeBP();
				book = getBook();
				page = getPage();
			}
			else if (getFormLink().equals("inst")) {
				logType = "3";
				instDocType = getDocTypeI();
				year = getYear();
				prefix = getPrefix();
				middle = getMiddle();
				suffix = getSuffix();
			}
			else if (getFormLink().equals("date")) {
				logType = "4";
				dateDocType = getDocTypeDR();
				dateStartDate = Result.transformDate(getFromDateDR(), true);
				dateEndDate = Result.transformDate(getToDateDR(), true);
				datePartyType = getPartyOptDR();
			}
			
			qryStr +=
		  		     "VALUES ('" + logType + "', '" + logDate + "', '" + logTime + "', '" + addressIP + "', '" + 
		  		              lastName + "', '" + lastType + "', '" + firstName + "', '" + firstType + "', '" +
		  		              startDate + "', '" + endDate + "', '" + partyType + "', '" + nameDocTypes + "', '" +
		  		              resPerPage + "', '" + bookDocType + "', '" + book + "', '" + page + "', '" +
		  		              instDocType + "', '" + year + "', '" + prefix + "', '" + middle + "', '" + suffix + "', '" +
		  		              dateDocType + "', '" + dateStartDate + "', '" + dateEndDate + "', '" + datePartyType + "', '" + 
		  		              servletInstance + "')";
		  		            
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(qryStr);
		  		            
		}
		catch (Exception ex) {
			UCPA.throwExMsg("SearchOptions", "save", ex);
		}
	
		// close all resources
		finally {
			DbUtil.close(conn, stmt, null);
		}
	
	}

	public void setBook(String book) {
		if (book != null) {
			this.book = book;
		}
		else {
			this.book = "";
		}
	}

	public void setDocTypeBP(String docTypeBP) {
		if (docTypeBP != null) {
			this.docTypeBP = docTypeBP;
		}
		else {
			this.docTypeBP = "";
		}
	}

	public void setDocTypeI(String docTypeI) {
		if (docTypeI != null) {
			this.docTypeI = docTypeI;
		}
		else {
			this.docTypeI = "";
		}
	}

	public void setDocTypeDR(String docTypeDR) {
		if (docTypeDR != null) {
			this.docTypeDR = docTypeDR;
		}
		else {
			this.docTypeDR = "";
		}
	}

	public void setDocTypes(String[] dt) {
		if (dt != null) {
			docTypes = new String[dt.length];
			for (int i = 0; i < dt.length; i++) {
				docTypes[i] = dt[i];
			}
		}
		else {
			docTypes = null;
		}
	}

	public void setFirstName(String firstName) {
		if (firstName != null) {
			this.firstName = firstName;
		}
		else {
			this.firstName = "";
		}
	}

	public void setFirstNameOpt(String firstNameOpt) {
		if (firstNameOpt != null) {
			this.firstNameOpt = firstNameOpt;
		}
		else {
			this.firstNameOpt = "";
		}
	}

	public void setFormLink(String formLink) {
		this.formLink = formLink;
	}

	public void setFromDate(String fromDate) {
		if (fromDate != null) {
			this.fromDate = fromDate;
		}
		else {
			this.fromDate = "";
		}
	}

	public void setLastName(String lastName) {
		if (lastName != null) {
			this.lastName = lastName;
		}
		else {
			this.lastName = "";
		}
	}

	public void setLastNameOpt(String lastNameOpt) {
		if (lastNameOpt != null) {
			this.lastNameOpt = lastNameOpt;
		}
		else {
			this.lastNameOpt = "";
		}
	}

	public void setMiddle(String middle) {
		if (middle != null) {
			this.middle = middle;
		}
		else {
			this.middle = "";
		}
	}

	public void setPage(String page) {
		if (page != null) {
			this.page = page;
		}
		else {
			this.page = "";
		}
	}

	public void setPartyOpt(String partyOpt) {
		if (partyOpt != null) {
			this.partyOpt = partyOpt;
		}
		else {
			this.partyOpt = "";
		}
	}

	public void setPrefix(String prefix) {
		if (prefix != null) {
			this.prefix = prefix;
		}
		else {
			this.prefix = "";
		}
	}

	public void setResultsPerPage(String resultsPerPage) {
		if (resultsPerPage != null) {
			this.resultsPerPage = resultsPerPage;
		}
		else {
			this.resultsPerPage = "";
		}
	}

	public void setSuffix(String suffix) {
		if (suffix != null) {
			this.suffix = suffix;
		}
		else {
			this.suffix = "";
		}
	}

	public void setToDate(String toDate) {
		if (toDate != null) {
			this.toDate = toDate;
		}
		else {
			this.toDate = "";
		}
	}

	public void setYear(String year) {
		if (year != null) {
			this.year = year;
		}
		else {
			this.year = "";
		}
	}

	public void setFromDateDR(String fromDateDR) {
		this.fromDateDR = fromDateDR;
	}

	public void setResultsPerPageDR(String resultsPerPageDR) {
		this.resultsPerPageDR = resultsPerPageDR;
	}

	public void setToDateDR(String toDateDR) {
		this.toDateDR = toDateDR;
	}

	public void setPartyOptDR(String partyOptDR) {
		this.partyOptDR = partyOptDR;
	}

}
