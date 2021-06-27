function validateInput()
{
	// doc type selected
	if (document.searchForm.dt.selectedIndex == 0) {
		alert("Please select a document type.");
		return false;
	}

	if (document.searchForm.from.value == "") {
		alert("Please enter a value for the 'from' date.");
		return false;
	}
	if (document.searchForm.to.value == "") {
		alert("Please enter a value for the 'to' date.");
		return false;
	}
	if (checkDates(document.searchForm) == false)
		return false;

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

function set_pOpt(pOpt)
{
	if (pOpt == "1") {
   		document.searchForm.pOpt[0].checked = true;
	}
	else if (pOpt == "2") {
  		document.searchForm.pOpt[1].checked = true;
	}
	else {
   		document.searchForm.pOpt[0].checked = true;
	}
}

function clearAll(defFromDate, defToDate, defResultsPerPage)
{
	setDocType(0);
	document.searchForm.from.value = defFromDate;
	document.searchForm.to.value = defToDate;
	document.searchForm.rpp.value = defResultsPerPage;
}

function checkDates(aForm) {

  var MAX_DAYS = 31;

  var dFrom = stringToDate(aForm.from.value);
  var dTo = stringToDate(aForm.to.value);

  var nDaysElapsed = daysElapsed(dTo, dFrom);

  var bSwapped = false;
  if (nDaysElapsed < 0) {
    var dTemp = new Date(dFrom.getTime());
    dFrom = new Date(dTo.getTime());
    dTo = new Date(dTemp.getTime());
    bSwapped = true;
  }

  var bClipped = false;
  if (Math.abs(nDaysElapsed) > MAX_DAYS) {
    dTo = new Date(dFrom.getTime() + MAX_DAYS*24*60*60*1000);
    bClipped = true;
  }

  if (bSwapped == true || bClipped == true) {
    aForm.from.value = dateToString(dFrom);
    aForm.to.value = dateToString(dTo);
  }

  if (bClipped == true) {
    var msg = "The maximum permitted date range is " + MAX_DAYS + " days.\n" +
              "The 'To' date has automatically been adjusted for you.\n" +
              "Please check the adjusted date.";
    alert(msg);
    return false;
  }
  else {
    return true;
  }
}

function stringToDate(sDate) {
  var month = sDate.substr(0, 2) - 1;
  var day = sDate.substr(3, 2);
  var year = sDate.substr(6, 4);
  return new Date(year, month, day, 0, 0, 0);
}

function dateToString(aDate) {
  var month = aDate.getMonth() + 1;
  if (month < 10) month = "0" + month;
  var day = aDate.getDate();
  if (day < 10) day = "0" + day;
  var year = aDate.getFullYear();
  return month + "/" + day + "/" + year;
}

function daysElapsed(date1, date2) {
    var difference = 
          Date.UTC(date1.getYear(), date1.getMonth() , date1.getDate(), 0, 0, 0)
        - Date.UTC(date2.getYear(), date2.getMonth(),date2.getDate(), 0, 0, 0);
    return difference/1000/60/60/24;
}
