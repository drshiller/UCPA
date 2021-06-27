package org.ucnj.paccess;

/**
 * Wrapper class for a hashmap that holds account members
 * who have logged-in to view images; this wrapper is
 * used to make sure that adding/removing members is
 * synchronized when used within a web application context
 * Creation date: (2/20/2002 9:58:19 AM)
 * @author: Administrator
 */
 
import java.util.Enumeration;
import java.util.Hashtable;

public class LoggedIn {
    private Hashtable<String, AcctMember> members = null;
    
	public LoggedIn() {
		members = new Hashtable<String, AcctMember>();
	}
	
	public synchronized void add(String sessionID, AcctMember member) {
		members.put(sessionID, member);
	}
	
	public synchronized boolean contains(String sessionID) {
		return members.containsKey(sessionID);
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable getMembers() {
		return members;
	}
	/**
	 * Remove a member based on his/her account information
	 * Creation date: (2/20/2002 10:48:07 AM)
	 * @param member org.ucnj.paccess.AcctMember
	 */
	@SuppressWarnings("unchecked")
	public synchronized void remove(AcctMember aMember) {
	
		// look thru all the available keys
		Enumeration e = members.keys();
		while (e.hasMoreElements()) {
	
			// get next key
			String sessionID = (String)e.nextElement();
	
			// pick up member based on this key; if there is a match
			// based on acct primary details then remove it
			AcctMember thisMember = (AcctMember)members.get(sessionID);
			if ((thisMember.getAcctNumber() == aMember.getAcctNumber()) &&
				thisMember.getName().equals(aMember.getName())) {
				members.remove(sessionID);
				break;
			}
				
		}
		return;
	
	}
	
	/**
	 * Remove a member based on its session ID
	 * Creation date: (2/20/2002 10:48:07 AM)
	 * @param sessionID java.lang.String
	 */
	public synchronized void remove(String sessionID) {
		members.remove(sessionID);
	}
}
