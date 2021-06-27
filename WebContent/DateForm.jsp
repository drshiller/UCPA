<%@page import="org.ucnj.paccess.UCPA"%>
<% String users = application.getInitParameter("users"); %>

<HTML>

<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="/UCPA/theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="includes/LoginStatus.jsp" %>

<SCRIPT language="JavaScript" src="/UCPA/scripts/common.js">
</SCRIPT>
<SCRIPT language="JavaScript" src="/UCPA/scripts/Date.js">
</SCRIPT>
<SCRIPT language="JavaScript" src="/UCPA/scripts/DateForm.js">
</SCRIPT>

</HEAD>

<BODY vlink="#006633" link="#006633" alink="#990000" background="/UCPA/grfx/wood1.gif" onload="formFocus();">

<%@ page 
import="org.ucnj.paccess.SearchOptions" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true" 
isErrorPage="false" 
%>

<%
SearchOptions currOpts = userUCPArq.getCurrentSrchOpts();
String docType = currOpts.getDocTypeDR();
String fromDate = currOpts.getFromDateDR();
String toDate = currOpts.getToDateDR();
String resultsPerPage = currOpts.getResultsPerPageDR();
String pOpt = currOpts.getPartyOptDR();
if (resultsPerPage.equals("")) resultsPerPage = UCPA.getDefaultResultsPerPage().toString();
%>

<DIV style="font-family: arial;" align="center">

<IMG src="/UCPA/grfx/logoheader2wd.gif" border="0">
<BR>
<IMG src="/UCPA/grfx/datewd.gif" border="0">&nbsp;&nbsp;<IMG src="/UCPA/grfx/colage1.jpg" border="0">

<FORM method="POST" action="/UCPA/DateResults" name="searchForm" onSubmit="return validateInput()">

<TABLE width="657" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD align="left" width="170">
	  	<SELECT name="dt">
			<OPTION value="<%= UCPA.DOC_TYPE_NONE %>" selected>&lt;Choose Document Type&gt;</OPTION>
<%
if (users.equals(UCPA.USERS_PUBLIC)) {
%>
			<OPTION value="<%= UCPA.DOC_TYPE_DEED %>">Deed</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_MORTGAGE %>">Mortgage</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_MORTGAGE_CANCELLATION %>">Mortgage Cancellation</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_ASSIGNMENT %>">Assignment</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_DISCHARGE %>">Discharge</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_RELEASE %>">Release</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT %>">Notice of Settlement</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_INHERITANCE_TAX_WAIVERS %>">Inheritance Tax Waivers</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_FEDERAL_TAX_LIEN %>">Federal Tax Lien</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_FEDERAL_TAX_LIEN_RELEASE %>">Fed Tax Release</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_LIS_PENDENS %>">Lis Pendens</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_LIS_PENDENS_FORECLOSURE %>">Foreclosure</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_UCC %>">UCC</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_TRADE_NAMES %>">Trade Name</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_TRADE_NAMES_DISSOLUTION %>">Trade Name Dissolution</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_NUB %>">Notice of Unpaid Bal</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_NUB_DISCHARGE %>">NUB Discharge</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_NUB_ADMENDENT %>">NUB Amendment</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN %>">Construction Lien</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN_ADMENDMENT %>">Const Lien Amend</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN_DISCHARGE %>">Const Lien Discharge</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN_BOND_RELEASE %>">Const Lien Bond Rel</OPTION>
			<OPTION value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN_PARTIAL_DISCHARGE %>">Const Lien Partial Rel</OPTION>
<%
} // users=public
else if (users.equals(UCPA.USERS_STAFF)) {
%>
	        <OPTION selected value="<%= UCPA.DOC_TYPE_NOTARY_CERTIFICATION %>">Notary Certification</OPTION>
<%
} // users=staff
%>
		</SELECT>
      </TD>
      <TD align="right" width="53"><SPAN STYLE="font-family: Arial">From:</SPAN></TD>
      <TD align="left" width="50"><INPUT type="text" size="10" name="from" value="<%= fromDate %>" onblur="convertDate(from, false)"></TD>
      <TD align="right" width="32"><SPAN STYLE="font-family: Arial">To:</SPAN></TD>
      <TD align="left" width="50"><INPUT type="text" size="10" name="to" value="<%= toDate %>" onblur="convertDate(to, false)"></TD>
      <TD align="right" width="135"><SPAN STYLE="font-family: Arial">Results Per Page:</SPAN></TD>
      <TD align="left" width="55"><INPUT type="text" size="4" name="rpp" value="<%= resultsPerPage %>"></TD>
    </TR>
  </TBODY>
</TABLE>
<TABLE width="657" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD width="80" height="40" valign="bottom">Party Type:</TD>
      <TD valign="bottom" align="left" height="22" width="80" height="40">
        <INPUT type="radio" name="pOpt" value="1"><FONT face="Arial" size="+0">Grantor</FONT>
      </TD>
      <TD valign="bottom" align="left" height="22" width="" height="40">
        <INPUT type="radio" name="pOpt" value="2"><FONT face="Arial" size="+0">Grantee</FONT>
      </TD>
    </TR>
  </TBODY>
</TABLE>
<TABLE width="657" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD height="40" valign="bottom" width="360">
         <INPUT type="submit" value="Search">&nbsp;
         <INPUT type="button" value="  Clear  " onclick="clearAll('', '', '<%= UCPA.getDefaultResultsPerPage().toString() %>')">
      </TD>
      <TD align="right" height="40" width="297" valign="bottom">
         &nbsp;
         <!--A href="javascript:void(0)" onclick="popupBoardDates()">Click here for Document Availability Dates</A-->
      </TD>
    </TR>
  </TBODY>
</TABLE>

</FORM>

</DIV>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="/includes/LinkBar.jsp" %>

<%-- 
Use settings held in current session to set select option;
This is a mix of JSP and Javascript: the JSP is req'd to pick up on the
session data, while the Javascript is req'd to actually set the controls
as it can't be done in the HTML code above.
--%>
<SCRIPT LANGUAGE="JavaScript">
setDocType(<%= docType %>);
set_pOpt("<%= pOpt %>");
</SCRIPT>

</BODY>
</HTML>
