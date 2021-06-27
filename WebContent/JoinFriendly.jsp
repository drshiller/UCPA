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

<%
int usersPerBlock = 5;
int cost1stBlock = 600;
int costOtherBlock = 400;
int nUsers = new Integer(request.getParameter("nusers").trim()).intValue();
if (nUsers < 0) nUsers = 0;
int nBlocks = nUsers / usersPerBlock;
if ((nBlocks * usersPerBlock) != nUsers) nBlocks++;
int totalDue = cost1stBlock;
if (nBlocks > 1) totalDue += ((nBlocks-1) * costOtherBlock);
String sTotalDue = "$" + new Integer(totalDue).toString() + ".00";
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
  <TR><TD class="name" width="650" align="center">
    DOCUMENT VIEWING<BR>
    REGISTRATION FORM
  </TD></TR>
  <TR><TD height="15" width="650" align="left">&nbsp;</TD></TR>
  <TR><TD width="650" bgcolor="#cccccc">New Member Details:</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>

  <!-- requester's address -->
  <TR><TD width="650" align="left">
    <TABLE cellpadding="0" cellspacing="0" width="305" border="0" align="left">
      <TR>
        <TD class="addLbl" width="75">Name:</TD>
        <TD class="req" width="225"><%= request.getParameter("name") %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">Address:</TD>
        <TD class="req" width="225"><%= request.getParameter("add1") %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">Address:</TD>
        <TD class="req" width="225"><%= request.getParameter("add2") %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">City:</TD>
        <TD class="req" width="225"><%= request.getParameter("city") %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">State:</TD>
        <TD class="req" width="225"><%= request.getParameter("state") %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">Zipcode:</TD>
        <TD class="req" width="225"><%= request.getParameter("zipcode") %></TD>
      </TR>
      <TR>
        <TD class="addLbl" width="75">Phone:</TD>
        <TD class="req" width="225"><%= request.getParameter("phone") %></TD>
      </TR>
    </TABLE>
  </TD></TR>

  <TR><TD height="5" width="650" align="left">&nbsp;</TD></TR>
  <TR><TD width="650" bgcolor="#cccccc">Primary E-mail Address for Correspondence:</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left">
    <%= request.getParameter("email") %>
  </TD></TR>

  <TR><TD height="5" width="650" align="left">&nbsp;</TD></TR>
  <TR><TD width="650" bgcolor="#cccccc">User Names and E-mail Addresses:</TD></TR>
  <TR><TD height="5" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left">
    <TABLE cellpadding="0" cellspacing="0" width="415" border="0">
      <TR>
        <TD class="req" width="20">1.</TD>
        <TD class="req" width="180"><%= request.getParameter("u1") %></TD>
        <TD class="req" width="225"><%= request.getParameter("e1") %></TD>
      </TR>
      <TR>
        <TD class="req" width="20">2.</TD>
        <TD class="req" width="180"><%= request.getParameter("u2") %></TD>
        <TD class="req" width="225"><%= request.getParameter("e2") %></TD>
      </TR>
      <TR>
        <TD class="req" width="20">3.</TD>
        <TD class="req" width="180"><%= request.getParameter("u3") %></TD>
        <TD class="req" width="225"><%= request.getParameter("e3") %></TD>
      </TR>
      <TR>
        <TD class="req" width="20">4.</TD>
        <TD class="req" width="180"><%= request.getParameter("u4") %></TD>
        <TD class="req" width="225"><%= request.getParameter("e4") %></TD>
      </TR>
      <TR>
        <TD class="req" width="20">5.</TD>
        <TD class="req" width="180"><%= request.getParameter("u5") %></TD>
        <TD class="req" width="225"><%= request.getParameter("e5") %></TD>
      </TR>
    </TABLE>
  </TD></TR>

  <TR><TD height="20" width="650" align="left"></TD></TR>
  <TR><TD width="650" align="left" bgcolor="#cccccc">
    Please send this form with a check or money order of 
    <B><%= sTotalDue %></B>
    payable to Union County Clerk to the following address:
  </TD></TR>
  <TR><TD height="3" width="650" ></TD></TR>
  <TR><TD width="650" align="left">
    Union County Clerk's Office<BR>
    2 Broad Street<BR>
    Room 115<BR>
    Elizabeth, NJ 07207
  </TD></TR>

  <TR><TD height="30" width="650" ></TD></TR>
  <TR><TD width="650" align="left">
    <SPAN style="font-size: 12px;">
      If futher assistance is required, please call the County Clerk's office at 908-527-4787.
    </SPAN>
  </TD></TR>
  <TR><TD height="3" width="650" align="left"></TD></TR>
  <TR><TD height="15" width="650" align="left">Return to <a href="javascript:history.back(1)">Previous Page</a></TD></TR>

</TABLE>

</DIV>

</BODY>
</HTML>


