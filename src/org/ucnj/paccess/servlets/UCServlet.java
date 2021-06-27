package org.ucnj.paccess.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Parent servlet class to hold doGet/doPost boilerplate 
 */

public abstract class UCServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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
	
	public abstract void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception;
}
