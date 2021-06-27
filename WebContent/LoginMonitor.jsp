<HTML>

<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="refresh" content="60">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
<STYLE type="text/css">
<!--
TD{
  font-family : Arial;
}
TD.mem{
  font-size: 12px;
}
TD.hd{
  font-size: 12px;
  font-weight: bold;
  color: #ffffff;
}
-->
</STYLE>

</HEAD>

<BODY background="grfx/wood1.gif" bgcolor="#FFFFFF" vlink="#006633" link="#006633" alink="#993300">

<%@ page 
import="org.ucnj.paccess.AcctMember,
        org.ucnj.paccess.LoggedIn" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<%
java.util.Hashtable members = null;
LoggedIn loggedIn = (LoggedIn)getServletContext().getAttribute("LoggedIn");
if (loggedIn != null)
   members = loggedIn.getMembers();
%>

<DIV style="font-family: arial;" align="center">

<IMG src="grfx/logoheader3wd.gif" border="0">
<BR>
<B><FONT size="+2">Logged In Members</FONT></B>
<BR>

<%-- single cell table used for centering content --%>
<TABLE cellpadding="0" cellspacing="0" width="80" border="0" align="center">
  <TBODY>
    <TR>
      <TD width="200">

      <!-- Members Table -->
      <TABLE cellpadding="0" cellspacing="0" width="80" border="0">
        <TBODY>

          <!-- headings -->
          <TR bgColor="#006633">
            <TD class="hd" width="80" align="center">Name</TD>
          </TR>

<%
String liteColor = "#ffffcc";
String darkColor = "#99ff99";
String bgColor = liteColor;

java.util.Enumeration memberKeys = members.keys();
while (memberKeys.hasMoreElements()) {
   String itemKey = (String)memberKeys.nextElement();
   AcctMember member = (AcctMember)members.get(itemKey);

   if (bgColor.equals(liteColor))
      bgColor = darkColor;
   else
      bgColor = liteColor;

%>
          <TR bgColor="<%= bgColor %>">
            <TD class="mem" width="80"><%= member.getName().trim() %></TD>
          </TR>
<%
} // members record while loop
%>

        </TBODY>
      </TABLE>
      <!-- end of results table -->

      </TD>
    </TR>
  </TBODY>
</TABLE>
<%-- single cell table used for centering content --%>

</DIV>

</BODY>
</HTML>


