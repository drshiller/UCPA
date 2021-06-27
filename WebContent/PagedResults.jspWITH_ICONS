<%@ page 
import="org.ucnj.paccess.UCPA" 
%>

<HTML>

<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
<STYLE type="text/css">
<!--
TD {
  font-family : Arial;
}
-->
</STYLE>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="includes/LoginStatus.jsp" %>

<%@ taglib uri="/WEB-INF/tlds/ucnj-taglib.tld" prefix="pn" %>

<script src="/UCPA/scripts/Date.js" type="text/javascript"></script>

</HEAD>

<%@ page 
import="org.ucnj.paccess.Result,
        org.ucnj.paccess.SearchOptions" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<%
SearchOptions currOpts = userUCPArq.getCurrentSrchOpts();
String formLink = currOpts.getFormLink();

String sType = "";
Integer resultsPerPage = null;
if (formLink.equals("name") == true) {
   resultsPerPage = new Integer(currOpts.getResultsPerPage());
   sType = "Name";
}
else if (formLink.equals("date") == true) {
   resultsPerPage = new Integer(currOpts.getResultsPerPageDR());
   sType = "Date";
}

Integer defResultsPerPage = UCPA.getDefaultResultsPerPage();

java.util.Vector results = userUCPArq.getResults();
Integer thisPage = userUCPArq.getPageNum();
Boolean moreRecords = userUCPArq.getMore();
%>

<BODY onLoad="waitOff()" background="grfx/wood1.gif" bgcolor="#FFFFFF" vlink="#006633" link="#006633" alink="#993300">

<A name="top">&nbsp;</A>

<DIV ID="main" align="center" STYLE="font-family: arial;">

<TABLE width="755" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD width="378" align="left"><IMG src="grfx/logoheader3wd.gif" border="0"></TD>
      <TD width="377" align="right"><B><FONT size="+2">Search by <%= sType %> Results</FONT></B></TD>
    </TR>
  </TBODY>
</TABLE>

<%
// if no results were found then include some search hints
if (results.size() == 0) { 
%>

<!-- message with hints -->
<BR>
<BR>
<%@ include file="includes/searchHints.html" %>
<BR>
<BR>
<A href="DocIndex?s=<%= sType %>">Back to <%= sType %> Search Form</A>
<BR>
<BR>

<%
// else results found so display then
} else { 
%>

<%-- single cell table used for centering content --%>
<TABLE width="755" border="0">
  <TBODY>
    <TR>
      <TD width="755">

      <%-- Top Page Navigation Table --%>
<% if (resultsPerPage.intValue() > defResultsPerPage.intValue()) { %>
      <BR>
      <pn:pageNav thisPage="<%= thisPage.intValue() %>" 
                  morePages="<%= moreRecords.booleanValue() %>" 
                  resultsPerPage="<%= resultsPerPage.intValue() %>"
                  searchForm="<%= sType %>" />
      <BR>
<% } %>

      <%-- Legend Table --%>
      <TABLE width="755" cellpadding="0" cellspacing="0" 
      	     style="border: 1px solid black; background-color:#f3f3f3;">
        <TBODY>
          <TR>
            <TD width="86" valign="top" rowspan="2"><FONT color="#000000">Document</FONT></TD>
            <TD width="84" valign="top" rowspan="2"><FONT color="#000000">Party</FONT></TD>
            <TD width="420" valign="top"><B><FONT color="#006633">Search Name</FONT></B></TD>
            <TD width="165"><B>Recorded Date</B></TD>
          </TR>
          <TR>
            <TD width="420"><FONT color="#000000">First Reverse Party Type and Name</FONT></TD>
            <TD width="165" valign="top"><FONT color="#660000"><B>Book/Page or File #</B></FONT></TD>
          </TR>
        </TBODY>
      </TABLE>
      <BR>

<%
for (int idx = 0; idx < results.size(); idx++) {
   Result aResult = (Result)results.elementAt(idx);

   String partyType = "";
   String partyTypeRev = "";
   if (aResult.getPartyType().equals("1")) {
      partyType = aResult.getParty1Name();
      partyTypeRev = aResult.getParty2Name();
   }
   else {
      partyType = aResult.getParty2Name();
      partyTypeRev = aResult.getParty1Name();
   }

   if (aResult.getLastNameRev().length() == 0) {
      partyTypeRev = "";
   }
 
   String comma = ", ";
   if (aResult.getFirstName().equals("") || 
       aResult.getCorpType().equals("C") || 
       aResult.getCorpType().equals("N") || 
       aResult.getCorpType().equals("R") || 
       aResult.getCorpType().equals("U")) {
	comma = "";
   }

   String commaRev = ", ";
   if (aResult.getFirstNameRev().equals("") || 
       aResult.getCorpTypeRev().equals("C") || 
       aResult.getCorpTypeRev().equals("N") || 
       aResult.getCorpTypeRev().equals("R") || 
       aResult.getCorpTypeRev().equals("U")) {
	commaRev = "";
   }

   String instrNumber = "";
   if (aResult.getInstrPrefix().equals("") == false)
      instrNumber = aResult.getInstrPrefix() + "-";
   instrNumber += aResult.getInstrMiddle();
   if (aResult.getInstrSuffix().equals("") == false)
      instrNumber += "-" + aResult.getInstrSuffix();

   // pick up the book & page;
   // if it's an UCC or NOS then substitute with the instrument #
   String bookPage = "";
   if (aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_INITIAL)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_AMENDMENT)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_ASSIGNMENT)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_CONTINUATION)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_TERMINATION)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_RELEASE)) ||
       aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT))) {
      bookPage = instrNumber;
   }
   else {
      if (aResult.getBook().equals("") == false)
         bookPage = aResult.getBook() + "/";
      bookPage += aResult.getPage();
   }
