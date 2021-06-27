<%@page import="org.ucnj.paccess.UCPA"%>
<% String users = application.getInitParameter("users"); %>

<HTML>

<HEAD> 

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="includes/LoginStatus.jsp" %>

<SCRIPT language="JavaScript" src="scripts/common.js">
</SCRIPT>
<SCRIPT language="JavaScript" src="scripts/NameForm.js">
</SCRIPT>
<SCRIPT language="JavaScript" src="scripts/Date.js">
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

String lOpt = currOpts.getLastNameOpt();
String fOpt = currOpts.getFirstNameOpt();
String pOpt = currOpts.getPartyOpt();
String[] docTypes = currOpts.getDocTypes();

String fromDate = currOpts.getFromDate();
if (fromDate.equals("")) fromDate = UCPA.getInitialDate();
String toDate = currOpts.getToDate();
if (toDate.equals("")) toDate = userUCPArq.getCurrentIndexDate(false);

String resultsPerPage = currOpts.getResultsPerPage();
if (resultsPerPage.equals("")) resultsPerPage = UCPA.getDefaultResultsPerPage().toString();
%>

<DIV align="center">

<IMG src="grfx/logoheader2wd.gif" width="500" height="40" border="0">
<BR>
<TABLE width="610" border="0" cellpadding="0" cellspacing="0">
  <TBODY>
    <TR>
      <TD valign="middle" align="left" width="159"><IMG src="grfx/namewd.gif" width="159" height="26" border="0"></TD>
      <TD valign="middle" align="right" width="451"><IMG src="grfx/colage1.jpg" width="400" border="0"></TD>
    </TR>
  </TBODY>
</TABLE>

<FORM method="POST" action="NameResults" name="searchForm" onSubmit="return validateInput()">
<TABLE width="612" height="79" cellpadding="0" border="0" cellspacing="1">
  <TBODY>
    <TR>
      <TD width="306" height="79" align="left">
      <TABLE cellpadding="0" cellspacing="0" bgcolor="#006633" width="304" height="22" border="0">
        <TBODY>
          <TR>
            <TD width="304" height="22">
              <FONT color="#FFFFFF" face="Arial" size="+1"><B>&nbsp;Last Name</B></FONT>
              <FONT color="#FFFFFF" size="-2" face="Arial"> -- Required</FONT>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      <TABLE cellpadding="0" cellspacing="0" width="304" height="57" border="0">
        <TBODY>
           <TR>
            <TD bgcolor="#006633" valign="top" align="center" width="304" height="57">
            <CENTER>
            <TABLE width="300" height="55" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" border="0">
              <TBODY>
                <TR>
                  <TD colspan="3" valign="middle" align="center" height="29" width="300">
                    <INPUT type="text" name="lName" value="<%= currOpts.getLastName() %>" maxlength="45" size="33">
                  </TD>
                </TR>
                <TR>
                  <TD valign="middle" align="center" height="26" width="85">
                     <INPUT type="radio" name="lOpt" value="e"><FONT face="Arial" size="+0">Exact</FONT>
                  </TD>
                  <TD valign="middle" align="center" height="26" width="100">
                     <INPUT type="radio" name="lOpt" value="s"><FONT face="Arial" size="+0">Starts with</FONT>
                  </TD>
                  <TD valign="middle" align="center" height="26" width="115">
                     <INPUT type="radio" name="lOpt" value="x"><FONT face="Arial" size="+0">Soundex</FONT>
                  </TD>
                </TR>
              </TBODY>
            </TABLE>
            </CENTER>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      </TD>
      <TD width="306" height="79" align="right">
      <TABLE cellpadding="0" cellspacing="0" bgcolor="#006633" width="304" height="22" border="0">
        <TBODY>
          <TR>
            <TD width="304" height="22">
               <FONT color="#FFFFFF" face="Arial" size="+1"><B>&nbsp;First Name</B></FONT>
               <FONT color="#FFFFFF" size="-2" face="Arial"> -- Optional</FONT>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      <TABLE cellpadding="0" cellspacing="0" width="304" height="57" border="0">
        <TBODY>
          <TR>
            <TD bgcolor="#006633" valign="top" align="center" width="304" height="57">
            <TABLE width="300" height="55" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" border="0">
              <TBODY>
                <TR>
                  <TD colspan="3" valign="middle" align="center" height="29" width="300">
                     <INPUT type="text" name="fName" value="<%= currOpts.getFirstName() %>" maxlength="40" size="33">
                  </TD>
                </TR>
                <TR>
                  <TD valign="middle" align="center" height="26" width="85">
                      <INPUT type="radio" name="fOpt" value="e"><FONT face="Arial" size="+0">Exact</FONT>
                  </TD>
                  <TD valign="middle" align="center" height="26" width="100">
                      <INPUT type="radio" name="fOpt" value="s"><FONT face="Arial" size="+0">Starts with</FONT>
                  </TD>
                  <TD valign="middle" align="center" height="26" width="115">
                      <INPUT type="radio" name="fOpt" value="c"><FONT face="Arial" size="+0">Contains</FONT>
                  </TD>
                </TR>
              </TBODY>
            </TABLE>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      </TD>
    </TR>
  </TBODY>
