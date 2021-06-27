package org.ucnj.paccess.servlets;

import javax.servlet.http.HttpServletRequest;

import org.ucnj.paccess.*;

public class NameResults extends PagedResults {

	private static final long serialVersionUID = 1L;

	protected String formatQuery(HttpServletRequest request)
	throws Exception {
	
		// pick up the last name
		if (request.getParameter("lName") != null) {
	
			// turn last name into sort name format
			String lastName = UCPA.removeSpecialChars(request.getParameter("lName"), true);
	
			if (lastName.length() > 0) {		
	
				// Are we doing a SoundEx query or a 'normal' one
				String sirNameopt = request.getParameter("lOpt");
				String qryStr = "";
				if (sirNameopt.equalsIgnoreCase("x"))
					qryStr = formatQryE(request, lastName);
				else
					qryStr = formatQryA(request, lastName);
				return qryStr;
			}
			else {
				throw new QueryException("The last name was empty after formatting.", "name");
			}
		}
		else {
			throw new QueryException("The last name was empty.", "name");
		}
		
	}

	protected void setCurrentOptions(UCPA userUCPA,
								   HttpServletRequest request,
								   Integer resultsPerPage)
	throws Exception {
	
		SearchOptions currentOptions = userUCPA.getCurrentSrchOpts();
		
		currentOptions.setFormLink("name");
		currentOptions.setLastName(request.getParameter("lName"));
		currentOptions.setLastNameOpt(request.getParameter("lOpt"));
		currentOptions.setFirstName(request.getParameter("fName"));
		currentOptions.setFirstNameOpt(request.getParameter("fOpt"));
		currentOptions.setDocTypes(request.getParameterValues("dt"));
		currentOptions.setResultsPerPage(resultsPerPage.toString());
		currentOptions.setPartyOpt(request.getParameter("pOpt"));
	
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		if (areDatesInOrder(from, to) == false) {
			String tmp = from;
			from = to;
			to = tmp;
		}
		currentOptions.setFromDate(from);
		currentOptions.setToDate(to);
	
		try {
			currentOptions.save(userUCPA, request.getRemoteAddr());
		}
		catch (Exception ex) {
			UCPA.throwExMsg("NameResults", "setCurrentOptions", ex);
		}
		
		return;
	}

	private static boolean isCodeMember (char c1, char chArray[]) { 
	
		for (int i = 0; i < chArray.length; i++) {
			if (c1 == chArray[i]) 
				return true;
		}
		return false;
	}

