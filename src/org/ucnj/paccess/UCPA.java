package org.ucnj.paccess;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import org.ucnj.paccess.dataaccess.beans.Order;
import org.ucnj.paccess.dataaccess.beans.Request;
import org.ucnj.paccess.servlets.PwMgr;

public class UCPA {

	public final static String AUDIENCE_EXTERNAL = "external";
	public final static String AUDIENCE_INTERNAL = "internal";
	
	public final static String USERS_STAFF	= "staff";
	public final static String USERS_PUBLIC	= "public";
	
	public final static String REDIRECT_PAGE = "/Redirect.html";
	
	public final static int DOC_TYPE_NONE                         			= 0;
	public final static int DOC_TYPE_DEED                         			= 1;
	public final static int DOC_TYPE_MORTGAGE                     			= 2;
	public final static int DOC_TYPE_MORTGAGE_CANCELLATION        			= 3;
	public final static int DOC_TYPE_ASSIGNMENT                   			= 4;
	public final static int DOC_TYPE_RELEASE                      			= 5;
	public final static int DOC_TYPE_DISCHARGE                    			= 6;
	public final static int DOC_TYPE_FEDERAL_TAX_LIEN             			= 10;
	public final static int DOC_TYPE_FEDERAL_TAX_LIEN_RELEASE     			= 30;
	public final static int DOC_TYPE_NOTICE_OF_SALE               			= 18;
	public final static int DOC_TYPE_NOTICE_OF_SETTLEMENT         			= 15;
	public final static int DOC_TYPE_LIS_PENDENS                  			= 7;
	public final static int DOC_TYPE_LIS_PENDENS_FORECLOSURE      			= 17;
	public final static int DOC_TYPE_LIS_PENDENS_DISCHARGE        			= 27;
	public final static int DOC_TYPE_LIS_PENDENS_IN_REM           			= 37;
	public final static int DOC_TYPE_LIS_PENDENS_IN_REM_DISMISSED 			= 38;
	public final static int DOC_TYPE_UCC                          			= 11;
	public final static int DOC_TYPE_UCC_INITIAL                  			= 40;
	public final static int DOC_TYPE_UCC_AMENDMENT                			= 41;
	public final static int DOC_TYPE_UCC_ASSIGNMENT               			= 42;
	public final static int DOC_TYPE_UCC_CONTINUATION             			= 43;
	public final static int DOC_TYPE_UCC_TERMINATION              			= 44;
	public final static int DOC_TYPE_UCC_RELEASE                  			= 45;
	public final static int DOC_TYPE_TRADE_NAMES                  			= 57;
	public final static int DOC_TYPE_TRADE_NAMES_DISSOLUTION      			= 58;
	public final static int DOC_TYPE_NUB									= 31;
	public final static int DOC_TYPE_NUB_DISCHARGE							= 50;
	public final static int DOC_TYPE_NUB_ADMENDENT							= 59;
	public final static int DOC_TYPE_CONSTRUCTION_LIEN						= 32;
	public final static int DOC_TYPE_CONSTRUCTION_LIEN_ADMENDMENT			= 33;
	public final static int DOC_TYPE_CONSTRUCTION_LIEN_BOND_RELEASE			= 34;
	public final static int DOC_TYPE_CONSTRUCTION_LIEN_DISCHARGE			= 35;
	public final static int DOC_TYPE_CONSTRUCTION_LIEN_PARTIAL_DISCHARGE	= 36;
	public final static int DOC_TYPE_NOTARY_CERTIFICATION					= 24;
	public final static int DOC_TYPE_INHERITANCE_TAX_WAIVERS				= 39;
	
	private final static String INITIAL_DATE = "06/01/1977";
	private final static Integer DEF_RESULTS_PER_PAGE = new Integer(6);
	private final static Integer MAX_RESULTS_PER_PAGE = new Integer(100);

