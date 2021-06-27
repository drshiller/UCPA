package org.ucnj.paccess;

public class AcctMember {
	private int acctNumber = 0;
	private String name = "";
	private String password = "";
	private String email = "";
	private boolean active = false;

	public AcctMember() {
		super();
	}
	
	public AcctMember(int newAcctNumber, String newName, String newPassword, String newEmail) {
		super();
		acctNumber = newAcctNumber;
		name = newName;
		password = newPassword;
		email = newEmail;
	}
	
	public int getAcctNumber() {
		return acctNumber;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setAcctNumber(int newAcctNumber) {
		acctNumber = newAcctNumber;
	}
	
	public void setActive(boolean newActive) {
		active = newActive;
	}
	
	public void setEmail(String newEmail) {
		email = newEmail;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public void setPassword(String newPassword) {
		password = newPassword;
	}
}
