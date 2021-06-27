<!-- Hide script from old browsers

// popup a window warning that the member was logged off from
// another session
function popupLoggedOut()
{
   var height = 310;
   var width = 425;
   var top = (screen.height - height) / 2;
   var left = (screen.width - width) / 2;
   var features = "width=" + width + ",height=" + height + ",top=" + top + ",left=" + left;

   var loPopup = window.open("LoggedOut.html", "loPopup", features);
   loPopup.focus();
}
// End hiding script from old browsers -->