	private String formatDateOpt(String keyView, String fromDate, String toDate) {
	
					
		String outStr = "(C573WEB." + keyView + ".WSTPDT BETWEEN '";
	
		// if no FROM date provided then use default initial date
		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = UCPA.getInitialDate();
			}
		}
		else {
			fromDate = UCPA.getInitialDate();
		}
		
		// if no TO date provided then use today's date
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = UCPA.todaysDate();
			}
		}
		else {
			toDate = UCPA.todaysDate();
		}
	
		if (areDatesInOrder(fromDate, toDate) == false) {
			String tmp = fromDate;
			fromDate = toDate;
			toDate = tmp;
		}
		
		outStr += Result.transformDate(fromDate, true) + 
				  "' AND '" + Result.transformDate(toDate, true) + "')";
		
		return outStr;
	}

	private String formatNameOpt(String keyView, String nameOpt, String nameField, String nameStr) {
	
		String outStr = "(C573WEB." + keyView + "." + nameField;
		
	  	if (nameOpt != null) {
		  	if (nameOpt.equalsIgnoreCase("s")) {		// "starts with" wild card
			  	outStr += " LIKE '" + nameStr + "%')";
		  	}
			else if (nameOpt.equalsIgnoreCase("c")) {	// "contains" wild card
				outStr += " LIKE '%" + nameStr + "%')";
			}
		  	else {										// exact match
			  	outStr += " = '" + nameStr + "')";
		  	}
	  	}
		else {											// default: exact match
			outStr += " = '" + nameStr + "')";
		}
		
		return outStr;
	}

	private String formatQryA(HttpServletRequest request, String lastName) {
	
		// query string starts here
		String qryStr = "SELECT C573WEB.WALF01A.*, C573WEB.WAPF11.* " +
	  		            "FROM C573WEB.WALF01A, C573WEB.WAPF11 " +
	  		            "WHERE (C573WEB.WALF01A.WDOCTP = C573WEB.WAPF11.TCODE) ";
	
		// main condition is on sort name
		qryStr += "AND " + formatNameOpt("WALF01A", request.getParameter("lOpt"), "WSRTNM", lastName);
	
		// optional first name (i.e. given name) condition
		String givenName = request.getParameter("fName");
		if (givenName != null) {
			givenName = UCPA.removeSpecialChars(givenName, false);
			if (givenName.length() > 0 ) {
				qryStr += " AND " + formatNameOpt("WALF01A", request.getParameter("fOpt"), "WGIVNM", givenName);
			}
		}

		// date range condition
		qryStr += " AND " + formatDateOpt("WALF01A", request.getParameter("from"), request.getParameter("to"));

		// doc type conditions
		qryStr += formatDocTypeOpt("WALF01A", request.getParameterValues ("dt"));

		// party type condition
		qryStr += formatPartyOpt("WALF01A", request.getParameter("pOpt"));
	
		// sort order
		qryStr += " ORDER BY" +
		          " C573WEB.WALF01A.WSRTNM," +
		          " C573WEB.WALF01A.WSTPDT," +
		          " C573WEB.WALF01A.WINT#P," +
		          " C573WEB.WALF01A.WINT#M," +
		          " C573WEB.WALF01A.WINT#S," +
		          " C573WEB.WALF01A.WGIVNM";
	
		return qryStr;
	
	}

	private String formatQryE(HttpServletRequest request, String lastName) {
	
		// query string starts here
		String qryStr = "SELECT C573WEB.WALF01E.*, C573WEB.WAPF11.* " +
	  		            "FROM C573WEB.WALF01E, C573WEB.WAPF11 " +
	  		            "WHERE (C573WEB.WALF01E.WDOCTP = C573WEB.WAPF11.TCODE) ";
	
		// main condition is on SoundEx name
		qryStr += "AND (C573WEB.WALF01E.WSDXKY ='" + formatSoundex(lastName) + "')";
	
		// optional first name (i.e. given name) condition
		String givenName = request.getParameter("fName");
		if (givenName != null) {
			givenName = UCPA.removeSpecialChars(givenName, false);
			if (givenName.length() > 0 ) {
				qryStr += " AND " + formatNameOpt("WALF01E", request.getParameter("fOpt"), "WGIVNM", givenName);
			}
		}
	
		// date range condition
		qryStr += " AND " + formatDateOpt("WALF01E", request.getParameter("from"), request.getParameter("to"));
	
		// doc type conditions
		qryStr += formatDocTypeOpt("WALF01E", request.getParameterValues ("dt"));
	
		// party type condition
		qryStr += formatPartyOpt("WALF01E", request.getParameter("pOpt"));
	
		return qryStr;
	
	}

	private String formatSoundex(String sIn) {
	
		final int CODE_LENGTH = 5;
		
		char tmpCode;
		char vowelsEtc[] = {'A','E','I','O','U','Y','W','H'};
		char HandW[] = {'W','H'};
		int jump = 'a' - 'A';
		
		char codeArr[] = new char[CODE_LENGTH];
		for (int i = 0; i < CODE_LENGTH; i++)
			codeArr[i] = ' ';
		
		char chArr1[] = sIn.toCharArray(); 
	   
		// init first character
		if (chArr1[0] > 'Z') chArr1[0] = (char) (chArr1[0] - jump);
		codeArr[0] = findCodeMember(chArr1[0]);
		
		for (int i = 1, j = 1; ((i < chArr1.length) & (j < CODE_LENGTH)); i++) {
	
			// capitalize
			if (chArr1[i] > 'Z') chArr1[i] = (char) (chArr1[i] - jump);
			
			// check vowels
			if (isCodeMember(chArr1[i], vowelsEtc) == false) {
				tmpCode = findCodeMember(chArr1[i]);
	
				// previous character not H or W
				if (isCodeMember(chArr1[i-1], HandW) == false) {
					if (tmpCode != findCodeMember(chArr1[i-1])) 
						codeArr[j++] = tmpCode;
					else if (tmpCode != codeArr[j-1])
						codeArr[j++] = tmpCode;
				}
			}
		}
		
		 // restore the letter in the first position of the code
		codeArr[0] = chArr1[0];
	
		// working from the end, eliminate adjacent numeric chars that are the same
		for (int i = CODE_LENGTH-1; i > 0; i--)
			if (codeArr[i] == codeArr[i-1])
				codeArr[i] = ' ';
		  
		return new String(codeArr);
	
	}

	private static char findCodeMember (char ch) {
		
		char code1[] = {'B','P','F','V'};
		char code2[] = {'C','S','K','G','J','Q','X','Z'};
		char code3[] = {'D','T'};
		char code4[] = {'L'};
		char code5[] = {'M','N'};
		char code6[] = {'R'};
		
		char code = 0;
		if (isCodeMember (ch, code1)) code = '1';
		else if (isCodeMember (ch, code2)) code = '2';
		else if (isCodeMember (ch, code3)) code = '3';
		else if (isCodeMember (ch, code4)) code = '4';
		else if (isCodeMember (ch, code5)) code = '5';
		else if (isCodeMember (ch, code6)) code = '6';  
		
		return code;
										 
	}

}
