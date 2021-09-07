/**************************************************************
 * Title       : 저작기 라인
 * Description : 저작기 핸들러를 라인단위로 읽게끔 수행
 *
 * Author      : 권동욱
 * Date        : 2009.12.28
 * version     : V 1.0
 **************************************************************/

/**
 * Line 단위로 읽는 클래스
 */
Line = function(msg) {
	this.msg = msg;
};

Line.prototype.readLine = function() {
	var line = new String();

	if(this.msg.length == 0) {
		line = null;
	} else {
		for(var i = 0; i < this.msg.length; i++) {
			if(this.msg.charAt(i) == '\r') {
				continue;
			} if(this.msg.charAt(i) == '\n') {
				this.msg = this.msg.substring(i + 1, this.msg.length);
				break;
			} else {
				line += this.msg.charAt(i);
			}

			if((i + 1) == this.msg.length) {
				this.msg = this.msg.substring(i + 1, this.msg.length);
			}
		}
	}

	return line;
};

Line.prototype.toString = function() {
	return this.msg;
};