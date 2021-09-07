;(function ($) {
    /**
     * Object를 문자열로 변환 (alert로 디버깅시 사용)
     */
    $.dump = function(obj, name) {
        var indent = "  ";
        if (typeof obj == "object") {
            var child = null;
            var output = (name) ? indent + name + "\n" : "";
            indent += "\t";

            for (var item in obj) {
                try {
                    child = obj[item];
                } catch (e) {
                    child = "<Unable to Evaluate>";
                }

                if (typeof child == "object") {
                    output += $.dump(child, item);
                } else {
                    output += indent + item + ": " + child + "\n";
                }
            }

            return output;
        } else {
            return obj;
        }
    };

    $.mdf = $.mdf || {};
    $.extend($.mdf, {
        // /////////////////////////////////////////////////////////////////////////////
        // 공통 유틸리티
        /**
         * 배열을 Object로 변환
         *
         * @param array 배열
         * @returns {Object} Object
         */
        arrayToObject : function(array) {
            var obj = {};
            $.each(array, function() {
                var name;
                $.each(this, function(i, value){
                    if (i=="name") {
                        name = value;
                    } else if (i=="value") {
                        obj[name] = value;
                    }
                });
            });
            return obj;
        },
        /**
         * 중복을 제거한 배열을 반환.
         * JQuery의 $.unique(), $.uniqueSort()가 정상동작하지 않아 만듬
         */
        unique : function(array) {
            var uniqueArray = [];

            $.each(array, function(i, value) {  // 중복제거
                if($.inArray(value, uniqueArray) == -1) {
                    uniqueArray.push(value);
                }
            });

            return uniqueArray;
        },
        /**
         * 주어진 selector 폼 객체 disabled 속성을 toggle
         */
        toggleDisabled : function(selector) {
            var disabled = $(selector).attr("disabled");
            $(selector).attr("disabled", !disabled);
        },
        /**
         * 주어진 selector 폼 객체의 Outer HTML을 반환
         */
        outerHTML : function(selector) {
            var html = $(selector).clone().wrapAll("<div/>").parent().html();
            return (html) ? html : null;
        },
        /**
         * console.log() 함수를 대체.
         * IE9 이하에서 console 객체가 개발자 도구 실행시만 동작되는 문제 해결
         */
        log : function(obj) {
            if(window.console) {
                console.log(obj);
            }
        },

        // /////////////////////////////////////////////////////////////////////////////
        // 화면 유틸리티
        /**
         * 주어진 url을 GET 메소드로 팝업 호출 (팝업창 자동 중앙배치)
         */
        popupGet : function(url, target, width, height, status) {
            if($.mdf.isBlank(status)) {
                status = "scrollbars=no,resizable=no";
            }

            var left = (window.outerWidth - width) / 2 + window.screenX;
            var top = (window.outerHeight - height + window.screenY) / 4;

            status += ",height=" + height + ",width=" + width + ",top=" + top + ",left=" + left;
            var win = window.open(url, target, status);

            try {
                win.focus();
            } catch(e){
            }

            return win;
        },
        /**
         * 주어진 폼 selector의 폼을 GET/POST 메소드로 submit하여 팝업 호출 (팝업창 자동 중앙배치)
         */
        popupSubmit : function(formSelector, action, target, width, height, status) {
            if($.mdf.isBlank(status)) {
                status = "scrollbars=no,resizable=no";
            }

            var left = (window.outerWidth - width) / 2 + window.screenX;
            var top = (window.outerHeight - height + window.screenY) / 4;

            status += ",height=" + height + ",width=" + width + ",top=" + top + ",left=" + left;
            var win = window.open("about:blank", target, status);

            $(formSelector).attr('action', action).attr('target', target).submit();

            try {
                win.focus();
            } catch(e){
            }

            return win;
        },
        /**
         * 주어진 selector의 iframe 객체 height를 자동 resize
         */
        resizeIframe : function(selector) {
            $(selector, parent.document).height($(selector, parent.document).contents().find("body").height() + 2);
        },
        // /////////////////////////////////////////////////////////////////////////////
        // 폼 유틸리티
        /**
         * 주어진 selector의 폼 데이타를 Object로 변환
         *
         * @param formName 폼명
         * @returns {Object} Object
         */
        serializeObject : function(selector) {
            var array = $(selector).serializeArray();
            var obj = {};
            $.each(array, function() {
                var name;
                $.each(this, function(i, value){
                    if (i=="name") {
                        name = value;
                    } else if (i=="value") {
                        if(obj[name]) {
                            if($.isArray(obj[name])) {
                                obj[name].push(value);
                            } else {//$.type(obj)
                                var objArray = new Array();
                                objArray.push(obj[name]);
                                objArray.push(value);
                                obj[name] = objArray;
                            }
                        } else {
                            obj[name] = value;
                        }
                    }
                });
            });
            $.mdf.log(obj);
            return obj;
        },
        /**
         * 주어진 selector의 폼 데이타를 reset
         */
        resetForm : function(selector) {
            try {
                $(selector).validate().destroy();
            } catch(e) {
                $.mdf.log(e);
            }

            $(selector).each(function() {
                this.reset();
            });
        },
        /**
         * 주어진 selector의 모든 checkbox 객체의 checked 속성을 toggle
         */
        checkAll : function(selector) {
            if ($(selector).is(':checked')) {
                $("input[type=checkbox]").prop("checked", true);
            } else {
                $("input[type=checkbox]").prop("checked", false);
            }
        },
        // /////////////////////////////////////////////////////////////////////////////
        // String 유틸리티
        /**
         * 주어진 문자열의 개행문자(\n)를 <br/>태그로 변환
         *
         * @param str 문자열
         * @returns {String} 개행문자(\n)가 <br/>태그로 변환된 문자열
         */
        nlToBr : function(str) {
            return str ? str.replace(/\n/g, "<br/>") : str;
        },
        /**
         * 주어진 문자열의 바이트 길이를 가져온다
         */
        getByteLength : function(str) {
            var length = 0;
            for(var i = 0; i < str.length; i++) {
                length += (escape(str.charAt(i)).indexOf("%u") == -1) ? 1 : 2;
            }

            return length;
        },
        /**
         * 주어진 문자열을 지정된 바이트수 만큼 잘라서 반환
         * 한글의 경우 2바이트로 계산하며, 글자 중간에서 잘리지 않도록 처리
         */
        cutByte : function(str, length) {
            var count = 0;

            for(var i = 0; i < str.length; i++) {
                if(escape(str.charAt(i)).length >= 4)
                    count += 2;
                else if(escape(str.charAt(i)) != "%0D")
                    count++;

                if(count > length) {
                    if(escape(str.charAt(i)) == "%0A")
                        i--;
                    break;
                }
            }
            return str.substring(0, i);
        },
        /**
         * org.apache.commons.lang.StringUtils 자바클래스 startsWith() 함수와 유사
         */
        startsWith : function(str, prefix) {
            return (str == null || prefix == null) ? false : str.startsWith(prefix);
        },
        /**
         * org.apache.commons.lang.StringUtils 자바클래스 endsWith() 함수와 유사
         */
        endsWith  : function(str, suffix) {
            return (str == null || suffix == null) ? false : str.endsWith(suffix);
        },
        /**
         * org.apache.commons.lang.StringUtils 자바클래스 defaultString() 함수와 유사
         */
        defaultString : function(obj) {
            if(obj) {
                if($.type(obj) === "string") {
                    return obj;
                } else if (obj instanceof jQuery) {
                    return obj.val() + "";
                }
            }

            return "";
        },
        /**
         * org.apache.commons.lang.StringUtils 자바클래스 defaultIfBlank() 함수와 유사
         */
        defaultIfBlank : function(str, defaultStr) {
            return $.mdf.isBlank(str) ? defaultStr : str;
        },
        /**
         * 주어진 배열에 지정된 값을 갖는 요소의 수를 반환
         * org.apache.commons.lang.StringUtils 자바클래스 countMatches() 함수와 유사
         */
        countMatches : function(array, value) {
            var count = 0;

            $.each(array, function(i, element) {
                if(element == value) {
                    count++;
                }
            });

            return count;
        },
        /**
         * org.apache.commons.lang.StringUtils 자바클래스 equalsAny() 함수와 유사
         */
        equalsAny : function(value, array) {
            for(var i = 0; i < array.length; i++) {
                if(array[i] == value) {
                    return true;
                }
            }

            return false;
        },
        /**
         * value1이 value2보다 작으면 true, 아니면 false를 반환
         */
        lessThan : function(value1, value2) {
            return $.mdf.defaultString(value1) < $.mdf.defaultString(value2);
        },
        /**
         * value1이 value2보다 작거나 같으면 true, 아니면 false를 반환
         */
        lessEqual : function(value1, value2) {
            return $.mdf.defaultString(value1) <= $.mdf.defaultString(value2);
        },
        /**
         * value1이 value2보다 크면 true, 아니면 false를 반환
         */
        greaterThan : function(value1, value2) {
            return $.mdf.defaultString(value1) > $.mdf.defaultString(value2);
        },
        /**
         * value1이 value2보다 크거나 같으면 true, 아니면 false를 반환
         */
        greaterEqual : function(value1, value2) {
            return $.mdf.defaultString(value1) >= $.mdf.defaultString(value2);
        },

        // /////////////////////////////////////////////////////////////////////////////
        // 유효성 체크 유틸리티
        /**
         * 주어진 문자열이 영문 대문자를 포함하면 true, 아니면 false를 반환
         */
        containsUpperCase : function(str) {
            return /[A-Z]/.test(str);
        },
        /**
         * 주어진 문자열이 영문 소문자를 포함하면 true, 아니면 false를 반환
         */
        containsLowerCase : function(str) {
            return /[a-z]/.test(str);
        },
        /**
         * 주어진 문자열이 숫자를 포함하면 true, 아니면 false를 반환
         */
        containsDigit : function(str) {
            return /[0-9]/.test(str);
        },
        /**
         * 주어진 문자열이 숫자나 -, = 문자를 포함하면 true, 아니면 false를 반환
         */
        containsNumericEqual : function(str) {
            var numberChar = "1234567890-=";
            for(var i = 0; i < str.length; i++) {
                for(var j = 0; j < numberChar.length; j++) {
                    if(str.charAt(i) == numberChar.charAt(j)) {
                        return true;
                    }
                }
            }
            return false;
        },
        /**
         * 주어진 문자열이 특수문자를 포함하면 true, 아니면 false를 반환
         */
        containsSpecialChar : function(str) {
            return /[~!@\#$%^&*\()\-=+_]/gi.test(str);
        },
        /**
         * 주어진 문자열이 빈문자열(null, '', 공백)만 있으면 true, 아니면 false를 반환
         * org.apache.commons.lang.StringUtils 자바클래스 isBlank() 함수와 유사
         *
         * @param obj 문자열(jQuery 객체나 String 객체)
         * @returns 빈문자열이면 true, 아니면 false
         */
        isBlank : function(obj) {
            return $.trim($.mdf.defaultString(obj)) == "" ? true : false;
        },
        /**
         * 주어진 문자열이 빈문자열(null, '', 공백)이 아닌 문자를 포함하고 있으면 true, 아니면 false를 반환
         * org.apache.commons.lang.StringUtils 자바클래스 isNotBlank() 함수와 유사
         *
         * @param obj 문자열(jQuery 객체나 String 객체)
         * @returns 빈문자열아니면 true, 이면 false
         */
        isNotBlank : function(obj) {
            return $.trim($.mdf.defaultString(obj)) == "" ? false : true;
        },
        /**
         * 주어진 문자열이 영문, 숫자만 포함하고 있으면 true, 아니면 false를 반환
         */
        isAlphaDigit : function(obj) {
            return /^[A-Za-z0-9]*$/.test($.mdf.defaultString(obj));
        },
        /**
         * 주어진 문자열이 영문, 숫자, _문자만 포함하고 있으면 true, 아니면 false를 반환
         */
        isAlphaDigitUnderscore : function(obj) {
            return /^[A-Za-z0-9_]+$/.test($.mdf.defaultString(obj));
        },
        /**
         * 주어진 문자열이 이메일 주소 형식에 맞으면 true, 아니면 false를 반환
         *
         * @param   str 문자열
         * @return  boolean
         */
        isEmail : function(obj) {
            var pattern  = /^([\w-]+(?:\.\w+)*)@((?:\w+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
            return pattern.test($.mdf.defaultString(obj));
        },
        /**
         * 주어진 문자열이 URL 주소 형식에 맞으면 true, 아니면 false를 반환
         */
        isUrl : function(obj) {
            var pattern  = /^(http:|https:)/;
            return pattern.test($.mdf.defaultString(obj));
        },

        // /////////////////////////////////////////////////////////////////////////////
        // Number 유틸리티
        /**
         * org.apache.commons.lang.NumberUtils 자바클래스 toInt() 함수와 유사
         */
        toInt : function(fromValue, defaultValue) {
            var toValue = parseInt(fromValue);
            return isNaN(toValue) ? defaultValue : toValue;
        },
        /**
         * org.apache.commons.lang.NumberUtils 자바클래스 toFloat() 함수와 유사
         */
        toFloat : function(fromValue, defaultValue) {
            var toValue = parseFloat(fromValue);
            return isNaN(toValue) ? defaultValue : toValue;
        },

        // /////////////////////////////////////////////////////////////////////////////
        // Date 유틸리티
        /**
         * yyyyMMdd형 또는 yyyyMMddHHmmss형 문자열을 Date 객체로 변환
         *
         * @param yyyyMMdd형 또는 yyyyMMddHHmmss형 문자열
         * @returns {Date} Date 객체
         */
        toDate : function(dateString) {
            var year  = dateString.substr(0,4);
            var month = (dateString.length >= 6) ? dateString.substr(4,2) - 1 : '01'; // 1월=0,12월=11
            var day   = (dateString.length >= 8) ? dateString.substr(6,2) : '01';

            if(dateString.length >= 14) {
                var hour  = dateString.substr(8,2);
                var min   = dateString.substr(10,2);
                var sec   = dateString.substr(12,2);

                return new Date(year, month, day, hour, min, sec);
            }

            return new Date(year, month, day);
        },
        /**
         * org.apache.commons.lang.DateUtils 자바클래스 addMinutes() 함수와 유사
         */
        addMinutes : function(date, amount) {
            var tempDate;

            if(date.constructor == Date) {
                tempDate = date;
            } else if(date.constructor == String) {
                tempDate = this.toDate(date);
            }
            var minutes = $.mdf.toInt(tempDate.getMinutes(), 0) + $.mdf.toInt(amount, 0);
            tempDate.setMinutes(minutes);
            return tempDate;
        },
        /**
         * org.apache.commons.lang.DateUtils 자바클래스 addHours() 함수와 유사
         */
        addHours : function(date, amount) {
            var tempDate;

            if(date.constructor == Date) {
                tempDate = date;
            } else if(date.constructor == String) {
                tempDate = this.toDate(date);
            }
            var hours = $.mdf.toInt(tempDate.getHours(), 0) + $.mdf.toInt(amount, 0);
            tempDate.setHours(hours);
            return tempDate;
        },
        /**
         * org.apache.commons.lang.DateUtils 자바클래스 addDays() 함수와 유사
         */
        addDays : function(date, amount) {
            var tempDate;

            if(date.constructor == Date) {
                tempDate = date;
            } else if(date.constructor == String) {
                tempDate = this.toDate(date);
            }

            tempDate.setDate(tempDate.getDate() + amount);
            return tempDate;
        },
        /**
         * org.apache.commons.lang.DateUtils 자바클래스 addMonths() 함수와 유사
         */
        addMonths : function(date, amount) {
            var tempDate;

            if(date.constructor == Date) {
                tempDate = date;
            } else if(date.constructor == String) {
                tempDate = this.toDate(date);
            }

            tempDate.setMonth(tempDate.getMonth() + amount);
            return tempDate;
        },
        /**
         * 주어진 Date 객체(또는 yyyyMMdd, yyyyMM 문자열)를 yyyy-MM-dd형 또는 yyyy-MM형 문자열로 변환
         */
        toFmtDateString : function (date) {
            var dateString;
            if(date.constructor == Date) {
                dateString = this.toDateString(date);
            } else if(date.constructor == String) {
                dateString = date;
            }

            if(dateString) {
                if(dateString.length >= 8) {
                    return dateString.substr(0,4) + "-" + dateString.substr(4,2) + "-" + dateString.substr(6,2);
                } else if(dateString.length >= 6) {
                    return dateString.substr(0,4) + "-" + dateString.substr(4,2);
                }

                return dateString;
            }

            return "";
        },
        /**
         * 주어진 Date 객체를 yyyyMMdd형 문자열로 변환
         */
        toDateString : function(date) {
            var dateString = date.getFullYear() + '';
            var month = date.getMonth() + 1;
            dateString += (month < 10) ? '0' + month: month;
            var day = date.getDate();
            dateString += (day < 10) ? '0' + day : day;

            return dateString;
        },
        /**
         * 주어진 Date 객체를 HHmmss형 문자열로 변환
         */
        toTimeString : function(date) {
            var dateString = '';
            var hours = date.getHours();
            dateString += (hours <10) ? '0' + hours : hours;
            var minutes = date.getMinutes();
            dateString += (minutes <10) ? '0' + minutes : minutes;
            var seconds = date.getSeconds();
            dateString += (seconds <10) ? '0' + seconds : seconds;
            return dateString;
        },
        /**
         * 주어진 Date 객체를 yyyyMMddHHmmss형 문자열로 변환
         */
        toDateTimeString : function(date) {
            var dateString = date.getFullYear() + '';
            var month = date.getMonth() + 1;
            dateString += (month < 10) ? '0' + month: month;
            var day = date.getDate();
            dateString += (day < 10) ? '0' + day : day;
            var hours = date.getHours();
            dateString += (hours <10) ? '0' + hours : hours;
            var minutes = date.getMinutes();
            dateString += (minutes <10) ? '0' + minutes : minutes;
            var seconds = date.getSeconds();
            dateString += (seconds <10) ? '0' + seconds : seconds;
            return dateString;
        },

        // /////////////////////////////////////////////////////////////////////////////
        // File 유틸리티
        /**
         * org.apache.commons.io.FilenameUtils 자바클래스 getExtension() 함수와 유사
         */
        getExtension : function(fileName) {
            var index = fileName.lastIndexOf(".");
            return (index == -1) ? "" : fileName.substring(index + 1);
        },

        // /////////////////////////////////////////////////////////////////////////////
        // 테이블 유틸리티
        /**
         * 주어진 tr JQuery 객체에서 지정된 cell의 텍스트 값을 추출해 배열로 반환 (Chart 구현시 사용)
         */
        getTableCellValueArray : function($tableTr, cellIndex, isNumberValue) {
            var valueArray = new Array();

            if(isNumberValue == null || isNumberValue == true) {
                $tableTr.each(function(i) {
                    var value = $(this).children().eq(cellIndex).text();
                    if(value) {
                        value = value.replace(/,/g, "")
                    }
                    valueArray.push(parseFloat(value));
                });
            } else {
                $tableTr.each(function(i) {
                    valueArray.push($(this).children().eq(cellIndex).text());
                });
            }

            return valueArray
        },
        /**
         * 주어진 tr JQuery 객체에서 지정된 카테고리와 cell의 텍스트 값을 추출해 배열로 반환 (Chart 구현시 사용)
         */
        getTableCategoryValueArray : function($tableTr, categoryIndex, cellIndex, isNumberValue) {
            var valueArray = new Array();

            if(isNumberValue == null || isNumberValue == true) {
                $reportTr.each(function(i) {
                    var $tdArray = $(this).children();
                    var categoryValue = new Array();
                    categoryValue.push($tdArray.eq(categoryIndex).text());
                    categoryValue.push(parseFloat($tdArray.eq(cellIndex).text()));
                    valueArray.push(categoryValue);
                });
            } else {
                $reportTr.each(function(i) {
                    var $tdArray = $(this).children();
                    var categoryValue = new Array();
                    categoryValue.push($tdArray.eq(categoryIndex).text());
                    categoryValue.push($tdArray.eq(cellIndex).text());
                    valueArray.push(categoryValue);
                });
            }

            return valueArray
        },

        // /////////////////////////////////////////////////////////////////////////////
        // AJAX
        /**
         * 주어진 url에 GET 메소드로 Ajax 통신으로 JSON 타입의 데이터를 가져온다.
         * JQuery의 $.getJSON()가 async, global 등의 옵션 설정을 못하는 문제를 해결.
         */
        getJSON : function(url, okCallback, option) {
            var option = $.extend(true, {
                cache : false,
                global : true,
                async : true,
                contentType : 'application/json; charset=utf-8'
            }, option || {});

            $.ajax({
                url : url,
                type : "get",
                cache : option.cache,
                global : option.global,
                async : option.async,
                contentType : option.contentType,
                success : function(data, textStatus, jqXHR) {
                    if($.isFunction(okCallback)) {
                        okCallback(data);
                    } else {
                        return data;
                    }
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    if($.isFunction(okCallback)) {
                        okCallback("error");
                    } else {
                        return "error";
                    }
                }
            });
        },
        /**
         * 주어진 url에 POST 메소드로 Ajax 통신으로 JSON 타입의 데이터를 가져온다.
         */
        postJSON : function(url, param, okCallback, option) {
            var option = $.extend(true, {
                cache : false,
                global : true,
                async : true,
                contentType : 'application/json; charset=utf-8',
            }, option || {});

            //jQuery.ajaxSettings.traditional = true;

            $.ajax({
                url : url,
                type : "post",
                cache : option.cache,
                global : option.global,
                async : option.async,
                contentType : option.contentType,
                data : param,
                success : function(data, textStatus, jqXHR) {
                    if($.isFunction(okCallback)) {
                        okCallback(data);
                    } else {
                        return data;
                    }
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    if($.isFunction(okCallback)) {
                        okCallback("error");
                    } else {
                        return "error";
                    }
                }
            });
        },
        // /////////////////////////////////////////////////////////////////////////////
        // 로딩바
        /**
         * 로딩바 표시.
         * plugin.jsp내 ajaxStart 메소드에서 사용.
         */
        openLoading : function() {
            if($("#loadingBar").length == 0) {
                $("<div id='loadingBar' style='position:absolute;visibility:visible;z-index:15;'><img src='/images/ajax/plugin/ajaxLoader.gif'></div>").appendTo($('body'));
                $("#loadingBar").css("top", $(window).scrollTop() + $(window).height() / 2 - 80);
                $("#loadingBar").css("left", $(window).scrollLeft() + $(window).width() / 2);
            }
            $("#loadingBar").show();
        },
        /**
         * 로딩바 숨김.
         * plugin.jsp내 ajaxStop, ajaxComplete 메소드에서 사용.
         */
        closeLoading : function() {
            $("#loadingBar").hide();
        },

        // /////////////////////////////////////////////////////////////////////////////
        // 다이얼로그
        /**
         * 자바스크립트 alert() 함수와 유사하나 확인 버튼이 없고 2초뒤 자동 사라짐
         */
        notify : function(message, option) {
            var option = $.extend(true, {
                autoClose : true,
                timeout : 2000,
                popupClose : false
            }, option || {});

            var dialogId = "mdfDialogId_" + (Math.round(Math.random()*10000000));
            $("<div id='" + dialogId + "'>" + $.mdf.nlToBr(message) + "</div>").appendTo($('body'));
            var bodyOverfow = $('body').css("overflow");

            $("#" + dialogId).dialog({
                title : '안내',
                modal : false,
                resizable: false,
                width : 350,
                height : 'auto',
                dialogClass : 'alert',
                position : {
                    my: "center",
                    at: "center top+200",
                    of: window
                },
                closeOnEscape: false,
                open: function(event, ui) {
                    $("body").css("overflow", "hidden");
                    $(".ui-dialog-titlebar-close").hide();
                },
                beforeClose: function(event, ui) {
                    $("body").css("overflow", bodyOverfow);
                },
            });

            if(option.autoClose) {
                setTimeout(function() {
                    $("#" + dialogId).dialog("close");
                    $("#" + dialogId).remove();
                    if(option.popupClose){
                        window.close();
                    }
                }, option.timeout);
            }
        },
        /**
         * 자바스크립트 alert() 함수와 유사
         *
         * @param message 출력할 메시지
         * @param callback 메시지창이 close된 이후에 호출되는 함수
         */
        alert : function(message, okCallback, argArray) {
            var dialogId = "mdfDialogId_" + (Math.round(Math.random()*10000000));
            $("<div id='" + dialogId + "'>" + $.mdf.nlToBr(message) + "</div>").appendTo($('body'));
            var bodyOverfow = $('body').css("overflow");

            $("#" + dialogId).dialog({
                title : '알림',
                modal : false,
                resizable: false,
                width : 350,
                height : 'auto',
                dialogClass : 'alert',
                position : {
                    my: "center",
                    at: "center top+200",
                    of: window
                },
                closeOnEscape: false,
                open: function(event, ui) {
                    $("body").css("overflow", "hidden");
                    $(".ui-dialog-titlebar-close").hide();
                },
                beforeClose: function(event, ui) {
                    $("body").css("overflow", bodyOverfow);
                },
                close: function () {
                    $(this).remove();
                },
                buttons: [{
                    text : $.i18n.prop("button.confirm"),  // 확인
                    "class" : "btn btn-sm btn-outline-primary btn-round",
                    click : function() {
                        $(this).dialog("close");
                        if ($.isFunction(okCallback)) {
                            okCallback.apply(this, argArray || []);
                        }
                    }
                }]
            });
        },
        /**
         * 자바스크립트 confirm() 함수와 유사 (주의 : await, async 키워드를 추가해야 정상 modal 처리된다)
         * - 호출시 앞에 await 키워드를 추가 : 예) await $.mdf.confirm(...);
         * - 호출코드를 포함하는 함수에는 async 키워드를 추가 : $("#deleteBtn").on("click", async function(event) {});
         */
        confirm : function(message, okCallback, argArray) {
            var dialogId = "mdfDialogId_" + (Math.round(Math.random()*10000000));
            var dfd = $.Deferred();
            $("<div id='" + dialogId + "'>" + $.mdf.nlToBr(message) + "</div>").appendTo($('body'));
            var bodyOverfow = $('body').css("overflow");

            $("#" + dialogId).dialog({
                title : '알림',
                modal : true,
                resizable: false,
                width : 350,
                height : 'auto',
                dialogClass : 'alert',
                position : {
                    my: "center",
                    at: "center top+200",
                    of: window
                },
                closeOnEscape: false,
                open: function(event, ui) {
                    $("body").css("overflow", "hidden");
                    $(".ui-dialog-titlebar-close").hide();
                },
                beforeClose: function(event, ui) {
                    $("body").css("overflow", bodyOverfow);
                },
                close: function () {
                    $(this).remove();
                },
                buttons: [{
                    text : $.i18n.prop("button.confirm"),  // 확인
                    "class" : "btn btn-sm btn-outline-primary btn-round",
                    click : function() {
                        $(this).dialog("close");
                        /*if ($.isFunction(okCallback)) {
                            okCallback.apply(this, argArray || []);
                        }*/
                        dfd.resolve(true);
                        //return true;
                    },
                },
                {
                    text : $.i18n.prop("button.cancel"),  // 취소
                    "class" : "btn btn-sm btn-outline-primary btn-round",
                    click : function() {
                        $(this).dialog("close");
                        dfd.resolve(false);
                        //return false;
                    }
                }]
            });

            return dfd.promise();
        },
        /**
         * 폼 데이터 유효성을 체크
         * @param 선택자, 옵션
         * @param defaults 설정
         * @param 유효성 규칙
         * var rules ={
                    email : { required : true}
             };
         * @param 유효성 메세지
         * var messages ={
                      ecarePreface: {
                          email: 'We need your email address to contact you'
                      }
             };
         */
        validForm : function(selector, rules, messages, ignore) {
            $(selector).validate({
                rules : rules,
                messages : messages,
                ignore : ignore,
                invalidHandler: function (form, validator) {
                    if(validator.numberOfInvalids()) {  // 첫번째 에러 항목에 포커스 설정
                        validator.errorList[0].element.focus();
                    }
                },
                errorPlacement : function(error, element) {
                    if(element.parent('.custom-file').length > 0) {
                        element.parent().after(error);
                    } else if(element.parent().parent('.form-row').length > 0) {
                        element.parent().parent().after(error);
                    } else if(element.parent('.form-inline').length > 0) {
                        element.parent().after(error);
                    } else if(element.is(":radio") || element.is(":checkbox")) {
                        element.parent().parent().after(error);
                    } else {
                        element.after(error);
                    }
                },
            });

            return $(selector).valid();
        },
        /**
         * 폼 요소의 데이터 유효성을 체크
         */
        validElement : function(formSelector, elementSelector, rules, messages, ignore) {
            return $(formSelector).validate({
                rules : rules,
                messages : messages,
                ignore : ignore,
                errorPlacement : function(error, element) {
                    if (element.is(":radio") || element.is(":checkbox")) {
                        element.parent().parent().after(error);
                    } else {
                        element.after(error);
                    }
                },
            }).element(elementSelector);
        }
    });
})(jQuery);

