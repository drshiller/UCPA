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
int pwMgrResult = userUCPArq.getPwMgrResult();

boolean showForms = true;
String msg = "";
String msgAlign = "center";
String pwMgrName = "";

switch (pwMgrResult) {
   case org.ucnj.paccess.servlets.PwMgr.RESULT_OK:
      msg = "Your password has been changed.";
      msgAlign = "center";
      showForms = false;
      break;
   case org.ucnj.paccess.servlets.PwMgr.RESULT_BAD_NAME_OR_PW:
      msg = "The user name or old password is incorrect. Please try again.";
      msgAlign = "center";
      pwMgrName = userUCPArq.getPwMgrName();
      showForms = true;
      break;
   case org.ucnj.paccess.servlets.PwMgr.RESULT_BAD_PW:
      msg = "Your new password must be at least " + org.ucnj.paccess.servlets.PwMgr.MIN_LENGTH + " characters, " + 
            "and up to a maximum of " + org.ucnj.paccess.servlets.PwMgr.MAX_LENGTH + " characters. " +
            "Type a password that meets these requirements in both text boxes.";
      msgAlign = "left";
      pwMgrName = userUCPArq.getPwMgrName();
      showForms = true;
      break;
   case org.ucnj.paccess.servlets.PwMgr.RESULT_BAD_CONFIRM:
      msg = "The passwords you typed do not match. " +
            "Type the new password in both text boxes.";
      msgAlign = "left";
      pwMgrName = userUCPArq.getPwMgrName();
      showForms = true;
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
  <TR><TD class="formTitle">Change Password</TD></TR>
  <TR><TD>&nbsp;</TD></TR>
</TABLE>

<% if (showForms == false) { %>

<!-- Success Message Table -->
<TABLE border="0" cellspacing="0" cellpadding="5" width="615" class="hilite">
  <TR><TD class="plus1" align="center">
    <SPAN style="color: green;"><% out.print(msg); %></SPAN>
  </TD></TR>
  <TR><TD>&nbsp;</TD></TR>
</TABLE>

<% } else { %>

<!-- Forms Tables -->
<TABLE border="0" cellspacing="0" cellpadding=5 width="615" class="hilite">
  <TR><TD class="clsArial">
    Use the form below to change your password.
    The new password must be at least <%= org.ucnj.paccess.servlets.PwMgr.MIN_LENGTH %> characters long,
    up to a maximum of <%= org.ucnj.paccess.servlets.PwMgr.MAX_LENGTH %> characters.
    Case is irrelevant.
  </TD></TR>
  <TR><TD class="plus1" align="<%= msgAlign %>">
    <SPAN style="color: red;"><% out.print(msg); %></SPAN>
  </TD></TR>

  <TR><TD align="center">

     <FORM method="POST" action="PwMgr">
       <TABLE cellpadding="1" cellspacing="0" border="0" width="450" class="border">
         <TR><TD valign="top">
           <TABLE width="448" cellspacing="0" cellpadding="4" border="0" class="hilite">
             <TR>
               <TD width="180" align="left" class="clsArial">Login Name:</TD>
               <TD width="260" align="left"><INPUT type="text" name="name" value="<%= pwMgrName %>" size="30" maxlength="10"></TD>
             </TR>
             <TR>
               <TD width="180" align="left" class="clsArial">Old Password:</TD>
               <TD width="260" align="left"><INPUT type="password" name="old" size="30" maxlength="10"></TD>
             </TR>
             <TR>
               <TD width="180" align="left" class="clsArial">New Password:</TD>
               <TD width="260" align="left"><INPUT type="password" name="new" size="30" maxlength="10"></TD>
             </TR>
             <TR>
               <TD width="180" align="left" class="clsArial">Confirm New Password:</TD>
               <TD width="260" align="left"><INPUT type="password" name="con" size="30" maxlength="10"></TD>
             </TR>
             <TR>
               <TD width="440" align="center" colspan="2"><INPUT type="submit" name="btnSubmit" value="Change"></TD>
             </TR>
             <TR>
               <TD width="440" align="left" colspan="2">
                 <FONT size="-1">
                    <A href="DocIndex?s=login&ls=send">Lost your name or old password?</A>
                 </FONT>
               </TD>
             </TR>
           </TABLE>
         </TD></TR>
       </TABLE>
     </FORM>

   </TD></TR>

</TABLE>

<% } %>

<jsp:include page="includes/LinkBar.jsp" flush="true" />

</DIV>

</BODY>
</HTML>
