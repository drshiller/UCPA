/******************************************************************
   convertDate()
   
   Function to convert supplied dates to format - dd/mm/yyyy.
	Valid input dates = 
		ddmmyy, ddmmmyy, ddmmyyyy, ddmmmyyyy,
		d/m/yy, dd/m/yy, d/mm/yy, dd/mm/yy, d/mmm/yy, dd/mmm/yy,
		d/m/yyyy, dd/m/yyyy, d/mm/yyyy, dd/mm/yyyy, d/mmm/yyyy, dd/mmm/yyyy
	Valid date seperators =
		'-','.','/',' ',':','_',','
		
	Calls convertMonth()
			invalidDate()
			validateDate()
			validateYear()

*******************************************************************/
function convertDate(field1, isDayFirst)
{
	var fLength = field1.value.length;			// Length of supplied field in characters.
	var divider_values = new Array ('-','.','/',' ',':','_',',');	 // Array to hold permitted date seperators.  Add in '\' value
	var array_elements = 7; 			// Number of elements in the array - divider_values.
	var day1 = new String(null); 			// day value holder
	var month1 = new String(null);			// month value holder
	var year1 = new String(null);			// year value holder
	var divider1 = null;				// divider holder
	var outdate1 = null;				// formatted date to send back to calling field holder
	var counter1 = 0;				// counter for divider looping 
	var divider_holder = new Array ('0','0','0');		// array to hold positions of dividers in dates
	var s = String(field1.value);			// supplied date value variable

	// If field is empty do nothing
	if ( fLength == 0 )  {
		return true;
	}

	// Deal with today or now
	if ( field1.value.toUpperCase() == 'NOW' || field1.value.toUpperCase() == 'TODAY' ) {
   
		var newDate1 = new Date();
	
		var myYear1;
//  		if (navigator.appName == "Netscape") {
//    			myYear1 = newDate1.getYear() + 1900;
//  		}
//  		else {
  			myYear1 = newDate1.getFullYear();
//  		}
  
		var myMonth1 = newDate1.getMonth()+1;  
		var myDay1 = newDate1.getDate();
		if (isDayFirst == true) {
			field1.value = myDay1 + "/" + myMonth1 + "/" + myYear1;
		}
		else {
			field1.value = myMonth1 + "/" + myDay1 + "/" + myYear1;
		}
		fLength = field1.value.length;//re-evaluate string length.
		s = String(field1.value)//re-evaluate the string value.
	}

	//Check the date is the required length
	if ( fLength != 0 && (fLength < 6 || fLength > 11) ) {
		invalidDate(field1);
		return false;   
	}

	// Find position and type of divider in the date
	for ( var i=0; i<3; i++ ) {
		for ( var x=0; x<array_elements; x++ ) {
			if ( s.indexOf(divider_values[x], counter1) != -1 ) {
				divider1 = divider_values[x];
				divider_holder[i] = s.indexOf(divider_values[x], counter1);
		   		//alert(i + " divider1 = " + divider_holder[i]);
				counter1 = divider_holder[i] + 1;
				//alert(i + " counter1 = " + counter1);
				break;
			}
 		}
	 }

	// if element 2 is not 0 then more than 2 dividers have been found so date is invalid.
	if ( divider_holder[2] != 0 ) {
  		 invalidDate(field1);
		return false;   
	}

	// See if no dividers are present in the date string.
	if ( divider_holder[0] == 0 && divider_holder[1] == 0 ) { 
   
		// continue processing
		if ( fLength == 6 ) { //ddmmyy

			if (isDayFirst == true) {
   				day1 = field1.value.substring(0,2);
     				month1 = field1.value.substring(2,4);
			}
			else {
				day1 = field1.value.substring(2,4);
				month1 = field1.value.substring(0,2);
			}

  			year1 = field1.value.substring(4,6);
  			if ( (year1 = validateYear(year1)) == "" ) {
   				invalidDate(field1);
				return false; 
			}
		}
			
		else if ( fLength == 7 ) { //ddmmmy
  			if (isDayFirst == true) {
   				day1 = field1.value.substring(0,2);
     				month1 = field1.value.substring(2,5);
			}
			else {
				day1 = field1.value.substring(2,5);
				month1 = field1.value.substring(0,2);
			}
  			year1 = field1.value.substring(5,7);
  			if ( (month1 = convertMonth(month1)) == "" ) {
   				invalidDate(field1);
				return false; 
			}
  			if ( (year1 = validateYear(year1)) == "" ) {
   				invalidDate(field1);
				return false; 
			}
		}

		else if ( fLength == 8 ) { //ddmmyyyy
			if (isDayFirst == true) {
   				day1 = field1.value.substring(0,2);
     				month1 = field1.value.substring(2,4);
			}
			else {
				day1 = field1.value.substring(2,4);
				month1 = field1.value.substring(0,2);
			}
  			year1 = field1.value.substring(4,8);
		}

		else if ( fLength == 9 ) { //ddmmmyyyy
  			if (isDayFirst == true) {
   				day1 = field1.value.substring(0,2);
     				month1 = field1.value.substring(2,5);
			}
			else {
				day1 = field1.value.substring(2,5);
				month1 = field1.value.substring(0,2);
			}
  			year1 = field1.value.substring(5,9);
  			if ( (month1 = convertMonth(month1)) == "" ) {
   				invalidDate(field1);
				return false; 
			}
		}
		
		if ( (outdate1 = validateDate(day1,month1,year1, isDayFirst)) == false ) {
  			if (isDayFirst == true) {
   				alert("The value " + field1.value + " is not a valid date.\n\r" +  
				          "Please enter a valid date in the format dd/mm/yyyy");
			}
			else {
   				alert("The value " + field1.value + " is not a valid date.\n\r" +  
				          "Please enter a valid date in the format mm/dd/yyyy");
			}
			field1.focus();
			field1.select();
			return false;
		}

		field1.value = outdate1;
		return true; // All OK
	}
		
	// 2 dividers are present so continue to process	
	if ( divider_holder[0] != 0 && divider_holder[1] != 0 ) {
		if (isDayFirst == true) {
  			day1 = field1.value.substring(0, divider_holder[0]);
  			month1 = field1.value.substring(divider_holder[0] + 1, divider_holder[1]);
		}
		else {
  			month1 = field1.value.substring(0, divider_holder[0]);
  			day1= field1.value.substring(divider_holder[0] + 1, divider_holder[1]);
		}
  		//alert(month1);
  		year1 = field1.value.substring(divider_holder[1] + 1, field1.value.length);
	}

	if ( isNaN(day1) && isNaN(year1) ) { // Check day and year are numeric
		invalidDate(field1);
		return false;  
   	}

	if ( day1.length == 1 ) {  //Make d day dd
   		day1 = '0' + day1;  
	}

	if ( month1.length == 1 ) {//Make m month mm
		month1 = '0' + month1;   
	}

	if ( year1.length == 2 ) {//Make yy year yyyy
		if ( (year1 = validateYear(year1)) == "" ) {
   			invalidDate(field1);
			return false;  
		}
	}

	if ( month1.length == 3 || month1.length == 4 ) {//Make mmm month mm
  		if ( (month1 = convertMonth(month1)) == "" ) {
   			alert("month1" + month1);
   			invalidDate(field1);
   			return false;  
		}
	}

	// Date components are OK
	if ( (day1.length == 2 || month1.length == 2 || year1.length == 4) == false) {
   		invalidDate(field1);
   		return false;
	}

	//Validate the date
	if ( (outdate1 = validateDate(day1, month1, year1, isDayFirst)) == false ) {
   		alert("The value " + field1.value + " is not a valid date.\n\r" +  
		"Please enter a valid date in the format dd/mm/yyyy");
		field1.focus();
		field1.select();
		return false;
	}

	// Redisplay the date in dd/mm/yyyy format
	field1.value = outdate1;
	return true; //All is well

}

