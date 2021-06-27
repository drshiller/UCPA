package org.ucnj.utilities;

import java.text.StringCharacterIterator;
import java.text.CharacterIterator;
import java.util.StringTokenizer;

/**
	------------------------------------------------------------------------
	Browser Client Sniffer.	Copyright (C)2000 by Jason Pell.

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
		
	Email: 	jasonpell@hotmail.com
	Url:	http://www.geocities.com/SiliconValley/Haven/9778
	------------------------------------------------------------------------

	Converted to server side user agent client browser sniffer.
	
	See http://developer.netscape.com/docs/examples/javascript/browser_type.html for
	the original Netscape javascript version.
    
	Everything you always wanted to know about your JavaScript client
	but were afraid to ask. Creates "is_" variables indicating:
	(1) browser vendor:
		is_nav, is_ie, is_opera, is_hotjava, is_webtv, is_TVNavigator, is_AOLTV
	(2) browser version number:
		is_major (integer indicating major version number: 2, 3, 4 ...)
		is_minor (float   indicating full  version number: 2.02, 3.01, 4.04 ...)
	(3) browser vendor AND major version number
		is_nav2, is_nav3, is_nav4, is_nav4up, is_nav6, is_nav6up, is_gecko, is_ie3,
		is_ie4, is_ie4up, is_ie5, is_ie5up, is_ie5_5, is_ie5_5up, is_ie6, is_ie6up, is_hotjava3, is_hotjava3up,
		is_opera2, is_opera3, is_opera4, is_opera5, is_opera5up
	(4) JavaScript version number:
		is_js (float indicating full JavaScript version number: 1, 1.1, 1.2 ...)
	(5) OS platform and version:
		is_win, is_win16, is_win32, is_win31, is_win95, is_winnt, is_win98, is_winme, is_win2k
		is_os2
		is_mac, is_mac68k, is_macppc
		is_unix
		is_sun, is_sun4, is_sun5, is_suni86
		is_irix, is_irix5, is_irix6
		is_hpux, is_hpux9, is_hpux10
		is_aix, is_aix1, is_aix2, is_aix3, is_aix4
		is_linux, is_sco, is_unixware, is_mpras, is_reliant
		is_dec, is_sinix, is_freebsd, is_bsd
		is_vms

	See http://www.it97.de/JavaScript/JS_tutorial/bstat/navobj.html and
	http://www.it97.de/JavaScript/JS_tutorial/bstat/Browseraol.html
	for detailed lists of userAgent strings.

	Note: you don't want your Nav4 or IE4 code to "turn off" or
	stop working when new versions of browsers are released, so
	in conditional code forks, use is_ie5up ("IE 5.0 or greater") 
	is_opera5up ("Opera 5.0 or greater") instead of is_ie5 or is_opera5
	to check version in code which you want to work on future
	versions.

	Basic Data
	----------
	navigator.appName:			Netscape
	navigator.userAgent:		Mozilla/4.7 [en] (WinNT; I)
	navigator.appVersion:		4.7 [en] (WinNT; I)

	@version 1.02	Updated to 2nd October 2001 version.
    @version 1.01	Changed the name of getMinorMozillaVersion to getMozillaVersion, as this actually
    				better represents the information being returned.  11/02/2000
    @version 1.0	Converted from is object provided by Netscape.  Revised 17 May 99
*/

public class BrowserType
{
    // These are the toLowerCase working variables.
	private String strUserAgent = "";
	private String strAppVersion = "";
	
	private int intMajorMozillaVersion = 0;
	private float floatMozillaVersion = (float) 0.0;

