
function validateInput()
{
	// validate address fields
	if (document.joinForm.name.value == "") {
		alert("Please enter a value for the name.");
		return false;
	}
	if (document.joinForm.add1.value == "") {
		alert("Please enter a value for the address.");
		return false;
	}
	if (document.joinForm.city.value == "") {
		alert("Please enter a value for the city.");
		return false;
	}
	if (document.joinForm.state.value == "") {
		alert("Please enter a value for the state.");
		return false;
	}
	if (document.joinForm.zipcode.value == "") {
		alert("Please enter a value for the zipcode.");
		return false;
	}
	if (document.joinForm.phone.value == "") {
		alert("Please enter a value for the phone number.");
		return false;
	}
	if (document.joinForm.email.value == "") {
		alert("Please enter a value for the primary e-mail address.");
		return false;
	}
	if ((document.joinForm.nusers.value == "") ||
	    (IsNumeric(document.joinForm.nusers.value) == false)) {
		alert("Please enter a numeric value for the number of users.");
		return false;
	}

	return true;
}

function IsNumeric(strString)
{
	var i;
	var strValidChars = "0123456789.-";
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