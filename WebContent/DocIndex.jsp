<HTML>

<HEAD>

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<META name="description" content="Welcome to the Union County Clerk's 
  Online Public Land Records Search Page. We are pleased you are visiting
  this site which contains information recorded into the Official Land
  Records of Union County, New Jersey.">
<META name="keywords" content="Union County, New Jersey, Land Records, Search"> 
<META http-equiv="Content-Style-Type" content="text/css">

<LINK href="/UCPA/theme/Master.css" rel="stylesheet" type="text/css">
<link href="/UCPA/theme/news1.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="/includes/LoginStatus.jsp" %>

<SCRIPT language="JavaScript" src="/UCPA/scripts/rollover.js">
</SCRIPT>
<SCRIPT language="JavaScript" src="/UCPA/scripts/banner.js">
</SCRIPT>
<script language="JavaScript" src="/UCPA/scripts/Viewer.js">
</script>
</HEAD>

<BODY vlink="#006633" link="#006633" alink="#990000" background="/UCPA/grfx/wood1.gif" onload="rotate()">

<DIV align="center" STYLE="font-family: Arial">

<TABLE width="1024" cellspacing="0" cellpadding="0" border="0">
  <TBODY>
    <TR>
      <TD rowspan="2" align="right" valign="top" width="125">
         &nbsp;
      </TD>
      <TD align="center" valign="middle" width="500">
         <IMG src="/UCPA/grfx/logoheader2wd.gif" width="500" height="40">
      </TD>
      <TD rowspan="2" align="right" valign="top" width="125">
         <IMG src="/UCPA/grfx/banner/ban1.jpg" name="imgBanner" border="0" width="125" height="97">
      </TD>
    </TR>
    <TR>
      <TD align="center" valign="middle" width="500">
         <IMG src="/UCPA/grfx/publicwd.gif">
      </TD>
    </TR>
  </TBODY>
</TABLE>
<TABLE width="1024" cellspacing="0" cellpadding="0" border="0">
  <TBODY>
    <TR>
      <TD width="1024" valign="top" colspan="2">
        <SPAN STYLE="font-family: Arial; font-size:14px">
        Welcome to the Union County Clerk's Online Public Land Records Search Page.
        We are pleased you are visiting this site which contains information recorded
        into the Official Land Records of Union County, New Jersey from June 1,
        1977 through <%= userUCPArq.getCurrentIndexDate(true) %>.
        </SPAN>
      </TD>
    </TR>
    <TR>
      <TD height="4" width="1024" valign="top" colspan="2"></TD>
    </TR>
    <TR>
      <TD width="1024" valign="top" colspan="2">
        <SPAN STYLE="font-family: Arial; font-size:14px">
        As County Clerk, I present this information as a service to the public,
        our residents and our clients. Currently, the data included on this web
        site will permit a searcher to locate documents by name within a specific
        time period which are recorded in the office.
        </SPAN>
      </TD>
    </TR>
    <TR>
      <TD height="4" width="1024" valign="top" colspan="2"></TD>
    </TR>
    <TR>
      <TD width="1024" valign="top" colspan="2">
        <SPAN STYLE="font-family: Arial; font-size:14px">
        The public can view any document that has been published on the web.
        We are in the process of imaging documents prior to January 1986.
        If a document has been imaged,
        you will see the image icon next to the name result.
        <B>You will need to have installed
        <a href="#" onclick="popupAdobe()">Adobe Reader</a>
        to see a published document.</B>
        </SPAN>
      </TD>
    </TR>
    <TR>
      <TD height="4" width="1024" valign="top" colspan="2"></TD>
    </TR>
    <TR>
      <TD width="1024" valign="top" colspan="2">
        <SPAN STYLE="font-family: Arial; font-size:14px">
        Users may also request certified copies of documents be sent directly to them.
        <A href="/UCPA/DocIndex?s=copies">Click here</A> for more information on requesting copies.
        </SPAN>
      </TD>
    </TR>
    <TR>
      <TD height="4" width="1024" valign="top" colspan="2"></TD>
    </TR>
    <TR>
      <TD width="1024" valign="top" colspan="2">
        <SPAN STYLE="font-family: Arial; font-size:14px">
        Please be advised that Dedications, Disclaimers, Notices of Sale and Vacations are indexed 
        and available in our office. Please contact us for additional information.
        </SPAN>
      </TD>
    </TR>
    <TR>
      <TD height="4" width="1024" valign="top" colspan="2"></TD>
    </TR>
    <TR>
      <TD width="1024" valign="top" colspan="2">
        <SPAN STYLE="font-family: Arial; font-size:14px">
        As always you are invited to visit our office and Record Room where copies of
        all land transactions within the county are located from 1857 to the present.
        Our staff looks forward to assisting you.
        </SPAN>
      </TD>
    </TR>
    <TR>
      <TD height="4" width="1024" valign="top" colspan="2"></TD>
    </TR>
    <TR>
      <TD width="500" valign="top">
        <SPAN STYLE="font-family: Arial; font-size:14px">
        If this is your first visit to this site, please review the 
        <A href="/UCPA/DocIndex?s=terms">Terms of Use</A>.
        </SPAN>
      </TD>
      <TD width="250" align="right">
        <SPAN STYLE="font-family: Arial; font-size:14px; font-style: italic; font-weight: bold">
          Joanne Rajoppi
        </SPAN>
        <SPAN STYLE="font-family: Arial; font-size:14px"> -- Union County Clerk</SPAN>
      </TD>
    </TR>
    <TR>
      <TD height="10" width="1024" valign="top" colspan="2"></TD>
    </TR>
  </TBODY>
