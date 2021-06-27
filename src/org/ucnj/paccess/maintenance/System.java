package org.ucnj.paccess.maintenance;

import java.io.Serializable;

public class System implements Serializable {

	private static final long serialVersionUID = 1L;

	// status codes
	public final static int STATUS_DOWN = 0,
							STATUS_UP   = 1;
	
	private int status;
	private String message;

	public System() {
		status = STATUS_UP;
		message = "";
	}
	
	public System(int status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}
