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
TD.req{
  font-size: 12px;
}
TD.hd{
  font-size: 12px;
  font-weight: bold;
  color: #ffffff;
}
TD.addLbl{
  font-size: 14px;
  font-weight: bold;
}
-->
</STYLE>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="includes/LoginStatus.jsp" %>

<SCRIPT language="JavaScript" src="scripts/RequestList.js">
</SCRIPT>
<SCRIPT language="JavaScript" src="scripts/common.js">
</SCRIPT>

</HEAD>

<BODY background="grfx/wood1.gif" bgcolor="#FFFFFF" vlink="#006633" link="#006633" alink="#993300" onload="formFocus();">

<%@ page 
import="org.ucnj.paccess.*,
        org.ucnj.paccess.dataaccess.beans.*" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<%
Request currRequest = userUCPArq.getCurrentRequest();
@SuppressWarnings("unchecked")
java.util.Hashtable requestList = currRequest.getList();

// full pkg req'd to avoid conflict with jsp name
org.ucnj.paccess.Address currAddress = currRequest.getAddress();
%>

<DIV style="font-family: arial;" align="center">

<IMG src="grfx/logoheader3wd.gif" border="0">
<BR>
<B><FONT size="+2">Request List -- Order by Mail</FONT></B>
<BR>

<FORM method="POST" action="RequestList" name="reqForm" onSubmit='return validateAddress()'>

<%-- single cell table used for centering content --%>
<TABLE width="776" border="0">
  <TBODY>
    <TR>
      <TD width="776" align="center">

      <!-- Results Table -->
      <TABLE cellpadding="0" cellspacing="0" width="774" border="0">
        <TBODY>

          <!-- headings -->
          <TR bgColor="#006633">
            <TD class="hd" width="88" align="left">Instrument</TD>
            <TD class="hd" width="117" align="left">Document</TD>
            <TD class="hd" width="74" align="left">Recorded</TD>
            <TD class="hd" width="74" align="left">&nbsp;</TD>
            <TD class="hd" width="222">&nbsp;</TD>
            <TD class="hd" width="30" align="center">#</TD>
            <TD class="hd" width="100" colspan="2" align="center"># of Copies</TD>
            <TD class="hd" width="68">&nbsp;</TD>
          </TR>
          <TR bgColor="#006633">
            <TD class="hd" width="88" align="left">Number</TD>
            <TD class="hd" width="117" align="left">Type</TD>
            <TD class="hd" width="74" align="left">Date</TD>
            <TD class="hd" width="74" align="left">Book/Page</TD>
            <TD class="hd" width="222" align="left">Name</TD>
            <TD class="hd" width="30" align="center">Pgs</TD>
            <TD class="hd" width="50" align="center">Cert</TD>
            <TD class="hd" width="68">&nbsp;</TD>
          </TR>

<%
String liteColor = "#ffffcc";
String darkColor = "#99ff99";
String bgColor = liteColor;

@SuppressWarnings("unchecked")
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
   String bookPage = "&nbsp;";
   if (aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC)) == false &&
       aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT)) == false) {
      bookPage = aResult.getBook() + slash + aResult.getPage();
   }

   if (bgColor.equals(liteColor))
      bgColor = darkColor;
   else
      bgColor = liteColor;

   String valueCert = Integer.toString(listItem.getNumCertified());
%>
          <TR bgColor="<%= bgColor %>">
            <TD class="req" width="88" height="24">
              <%= aResult.getInstrPrefix() %><%= dashPrefix %><%= aResult.getInstrMiddle() %><%= dashSuffix %><%= aResult.getInstrSuffix() %>
            </TD>
            <TD class="req" width="117"><%= aResult.getDocTypeDesc() %></TD>
            <TD class="req" width="74"><%= aResult.getStampDate() %></TD>
            <TD class="req" width="74"><%= bookPage %></TD>
            <TD class="req" width="222"><%= aResult.getLastName() %><%= comma %><%= aResult.getFirstName() %></TD>
            <TD class="req" width="30" align="center"><%= aResult.getNumPages().toString() %></TD>
            <TD class="req" width="50" align="center"><%= valueCert %></TD>
            <TD width="68">&nbsp;</TD>
          </TR>
<%
} // requestList record while loop
%>

        </TBODY>
      </TABLE>
      <!-- end of results table -->

      <!-- control buttons for results list -->
      <TABLE cellpadding="0" cellspacing="0" width="774" border="0">
        <TR>
          <TD height="33" width="160" align="left" valign="bottom">
            <A href="DocIndex?s=copies">Request List Help</A>
          </TD>
          <TD height="33" width="479" align="right" valign="bottom">&nbsp;</TD>
          <TD width="10">&nbsp;</TD>
          <TD height="33" width="85" align="right" valign="bottom">
            <A href="javascript:history.back(1)">Back</A>
          </TD>
        </TR>
      </TABLE>
      
      The total payment due for this order is <b><%= currRequest.costString() %></b>.
      <BR><BR>

      <!-- address details -->
      <TABLE cellspacing="0" border="0" width="325" class="border">
        <TR><TD valign="top">
          <TABLE cellpadding="4" cellspacing="0" width="100%" border="0" bgColor="<%= liteColor %>">
            <TR>
              <TD colspan="2" align="center" class="addLbl">Please enter your name and address below:</TD>
            </TR>
             <TR>
              <TD class="addLbl" width="75">Name:</TD>
              <TD><INPUT type="text" name="name" value="<%= currAddress.getName() %>" size="30"></TD>
            </TR>
            <TR>
              <TD class="addLbl" width="75">Address:</TD>
              <TD><INPUT type="text" name="add1" value="<%= currAddress.getAddress1() %>" size="30"></TD>
            </TR>
            <TR>
              <TD class="addLbl" width="75">Address:</TD>
              <TD><INPUT type="text" name="add2" value="<%= currAddress.getAddress2() %>" size="30"></TD>
            </TR>
            <TR>
              <TD class="addLbl" width="75">City:</TD>
              <TD><INPUT type="text" name="city" value="<%= currAddress.getCity() %>" size="30"></TD>
            </TR>
            <TR>
              <TD class="addLbl" width="75">State:</TD>
              <TD><INPUT type="text" name="state" value="<%= currAddress.getState() %>" size="5"></TD>
            </TR>
            <TR>
              <TD class="addLbl" width="75">Zipcode:</TD>
              <TD><INPUT type="text" name="zipcode" value="<%= currAddress.getZipcode() %>" size="5"></TD>
            </TR>
         </TABLE>
        </TD></TR>
      </TABLE>
      <BR>
      <INPUT type="submit" name="submitRequest" value="Go to Print Form" onClick="nextPage('Print');">

      </TD>
    </TR>
  </TBODY>
</TABLE>
<%-- single cell table used for centering content --%>

<INPUT type="hidden" name="next" value="">
</FORM>

</DIV>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="includes/LinkBar.jsp" %>

</BODY>
</HTML>


