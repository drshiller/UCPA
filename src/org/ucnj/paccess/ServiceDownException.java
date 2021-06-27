package org.ucnj.paccess;

public class ServiceDownException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceDownException(String aDescriptionStr) {
		super(aDescriptionStr);
	}
}
