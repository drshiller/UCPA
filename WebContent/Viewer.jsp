<HTML>

<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Union County Clerk's Office Public Land Records</TITLE>

<link href="theme/Master.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />

<script type="text/javascript" src="/UCPA/scripts/Viewer.js"></script>

<STYLE type="text/css">
<!--
TD{
  font-family : Arial;
}
TD.req{
  font-size: 12px;
}
TD.hd{
  font-size: 12px;
  font-weight: bold;
  color: #ffffff;
}

div#adobe
{
  width: 600px;
  border: solid 1px black;
  padding: 5px;
  background-color: white;
  display: none;
}
div#pdf
{
  margin: 0px 20px 0px 20px;
  display: none;
}
-->
</STYLE>

<%-- This scriplet must be included to track member login state --%>
<%@ include file="includes/LoginStatus.jsp" %>

<%@ page 
import="org.ucnj.paccess.AnnotationSet, 
        org.ucnj.paccess.Annotation,
        org.ucnj.paccess.SearchOptions" 
errorPage="error.jsp" 
session="true" 
isThreadSafe="true"
isErrorPage="false" 
%>

<%
Integer thisPage = userUCPArq.getPageNum();
SearchOptions currOpts = userUCPArq.getCurrentSrchOpts();
String formLink = currOpts.getFormLink();
String sType = "";
Integer resultsPerPage = null;
if (formLink.equals("name") == true) {
	resultsPerPage = new Integer(currOpts.getResultsPerPage());
	sType = "Name";
}
else if (formLink.equals("date") == true) {
	resultsPerPage = new Integer(currOpts.getResultsPerPageDR());
	sType = "Date";
}
%>

</HEAD>

<BODY background="grfx/wood1.gif" bgcolor="#FFFFFF" vlink="#006633" link="#006633" alink="#993300">

<iframe style="overflow:hidden; border:0px;" frameborder="0" 
  src="PDFReaderDetect_frame.html" width="0" height="0">
</iframe>

<DIV align="center" STYLE="font-family: Arial">

<IMG src="grfx/logoheader3.gif" border="0">
<BR>

<%
java.util.Vector<AnnotationSet> annotationSets = userUCPArq.getCurrentView().getAnnotationSets();
if (annotationSets.size() > 0) {
   String liteColor = "#ffffcc";
   String darkColor = "#99ff99";
   String bgColor = darkColor;
%>
<BR>
<TABLE cellpadding="2" cellspacing="0" width="470" border="0">
  
  <TR bgColor="#006633">
    <TD class="hd" width="466" align="left">
      Annotations -- If an annotation is underlined then click it to go directly to its record.
      <BR>
      Please be sure to check the document image below for additional annotations.
    </TD>
  </TR>
<%
   // for each set of annotation notes
    for (AnnotationSet annotationSet : annotationSets) {
      boolean isNewSet = true;
      
      // for each note in a set
      java.util.Vector<Annotation> annotations = annotationSet.getAnnotations();
      for (Annotation annotation : annotations) {

         // change background color for each groups of lines that make up an annotation
         if (isNewSet == true) {
            if (bgColor.equals(liteColor)) {
               bgColor = darkColor;
            }
            else {
               bgColor = liteColor;
            }
         }
%>
  <TR bgColor="<%= bgColor %>">
    <TD class="req" width="466" align="left">
<%       if (isNewSet && annotationSet.getDocType().length() > 0) {  // link to related doc if 1st line of set %>
      <A href="BookDetails?dt=<%= annotationSet.getDocType() %>&bk=<%= annotationSet.getBook() %>&pg=<%= annotationSet.getPage() %>">
        <%= annotation.getNote() %>
      </A>
<%       } else { %>
      <%= annotation.getNote() %>

<%       } %>
    </TD>
  </TR>
<%
	  	isNewSet = false;
      } // end annotations for loop
   } // end annotation sets for loop
%>
  <TR>
    <TD width="466" align="center">
      <FORM method="POST" action="Viewer">
        <INPUT type="submit" name="notesOnly" value="View Annotations in Print Format">
      </FORM>
    </TD>
  </TR>
</TABLE>

<%
} // end if any annotations
%>

<div id="adobe">
	<div style="color: red; font-size: 14pt; font-weight: bold;">
		Warning!
	</div>
	<div style="text-align: left; margin-top: 10px;">
		Your document cannot be displayed because your browser is not using Adobe Reader.
		Click on the button below to install the Adobe Reader.
		Once the installation is complete, refresh the page to view your document.
	</div>
	<div style="margin-top: 10px;">
		<a href="#" onclick="popupAdobe()" >
			<img src="grfx/get_adobe_reader.png" style="border: none;" />
		</a>
	</div>
</div>
<div id="pdf">
	<iframe name="pdfFrame" style="width: 95%; height: 80%" 
		src="<%= userUCPArq.getCurrentView().getPdfUrl() %>">
	</iframe>
</div>

<BR>
<BR>
</DIV>

<%-- This scriplet must be included to use the standard set of links --%>
<%@ include file="includes/LinkBar.jsp" %>

</BODY>
</HTML>