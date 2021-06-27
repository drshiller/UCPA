package org.ucnj.paccess.servlets;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.*;

import org.ucnj.paccess.*;

public class DateResults extends PagedResults {
	
	private static final long serialVersionUID = 1L;
	
	private final int MAX_DIFF_DAYS = 31;

	/**
	 * @see com.aspedient.unionco.paccess.servlets.PagedResults#formatQuery(HttpServletRequest)
	 */
	protected String formatQuery(HttpServletRequest request) throws Exception {

		// doc type and date range args must be present on request
		if ((request.getParameter("dt") == null) ||
			(request.getParameter("from") == null) ||
			(request.getParameter("to") == null))
		{
			throw new QueryException("Missing values for query", "date");
		}

		// query string starts here
		String qryStr = "SELECT C573WEB.WALF01B.*, C573WEB.WAPF11.* " +
	  		            "FROM C573WEB.WALF01B, C573WEB.WAPF11 " +
	  		            "WHERE (C573WEB.WALF01B.WDOCTP = C573WEB.WAPF11.TCODE)";
		
		// begin document type
		String sDocType = "C573WEB.WALF01B.WDOCTP = ";
		int docType = Integer.parseInt(request.getParameter("dt").trim());
		qryStr +=       " AND (" + sDocType + docType;
		
		// special cases
		if (docType == UCPA.DOC_TYPE_LIS_PENDENS) {
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_LIS_PENDENS_DISCHARGE);
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_LIS_PENDENS_IN_REM);
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_LIS_PENDENS_IN_REM_DISMISSED);
		}
		else if (docType == UCPA.DOC_TYPE_UCC) {
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_UCC_INITIAL);
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_UCC_AMENDMENT);
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_UCC_ASSIGNMENT);
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_UCC_CONTINUATION);
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_UCC_TERMINATION);
			qryStr +=   (" OR " + sDocType + UCPA.DOC_TYPE_UCC_RELEASE);
		}
		
		// end document type
		qryStr +=       ")";

		// reverse flag doesn't apply to certain doc types
		if (docType != UCPA.DOC_TYPE_FEDERAL_TAX_LIEN
			&& docType != UCPA.DOC_TYPE_FEDERAL_TAX_LIEN_RELEASE
			&& docType != UCPA.DOC_TYPE_UCC 
			&& docType != UCPA.DOC_TYPE_UCC_INITIAL 
			&& docType != UCPA.DOC_TYPE_UCC_AMENDMENT 
			&& docType != UCPA.DOC_TYPE_UCC_ASSIGNMENT
			&& docType != UCPA.DOC_TYPE_UCC_CONTINUATION 
			&& docType != UCPA.DOC_TYPE_UCC_TERMINATION 
			&& docType != UCPA.DOC_TYPE_UCC_RELEASE
			&& docType != UCPA.DOC_TYPE_TRADE_NAMES
			&& docType != UCPA.DOC_TYPE_TRADE_NAMES_DISSOLUTION
			&& docType != UCPA.DOC_TYPE_NOTARY_CERTIFICATION
			&& docType != UCPA.DOC_TYPE_INHERITANCE_TAX_WAIVERS)
		{
	  		qryStr +=   " AND (C573WEB.WALF01B.WREVFL = 'R')";
		}
	
		// date range condition
		String fromStr = request.getParameter("from").trim();
		String toStr = request.getParameter("to").trim();
		qryStr += 		" AND " + formatDateOpt("WALF01B", fromStr, toStr);
		
		// party type condition
		qryStr += formatPartyOpt("WALF01B", request.getParameter("pOpt"));

		return qryStr;
		
	}

	protected void setCurrentOptions(
		UCPA userUCPA,
		HttpServletRequest request,
		Integer resultsPerPage)
	throws Exception {
	
		SearchOptions currentOptions = userUCPA.getCurrentSrchOpts();
		
		currentOptions.setFormLink("date");
		currentOptions.setDocTypeDR(request.getParameter("dt"));
		currentOptions.setResultsPerPageDR(resultsPerPage.toString());
	
		String fromStr = formatDate(request.getParameter("from"));
		String toStr = formatDate(request.getParameter("to"));
		if (areDatesInOrder(fromStr, toStr) == false) {
			String tmp = fromStr;
			fromStr = toStr;
			toStr = tmp;
		}
		toStr = checkEndDate(fromStr, toStr);
		currentOptions.setFromDateDR(fromStr);
		currentOptions.setToDateDR(toStr);
		currentOptions.setPartyOptDR(request.getParameter("pOpt"));
	
		try {
			currentOptions.save(userUCPA, request.getRemoteAddr());
		}
		catch (Exception ex) {
			UCPA.throwExMsg("DateResults", "setCurrentOptions", ex);
		}
		
		return;
	}

	private String formatDateOpt(String keyView, String fromStr, String toStr) throws Exception {
					
		String outStr = "(C573WEB." + keyView + ".WSTPDT BETWEEN '";
		
		if (validateDate(fromStr) == false)
			throw new QueryException("The starting date is not valid.", "date");
		if (validateDate(toStr) == false)
			throw new QueryException("The ending date is not valid.", "date");
			
		fromStr = formatDate(fromStr);
		toStr = formatDate(toStr);
			
		if (areDatesInOrder(fromStr, toStr) == false) {
			String tmp = fromStr;
			fromStr = toStr;
			toStr = tmp;
		}
		
		toStr = checkEndDate(fromStr, toStr);
		
		outStr += Result.transformDate(fromStr, true) + 
				  "' AND '" + Result.transformDate(toStr, true) + "')";
		
		return outStr;
	}
	
	private boolean validateDate(String dt) {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			df.setLenient(false);
			df.parse(dt);
			return true;
		}
		catch (ParseException e) {
			return false;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	private String checkEndDate(String fromStr, String toStr) {
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		Calendar fromCal = Calendar.getInstance();
		try {
			fromCal.setTime(df.parse(fromStr));
		}
		catch (ParseException e) {}
		Calendar toCal = Calendar.getInstance();
		try {
			toCal.setTime(df.parse(toStr));
		}
		catch (ParseException e) {}
			
		long diffMs = Math.abs(toCal.getTime().getTime() - fromCal.getTime().getTime());
		long diffDays = diffMs / (24*60*60*1000);
		if (diffDays > MAX_DIFF_DAYS) {
			toCal = (Calendar)fromCal.clone();
			toCal.add(Calendar.DATE, MAX_DIFF_DAYS);
		}
		
		int iMonth = toCal.get(Calendar.MONTH) + 1;
		String month = "";
		if (iMonth < 10) month = "0";
		month += Integer.toString(iMonth);
		int iDay = toCal.get(Calendar.DAY_OF_MONTH);
		String day = "";
		if (iDay < 10) day = "0";
		day += Integer.toString(iDay);
		String year = Integer.toString(toCal.get(Calendar.YEAR));
		toStr = month + "/" + day + "/" + year;
		
		return toStr;
	}
	
	private String formatDate(String dateStr) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		Calendar aCal = Calendar.getInstance();
		try {
			aCal.setTime(df.parse(dateStr));
		}
		catch (ParseException e) {}
		
		int iMonth = aCal.get(Calendar.MONTH) + 1;
		String month = "";
		if (iMonth < 10) month = "0";
		month += Integer.toString(iMonth);
		int iDay = aCal.get(Calendar.DAY_OF_MONTH);
		String day = "";
		if (iDay < 10) day = "0";
		day += Integer.toString(iDay);
		String year = Integer.toString(aCal.get(Calendar.YEAR));
		dateStr = month + "/" + day + "/" + year;
		
		return dateStr;
	}

}
