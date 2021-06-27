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

<%-- This scriplet must be included to track member login state --%>
<%@ include file="/includes/LoginStatus.jsp" %>

</HEAD>

<%@ page 
import="org.ucnj.paccess.Result, 
        org.ucnj.paccess.PartyName,
        org.ucnj.paccess.SearchOptions,
        org.ucnj.paccess.MiscDetail" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true" 
isErrorPage="false" 
%>

<%
SearchOptions currOpts = userUCPArq.getCurrentSrchOpts();
String formLink = currOpts.getFormLink();

String subTitle = "";
if (formLink.equals("name") == true) {
   subTitle = "Name";
}
else if (formLink.equals("book") == true) {
   subTitle = "Book & Page";
}
else if (formLink.equals("inst") == true) {
   subTitle = "Instrument No.";
}
subTitle += " Details";
%>

<BODY bgcolor="#FFFFFF" vlink="#0000ff" link="#0000ff" alink="#ff0000">

<A name="top">&nbsp;</A>

<DIV ID="main" align="center" STYLE="font-family: arial;">

<TABLE>
  <TR>
    <TD align="center">
      <IMG src="/UCPA/grfx/logoheader3.gif" border="0"><BR>
      <B><FONT face="Arial" size="+2"><%= subTitle %></FONT></B><BR>
    </TD>
  </TR>
  <TR>
    <TD align="center">&nbsp;</TD>
  </TR>
</TABLE>

<%
Boolean found = userUCPArq.getFound();
if (found.booleanValue() == false) {
%>

<BR>
<FONT face="Arial" color="#ff0000" size="+2">Sorry, no results were found.</FONT>
<BR>
<BR>
<A href="/UCPA/DocIndex?s=<%= formLink %>">Back to Search Form</A>
<BR>
<BR>

<%
} else {
   Result mainResult = userUCPArq.getMainResult();
   @SuppressWarnings("unchecked")
   java.util.Vector party1Names = userUCPArq.getParty1();
   @SuppressWarnings("unchecked")
   java.util.Vector party2Names = userUCPArq.getParty2();
   @SuppressWarnings("unchecked")
   java.util.Vector miscDetails = userUCPArq.getMiscDetails();

   String dashPrefix = "-";
   if (mainResult.getInstrPrefix().equals("")) {
      dashPrefix = "";
   }

   String dashSuffix = "-";
   if (mainResult.getInstrSuffix().equals("")) {
      dashSuffix = "";
   }

   String slash = "/";
   if (mainResult.getBook().equals("")) {
	slash = "";
   }

   String instrNumber = "";
   if (mainResult.getInstrPrefix().equals("") == false)
      instrNumber = mainResult.getInstrPrefix() + "-";
   instrNumber += mainResult.getInstrMiddle();
   if (mainResult.getInstrSuffix().equals("") == false)
      instrNumber += "-" + mainResult.getInstrSuffix();

   // pick up the book & page;
   // if it's an UCC or NOS then substitute with the instrument #
   String bookPage = "";
   if (mainResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC)) ||
   	   mainResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_INITIAL)) ||
   	   mainResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_AMENDMENT)) ||
   	   mainResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_ASSIGNMENT)) ||
   	   mainResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_CONTINUATION)) ||
   	   mainResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_TERMINATION)) ||
   	   mainResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_RELEASE)) ||
       mainResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT))) {
      bookPage = instrNumber;
   }
   else {
      if (mainResult.getBook().equals("") == false)
         bookPage = mainResult.getBook() + "/";
      bookPage += mainResult.getPage();
   }
%>

<%-- single cell table used for centering content --%>
<TABLE>
  <TBODY>
    <TR>
      <TD width="410">
      <FONT size="+1"><B><%= mainResult.getDocTypeDesc() %></B></FONT>
      <BR>
      <HR width="410" size="1" align="left">
      <TABLE width="410" border="0" cellpadding="0" cellspacing="0">
        <TBODY>
          <TR>
            <TD width="111"><B>Instrument #</B></TD>
            <TD width="71"><B>Book/Page</B></TD>
            <TD width="82"><B>Recorded</B></TD>
            <TD rowspan="2" width="100" style="text-align: center; vertical-align:middle;">
            	<A href="RequestList?m=" style="text-decoration:none;">
					<IMG src="grfx/certified.png" title="Request Certified Copy" alt="Request Certified Copy" style="border-width: 0;">
            	</A>
