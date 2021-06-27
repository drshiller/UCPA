
var $$ = PluginDetect;
var divAdobe = 'adobe';
var divPdf = 'pdf';

function handlePdfDetectionDone($$) {
	var status = $$.isMinVersion('PDFReader', 0); 

	var elementId = (status >= 0) ? divPdf : divAdobe;

	var theElement = window.top.document.getElementById(elementId)
	var theStyle = theElement.style;
	if (theStyle.display == '' && theElement.offsetWidth != undefined && theElement.offsetHeight != undefined) {
		theStyle.display = (theElement.offsetWidth !=0 && theElement.offsetHeight != 0) ? 'block' : 'none';
	}
	theStyle.display = (theStyle.display == '' || theStyle.display == 'block') ? 'none' : 'block';
};

$$.onDetectionDone('PDFReader', handlePdfDetectionDone, 'empty.pdf');
