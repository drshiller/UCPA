package org.ucnj.paccess;

import java.io.Serializable;

public class NewsItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String headline;
	private String details;
	
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
}
