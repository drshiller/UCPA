function validateInput()
{
	// doc type selected
	if (document.searchForm.dt.selectedIndex == 0) {
		alert("Please select a document type.");
		return false;
	}

	// year provided?
	if ((document.searchForm.yr.value == "") ||
	    (IsPositiveInteger(document.searchForm.yr.value) == false)) {
		alert("Please enter a numeric value for the year.");
		return false;
	}

	// middle instrument # provided?
	if ((document.searchForm.im.value == "") ||
	    (IsPositiveInteger(document.searchForm.im.value) == false)) {
		alert("Please enter a numeric value for the instrument number.");
		return false;
	}

	return true;
}

function setDocType(dt)
{
	var i = 0;
	for (i = 0; i < document.searchForm.dt.length; i++) {
   		if (document.searchForm.dt.options[i].value == dt) {
     			document.searchForm.dt.selectedIndex = i;
      			break;
   		}
	}
}

function clearAll(defYear)
{
	setDocType(0);
	document.searchForm.yr.value = defYear;
	document.searchForm.im.value = "";
	document.searchForm.is.value = "";
}

function IsPositiveInteger(strString)
{
	var i = 0;
	var strValidChars = "0123456789";
	var strChar;
	var blnResult = true;

	//  test strString consists of valid characters listed above
	for (i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
			break;
		}
	}
	return blnResult;
}