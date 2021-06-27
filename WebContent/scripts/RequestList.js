// JQuery BlockUI plugin (see http://www.malsup.com/jquery/block/)
$(function($) {
	$('#payOnline').click(function() { 
		$.blockUI({ 
			message: '<span id="payOnline">You are being redirected to our online payment system...</span>',
			css: { 
				padding: '5px',
				border: '3px solid #006633',
				backgroundColor: '#FFFFCC'
			}
		}); 
	}); 
});

function nextPage(next) {
   document.reqForm.next.value = next;
}

function validateInput()
{
	var i;
	
	if (document.reqForm.next.value == "OrderByCard" ||
		document.reqForm.next.value == "OrderByMail")
	{

		// validate # of copies fields;
		// check to see if no hidden key fields are present (i.e. empty list)
		if (document.reqForm.key == null) {
			alert("Please enter some items in the list and try again.");
			return false;
		}
		else {
			// check to see if only 1 item in list (i.e. it's not an array yet)
			if (document.reqForm.key.length == null) {
				ckey = "c_" + document.reqForm.key.value;
				sCert = document.reqForm[ckey].value;
				nCert = parseInt(sCert);
				row = 1;
				if (nCert == 0) {
					alert("Please enter a positive number of required copies in row " + row + ".");
					return false;
				}
				if (IsPositiveInteger(sCert) == false) {
					alert("Please enter a positive number of required copies in row " + row + ".");
					return false;
				}
			}
				// loop thru array of hidden key fields
			else {
				for (i = 0; i < document.reqForm.key.length; i++) {
					row = i + 1;
					ckey = "c_" + document.reqForm.key[i].value;
					sCert = document.reqForm[ckey].value;
					if (IsPositiveInteger(sCert) == false) {
						alert("Please enter a positive number of required copies in row " + row + ".");
						return false;
					}
					nCert = parseInt(sCert);
					if (nCert == 0) {
						alert("Please enter a positive number of required copies in row " + row + ".");
						return false;
					}
				}
			}
		}
	}
	return true;
}

function validateAddress() {
	if (document.reqForm.name.value == "") {
		alert("Please enter a value for the name.");
		return false;
	}
	if (document.reqForm.add1.value == "") {
		alert("Please enter a value for the address.");
		return false;
	}
	if (document.reqForm.city.value == "") {
		alert("Please enter a value for the city.");
		return false;
	}
	if (document.reqForm.state.value == "") {
		alert("Please enter a value for the state.");
		return false;
	}
	if (document.reqForm.zipcode.value == "") {
		alert("Please enter a value for the zipcode.");
		return false;
	}
	return true;
}

function IsPositiveInteger(strString)
{
	var i;
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