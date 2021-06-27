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
import="org.ucnj.paccess.dataaccess.beans.Order,
		org.ucnj.paccess.Result,
        org.ucnj.paccess.dataaccess.beans.Request,
        org.ucnj.paccess.dataaccess.beans.OrderItem" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<jsp:useBean id="userUCPArq" class="org.ucnj.paccess.UCPA" scope="request">
</jsp:useBean>

<%
Order lastOrder = userUCPArq.getLastOrder();
Request currRequest = userUCPArq.getCurrentRequest();
java.util.Hashtable requestList = currRequest.getList();
%>

<DIV style="font-family: arial;" align="left">

<TABLE align="left" width="650" border="0">

  <TR><TD height="15" width="650" align="left">Return to <a href="/UCPA/DocIndex">Land Records Home Page</a></TD></TR>
  <TR><TD height="15" width="650" align="left"></TD></TR>

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
  <TR><TD class="name" width="650" align="center">
    ONLINE PAYMENT RECEIPT FOR DOCUMENT COPIES
  </TD></TR>
  <TR><TD width="650" align="center">
    This is your receipt.
    Please print and keep a copy for your records.<BR>
    Allow 5 to 7 business days for processing.<BR>
    Thank you - we are pleased to provide you with this service.<BR>
    Office Phone: (908) 527-4787
  </TD></TR>
  <TR><TD height="15" width="650" align="left">&nbsp;</TD></TR>
  
  <TR><TD width="650" bgcolor="#cccccc">Receipt:</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left">
    <TABLE cellpadding="0" cellspacing="0" width="375" border="0" align="left">
      <TR>
        <TD class="addLbl" width="110">Transaction Type:</TD>
        <TD class="req" width="265"><%= lastOrder.getTxType() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Transaction ID:</TD>
        <TD class="req" width="265"><%= lastOrder.getTxId() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Date/Time:</TD>
        <TD class="req" width="265"><%= lastOrder.getTxTimestamp() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Approval Code:</TD>
        <TD class="req" width="265"><%= lastOrder.getTxApprovalCode() %></TD>
      </TR>
    </TABLE>
  </TD></TR>
  
  <TR><TD height="15" width="650" align="left">&nbsp;</TD></TR>
  <TR><TD width="650" bgcolor="#cccccc">Order Information:</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left">
    <TABLE cellpadding="0" cellspacing="0" width="375" border="0" align="left">
      <TR>
        <TD class="addLbl" width="110">Invoice Number:</TD>
        <TD class="req" width="265"><%= lastOrder.getInvoiceNumber() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Amount:</TD>
        <TD class="req" width="265"><%= lastOrder.getAmountString() %></TD>
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
        <TD class="hd" width="207" align="left">Name</TD>
        <TD class="hd" width="25" align="center">Pg</TD>
        <TD class="hd" width="30" align="center">Cert</TD>
      </TR>

<%
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
       aResult.getDocType().equals(new Integer(org.ucnj.paccess.UCPA.DOC_TYPE_NOTICE_OF_SETTLEMENT)) == false) {
      bookPage = aResult.getBook() + slash + aResult.getPage();
   }

   // only display items with available number of pages
   if (aResult.getNumPages().intValue() > 0) {
%>
      <TR>
        <TD class="req" width="88" align="left" valign="top">
          <%= aResult.getInstrPrefix() %><%= dashPrefix %><%= aResult.getInstrMiddle() %><%= dashSuffix %><%= aResult.getInstrSuffix() %>
        </TD>
        <TD class="req" width="117" align="left" valign="top"><%= aResult.getDocTypeDesc() %></TD>
        <TD class="req" width="74" align="left" valign="top"><%= aResult.getStampDate() %></TD>
        <TD class="req" width="74" align="left" valign="top"><%= bookPage %></TD>
        <TD class="req" width="207" align="left" valign="top"><%= aResult.getLastName() %><%= comma %><%= aResult.getFirstName() %></TD>
        <TD class="req" width="25" align="center" valign="top"><%= aResult.getNumPages().toString() %></TD>
        <TD class="req" width="30" align="center" valign="top"><%= Integer.toString(listItem.getNumCertified()) %></TD>
      </TR>
<%
   }
} // requestList record while loop

// on receipt, clear the list
currRequest.getList().clear();
%>

    </TABLE>
    <!-- end of results table -->

  </TD></TR>
  
  <TR><TD height="20" width="650" align="left"></TD></TR>
  <TR><TD width="650" bgcolor="#cccccc">Billing Address:</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left">
    <TABLE cellpadding="0" cellspacing="0" width="305" border="0" align="left">
      <TR>
        <TD class="addLbl" width="110">Company:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getCompany() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">First Name:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getFirstName() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Last Name:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getLastName() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Address:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getAddress1() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Address:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getAddress2() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">City:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getCity() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">State:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getState() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Zipcode:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getPostCode() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Country:</TD>
        <TD class="req" width="265"><%= lastOrder.getBillTo().getCountry() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Email Address:</TD>
        <TD class="req" width="265"><%= lastOrder.getEmail() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Phone:</TD>
        <TD class="req" width="265"><%= lastOrder.getPhone() %></TD>
      </TR>
    </TABLE>
  </TD></TR>
  
  <TR><TD height="20" width="650" align="left"></TD></TR>
  <TR><TD width="650" bgcolor="#cccccc">Shipping Address (if different from above):</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left">
    <TABLE cellpadding="0" cellspacing="0" width="305" border="0" align="left">
      <TR>
        <TD class="addLbl" width="110">Company:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getCompany() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">First Name:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getFirstName() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Last Name:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getLastName() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Address:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getAddress1() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Address:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getAddress2() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">City:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getCity() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">State:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getState() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Zipcode:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getPostCode() %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="110">Country:</TD>
        <TD class="req" width="265"><%= lastOrder.getShipTo().getCountry() %></TD>
      </TR>
    </TABLE>
  </TD></TR>

  <TR><TD height="15" width="650" align="left"></TD></TR>
  <TR><TD height="15" width="650" align="left">Return to <a href="/UCPA/DocIndex">Land Records Home Page</a></TD></TR>

</TABLE>

</DIV>

</BODY>
</HTML>


