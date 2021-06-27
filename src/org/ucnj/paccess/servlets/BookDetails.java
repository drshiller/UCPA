package org.ucnj.paccess.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.ucnj.paccess.QueryException;
import org.ucnj.paccess.SearchOptions;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public class BookDetails extends Details {

	private static final long serialVersionUID = 1L;

	public boolean doMainQuery(HttpServletRequest request,
						 	   UCPA userUCPA)
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		// doc type, book and page args must be present on request
		if ((request.getParameter("dt") == null) ||
			(request.getParameter("bk") == null) ||
			(request.getParameter("pg") == null)) {
				throw new QueryException("Missing values for details key.", "book");
		}
	
		try {
			// pick up key values from request
			// (hardcoded field widths should be pikced up from db)
			String docType = request.getParameter("dt");
			String book = UCPA.formatSpace(request.getParameter("bk"), 5, true);
			String page = UCPA.formatSpace(request.getParameter("pg"), 4, true);
	
			// query string starts here
			String qryStr = "SELECT C573WEB.WALF01C.*, C573WEB.WAPF11.* " +
		  		            "FROM C573WEB.WALF01C, C573WEB.WAPF11 " +
		  		            "WHERE (C573WEB.WALF01C.WDOCTP = C573WEB.WAPF11.TCODE)";
		  	
			// special case for UCC
			if (docType.equals(Integer.toString(UCPA.DOC_TYPE_UCC))) {
				qryStr +=   " AND (C573WEB.WALF01C.WDOCTP = " + UCPA.DOC_TYPE_UCC 
				               + " OR C573WEB.WALF01C.WDOCTP = " + UCPA.DOC_TYPE_UCC_INITIAL + ")";
			}
			else {
				qryStr +=   " AND (C573WEB.WALF01C.WDOCTP = " + docType + ")";
			}
			
			qryStr +=       " AND (C573WEB.WALF01C.WBOOK = '" + book + "')" +
				            " AND (C573WEB.WALF01C.WPAGE = '" + page + "') ";
		  		            
		  	// order such that Party 1 Names appear before Party 2 Names,
		  	// and the primary name appears first within each party group
		  	qryStr +=       "ORDER BY C573WEB.WALF01C.WTYPE, C573WEB.WALF01C.WREVFL DESC";
			  		            
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(qryStr);
	
			return (collectMainDetails(rs, userUCPA));
		}
		
		catch (Exception ex) {
			UCPA.throwExMsg("BookDetails", "doMainQuery", ex);
			return false;
		}
		
		// release all resources
		finally {
			DbUtil.close(conn, stmt, rs);
		}
	
	}

	public void setCurrentOptions(UCPA userUCPA, HttpServletRequest request)
	throws Exception {
	
		SearchOptions currentOptions = userUCPA.getCurrentSrchOpts();
		
		currentOptions.setFormLink("book");
		currentOptions.setDocTypeBP(request.getParameter("dt"));
		currentOptions.setBook(request.getParameter("bk"));
		currentOptions.setPage(request.getParameter("pg"));
	
		try {
			currentOptions.save(userUCPA, request.getRemoteAddr());
		}
		catch (Exception ex) {
			UCPA.throwExMsg("BookDetails", "setCurrentOptions", ex);
		}
		
		return;
	}
}
