$(document).ready(function() {

});

$.fn.addKkoButtonValidRule = function(rules, messages) {
    this.validate({
        rules : rules,
        messages : messages,
        ignoreTitle: true	// input의 title값을 에러메시지로 표시하지 않도록 설정(디폴트 false)
    });

    $(this).find("input[name*='name']").each(function() {
        $(this).rules("add", {notBlank : true , maxlength:14});
    });
    $(this).find("input[name*='linkMo']").each(function() {
        $(this).rules("add", {notBlank : true, url : true});
    });
    $(this).find("input[name*='linkAnd']").each(function() {
        $(this).rules("add", {notBlank : true, url : true});
    });
    $(this).find("input[name*='linkIos']").each(function() {
        $(this).rules("add", {notBlank : true, url : true});
    });
    $(this).find("input[name*='linkPc']").each(function() {
    	$(this).rules("add", {url : true});
    });
};


function convertKakaoButtonsToJson(type) {
    var str = '';

    var selector = (type === 'quickReplies') ? '#quick_reply_table #kakaoButtonList.kakaoButtonList' : '#kakao_button_table #kakaoButtonList.kakaoButtonList';
    $(selector).each(function(i, tr) {

        str += '{';
        str += '\"ordering\":';
        str += i + 1;
        str += ','
        $('td', tr).each(function(j, td) {
            if($(td).attr('class') === 'option') {
                return;
            }
            if($(td).find(':input')==null){
                return true;
            }
            var value = $(td).find(':input').val()
            var key = $(td).find(':input').attr('data');
            if ($.trim(value).length > 0) {
                str += '\"' + key + '\":';
                str += '\"' + value + '\",';
            }
        });
        var next = $(tr).next('tr');
        if (next[0] !== undefined && next[0].id === 'kakaoButtonSubList') {
        	$('td', next[0]).each(function(k, td) {
    		   var value = $(td).find(':input').val()
               var key = $(td).find(':input').attr('data');
               if ($.trim(value).length > 0) {
                       str += '\"' + key + '\":';
                       str += '\"' + value + '\",';
               }
        	});
        }

        str += '},';
        str = str.replace(/,},$/, '},')

    });

    str = str.replace(/},$/, '}');
    if (str === '') {
        return str;
    } else {
        return '[' + str + ']';
    }
}


function getCategoryCdList(){
    $("select[name=categoryCd]").html('');

    var categoryGroup = $("select[name=categoryGroup]").val();

    if ($.mdf.isBlank(categoryGroup)) {
        return;
    } else {
        var param = {
            categoryGroup : categoryGroup
        };

        $.post("/template/getKakaoCategoryCd.json", $.param(param, true), function(result) {
            if (result.code == "OK") {
                $.each(result.value, function(i, map) {
                    var option = $(document.createElement("option"));
                    option.text(map.val);
                    option.val(map.cd);
                    $("select[name=categoryCd]").append(option);
                })
            }
        });
    }
}