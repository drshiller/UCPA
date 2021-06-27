package org.ucnj.paccess.maintenance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SetStatus extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private String etcRealPath = "";
	private String sysFile = "";
	private String password = "";

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

	public void init() throws ServletException {
		ServletContext sc = getServletContext();
		etcRealPath = sc.getRealPath(sc.getInitParameter("etcVirtualPath"));
		sysFile = etcRealPath + "/" + sc.getInitParameter("sysFileName");
		password = sc.getInitParameter("password");
		load(sc);
	}

	private System load(ServletContext sc) {
	
		// check for system status in application context
		System sys = (System)sc.getAttribute("System");
		if (sys == null) {
	
			try {
	
				// we're looking to see if the system object has been
				// serialized to a disk file held in this subdirectory;
				// create the subdir if it doesn't exist
				File f = new File(etcRealPath);
				if (f.exists() == false)
					f.mkdir();
	
				// the serialized object file:
				// if it exists then load its info;
				f = new File(sysFile);
				if (f.exists()) {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
					sys = (System)in.readObject();
				}
				
				// else create default system object and serialize it
				else {
					sys = new System();
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
					out.writeObject(sys);
					out.flush();
				}
	
				// stick into servlet context
				sc.setAttribute("System", sys);
	
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	
		return sys;
	
	}

	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		boolean pwOK = true;
		
		// pick up system object from servlet context
		ServletContext sc = getServletContext();
		System sys = load(sc);
	
		// if some params then must be setting values
		if (request.getParameter("s") != null) {
			
			if (request.getParameter("pw") != null) {
				if ((pwOK = request.getParameter("pw").equals(password)) == true) {
			
					if (request.getParameter("status") != null) {
						if (request.getParameter("status").equalsIgnoreCase("up"))
							sys.setStatus(System.STATUS_UP);
						else
							sys.setStatus(System.STATUS_DOWN);
					}
					
					if (request.getParameter("message") != null)
						sys.setMessage(request.getParameter("message"));
	
					// set in servlet context
					sc.setAttribute("System", sys);
	
					// save object using lightweight persistence
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(sysFile));
					out.writeObject(sys);
					out.flush();
	
				}
			}
		}
	
		showPage(response, sys, pwOK);
	}

	private void showPage(HttpServletResponse response, System sys, boolean pwOK) 
	throws Exception {
	
		try {
			
			// init the response content type:
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
	
			out.println("<HTML>");
			out.println("");
			out.println("<HEAD>");
			out.println("");
			out.println("<TITLE>Union County Public Access System Status</TITLE>");
			out.println("");
			out.println("</HEAD>");
			out.println("");
			out.println("<BODY>");
			out.println("");
			
			out.println("<DIV STYLE=\"font-family: Arial\">");
			out.println("");
	
			out.println("<SPAN style=\"font-size:16pt;\">");
			out.println("Union County Clerk's Office<BR>");
			out.println("Public Land Records<BR>");
			out.println("System Status Page<BR>");
			out.println("</SPAN>");
			out.println("<HR align=\"left\" width=\"421\">");
			out.println("");
			
			out.println("<FORM method=\"POST\" action=\"SetStatus\">");
	
			if (pwOK == false) {
				out.println("<SPAN style=\"font-weight:bold;color:red\">Incorrect password!</SPAN>");
				out.println("<BR>");
			}
			out.println("<B>Password:</B> <INPUT type=\"Password\" name=\"pw\">");
			out.println("<BR>");
			out.println("<BR>");
			
			out.println("<B>Set the System Status:</B>");
			out.println("<BR>");
			String checked = "";
			if (sys != null && sys.getStatus() == System.STATUS_UP) {
				checked = " checked";
			}
			out.println("<INPUT type=\"radio\" name=\"status\" value=\"up\"" + checked + ">Up");
			out.println("<BR>");
			checked = "";
			if (sys != null && sys.getStatus() == System.STATUS_DOWN) {
				checked = " checked";
			}
			out.println("<INPUT type=\"radio\" name=\"status\" value=\"down\"" + checked + ">Down");
			out.println("<BR>");
			
			out.println("<BR>");
			out.println("<B>Set the System Down Message:</B>");
			out.println("<BR>");
			
			out.println("<TEXTAREA name=\"message\" rows=\"10\" cols=\"50\">");
			if (sys != null) {
				out.print(sys.getMessage());
			}
			out.println("</TEXTAREA>");
			out.println("<BR>");
			
			out.println("<BR>");
	        out.println("<INPUT type=\"submit\" value=\"Set\">");
	        out.println("<INPUT type=\"hidden\" name=\"s\">");
			out.println("</FORM>");
			out.println("");
			
			out.println("</DIV>");
			out.println("");
	
			out.println("</BODY>");
			out.println("");
			out.println("</HTML>");
			
			out.flush();
		}
	
		catch(Exception ex) {
			ex.printStackTrace();
		}
			
	}
}
