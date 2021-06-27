package org.ucnj.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author shiller
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PageNavTag extends TagSupport {
	
	private static final long serialVersionUID = 1L;
	
	private int resultsPerPage;
	private int thisPage;
	private boolean morePages;
	private String searchForm;

	public int doEndTag() throws JspTagException {
		return EVAL_PAGE;
	}
	
	public int doStartTag() throws JspTagException {
	
		try {
			JspWriter out = pageContext.getOut();
			out.println("<TABLE width='755' cellpadding='0' cellspacing='0' border='0'>");
			out.println("  <TR>");
			out.println("    <TD align='center' width='59'>");
			if (thisPage > 1) {
				out.println("      <A href='" + searchForm + "Results?page=" + (thisPage-1) + "&rpp=" + resultsPerPage + "'>");
				out.println("        <IMG src='/UCPA/grfx/previouswd.gif' width='13' height='18' border='0'>");
				out.println("      </A>");
				out.println("      <BR>");
				out.println("      <A href='" + searchForm + "Results?page=" + (thisPage-1) + "&rpp=" + resultsPerPage + "'>");
				out.println("        Previous");
				out.println("      </A>");
			}
			out.println("    </TD>");
			out.println("    <TD align='center' width='52'>");
			if (morePages) {
				out.println("      <A href='" + searchForm + "Results?page=" + (thisPage+1) + "&rpp=" + resultsPerPage + "'>");
				out.println("        <IMG src='/UCPA/grfx/nextwd.gif' width='13' height='18' border='0'>");
				out.println("      </A>");
				out.println("      <BR>");
				out.println("      <A href='" + searchForm + "Results?page=" + (thisPage+1) + "&rpp=" + resultsPerPage + "'>");
				out.println("        Next");
				out.println("      </A>");
			}
			out.println("    </TD>");
            out.println("    <TD align='right' width='644'>");
            out.println("      <A href='/UCPA/DocIndex?s=" + searchForm + "'>Back to " + searchForm + " Search Form</A>");
			out.println("    </TD>");
			out.println("  </TR>");
			out.println("</TABLE>");
		}
		catch(IOException exp) {
			throw new JspTagException("TagLibrary exception: " + exp);
		}
		
		return SKIP_BODY;
	}

	public void setThisPage(int thisPage) {
		this.thisPage = thisPage;
	}

	public void setMorePages(boolean morePages) {
		this.morePages = morePages;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public void setSearchForm(String searchForm) {
		this.searchForm = searchForm;
	}

}
