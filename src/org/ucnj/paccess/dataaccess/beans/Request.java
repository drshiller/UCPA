package org.ucnj.paccess.dataaccess.beans;

import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

import org.ucnj.paccess.Address;
import org.ucnj.paccess.Result;

public class Request {
	private Hashtable<String, OrderItem> list = new Hashtable<String, OrderItem>();
	private Address address = new Address();

	public Request() {
		super();
	}
	
	public void add(Result result) {
		OrderItem item = new OrderItem(result, 0, 0);
		list.put(makeKey(result), item);
	}
	
	public void add(Result result, int numPlain, int numCert) {
		OrderItem item = new OrderItem(result, numPlain, numCert);
		list.put(makeKey(result), item);
	}
	
	public void add(Result result, int numCert) {
		OrderItem item = new OrderItem(result, 0, numCert);
		list.put(makeKey(result), item);
	}
	
	@SuppressWarnings("unchecked")
	public double cost() {
		double cost = 0.0;
		Enumeration e = list.elements();
		while (e.hasMoreElements()) {
			OrderItem item = (OrderItem)e.nextElement();
			cost += item.cost();
		}
		return cost;
	}
	
	public String costString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		return nf.format(this.cost());
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable getList() {
		return list;
	}
	
	private String makeKey(Result result) {
		return (result.getDocType().toString() + "_" +
				Result.transformDate(result.getStampDate(), true)  + "_" +
				result.getInstrPrefix() + "_" +
				result.getInstrMiddle()  + "_" +
				result.getInstrSuffix());
	}
}
