package org.ucnj.paccess;

import org.ucnj.paccess.servlets.Login;
import org.ucnj.paccess.servlets.SendLogin;

public class LoginStatus {

	private boolean loggedIn = false;
	
	private int loginResult = Login.RESULT_NONE;
	private String loginName = "";

	private int sendResult = SendLogin.RESULT_NONE;
	private String sendAddress = "";
	private String sendName = "";

	public LoginStatus() {
		super();
	}
	
	public String getLoginName() {
		return loginName;
	}
	
	public int getLoginResult() {
		return loginResult;
	}
	
	public String getSendAddress() {
		return sendAddress;
	}
	
	public String getSendName() {
		return sendName;
	}
	
	public int getSendResult() {
		return sendResult;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public void setLoginResult(int loginResult) {
		this.loginResult = loginResult;
	}
	
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	
	public void setSendResult(int sendResult) {
		this.sendResult = sendResult;
	}
}