	private LoginStatus currLoginStatus = null;
	private Request currRequest = null;
	private SearchOptions currSrcOpts = null;
	private String currIndexDate = null;
	private View currView = null;

	@SuppressWarnings("unchecked")
	private Vector results = new Vector();
	private Integer pageNum = null;
	private Boolean moreResults = new Boolean(false);
	private String lastQuery = null;

	@SuppressWarnings("unchecked")
	private Vector dates = new Vector();

	private Result mainResult = null;
	@SuppressWarnings("unchecked")
	private Vector party1Names = new Vector();
	@SuppressWarnings("unchecked")
	private Vector party2Names = new Vector();
	@SuppressWarnings("unchecked")
	private Vector miscDetails = new Vector();
	private Boolean resFound = null;

	private int pwMgrResult = PwMgr.RESULT_OK;
	private String pwMgrName = "";
	
    private Order lastOrder = null;

	private String testMsg = "";

	public UCPA() {
	
		// create empty search options
		currSrcOpts = new SearchOptions();
	
		currLoginStatus = new LoginStatus();
	
		currRequest = new Request();
		
		currView = new View();
	}

	public static String formatSpace(String inStr, int outLen, boolean inFront) {
	
		String outStr = inStr;
		
		int inLen = inStr.length();
		
	  	if (inLen < outLen) {
		  	
		  	StringBuffer aStrBuf = new StringBuffer();
		  	
		  	for (int i = 0; i < outLen-inLen; i++) {
			  	aStrBuf.append(" ");
		  	}
		  	if (inFront == true) {
				outStr = aStrBuf + inStr;
		  	}
		  	else {
				outStr = inStr + aStrBuf;
		  	}
		  	
	  	}
		  	
		return outStr;
		
	}