</TABLE>
<TABLE width="612" height="79" cellpadding="0" border="0" cellspacing="1">
  <TBODY>
    <TR>
      <TD width="306" height="77" align="left">
      <TABLE cellpadding="0" cellspacing="0" bgcolor="#006633" width="304" height="22" border="0">
        <TBODY>
          <TR>
            <TD width="304" height="22">
               <FONT color="#FFFFFF" face="Arial" size="+1"><B>&nbsp;Date Range</B></FONT>
               <FONT color="#FFFFFF" size="-2" face="Arial"> -- Required</FONT></TD>
          </TR>
        </TBODY>
      </TABLE>
      <TABLE cellpadding="0" cellspacing="0" width="304" height="57" border="0">
        <TBODY>
          <TR>
            <TD bgcolor="#006633" valign="top" align="center" width="304" height="57">
            <TABLE width="300" height="55" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" border="0">
              <TBODY>
                <TR>
                  <TD valign="middle" align="right" height="55" width="60">
                      <FONT face="Arial" size="+0">From:&nbsp;</FONT></TD>
                  <TD valign="middle" align="left" height="55" width="90" class="ns4col">
                      <INPUT type="text" size="10" name="from" value="<%= fromDate %>" onblur="convertDate(from, false)">
                  </TD>
                  <TD valign="middle" align="right" height="55" width="45">
                      <FONT face="Arial" size="+0">To:&nbsp;</FONT></TD>
                  <TD valign="middle" align="left" height="55" width="105">
                      <INPUT type="text" size="10" name="to" value="<%= toDate %>" onblur="convertDate(to, false)">
                  </TD>
                </TR>
              </TBODY>
            </TABLE>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      </TD>
      <TD width="306" height="77" align="right">
      <TABLE cellpadding="0" cellspacing="0" bgcolor="#006633" width="304" height="22" border="0">
        <TBODY>
          <TR>
            <TD>
               <FONT color="#FFFFFF" face="Arial" size="+1"><B>&nbsp;Party Type</B></FONT>
               <FONT color="#FFFFFF" size="-2" face="Arial"> -- Required</FONT>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      <TABLE cellpadding="0" cellspacing="0" width="304" height="57" border="0">
        <TBODY>
          <TR>
            <TD bgcolor="#006633" valign="top" align="center" width="304" height="57">
            <TABLE width="300" height="55" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" border="0">
              <TBODY>
                <TR>
                  <TD valign="middle" align="center" height="22" width="80">
                      <INPUT type="radio" name="pOpt" value="3"><FONT face="Arial" size="+0">Both</FONT>
                  </TD>
                  <TD valign="middle" align="center" height="22" width="110">
                      <INPUT type="radio" name="pOpt" value="1"><FONT face="Arial" size="+0">Grantor</FONT>
                  </TD>
                  <TD valign="middle" align="center" height="22" width="110">
                      <INPUT type="radio" name="pOpt" value="2"><FONT face="Arial" size="+0">Grantee</FONT>
                  </TD>
                </TR>
              </TBODY>
            </TABLE>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      </TD>
    </TR>
  </TBODY>
