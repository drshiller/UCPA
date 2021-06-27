package org.ucnj.paccess;

public class QueryException extends Exception {
	private static final long serialVersionUID = 1L;

	private String callingForm = "";

	public QueryException(String aDescriptionStr, String callingForm) {
		super(aDescriptionStr);
		this.callingForm = callingForm;
	}
	
	public java.lang.String getCallingForm() {
		return callingForm;
	}
}