	public String getCurrentIndexDate(boolean longFormat) {
	
		String idxDate = "";
		
		if (longFormat == false) {
			idxDate = Result.transformDate(currIndexDate, false);
		}
		else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, new Integer(currIndexDate.substring(0, 4)).intValue());
			cal.set(Calendar.MONTH, new Integer(currIndexDate.substring(4, 6)).intValue() - 1);
			cal.set(Calendar.DAY_OF_MONTH, new Integer(currIndexDate.substring(6, 8)).intValue());
			SimpleDateFormat formatter = new SimpleDateFormat ("MMMM d, yyyy");
			idxDate = formatter.format(cal.getTime());
		}
	
		return idxDate;
		
	}

	public LoginStatus getCurrentLoginStatus() {
		return currLoginStatus;
	}

	public Request getCurrentRequest() {
		return currRequest;
	}

	public SearchOptions getCurrentSrchOpts() {
		return currSrcOpts;
	}

	public View getCurrentView() {
		return currView;
	}

	@SuppressWarnings("unchecked")
	public Vector getDates() {
		return dates;
	}

	public static Integer getDefaultResultsPerPage() {
		return DEF_RESULTS_PER_PAGE;
	}

	public Boolean getFound() {
		return resFound;
	}

	public static String getInitialDate() {
		return INITIAL_DATE;
	}

	public String getLastQuery() {
		return lastQuery;
	}

	public Result getMainResult() {
		return mainResult;
	}

	public static Integer getMaxResultsPerPage() {
		return MAX_RESULTS_PER_PAGE;
	}

	@SuppressWarnings("unchecked")
	public Vector getMiscDetails() {
		return miscDetails;
	}

	public Boolean getMore() {
		return moreResults;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	@SuppressWarnings("unchecked")
	public Vector getParty1() {
		return party1Names;
	}

	@SuppressWarnings("unchecked")
	public Vector getParty2() {
		return party2Names;
	}

	public String getPwMgrName() {
		return pwMgrName;
	}

	public int getPwMgrResult() {
		return pwMgrResult;
	}

	@SuppressWarnings("unchecked")
	public Vector getResults() {
		return results;
	}

	public String getTestMsg() {
		return testMsg;
	}

	public Order getLastOrder() {
		return lastOrder;
	}

	public static String removeSpecialChars(String in, boolean skipSpaces) {
	
		// format incoming string to sort name conventions
		// (i.e. assorted special chars removed, and in upper case)
		
		String out = "";
		String s = in.trim();
		int len = s.length();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				char aChar = s.charAt(i);
				if (skipSpaces == true && aChar == ' ')
					continue;
				if (aChar != '\'' &&
					aChar != '"'  &&
					aChar != '-'  &&
					aChar != '&'  &&
					aChar != '('  &&
					aChar != ')'  &&
					aChar != '/'  &&
					aChar != '\\' &&
					aChar != '.'  &&
					aChar != '<'  &&
					aChar != '>')
				{
						out += aChar;
				}
			}
			out = out.toUpperCase();
		}
		return out;
	}

	public static String repeatChar(int count, char aChar) {
		String outStr = "";
		for (int i = 0; i < count; i++)
			outStr += String.valueOf(aChar);
		return outStr;
	}

	public void setCurrentIndexDate(String currIndexDate) {
		this.currIndexDate = currIndexDate;
	}

	public void setCurrentView(View currView) {
		this.currView = currView;
	}

	@SuppressWarnings("unchecked")
	public void setDates(Vector dates) {
		this.dates = dates;
	}

	public void setFound(Boolean resFound) {
		this.resFound = resFound;
	}

	public void setLastQuery(String lastQuery) {
		this.lastQuery = lastQuery;
	}

	public void setMainResult(Result mainResult) {
		this.mainResult = mainResult;
	}

	@SuppressWarnings("unchecked")
	public void setMiscDetails(Vector miscDetails) {
		this.miscDetails = miscDetails;
	}

	public void setMore(Boolean moreResults) {
		this.moreResults = moreResults;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	@SuppressWarnings("unchecked")
	public void setParty1(Vector party1Names) {
		this.party1Names = party1Names;
	}

	@SuppressWarnings("unchecked")
	public void setParty2(Vector party2Names) {
		this.party2Names = party2Names;
	}

	public void setPwMgrName(String pwMgrName) {
		this.pwMgrName = pwMgrName;
	}

	public void setPwMgrResult(int pwMgrResult) {
		this.pwMgrResult = pwMgrResult;
	}

	@SuppressWarnings("unchecked")
	public void setResults(Vector results) {
		this.results = results;
	}

	public void setTestMsg(String testMsg) {
		this.testMsg = testMsg;
	}

	public void setLastOrder(Order lastOrder) {
		this.lastOrder = lastOrder;
	}

	public static void throwExMsg(String theClass, String theMethod, Exception theException) 
	throws Exception {
	
		String msg = "<B>" + theClass + ".</B><I>" + theMethod + "</I>";
		msg +=       "<BR>";
		msg +=       theException.getMessage();
	
		// looking for a "Internal drive error" message from AS/400 driver;
		// if found then change to Service Down exception;
		// otherwise just track exception
		if (theException instanceof SQLException) {
			if (((SQLException)theException).getSQLState().equalsIgnoreCase("HY000"))
				throw new ServiceDownException(msg);
			else
				throw new Exception(msg);
		}
	
		// preserve exceptions sof interest
		else if (theException instanceof ServiceDownException)
			throw new ServiceDownException(msg);
	
		else
			throw new Exception(msg);
	
	}

	public static String todaysDate() {
	
		// formatted as MM/DD/YYYY
		Calendar rightNow = Calendar.getInstance();
		int day = rightNow.get(Calendar.DAY_OF_MONTH);
		int month = rightNow.get(Calendar.MONTH) + 1;
		int year = rightNow.get(Calendar.YEAR);
	
		String today ="";
		
		if (month < 10) {
			today += "0";
		}
		today += String.valueOf(month) + "/";
		
		if (day < 10) {
			today += "0";
		}
		today += String.valueOf(day) + "/";
		
		today += String.valueOf(year);
		
		return today;
		
	}

}
