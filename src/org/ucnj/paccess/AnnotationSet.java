package org.ucnj.paccess;

import java.util.Vector;

/**
 * Class that represents one set of annotations;
 * the doc type, book and page can be used for finding
 * the associated document
 */

public class AnnotationSet {
	private Vector<Annotation> annotations = new Vector<Annotation>();
	private String docType = "";
	private String book = "";
	private String page = "";
	
	public Vector<Annotation> getAnnotations() {
		return annotations;
	}

	public String getBook() {
		return book;
	}

	public String getDocType() {
		return docType;
	}

	public String getPage() {
		return page;
	}

	public void setAnnotations(Vector<Annotation> annotations) {
		this.annotations = annotations;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public void setPage(String page) {
		this.page = page;
	}

}
