package org.ucnj.paccess.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.LoggedIn;
import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.FileTransfer;

public class Viewer extends UCServlet {

	private static final long serialVersionUID = 1L;
	
	static final String RVI_PDF_LINE 		= "PDF";
	static final String RVI_COMMAND_LINE 	= "document.RVIViewer.RunCommand";
	static final String RVI_PGM          	= "/pgms/RVIMAIN.PGM";

	private String appURL;
	private String rviHost;
	private String rviVirtualPath;
	private ResourceBundle reservedIPAddresses;

	public void init() throws ServletException {
	
		ServletContext sc = getServletContext();
		appURL = sc.getInitParameter("appURL");
		rviHost = sc.getInitParameter("rviHost");
		rviVirtualPath = sc.getInitParameter("rviVirtualPath");
	
		// pick up the reserved IP addresses from a properties file;
		// these addresses will have unfettered access to the web site;
		// ignore if the properties file is missing	
		try {
			reservedIPAddresses = ResourceBundle.getBundle("reservedIPAddresses");
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	
	}

	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception 
	{
		String nextURL = UCPA.REDIRECT_PAGE;
		
		// Get the user's session object.
		HttpSession session = request.getSession(false);
	
		if (session != null) {
		
			// default target URL
			UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");
	
			// if user requested just to see annotations only then jump to that page;
			// this should be done after a trip to the server via the Viewer.jsp;
			// otherwise the displayed data might be empty or from some other query
			if (request.getParameter("notesOnly") != null) {
				nextURL = "/Annotations.jsp";
			}
	
			// else get images and annotations for display in viewer
			else {
				nextURL = "/Viewer.jsp";
					
//				// check to see if user can view multiple pages;
//				// NOTE: page flag settings are Y=single or N=multiple;
//				String pageFlag = "Y";
//				
//				// First test checks if the requesting IP address belongs to one
//				// of the internal Union County machines, which have full viewing priviledges
//				if (isReservedAddress(request.getRemoteAddr()) == true)
//					pageFlag = "N";
//					
//				// else see if user has logged in via some other external address
//				else {
//					LoggedIn loggedIn = (LoggedIn)getServletContext().getAttribute("LoggedIn");
//					if (loggedIn != null) {
//						if (loggedIn.contains(session.getId()))
//							pageFlag = "N";
//					}
//				}
				
				String pageFlag = "N";
	
				// execute the query
				userUCPA.getCurrentView().setPdfUrl("");

				String rviTransactionCode = request.getParameter("id");
				String remoteFileURL = doAnnotationQuery(rviTransactionCode, pageFlag);
				if (remoteFileURL.length() > 0) {
					doAnnotationTransfer(request, userUCPA, session.getId(), rviTransactionCode, remoteFileURL);
				}
				
				remoteFileURL = doPdfQuery(rviTransactionCode, pageFlag);
				if (remoteFileURL.length() > 0) {
					doPdfTransfer(request, userUCPA, session.getId(), rviTransactionCode, remoteFileURL);
				}
			}
	
			// store data required by JSP that will display results	
			request.setAttribute("userUCPArq", userUCPA);
		}
	
		// hand onto viewer
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}
	
	/**
	 * Execute an image search on the AS/400 that handles the RVI annontation data.
	 */
	private String doAnnotationQuery(String sKey, String pageFlag)
	throws Exception 
	{
		// the URL for executing the RVI application on its AS/400
		String sURL = "http://" + rviHost + RVI_PGM + "?" + 
					  "RQSTYP=IMAGEV&" + 
					  "RQSDTA=" + sKey + "&" +
					  "SINGLE=" + pageFlag;
	
		return getRemoteFileURL(sURL, RVI_COMMAND_LINE, "LST");
	}
	
	/**
	 * transfer the data from the RVI AS/400 to the local web server.
	 */
	private void doAnnotationTransfer(HttpServletRequest request, UCPA userUCPA, 
		String sessionID, String rviTransactionCode, String remoteAnnotationFileURL)
	throws Exception 
	{
		String rviRealPath = request.getSession().getServletContext().getRealPath(rviVirtualPath);
		
		int idxBeg = remoteAnnotationFileURL.lastIndexOf("/") + 1;
		int idxEnd = remoteAnnotationFileURL.lastIndexOf(".");
		String baseName = remoteAnnotationFileURL.substring(idxBeg, idxEnd);
		String rootLocalFile = rviRealPath + "//" + sessionID + "_";
		
		transferAnnotations(request, userUCPA, remoteAnnotationFileURL, rviTransactionCode, rootLocalFile, baseName);
	}

	private void transferAnnotations(HttpServletRequest request, UCPA userUCPA, 
		String remoteAnnotationFileURL, String rviTransactionCode, String rootLocalFile, String baseName)
	throws Exception
	{
		// remote location of RVI-generated annotation file
		// Note: root uses transaction code
		int endIdx = remoteAnnotationFileURL.lastIndexOf("/") + 1;
		String remoteURL = remoteAnnotationFileURL.substring(0, endIdx);
		URL fromURL = new URL(remoteURL + rviTransactionCode + ".TXT");

		// local location of replicated annotation file
		// Note: use image name to sync up with image files
		String annName = rootLocalFile + baseName + ".ann";
		File annFile = new File(annName);

		// replicate annotation file
		FileTransfer ft = new FileTransfer();
		if (ft.download(fromURL, annFile) == true) {
			userUCPA.getCurrentView().setAnnotations(annName,
				request.getParameter("dt"), request.getParameter("in"), request.getParameter("bp"));
		}
		else {
			throw new Exception("Annotation file download failed.");
		}
	}
	
	/**
	 * Execute an image search on the AS/400 that handles the RVI PDF data.
	 */
	private String doPdfQuery(String sKey, String pageFlag)
	throws Exception 
	{
		// the URL for executing the RVI application on its AS/400
		String sURL = "http://" + rviHost + RVI_PGM + "?" + 
					  "RQSTYP=IMAGEV&" + 
					  "DELTYP=P&" + 
					  "RQSDTA=" + sKey + "&" +
					  "SINGLE=" + pageFlag;
		
		return getRemoteFileURL(sURL, RVI_PDF_LINE, "PDF");
	}
	
	/**
	 * transfer the PDF file from the RVI AS/400 to the local web server.
	 */
	private void doPdfTransfer(HttpServletRequest request, UCPA userUCPA, 
		String sessionID, String rviTransactionCode, String remotePdfFileURL)
	throws Exception 
	{
		String localURL = appURL + rviVirtualPath + "/";
		String rviRealPath = request.getSession().getServletContext().getRealPath(rviVirtualPath);
		
		int idxBeg = remotePdfFileURL.lastIndexOf("/") + 1;
		int idxEnd = remotePdfFileURL.lastIndexOf(".");
		String baseName = remotePdfFileURL.substring(idxBeg, idxEnd);
		String rootLocalFile = rviRealPath + "//" + sessionID + "_";
		
		URL fromURL = new URL(remotePdfFileURL);
		File pdfFile = new File(rootLocalFile + baseName + ".pdf");	// copy of remote
		FileTransfer ft = new FileTransfer();
		if (!ft.download(fromURL, pdfFile)) {
			throw new Exception("PDF download failed.");
		}

		userUCPA.getCurrentView().setPdfUrl(localURL + sessionID + "_" + baseName + ".pdf");
	}
	
	private String getRemoteFileURL(String sURL, String commandLine, String fileType)
	throws Exception 
	{
		String remoteListFileURL = "";
	
		// open an connection to the app server handling the RVI data
		URL rviURL = new URL(sURL);
		URLConnection connURL = rviURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connURL.getInputStream()));

		// start reading the HTTP response from the server
		String inLine;
		while ((inLine = in.readLine()) != null) {

			// when the critical line is found then pull out the URL for the RVI list file
			if(inLine.indexOf(commandLine) >= 0) {
				int idxBeg = inLine.toUpperCase().indexOf("HTTP:");
				int idxEnd = inLine.toUpperCase().lastIndexOf(fileType) + fileType.length();
				remoteListFileURL = inLine.substring(idxBeg, idxEnd);
				break;
			}
		}
		in.close();
	
		return remoteListFileURL;
	}

	private boolean isReservedAddress(String ipAddress) {
		if (reservedIPAddresses != null) {
			Enumeration<String> theKeys = reservedIPAddresses.getKeys();
			while (theKeys.hasMoreElements()) {
		    	String key = (String)theKeys.nextElement();
		    	if (key.equals(ipAddress))
		    		return true;
			}
		}
		return false;
	}
}
