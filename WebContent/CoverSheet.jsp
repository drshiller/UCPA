<%@page import="org.ucnj.paccess.UCPA"%>

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

<div style="width: 615px;">
	<jsp:include page="/includes/coversheet.html" flush="true"/>
</div>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="/includes/LinkBar.jsp" %>

</DIV>

</BODY>
</HTML>
