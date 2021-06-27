function validateInput()
{
	// doc type selected
	if (document.searchForm.dt.selectedIndex == 0) {
		alert("Please select a document type.");
		return false;
	}

	// book provided?
	if ((document.searchForm.bk.value == "") ||
	    (IsPositiveInteger(document.searchForm.bk.value) == false)) {
		alert("Please enter a numeric value for the book.");
		return false;
	}

	// page provided?
	if ((document.searchForm.pg.value == "") ||
	    (IsPositiveInteger(document.searchForm.pg.value) == false)) {
		alert("Please enter a numeric value for the page.");
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

function clearAll()
{
	setDocType(0);
	document.searchForm.bk.value = "";
	document.searchForm.pg.value = "";
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