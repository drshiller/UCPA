package org.ucnj.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class BackNavTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	private int resultsPerPage;
	private int thisPage;
	private String searchForm;
	private String text;

	@Override
	public int doEndTag() throws JspException 
	{
		JspWriter out = pageContext.getOut();
		try {
//			out.println("<a href='" + searchForm + "Results?page=" + thisPage + "&rpp=" + resultsPerPage + "'>");
out.println("<a href='BookDetails'>");
			out.println(text);
			out.println("</a>");
		}
		catch(IOException exp) {
			throw new JspTagException("TagLibrary exception: " + exp);
		}
		
		return EVAL_PAGE;
	}
	
	@Override
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public void setThisPage(int thisPage) {
		this.thisPage = thisPage;
	}
	
	public void setSearchForm(String searchForm) {
		this.searchForm = searchForm;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
