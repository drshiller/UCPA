
<%@page import="org.ucnj.paccess.UCPA"%>
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


<%@ taglib uri="/WEB-INF/tlds/ucnj-taglib.tld" prefix="pn" %>


<SCRIPT language="JavaScript" src="scripts/RequestList.js">
</SCRIPT>
<SCRIPT language="JavaScript" src="scripts/common.js">
</SCRIPT>

</HEAD>

<BODY background="grfx/wood1.gif" bgcolor="#FFFFFF" vlink="#006633" link="#006633" alink="#993300" onload="formFocus();">

<%@ page 
import="org.ucnj.paccess.Result,
        org.ucnj.paccess.dataaccess.beans.Request,
        org.ucnj.paccess.dataaccess.beans.OrderItem,
        org.ucnj.paccess.Address,
        org.ucnj.paccess.SearchOptions" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<%
String audience = application.getInitParameter("audience");
Request currRequest = userUCPArq.getCurrentRequest();
java.util.Hashtable requestList = currRequest.getList();
Address currAddress = currRequest.getAddress();
%>

<DIV style="font-family: arial;" align="center">

<IMG src="grfx/logoheader3wd.gif" border="0">
<BR>
<B><FONT size="+2">Request List</FONT></B>
<BR>

<FORM method="POST" action="RequestList" name="reqForm" onSubmit='return validateInput()'>

<%-- single cell table used for centering content --%>
<TABLE width="776" border="0">
  <TBODY>
    <TR>
      <TD width="776" align="center">

      <!-- Results Table -->
      
      <!-- end of results table -->
      
      <!-- list controls table -->
      <table cellpadding="0" cellspacing="0" width="774" border="0">
        <tbody>

          <!-- headings -->
          <tr bgColor="#006633">
            <td class="hd" width="88" align="left">Instrument</td>
            <td class="hd" width="117" align="left">Document</td>
            <td class="hd" width="74" align="left">Recorded</td>
            <td class="hd" width="74" align="left">&nbsp;</td>
            <td class="hd" width="222">&nbsp;</td>
            <td class="hd" width="30" align="center">#</td>
            <td class="hd" width="100" align="center"># Certified</td>
            <td class="hd" width="68">&nbsp;</td>
          </tr>
          <tr bgColor="#006633">
            <td class="hd" width="88" align="left">Number</td>
            <td class="hd" width="117" align="left">Type</td>
            <td class="hd" width="74" align="left">Date</td>
            <td class="hd" width="74" align="left">Book/Page</td>
            <td class="hd" width="222" align="left">Name</td>
            <td class="hd" width="30" align="center">Pgs</td>
            <td class="hd" width="100" align="center">Copies</td>
            <td class="hd" width="68">&nbsp;</td>
          </tr>

<%
String liteColor = "#ffffcc";
String darkColor = "#99ff99";
String bgColor = liteColor;

java.util.Enumeration listKeys = requestList.keys();
while (listKeys.hasMoreElements()) {
   String itemKey = (String)listKeys.nextElement();
   OrderItem orderItem = (OrderItem)requestList.get(itemKey);
   Result aResult = (Result)orderItem.getResult();
 
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
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_INITIAL)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_AMENDMENT)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_ASSIGNMENT)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_CONTINUATION)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_TERMINATION)) ||
   	   aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_UCC_RELEASE)) ||
       aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT)) == false) {
      bookPage = aResult.getBook() + slash + aResult.getPage();
   }

   if (bgColor.equals(liteColor))
      bgColor = darkColor;
   else
      bgColor = liteColor;

   String nameCert = "c_" + itemKey;
   String valueCert = Integer.toString(orderItem.getNumCertified());
%>
          <tr bgColor="<%= bgColor %>">
            <td class="req" width="88" height="24">
              <%= aResult.getInstrPrefix() %><%= dashPrefix %><%= aResult.getInstrMiddle() %><%= dashSuffix %><%= aResult.getInstrSuffix() %>
            </td>
            <td class="req" width="117"><%= aResult.getDocTypeDesc() %></td>
            <td class="req" width="74"><%= aResult.getStampDate() %></td>
            <td class="req" width="74"><%= bookPage %></td>
            <td class="req" width="222"><%= aResult.getLastName() %><%= comma %><%= aResult.getFirstName() %></td>
            <td class="req" width="30" align="center"><%= aResult.getNumPages().toString() %></td>
            <td width="100" align="center"><input type="text" name="<%= nameCert %>" value="<%= valueCert %>" size="1"></td>
            <td width="68">
              <span style="font-family: arial; font-size: 10px;">
                <a href="RequestList?r=<%= itemKey %>">Remove Entry</a>
              </span>
              <input type="hidden" name="key" value="<%= itemKey %>">
            </td>
          </tr>
<%
} // requestList record while loop
%>

        </tbody>
      </table><TABLE cellpadding="0" cellspacing="0" width="774" border="0">
        <TR>
          <TD height="33" width="160" align="left" valign="bottom">
            <A href="DocIndex?s=copies">Request List Help</A>
          </TD>
          <TD height="33" width="509" align="right" valign="bottom">
            <INPUT type="submit" name="submitRequest" value="Clear All Entries" onClick="nextPage('Clear');">
            <INPUT type="submit" name="submitRequest" value="Update Order and Continue Searching" onClick="nextPage('Update');">
          </TD>
          <TD width="10">&nbsp;</TD>
          <TD height="33" width="55" align="right" valign="bottom">
				&nbsp;
          </TD>
        </TR>
      </TABLE>
      <BR>

<%
if (audience.equals(UCPA.AUDIENCE_EXTERNAL)) {
%>
      <INPUT type="submit" name="submitRequest" value="Order Online" onClick="nextPage('OrderByCard');">
      <INPUT type="submit" name="submitRequest" value="Order by Mail" onClick="nextPage('OrderByMail');">
      <BR><BR>
      <I>
      	We accept credit card payments from Visa and Mastercard only.<br>
      	Your copy(s) will be mailed within 3 to 5 days.
      </I>
<%
} // audience=external
else if (audience.equals(UCPA.AUDIENCE_INTERNAL)) {
%>
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
<%
} // audience=internal
%>
      
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


