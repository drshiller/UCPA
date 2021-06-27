
<%-- This scriplet must be included in any JSP that uses the standard set of links --%>

<%@page import="org.ucnj.paccess.UCPA" %>
<%@page import="org.ucnj.paccess.LoggedIn" %>

<%
// switch for setting login link
String loginSwitch = "In";
LoggedIn linkBarloggedIn = (LoggedIn)getServletContext().getAttribute("LoggedIn");
if (linkBarloggedIn != null)
   if (linkBarloggedIn.contains(session.getId()))
      loginSwitch = "Out";

String calcpopup = "window.open('" 
	+ application.getInitParameter("calculatorUri") 
	+ "', 'calcpopup', 'width=700,height=650,screenX=0,screenY=0,top=0,left=0,resizable=yes,scrollbars=yes');";
%>

<DIV style="font-family: arial;" align="center">

<TABLE width="700" border="0">
  <TBODY>
    <TR>
      <TD width="700" align="center">
        <HR width="560">
        <FONT face="Arial" size="-1">
        <A href="/UCPA/DocIndex">Land Records Home</A> |
        <A href="/UCPA/DocIndex?s=name">Name Search</A> |
        <A href="/UCPA/DocIndex?s=date">Date Range Search</A> |
        <A href="/UCPA/DocIndex?s=book">Book / Page Search</A> |
        <A href="/UCPA/DocIndex?s=inst">Instrument No. Search</A>
        <BR>
        <A href="/UCPA/DocIndex?s=help">Search Help</A> |
        <A href="/UCPA/DocIndex?s=copies">Request Certified Copies Help</A> |
        <A href="/UCPA/DocIndex?s=glossary">Glossary</A> |
        <A href="/UCPA/DocIndex?s=terms">Terms of Use</A> | 
        <A href="/UCPA/DocIndex?s=comments">Comments</A> | 
        <A href="/UCPA/DocIndex?s=privacy">Privacy Policy</A>
        <BR>
        <A href="/UCPA/DocIndex?s=req">Request List</A> |
        <A href="/UCPA/DocIndex?s=cov">Cover Sheet Recording Requirement</A>
        <BR>
        <A href="http://ucnj.org/government/county-clerk/">Union County Clerk Home</A> |
        <A href="http://www.njactb.org/">New Jersey County Tax Boards Association</A>
        </FONT>
        <BR>
        <BR>
		<table width="600" border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td width="300" align="center" valign="center" >
					<A href="javascript:void(0)" onclick="<%= calcpopup %>" >
						<IMG src="grfx/calc.png" style="border-style: none;" alt="Realty Transfer Tax Calculation" title="Realty Transfer Tax Calculation">
					</A>
					<DIV style="font: 10pt arial, sans-serif; margin: 0 0 10px 0;">
						Realty Transfer Tax Calculation
					</DIV>
				 </td>
				<td width="300" align="center" valign="center">
					<A href="pdf/NewJersey-SAMPLE_ACKNOWLEDGEMENTS.pdf" >
						<IMG src="grfx/sample.jpg" style="border-style: none;" alt="New Jersey Sample Acknowledgements" title="New Jersey Sample Acknowledgements">
					</A>
					<DIV style="font: 10pt arial, sans-serif; margin: 0 0 10px 0;">
						New Jersey Sample Acknowledgements
					</DIV>
				</td>
			</tr>
		</table>
        <FONT face="Arial" size="-2">
        This Index is copyrighted 1997-2018.
        County of Union, New Jersey -- Union County Clerk.
        All rights reserved.
        <BR>
        <BR>
        The Land Document Index that you can search by is provided as a public
        service for your convenience. Neither Union County nor the Union County
        Clerk shall incur a liability for errors or omissions with respect to the
        information provided in the index.<BR>
        &copy;1997-2018 Union County Clerk
        <BR>
        Version 2.0.15
        </FONT>
      </TD>
    </TR>
  </TBODY>
</TABLE>

</DIV>
