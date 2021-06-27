<HTML>

<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
<STYLE type="text/css">
<!--
TD{
  font-family : Arial;
}
TD.header{
  font-size: 24px;
}
TD.name{
  font-size: 30px;
}
TD.req{
  font-size: 12px;
}
TD.hd{
  font-size: 12px;
  font-weight: bold;
  color: #000000;
}
TD.addLbl{
  font-size: 12px;
  font-weight: bold;
}
-->
</STYLE>

</HEAD>

<BODY bgcolor="#FFFFFF" vlink="#0000ff" link="#0000ff" alink="#ff0000">

<%@ page 
import="org.ucnj.paccess.Result,
        org.ucnj.paccess.dataaccess.beans.Request,
        org.ucnj.paccess.dataaccess.beans.OrderItem,
        org.ucnj.paccess.Address" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<jsp:useBean id="userUCPArq" class="org.ucnj.paccess.UCPA" scope="request">
</jsp:useBean>

<%
Request currRequest = userUCPArq.getCurrentRequest();
java.util.Hashtable requestList = currRequest.getList();
Address currAddress = currRequest.getAddress();
%>

<DIV style="font-family: arial;" align="left">

<TABLE align="left" width="650" border="0">

  <TR><TD>
    <TABLE align="center" width="650" border="0">
      <TR>
        <TD width="420" align="left">
          <IMG src="grfx/Seal.jpg" border="0">
        </TD>
        <TD class="header" width="230" align="left">
          Joanne Rajoppi<BR>
          Union County Clerk<BR>
          Public Land Records
        </TD>
      </TR>
    </TABLE>
  </TD></TR>

  <TR><TD height="15" width="650" align="left">&nbsp;</TD></TR>
  <TR><TD class="name" width="650" align="center">DOCUMENT REQUEST FORM</TD></TR>
  <TR><TD height="15" width="650" align="left">&nbsp;</TD></TR>
  <TR><TD width="650" bgcolor="#cccccc">Deliver To:</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>

  <!-- requester's address -->
  <TR><TD width="650" align="left">
    <TABLE cellpadding="0" cellspacing="0" width="305" border="0" align="left">
      <TR>
        <TD class="addLbl" width="75">Name:</TD>
        <TD class="req" width="225"><%= currAddress.getName() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">Address:</TD>
        <TD class="req" width="225"><%= currAddress.getAddress1() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">Address:</TD>
        <TD class="req" width="225"><%= currAddress.getAddress2() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">City:</TD>
        <TD class="req" width="225"><%= currAddress.getCity() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">State:</TD>
        <TD class="req" width="225"><%= currAddress.getState() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">Zipcode:</TD>
        <TD class="req" width="225"><%= currAddress.getZipcode() %></TD>
      </TR>
    </TABLE>
  </TD></TR>

  <TR><TD height="20" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left" bgcolor="#cccccc">Requested Documents:</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>

  <TR><TD width="650" align="left">

    <!-- Results Table -->
    <TABLE cellpadding="0" cellspacing="0" width="650" border="0" align="left">

      <!-- headings -->
      <TR bgColor="#FFFFFF">
        <TD class="hd" width="88" align="left">Instrument #</TD>
        <TD class="hd" width="117" align="left">Document Type</TD>
        <TD class="hd" width="74" align="left">Recorded</TD>
        <TD class="hd" width="74" align="left">Book/Page</TD>
        <TD class="hd" width="197" align="left">Name</TD>
        <TD width="10" align="center">&nbsp;</TD>
        <TD class="hd" width="25" align="center">Pg</TD>
        <TD class="hd" width="30" align="center">Cert</TD>
      </TR>

<%
boolean zeroPgCnt = false;
java.util.Enumeration listKeys = requestList.keys();
while (listKeys.hasMoreElements()) {
   String itemKey = (String)listKeys.nextElement();
   OrderItem listItem = (OrderItem)requestList.get(itemKey);
   Result aResult = (Result)listItem.getResult();
 
   String comma = ", ";
   if (aResult.getFirstName().equals("") || 
       aResult.getCorpType().equals("C") || 
       aResult.getCorpType().equals("N") || 
       aResult.getCorpType().equals("R") || 
       aResult.getCorpType().equals("U")) {
	comma = "";
   }

   String dashPrefix = "-";
   if (aResult.getInstrPrefix().equals("")) {
      dashPrefix = "";
   }

   String dashSuffix = "-";
   if (aResult.getInstrSuffix().equals("")) {
      dashSuffix = "";
   }

   String slash = "/";
   if (aResult.getBook().equals("")) {
	slash = "";
   }

   // pick up the book & page;
   // if it's an UCC or NOS then leave blank
   String bookPage = "";
   if (aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC)) == false &&
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_INITIAL)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_AMENDMENT)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_ASSIGNMENT)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_CONTINUATION)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_TERMINATION)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_RELEASE)) ||
       aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT)) == false) {
      bookPage = aResult.getBook() + slash + aResult.getPage();
   }

   String note = "";
   if (aResult.getNumPages().intValue() == 0) {
      note = "*";
      zeroPgCnt = true;
   }

%>
      <TR>
        <TD class="req" width="88" align="left" valign="top">
          <%= aResult.getInstrPrefix() %><%= dashPrefix %><%= aResult.getInstrMiddle() %><%= dashSuffix %><%= aResult.getInstrSuffix() %>
        </TD>
        <TD class="req" width="117" align="left" valign="top"><%= aResult.getDocTypeDesc() %></TD>
        <TD class="req" width="74" align="left" valign="top"><%= aResult.getStampDate() %></TD>
        <TD class="req" width="74" align="left" valign="top"><%= bookPage %></TD>
        <TD class="req" width="197" align="left" valign="top"><%= aResult.getLastName() %><%= comma %><%= aResult.getFirstName() %></TD>
        <TD class="req" width="10" align="right" valign="top"><SPAN style="color: red;"><%= note %></SPAN></TD>
        <TD class="req" width="25" align="center" valign="top"><%= aResult.getNumPages().toString() %></TD>
        <TD class="req" width="30" align="center" valign="top"><%= Integer.toString(listItem.getNumCertified()) %></TD>
      </TR>
<%
} // requestList record while loop
%>

    </TABLE>
    <!-- end of results table -->

  </TD></TR>

<% if (zeroPgCnt == true) { %>
  <TR><TD height="3" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left">
    <SPAN style="color: red;">* In the event that any of the documents do not register the number of
      pages, you may send a separate check for each of these documents. Please
      state &quot;Not to exceed $40.00&quot; on each check beneath the payline
      and make the check payable to &quot;Union County Clerk&quot;. You will
      need to leave the pay box on the check blank.</SPAN>
  </TD></TR>
<% } %>

  <TR><TD height="20" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left" bgcolor="#cccccc">
    Please send this form with a check or money order of 
    <B><%= currRequest.costString() %></B>
    payable to Union County Clerk to the address shown below.
    Copies will be mailed to you within 3 to 5 business days.
  </TD></TR>
  <TR><TD height="3" width="650" ></TD></TR>
  <TR><TD width="650" align="left">
    Union County Clerk's Office<BR>
    2 Broad Street<BR>
    Room 115<BR>
    Elizabeth, NJ 07207<BR>(908) 527-4787
		</TD></TR>
  <TR><TD height="30" width="650" ></TD></TR>

  <TR><TD height="3" width="650" align="left"></TD></TR>
  <TR><TD height="15" width="650" align="left">Return to <a href="javascript:history.back(1)">Previous Page</a></TD></TR>

</TABLE>

</DIV>

</BODY>
</HTML>


