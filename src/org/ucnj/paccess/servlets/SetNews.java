package org.ucnj.paccess.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ucnj.paccess.News;
import org.ucnj.paccess.NewsItem;

public class SetNews extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	private static final int NUM_NEWS_ITEMS = 5;
	
	private String etcRealPath;
	private String password;
	private String newsFile;

    public SetNews() {
    }
    
    @Override
    public void init() throws ServletException {
		ServletContext sc = getServletContext();
		etcRealPath = sc.getRealPath(sc.getInitParameter("etcVirtualPath"));
		newsFile = etcRealPath + "/" + sc.getInitParameter("newsFileName");
    	password = sc.getInitParameter("password");
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException 
	{
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
	throws ServletException, IOException
	{
		try { 
			performTask(request, response);
		}
		catch(Exception exp){
			request.setAttribute("exception", exp);
			RequestDispatcher dispatcher =  getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
		}
	}
	

	private void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		News news = load();
		
		boolean pwOK = true;
		
		if (request.getParameter("p") != null
			&& request.getParameter("pw") != null
			&& (pwOK = request.getParameter("pw").equals(password)) == true)
		{
			publish(request, news);
		}
		
		showPage(response, pwOK, news);
	}
	
	private News load() {
		ServletContext sc = getServletContext();
		
		// check for system status in application context
		News news = (News)sc.getAttribute("News");
		if (news == null) {
	
			try {
				// we're looking to see if the system object has been
				// serialized to a disk file held in this subdirectory;
				// create the subdir if it doesn't exist
				File f = new File(etcRealPath);
				if (!f.exists()) {
					f.mkdir();
				}
				
				// the serialized object file:
				// if it exists then load its info;
				f = new File(newsFile);
				if (f.exists()) {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
					news = (News)in.readObject();
					in.close();
				}
				
				// else create default system object and serialize it
				else {
					news = new News();
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
					out.writeObject(news);
					out.flush();
					out.close();
				}
	
				// stick into servlet context
				sc.setAttribute("News", news);
	
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	
		return news;
	}
	
	private void publish(HttpServletRequest request, News news) 
	throws Exception
	{
		if (news.getNews() == null) {
			news.setNews(new ArrayList<NewsItem>(NUM_NEWS_ITEMS));
		}
		else {
			news.getNews().clear();
		}

		ServletContext sc = getServletContext();
		String newsPath = sc.getRealPath("") + "/includes/news.html";
		PrintWriter pw = new PrintWriter(new FileWriter(newsPath));
		pw.println("<html>");
		pw.println("   <head>");
		pw.println("   </head>");
		pw.println("   <body>");

		pw.println("      <ul>");
		for (int idx = 0; idx < NUM_NEWS_ITEMS; idx++) {
			NewsItem newsItem = createNewsItem(request, pw, idx);
			news.getNews().add(newsItem);
		}
		pw.println("      </ul>");
		
		pw.println("   </body>");
		pw.println("</html>");
		pw.close();	
		
		// save object using lightweight persistence
		sc.setAttribute("News", news);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newsFile));
		out.writeObject(news);
		out.flush();
		out.close();
	}
	
	private NewsItem createNewsItem(HttpServletRequest request, PrintWriter out, int idx) 
	{
		NewsItem newsItem = null;
		
		String headlineId = "headline" + idx;
		String headline = request.getParameter(headlineId);
		if (headline != null) {
			headline = headline.trim();
		}
		String detailsId = "details" + idx;
		String details = request.getParameter(detailsId);
		if (details != null) {
			details = details.trim();
		}
		
		if (headline != null && headline.length() > 0) {
			newsItem = new NewsItem();
			newsItem.setHeadline(headline);
			
			out.print("         <li id='" + headlineId + "' class='headline'>");
			out.println(headline);
			if (details != null && details.length() > 0) {
				newsItem.setDetails(details);
				
				out.print("            <div id='" + detailsId + "' class='details detailDisplay'>");
				out.print(details);
				out.println("</div>");
			}
			out.println("         </li>");
		}
		
		return newsItem;
	}

	private void showPage(HttpServletResponse response, boolean pwOK, News news) 
	throws Exception
	{
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("   <head>");
			out.println("      <title>Union County Public Access News Editor</title>");
			out.println("   </head>");
			out.println("   <body style='font-family: arial;'>");
			
			out.println("      <div style='font-size: 16pt;'>");
			out.println("         Union County Clerk's Office<br/>");
			out.println("         Public Land Records<br/>");
			out.println("        News Editor<br/>");
			out.println("      </div>");
			
			out.println("      <hr align='left' width='421'>");
			
			out.println("      <form method='POST' action='NewsServlet'>");
			
			if (!pwOK) {
				out.println("         <div style='font-weight:bold;color:red'>Incorrect password!</div>");
			}
			out.println("         <div style='margin-bottom: 30px;'>");
			out.println("            <b>Password:</b> <input type='password' name='pw'>");
			out.println("         </div>");
			
			out.println("         <table cellpadding='0' cellspacing='0'>");
			
			for (int idx = 0; idx < NUM_NEWS_ITEMS; idx++) {
				String headline = "";
				String details = "";
				if (news.getNews() != null) {
					NewsItem anItem = news.getNews().get(idx);
					if (anItem != null && anItem.getHeadline() != null) {
						headline = anItem.getHeadline();
						if (anItem.getDetails() != null) {
							details = anItem.getDetails();
						}
					}
				}
				
				String sIdx = "" + (idx + 1);
				out.println("            <tr>");
				out.println("               <td style='text-align: right;font-weight:bold;padding-bottom: 5px;'>Headline " + sIdx + ":</td>");
				out.println("               <td style='text-align: right;font-weight:bold;padding-bottom: 5px;'>");
				out.println("               	<input type='text' style='width: 500px;' name='headline" + sIdx + "' value='" + headline + "'>");
				out.println("               </td>");
				out.println("            </tr>");
				out.println("            <tr>");
				out.println("               <td style='text-align: right;font-weight:bold;padding-bottom: 20px;'>Details " + sIdx + ":</td>");
				out.println("               <td style='text-align: right;font-weight:bold;padding-bottom: 20px;'>");
				out.println("               	<input type='text' style='width: 500px;' name='details" + sIdx + "' value='" + details + "'>");
				out.println("               </td>");
				out.println("            </tr>");
			}
			
			out.println("         </table>");
			out.println("         <input type='submit' value='Publish'>");
			out.println("         <input type='hidden' name='p'>");
			out.println("      </form>");
			out.println("   </body>");
			out.println("</html>");
			out.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
