package org.ucnj.paccess;

import java.io.Serializable;
import java.util.List;

public class News implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<NewsItem> news;

	public List<NewsItem> getNews() {
		return news;
	}

	public void setNews(List<NewsItem> news) {
		this.news = news;
	}
}
