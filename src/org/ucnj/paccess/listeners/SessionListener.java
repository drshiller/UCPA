package org.ucnj.paccess.listeners;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.ucnj.paccess.LoggedIn;

public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		cleanup(event.getSession());
	}

	private void cleanup(HttpSession session) {
	
		System.out.println("Session Destroyed: " + session.getId());
		
		ServletContext sc = session.getServletContext();
	
		// remove image files viewed during this session;
		// the root name of each file starts with the related session ID
		String rviVirtualPath = sc.getInitParameter("rviVirtualPath");
		String rviRealPath = sc.getRealPath(rviVirtualPath);
		File rviDir = new File(rviRealPath);
		File list[] = rviDir.listFiles();
		for (int i = 0; i < list.length; i++) {
			if (list[i].getName().startsWith(session.getId()))
				list[i].delete();
		}
	
		// remove user from login object in case he/she is logged in;
		// logged in info held within the servlet context
		LoggedIn loggedIn = (LoggedIn)sc.getAttribute("LoggedIn");
		loggedIn.remove(session.getId());
	}
}