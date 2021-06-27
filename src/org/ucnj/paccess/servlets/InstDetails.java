package org.ucnj.paccess.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.ucnj.paccess.QueryException;
import org.ucnj.paccess.SearchOptions;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public class InstDetails extends Details {

	private static final long serialVersionUID = 1L;

	public boolean doMainQuery(HttpServletRequest request,
						 	   UCPA userUCPA)
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		// doc type, year and middle instrument # args must be present on request
		if ((request.getParameter("dt") == null) ||
			(request.getParameter("yr") == null) ||
			(request.getParameter("im") == null)) {
				throw new QueryException("Missing values for details key.", "inst");
		}
	
		try {
			// pick up key values from request
			String docType = request.getParameter("dt").trim();
			String year = request.getParameter("yr");
			
			// Instrument # middle has leading spaces
			// NB: field width is 6 chars and should be stored somewhere else
			String instrMiddle = UCPA.formatSpace(request.getParameter("im"), 6, true);
	
			// Pick up req'd prefix; if not present then uses default blank prefix (field width = 2)
			String instrPrefix = "  ";
			if (request.getParameter("ip") != null)
				if (request.getParameter("ip").length() > 0)
					instrPrefix = UCPA.formatSpace(request.getParameter("ip").toUpperCase(), 2, false);
	
			// Pick up req'd suffix; if not present then uses default blank suffix (field width = 2)
			String instrSuffix = "  ";
			if (request.getParameter("is") != null)
				if (request.getParameter("is").length() > 0)
					instrSuffix = UCPA.formatSpace(request.getParameter("is").toUpperCase(), 2, false);
	
			// query string starts here
			String qryStr = "SELECT C573WEB.WALF01D.*, C573WEB.WAPF11.* " +
		  		            "FROM C573WEB.WALF01D, C573WEB.WAPF11 " +
		  		            "WHERE (C573WEB.WALF01D.WDOCTP = C573WEB.WAPF11.TCODE)";
		  	
			// special case for UCC
			if (docType.equals(Integer.toString(UCPA.DOC_TYPE_UCC))) {
				qryStr +=   " AND (C573WEB.WALF01D.WDOCTP = " + UCPA.DOC_TYPE_UCC 
				               + " OR C573WEB.WALF01D.WDOCTP = " + UCPA.DOC_TYPE_UCC_INITIAL + ")";
			}
			else {
				qryStr +=   " AND (C573WEB.WALF01D.WDOCTP = " + docType + ")";
			}
		  	
		  	// key values	             
			qryStr +=       " AND (C573WEB.WALF01D.WSTPYY = '" + year + "')" +
		  		            " AND (C573WEB.WALF01D.WINT#M = '" + instrMiddle + "')" +
							" AND (C573WEB.WALF01D.WINT#S = '" + instrSuffix + "')" +
							" AND (C573WEB.WALF01D.WINT#P = '" + instrPrefix + "') ";
		  		            
		  	// order such that Party 1 Names appear before Party 2 Names,
		  	// and the primary name appears first within each party group
		  	qryStr +=       "ORDER BY C573WEB.WALF01D.WTYPE, C573WEB.WALF01D.WREVFL DESC";
			  		            
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(qryStr);
	
			return (collectMainDetails(rs, userUCPA));
		}
		
		catch (Exception ex) {
			UCPA.throwExMsg("InstDetails", "doMainQuery", ex);
			return false;
		}
	
		// close all resources
		finally {
			DbUtil.close(conn, stmt, rs);
		}
	
	}

	public void setCurrentOptions(UCPA userUCPA, HttpServletRequest request)
	throws Exception {
	
		SearchOptions currentOptions = userUCPA.getCurrentSrchOpts();
		
		currentOptions.setFormLink("inst");
		currentOptions.setDocTypeI(request.getParameter("dt"));
		currentOptions.setYear(request.getParameter("yr"));
		currentOptions.setPrefix(request.getParameter("ip").toUpperCase());
		currentOptions.setMiddle(request.getParameter("im"));
		currentOptions.setSuffix(request.getParameter("is").toUpperCase());
	
		try {
			currentOptions.save(userUCPA, request.getRemoteAddr());
		}
		catch (Exception ex) {
			UCPA.throwExMsg("InstDetails", "setCurrentOptions", ex);
		}
	
		return;
	}
}
