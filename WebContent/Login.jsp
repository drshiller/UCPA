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
int loginResult = userUCPArq.getCurrentLoginStatus().getLoginResult();

boolean showForms = true;
String msg = "";
String msgColor = "";
String includePanel = "includes/";
String loginName = "";

switch (loginResult) {
   case org.ucnj.paccess.servlets.Login.RESULT_OK_IN:
      msg = "You have been signed in.";
      showForms = false;
      break;
   case org.ucnj.paccess.servlets.Login.RESULT_OK_OUT:
      msg = "You have been signed out.";
      showForms = false;
      break;
   case org.ucnj.paccess.servlets.Login.RESULT_BAD_ARGS:
      msg = "The name and password fields are both required to sign in.";
      msgColor = "red";
      includePanel += "loginNew.html";
      loginName = userUCPArq.getCurrentLoginStatus().getLoginName();
      showForms = true;
      break;
   case org.ucnj.paccess.servlets.Login.RESULT_BAD_NAME:
      msg = "The name that was entered is not a registered member.";
      msgColor = "red";
      includePanel += "loginHints.html";
      loginName = userUCPArq.getCurrentLoginStatus().getLoginName();
      showForms = true;
      break;
   case org.ucnj.paccess.servlets.Login.RESULT_BAD_PW:
      msg = "The password that was entered is not valid for this name.";
      msgColor = "red";
      includePanel += "loginHints.html";
      loginName = userUCPArq.getCurrentLoginStatus().getLoginName();
      showForms = true;
      break;
   case org.ucnj.paccess.servlets.Login.RESULT_NOT_ACTIVE:
      msg = "Your account is no longer active.";
      msgColor = "red";
      includePanel += "loginHints.html";
      loginName = userUCPArq.getCurrentLoginStatus().getLoginName();
      showForms = true;
      break;
   case org.ucnj.paccess.servlets.Login.RESULT_NONE:
   default:
      msg = "You must sign in to view documents in their entirety.";
      msgColor = "green";
      includePanel += "loginNew.html";
      showForms = true;
      break;
}

%>

<DIV style="font-family: arial;" align="center">

<IMG src="grfx/logoheader3wd.gif" border="0">
<BR>
<BR>

<!-- Header Info Table -->
<TABLE border="0" cellspacing="0" cellpadding="5" width="696" class="hilite">
  <TR><TD class="title">Online Document Viewing Service</TD></TR>
  <TR><TD>&nbsp;</TD></TR>
  <TR><TD class="formTitle">Member Sign-in</TD></TR>
  <TR><TD>&nbsp;</TD></TR>
</TABLE>

<% if (showForms == false) { %>

<!-- Success Message Table -->
<TABLE border="0" cellspacing="0" cellpadding="5" width="696" class="hilite">
  <TR><TD class="plus1" align="center">
    <SPAN style="color: green;"><% out.print(msg); %></SPAN>
  </TD></TR>
  <TR><TD>&nbsp;</TD></TR>
</TABLE>

<% } else { %>

<!-- Forms Tables -->
<TABLE border="0" cellspacing="0" cellpadding=5 width="696" class="hilite">
  <TR><TD class="plus1" align="center">
    <SPAN style="color: <%= msgColor %>;"><% out.print(msg); %></SPAN>
  </TD></TR>
  <TR><TD>&nbsp;</TD></TR>
  <TR><TD width="696">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="686" class="hilite">
      <TR><TD align="center" width="303" valign="top">
        <FORM method="POST" action="Login?s=in" name="loginForm">
          
          <TABLE cellpadding="1" cellspacing="0" border="0" width="300" class="border">
            <TR><TD>

              <TABLE width="298" cellspacing="0" cellpadding="4" border="0" class="hilite">
                <TR><TD width="290" align="center" class="formTitle">Existing Members</TD></TR>
                <TR><TD width="290" align="center" class="inst">Enter your name and password to sign in:</TD></TR>
                <TR><TD width="290" align="center">
                  <TABLE width="290" cellspacing="0" cellpadding="0" border="0" class="hilite">
                    <TR>
                      <TD class="clsArial" align="left" width="80">Name:</TD>
                      <TD align="left" width="210">
                        <INPUT type="text" name="name" value="<%= loginName %>" size="24" maxlength="10">
                      </TD>
                    </TR>
                    <TR>
                      <TD class="clsArial" align="left" width="80">Password:</TD>
                      <TD align="left" width="210">
                        <INPUT type="password" name="pw" value="" size="24" maxlength="10">
                      </TD>
                    </TR>
                  </TABLE>
                </TD></TR>
                <TR><TD width="290" align="center">
                  <INPUT type="submit" name="btnSubmit" value="Sign In">
                </TD></TR>
              </TABLE>

            </TD></TR>
          </TABLE>

         </FORM>
      </TD>
      <TD align="center" width="383" valign="top">
        <jsp:include page="<%= includePanel %>" flush="true" />
      </TD></TR>
    </TABLE>
  </TD></TR>
</TABLE>

<% } %>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="includes/LinkBar.jsp" %>

</DIV>

</BODY>
</HTML>