%>
      <%-- Results Table --%>
      <TABLE width="755" cellpadding="0" cellspacing="0" border="0">
        <TBODY>
          <TR>

            <TD width="86" rowspan="2" valign="top" align="left">
              <%= aResult.getDocTypeDesc() %>
            </TD>
            <TD width="84" rowspan="2" valign="top" align="left">
              <FONT color="#000000"><%= partyType %></FONT></TD>
            <TD width="420" colspan="2" valign="top">
               <B><FONT color="#0000ff">
               <A href="NameDetails?dt=<%= aResult.getDocType() %>&sd=<%= aResult.getStampDate() %>&ip=<%= aResult.getInstrPrefix() %>&im=<%= aResult.getInstrMiddle() %>&is=<%= aResult.getInstrSuffix() %>">
                 <%= aResult.getLastName() %><%= comma %><%= aResult.getFirstName() %>
               </A>
               </FONT></B>
            </TD>
            <TD width="94">
              <B><%= aResult.getStampDate() %></B>
            </TD>

            <TD width="71" rowspan="2" style="text-align: center; vertical-align: middle;">
              <A href="RequestList?i=<%= idx %>" style="text-decoration:none;">
                <IMG src="grfx/certified.png" title="Request Certified Copy" alt="Request Certified Copy" style="border-width: 0;">
              </A>
<% if (aResult.getImageID().length() > 0) { %>
              <A href="Viewer?id=<%= aResult.getImageID() %>&dt=<%= aResult.getDocTypeDesc() %>&in=<%= instrNumber %>&bp=<%= bookPage %>" onclick="waitOn()" style="text-decoration:none;">
                <IMG src="grfx/pdf.png" title="View PDF document" alt="View PDF document" style="border-width: 0;">
              </A>
<% } else { %>
              <IMG src="grfx/pdf_blank.png" style="border-width: 0;">
<% } %>
            </TD>

          </TR>
          <TR>
            <TD width="85" valign="top"><FONT color="#000000"><%= partyTypeRev %></FONT></TD>
            <TD width="335" valign="top"><FONT color="#000000"><%= aResult.getLastNameRev() %><%= commaRev %><%= aResult.getFirstNameRev() %></FONT></TD>
            <TD width="94" valign="top"><FONT color="#660000"><B><%= bookPage %></B></FONT></TD>
          </TR>
        </TBODY>
      </TABLE>
      <HR width="755" align="left" size="1">
      <%
} // results record for loop
%>

        <%-- Bottom Page Navigation Table --%>
        <pn:pageNav thisPage="<%= thisPage.intValue() %>" 
                    morePages="<%= moreRecords.booleanValue() %>" 
                    resultsPerPage="<%= resultsPerPage.intValue() %>"
                    searchForm="<%= sType %>" />

      </TD>
    </TR>
  </TBODY>
</TABLE>
<%-- single cell table used for centering content --%>

<%
} // if results have been returned
%>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="includes/LinkBar.jsp" %>

</DIV>

<jsp:include page="includes/wait.html" flush="true" />
<SCRIPT language="JavaScript" src="scripts/wait.js">
</SCRIPT>

</BODY>
</HTML>
