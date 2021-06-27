// this javascript checks for the presence of the Adobe Reader in the user's browser;

function isAdobeReaderInstalled() {
    var isInstalled = false;

    // test in Internet Explorer for ActiveX
    if (window.ActiveXObject) {
        var axo = null;
        try {
            // AcroPDF.PDF is used by version 7 and later
            axo = new ActiveXObject('AcroPDF.PDF');
        }
        catch (e) {
            // Do nothing
        }
        if (!axo) {
            try {
                // PDF.PdfCtrl is used by version 6 and earlier
                axo = new ActiveXObject('PDF.PdfCtrl');
            }
            catch (e){
                // Do nothing
            }
        }
        if (axo) {
            isInstalled = true;
            version = axo.GetVersions().split(',');
            version = version[0].split('=');
            version = parseFloat(version[1]);
//            alert("ie: " + version);
        }
    }
   
    // test in navigator-based browsers
    else {
        for (var i = 0; i < navigator.plugins.length; i++) {
            if ((navigator.plugins[i].name == "nppdf.so") ||
                    (navigator.plugins[i].name.indexOf("Acrobat") > -1))
            {
//                alert("navigator: " + navigator.plugins[i].name);
                isInstalled = true;
                break;
            }
        }
    }

    return isInstalled;
}
