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

#payOnline {
  font-family : Arial;
}
-->
</STYLE>

<%-- This scriptlet must be included to track member login state --%>
<%@ include file="includes/LoginStatus.jsp" %>

<script src="scripts/jquery-1.10.2.min.js"></script>
<script src="scripts/jquery.blockUI.js"></script>
<script src="scripts/RequestList.js"></script>
<script src="scripts/common.js"></script>

</HEAD>

<BODY background="grfx/wood1.gif" bgcolor="#FFFFFF" vlink="#006633" link="#006633" alink="#993300" onload="formFocus();">

<%@ page 
import="org.ucnj.paccess.Result,
        org.ucnj.paccess.dataaccess.beans.Request,
        org.ucnj.paccess.dataaccess.beans.OrderItem" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<%
Request currRequest = userUCPArq.getCurrentRequest();
@SuppressWarnings("rawtypes")
java.util.Hashtable requestList = currRequest.getList();
%>

<DIV style="font-family: arial;" align="center">

<IMG src="grfx/logoheader3wd.gif" border="0">
<BR>
<B><FONT size="+2">Request List -- Order Online</FONT></B>
<BR>

<FORM method="POST" action="RequestList" name="reqForm">

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
            <TD class="hd" width="212">&nbsp;</TD>
            <TD class="hd" width="10">&nbsp;</TD>
            <TD class="hd" width="30" align="center">#</TD>
            <TD class="hd" width="100" colspan="2" align="center"># of Copies</TD>
            <TD class="hd" width="68">&nbsp;</TD>
          </TR>
          <TR bgColor="#006633">
            <TD class="hd" width="88" align="left">Number</TD>
            <TD class="hd" width="117" align="left">Type</TD>
            <TD class="hd" width="74" align="left">Date</TD>
            <TD class="hd" width="74" align="left">Book/Page</TD>
            <TD class="hd" width="212" align="left">Name</TD>
            <TD class="hd" width="10">&nbsp;</TD>
            <TD class="hd" width="30" align="center">Pgs</TD>
            <TD class="hd" width="50" align="center">Cert</TD>
            <TD class="hd" width="68">&nbsp;</TD>
          </TR>

<%
String liteColor = "#ffffcc";
String darkColor = "#99ff99";
String bgColor = liteColor;

boolean zeroPgCnt = false;
@SuppressWarnings("rawtypes")
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

   int numPages = aResult.getNumPages().intValue();
   String valueCert = Integer.toString(listItem.getNumCertified());
   String note = "";
   if (numPages == 0) {
      note = "*";
      zeroPgCnt = true;
   }
%>
          <TR bgColor="<%= bgColor %>">
            <TD class="req" width="88" height="24">
              <%= aResult.getInstrPrefix() %><%= dashPrefix %><%= aResult.getInstrMiddle() %><%= dashSuffix %><%= aResult.getInstrSuffix() %>
            </TD>
            <TD class="req" width="117"><%= aResult.getDocTypeDesc() %></TD>
            <TD class="req" width="74"><%= aResult.getStampDate() %></TD>
            <TD class="req" width="74"><%= bookPage %></TD>
            <TD class="req" width="212"><%= aResult.getLastName() %><%= comma %><%= aResult.getFirstName() %></TD>
            <TD class="req" width="10" align="right" valign="middle"><SPAN style="color: red;"><%= note %></SPAN></TD>
            <TD class="req" width="30" align="center"><%= numPages %></TD>
            <TD class="req" width="50" align="center"><%= valueCert %></TD>
            <TD class="req" width="68">&nbsp;</TD>
          </TR>
<%
} // requestList record while loop
%>

        </TBODY>
      </TABLE>
      <!-- end of results table -->
      
    </TD>
  </TR>      

<% if (zeroPgCnt == true) { %>
  <TR><TD height="3" width="776" align="left"></TD></TR>
  <TR><TD width="776" align="left">
    <SPAN style="color: red;">* Any documents that do not register the number of
      pages will not be included as part of the credit card transaction. Please
      contact the &quot;Union County Clerk&quot; for further information.</SPAN>
  </TD></TR>
<% } %>
      
    <TR>
      <TD width="776" align="center">

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
      <BR>
      <I>Click the button below to pay online via credit card.<BR>
      We accept credit card payments from Visa and Mastercard only.<br>
      Your copy(s) will be mailed within 3 to 5 days.</I>
      <BR><BR>
      <INPUT id="payOnline" type="submit" name="submitRequest" value="Click To Pay Online">

      </TD>
    </TR>
  </TBODY>
</TABLE>
<%-- single cell table used for centering content --%>

<INPUT type="hidden" name="next" value="Pay">
</FORM>

</DIV>

</BODY>
</HTML>