/******************************************************************
   convertMonth()
   
   Function to convert mmm month to mm month 
   
   Called by convertDate()    

*******************************************************************/
function convertMonth(monthIn) {

	var month_values = new Array ("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");

	monthIn = monthIn.toUpperCase(); 

	if ( monthIn.length == 3 ) {
		for ( var i = 0; i < 12; i++ )  {
   			if ( monthIn == month_values[i] ) {
				monthIn = i + 1;
				if ( i != 10 && i != 11 && i != 12 ) {
   					monthIn = '0' + monthIn;
				}
				return monthIn;
			}
		}
	}

	else if ( monthIn.length == 4 && monthIn == 'SEPT') {
		monthIn = '09';
		return monthIn;
	}
//	
//	else {
//		return "";
//	}
	
	return "";
}

/******************************************************************
   invalidDate()
   
   If an entered date is deemed to be invalid, invali
   d_date() is called to display a warning message to
   the user.  Also returns focus to the date  in que
   stion and selects the date for edit.
        
   Called by convertDate()
  
*******************************************************************/
function invalidDate(inField) 
{
	alert("The value " + inField.value + " is not in a vaild date format.\n\r" + 
        	"Please enter date in the format dd/mm/yyyy");
	inField.focus();
	inField.select();
	return true   
}

/******************************************************************
   validateDate()
   
   Validates date output from convertDate().  Checks
   day is valid for month, leap years, month !> 12,.

*******************************************************************/
function validateDate(day2, month2, year2, isDayFirst2)
{
	var DayArray = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
	var MonthArray = new Array("01","02","03","04","05","06","07","08","09","10","11","12");
	var inpDate = day2 + month2 + year2;

	var filter=/^[0-9]{2}[0-9]{2}[0-9]{4}$/;

	//Check ddmmyyyy date supplied
	if (! filter.test(inpDate)) {
		return false;
	} 

	/* Check Valid Month */
	filter=/01|02|03|04|05|06|07|08|09|10|11|12/ ;
	if (! filter.test(month2)) {
		return false;
  	}

	/* Check For Leap Year */
	var N = Number(year2);
	if ( ( N%4==0 && N%100 !=0 ) || ( N%400==0 ) ) {
		DayArray[1]=29;
  	} 

	/* Check for valid days for month */
	for(var ctr=0; ctr<=11; ctr++) {
		if (MonthArray[ctr]==month2) {
			if (day2<= DayArray[ctr] && day2 >0 ) {
				if (isDayFirst2 == true) {
					inpDate = day2 + '/' + month2 + '/' + year2;
				}
				else {
					inpDate = month2 + '/' + day2 + '/' + year2;
				}
				return inpDate;
			}
			else {
				return false;
			}
		}
	} 
	
	return false;
}

/******************************************************************
   validateYear()
   
   converts yy years to yyyy
   Uses a hinge date of 10
        < 10 = 20yy 
        => 10 = 19yy.
         
   Called by convertDate() before validateDate().

*******************************************************************/
function validateYear(inYear) 
{
	if ( inYear < 10 ) {
		inYear = "20" + inYear;
		return inYear;
	}
	else if ( inYear >= 10 ) {
		inYear = "19" + inYear;
		return inYear;
	}
//	else {
//		return "";
//	}
	return "";
}

function popupBoardDates()
{
	window.open("BoardDates", "bdpopup", "width=550,height=400,screenX=0,screenY=0,top=0,left=0");
}
