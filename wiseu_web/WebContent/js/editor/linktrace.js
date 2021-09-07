/**************************************************************
 * Title       : 캠페인, 이케어 저작기 링크트레이스
 * Description : 링크트레이스의 정보를 담음
 *
 * Author      : 권동욱
 * Date        : 2009.11.23
 * version     : V 1.0
 **************************************************************/
Linktrace = function() {
	this.linktrace = new Array();
};

Linktrace.prototype.add = function(no, linkSeq, linkTitle, linkType, linkUrl, linkDesc, linkYn) {
	this.linktrace[this.linktrace.length] = {
		no: no,
		linkSeq: linkSeq, // LINK_SEQ
		linkTitle: linkTitle, // LINK_TITLE
		linkType: linkType,
		linkUrl: linkUrl, // LINK_URL
		//linkUrl: decodeURIComponent(linkUrl), // LINK_URL
		linkDesc: linkDesc, // LINK_DESC
		linkYn: linkYn
	};
};

// LINK_SEQ 에 대한 링크정보를 가져온다.
Linktrace.prototype.getLink = function(linkSeq) {
	for(var i = 0; i < this.linktrace.length; i++) {
		if(linkSeq == this.linktrace[i].linkSeq) {
			return this.linktrace[i];
		}
	}

	return null;
};

// index i 에 대한 링크정보를 가져온다.
Linktrace.prototype.get = function(i) {
	if(i >= this.linktrace.length) {
		return null;
	}

	return this.linktrace[i];
};

// 링크추적이 걸리면 LINK_SEQ 에 대한 LINK_YN 값을 y 로 한다. 링크추적이 안걸린거는 n 이다.
Linktrace.prototype.setLinkYn = function(linkSeq, linkYn) {
	for(var i = 0; i < this.linktrace.length; i++) {
		if(linkSeq == this.linktrace[i].linkSeq) {
			this.linktrace[i].linkYn = linkYn;
			return;
		}
	}
};

// 링크추적이 걸린 값이나 안걸린 값의 URL 값
Linktrace.prototype.setLinkUrl = function(linkSeq, linkUrl) {
	for(var i = 0; i < this.linktrace.length; i++) {
		if(linkSeq == this.linktrace[i].linkSeq) {
			this.linktrace[i].linkUrl = linkUrl;
			return;
		}
	}
};

// 링크추적 정보를 모두 지운다.
Linktrace.prototype.clear = function() {
	this.linktrace.length = 0;
};

// 링크개수
Linktrace.prototype.size = function() {
	return this.linktrace.length;
};

// 링크 값이 있는지 확인
Linktrace.prototype.contains = function(linkUrl) {
	for(var i = 0; i < this.linktrace.length; i++) {
		if(linkUrl == this.linktrace[i].linkUrl) {
			return true;
		}
	}

	return false;
};

Linktrace.prototype.toArray = function() {
	return this.linktrace;
};

// 모든 링크 값을 조회한다.
Linktrace.prototype.toString = function() {
	var val = "";
	for(var i = 0; i < this.linktrace.length; i++) {
		val += i + "=" +
				this.linktrace[i].no + ", " +
				this.linktrace[i].linkSeq + ", " +
				this.linktrace[i].linkTitle + ", " +
				this.linktrace[i].linkType + ", " +
				this.linktrace[i].linkUrl + ", " +
				this.linktrace[i].linkDesc + ", " +
				this.linktrace[i].linkYn;

		if(i < (this.linktrace.length -1)) val += "\n";
	}

	alert(val);
};