package org.ucnj.paccess.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.AcctMember;
import org.ucnj.paccess.LoginStatus;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public class SendLogin extends UCServlet {

	private static final long serialVersionUID = 1L;

	// login results
	public final static int RESULT_NONE      = 0,
							RESULT_SENT_NAME = 1,
							RESULT_BAD_ADD   = 2,
							RESULT_SENT_PW   = 3,
							RESULT_BAD_NAME  = 4;
	
	private static String subject = "A Message from the Union County Clerk's Office";
		
    private String host = "";
    private String from = "";

	private AcctMember find(String type,
							HttpServletRequest request,
							UCPA userUCPA)
	throws Exception {
	
		AcctMember acctMember = null;
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
	
			// query string starts here
			String qryStr = "SELECT C573WEB.RGPF03.* " +
		  		            "FROM C573WEB.RGPF03 ";
		  	if (type.equalsIgnoreCase("name"))
		  		qryStr +=   "WHERE UPPER(C573WEB.RGPF03.MEMAIL) = '" + request.getParameter("to").toUpperCase() + "'";
		  	else if (type.equalsIgnoreCase("pw"))
		  		qryStr +=   "WHERE C573WEB.RGPF03.MNAME = '" + 
		  					request.getParameter("name").toUpperCase() + "'";
				  		            
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
	
	public void init() throws ServletException {
		ServletContext sc = getServletContext();
		host = sc.getInitParameter("host");
		from = sc.getInitParameter("from");
	}
	
	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		String nextURL = UCPA.REDIRECT_PAGE;
		AcctMember acctMember;
		String msg = "";
		
		// pick up the top level application object from the session object
		HttpSession session = request.getSession(false);
		if (session != null) {
		
			UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");
	
			// init to initial display until proven otherwise
			LoginStatus currStatus = userUCPA.getCurrentLoginStatus();
			currStatus.setSendResult(SendLogin.RESULT_NONE);
			currStatus.setSendAddress("");
			currStatus.setSendName("");
	
			// determine if processing is req'd
			String type = "";
			if (request.getParameter("type") != null)
				if (request.getParameter("type").length() > 0)
					type = request.getParameter("type");
	
			// looking for lost login name?
			if (type.equalsIgnoreCase("name")) {
	
				// init to bad input until proven otherwise
				currStatus.setSendResult(SendLogin.RESULT_BAD_ADD);
				currStatus.setSendAddress(request.getParameter("to"));
	
				// e-mail address provided by user?
				if (request.getParameter("to").length() > 0) {
	
					// look-up login name based on e-mail address
					acctMember = find(type, request, userUCPA);
	
					// member found?
					if (acctMember != null) {
						msg = "You recently requested that we mail you your login name.\n";
						msg += "Your login name is:\n\n";
						msg += acctMember.getName();
						msg += "\n\nThank you.\n";
	
						// deliver
						try {
							send(acctMember.getEmail(), from, subject, msg);
						}
						catch (Exception ex) {
							UCPA.throwExMsg("SendLogin", "performTask", ex);
						}
						
						currStatus.setSendResult(SendLogin.RESULT_SENT_NAME);
					}
				}
			}
				
			// looking for lost login name?
			else if (type.equalsIgnoreCase("pw")) {
	
				// init to bad input until proven otherwise
				currStatus.setSendResult(SendLogin.RESULT_BAD_NAME);
				currStatus.setSendName(request.getParameter("name"));
	
				// login name provided by user?
				if (request.getParameter("name").length() > 0) {
					
					// look-up e-mail address and password based on login name
					acctMember = find(type, request, userUCPA);
	
					// member found?
					if (acctMember != null) {
						msg = "You recently requested that we mail you your password.\n";
						msg += "Your password is:\n\n";
						msg += acctMember.getPassword();
						msg += "\n\nThank you.\n";
	
						// deliver
						try {
							send(acctMember.getEmail(), from, subject, msg);
						}
						catch (Exception ex) {
							UCPA.throwExMsg("SendLogin", "performTask", ex);
						}
						
						currStatus.setSendResult(SendLogin.RESULT_SENT_PW);
					}
				}
			}
	
			// store data required by JSP that will display results	
			request.setAttribute("userUCPArq", userUCPA);
	
			nextURL = "/SendLogin.jsp";
		}
	
		// hand onto results JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}
	
	private void send(String to, String from, String subj, String text)
	throws Exception {
	
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(false);
	
			Message msg = new MimeMessage(session);
			
			InternetAddress[] toAddrs = null;
			toAddrs = InternetAddress.parse(to, false);
			msg.setRecipients(Message.RecipientType.TO, toAddrs);
	
			msg.setFrom(new InternetAddress(from));
	
			msg.setSubject(subj);
	
			msg.setText(text);
	
			Transport.send(msg);
		}
		catch (Exception ex) {
			UCPA.throwExMsg("SendLogin", "send", ex);
		}
	
	}
}
