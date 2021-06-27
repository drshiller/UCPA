package org.ucnj.paccess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class View {

	private Vector<AnnotationSet> annotationSets = null;
	private String pdfUrl = "";
	private String docType = "";
	private String instrNumber = "";
	private String bookPage = "";
	
	public View() {
		super();
	}

	public Vector<AnnotationSet> getAnnotationSets() {
		return annotationSets;
	}

	public String getBookPage() {
		return bookPage;
	}

	public String getDocType() {
		return docType;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}
	
	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getInstrNumber() {
		return instrNumber;
	}

	public void setAnnotations(String annFile, String docType, String instrNumber, String bookPage)
	throws Exception {
	
		final String FIRST_LINE_STARTS_WITH = "(MSG";
		final String RECORDED_DATE = "Recorded Date: ";
		final String BOOK = " Book ";
		final String PAGE = " Page ";
		final int DATE_MIN = 1993;
		
		// msgs in
		final int SEE_DEED					= 1;
		final int SEE_MTG					= 2;
		final int SEE_MTG_CANCEL			= 3;
		final int SEE_ASSIGNMENT			= 4;
		final int SEE_RELEASE				= 5;
		final int SEE_DISCHARGE				= 6;
		final int SEE_SUBORD				= 15;
		final int SEE_POSTPONE				= 20;
		final int SEE_EXT_MTG				= 23;
		final int SEE_NUB					= 31;
		final int SEE_CONST_LIEN			= 32;
		final int SEE_CONST_LIEN_ADMENDMENT	= 33;
		final int SEE_CONST_LIEN_BOND_REL	= 34;
		final int SEE_CONST_LIEN_DIS		= 35;
		final int SEE_CONST_LIEN_PART_DIS	= 36;
		final int SEE_NUB_DISCHARGE			= 50;
		final int SEE_TRADE_NAMES_DIS		= 58;
		final int SEE_NUB_ADMENDENT			= 59;
		
		annotationSets = new Vector<AnnotationSet>();	
			
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(annFile)));
	
			// read each line in the RVI-generated annotation file
			AnnotationSet annotationSet = null;
			String inLine;
			while ((inLine = in.readLine()) != null) {
				
				// parse this annotation note
				String note = inLine.substring(0, 65);
				String openDate = inLine.substring(65, 73);
				Annotation annotation = new Annotation(note, openDate);
			
				// look for a string that indicates the 1st line of an annotation set
				if (note.startsWith(FIRST_LINE_STARTS_WITH)) {
					
					// if we were storing lines in a previous set then store it
					if (annotationSet != null)
						annotationSets.add(annotationSet);
						
					// start a new set
					annotationSet = new AnnotationSet();
					
					// the first line contains the message
					int start = FIRST_LINE_STARTS_WITH.length();
					int end = note.indexOf(")");
					int iMsg = Integer.parseInt(note.substring(start, end).trim());
					
					// translate the msg as follows
					switch (iMsg) {
					   case SEE_RELEASE:
					   case SEE_SUBORD:
					   case SEE_POSTPONE:
					   case SEE_EXT_MTG:
					      annotationSet.setDocType(String.valueOf(SEE_RELEASE));
					      break;
					   case SEE_DEED:
					   case SEE_MTG:
					   case SEE_MTG_CANCEL:
					   case SEE_ASSIGNMENT:
					   case SEE_DISCHARGE:
					   case SEE_NUB:
					   case SEE_CONST_LIEN:
					   case SEE_CONST_LIEN_ADMENDMENT:
					   case SEE_CONST_LIEN_BOND_REL:
					   case SEE_CONST_LIEN_DIS:
					   case SEE_CONST_LIEN_PART_DIS:
					   case SEE_NUB_DISCHARGE:
					   case SEE_TRADE_NAMES_DIS:
					   case SEE_NUB_ADMENDENT:
					      annotationSet.setDocType(String.valueOf(iMsg));
					      break;
					   default:
					      annotationSet.setDocType("");
					      break;
					}
				}
			
				// look for a line that begins with pound sign;
				// this line contains the book and page for reverse lookup;
				// only use if msg is an accepted type
				if (note.startsWith("#") && annotationSet.getDocType().length() > 0) {
					int start = note.indexOf(BOOK) + BOOK.length();
					int end = note.indexOf(PAGE);
					int iBook = Integer.parseInt(note.substring(start, end).trim());
					annotationSet.setBook(String.valueOf(iBook));
					start = note.indexOf(PAGE) + PAGE.length();
					int iPage = Integer.parseInt(note.substring(start).trim());
					annotationSet.setPage(String.valueOf(iPage));
				}
				
				// look for a line that indicates the recorded date;
				// check if year after minumum date; if not then don't link
				if (note.startsWith(RECORDED_DATE) && annotationSet.getDocType().length() > 0) {
					int start = note.lastIndexOf('/');
					if (start != -1) {
						int iDate = Integer.parseInt(note.substring(start+1).trim());
						if (iDate < DATE_MIN) {
							annotationSet.setDocType("");
							annotationSet.setBook("");
							annotationSet.setPage("");
						}
					}
					else {
						annotationSet.setDocType("");
						annotationSet.setBook("");
						annotationSet.setPage("");
					}
				}
				
				// store the lastest note into the current set
				if (annotationSet != null)
					annotationSet.getAnnotations().add(annotation);
			}
			
			// flush the current set
			if (annotationSet != null)
				annotationSets.add(annotationSet);
			
			in.close();
		} 
		catch (Exception ex) {
			UCPA.throwExMsg("View", "setAnnotations", ex);
		}
	
		// other info for display purposes
		this.docType = docType;
		this.instrNumber = instrNumber;
		this.bookPage = bookPage;
	}
}
