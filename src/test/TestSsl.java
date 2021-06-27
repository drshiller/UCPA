package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestSsl
 */
public class TestSsl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String TARGET_HTTPS_SERVER = "www.myvirtualmerchant.com"; 
	private static final int    TARGET_HTTPS_PORT   = 443; 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestSsl() {
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
		Socket socket = null;
		try {
			response.setContentType("text/html");
			PrintWriter pout = response.getWriter();
			pout.println("<HTML>");
			pout.println("");
			pout.println("<HEAD>");
			pout.println("");
			pout.println("<TITLE>Test SSL</TITLE>");
			pout.println("");
			pout.println("</HEAD>");
			pout.println("");
			pout.println("<BODY>");
			pout.println("");
			pout.println("Testing SSL...");

//			socket = SocketFactory.getDefault().createSocket("www.cnn.com", 80);
//			Writer out = new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1");
//			out.write("GET / HTTP/1.1\r\n");
//			out.write("Host: " + "www.cnn.com" + ":" + 80 + "\r\n");
//			out.write("Agent: SSL-TEST\r\n");
//			out.write("\r\n");
//			out.flush();
			
			socket = SSLSocketFactory.getDefault().createSocket(
				TARGET_HTTPS_SERVER, TARGET_HTTPS_PORT);
			Writer out = new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1");
			out.write("GET / HTTP/1.1\r\n");
			out.write("Host: " + TARGET_HTTPS_SERVER + ":" + TARGET_HTTPS_PORT + "\r\n");
			out.write("Agent: SSL-TEST\r\n");
			out.write("\r\n");
			out.flush();
			
			BufferedReader in = new BufferedReader(
				new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
			String line = null;
			while ((line = in.readLine()) != null) {
				pout.println(line);
				pout.println("<br/>");
			}
			
			pout.println("");
			pout.println("</BODY>");
			pout.println("");
			pout.println("</HTML>");			
			pout.flush();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		finally {
			try {
				socket.close();
			}
			catch (Throwable t1) {
				t1.printStackTrace();
			}
	    }
	}
}
