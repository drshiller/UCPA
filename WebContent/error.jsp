<HTML>
<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="/UCPA/theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

</HEAD>

<BODY bgcolor="#FFFFFF" vlink="#0000ff" link="#0000ff" alink="#ff0000">

<%@ page 
import="org.ucnj.paccess.ServiceDownException" 
%>

<jsp:useBean id="exception" class="java.lang.Exception" scope="request">
</jsp:useBean>

<DIV style="font-family: arial;">

<CENTER>

<IMG src="/UCPA/grfx/logoheader3.gif" border="0">
<BR>
<BR>

<% if (exception instanceof ServiceDownException) { %>

<FONT color="#ff0000" size="+2">Sorry, this service is temporarily unavailable.</FONT>
<BR>
<BR>
Please close this browser session and try the service again later.

<% } else { %>

<FONT color="#ff0000" size="+2">Sorry, an error has occurred.</FONT>
<BR>
<BR>
Please copy the following error message and e-mail it to the Union County Clerk's
<A href="mailto:DocIndex@ucnj.org">Webmaster</A>.

</CENTER>

<BR>
<B><U>Begin Error Message</U></B>
<BR>
<%= exception.getMessage() %>
<BR>
<B><U>End Error Message&nbsp;&nbsp;</U></B>
<BR>
<BR>

<% } %>

</DIV>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="/includes/LinkBar.jsp" %>

</BODY>
</HTML>
