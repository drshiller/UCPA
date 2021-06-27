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

import org.ucnj.paccess.BoardDate;
import org.ucnj.paccess.Result;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public class BoardDates extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private void collectDates(ResultSet rs, UCPA userUCPA)
	throws Exception {
		
		try {
	
			boolean moreRecords = rs.first();
			
			Vector<BoardDate> dates = new Vector<BoardDate>();
			while (moreRecords == true) {
				BoardDate aDate = new BoardDate();
				aDate.setDocType(rs.getString("TCDESC").trim());
				aDate.setBoardDate(Result.transformDate(rs.getString("TBDATE").trim(), false));
				aDate.setStartDate(Result.transformDate(rs.getString("TSDATE").trim(), false));
	
				dates.addElement(aDate);
	
				moreRecords = rs.next();
			}
	
			// save results onto main session object
			userUCPA.setDates(dates);
	
		}
		catch (Exception ex) {
			UCPA.throwExMsg("BoardDates", "collectDates", ex);
		}
		
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

	private void doQuery(UCPA userUCPA)
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
	
			// query string starts here
			String qryStr = "SELECT C573WEB.WAPF11.* " +
		  		            "FROM C573WEB.WAPF11 " +
		  		            "ORDER BY C573WEB.WAPF11.TCODE";
				  		            
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(qryStr);
		
			// copy results from set to a collection of date objects	
			collectDates(rs, userUCPA);
			
		}
		catch (Exception ex) {
			UCPA.throwExMsg("BoardDates", "doQuery", ex);
		}
	
		// close all resources
		finally {
			DbUtil.close(conn, stmt, rs);
		}
	
	}

	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		String nextURL = UCPA.REDIRECT_PAGE;
	
		// Get the user's session object.
		HttpSession session = request.getSession(false);
		if (session != null) {
		
			UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");
	
			try	{
				// execute the query
				doQuery(userUCPA);
			}
			catch (Exception ex) {
				UCPA.throwExMsg("BoardDates", "performTask", ex);
			}
			
			// store data required by JSP that will display results	
			request.setAttribute("userUCPArq", userUCPA);
	
			nextURL = "/BoardDates.jsp";
		}
	
		// hand onto results next URL
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}
}
