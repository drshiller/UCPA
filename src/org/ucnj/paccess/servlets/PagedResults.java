package org.ucnj.paccess.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.QueryException;
import org.ucnj.paccess.Result;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public abstract class PagedResults extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected String formatPartyOpt(String keyView, String partyOpt) {
	
		// there are actually 3 opts including
		// 1 = grantor, 2 = grantee and 3 = both;
		// if 1 or 2 not specified then no option is formatted
		// as this will cause both to be selected
		String outStr = "";
	
		if (partyOpt != null) {
			if (partyOpt.equals("1")) {
				outStr = " AND (C573WEB." + keyView + ".WTYPE = '1')";
			}
			else if (partyOpt.equals("2")) {
				outStr = " AND (C573WEB." + keyView + ".WTYPE = '2')";
			}
		}
	
		return outStr;
	}

	public boolean areDatesInOrder(String from, String to) {
		
		Calendar calFrom = Calendar.getInstance();
		calFrom.set(Calendar.DAY_OF_MONTH, new Integer(from.substring(3, 5)).intValue());
		calFrom.set(Calendar.MONTH, new Integer(from.substring(0, 2)).intValue());
		calFrom.set(Calendar.YEAR, new Integer(from.substring(6, 10)).intValue());
		
		Calendar calTo = Calendar.getInstance();
		calTo.set(Calendar.DAY_OF_MONTH, new Integer(to.substring(3, 5)).intValue());
		calTo.set(Calendar.MONTH, new Integer(to.substring(0, 2)).intValue());
		calTo.set(Calendar.YEAR, new Integer(to.substring(6, 10)).intValue());
	
		if (calFrom.before(calTo))
			return true;
		else
			return false;
	
	}

	private void collectResults(ResultSet rs, Integer pageNum, Integer resultsPerPage, UCPA userUCPA)
	throws Exception {
		
		try {
	
			// jump to first record on page
			int startingRec = ((pageNum.intValue() - 1) * resultsPerPage.intValue()) + 1;
			rs.last(); // guarentees that absolute won't fail
			boolean moreRecords = rs.absolute(startingRec);
	
			// load up the results vector if there is more to be done
			Vector<Result> results = new Vector<Result>();
			for (int rec = 0; rec < resultsPerPage.intValue() && moreRecords == true; rec++) {
			
				Result aResult = new Result();
				aResult.setDocType(new Integer(rs.getInt("WDOCTP")));
				aResult.setDocTypeDesc(rs.getString("TCDESC").trim());
				aResult.setLastName(rs.getString("WSURNM").trim());
				aResult.setFirstName(rs.getString("WGIVNM").trim());
				aResult.setStampDate(Result.transformDate(rs.getString("WSTPDT").trim(), false));
	
				aResult.setInstrPrefix(rs.getString("WINT#P").trim());
				aResult.setInstrMiddle(rs.getString("WINT#M").trim());
				aResult.setInstrSuffix(rs.getString("WINT#S").trim());
				
				aResult.setBook(rs.getString("WBOOK").trim());
				aResult.setPage(rs.getString("WPAGE").trim());
				aResult.setCorpType(rs.getString("WCTYPE").trim());
				aResult.setPartyType(rs.getString("WTYPE").trim());
				aResult.setParty1Name(rs.getString("TPT1NM").trim());
				
				aResult.setLastNameRev(rs.getString("RSURNM").trim());
				aResult.setFirstNameRev(rs.getString("RGIVNM").trim());
				aResult.setCorpTypeRev(rs.getString("RCTYPE").trim());
				aResult.setPartyTypeRev(rs.getString("RTYPE").trim());
				aResult.setParty2Name(rs.getString("TPT2NM").trim());
	
				// pick up image information;
				// WIMGID temporarily includes both the RVI image ID and
				// the annotation state (the latter should be in a separate field)
				if (rs.getString("WIMGID").trim().length() > 0) {
					aResult.setImageID(rs.getString("WIMGID").substring(0, 8));
					if (rs.getString("WIMGID").substring(9, 10).equalsIgnoreCase("Y") == true)
						aResult.setImageAnnotated(true);
				}
				aResult.setNumPages(new Integer(rs.getInt("WIMGPG")));
	
				results.addElement(aResult);
	
				moreRecords = rs.next();
			}
	
			// save results onto top level application object
			userUCPA.setResults(results);
			userUCPA.setPageNum(pageNum);
			userUCPA.setMore(new Boolean(moreRecords));
		}
	
		catch (Exception ex) {
			UCPA.throwExMsg("PagedResults", "collectResults", ex);
		}
	
		return;
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	
		try {
			performTask(request, response);
		}
		catch(Exception exp) {
			request.setAttribute("exception", exp);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
		}
	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	
		try { 
			performTask(request, response);
		}
		catch(Exception exp){
			request.setAttribute("exception", exp);
			RequestDispatcher dispatcher =  getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
		}
	
	}

	private void doQuery(HttpServletRequest request,
						 UCPA userUCPA,
						 Integer pageNum,
						 Integer resultsPerPage,
						 boolean firstTime) 
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try {
	
			// if this is the 1st time for this query then create and store it;
			// else pick up previously stored query string
			String qryStr = "";
			if (firstTime == true) {
				qryStr = formatQuery(request);
				userUCPA.setLastQuery(qryStr);
				setCurrentOptions(userUCPA, request, resultsPerPage);
			}
			else if ((qryStr = userUCPA.getLastQuery()) == null) {
				throw new QueryException("Missing Last Query.", "name");
			}
	
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(qryStr);
	
			// collect the page of results
			collectResults(rs, pageNum, resultsPerPage, userUCPA);
	
			return;
		}
	
		catch (QueryException ex) {
			throw new QueryException(ex.getMessage(), ex.getCallingForm());
		}
		catch (Exception ex) {
			UCPA.throwExMsg("PagedResults", "doQuery", ex);
		}
	
		// close the JDBC resources
		finally {
			DbUtil.close(conn, stmt, rs);
		}
	
	}

	protected String formatDocTypeOpt(String keyView, String[] docTypes) {
	
		String outStr = "";
		String sLisPendens = Integer.toString(UCPA.DOC_TYPE_LIS_PENDENS);
		String sUCC = Integer.toString(UCPA.DOC_TYPE_UCC);
		String sDocType = "C573WEB." + keyView + ".WDOCTP = ";
		
		if (docTypes != null) {
			
			outStr += " AND (";
	
			// work thru the array of doc type options
			for (int i = 0; i < docTypes.length; i++) {
	
				// if multiple opts then add OR condition
				if (i > 0) {
					outStr += " OR ";
				}
	
				// now add the condition
				outStr += (sDocType + docTypes[i]);
				
				// special cases
				if (docTypes[i].equals(sLisPendens)) {
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_LIS_PENDENS_DISCHARGE);
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_LIS_PENDENS_IN_REM);
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_LIS_PENDENS_IN_REM_DISMISSED);
				}
				else if (docTypes[i].equals(sUCC)) {
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_UCC_INITIAL);
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_UCC_AMENDMENT);
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_UCC_ASSIGNMENT);
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_UCC_CONTINUATION);
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_UCC_TERMINATION);
					outStr += (" OR " + sDocType + UCPA.DOC_TYPE_UCC_RELEASE);
				}
			}
			
			outStr += ")";
			
		}
		
		return outStr;
	}

	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		String nextURL = UCPA.REDIRECT_PAGE;
		
		// pick up the top level application object from the session object
		HttpSession session = request.getSession(false);
		if (session != null) {
		
			UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");
			
			// if no page # in URL so it must be the 1st time;
			// else page # is in URL so must be the next requested page to display
			Integer pageNum;
			boolean firstTime;
			if (request.getParameter("page") == null) {
				pageNum = new Integer(1);
				firstTime = true;
			}
			else {
				pageNum = new Integer(request.getParameter("page"));
				firstTime = false;
			}
	
			// pick up results per page setting;
			// if it not numeric or negative then use default;
			// if not present then use default
			Integer resultsPerPage = null;
			if (request.getParameter("rpp") != null) {
				
				String rppStr = request.getParameter("rpp").trim();
				int rppInt;
				try {
					rppInt = Integer.parseInt(rppStr);
				}
				catch(NumberFormatException ex) {
					rppInt = UCPA.getDefaultResultsPerPage().intValue();
				}
	
				resultsPerPage = new Integer(rppInt);
				if (resultsPerPage.intValue() <= 0) {
					resultsPerPage = UCPA.getDefaultResultsPerPage();
				}
				if (resultsPerPage.intValue() > UCPA.getMaxResultsPerPage().intValue()) {
					resultsPerPage = UCPA.getMaxResultsPerPage();
				}
			}
			else {
				resultsPerPage = UCPA.getDefaultResultsPerPage();
			}
				
			// execute the query
			try {
				doQuery(request, userUCPA, pageNum, resultsPerPage, firstTime);
				nextURL = "/PagedResults.jsp";
			}
	
			// if query exception then the user tried to jump to this servlet
			// without going thru input form that supplies args;
			// redirect the user back to associated form
			catch (QueryException ex) {
				nextURL = "/DocIndex?s=" + ex.getCallingForm();
			}
			catch (Exception ex) {
				UCPA.throwExMsg("PagedResults", "performTask", ex);
			}
	
			// store data required by JSP that will display results
			request.setAttribute("userUCPArq", userUCPA);
		}
	
		// hand onto results JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	
	}

	protected abstract String formatQuery(HttpServletRequest request)
	throws Exception;

	protected abstract void setCurrentOptions(
		UCPA userUCPA, HttpServletRequest request, Integer resultsPerPage)
	throws Exception;

}
