package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HttpsClient
 */
public class HttpsClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HttpsClient() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doIt(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doIt(request, response);
	}
	
	private void doIt(HttpServletRequest request, HttpServletResponse response) {
		String https_url = "https://www.myvirtualmerchant.com/VirtualMerchant/process.do";
		URL url;
		try {
			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			
			response.setContentType("text/html");
			PrintWriter pout = response.getWriter();
			pout.println("<HTML>");
			pout.println("");
			pout.println("<HEAD>");
			pout.println("");
			pout.println("<TITLE>Test HttpsClient</TITLE>");
			pout.println("");
			pout.println("</HEAD>");
			pout.println("");
			pout.println("<BODY>");
			pout.println("");
			pout.println("Testing HttpsClient...");

			print_https_cert(con, pout);
			print_content(con, pout);
			
			pout.println("");
			pout.println("</BODY>");
			pout.println("");
			pout.println("</HTML>");			
			pout.flush();
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void print_https_cert(HttpsURLConnection con, PrintWriter pout) {
		if (con!=null) {
			try {
				pout.println("Response Code : " + con.getResponseCode());
				pout.println("<br/>");
				pout.println("Cipher Suite : " + con.getCipherSuite());
				pout.println("<br/>");
				 
				Certificate[] certs = con.getServerCertificates();
				for(Certificate cert : certs){
					pout.println("Cert Type : " + cert.getType());
					pout.println("<br/>");
					pout.println("Cert Hash Code : " + cert.hashCode());
					pout.println("<br/>");
					pout.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
					pout.println("<br/>");
					pout.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
					pout.println("<br/>");
				}
			} 
			catch (SSLPeerUnverifiedException e) {
				e.printStackTrace();
			} 
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	 
	private void print_content(HttpsURLConnection con, PrintWriter pout) {
		if (con != null) {
			try {
				pout.println("****** Content of the URL ********");			
				pout.println("<br/>");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				while ((input = br.readLine()) != null) {
					pout.println(input);
					pout.println("<br/>");
				}
				br.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
