package org.ucnj.paccess.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;
import org.ucnj.paccess.maintenance.System;

public class DocIndex extends UCServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		// default is system down
		String urlPath = "/SysDown.jsp";
		
		if (isSystemUp()) {			
			// get the top level object, which is held in the session
			UCPA userUCPA = null;
			try {
				userUCPA = createOrRetrieveUserUCPA(request);
			}
			catch (Exception ex) {
				UCPA.throwExMsg("DocIndex", "performTask", ex);
			}
			
			// main switch on request; if missing then assume main JSP
			if (request.getParameter("s") == null) {
				urlPath = "/DocIndex.jsp";
			}
			else {
				String form = request.getParameter("s");
				
				// switch to login, which has its own switch
				if (form.equals("login")) {
					
					// default if no or wrong switch provided
					urlPath = "/Login?s=in";
					
					if (request.getParameter("ls") != null) {
						String loginSwitch = request.getParameter("ls").toLowerCase();
						if (loginSwitch.equals("in"))			// logging in
							urlPath = "/Login?s=in";
						else if (loginSwitch.equals("out"))	// logging out
							urlPath = "/Login?s=out";
						else if (loginSwitch.equals("send"))	// send lost login or password
							urlPath = "/SendLogin";
					}
				}
				
				// all other pages to be displayed
				else if (form.equalsIgnoreCase("name")) 
					urlPath = "/NameForm.jsp";
				else if (form.equalsIgnoreCase("date")) 
					urlPath = "/DateForm.jsp";
				else if (form.equalsIgnoreCase("book")) 
					urlPath = "/BookForm.jsp";
				else if (form.equalsIgnoreCase("inst")) 
					urlPath = "/InstForm.jsp";
				else if (form.equalsIgnoreCase("glossary")) 
					urlPath = "/Glossary.jsp";
				else if (form.equalsIgnoreCase("terms")) 
					urlPath = "/Terms.jsp";
				else if (form.equalsIgnoreCase("comments")) 
					urlPath = "/Comments.jsp";
				else if (form.equalsIgnoreCase("privacy")) 
					urlPath = "/Privacy.jsp";
				else if (form.equalsIgnoreCase("news")) 
					urlPath = "/News.jsp";
				else if (form.equalsIgnoreCase("join")) 
					urlPath = "/Join.jsp";
				else if (form.equalsIgnoreCase("help"))
					urlPath = "/HelpSearch.jsp";
				else if (form.equalsIgnoreCase("copies")) 
					urlPath = "/HelpCopies.jsp";
				else if (form.equalsIgnoreCase("pw")) 
					urlPath = "/PwMgr";
				else if (form.equalsIgnoreCase("req")) 
					urlPath = "/RequestList.jsp";
				else if (form.equalsIgnoreCase("cov")) 
					urlPath = "/CoverSheet.jsp";
			}
				
			request.setAttribute("userUCPArq", userUCPA);
		}
		
		// go to next page
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(urlPath);
		dispatcher.forward(request, response);	
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
	
			// pick up current index date for home page
			String currIndexDate = "";
			try {
				currIndexDate = getCurrentIndexDate(userUCPA);
			}
			catch (Exception ex) {
				UCPA.throwExMsg("DocIndex", "createOrRetrieveUserUCPA", ex);
			}
	
			userUCPA.setCurrentIndexDate(currIndexDate);
			
			session.setAttribute("userUCPA", userUCPA);
		}
	
		return userUCPA;
		
	}

	/**
	 * JDBC query to pick up the latest document index date
	 * Creation date: (6/28/2001 6:00:47 PM)
	 *
	 * @return String
	 * @param top level Object that contains connection pool info 
	 * @exception java.lang.Exception The exception description.
	 */
	private String getCurrentIndexDate(UCPA userUCPA)
	throws Exception
	{
		String currentIndexDate = UCPA.todaysDate();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			String qryStr = "SELECT C573WEB.WAPF00.CDATA " +
		  		            "FROM C573WEB.WAPF00 " +
		  		            "WHERE C573WEB.WAPF00.CRECTP = 'CURRENTINDEXDATE'";
				  		            
			conn = DbUtil.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(qryStr);
	
			// if a record returned then return the date;
			// else use today's date
			if (rs.next() == true) {
				currentIndexDate = rs.getString("CDATA").trim();
			}
		}
	
		catch (Exception ex) {
			UCPA.throwExMsg("DocIndex", "getCurrentIndexDate", ex);
			return null;
		}
	
		// release all resources
		finally {
			DbUtil.close(conn, stmt, rs); 
		}
		return currentIndexDate;
//return "20180418";
	}
	
	private boolean isSystemUp() {
		boolean result = false;
		
		ServletContext sc = getServletContext();
		System sys = (System)sc.getAttribute("System");
		
		if (sys != null) {
			if (sys.getStatus() == System.STATUS_UP) {
				result = true;
			}
		}
		else {
			String etcRealPath = sc.getRealPath(sc.getInitParameter("etcVirtualPath"));
			String sysFile = etcRealPath + "/" + sc.getInitParameter("sysFileName");
			ObjectInputStream in = null;
			try {
				File f = new File(etcRealPath);
				if (f.exists()) {
					f = new File(sysFile);
					if (f.exists()) {
						in = new ObjectInputStream(new FileInputStream(f));
						sys = (System)in.readObject();
						if (sys.getStatus() == System.STATUS_UP) {
							result = true;
							sc.setAttribute("System", sys);
						}
					}
				}
			}
			catch (Throwable t) {
				// print and ignore
				t.printStackTrace();
			}
			finally {
				if (in != null) {
					try {
						in.close();
					}
					catch (Throwable t) {
						// ignored on purpose
					}
				}
			}
		}
		
		return result;
//return true;
	}
}
