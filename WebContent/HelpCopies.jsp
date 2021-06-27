<%@page import="org.ucnj.paccess.UCPA"%>
<% String audience = application.getInitParameter("audience"); %>

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
TD.addLbl{
  font-size: 14px;
  font-weight: bold;
}
-->
</STYLE>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="/includes/LoginStatus.jsp" %>

</HEAD>

<BODY background="/UCPA/grfx/wood1.gif" vlink="#006633" link="#006633" alink="#993300">

<DIV style="font-family: arial;" align="center">

<IMG src="/UCPA/grfx/logoheader3wd.gif" border="0">
<BR>
<BR>

<%
if (audience.equals(UCPA.AUDIENCE_INTERNAL)) {
%>
<!-- Header Info Table -->
<TABLE border="0" cellspacing="0" cellpadding="5" width="615" class="hilite">
  <TR><TD class="title">Requesting Certified Copies of Documents</TD></TR>
  <TR><TD>Users may request certified copies of documents be sent directly to them by the Union
      County Clerk's Office. To make a request, the user creates a list of required
      documents. This list is sent to the Clerk's Office along with a check,
      money order, or credit card to cover the cost of copying.</TD></TR>
  <TR><TD>&nbsp;</TD></TR>
  <TR><TD class="formTitle">1. Creating the Request List</TD></TR>
  <TR><TD>
    The request list is created using the Name Search, Book and Page Search, and Instrument Number Search.
    Each document record displayed on the Search by Name Results page includes a link
    labeled "Request Certified Copy",
    which is located to the right of each record.
    To add a document to the request list,
    click on its associated "Request Certified Copy" link.
    Clicking on this link takes the user to the Request List form.
  </TD></TR>
  <TR><TD>&nbsp;</TD></TR>
  <TR><TD class="formTitle">2. Using the Request List Form</TD></TR>
  <TR><TD>
    <OL class="OLa">
      <LI><B>Choose Copies</B> -- Each item in the list displays the pertinent information for a document
        including instrument number, document type, recorded date and the number
        of pages. There is also a box where the user enters the number of certified copies
        being ordered. A user can order a certified copy at a cost of $8.00 for the first page plus $2.00
        for each additional page. The user can choose to remove a given item by
        clicking on its associated &quot;Remove Entry&quot; link.</LI>
      <LI><B>Add More Items</B> --
        To add more items to the list, click on the "Update Order and Continue Searching" button.
        This will take the
        user back to the Search Results page where other items can be selected.
        Any new searches can be completed at this time, and new items can be added.</LI>
      <LI><B>Clear All Items</B> --
        Click on the "Clear All" button to remove all of the items from the request list form.</LI>
      <LI><B>Set Delivery Details</B> --
        Use the name and address form to enter the delivery information for the certified copies.</LI>
      <LI><B>Creating the Printable Version of the Request List</B> --
        After all the information is added to the request list, click on the "Go to Print Form"
        button to view a printable version of the list.</LI>
     </OL>
  </TD></TR>
  <TR><TD class="formTitle">3. Using the Print Form</TD></TR>
  <TR><TD>
    Select the print function of the browser to create a hardcopy of the request list.
    The copy should be sent to the Union County Clerk's office at the address printed
    on the form. The form also displays the amount that must be sent with it in order
    to have the requested certified copies made.
  
  The document(s) will be sent to you via mail regardless of the type of payment used within 3 to 5 days.</TD></TR>

</TABLE>
<%
} // audience=internal
else if (audience.equals(UCPA.AUDIENCE_EXTERNAL)) {
%>
<TABLE border="0" cellspacing="0" cellpadding="5" width="615" class="hilite">
  <TR><TD class="title">Requesting Certified Copies of Documents</TD></TR>
  <TR><TD>Users may request certified copies of documents be sent directly to them by the Union
      County Clerk's Office. Documents can ordered online or via regular mail.
      If the user selects to order online then a credit card will be required
      for payment. If the user chooses to order via regular mail then a list of
      required documents must be printed. This printed list is sent to the Clerk's Office along with a check,
      money order, or credit card to cover the cost of copying.</TD></TR>
  <TR><TD>&nbsp;</TD></TR>
  <TR><TD class="formTitle">1. Creating the Request List</TD></TR>
  <TR><TD>
    The request list is created using the Name Search, Book and Page Search, and Instrument Number Search.
    Each document record displayed on the Search by Name Results page includes a link
    labeled "Request Certified Copy",
    which is located to the right of each record.
    To add a document to the request list,
    click on its associated "Request Certified Copy" link.
    Clicking on this link takes the user to the Request List form.
  </TD></TR>
  <TR><TD>&nbsp;</TD></TR>
  <TR><TD class="formTitle">2. Using the Request List Form</TD></TR>
  <TR><TD>
    <OL class="OLa">
      <LI><B>Choose Copies</B> -- Each item in the list displays the pertinent information for a document
        including instrument number, document type, recorded date and the number
        of pages. There is also a box where the user enters the number of certified copies
        being ordered. A user can order a certified copy at a cost of $8.00 for the first page plus $2.00
        for each additional page. The user can choose to remove a given item by
        clicking on its associated &quot;Remove Entry&quot; link.</LI>
      <LI><B>Add More Items</B> --
        To add more items to the list, click on the "Update Order and Continue Searching" button.
        This will take the
        user back to the Search Results page where other items can be selected.
        Any new searches can be completed at this time, and new items can be added.</LI>
      <LI><B>Clear All Items</B> --
        Click on the "Clear All" button to remove all of the items from the request list form.</LI>
      <LI><B>Order Online</B> --
        After all the information is added to the request list, click on the "Order Online"
        button to begin on online credit card transaction.
        <BR><BR>
        On the following "Request List -- Order Online" page, select the "Click to Pay Online"
        button, which will take the user to our third party credit card merchant via a secure
        encrypted network.
        </LI>
      <LI><B>Order by Mail</B> --
        After all the information is added to the request list, click on the "Order by Mail"
        button to create a printable order form. This printed list is sent to the Clerk's
        Office along with a check, money order, or credit card to cover the cost of copying.
        <BR><BR>
        On the  following "Request List -- Order by Mail" page, use the name and address form to
        enter the delivery information for the certified copies. Click on the "Go to Print Form"
        button to view a printable version of the list.
      </LI>
     </OL>
  </TD></TR>
</TABLE>
<%
} // audience=external
%>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="/includes/LinkBar.jsp" %>

</DIV>

</BODY>
</HTML>