</TABLE>

<TABLE width="1024" cellpadding="0" cellspacing="0" border="0">
  <TBODY>
    <TR>
      <TD width="303" align="left" valign="top">
        <jsp:include page="/includes/news.html" flush="true"/>
      </TD>
      <TD width="18">
        &nbsp;
      </TD>
      <TD valign="top" width="375">
      <TABLE width="375" cellpadding="0" cellspacing="0" border="0">
          <TBODY>
            <TR>
            <TD colspan="3" valign="top" align="center" width="375">
                <SPAN STYLE="font-family: Arial; font-weight: bold; font-size: large">You may search by:</SPAN>
              </TD>
          </TR>
          <TR>
              <TD align="center" valign="middle" width="125">
                <A HREF="/UCPA/DocIndex?s=name" onMouseOver="F_roll('ByName',1)" onMouseOut="F_roll('ByName',0)">
                  <IMG name="ByName" height="24" width="125" src="/UCPA/grfx/name.gif" onLoad="F_loadRollover(this,'nameRO.gif')" border="0" alt="Search by Name">
                </A>
              </TD>
              <TD align="center" valign="middle" width="125">
                <A HREF="/UCPA/DocIndex?s=book" onMouseOver="F_roll('ByBook',1)" onMouseOut="F_roll('ByBook',0)">
                  <IMG name="ByBook" height="24" width="125" src="/UCPA/grfx/book.gif" onLoad="F_loadRollover(this,'bookRO.gif')" border="0" alt="Search by Book and Page">
                </A>
              </TD>
              <TD align="center" valign="middle" width="125">
                <A HREF="/UCPA/DocIndex?s=inst" onMouseOver="F_roll('ByInstNo',1)" onMouseOut="F_roll('ByInstNo',0)">
                  <IMG name="ByInstNo" height="24" width="125" src="/UCPA/grfx/inst.gif" onLoad="F_loadRollover(this,'instRO.gif')" border="0" alt="Search by Instrument Number">
                </A>
              </TD>
            </TR>
            <TR>
              <TD colspan="3" valign="top" align="center" width="375">
                <A HREF="/UCPA/DocIndex?s=date" onMouseOver="F_roll('ByDate',1)" onMouseOut="F_roll('ByDate',0)">
                  <IMG name="ByDate" height="24" width="125" src="/UCPA/grfx/date.gif" onLoad="F_loadRollover(this,'dateRO.gif')" border="0" alt="Search by Date">
                </A>
              </TD>
            </TR>
          </TBODY>
        </TABLE>
      </TD>
      <TD width="18">
        &nbsp;
      </TD>
      <TD width="303" align="left" valign="top">
        <jsp:include page="/includes/coversheet.html" flush="true"/>
      </TD>
    </TR>
  </TBODY>
</TABLE>

</DIV>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="/includes/LinkBarHome.jsp" %>

</BODY>
</HTML>