<% if (mainResult.getImageID().length() > 0) { %>
              <A href="/UCPA/Viewer?id=<%= mainResult.getImageID() %>&dt=<%= mainResult.getDocTypeDesc() %>&in=<%= instrNumber %>&bp=<%= bookPage %>" onclick="waitOn()" style="text-decoration:none;">
                 <IMG src="/UCPA/grfx/pdf.png" title="View PDF document" alt="View PDF document" style="border-width: 0;">
              </A>
<% } else { %>
              <IMG src="grfx/pdf_blank.png" style="border-width: 0;">
<% } %>
            </TD>
          </TR>
          <TR>
            <TD width="111"><%= mainResult.getInstrPrefix() %><%= dashPrefix %><%= mainResult.getInstrMiddle() %><%= dashSuffix %><%= mainResult.getInstrSuffix() %></TD>
            <TD width="71"><%= mainResult.getBook() %><%= slash %><%= mainResult.getPage()%></TD>
            <TD width="82"><%= mainResult.getStampDate() %></TD>
          </TR>
        </TBODY>
      </TABLE>
      <%
if (miscDetails.size() > 0)
%>
      <HR width="410" size="1" align="left">
<%
for (int i = 0; i < miscDetails.size(); i++) {
   MiscDetail aDetail = (MiscDetail)miscDetails.elementAt(i);

   if (aDetail.getSeqNumber() == 1) {
%>
      <B><%= aDetail.getDescription() %></B>
<%
      if (aDetail.getColumn() != 2) out.print("<BR>");
   } // if (aDetail.getSeqNumber() == 1)

   if (aDetail.getData().length() > 0) {
%>
      <%= aDetail.getData() %>
<%
      if (aDetail.getColumn() != 2) out.print("<BR>");
   } // if (aDetail.getData().length() > 0)
} // miscDetails loop
%>
      <HR width="410" size="1" align="left">
      <FONT face="Arial" size="+1"><B><%= mainResult.getParty1Name() %></B></FONT>
      <BR>
<%
for (int i = 0; i < party1Names.size(); i++) {
   PartyName aParty = (PartyName)party1Names.elementAt(i);

   String comma = ", ";
   if (aParty.getFirstName().equals("") ||
       aParty.getCorpType().equals("C") || 
       aParty.getCorpType().equals("N") || 
       aParty.getCorpType().equals("R") || 
       aParty.getCorpType().equals("U")) {
	comma = "";
   }
%>
      <FONT face="Arial"><%= aParty.getLastName() %><%= comma %><%= aParty.getFirstName() %></FONT>
      <BR>
<%
} // party loop

// print if there are reverse parties
if (party2Names.size() > 0) {
%>

      <FONT face="Arial" size="+1"><B><%= mainResult.getParty2Name() %></B></FONT>
      <BR>
<%
   for (int i = 0; i < party2Names.size(); i++) {
      PartyName aParty = (PartyName)party2Names.elementAt(i);

      String comma = ", ";
      if (aParty.getFirstName().equals("") ||
          aParty.getCorpType().equals("C") || 
          aParty.getCorpType().equals("N") || 
          aParty.getCorpType().equals("R") || 
          aParty.getCorpType().equals("U")) {
	   comma = "";
      }
%>
      <FONT face="Arial"><%= aParty.getLastName() %><%= comma %><%= aParty.getFirstName() %></FONT>
      <BR>
<%
   } // party loop
} // if (party2Names.size() > 0)
%>

      <HR width="410" size="1" align="left">
      </TD>
    </TR>
  </TBODY>
</TABLE>
<%-- single cell table used for centering content --%>

<A href="/UCPA/DocIndex?s=<%= formLink %>">Back to Search Form</A>

<%
} // if results have been returned
%>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="/includes/LinkBar.jsp" %>

</DIV>

<jsp:include page="/includes/wait.html" flush="true" />
<SCRIPT language="JavaScript" src="/UCPA/scripts/wait.js">
</SCRIPT>

</BODY>
</HTML>
