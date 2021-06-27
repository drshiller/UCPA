<HTML>
<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

</HEAD>

<BODY bgcolor="#FFFFFF" vlink="#0000ff" link="#0000ff" alink="#ff0000">

<%@ page 
import="org.ucnj.paccess.maintenance.System" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<%
System sys = (System)getServletContext().getAttribute("System");
String message = "";
if (sys != null)
   message = sys.getMessage();
else
   message = "Please return to this site at a later time for further information.";
%>

<DIV style="font-family: arial;">

<CENTER>

<IMG src="grfx/logoheader3.gif" border="0">
<BR>
<BR>

<FONT color="#ff0000" size="+2">Sorry, this service is temporarily unavailable.</FONT>
<BR>
<BR>

<TABLE width="500" cellpadding="5" cellspacing="0" border="1">
  <TR><TD><%= message %></TD></TR>
</TABLE>

</CENTER>

</DIV>

</BODY>
</HTML>
