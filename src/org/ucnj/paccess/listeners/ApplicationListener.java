package org.ucnj.paccess.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.ucnj.paccess.LoggedIn;

public class ApplicationListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent sce) {
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		LoggedIn loggedIn = (LoggedIn)sc.getAttribute("LoggedIn");
		if (loggedIn == null) {
			loggedIn = new LoggedIn();
			sc.setAttribute("LoggedIn", loggedIn);
		}
	}

}
