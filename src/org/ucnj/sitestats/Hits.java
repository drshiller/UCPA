package org.ucnj.sitestats;

	public class Hits {
		String date;
		int ts00to04;
		int ts04to08;
		int ts08to12;
		int ts12to16;
		int ts16to20;
		int ts20to00;
	
	public Hits(String date) {
		this.date = date;
		ts00to04 = 0;
		ts04to08 = 0;
		ts08to12 = 0;
		ts12to16 = 0;
		ts16to20 = 0;
		ts20to00 = 0;
	}
	
	public int total() {
		return ts00to04 + ts04to08 + ts08to12 + ts12to16 + ts16to20;
	}
}
