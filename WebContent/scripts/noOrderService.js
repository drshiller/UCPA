jQuery(function($) {
	
	$('[id=noOrderService]').bt(
		'The document ordering service is temporarily unavailable.', 
		{
			trigger: 'click', 
			fill: '#F7F7F7',
			strokeStyle: '#B7B7B7',
			spikeLength: 10,
			spikeGirth: 10, 
			padding: 8,
			width: '335px',
			closeWhenOthersOpen: true,
			cssStyles: { 
				fontFamily: 'arial, verdana, helvetica',
				fontSize: '10pt',
				textAlign: 'center'
			}
		}
	);
	
});
