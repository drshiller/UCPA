<!-- Hide script from old browsers

// set the focus on the 1st useful element in a form
function formFocus() {
	for (f = 0; f < document.forms.length; f++) {
		var aForm = document.forms[f]
		for (e = 0; e < aForm.elements.length; e++) {
			var anElement = aForm.elements[e];
			if (anElement.type != "hidden" &&
				anElement.type != "submit" &&
				anElement.type != "button" &&
				anElement.type != "reset" &&
				anElement.disabled == false)
			{
				try {
					anElement.focus();
					return;
				}
				catch (ex) {
					// ignore any errors
				}
			}
		}
	}
}

// End hiding script from old browsers -->
