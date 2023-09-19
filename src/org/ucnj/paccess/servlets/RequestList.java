package org.ucnj.paccess.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.ucnj.paccess.Address;
import org.ucnj.paccess.Result;
import org.ucnj.paccess.UCPA;
import org.ucnj.paccess.dataaccess.beans.OrderItem;
import org.ucnj.paccess.dataaccess.beans.Request;
import org.ucnj.paccess.dataaccess.helpers.MortgageCancellationHelper;
import org.ucnj.paccess.dataaccess.helpers.OrderHelper;

public class RequestList extends UCServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String vmURL = "";
	private String vmMerchantID = "";
	private String vmUserID = "";
	private String vmPIN = "";
	private String vmReceiptApprovalUrl = "";
	private String vmReceiptDeclinedUrl = "";
	
	private enum CheckoutState {
		NOT_SET, CLEARED, SET;
	}
	private CheckoutState checkoutState = CheckoutState.NOT_SET;
	
	@Override
	public void init() throws ServletException {
		ServletContext sc = getServletContext();
		vmMerchantID = sc.getInitParameter("vmMerchantID");	
		vmUserID = sc.getInitParameter("vmUserID");	
		vmPIN = sc.getInitParameter("vmPIN");
		vmURL = sc.getInitParameter("vmURL");
		vmReceiptApprovalUrl = sc.getInitParameter("vmReceiptApprovalUrl");
		vmReceiptDeclinedUrl = sc.getInitParameter("vmReceiptDeclinedUrl");
	}

	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		String nextURL = UCPA.REDIRECT_PAGE;
		
		// pick up the top level application object from the session object
		HttpSession session = request.getSession(false);
		if (session != null) {
		
			// default is to jump to associated JSP; this enables the display
			// of the current list at any time
			nextURL = "/RequestList.jsp";
	
			UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");
			Request currRequest = userUCPA.getCurrentRequest();
	
			// link-based insert (from paged results)
			if (request.getParameter("i") != null) {
				int index = -1;
				if (request.getParameter("i").length() > 0) {
					String indexStr = request.getParameter("i");
					try {
						index = Integer.parseInt(indexStr);
					}
					catch(NumberFormatException ex) {}
				}
				try {
					Result result = (Result)userUCPA.getResults().elementAt(index);
					updateNumPages(result);
					currRequest.add(result);
				}
				catch(ArrayIndexOutOfBoundsException ex) {}
			}
			
			// link-based insert (from details results)
			else if (request.getParameter("m") != null) {
				Result result = (Result)userUCPA.getMainResult();
				updateNumPages(result);
				currRequest.add(result);
			}
	
			// link-based remove (usually from RequestList.jsp)
			else if (request.getParameter("r") != null) {
				if (request.getParameter("r").length() > 0)
					currRequest.getList().remove(request.getParameter("r"));
			}
	
			// submit buttons (from RequestList.jsp)
			else if (request.getParameter("submitRequest") != null) {
				String submitRequest = request.getParameter("next");
	
				// clicked update so save any changes to request counts and/or address
				if (submitRequest.equals("Update")) {
					currRequest.setAddress(
						new Address(
							request.getParameter("name"), 
							request.getParameter("add1"),
							request.getParameter("add2"), 
							request.getParameter("city"), 
							request.getParameter("state"), 
							request.getParameter("zipcode")
						)
					);
					addCounts(request, currRequest);
					nextURL = "/DocIndex";
					if (userUCPA.getCurrentSrchOpts() != null 
						&& userUCPA.getCurrentSrchOpts().getFormLink() != null)
					{
						nextURL += ("?s=" + userUCPA.getCurrentSrchOpts().getFormLink());
					}
				}
	
				// clicked clear so clear all selected items and loop on self	
				else if (submitRequest.equals("Clear")) {
					currRequest.getList().clear();
				}
	
				// clicked print form so save any changes to request counts and go to print-friendly form
				else if (submitRequest.equals("OrderByMail")) {
					addCounts(request, currRequest);
					nextURL = "/Address.jsp";
				}
	
				// clicked print form so save any changes to request counts and go to print-friendly form
				else if (submitRequest.equals("Print")) {
					currRequest.setAddress(
						new Address(
							request.getParameter("name"), 
							request.getParameter("add1"),
							request.getParameter("add2"), 
							request.getParameter("city"), 
							request.getParameter("state"), 
							request.getParameter("zipcode")
						)
					);
					nextURL = "/ReqFriendly.jsp";
				}

				// credit card check out
				else if (submitRequest.equals("OrderByCard")) {
					addCounts(request, currRequest);
					checkoutState = CheckoutState.CLEARED;
					nextURL = "/Checkout.jsp";
				}

				// credit card payment
				else if (submitRequest.equals("Pay")) {
					if (checkoutState.equals(CheckoutState.CLEARED)) {
						OrderHelper orderHelper = new OrderHelper();
						long invoiceNumber = orderHelper.create(userUCPA.getCurrentRequest());
						checkoutState = CheckoutState.SET;
						virtualMerchant(response, 
							Long.toString(invoiceNumber),
							Double.toString(userUCPA.getCurrentRequest().cost()));
						return;
					}
				}
			}
			
			// store data required by JSP that will display results	
			request.setAttribute("userUCPArq", userUCPA);
		}
	
		// hand onto results JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}

	@SuppressWarnings("unchecked")
	public void addCounts(HttpServletRequest reqServlet, Request reqList) {
	
		Hashtable list = reqList.getList();
	
		// work thru all the stored items in the request list
		Enumeration listKeys = list.keys();
		while (listKeys.hasMoreElements()) {
			String itemKey = (String)listKeys.nextElement();
			OrderItem listItem = (OrderItem)list.get(itemKey);
			Result aResult = (Result)listItem.getResult();
	
			// update the counts and overwrite item in list
			String nameCertified = "c_" + itemKey;
			int numCertified = Integer.parseInt(reqServlet.getParameter(nameCertified));
			reqList.add(aResult, numCertified);
		}
	}
	
	private void virtualMerchant(HttpServletResponse response, String invoiceNumber, String amount)
	{
		NameValuePair[] data = new NameValuePair[11];
		data[0] = new NameValuePair("ssl_merchant_ID", vmMerchantID);
		data[1] = new NameValuePair("ssl_user_id", vmUserID);
		data[2] = new NameValuePair("ssl_pin", vmPIN);
		data[3] = new NameValuePair("ssl_transaction_type", "ccsale");
		data[4] = new NameValuePair("ssl_show_form", "true");
		data[5] = new NameValuePair("ssl_amount", amount);
		data[6] = new NameValuePair("ssl_invoice", invoiceNumber);
		data[7] = new NameValuePair("ssl_receipt_apprvl_method", "POST");
		data[8] = new NameValuePair("ssl_receipt_apprvl_post_url", vmReceiptApprovalUrl);
		data[9] = new NameValuePair("ssl_receipt_decl_method", "POST");
		data[10] = new NameValuePair("ssl_receipt_decl_post_url", vmReceiptDeclinedUrl);
		
		PostMethod method = new PostMethod(vmURL + "/process.do");
		method.setRequestBody(data);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
		    new DefaultHttpMethodRetryHandler(3, false));
		
		HttpClient client = new HttpClient();
	    try {
	        // Execute the method.
	        int statusCode = client.executeMethod(method);
	        if (statusCode != HttpStatus.SC_OK) {
	          System.err.println("Method failed: " + method.getStatusLine());
	        }

	        // Read the response body.
	        String responseBody = readFully(
	        	new InputStreamReader(method.getResponseBodyAsStream(), method.getResponseCharSet()));

	        // Deal with the response.
	        // Use caution: ensure correct character encoding and is not binary data
//	        System.out.println(fixUrls(responseBody));
	        
	        PrintWriter out = response.getWriter();
	        out.print(embellish(fixUrls(responseBody)));
	        out.close();
	    }
	    catch (HttpException e) {
	        System.err.println("Fatal protocol violation: " + e.getMessage());
	        e.printStackTrace();
	    }
	    catch (IOException e) {
	        System.err.println("Fatal transport error: " + e.getMessage());
	        e.printStackTrace();
	    }
	    finally {
	        // Release the connection.
	        method.releaseConnection();
	    }  
	}
	
	//consuming the response body as a byte or char stream is more memory efficient
	private String readFully(Reader input) throws IOException {
		BufferedReader bufferedReader =
			(input instanceof BufferedReader) ? (BufferedReader)input : new BufferedReader(input);
		StringBuffer result = new StringBuffer();
		char[] buffer = new char[4 * 1024];
		int charsRead;
		while ((charsRead = bufferedReader.read(buffer)) != -1) {
			result.append(buffer, 0, charsRead);
		}
		return result.toString();
	}
	
	private String fixUrls(String in) {
		String out = in.replaceAll("process.do", vmURL + "/process.do");
		out = out.replaceAll("img src=\"images/", "img src=\"" + vmURL + "/images/");
		out = out.replaceAll("class='large'", "size='3'");
		out = out.replaceAll("Shipping Address", "Shipping Address (if different from Billing Address)");
		return out;
	}
	
	private String embellish(String in) {
		String out = in;
		
		// change name of Process button to Submit
		out = out.replaceAll("value=\"Process\"", "value='Submit'");

		// add a Cancel button to the VM payment form
		int idx = out.indexOf("</head>");
		if (idx != -1) {
			out = out.substring(0, idx)
			+ "        <script language='javascript' type='text/javascript'>\n"
			+ "           function cancel() {\n"
			+ "              window.location = '/UCPA/DocIndex?s=req';\n"
			+ "              return true;\n"
			+ "           }\n"
			+ "        </script>\n"
			+ out.substring(idx);
		}
		idx = out.indexOf("<input type=\"button\"");
		if (idx != -1) {
			out = out.substring(0, idx)
			+ "<input type='button' value='Cancel' onclick='cancel();'/>"
			+ out.substring(idx);
		}
		
		// remove required symbol from 1st 2 fields (Amount and Invoice Number)
		String reqd = "<img src=\"" + vmURL + "/images/required.gif\">";
		idx = out.indexOf(reqd);
		if (idx != -1) {
			out = out.substring(0, idx) + out.substring(idx + reqd.length());
		}
		idx = out.indexOf(reqd);
		if (idx != -1) {
			out = out.substring(0, idx) + out.substring(idx + reqd.length());
		}
		return out;
	}

	private void updateNumPages(Result r) throws Exception 
	{
		switch (r.getDocType()) {
			case UCPA.DOC_TYPE_MORTGAGE_CANCELLATION:
				int numPages = r.getNumPages() +
					MortgageCancellationHelper.getNumberOfPagesFromMortgage(
						r.getBook(), r.getPage());
				r.setNumPages(numPages);
				break;
			case UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT:
				if (r.getNumPages() == 0) {
					r.setNumPages(2);
				}
				break;
		}
	}
	
	private void printHttpsCert() {
		System.out.println("printHttpsCert: start");
		try {
			URL url = new URL(vmURL + "/process.do");
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			System.out.println("Response Code : " + con.getResponseCode());
			System.out.println("Cipher Suite : " + con.getCipherSuite());
			System.out.println("\n");
			 
			Certificate[] certs = con.getServerCertificates();
			for(Certificate cert : certs){
				System.out.println("Cert Type : " + cert.getType());
				System.out.println("Cert Hash Code : " + cert.hashCode());
				System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
				System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
				System.out.println("\n");
			}
		} 
		catch (SSLPeerUnverifiedException e) {
			e.printStackTrace();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("printHttpsCert: end");
	}
}
