package org.ucnj.paccess;

public class Address {
	private String name = "";;
	private String address1 = "";
	private String address2 = "";
	private String city = "";
	private String state = "";
	private String zipcode = "";
	
	public Address() {
		super();
	}
	
	public Address(String name, String address1, String address2, String city, String state, String zipcode) {
		this.name = name;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
	}
	
	public String getAddress1() {
		return address1;
	}
	
	public String getAddress2() {
		return address2;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getName() {
		return name;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZipcode() {
		return zipcode;
	}
}
