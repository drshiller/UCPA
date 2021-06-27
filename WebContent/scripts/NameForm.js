function validateInput()
{
	// last name provided?
	if (document.searchForm.lName.value == "") {
		alert("Please enter a value for the last name.");
		return false;
	}

	if (document.searchForm.from.value == "") {
		alert("Please enter a value for the 'from' date.");
		return false;
	}
	if (document.searchForm.to.value == "") {
		alert("Please enter a value for the 'to'date.");
		return false;
	}

	// at least 1 doc type selected?
	if (document.searchForm.dt.length) { //multiple choices
		for (i = 0; i < document.searchForm.dt.length; i++ ) {
			if (document.searchForm.dt[i].checked) {
				return true;
			}
		}
	}
	else if (document.searchForm.dt.checked) { //single choice
		return true;
	}

	alert("Please select at least one document type option.");
	return false;
}

function setAllDocTypes(state)
{
	for (i = 0; i < document.searchForm.dt.length; i++ ) {
		document.searchForm.dt[i].checked = state;
	}
}

function set_lOpt(lOpt)
{
 	if (lOpt == "e") {
   		document.searchForm.lOpt[0].checked = true;
	}
	else if (lOpt == "s") {
   		document.searchForm.lOpt[1].checked = true;
	}
	else if (lOpt == "x") {
  		document.searchForm.lOpt[2].checked = true;
	}
	else {
   		document.searchForm.lOpt[0].checked = true;
	}
}

function set_fOpt(fOpt)
{
 	if (fOpt == "e") {
   		document.searchForm.fOpt[0].checked = true;
	}
	else if (fOpt == "s") {
   		document.searchForm.fOpt[1].checked = true;
	}
	else if (fOpt == "c") {
  		document.searchForm.fOpt[2].checked = true;
	}
	else {
   		document.searchForm.fOpt[0].checked = true;
	}
}

function set_pOpt(pOpt)
{
 	if (pOpt == "3") {
   		document.searchForm.pOpt[0].checked = true;
	}
	else if (pOpt == "1") {
   		document.searchForm.pOpt[1].checked = true;
	}
	else if (pOpt == "2") {
  		document.searchForm.pOpt[2].checked = true;
	}
	else {
   		document.searchForm.pOpt[0].checked = true;
	}
}

function setDocType(dt)
{
	var dtArray = dt.split(":");
	var i = 0;
	while (i < dtArray.length) {
		for (j = 0; j < document.searchForm.dt.length; j++ ) {
			if (document.searchForm.dt[j].value == dtArray[i]) {
				document.searchForm.dt[j].checked = true;
			}
		}
		i++;
	}
}

function clearAll(defFromDate, defToDate, defResultsPerPage)
{
	document.searchForm.lName.value = "";
	set_lOpt("e");
	document.searchForm.fName.value = "";
	set_fOpt("e");
	setAllDocTypes(false);
	document.searchForm.from.value = defFromDate;
	document.searchForm.to.value = defToDate;
	document.searchForm.rpp.value = defResultsPerPage;
	set_pOpt("3");
}