</TABLE>
<%
if (users.equals(UCPA.USERS_PUBLIC)) {
%>	  
<TABLE width="612" cellpadding="0" border="0" cellspacing="1" height="216">
  <TBODY>
    <TR>
      <TD valign="top" align="center" bgcolor="#006633" height="216" width="610">
      <TABLE cellpadding="0" cellspacing="0" width="610" height="22" border="0">
        <TBODY>
          <TR>
            <TD valign="top" align="left" width="610" height="22">
               <FONT color="#FFFFFF" face="Arial" size="+1"><B>&nbsp;&nbsp;Document Types</B></FONT>
               <FONT color="#FFFFFF" size="-2" face="Arial"> -- Select at least one</FONT>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      <TABLE width="606" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" border="0" height="102">
        <TBODY>
          <TR>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_DEED %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Deed</FONT></TD>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_FEDERAL_TAX_LIEN %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Fed Tax Lien</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_NUB %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Notice of Unpaid Bal</FONT></TD>
          </TR>
          <TR>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_MORTGAGE %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Mortgage</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_FEDERAL_TAX_LIEN_RELEASE %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">Fed Tax Release</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_NUB_DISCHARGE %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">NUB Discharge</FONT></TD>
          </TR>
          <TR>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_MORTGAGE_CANCELLATION %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Mortgage Cancellation</FONT></TD>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_LIS_PENDENS %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Lis Pendens</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_NUB_ADMENDENT %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">NUB Amendment</FONT></TD>
          </TR>
          <TR>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_ASSIGNMENT %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Assignment</FONT></TD>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_LIS_PENDENS_FORECLOSURE %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Foreclosure</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">Construction Lien</FONT></TD>
          </TR>
          <TR>
             <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_DISCHARGE %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Discharge</FONT></TD>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_UCC %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">UCC</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN_ADMENDMENT %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">Const Lien Amend</FONT></TD>
          </TR>
          <TR>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_RELEASE %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Release</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_TRADE_NAMES %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">Trade Name</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN_DISCHARGE %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">Const Lien Discharge</FONT></TD>
          </TR>
          <TR>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Notice of Settlement</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_TRADE_NAMES_DISSOLUTION %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">Trade Name Dissolution</FONT></TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN_BOND_RELEASE %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">Const Lien Bond Rel</FONT></TD>
          </TR>
          <TR>
            <TD width="18" height="18"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_INHERITANCE_TAX_WAIVERS %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Inheritance Tax Waivers</FONT></TD>
            <TD colspan="2" width="202" height="18" valign="top">&nbsp;</TD>
            <TD width="18" height="18" valign="top"><INPUT type="checkbox" name="dt" value="<%= UCPA.DOC_TYPE_CONSTRUCTION_LIEN_PARTIAL_DISCHARGE %>"></TD>
            <TD width="184" height="18" valign="top"><FONT face="Arial" size="+0">Const Lien Partial Rel</FONT></TD>
          </TR>
          <TR>
            <TD colspan="6" height="30" valign="middle" width="606">&nbsp;
                <INPUT type="button" value=" Select All " onclick="setAllDocTypes(true)"> &nbsp;
                <INPUT type="button" value="Deselect All" onclick="setAllDocTypes(false)">
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      </TD>
    </TR>
  </TBODY>
</TABLE>
<%
} // users=public
else if (users.equals(UCPA.USERS_STAFF)) {
%>
<TABLE width="612" cellpadding="0" border="0" cellspacing="1" height="46">
  <TBODY>
    <TR>
      <TD valign="top" align="center" bgcolor="#006633" width="610">
      <TABLE cellpadding="0" cellspacing="0" width="610" height="22" border="0">
        <TBODY>
          <TR>
            <TD valign="top" align="left" width="610" height="22">
               <FONT color="#FFFFFF" face="Arial" size="+1"><B>&nbsp;&nbsp;Document Types</B></FONT>
               <FONT color="#FFFFFF" size="-2" face="Arial"> -- Select at least one</FONT>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      <TABLE width="606" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" border="0">
        <TBODY>
          <TR>
            <TD width="18" height="18"><INPUT type="checkbox" checked name="dt" value="<%= UCPA.DOC_TYPE_NOTARY_CERTIFICATION %>"></TD>
            <TD width="184" height="18"><FONT face="Arial" size="+0">Notary Certification</FONT></TD>
            <TD colspan="4" width="404" height="18" valign="top">&nbsp;</TD>
          </TR>
        </TBODY>
      </TABLE>
      </TD>
    </TR>
  </TBODY>
</TABLE>
<%
} // users=staff
%>
<TABLE width="612" cellpadding="0" border="0" cellspacing="1" height="20">
  <TBODY>
    <TR>
      <TD width="130" height="20">
         <FONT face="Arial" size="+0">Results Per Page:</FONT>
      </TD>
      <TD align="left" width="60">
         <INPUT type="text" size="4" name="rpp" value="<%= resultsPerPage %>">
      </TD>
      <TD align="right" width="424" height="20">
         &nbsp;
         <!-- A href="javascript:void(0)" onclick="popupBoardDates()">Click here for Document Availability Dates</A -->
      </TD>
    </TR>
  </TBODY>
</TABLE>
<TABLE width="614" cellpadding="0" border="0" cellspacing="2" height="24">
  <TBODY>
    <TR>
      <TD valign="bottom" width="612" colspan="3" height="22">
          <INPUT type="submit" value="Search"> &nbsp;
          <INPUT type="button" value="  Clear  " onclick="clearAll('<%= UCPA.getInitialDate() %>', '<%= UCPA.todaysDate() %>', '<%= UCPA.getDefaultResultsPerPage().toString() %>')">
      </TD>
    </TR>
  </TBODY>
</TABLE>
</FORM>

</DIV>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="includes/LinkBar.jsp" %>

<%-- 
Use settings held in current session to set radio buttons and check boxes;
This is a mix of JSP and Javascript: the JSP is req'd to pick up on the
session data, while the Javascript is req'd to actually set the controls
as it can't be done in the HTML code above.
--%>
<SCRIPT LANGUAGE="JavaScript">
set_lOpt("<%= lOpt %>");
set_fOpt("<%= fOpt %>");
set_pOpt("<%= pOpt %>");
</SCRIPT>
<%
// Can't pass string arrays to Javascript so create a single string
// with a colon as a separator; Javascript will parse this string
if (docTypes != null) {
   String dt = "";
   for (int i = 0; i < docTypes.length; i++) {
      dt += docTypes[i];
      if (i < (docTypes.length - 1)) {
         dt += ":";
      }
   } // doc type for loop
%>
<SCRIPT LANGUAGE="JavaScript">
setDocType("<%= dt %>");
</SCRIPT>
<%
} // if docTypes != null
%>

</BODY>
</HTML>
