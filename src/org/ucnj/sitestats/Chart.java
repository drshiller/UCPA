package org.ucnj.sitestats;

import java.io.IOException;
import java.io.PrintWriter;
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

public class Chart extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private final static int MIN_APPLET_WIDTH = 800;
	private final static int BAR_WIDTH = 25;
	private final static int BAR_SPACE = 10;
	private final static int GRID_X_POS = 55;
	
	private String chartPassword;
	private String fromString;
	private String toString;

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
	
	@SuppressWarnings("unchecked")
	private Vector collectHits(ResultSet rs, String accumulate)
	throws Exception {
	
		Vector<Hits> hitsTable = new Vector<Hits>();
		
		try {
	
			String lastDate = "";
			Hits hits = null;
	
			boolean moreRecords = rs.first();
			
			while (moreRecords == true) {
				
				String thisDate = rs.getString("LDATE").trim();
				if (accumulate.equalsIgnoreCase("Monthly") == true)
					thisDate = thisDate.substring(0, 6) + "00";
				if (thisDate.equals(lastDate) == false) {
					if (hits != null) hitsTable.addElement(hits);
					hits = new Hits(thisDate);
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
	
	/**
	 * This method is used to create or retrieve the main
	 * top level application object, which is passed via
	 * the session and request objects.
	 */
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
			@SuppressWarnings("unchecked")
			Vector hitsTable = collectHits(rs, accumulate);
			showHitsPage(response, hitsTable, accumulate);
			
		}
		catch (Exception ex) {
			UCPA.throwExMsg("Chart", "doQuery", ex);
		}
	
		// close all resources
		finally {
			DbUtil.close(conn, stmt, rs);
		}
	
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
	
		fromString = fromDate;
		toString = toDate;
		
		return outStr;
	}
	
	public void init() throws ServletException {
		ServletContext sc = getServletContext();
		chartPassword = sc.getInitParameter("chartPassword");
	}
	
	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		// get the top level object, which is held in the session
		UCPA userUCPA = null;
		try {
			userUCPA = createOrRetrieveUserUCPA(request);
		}
		catch (Exception ex) {
			UCPA.throwExMsg("Chart", "performTask", ex);
		}
	
		if (request.getParameter("pw") != null)
			if (request.getParameter("pw").equals(chartPassword) == true) {
				try	{
					// execute the query
					doQuery(request, response, userUCPA);
				}
				catch (Exception ex) {
					UCPA.throwExMsg("Chart", "performTask", ex);
				}
			}
	
		// add the top level object to the request, to ensure the JSPs only use the request attributes.
		request.setAttribute("userUCPArq", userUCPA);
	
	}
	
	@SuppressWarnings("unchecked")
	private void showHitsPage(HttpServletResponse response, Vector hitsTable, String accumulate) 
	throws Exception {
	
		try {
			
			// init the response content type:
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
	
			out.println("<HTML>");
			out.println("");
			out.println("<HEAD>");
			out.println("");
			out.println("<TITLE>Union County Public Access Site Statistics</TITLE>");
			out.println("");
			out.println("</HEAD>");
			out.println("");
			out.println("<BODY>");
			out.println("");
			
			int appWidth = (BAR_WIDTH + BAR_SPACE) * hitsTable.size() + GRID_X_POS + 15;
			if (appWidth < MIN_APPLET_WIDTH) appWidth = MIN_APPLET_WIDTH;
			out.println("<APPLET code=\"SIRscolumn.class\" archive=\"applets/scol.jar\" width=\"" + 
						new Integer(appWidth).toString() + "\" height=\"520\">");
			
			out.println("");
			out.println("<!-- Start Up Parameters -->");
			out.println(" <PARAM name=\"LOADINGMESSAGE\" value=\"Please wait while the chart is loaded.\">");
			out.println(" <PARAM name=\"STEXTCOLOR\" value=\"0,0,100\">");
			out.println(" <PARAM name=\"STARTUPCOLOR\" value=\"255,255,255\">");
			out.println("");
			out.println("<!-- Chart Switches -->");
			out.println(" <PARAM name=\"3D\" value=\"false\">");
			out.println(" <PARAM name=\"grid\" value=\"true\">");
			out.println(" <PARAM name=\"axis\" value=\"true\">");
			out.println(" <PARAM name=\"ylabels\" value=\"true\">");
			out.println(" <PARAM name=\"outline\" value=\"true\">");
			out.println("");
			out.println("<!-- Chart Characteristics -->");
			out.println(" <PARAM name=\"nCols\" value=\"" + new Integer(hitsTable.size()).toString() + "\">");
	
			int totVertical = 352;
			int nRows = 10;   // Default: daily; max is 30000
			int chartScale = 3000;
			if (accumulate.equalsIgnoreCase("Monthly") == true) {
				nRows = 10;   // monthly: max is 300,000
				chartScale = 30000;
			}
			int vSpace = totVertical / nRows;
			
			out.println(" <PARAM name=\"nRows\" value=\"" + nRows + "\">");
			out.println(" <PARAM name=\"vSpace\" value=\"" + vSpace + "\">");
			out.println(" <PARAM name=\"nSeries\" value=\"6\">");
			out.println(" <PARAM name=\"barwidth\" value=\"" + new Integer(BAR_WIDTH).toString() + "\">");
			out.println(" <PARAM name=\"gridxpos\" value=\"" + new Integer(GRID_X_POS).toString() + "\">");
			out.println(" <PARAM name=\"gridypos\" value=\"400\">");
			out.println(" <PARAM name=\"depth3D\" value=\"15\">");
			out.println(" <PARAM name=\"ndecplaces\" value=\"0\">");
			out.println(" <PARAM name=\"labelsOrientation\" value=\"1\">");
			out.println(" <PARAM name=\"labelsY\" value=\"415\">");
			out.println(" <PARAM name=\"chartScale\" value=\"" + chartScale + "\">");
			out.println(" <PARAM name=\"chartStartY\" value=\"0\">");
			out.println("");
			out.println("<!-- Additional font information -->");
			out.println(" <PARAM name=\"font14\" value=\"Arial,N,10\">  <!-- Y labels Font -->");
			out.println(" <PARAM name=\"font15\" value=\"Arial,N,10\">  <!-- X labels Font -->");
			out.println("");
			out.println("<!-- Additional color information -->");
			out.println(" <PARAM name=\"color14\" value=\"100,100,100\">     <!-- gridcolor         -->");
			out.println(" <PARAM name=\"color15\" value=\"0,0,0\">           <!-- axiscolor         -->");
			out.println(" <PARAM name=\"color16\" value=\"0,100,100\">       <!-- floorcolor        -->");
			out.println(" <PARAM name=\"color17\" value=\"0,0,0\">           <!-- baroutline color  -->");
			out.println(" <PARAM name=\"color18\" value=\"0,0,0\">           <!-- label color       -->");
			out.println(" <PARAM name=\"color19\" value=\"0,0,0\">           <!-- Y color           -->");
			out.println("");
			out.println("<!-- Titles - Main, x and y -->");
			out.println("<!-- <PARAM name=\"title\" value=\"text,xpos,ypos,font-type,font-style,font-size,Rcolor,Gcolor,Bcolor\"> -->");
			out.println(" <PARAM name=\"title\"  value=\"Searches by Time of Day (" + accumulate + "),60,20,Arial,B,18,0,0,0 \">");
	//		out.println(" <PARAM name=\"xtitle\" value=\"Dates,200,335,Arial,B,16,0,0,0\">");
			out.println(" <PARAM name=\"ytitle\" value=\"searches,5,317,Arial,B,16,0,0,0\">");
			out.println("");
			out.println("<!-- Image Data -->");
			out.println(" <PARAM name=\"image1\" value=\"grfx/legend.gif,10,430\">");
			out.println("");
			
			out.println("<!-- Other text -->");
			String dateRange = fromString + " to " + toString;
			out.println(" <PARAM name=\"text1\" value=\"" + dateRange + ",60,37,Arial,B,14,128,128,128\">");
			out.println("");
	
			out.println("<!-- Column Labels and Data -->");
			out.println("<!-- <PARAM name=\"columnNseriesN\" value=\"value,RED,GREEN,BLUE,URL,Target Frame\">");
			int lblCount = 0;
			Enumeration enumAll = hitsTable.elements();
			while (enumAll.hasMoreElements()) {
				lblCount++;
				Hits hits = (Hits)enumAll.nextElement();
				String date = "";
				if (accumulate.equalsIgnoreCase("Daily") == true)
					date = hits.date.substring(4, 6) + "/" + hits.date.substring(6, 8);
				else if (accumulate.equalsIgnoreCase("Monthly") == true)
					date = hits.date.substring(4, 6) + "/" + hits.date.substring(2, 4);
				out.println(" <PARAM name=\"label" + lblCount + "\" value=\"" + date + "\">");
				out.println(" <PARAM name=\"column" + lblCount + "series1\" value=\"" + hits.ts00to04 + ",255,255,0\">");
				out.println(" <PARAM name=\"column" + lblCount + "series2\" value=\"" + hits.ts04to08 + ",255,0,0\">");
				out.println(" <PARAM name=\"column" + lblCount + "series3\" value=\"" + hits.ts08to12 + ",0,0,255\">");
				out.println(" <PARAM name=\"column" + lblCount + "series4\" value=\"" + hits.ts12to16 + ",0,255,0\">");
				out.println(" <PARAM name=\"column" + lblCount + "series5\" value=\"" + hits.ts16to20 + ",0,255,255\">");
				out.println(" <PARAM name=\"column" + lblCount + "series6\" value=\"" + hits.ts20to00 + ",255,0,255\">");
			}
			out.println("");
	
			out.println("</APPLET>");
			out.println("");
			
			out.println("</BODY>");
			out.println("");
			out.println("</HTML>");
		}
	
		catch(Exception ex) {
			UCPA.throwExMsg("Chart", "showPage", ex);
		}
			
	}
}
