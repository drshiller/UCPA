package org.ucnj.paccess.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.AcctMember;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public class PwMgr extends UCServlet {

	private static final long serialVersionUID = 1L;

	// pw change results
	public final static int RESULT_NONE           = 0,
							RESULT_BAD_NAME_OR_PW = 1,
							RESULT_BAD_PW         = 2,
							RESULT_BAD_CONFIRM    = 3,
							RESULT_OK			  = 4;

	public final static int MIN_LENGTH = 4;
	public final static int MAX_LENGTH = 10;

	private AcctMember find(String name,
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
		  					"WHERE C573WEB.RGPF03.MNAME = '" + name.toUpperCase() + "'";
				  		            
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
	
			// If either the name or old password name/value pair is missing
			// then display initial form to be completed
			userUCPA.setPwMgrResult(RESULT_NONE);
			String name = request.getParameter("name");
			String oldPW = request.getParameter("old");
			if ((name != null) && (oldPW != null)) {
	
				// assume empty name and password args until proven otherwise
				userUCPA.setPwMgrResult(RESULT_BAD_NAME_OR_PW);
				name = name.trim();
				userUCPA.setPwMgrName(name);
				oldPW = oldPW.trim();
				if ((name.length() > 0) && (oldPW.length() > 0)) {
	
					// assume bad name until proven otherwise
					AcctMember acctMember = find(name, userUCPA);
					if (acctMember != null) {
								
						// assume bad old password until proven otherwise
						if (acctMember.getPassword().trim().equalsIgnoreCase(oldPW)) {
	
							// assume bad new password until proven otherwise
							userUCPA.setPwMgrResult(RESULT_BAD_PW);
							String newPW = request.getParameter("new");
							if (newPW != null) {
								newPW = newPW.trim();
								if ((newPW.length() >= MIN_LENGTH) && (newPW.length() <= MAX_LENGTH)) {
	
									// assume bad confirmation until proven otherwise
									userUCPA.setPwMgrResult(RESULT_BAD_CONFIRM);
									String confirmPW = request.getParameter("con");
									if (confirmPW != null) {
										confirmPW = confirmPW.trim();
										if (newPW.equalsIgnoreCase(confirmPW)) {
	
											save(userUCPA, name, newPW);
											userUCPA.setPwMgrName("");
											userUCPA.setPwMgrResult(RESULT_OK);
	
										} // new = confirm password check
									} // confirm password missing arg check
								} // new password length check
							} // new password missing arg check
						} // password check
					} // name check
				} // name and password empty arg check
			} // Empty query string check
	
			// store data required by JSP that will display results	
			request.setAttribute("userUCPArq", userUCPA);
	
			nextURL = "/PwMgr.jsp";
		}
			
		// hand onto next JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}
	
	private void save(UCPA userUCPA, String name, String newPW)
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		String qryStr = "";
		
		try {
			
			// query string starts here
			qryStr = "UPDATE C573WEB.RGPF03 " +
		  		     "SET MPSWD = '" + UCPA.formatSpace(newPW.toUpperCase(), MAX_LENGTH, false) + "' " +
		  		     "WHERE C573WEB.RGPF03.MNAME = '" + name.toUpperCase() + "'";
		  		            
			// execute the query
			conn = DbUtil.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(qryStr);
		  		            
		}
		catch (Exception ex) {
			UCPA.throwExMsg("PwMgr", "save", ex);
		}
	
		// close all resources
		finally {
			DbUtil.close(conn, stmt, null);
		}
	
	}
}
