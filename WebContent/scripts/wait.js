// show a wait message via DHTML DIVs

var width = screen.width;
var oWait = new getObj('wait');
oWait.style.left = Math.round((width/2)-275);
oWait.style.visibility = 'hidden';
var oMain = new getObj('main');
oMain.style.visibility = 'visible';

function waitOn() {
	window.location.href ='#top';
	oWait.style.visibility = 'visible';
}

function waitOff() {
	oWait.style.visibility = 'hidden';
}

function getObj(name)
{
  if (document.getElementById)
  {
  	this.obj = document.getElementById(name);
	this.style = document.getElementById(name).style;
  }
  else if (document.all)
  {
	this.obj = document.all[name];
	this.style = document.all[name].style;
  }
  else if (document.layers)
  {
   	this.obj = document.layers[name];
   	this.style = document.layers[name];
  }
}
