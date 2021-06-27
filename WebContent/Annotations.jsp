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
  font-size: 16px;
}
TD.hd{
  font-size: 16px;
}
-->
</STYLE>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="/includes/LoginStatus.jsp" %>

</HEAD>

<BODY bgcolor="#FFFFFF" vlink="#0000ff" link="#0000ff" alink="#ff0000">

<%@ page 
import="org.ucnj.paccess.View,
        org.ucnj.paccess.AnnotationSet, 
        org.ucnj.paccess.Annotation" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<DIV align="center" STYLE="font-family: Arial">

<IMG src="grfx/logoheader3.gif" border="0">
<BR>

<%
View currView = userUCPArq.getCurrentView();
@SuppressWarnings("unchecked")
java.util.Vector annotationSets = currView.getAnnotationSets();
String instrNumber = "<B>Instr. No.: </B>" + currView.getInstrNumber();
String bookPage = "";
if (currView.getBookPage().equals(currView.getInstrNumber()) == false)
   bookPage = "<B>Book/Page: </B>" + currView.getBookPage();
%>

<BR>
<TABLE cellpadding="2" cellspacing="0" width="570" border="0">
  <TR>
    <TD class="hd" colspan="3" width="558" align="left"><B>Annotations for:</B></TD>
  </TR>
  <TR>
    <TD class="hd" width="183" align="left"><%= currView.getDocType() %></TD>
    <TD class="hd" width="193" align="left"><%= instrNumber %></TD>
    <TD class="hd" width="182" align="left"><%= bookPage %></TD>
  </TR>
</TABLE>
<BR>

<%
if (annotationSets.size() > 0) {
   String liteColor = "#ffffff";
   String darkColor = "#e5e5e5";
   String bgColor = darkColor;
%>

<TABLE cellpadding="2" cellspacing="0" width="570" border="0">
<%
   // for each set of annotation notes
   for (int i = 0; i < annotationSets.size(); i++) {
      AnnotationSet annotationSet = (AnnotationSet)annotationSets.elementAt(i);
      
      // for each note in a set
	  @SuppressWarnings("unchecked")
      java.util.Vector annotations = annotationSet.getAnnotations();
      for (int j = 0; j < annotations.size(); j++) {
         Annotation annotation = (Annotation)annotations.elementAt(j);

         // change background color for each groups of lines that make up an annotation
         if (j == 0) {
            if (bgColor.equals(liteColor))
               bgColor = darkColor;
            else
               bgColor = liteColor;
         }
%>
  <TR bgColor="<%= bgColor %>">
    <TD class="req" width="566" align="left">
<%       if (j == 0) { // link to related doc if 1st line of set %>
      <A href="/UCPA/BookDetails?dt=<%= annotationSet.getDocType() %>&bk=<%= annotationSet.getBook() %>&pg=<%= annotationSet.getPage() %>">
        <%= annotation.getNote() %>
      </A>
<%       } else { %>
      <%= annotation.getNote() %>

<%       } %>
    </TD>
  </TR>
<%
      } // end annotations for loop
   } // end annotation sets for loop
%>
</TABLE>
<BR>
<%
} // end if any annotations
%>

<A href="javascript:history.go(-1)">Back to Viewer</A>

</DIV>

</BODY>
</HTML>