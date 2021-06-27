package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SuperSnoop extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String ssver = "Version 2004-07-28 02:15:00 PM";
	private static final int K = 1024;

	//Initialize global variables
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	private String getClassInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("<h3>Class and Classloader Information</h3>");
		ClassLoader classloader = SuperSnoop.class.getClassLoader();
		sb.append("<h4>Classloader:</h4>" + classloader);

		Method[] m = SuperSnoop.class.getDeclaredMethods();
		if (m.length == 0) {
			sb.append("<br>There are no declared methods in this class.<br>");
		} else {

			sb.append("<h4>Declared Methods:</h4>");

			for (int i = 0; i < m.length; i++) {
				sb.append(m[i] + "<br>");
			}
		}

		Field[] f = SuperSnoop.class.getFields();
		if (f.length == 0) {
			sb.append("<br>There are no fields in this class.<br>");
		} else {

			sb.append("<h4>Fields:</h4>");

			for (int i = 0; i < f.length; i++) {
				sb.append(f[i] + "<br>");
			}
		}

		sb.append(
			"<br>Package to which this class belongs:  "
				+ SuperSnoop.class.getPackage()
				+ "<br>");

		Object[] o = SuperSnoop.class.getSigners();
		if (o == null) {
			sb.append("<br>There are no signers for this class.<br>");
		} else {
			sb.append("<h4>Signers:</h4>");
			String[] s = (String[]) o;
			for (int i = 0; i < s.length; i++) {
				sb.append(s[i] + "<br>");
			}
		}
		return new String(sb);
	}

	//Process the HTTP Get request
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {

		System.out.println("SuperSnoop running");
		PrintWriter out = new PrintWriter(res.getWriter());
		res.setContentType("text/html");

		HttpSession session = req.getSession(true);

		Integer count = (Integer) session.getAttribute("snoop.count");
		if (count == null) {
			session.setMaxInactiveInterval(-1); // Set session timeout
			count = new Integer(1);
		} else {
			count = new Integer(count.intValue() + 1);
		}
		session.setAttribute("snoop.count", count);

		out.println(
			"<HTML><HEAD><TITLE>SuperSnoop " + ssver + "</TITLE></HEAD>");
		out.println("<BODY><H1>SuperSnoop " + ssver + "</H1>");
		out.println(
			"You've visited this page "
				+ count
				+ ((count.intValue() == 1) ? " time." : " times."));

		out.println("<br><br>");
		out.println(
			"Requested sessionID from URL: "
				+ req.isRequestedSessionIdFromURL()
				+ "<br>");
		out.println(
			"Requested sessionID from cookie: "
				+ req.isRequestedSessionIdFromCookie()
				+ "<br>");
		out.println(
			"Requested sessionID valid: "
				+ req.isRequestedSessionIdValid()
				+ "<br>");
		out.println(
			"<a href="
				+ res.encodeURL(req.getRequestURI())
				+ ">Click to test session tracking via URL rewriting</a>");
		out.println("<br><br>");

		out.println(getClassInfo());

		out.println("<h3>Memory Status</h3>");
		out.println(logMemStats() + "<br><br>");

		out.println("<H3>Saved Session Data:</H3>");
		Enumeration anum = session.getAttributeNames();
		while (anum.hasMoreElements()) {
			String aname = (String) anum.nextElement();
			if (aname != null) {
				Object avalue = session.getAttribute(aname);
				out.println("Attribute: " + aname + ": " + avalue + "<br>");
			}
		}

		out.println("<H3>Session Information</H3>");
		out.println(
			"Maximum inactive interval: "
				+ session.getMaxInactiveInterval()
				+ " seconds <BR>");
		out.println("Session id: " + session.getId() + "<BR>");
		out.println("New session: " + session.isNew() + "<BR>");
		out.println("Creation time: " + session.getCreationTime() + "<BR>");
		out.println("<I>(" + new Date(session.getCreationTime()) + ")</I><BR>");
		out.println(
			"Last Access time: " + session.getLastAccessedTime() + "<BR>");
		out.println(
			"<I>(" + new Date(session.getLastAccessedTime()) + ")</I><BR>");

		out.println("<H3>Authentication Data</H3>");
		out.println("User Name: " + req.getRemoteUser() + "<BR>");
		out.println("Authorization type: " + req.getAuthType() + "<BR>");

		String principalName =
			(req.getUserPrincipal() == null)
				? null
				: req.getUserPrincipal().getName();
		out.println("Principal Name = " + principalName + "<br>");
		out.println(
			"isUserInRole('highrole') = "
				+ req.isUserInRole("highrole")
				+ "<br>");
		out.println(
			"isUserInRole('lowrole') = "
				+ req.isUserInRole("lowrole")
				+ "<br>");

		out.println("<H3>Client Machine Data</H3>");
		out.println("Client address: " + req.getRemoteAddr() + "<BR>");
		out.println("Client hostname: " + req.getRemoteHost() + "<BR>");

		out.println("<H3>HTTP Request Header Data</H3>");
		Enumeration e = req.getHeaderNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = req.getHeader(name);
			if (value != null) {
				out.println(name + ": " + value + "<br>");
			}
		}

		String pw = req.getHeader("authorization");
		if (pw != null) {
//			String pwe = pw.substring(6); // get userid and password
//			sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
//			String userpassDecoded = new String(dec.decodeBuffer(pwe));
//			out.println(
//				"<br>Decoded authorization header = " + userpassDecoded);
//			System.out.println("pass = " + userpassDecoded);
		}

		Properties props = System.getProperties();

		out.println("<H3>System Properties</H3>");
		for (e = props.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			out.println(key + " = " + System.getProperty(key) + "<br>");
		}

		out.println("<H3>java:comp Context</H3>");
		try {
			Context initCtx1 = new InitialContext();

			NamingEnumeration nenum1 = initCtx1.listBindings("java:comp");
			out.println(
				"<table border=3 cellpadding=3 bordercolor=blue>"
					+ "<tr><td>Name</td> <td>Type</td> <td>Value</td></tr>");

			while (nenum1.hasMoreElements()) {
				Binding binding = (Binding) nenum1.next();
				out.println(
					"<td>"
						+ binding.getName()
						+ "</td><td>"
						+ binding.getClassName()
						+ "</td> <td>"
						+ binding.getObject()
						+ "</td> </tr>");
			}

			out.println("</table>");
		} catch (NamingException ex) {
			ex.printStackTrace(out);
		}

		out.println("<H3>java:comp/env Context (if any)</H3>");
		try {
			Context initCtx2 = new InitialContext();
			NamingEnumeration nenum2 = initCtx2.listBindings("java:comp/env");

			out.println(
				"<table border=3 cellpadding=3 bordercolor=blue>"
					+ "<tr><td>Name</td> <td>Type</td> <td>Value</td></tr>");

			while (nenum2.hasMoreElements()) {
				Binding binding = (Binding) nenum2.next();
				out.println(
					"<td>"
						+ binding.getName()
						+ "</td><td>"
						+ binding.getClassName()
						+ "</td> <td>"
						+ binding.getObject()
						+ "</td> </tr>");
			}

			out.println("</table>");
		} catch (NamingException ex) {
			ex.printStackTrace(out);
		}
		out.println("</BODY></HTML>");
		out.close();
	}

	//Process the HTTP Post request
	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		doGet(req, res);
	}

	private String logMemStats() {
		Runtime r = Runtime.getRuntime();
		StringBuffer s = new StringBuffer("Total memory: ");
		s.append((r.totalMemory() / K));
		s.append("K, free memory: ");
		s.append((r.freeMemory() / K));
		s.append("K");

		return s.toString();

	}
}