	// nav3, nav4, nav46, nav5, nav5up, ie3, ie4, ie5, ie5up
	private String strApplication = null;
	private float floatJavaScriptVersion = (float) 0.0;
	private String strBrowserMatches = null;
	private String strPlatformMatches = null;

 
public BrowserType(String passedHTTPUserAgent) throws Exception
{
	init(passedHTTPUserAgent);
}
private void addBrowser(String strValue)
{
	this.strBrowserMatches = addValue(strValue, this.strBrowserMatches);
}
private void addPlatform(String strValue)
{
	this.strPlatformMatches = addValue(strValue, this.strPlatformMatches);
}
private String addValue(String strValue, String strFieldList)
{
	if (isEmpty(strFieldList))
		strFieldList=strValue;
	else
		strFieldList = strFieldList.concat("," + strValue);

	return strFieldList;
}
/**
 Returns "", if strString is nul, else returns original string.

 @param strString	String being passed, to check whether it is null.

 @return If strString was Null an empty string is returned, else the original value of strString.
*/
public static String checkNull (String strString)
{
	if (strString == null)
		return "";
	else
		return strString;
}
private void doBrowserCheck()
{
	// -----------------------------------------------------------------------------------
	// Netscape/Mozilla Browser Checking
	// -----------------------------------------------------------------------------------

    	// Note: Opera and WebTV spoof Navigator.  We do strict client detection. 
    	// If you want to allow spoofing, take out the tests for opera and webtv.   The "compatible" check is for MS,etc.
    	if ( strUserAgent.indexOf("mozilla")!=-1 && strUserAgent.indexOf("spoofer")==-1
        	        && strUserAgent.indexOf("compatible") ==-1 && strUserAgent.indexOf("opera")==-1
            	    && strUserAgent.indexOf("webtv")==-1 && strUserAgent.indexOf("hotjava")==-1)
	{
		// Assign it here because we know it is now a netscape product.
		setApplication("Netscape Navigator");
		addBrowser("nav");
	}

    	if (isBrowser("nav") && getMajorMozillaVersion() == 2)
		addBrowser("nav2");
    	if (isBrowser("nav") && getMajorMozillaVersion() == 3)
		addBrowser("nav3");
    	if (isBrowser("nav") && getMajorMozillaVersion() == 4)
 	    	addBrowser("nav4");

	// Some specialised values for ssfvadmins and Live Connect Compatibility.
	if (isBrowser("nav") && (getMozillaVersion() >= (float) 4.5))
 	    	addBrowser("nav45up");
	if (isBrowser("nav") && (getMozillaVersion() >= (float) 4.6 && getMozillaVersion() < (float) 4.61))
 	    	addBrowser("nav46");
        if (isBrowser("nav") && getMozillaVersion() >= (float) 4.06)
 	    	addBrowser("nav406up");

    	if (isBrowser("nav") && getMajorMozillaVersion() >= 4) 
	{
		// From version 4, it became Netscape Communicator.
		setApplication("Netscape Communicator");
		addBrowser("nav4up");
	}
	if (isBrowser("nav") && (strUserAgent.indexOf(";nav")!=-1 || strUserAgent.indexOf("; nav")!=-1))
		addBrowser("navonly");
    	if (isBrowser("nav") && getMajorMozillaVersion() == 5)
 	    	addBrowser("nav6");
	if (isBrowser("nav") && getMajorMozillaVersion() >= 5)
		addBrowser("nav6up");
	if (strUserAgent.indexOf("gecko")!=-1)
		addBrowser("gecko");

	// -----------------------------------------------------------------------------------
	// IE Browser Checking
	// -----------------------------------------------------------------------------------

	// The microsoft internet explorer indexOf is a custom edition not in javascript check...
    	if ((strUserAgent.indexOf("msie")!=-1 || strUserAgent.indexOf("microsoft internet explorer")!=-1) &&
			strUserAgent.indexOf("opera")==-1)
	{
		setApplication("Microsoft Internet Explorer");
 	    	addBrowser("ie");
	}
    	if (isBrowser("ie") && getMajorMozillaVersion() < 4)
		addBrowser("ie3");
    	if (isBrowser("ie") && getMajorMozillaVersion() == 4 && strUserAgent.indexOf("msie 5.0")==-1)
		addBrowser("ie4");
    	if (isBrowser("ie") && getMajorMozillaVersion() >= 4)
		addBrowser("ie4up");
    	if (isBrowser("ie") && getMajorMozillaVersion() == 4 && strUserAgent.indexOf("msie 5.0")!=-1)
		addBrowser("ie5");
	if (isBrowser("ie") && getMajorMozillaVersion() == 4 && strUserAgent.indexOf("msie 5.5")!=-1)
		addBrowser("ie5_5");
	if (isBrowser("ie") && !isBrowser("ie3") && !isBrowser("ie4")) 
		addBrowser("ie5up");
	if (isBrowser("ie") && !isBrowser("ie3") && !isBrowser("ie4") && !isBrowser("ie5")) 
		addBrowser("ie5_5up");
	if (isBrowser("ie") && getMajorMozillaVersion() == 4 && strUserAgent.indexOf("msie 6.")!=-1)
		addBrowser("ie6");
	if (isBrowser("ie") && !isBrowser("ie3") && !isBrowser("ie4") && !isBrowser("ie5") && !isBrowser("ie5_5")) 
		addBrowser("ie6up");

	// -----------------------------------------------------------------------------------
	// AOL Browser Checking
	// -----------------------------------------------------------------------------------

    	// KNOWN BUG: On AOL4, returns false if IE3 is embedded browser 
    	// or if this is the first browser window opened.  Thus the tests 
    	// isBrowser("aol"), isBrowser("aol3"), and isBrowser("aol4") aren't 100% reliable.
    	if (strUserAgent.indexOf("aol")!=-1)
	{
		setApplication("America Online Client");
 	    	addBrowser("aol");
	}
    	if (isBrowser("aol") && isBrowser("ie3"))
		addBrowser("aol3");
    	if (isBrowser("aol") && isBrowser("ie4"))
		addBrowser("aol4");
	if (strUserAgent.indexOf("aol 5")!=-1)
		addBrowser("aol5");
	if (strUserAgent.indexOf("aol 6")!=-1)
		addBrowser("aol6");

	// -----------------------------------------------------------------------------------
	// Opera Browser Checking
	// -----------------------------------------------------------------------------------

    	if (strUserAgent.indexOf("opera")!=-1)
	{
		setApplication("Opera");
		addBrowser("opera");
	}
	if (strUserAgent.indexOf("opera 2")!=-1 || strUserAgent.indexOf("opera/2")!=-1)
		addBrowser("opera2");
	if (strUserAgent.indexOf("opera 3")!=-1 || strUserAgent.indexOf("opera/3")!=-1)
		addBrowser("opera3");
	if (strUserAgent.indexOf("opera 4")!=-1 || strUserAgent.indexOf("opera/4")!=-1)
		addBrowser("opera4");
	if (strUserAgent.indexOf("opera 5")!=-1 || strUserAgent.indexOf("opera/5")!=-1)
		addBrowser("opera5");
	if(isBrowser("opera") && !isBrowser("opera2") && !isBrowser("opera3") && !isBrowser("opera3") && !isBrowser("opera4"))
		addBrowser("opera5up");

	// -----------------------------------------------------------------------------------
	// Miscellaneous Browser Checking
	// -----------------------------------------------------------------------------------
    	if (strUserAgent.indexOf("webtv")!=-1)
 	    {
		setApplication("Web TV");
		addBrowser("webtv");
	}

	if (strUserAgent.indexOf("navio")!=-1 || strUserAgent.indexOf("navio_aoltv")!=-1)
 	    {
		setApplication("Aol TV");
		addBrowser("aoltv");
		addBrowser("tvnavigator");
	}

        if (strUserAgent.indexOf("lynx")!=-1)
 	    {
		setApplication("Lynx");
		addBrowser("lynx");
	}

	// -----------------------------------------------------------------------------------
	// Hotjava Browser Checking
	// -----------------------------------------------------------------------------------
	if (strUserAgent.indexOf("hotjava")!=-1)
	{
		setApplication("Hotjava");
		addBrowser("hotjava");
	}

	if (isBrowser("hotjava") && getMajorMozillaVersion() == 3)
		addBrowser("hotjava3");
	if (isBrowser("hotjava") && getMajorMozillaVersion() >= 3)
		addBrowser("hotjava3up");			
}
private void doJavaScriptCheck()
{
	if (isBrowser("nav2") || isBrowser("ie3"))
		setJavaScriptVersion((float) 1.0); 
	else if (isBrowser("nav3") || isBrowser("opera"))
		setJavaScriptVersion((float) 1.1); 
	else if ((isBrowser("nav4") && getMozillaVersion() <= 4.05) || isBrowser("ie4"))
		setJavaScriptVersion((float) 1.2); 
	else if ((isBrowser("nav4") && getMozillaVersion() > 4.05) || isBrowser("ie5"))
		setJavaScriptVersion((float) 1.3);
	else if (isBrowser("hotjava3up"))
		setJavaScriptVersion((float) 1.4);
	else if (isBrowser("nav6") || isBrowser("gecko"))
		setJavaScriptVersion((float) 1.5);
		
	// NOTE: In the future, update this code when newer versions of JS 
	// are released. For now, we try to provide some upward compatibility 
	// so that future versions of Nav and IE will show they are at 
	// *least* JS 1.x capable. Always check for JS version compatibility 
	// with > or >=. 
	else if (isBrowser("nav6up"))
		setJavaScriptVersion((float) 1.5); 
	else if (isBrowser("ie5up"))// NOTE: ie5up on mac is 1.4
		setJavaScriptVersion((float) 1.3);
		
	// HACK: no idea for other browsers) always check for JS version with > or >= 
	else
		setJavaScriptVersion((float) 0.0); 
}
	private void doPlatformCheck()
	{
    	// Windows Platforms.
    	if (strUserAgent.indexOf("win")!=-1 || strUserAgent.indexOf("16bit")!=-1)
			addPlatform("win");

    	// NOTE: On Opera 3.0, the userAgent string includes "Windows 95/NT4" on all
    	//        Win32, so you can't distinguish between Win95 and WinNT.
    	if (strUserAgent.indexOf("win95")!=-1 || strUserAgent.indexOf("windows 95")!=-1)
			addPlatform("win95");

    	// is this a 16 bit compiled version?
    	if (strUserAgent.indexOf("win16")!=-1 ||
               strUserAgent.indexOf("16bit")!=-1 || strUserAgent.indexOf("windows 3.1")!=-1 ||
               strUserAgent.indexOf("windows 16-bit")!=-1)
			addPlatform("win16");  			   

    	if (strUserAgent.indexOf("windows 3.1")!=-1 || strUserAgent.indexOf("win16")!=-1 ||
                    strUserAgent.indexOf("windows 16-bit")!=-1)
			addPlatform("win31");

    	if (strUserAgent.indexOf("winnt")!=-1 || strUserAgent.indexOf("windows nt")!=-1)
			addPlatform("winnt");
		if (strUserAgent.indexOf("win 9x 4.90")!=-1)
			addPlatform("winme");
		if (strUserAgent.indexOf("win 9x 4.90")!=-1)
			addPlatform("win2k");

		// NOTE: Reliable detection of Win98 may not be possible. It appears that:
    	//       - On Nav 4.x and before you'll get plain "Windows" in userAgent.
    	//       - On Mercury client, the 32-bit version will return "Win98", but
    	//         the 16-bit version running on Win98 will still return "Win95".
    	if (strUserAgent.indexOf("win98")!=-1 || strUserAgent.indexOf("windows 98")!=-1)
			addPlatform("win98");

    	if (isPlatform("win95") || isPlatform("winnt") || isPlatform("win98") ||
                    //(getMajorMozillaVersion() >= 4 && isPlatform("Win32") ||
                    strUserAgent.indexOf("win32")!=-1 || strUserAgent.indexOf("32bit")!=-1)
			addPlatform("win32");

    	if (strUserAgent.indexOf("os/2")!=-1 ||
                    strAppVersion.indexOf("OS/2")!=-1 ||
                    strUserAgent.indexOf("ibm-webexplorer")!=-1)
			addPlatform("os2");

		// Macintosh platforms.
    	if (strUserAgent.indexOf("mac")!=-1)
			addPlatform("mac");
    	if (isPlatform("mac") && (strUserAgent.indexOf("68k")!=-1 ||
                               strUserAgent.indexOf("68000")!=-1))
			addPlatform("mac68k");
    	if (isPlatform("mac") && (strUserAgent.indexOf("ppc")!=-1 ||
                                strUserAgent.indexOf("powerpc")!=-1))
			addPlatform("macppc");

		// *nix platforms.
    	if (strUserAgent.indexOf("sunos")!=-1)
			addPlatform("sun");
    	if (strUserAgent.indexOf("sunos 4")!=-1)
			addPlatform("sun4");
    	if (strUserAgent.indexOf("sunos 5")!=-1)
			addPlatform("sun5");
    	if (isPlatform("sun") && strUserAgent.indexOf("i86")!=-1)
			addPlatform("suni86");
    	if (strUserAgent.indexOf("irix") !=-1)    // SGI
			addPlatform("irix");
    	if (strUserAgent.indexOf("irix 5") !=-1)
			addPlatform("irix5");
    	if (strUserAgent.indexOf("irix 6") !=-1 || strUserAgent.indexOf("irix6") !=-1)
			addPlatform("irix6");
    	if (strUserAgent.indexOf("hp-ux")!=-1)
			addPlatform("hpux");
    	if (isPlatform("hpux") && strUserAgent.indexOf("09.")!=-1)
			addPlatform("hpux9");
    	if (isPlatform("hpux") && strUserAgent.indexOf("10.")!=-1)
			addPlatform("hpux10");
    	if (strUserAgent.indexOf("aix") !=-1)      // IBM
			addPlatform("aix");
    	if (strUserAgent.indexOf("aix 1") !=-1)
			addPlatform("aix1");
    	if (strUserAgent.indexOf("aix 2") !=-1)
			addPlatform("aix2");
    	if (strUserAgent.indexOf("aix 3") !=-1)
			addPlatform("aix3");
    	if (strUserAgent.indexOf("aix 4") !=-1)
			addPlatform("aix4");
    	if (strUserAgent.indexOf("inux")!=-1)
			addPlatform("linux");
    	if (strUserAgent.indexOf("sco")!=-1 || strUserAgent.indexOf("unix_sv")!=-1)
			addPlatform("sco");
    	if (strUserAgent.indexOf("unix_system_v")!=-1)
			addPlatform("unixware");
    	if (strUserAgent.indexOf("ncr")!=-1)
			addPlatform("mpras");
    	if (strUserAgent.indexOf("reliantunix")!=-1)
			addPlatform("reliant");
    	if (strUserAgent.indexOf("dec")!=-1 || strUserAgent.indexOf("osf1")!=-1 ||
    				       	strUserAgent.indexOf("dec_alpha")!=-1 || strUserAgent.indexOf("alphaserver")!=-1 ||
           					strUserAgent.indexOf("ultrix")!=-1 || strUserAgent.indexOf("alphastation")!=-1)
			addPlatform("dec");
    	if (strUserAgent.indexOf("sinix")!=-1)
			addPlatform("sinix");
    	if (strUserAgent.indexOf("freebsd")!=-1)
			addPlatform("freebsd");
    	if (strUserAgent.indexOf("bsd")!=-1)
			addPlatform("bsd");
    	if (strUserAgent.indexOf("x11")!=-1 || isPlatform("sun") || isPlatform("irix") || isPlatform("hpux") ||
    			             isPlatform("sco") ||isPlatform("unixware") || isPlatform("mpras") || isPlatform("reliant") || 
                			 isPlatform("dec") || isPlatform("sinix") || isPlatform("aix") || isPlatform("linux") || isPlatform("bsd") || isPlatform("freebsd"))
			addPlatform("unix");
		if (strUserAgent.indexOf("vax")!=-1 || strUserAgent.indexOf("openvms")!=-1)
			addPlatform("vms");
	}
	/**
		Returns descriptive name of the application, ie Netscape Navigator, Microsoft Internet Explorer, etc.
	*/
	public String getApplication()
	{
		return this.strApplication;
	}
	/**
		@return string containing all matching references for the current browser.
	*/
	public String getBrowser()
	{
		return this.strBrowserMatches;
	}
/**
Will return the index of the strValue in , delimited strFieldList.
indexing starts at 0.

@param strValue		The value to look for in the fieldlist.
@param strFieldList	The, well fieldlist...
@param strDelimit	Specifies the delimiter that strFieldList is,
					well delimited with...
*/
public static int getIndexOf(String strValue, String strFieldList, String strDelimit)
{
	try {
		if (isEmpty(strFieldList) || isEmpty(strValue))
			return -1;

		StringTokenizer tok = new StringTokenizer(strFieldList, strDelimit);
		for (int i=0; tok.hasMoreTokens(); i++) {
			if (strValue.equals(checkNull(tok.nextToken()).trim()))
				return i;
		}

   		return -1;
	}
	catch (Exception e) {
		return -1;
	}
}
	public float getJavaScriptVersion()
	{
		return this.floatJavaScriptVersion;
	}
	public int getMajorMozillaVersion()
	{
		return this.intMajorMozillaVersion;
	}
private static int getMajorVersion(String strVersionNumber)
{
	try {
		if (strVersionNumber==null || strVersionNumber.equals(""))
			return -1;
		else {
			if (strVersionNumber.indexOf(".")!=-1)
				return Integer.valueOf(strVersionNumber.substring(0, strVersionNumber.indexOf("."))).intValue();
			else
				return Integer.valueOf(strVersionNumber).intValue();
		}
	}
	catch (Exception e) {
		return -1;
	}
}
	public float getMozillaVersion()
	{
		return this.floatMozillaVersion;
	}
	public String getPlatform()
	{
		return this.strPlatformMatches;
	}
/**
Converts the number to a full float version number.
*/
private static float getVersion(String strVersionNumber)
{
	try {
		if (strVersionNumber==null || strVersionNumber.equals(""))
			return (float) -1;
		else
			return Float.valueOf(strVersionNumber).floatValue();
	}
	catch (Exception e) {
		return (float) -1;
	}
}
/**
	Attempt to parse out the version number from a HTTP userAgent.  Everything before the / should
	have already been removed.

        @param strAppVersion

        @returns the complete browser version, minus any character subversion information, e.g
        	2.01g we would return "2.01" only.  Returns null on failure.
*/
private static String getVersionNumber(String strAppVersion)
{
	try {
		if (strAppVersion==null|| strAppVersion.equals(""))
			return null;
		else
			strAppVersion=strAppVersion.trim().toLowerCase();

		StringCharacterIterator iter = new StringCharacterIterator(strAppVersion);
		String strReturnNumber = "";
		boolean dotFound=false;

		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			if ((c>='0'&&c<='9') || (!dotFound&&c=='.')) {
				if (c=='.')
					dotFound=true;
    				strReturnNumber = strReturnNumber.concat(c+"");
			}
			else
				break;
		}

		return strReturnNumber;
	}
	catch (Exception e) {
		return null;
	}
}
	/**
		Can repeatedly call this method for each new HTTPUserAgent if required.
	*/
	public void init(String passedHTTPUserAgent) throws Exception
	{
		// Initialise the Match Strings.
		strApplication = "";
		floatJavaScriptVersion = (float) 0.0;
		strBrowserMatches = "";
		strPlatformMatches = "";

       	if (isEmpty(passedHTTPUserAgent))
           	throw new Exception("User Agent String is Empty");
        else
			this.strUserAgent = passedHTTPUserAgent.toLowerCase();

		if (strUserAgent.indexOf("/")!=-1)
			strAppVersion=strUserAgent.substring(strUserAgent.indexOf("/")+"/".length());
		else if (strUserAgent.indexOf("mozilla ")!=-1)
	   	    strAppVersion=strUserAgent.substring(strUserAgent.indexOf("mozilla ")+"mozilla ".length());
       	else
			strAppVersion = strUserAgent;

        String strVersionNumber = getVersionNumber(strAppVersion);
	   	intMajorMozillaVersion = getMajorVersion(strVersionNumber);
    	floatMozillaVersion = getVersion(strVersionNumber);
        strVersionNumber=null;
	
	   	doBrowserCheck();
		doJavaScriptCheck();
		doPlatformCheck();
	}
	public boolean isBrowser(String strValue)
	{
		return getIndexOf(strValue, this.strBrowserMatches, ",")!=-1;
	}
