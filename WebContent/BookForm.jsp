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
<SCRIPT language="JavaScript" src="/UCPA/scripts/BookForm.js">
</SCRIPT>

</HEAD>

<BODY vlink="#006633" link="#006633" alink="#990000" background="grfx/wood1.gif" onload="formFocus();">

<%@ page 
import="org.ucnj.paccess.SearchOptions" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true" 
isErrorPage="false" 
%>

<%
SearchOptions currOpts = userUCPArq.getCurrentSrchOpts();
String docType = currOpts.getDocTypeBP();
%>
 
<DIV align="center" style="font-family: Arial">

<IMG src="/UCPA/grfx/logoheader2wd.gif" border="0">
<BR>
<IMG src="/UCPA/grfx/bookwd.gif" border="0">&nbsp;&nbsp;<IMG src="/UCPA/grfx/colage2.jpg" border="0">

<FORM method="POST" action="/UCPA/BookDetails" name="searchForm" onSubmit="return validateInput()">

<TABLE width="466" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD align="left" width="210">
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
	        <OPTION value="<%= UCPA.DOC_TYPE_LIS_PENDENS_FORECLOSURE %>">Lis Pendens Foreclosure</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_LIS_PENDENS_DISCHARGE %>">Lis Pendens Discharge</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_LIS_PENDENS_IN_REM %>">Lis Pendens In Rem</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_LIS_PENDENS_IN_REM_DISMISSED %>">Lis Pendens In Rem Dismissed</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_UCC %>">UCC</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_UCC_INITIAL %>">UCC Initial</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_UCC_AMENDMENT %>">UCC Amendment</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_UCC_ASSIGNMENT %>">UCC Assignment</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_UCC_CONTINUATION %>">UCC Continuation</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_UCC_TERMINATION %>">UCC Termination</OPTION>
	        <OPTION value="<%= UCPA.DOC_TYPE_UCC_RELEASE %>">UCC Release</OPTION>
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
      <TD align="right" width="63">
         <FONT face="Arial" size="+0">Book:&nbsp;</FONT>
      </TD>
      <TD width="65">
         <INPUT type="text" size="6" maxlength="5" name="bk" value="<%= currOpts.getBook() %>">
      </TD>
      <TD align="right" width="63">
         <FONT face="Arial" size="+0">Page:</FONT>&nbsp;
      </TD>
      <TD width="65">
         <INPUT type="text" size="6" maxlength="4" name="pg" value="<%= currOpts.getPage() %>">
      </TD>
    </TR>
  </TBODY>
</TABLE>

<TABLE width="466" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD height="40" valign="bottom" width="170">
         <INPUT type="submit" value="Search">
         &nbsp;
         <INPUT type="button" value="  Clear  " onclick="clearAll()">
      </TD>
      <TD colspan="4" align="right" height="40" width="296" valign="bottom">
         &nbsp;
         <!--A href="javascript:void(0)" onclick="popupBoardDates()">Click here for Document Availability Dates</A-->
      </TD>
    </TR>
  </TBODY>
</TABLE>

</FORM>

</DIV>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="includes/LinkBar.jsp" %>

<%-- 
Use settings held in current session to set select option;
This is a mix of JSP and Javascript: the JSP is req'd to pick up on the
session data, while the Javascript is req'd to actually set the controls
as it can't be done in the HTML code above.
--%>
<SCRIPT LANGUAGE="JavaScript">
setDocType(<%= docType %>);
</SCRIPT>

</BODY>
</HTML>
