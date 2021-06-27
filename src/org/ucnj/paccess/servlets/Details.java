package org.ucnj.paccess.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.MiscDetail;
import org.ucnj.paccess.PartyName;
import org.ucnj.paccess.QueryException;
import org.ucnj.paccess.Result;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public abstract class Details extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected final boolean collectMainDetails(ResultSet rs, UCPA userUCPA)
	throws Exception {
	
	
		try {
			boolean moreRecords = rs.first();
			if (moreRecords == false)
				return false;
			
			// main details
			Result result = new Result();
			result.setDocType(new Integer(rs.getInt("WDOCTP")));
			result.setDocTypeDesc(rs.getString("TCDESC").trim());
			result.setLastName(rs.getString("WSURNM").trim());
			result.setFirstName(rs.getString("WGIVNM").trim());
			result.setStampDate(Result.transformDate(rs.getString("WSTPDT").trim(), false));
	
			result.setInstrPrefix(rs.getString("WINT#P").trim());
			result.setInstrMiddle(rs.getString("WINT#M").trim());
			result.setInstrSuffix(rs.getString("WINT#S").trim());
			
			result.setBook(rs.getString("WBOOK").trim());
			result.setPage(rs.getString("WPAGE").trim());
			result.setCorpType(rs.getString("WCTYPE").trim());
			result.setPartyType(rs.getString("WTYPE").trim());
			result.setParty1Name(rs.getString("TPT1NM").trim());
			result.setParty2Name(rs.getString("TPT2NM").trim());
	
			// pick up image information;
			// WIMGID temporarily includes both the RVI image ID and
			// the annotation state (the latter should be in a separate field)
			if (rs.getString("WIMGID").trim().length() > 0) {
				result.setImageID(rs.getString("WIMGID").substring(0, 8));
				if (rs.getString("WIMGID").substring(9, 10).equalsIgnoreCase("Y") == true)
					result.setImageAnnotated(true);
			}
			result.setNumPages(new Integer(rs.getInt("WIMGPG")));
	
			// party names
			Vector<PartyName> party1Names = new Vector<PartyName>();
			Vector<PartyName> party2Names = new Vector<PartyName>();
			
			while (moreRecords == true) {
				
				PartyName aName = new PartyName();
				aName.setLastName(rs.getString("WSURNM").trim());
				aName.setFirstName(rs.getString("WGIVNM").trim());
				aName.setCorpType(rs.getString("WCTYPE").trim());
	
				if (rs.getString("WTYPE").equals("1")) {
					party1Names.addElement(aName);
				}
				else {
					party2Names.addElement(aName);
				}
	
				moreRecords = rs.next();
			}
	
			// save results onto main application object
			userUCPA.setMainResult(result);
			userUCPA.setParty1(party1Names);
			userUCPA.setParty2(party2Names);
	
			return true;
		}
		catch(Exception ex) {
			UCPA.throwExMsg("Details", "collectMainDetails", ex);
			return false;
		}
	
	}
	
	private final void collectMiscDetails(ResultSet rs, UCPA userUCPA)
	throws Exception {
		
		Vector<MiscDetail> miscDetails = new Vector<MiscDetail>();
	
		try {
			boolean moreRecords = rs.first();
			while (moreRecords == true) {
				MiscDetail aDetail = new MiscDetail();
				aDetail.setSeqNumber(new Integer(rs.getString("MSEQ").trim()).intValue());
				aDetail.setDescription(rs.getString("MDESC").trim());
				aDetail.setData(rs.getString("MDATA").trim());
				aDetail.setColumn(new Integer(rs.getString("MCOL").trim()).intValue());
				miscDetails.addElement(aDetail);
				
				moreRecords = rs.next();
			}
	
		}	
		catch(Exception ex) {
			UCPA.throwExMsg("Details", "collectMiscDetails", ex);
			return;
		}
	
		userUCPA.setMiscDetails(miscDetails);
	
	}
	
	public final void doGet(HttpServletRequest request, HttpServletResponse response)
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
	
	public abstract boolean doMainQuery(HttpServletRequest request,
									    UCPA userUCPA)
	throws Exception;
	
	public void doMiscQuery(UCPA userUCPA)
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try {
			// this query uses params found in main result record
			Result mainResult = userUCPA.getMainResult();
	
			// format prefix and suffix; if not present then use default blank (field width = 2)
			String iPrefix = " ";
			if (mainResult.getInstrPrefix().length() > 0 )
				iPrefix = UCPA.formatSpace(mainResult.getInstrPrefix(), 2, false);
			String iSuffix = " ";
			if (mainResult.getInstrSuffix().length() > 0 )
				iSuffix = UCPA.formatSpace(mainResult.getInstrSuffix(), 2, false);
	
			// query string starts here
			String qryStr = "SELECT C573WEB.WAPF03.MSEQ, C573WEB.WAPF10.MDESC, C573WEB.WAPF03.MDATA, C573WEB.WAPF03.MCOL, C573WEB.WAPF03.MTYPE " + 
		  		            "FROM C573WEB.WAPF03, C573WEB.WAPF10 " +
		  		            "WHERE (C573WEB.WAPF03.MTYPE = C573WEB.WAPF10.MTYPE)";
		  	
		  	// key values	             
			qryStr +=       " AND (C573WEB.WAPF03.MDOCTP = " + mainResult.getDocType().toString() + ")" +
		  		            " AND (C573WEB.WAPF03.MSTPDT = '" + Result.transformDate(mainResult.getStampDate(), true) + "')" +
		  		            " AND (C573WEB.WAPF03.MINT#M = '" + UCPA.formatSpace(mainResult.getInstrMiddle(), 6, true) + "')" +
							" AND (C573WEB.WAPF03.MINT#P = '" + iPrefix + "')" +
							" AND (C573WEB.WAPF03.MINT#S = '" + iSuffix + "') ";
		  		            
		  	qryStr +=       "ORDER BY C573WEB.WAPF03.MTYPE, C573WEB.WAPF03.MSEQ";
		  	
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(qryStr);
	
			collectMiscDetails(rs, userUCPA);
			
		}
	
		catch (Exception ex) {
			UCPA.throwExMsg("Details", "doMiscQuery", ex);
			return;
		}
	
		// release all resources
		finally {
			DbUtil.close(conn, stmt, rs);
		}
	
	}
	
	public final void doPost(HttpServletRequest request, HttpServletResponse response)
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
	
	public final void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		String nextURL = UCPA.REDIRECT_PAGE;
		
		// pick up the top level application object from the session object
		HttpSession session = request.getSession(false);
		if (session != null) {
		
			UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");
	
			try	{
				// execute the main query; if a result is found then
				// collect associated miscellaneous details
				if (doMainQuery(request, userUCPA) == true) {
	
					doMiscQuery(userUCPA);
	
					userUCPA.setFound(new Boolean(true));
				}
				else {
					userUCPA.setFound(new Boolean(false));
				}
				
				// store data required by JSP that will display results	
				setCurrentOptions(userUCPA, request);
				
				nextURL = "/Details.jsp";
			}
	
			// if query exception then the user tried to jump to this servlet
			// without going thru input form that supplies args;
			// redirect the user back to associated form
			catch (QueryException ex) {
				nextURL = "/DocIndex?s=" + ex.getCallingForm();
			}
			catch (Exception ex) {
				UCPA.throwExMsg("Details", "performTask", ex);
				return;
			}
	
			// store data required by JSP that will display results
			request.setAttribute("userUCPArq", userUCPA);
		}
			
		// hand onto results JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}
	
	public abstract void setCurrentOptions(UCPA userUCPA, HttpServletRequest request)
	throws Exception;
}
