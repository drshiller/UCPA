package org.ucnj.paccess.dataaccess.beans;

import org.ucnj.paccess.Result;

public class OrderItem {
	
	private final static double COST_CERT_FIRST_PAGE = 8.0;
	private final static double COST_CERT_OTHER_PAGE = 2.0;
	private final static double COST_PLAIN_PAGE = 2.0;
	
	private Result result;
	private int numPlain;
	private int numCertified;
	
	public OrderItem() {
		super();
		result = null;
		numPlain = 0;
		numCertified = 0;
	}
	
	public OrderItem(Result result, int numPlain, int numCertified) {
		this.result = result;
		this.numPlain = numPlain;
		this.numCertified = numCertified;
	}
	
	public double cost() {
		return costCertified() + costPlain();
	}
	
	public double costCertified() {
	
		double cost = 0.0;
		int numPages = result.getNumPages().intValue();
		if (numPages > 0) {
			cost = COST_CERT_FIRST_PAGE;
			if (numPages > 1)
				cost += ((double)(numPages-1) * COST_CERT_OTHER_PAGE);
			cost *= (double)numCertified;
		}
		return cost;
		
	}
	
	public double costPlain() {
	
		double cost = 0.0;
		int numPages = result.getNumPages().intValue();
		if (numPages > 0) {
			cost = (double)numPages * COST_PLAIN_PAGE;
			cost *= (double)numPlain;
		}
		return cost;
		
	}
	
	public int getNumCertified()
	{
		return numCertified;
	}
	
	public int getNumPlain()
	{
		return numPlain;
	}
	
	public Result getResult()
	{
		return result;
	}
}