/**
 This method will return true if strValue is not null or an empty string (&quot;&quot;)

 @param strValue		The String to be checked for &quot;emptiness&quot;
*/
public static boolean isEmpty (String strValue)
{
	if (strValue==null || strValue.equals("") || strValue.length() == 0)
		return true;
	else
		return false;
}
	public boolean isPlatform(String strValue)
	{
        return getIndexOf(strValue, this.strPlatformMatches, ",")!=-1;
	}
public static void main (String args[])
{
	try
	{
		// test various User Agent strings that appear in HTTP request header
		BrowserType isObject = new BrowserType("Mozilla/4.6 [en-gb] (WinNT; I)");
		isObject.init("Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		isObject.init("Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:0.9.5) Gecko/2001101");
		isObject.init("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; Q312461;.NET CLR 1.0.3705)");
		isObject.init("Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0)");
		isObject.init("Mozilla/4.74 [en] (Windows NT 5.0; U)");

		System.out.println("JSVersion: " + isObject.getJavaScriptVersion());
        	System.out.println("Version: " + isObject.getMozillaVersion());
		System.out.println("Major: " + isObject.getMajorMozillaVersion());
		System.out.println("Application Name: " + isObject.getApplication());
		System.out.println("Browser: " + isObject.getBrowser());
		System.out.println("Platform: " + isObject.getPlatform());

		if(isObject.isBrowser("nav46"))
			System.out.println("Netscape Communicator 4.6 does not function correctly with Live Connect");
	}
	catch (Exception e)
	{
		System.err.println("Error occurred in BrowserType: " + e);
	}
}
	private void setApplication(String passedApplication)
	{
		this.strApplication = passedApplication;
	}
	private void setJavaScriptVersion(float passedJavaScriptVersion)
	{
		this.floatJavaScriptVersion = passedJavaScriptVersion;
	}
}
