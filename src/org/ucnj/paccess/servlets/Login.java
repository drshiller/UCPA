package org.ucnj.paccess.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.AcctMember;
import org.ucnj.paccess.LoggedIn;
import org.ucnj.paccess.LoginStatus;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public class Login extends UCServlet {

	private static final long serialVersionUID = 1L;
	
	// login results
	public final static int RESULT_NONE       = 0,
							RESULT_OK_IN      = 1,
							RESULT_OK_OUT	  = 2,
							RESULT_BAD_ARGS   = 3,
							RESULT_BAD_NAME   = 4,
							RESULT_BAD_PW     = 5,
							RESULT_NOT_ACTIVE = 6;

	private AcctMember find(HttpServletRequest request,
							UCPA userUCPA)
	throws Exception {
	
		AcctMember acctMember = null;
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
	
			// query string starts here
			String qryStr = "SELECT C573WEB.RGPF03.* " +
		  		            "FROM C573WEB.RGPF03 " +
		  					"WHERE C573WEB.RGPF03.MNAME = '" + request.getParameter("name").toUpperCase() + "'";
				  		            
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(qryStr);
		
			boolean recFound = rs.first();
			if (recFound == true) {
				acctMember = new AcctMember(rs.getInt("MACCT"), 
											rs.getString("MNAME"), 
											rs.getString("MPSWD"), 
											rs.getString("MEMAIL"));
				if (rs.getString("MACTIVE").equalsIgnoreCase("T"))
					acctMember.setActive(true);
				else
					acctMember.setActive(false);
			}
			
		}
		catch (Exception ex) {
			UCPA.throwExMsg("SendLogin", "find", ex);
		}
	
		// close all resources
		finally {
			DbUtil.close(conn, stmt, rs);
		}
	
		return acctMember;
	
	}
	
	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		String nextURL = UCPA.REDIRECT_PAGE;
		
		// pick up the top level application object from the session object
		HttpSession session = request.getSession(false);
		if (session != null) {
		
			UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");
			
			// get hashmap of logged in users held in servlet context
			ServletContext sc = getServletContext();
			LoggedIn loggedIn = (LoggedIn)sc.getAttribute("LoggedIn");
	
			// clear login state used by JSP
			LoginStatus currStatus = userUCPA.getCurrentLoginStatus();
			currStatus.setLoginName("");
			currStatus.setLoginResult(Login.RESULT_NONE);
	
			// check request for login or logout;
			// if missing or not out then assume in
			String inOrOut = "in"; 
			if (request.getParameter("s") != null) {
				if (request.getParameter("s").equalsIgnoreCase("out"))
					inOrOut = "out";
			}
	
			// logging in
			if (inOrOut.equals("in")) {
	
				// Are the name and passwords present?
				// If so then start verifying them; otherwise this must be the
				// initial call to display the login page
				if ((request.getParameter("name") != null) && (request.getParameter("pw") != null)) {
	
					// assume missing args until proven otherwise
					currStatus.setLoginResult(Login.RESULT_BAD_ARGS);
					currStatus.setLoginName(request.getParameter("name"));
					if ((request.getParameter("name").length() > 0) && (request.getParameter("pw").length() > 0)) {
	
						// assume bad name until proven otherwise
						currStatus.setLoginResult(Login.RESULT_BAD_NAME);
						AcctMember acctMember = find(request, userUCPA);
						if (acctMember != null) {
							
							// assume not active until proven otherwise
							currStatus.setLoginResult(Login.RESULT_NOT_ACTIVE);
							if (acctMember.isActive() == true) {
								
								// assume bad password until proven otherwise
								currStatus.setLoginResult(Login.RESULT_BAD_PW);
								if (acctMember.getPassword().trim().equalsIgnoreCase(request.getParameter("pw").trim())) {
	
									// member may be trying to login again from this or another browser session
									// so kill that other login automatically (this does nothing if not
									// already logged in)
									loggedIn.remove(acctMember);
	
									// add this member to the application and session contexts 
									loggedIn.add(session.getId(), acctMember);
									currStatus.setLoggedIn(true);
	
									// Success!
									currStatus.setLoginResult(Login.RESULT_OK_IN);
								} // password check
							} // active check
						} // name check
					} // arg check
				} // verification check
	
			} // logging in
	
			// logging out so remove member
			else {
				loggedIn.remove(session.getId());
				currStatus.setLoggedIn(false);
				currStatus.setLoginResult(Login.RESULT_OK_OUT);
			}
	
			// store data required by JSP that will display results	
			request.setAttribute("userUCPArq", userUCPA);
	
			nextURL = "/Login.jsp";
		}
			
		// hand onto next JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}
}
