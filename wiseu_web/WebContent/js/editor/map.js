/**************************************************************
 * Title       : Map 형태의 자료구조
 * Description : Map 형태의 자료구조
 *
 * Author      : 권동욱
 * Date        : 2009.11.20
 * version     : V 1.0
 **************************************************************/
Map = function() {
	this.map = new Array();
};

Map.prototype.put = function(key, value) {
	this.map[this.map.length] = {
		key: key,
		value: value
	};
};

Map.prototype.get = function(key) {
	for(var i = 0; i < this.map.length; i++) {
		if(key == this.map[i].key) {
			return this.map[i].value;
		}
	}
	
	return null;
};

Map.prototype.clear = function() {
	this.map.length = 0;
};

Map.prototype.size = function() {
	return this.map.length;
};