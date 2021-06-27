
function popupAdobe()
{
	var width = 650;
	var height = 400;
	var top = (screen.height - height) / 2;
	var left = (screen.width - width) / 2;
	var props = "width=" + width + ",height=" + height + ",screenX=" + left + ",screenY=" + top + ",top=" + top + ",left=" + left + ",scrollbars=yes,resizable=yes";
	var popup = window.open("http://get.adobe.com/reader/", "popupAdobe", props);
	popup.focus();
}
