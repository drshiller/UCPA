<%@ page 
import="org.ucnj.paccess.UCPA" 
%>

<HTML>

<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="/UCPA/theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
<STYLE type="text/css">
<!--
TD{
  font-family : Arial;
}
-->
</STYLE>

</HEAD>

<BODY BGCOLOR="#FFFFFF" vlink="#0000ff" link="#0000ff" alink="#ff0000" onBlur="window.focus()">

<%@ page 
import="org.ucnj.paccess.BoardDate" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true" 
isErrorPage="false" 
%>

<jsp:useBean id="userUCPArq" class="org.ucnj.paccess.UCPA" scope="request">
</jsp:useBean>

<%
@SuppressWarnings("unchecked")
java.util.Vector dates = userUCPArq.getDates();
%>

<DIV align="center" style="font-family: Arial">

<FONT face="Arial" size="+1"><U>Availability Dates as of <%= UCPA.todaysDate() %></U></FONT>
<BR>
<BR>
<TABLE width="500" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD width="50%"><B>Document</B></TD>
      <TD width="25%" align="center"><B>Start Date</B></TD>
      <TD width="25%" align="right"><B>Board Date</B></TD>
    </TR>
  </TBODY>
</TABLE>
<%
for (int i = 0; i < dates.size(); i++) {
   BoardDate aBoardDate = (BoardDate)dates.elementAt(i);
%>
<TABLE width="500" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD width="50%"><%= aBoardDate.getDocType() %></TD>
      <TD width="25%" align="center"><%= aBoardDate.getStartDate() %></TD>
      <TD width="25%" align="right"><%= aBoardDate.getBoardDate() %></TD>
    </TR>
  </TBODY>
</TABLE>
<%
} // dates record for loop
%>

<BR>
<TABLE width="500" cellpadding="0" cellspacing="0">
  <TBODY>
    <TR>
      <TD colspan="2" align="right"><A href="javascript:window.close();">Close This Window</A></TD>
    </TR>
  </TBODY>
</TABLE>

</DIV>


</BODY>
</HTML>
