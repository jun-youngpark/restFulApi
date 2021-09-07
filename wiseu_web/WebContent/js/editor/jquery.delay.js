// Delay Plugin for jQuery
// - http://www.evanbot.com
// - 2008 Evan Byrne

jQuery.fn.delay = function(time, func) {
	return this.each(function() {
		setTimeout(func, time);
	});
};

// SMS, MMS 저작기가 완료되면 나중에 제거되야 할 파일이다.