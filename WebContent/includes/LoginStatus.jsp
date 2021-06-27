<%-- This scriplet must be included in any JSP that needs to track member login state --%>

<jsp:useBean id="userUCPArq" class="org.ucnj.paccess.UCPA" scope="request">
</jsp:useBean>

<%
// this scriptlet determines if the session login state is out of
// sync with the application login state; this will happen if a member
// has logged on from another session, which kills the login in this
// session as far as the application is concerned; however, this
// session still thinks that the user many be logged in;
// use the difference to alert the member

boolean sessionState = userUCPArq.getCurrentLoginStatus().isLoggedIn();
boolean appState = false;
org.ucnj.paccess.LoggedIn statusloggedIn = 
   (org.ucnj.paccess.LoggedIn)getServletContext().getAttribute("LoggedIn");
if (statusloggedIn != null)
   appState = statusloggedIn.contains(session.getId());

if ((sessionState == true) && (appState == false)) {
   userUCPArq.getCurrentLoginStatus().setLoggedIn(false);  // warn only once
 
   out.println("<SCRIPT language=\"JavaScript\" src=\"scripts/login.js\">");
   out.println("</SCRIPT>");
   out.println("<SCRIPT language=\"JavaScript\">");
   out.println("popupLoggedOut();");
   out.println("</SCRIPT>");
}

%>
