package org.ucnj.sitestats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.Result;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public class Chart1 extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private String chartPassword;
	
	public void init() throws ServletException {
		ServletContext sc = getServletContext();
		chartPassword = sc.getInitParameter("chartPassword");
	}
	
	private UCPA createOrRetrieveUserUCPA(HttpServletRequest request)
	throws Exception {
		
		// Get the current session object, create one if necessary
		HttpSession session = request.getSession(true);
		
		// Try to retrieve the top level object from the session.
		UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");
	
		// not there so create it and put on session
		if (userUCPA == null) {
			userUCPA = new UCPA();
			session.setAttribute("userUCPA", userUCPA);
		}
		
		return userUCPA;
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
	
	private void performTask(HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
		
		// get the top level object, which is held in the session
		UCPA userUCPA = null;
		try {
			userUCPA = createOrRetrieveUserUCPA(request);
		}
		catch (Exception ex) {
			UCPA.throwExMsg("Chart", "performTask", ex);
		}
		
		if (request.getParameter("pw") != null) {
			if (request.getParameter("pw").equals(chartPassword) == true) {
				try	{
					// execute the query
					doQuery(request, response, userUCPA);
				}
				catch (Exception ex) {
					UCPA.throwExMsg("Chart", "performTask", ex);
				}
			}
		}

	}
	
	private void doQuery(HttpServletRequest request,
						 HttpServletResponse response,
						 UCPA userUCPA)
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
	
			// query string starts here
			String qryStr = "SELECT C573WEB.WAPF80.LDATE, C573WEB.WAPF80.LTIME " +
		  		            "FROM C573WEB.WAPF80 ";
	
			// date range condition
			qryStr +=       "WHERE " + formatDateOpt(request.getParameter("from"), request.getParameter("to")) + " ";
	
			qryStr +=       "ORDER BY C573WEB.WAPF80.LDATE";
	
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(qryStr);
			
			String accumulate = request.getParameter("acc");
			if (accumulate != null) {
				if (accumulate.equalsIgnoreCase("d") == true)
					accumulate = "Daily";
				else if (accumulate.equalsIgnoreCase("m") == true)
					accumulate = "Monthly";
				else
					accumulate = "Daily";
			}
			else
				accumulate ="Daily";
			@SuppressWarnings("rawtypes")
			Vector hitsTable = collectHits(rs, accumulate);
			showHitsPage(request, response, hitsTable, accumulate);
			
		}
		catch (Exception ex) {
			UCPA.throwExMsg("Chart", "doQuery", ex);
		}
	
		// close all resources
		finally {
			DbUtil.close(conn, stmt, rs);
		}
	}

	private boolean areDatesInOrder(String from, String to) {
		
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
	
	@SuppressWarnings("rawtypes")
	private Vector collectHits(ResultSet rs, String accumulate)
	throws Exception {
	
		Vector<Hits> hitsTable = new Vector<Hits>();
		
		try {
	
			String lastDate = "";
			Hits hits = null;
	
			boolean moreRecords = rs.first();
			
			while (moreRecords == true) {
				
				String thisDate = rs.getString("LDATE").trim();
				if (accumulate.equalsIgnoreCase("Monthly") == true) {
					thisDate = thisDate.substring(0, 6) + "00";
				}
				if (thisDate.equals(lastDate) == false) {
					if (hits != null) {
						hitsTable.addElement(hits);
					}
					String sDate = thisDate.substring(4, 6);
					if (!accumulate.equalsIgnoreCase("Monthly") == true) {
						sDate += "/" + thisDate.substring(6); 
					}
					sDate += "/" + thisDate.substring(0, 4);
					hits = new Hits(sDate);
				}
	
				int time = new Integer(rs.getString("LTIME").trim()).intValue();
				if (time >= 0 && time <= 40000)           hits.ts00to04++;
				else if (time > 40000 && time <= 80000)   hits.ts04to08++;
				else if (time > 80000 && time <= 120000)  hits.ts08to12++;
				else if (time > 120000 && time <= 160000) hits.ts12to16++;
				else if (time > 160000 && time <= 200000) hits.ts16to20++;
				else if (time > 200000 && time <= 235959) hits.ts20to00++;
	
				lastDate = thisDate;
	
				moreRecords = rs.next();
			}
			if (hits != null) hitsTable.addElement(hits);
	
		}
		catch (Exception ex) {
			UCPA.throwExMsg("Chart", "collectHits", ex);
		}
	
		return hitsTable;
	}
	
	private String formatDateOpt(String fromDate, String toDate) {
	
					
		String outStr = "(C573WEB.WAPF80.LDATE BETWEEN '";
	
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
	
	
	@SuppressWarnings("rawtypes")
	private void showHitsPage(HttpServletRequest request, 
		HttpServletResponse response, Vector hitList, String accumulate) 
	throws Exception 
	{
        String dataRealPath = request.getSession().getServletContext().getRealPath("/data/data.csv");
		File f = new File(dataRealPath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write("Date,00.00 to 04:00,04:00 to 08:00,08:00 to 12:00,12:00 to 16:00,16:00 to 20:00,20:00 to 00:00");
		Enumeration enumAll = hitList.elements();
		while (enumAll.hasMoreElements()) {
			Hits hits = (Hits)enumAll.nextElement();
	        bw.newLine();
			bw.write(hits.date + "," 
				+ hits.ts00to04 + "," 
				+ hits.ts04to08 + "," 
				+ hits.ts08to12 + "," 
				+ hits.ts12to16 + "," 
				+ hits.ts16to20 + "," 
				+ hits.ts20to00);
		}
        bw.close();

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Stacked.html");
		dispatcher.forward(request, response);
	}
}
