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
TD.addLbl{
  font-size: 14px;
  font-weight: bold;
}
-->
</STYLE>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="includes/LoginStatus.jsp" %>

<SCRIPT language="JavaScript" src="scripts/Join.js">
</SCRIPT>
<SCRIPT language="JavaScript" src="scripts/common.js">
</SCRIPT>

</HEAD>

<BODY background="grfx/wood1.gif" vlink="#006633" link="#006633" alink="#993300" onload="formFocus();">

<DIV style="font-family: arial;" align="center">

<IMG src="grfx/logoheader3wd.gif" border="0">
<BR>
<BR>

<!-- Form for going to print friendly page -->
<FORM method="POST" action="JoinFriendly.jsp" name="joinForm" onSubmit='return validateInput()'>

<!-- Header Info Table -->
<TABLE border="0" cellspacing="0" cellpadding="5" width="615" class="hilite">
  <TR><TD class="title"><FONT face="Arial">Online Document Viewing Service</FONT></TD></TR>
  <TR><TD>&nbsp;</TD></TR>
  <TR><TD class="formTitle">
    <FONT face="Arial">Our online service is loaded with great features including:</FONT>
  </TD></TR>
  <TR><TD>
    <BR>
    <DL>
      <DT>
        <SPAN style="font-family: arial; font-size: 12px; font-weight : bold;">
          Accessible from any Internet connection
        </SPAN>
      <DD>
        <SPAN style="font-family: arial; font-size: 12px">
          With Union County's online viewing service, you have access to our
          land records from any Internet-connected computer in the world.
        </SPAN>
    <BR><BR>
      <DT>
        <SPAN style="font-family: arial; font-size: 12px; font-weight : bold;">
          Online viewing of documents in their entirety
        </SPAN>
      <DD>
        <SPAN style="font-family: arial; font-size: 12px">
          The public can view the first page of any document that has been published on the web.
          As a registered searcher, you have the ability to view a land record in its entirety.
        </SPAN>
    <BR><BR>
      <DT>
        <SPAN style="font-family: arial; font-size: 12px; font-weight : bold;">
          User-friendly Document Viewer Plug-in
        </SPAN>
      <DD>
        <SPAN style="font-family: arial; font-size: 12px">
          You need to add our special document viewer module to your web browser
          to view a published document.
          This viewer gives you the ability to view and print the entire document.
          If you haven't already done so then
          <A href="/UCPA/plugins/rvi/download.html">download</A>
          the viewer from our site.
        </SPAN>
    </DL>
  </TD></TR>
  <TR><TD class="formTitle">
    <FONT face="Arial">Becoming a Registered User:</FONT>
  </TD></TR>
  <TR><TD>
    <SPAN style="font-family: arial; font-size: 12px;">
      Complete the form below and then click the <B>Go to Print Form</B> button.
      A printable version of the registration form will be displayed
      in your browser.
      Print this form and return it to the Union County Clerk's
      Office with a check, money order, or credit card for $600.00, the price of one year's membership.
    </SPAN>
  </TD></TR>
  <TR><TD>
    <SPAN style="font-family: arial; font-size: 12px;">
      A registered subscriber will be allowed up to (5) five users.
      Additional blocks of users are available in blocks of (5) five at $400.00 for each block.
      If you want more than 5 users,
      please add a seperate sheet of paper and adjust the total accordingly.
    </SPAN>
  </TD></TR>

  <TR><TD>
    <SPAN style="font-family: arial; font-size: 12px;">
      <B>Contact Details: </B>
      Please enter you contact information below.
      Make sure that your
      <FONT color="#ff0000">primary e-mail address</FONT> is provided. Once the account has been set up, a confirmation e-mail will
      be sent to this address.</SPAN>
  </TD></TR>
  <TR><TD align="left">
    <TABLE cellspacing="0" border="0" width="325" class="border">
      <TR><TD valign=top>
        <TABLE cellpadding="4" cellspacing="0" width="100%" border="0" bgcolor="#ffffcc">
          <TR>
            <TD class="addLbl" width="75">Name:</TD>
            <TD><INPUT type="text" name="name" value="" size="35"></TD>
          </TR>
          <TR>
            <TD class="addLbl" width="75">Address:</TD>
            <TD><INPUT type="text" name="add1" value="" size="35"></TD>
          </TR>
          <TR>
            <TD class="addLbl" width="75">Address:</TD>
            <TD><INPUT type="text" name="add2" value="" size="35"></TD>
          </TR>
          <TR>
            <TD class="addLbl" width="75">City:</TD>
            <TD><INPUT type="text" name="city" value="" size="35"></TD>
          </TR>
          <TR>
            <TD class="addLbl" width="75">State:</TD>
            <TD><INPUT type="text" name="state" value="" size="5"></TD>
          </TR>
          <TR>
            <TD class="addLbl" width="75">Zipcode:</TD>
            <TD><INPUT type="text" name="zipcode" value="" size="5"></TD>
          </TR>
          <TR>
            <TD class="addLbl" width="75">Phone:</TD>
            <TD><INPUT type="text" name="phone" value="" size="12"></TD>
          </TR>
          <TR>
            <TD class="addLbl" width="75">E-mail:</TD>
            <TD><INPUT type="text" name="email" value="" size="35"></TD>
          </TR>
       </TABLE>
      </TD></TR>
    </TABLE>
  </TD></TR>

  <TR><TD>
    <SPAN style="font-family: arial; font-size: 12px;">
      <B>User Details:</B>
      Please enter up to 5 users below.
      Each user has a login name and corresponding e-mail address.
      Each login name and e-mail address must be
      <FONT color="red">unique</FONT>. If the login name has already been assigned to another user then the
      new user will be given that name appended with a unique number (e.g. joe,
      joe1, joe2, etc.). A login name may contain up to 10 characters, with no
      spaces. An e-mail address may contain up to 50 characters, with no spaces.<BR>
      <BR>
      When a user signs into the Land Records website, they  enter a login name
      and password. Initially the password is the same as the login name. The
      user should <A href="DocIndex?s=pw">change the password</A> once they have signed on the the Land Records website.</SPAN>
  </TD></TR>
  <TR><TD align="left">
    <TABLE cellspacing="0" border="0" width="325" class="border">
      <TR><TD valign=top>
        <TABLE cellpadding="4" cellspacing="0" width="100%" border="0" bgcolor="#ffffcc">
          <TR>
            <TD>&nbsp;</TD>
            <TD class="addLbl">Login Names:</TD>
            <TD class="addLbl">E-mail Addresses:</TD>
          </TR>
          <TR>
            <TD>1.</TD>
            <TD><INPUT type="text" name="u1" value="" size="15" maxlength="10"></TD>
            <TD><INPUT type="text" name="e1" value="" size="22" maxlength="50"></TD>
          </TR>
          <TR>
            <TD>2.</TD>
            <TD><INPUT type="text" name="u2" value="" size="15" maxlength="10"></TD>
            <TD><INPUT type="text" name="e2" value="" size="22" maxlength="50"></TD>
          </TR>
          <TR>
            <TD>3.</TD>
            <TD><INPUT type="text" name="u3" value="" size="15" maxlength="10"></TD>
            <TD><INPUT type="text" name="e3" value="" size="22" maxlength="50"></TD>
          </TR>
          <TR>
            <TD>4.</TD>
            <TD><INPUT type="text" name="u4" value="" size="15" maxlength="10"></TD>
            <TD><INPUT type="text" name="e4" value="" size="22" maxlength="50"></TD>
          </TR>
          <TR>
            <TD>5.</TD>
            <TD><INPUT type="text" name="u5" value="" size="15" maxlength="10"></TD>
            <TD><INPUT type="text" name="e5" value="" size="22" maxlength="50"></TD>
          </TR>
        </TABLE>
      </TD></TR>
    </TABLE>
    <TABLE cellspacing="0" border="0" width="325" class="border">
      <TR><TD valign=top>
        <TABLE cellpadding="4" cellspacing="0" width="100%" border="0" bgcolor="#ffffcc">
          <TR>
            <TD align="left" class="addLbl">Enter the total number of users that are being registered:</TD>
             <TD align="center"><INPUT type="text" name="nusers" value="" size="2"></TD>
          </TR>
       </TABLE>
      </TD></TR>
    </TABLE>
  </TD></TR>

  <TR><TD class="addLbl" align="center">
    <INPUT type="submit" value="Go to Print Form">
  </TD></TR>

</TABLE>

</FORM>


<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="includes/LinkBar.jsp" %>

</DIV>

</BODY>
</HTML>
