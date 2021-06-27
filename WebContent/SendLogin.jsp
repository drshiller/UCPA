<HTML>

<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

<SCRIPT language="JavaScript" src="scripts/common.js">
</SCRIPT>

</HEAD>

<BODY background="grfx/wood1.gif" vlink="#006633" link="#006633" alink="#993300" onload="formFocus();">

<jsp:useBean id="userUCPArq" class="org.ucnj.paccess.UCPA" scope="request">
</jsp:useBean>

<%
int sendResult = userUCPArq.getCurrentLoginStatus().getSendResult();

boolean showForms = true;
String msg = "";
String sendAddress = "";
String sendName = "";

switch (sendResult) {
   case org.ucnj.paccess.servlets.SendLogin.RESULT_SENT_NAME:
      msg = "Your login name has been sent to your registered e-mail.";
      showForms = false;
      break;
   case org.ucnj.paccess.servlets.SendLogin.RESULT_BAD_ADD:
      msg = "The e-mail address is not valid. Please try again.";
      showForms = true;
      sendAddress = userUCPArq.getCurrentLoginStatus().getSendAddress();
      break;
   case org.ucnj.paccess.servlets.SendLogin.RESULT_SENT_PW:
      msg = "Your password has been sent to your registered e-mail address.";
      showForms = false;
      break;
   case org.ucnj.paccess.servlets.SendLogin.RESULT_BAD_NAME:
      msg = "The login name is not valid. Please try again.";
      showForms = true;
      sendName = userUCPArq.getCurrentLoginStatus().getSendName();
      break;
   default:
      msg = "&nbsp;";
      showForms = true;
      break;
}
%>

<DIV style="font-family: arial;" align="center">

<IMG src="grfx/logoheader3wd.gif" border="0">
<BR>

<!-- Header Info Table -->
<TABLE border="0" cellspacing="0" cellpadding="5" width="615" class="hilite">
  <TR><TD class="title">Online Document Viewing Service</TD></TR>
  <TR><TD>&nbsp;</TD></TR>
  <TR><TD class="formTitle">Sign-in Problems</TD></TR>
  <TR><TD>&nbsp;</TD></TR>
</TABLE>

<% if (showForms == false) { %>

<!-- Success Message Table -->
<TABLE border="0" cellspacing="0" cellpadding="5" width="615" class="hilite">
  <TR><TD class="plus1" align="center">
    <FONT color="green"><% out.print(msg); %></FONT>
  </TD></TR>
</TABLE>

<% } else { %>

<!-- Forms Tables -->
<TABLE border="0" cellspacing="0" cellpadding="5" width="615" class="hilite">
  <TR><TD>
    Did you forget your login name or password?
    If so, use one of the forms below to be reminded of it.
    If you have forgotten both,
    then fill in the form to find your login name and then come back to get your password.
    We will automatically send your details to your registered e-mail address.
  </TD></TR>
  <TR><TD class="plus1" align="center">
    <FONT color="red"><%= msg %></FONT>
  </TD></TR>

  <TR><TD align="center">

    <TABLE width="585" cellspacing="5" border="0" class="hilite">

      <TR><TD valign="top">

        <FORM method="POST" action="SendLogin" name="nameForm">
          <TABLE cellspacing="0" border="0" width="100%" class="border">
            <TR><TD valign="top">
                <TABLE width="100%" cellspacing="0" cellpadding="4" border="0" class="hilite">
                  <TR><TD class="formTitle">Forgot your login name?</TD></TR>
                  <TR><TD align="center" class="inst">Enter your registered <B>Email Address:</B></TD></TR>
                  <TR><TD align="center"><INPUT type="text" name="to" value="<%= sendAddress %>" size="30" maxlength="50"></TD></TR>
                  <TR><TD align="center"><INPUT type="submit" name="Submit" value="Submit"></TD></TR>
                </TABLE>
            </TD></TR>
          </TABLE>
          <INPUT type="hidden" name="type" value="name">
        </FORM>

      </TD>

      <TD class="plus1">OR</TD>

      <TD valign="top">

        <FORM method="POST" action="SendLogin" name="pwForm">
          <TABLE cellspacing="0" border="0" width="100%" class="border">
            <TR><TD valign="top">
                <TABLE width="100%" cellspacing="0" cellpadding="4 "border="0" class="hilite">
                  <TR><TD class="formTitle">Forgot your password?</TD></TR>
                  <TR><TD align="center" class="inst">Enter your registered <B>Login Name:</B></TD></TR>
                  <TR><TD align="center"><INPUT type="text" name="name" value="<%= sendName %>" size="30" maxlength="10"></TD></TR>
                  <TR><TD align="center"><INPUT type="submit" name="Submit" value="Submit"></TD></TR>
                </TABLE>
            </TD></TR>
          </TABLE>
          <INPUT type="hidden" name="type" value="pw">
        </FORM>

      </TD></TR>
    </TABLE>

  </TD></TR>
</TABLE>

<% } %>

<jsp:include page="includes/LinkBar.jsp" flush="true" />

</DIV>

</BODY>
</HTML>
