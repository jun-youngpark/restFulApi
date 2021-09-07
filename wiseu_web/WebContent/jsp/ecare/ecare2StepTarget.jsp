<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script src="/js/segment/target.js"></script><!-- 대상자 검색/선택/등록 -->
<script>
    var rules = {
        sqlHeadBody : { notBlank : true }
    };

    $(document).ready(function() {
        initTargetEventBind();
        initTargetPage();
    })

    var counter;
    var resultIdNm;
    function initTargetPage() {
        var notOmniEcare = ${notOmniEcare};
       	if(notOmniEcare){
       		if(serviceType === 'S' && (subType === 'S' ||  subType === 'R')){
       		   extractKey();
       	    }
       	}else{
        	$("#updateQuery").attr("readonly",true);
            $("#sqlHeadBody").attr("readonly",true);
        }
    }

    function initTargetEventBind() {
        resultIdNm = '${resultIdNm}';    //"data"
        counter = ($('#addQueryTable tr').length==0)?1:$('#addQueryTable tr').length;

        // 적용버튼 클릭
        $('#targetSave').on("click", function(event) {
            targetSave(false);
        });

        $('#variableHelpBtn').on("click", function(event) { //쿼리내 변수 입력 방법
            $("#variableHelpDetail").toggle();
        });
        $('#typeHelpBtn').on("click", function(event) { //유형 도움말
        	$("#typeHelpDetail").toggle();
        });
        $('#excuteTypeHelpBtn').on("click", function(event) {   //실행횟수 도움말
        	$("#excuteTypeHelpDetail").toggle();
        });
        $('#targetHelpBtn').on("click", function(event) { //result Id 매핑 도움말
            $("#targetHelpDetail").toggle();
        });

        // 쿼리 타입 변경 이벤트
        $("#addQueryTable").on("change", "#queryType", function (event) {
            var value = $('option:selected',this).val();
            if(value === 'RESULT'){
                $(this).parents().eq(1).find("#resultId").val("");
            }else{
                var index = $(this).parents().eq(1).find("#counter").children("span").html();
                $(this).parents().eq(1).find("#resultId").val(resultIdNm+index);
            }
        });

        // 대상자 쿼리체크 버튼 클릭
        $("#targetQueryCheckBtn").on("click", function(event) {
            if(!$.mdf.validElement("#targetFrm", "#sqlHeadBody", rules)){
                return false;
            }
            checkTargetQuery($("#dbInfoSeq option:selected").val(), $("#sqlHeadBody").val());
        });

        // 대상자 보기 버튼 클릭
        $("#targetSearchBtn").on("click", function(event) {
            var segmentNo = $("input[name='segmentNo']").val();
            var segmentSize = $("input[name='segmentSize']").val();
            var sqlHeadBody = $("#sqlHeadBody").val();
            if(segmentNo == undefined || segmentNo == 0) {
            	alert($.i18n.prop("common.alert.test.msg1"));
                return false;
            }
            if(!$.mdf.validElement("#targetFrm", "#sqlHeadBody", rules)){
                return false;
            }
            popupTargetList(segmentNo, null, segmentSize, 'EC');
        });

        // 대상자 선택 버튼 클릭
        $('#targetSelectBtn').on("click", function(event) {
            popupTargetSelect("EC", segmentNo, channelType, serviceType, ecareNo);
        });

        // 부가데이터 추가 버튼 클릭
        $('#addQueryBtn').on("click", function(event) {
            $('#addQueryBody').append(makeAddQuery(counter));
            counter += 1;
        });

        // 대상자 키 추출 버튼 클릭
        $('#extractKeyBtn').on("click", function(event) {
            if(!$.mdf.validElement("#targetFrm", "#sqlHeadBody", rules)){
                return false;
            }
            if(extractKey()){
            	//직접입력 버튼 삭제(직접입력할경우 컬럼의 시맨틱 저장 시 필요한 순서 값을 가져올수가없음.)
                //$("#reverseKeyBtn").show();
                //$("#extractKeyBtn").hide();
            }
        });

        // 직접 입력하기 버튼 클릭
        $('#reverseKeyBtn').on("click", function(event) {
            reverseKey();
            $("#extractKeyBtn").show();
            $("#reverseKeyBtn").hide();
        });

        // 부가데이터 삭제 버튼 클릭
        $("#addQueryTable").on("click", "#delQueryBtn", function (event) {
            if(!confirm("<spring:message code='ecare.alert.msg.del.4'/>")){
                return;
            }

            var child = $(this).closest('tr').nextAll();
            child.each(function () {   // 삭제 후 row 번호를 재정렬
                var span = $(this).children('#counter').children('span');
                var newIndex = span.html() - 1;
                span.html(newIndex);

                var resultId = $(this).children('#resultIdTd').children('#resultId');
                if($.mdf.isNotBlank(resultId.val())) {
                    resultId.val(resultIdNm + newIndex);
                }
            });

            counter--;
            $(this).closest("tr").remove();
        })
    }

    function addValidRule() {
        $("#targetFrm").validate({
            invalidHandler: function(form, validator) {
                if (!validator.numberOfInvalids())
                    return;
                $('html, body').animate({
                    scrollTop: ($(validator.errorList[0].element).offset().top -300)
                }, 2000);
            }
        });

        $("#targetFrm textarea[name*='addQuery']").each(function() {
            $(this).rules("add", {notBlank: true});
        });
        $("#targetFrm input[name*='slot']").each(function() {
            $(this).rules("add", {maxlength :15});
        });
        $("#targetFrm textarea[name='sqlHeadBody']").rules("add", {notBlank: true });

        $("#targetFrm select[name='semanticId']").rules("add", {selected: true});
        $("#targetFrm select[name='semanticNm']").rules("add", {selected: true});
        if(channelType ==='M'){
        	$("#targetFrm select[name='semanticEmail']").rules("add", {selected: true});
        }else if(channelType ==='F'){
        	$("#targetFrm select[name='semanticFax']").rules("add", {selected: true});
        }else{
        	$("#targetFrm select[name='semanticSms']").rules("add", {selected: true});
        }
    }

    var hasDuplSemantic;
    //대상자 정보 저장
    function targetSave(allSave){
        var deferred = $.Deferred(); //전체 저장을 위한 함수
        if(editAble === false) {
        	alert($.i18n.prop("common.alert.save.e1_"+webExecMode));
            return deferred.reject("noAlram");
        }
        addValidRule(); //유효성 체크 규칙
        if($.mdf.validForm('#targetFrm') === false){
            return deferred.reject($.i18n.prop('vaildation.check.fail'));  //유효성 체크 실패
        }
        var param = $.mdf.serializeObject('#targetFrm');
        var notOmniEcare = ${notOmniEcare};
        if(notOmniEcare){ //옴니채널이 아닌경우
        	param.addQueryData = JSON.stringify(setAddQueryData());
            param.semanticData = JSON.stringify(setSemanticData());
        }else{
        	param.addQueryData = JSON.stringify(new Array());
        	param.semanticData = JSON.stringify(new Array());
        }
        if(hasDuplSemantic === true){   //필수값 컬럼이 중복될 경우
            return deferred.reject("noAlram");
       }
        $.post("/ecare/ecare2StepTarget.json", $.param(param, true), function(result) {
            ajaxCallback(result, allSave, deferred);    //common2step.js 전체적용일 경우 deferred 성공/실패 return
            getOmniChannelList()    //옴니채널 채널 실시간 변경
        });
        return deferred;
    }

    function setSemanticData(){
    	hasDuplSemantic = false;
    	 var array  = new Array();
    	 setEtcSemanticData(array, "semanticId", "K", "segment.status.K") // 아이디
    	 setEtcSemanticData(array, "semanticNm", "N", "segment.status.N") //이름

    	 if(channelType ==='M'){
    		 setEtcSemanticData(array, "semanticEmail", "E", "segment.status.E") //이메일
    		 setEtcSemanticData(array, "semanticSms", "S", "segment.status.S") //문자 설정
    		 setEtcSemanticData(array, "semanticFax", "F", "segment.status.F") //팩스 설정
    	 }else if(channelType ==='F'){
             setEtcSemanticData(array, "semanticFax", "F", "segment.status.F") //팩스 설정
             setEtcSemanticData(array, "semanticSms", "S", "segment.status.S") //문자 설정
             setEtcSemanticData(array, "semanticEmail", "E", "segment.status.E") //이메일
    	 }else{
    		 setEtcSemanticData(array, "semanticSms", "S", "segment.status.S") //문자 설정
             setEtcSemanticData(array, "semanticFax", "F", "segment.status.F") //팩스 설정
             setEtcSemanticData(array, "semanticEmail", "E", "segment.status.E") //이메일
    	 }
    	 return array;
    }

    function setEtcSemanticData(array, selector, fieldKey, fieldDesc){
    	 var value = $("#"+selector).val();
    	 var index = document.getElementById(selector).selectedIndex;
    	 for ( var i in array ) {
               if(array[i].fieldSeq === index){
                   alert('필수키 컬럼은 중복하여 사용 할 수 없습니다.')
                   hasDuplSemantic = true;
                   return;
               }
          }
    	 if($.mdf.isNotBlank(value)){
    		 array.push({
                 "fieldSeq": index , "fieldKey":fieldKey, "fieldNm": value, "fieldDesc": $.i18n.prop(fieldDesc)
             });
         }
    }

    function setAddQueryData(){
    	 var tmp  = new Array();
         $('#addQueryTable tr#addQueryTr').each(function(i) {
        	 tmp.push({
                "ecareNo":'${ecareScenarioVo.ecareVo.ecareNo}',
                "querySeq":$(this).children('#counter').children('span').html(),
                "dbInfoSeq":$(this).find('#dbInfoSeq > option:selected').val(),
                "queryType":$(this).find('#queryType > option:selected').val(),
                "executeType":$(this).find('#executeType > option:selected').val(),
                "resultId":$(this).find('#resultId').val(),
                "query":$(this).find('textarea').val()
            });
        });
         return tmp;
    }

    //필수키 설정 text type -> select type 변경
    function extractKey(){
        var sql  = $("#sqlHeadBody").val().toLowerCase();
        var regexp =  new RegExp("select(.*)from");
        if(regexp.test(sql)) {
            var filed = sql.match(regexp)[1].split(",");
            makeSemanticSelectBox("semanticId", filed);
            makeSemanticSelectBox("semanticNm", filed);
            makeSemanticSelectBox("semanticEmail", filed);
            makeSemanticSelectBox("semanticFax", filed);
            makeSemanticSelectBox("semanticSms", filed);
            return true;
        }else{
            makeSemanticSelectBox("semanticId", '');
            makeSemanticSelectBox("semanticNm", '');
            makeSemanticSelectBox("semanticEmail", '');
            makeSemanticSelectBox("semanticFax", '');
            makeSemanticSelectBox("semanticSms", '');
        	//alert($.i18n.prop("ecare.2step.vaild.segment")); //대상자 쿼리가 존재하지 않습니다.
            return false;
        }
    }

    function makeSemanticSelectBox(id, filed){
    	var semanticVal = $('#'+id).val().toLowerCase();
        var html= '<select id="'+id+'" name="'+id+'"   class="form-control form-control-sm">';
        html +='<option value="">'+$.i18n.prop("segment.menu.sql.choice") + '</option>';
        for ( var i in filed ) {
        	var value =$.trim(filed[i]);
            var index = value.lastIndexOf(".");
            value = (index == -1) ? value : value.substring(index + 1); // alias 제거
        	if(value === semanticVal){
        		html +='<option value="'+value+'" selected>'+value+'</option>';
        	}else{
        	    html +='<option value="'+value+'">'+value+'</option>';
        	}
        }
        html +='</select>';
        $('#'+id+'').replaceWith(html);
    }


    //부가데이터 쿼리 행추가
    function makeAddQuery(counter){
        var elementStr = '';
        elementStr += '<tr id="addQueryTr" index="'+counter+'">';
        elementStr += '<td id="counter">';
        elementStr += ' <button class="btn btn-sm btn-outline-danger" type="button" id="delQueryBtn"><i  class="fas fa-minus"></i></button>';
        elementStr += '<span>'+counter+'</span>';
        elementStr += '</td>';
        elementStr += '<td>';
        elementStr += ' <select id="dbInfoSeq" class="form-control form-control-sm">';
        <c:forEach items="${dbInfoList}" var="dbInfoVo">
        elementStr += ' <option value="${dbInfoVo.dbInfoSeq}">${dbInfoVo.serverNm}</option>';
        </c:forEach>
        elementStr += ' </select>';
        elementStr += '</td>';
        elementStr += '<td>';
        elementStr += ' <select class="form-control form-control-sm" id="queryType">';
        elementStr += ' <option value="DATA"><spring:message code="segment.queryType.data" /></option>';
        elementStr += ' <option value="LIST"><spring:message code="segment.queryType.list" /></option>';
        elementStr += ' <option value="RESULT"><spring:message code="segment.queryType.result" /></option>';
        elementStr += ' </select>';
        elementStr += '</td>';
        elementStr += '<td>';
        elementStr += ' <select class="form-control form-control-sm" id="executeType">';
        elementStr += ' <option value="BYTARGET"><spring:message code="segment.executeType.bytarget" /></option>';
        elementStr += ' <option value="ONCE"><spring:message code="segment.executeType.once" /></option>';
        elementStr += ' </select>';
        elementStr += '</td>';
        elementStr += '<td id="resultIdTd"><input class="form-control" type="text" id="resultId" readonly value="data'+counter+'"/></td>';
        elementStr += '<td>';
        elementStr += ' <textarea class="form-control" id="addQuery['+counter+']" name="addQuery['+counter+']"  rows="4"></textarea>';
        elementStr += '</td>';
        elementStr += '</tr>';
        return elementStr;
    }

    // 필수키 설정 select type -> text type 변경
    function reverseKey(){
        makeSemanticTextBox("semanticId","ID");
        makeSemanticTextBox("semanticNm","NAME");
        makeSemanticTextBox("semanticEmail", "EMAIL");
        makeSemanticTextBox("semanticFax", "FAX");
        makeSemanticTextBox("semanticSms", "SMS");
    }

    function makeSemanticTextBox(id, key){
        var html='<input type="text" id='+id+' name="'+id+'" class="form-control form-control-sm" placeholder="'+key+'"/>';
        $('#'+id+'').replaceWith(html);
    }
</script>

<form name="targetFrm" id="targetFrm" method="post">
<input type="hidden" name="channelType" value="${ecareScenarioVo.ecareVo.channelType}">
<input type="hidden" name="segmentNo" value="${ecareScenarioVo.segmentNo}">
<input type="hidden" name="segmentSize" value="${ecareScenarioVo.segmentSize}">
<input type="hidden" name="ecareNo" value="${ecareScenarioVo.ecareVo.ecareNo}">
<input type="hidden" name="serviceType" value="${ecareScenarioVo.ecareVo.serviceType}" />
<input type="hidden" name="subType" value="${ecareScenarioVo.ecareVo.subType}" />

<div class="card">
    <div class="card-header bg-default toogleEvt" id="headingTwo" data-toggle="collapse" data-target="#collapseTwo" aria-expanded='true'>
        <h5 class="text-secondary pt-3">[STEP2] <spring:message code="ecare.2step.title.target"/></h5><!-- 대상자 -->
    </div>
    <div id="collapseTwo" class="collapse show" aria-labelledby="headingTwo">
        <div class="card-body px-3">
            <div class="row mt-2 mb-0">
                <div class="col-6 align-items-center py-1"><!-- title -->
                    <h4 class="h3 text-primary m-0"><spring:message code="segment.info"/></h4><!-- 대상자 설정 -->
                </div>
                <div class="col-6 justify-content-end pb-1">
                    <c:if test="${(serviceType eq 'schedule' or serviceType eq 'scheduleM') and notOmniEcare}">
                    <button class="btn btn-sm btn-outline-primary btn-round" id="targetQueryCheckBtn">
                        <i class="fas fa-file-alt"></i> <spring:message code="segment.msg.target" /> <spring:message code="segment.alt.sql.msg2" /><!-- 대상자 쿼리 체크 -->
                    </button>
                    <button class="btn btn-sm btn-outline-primary btn-round" id="targetSearchBtn">
                        <i class="fas fa-file-alt"></i> <spring:message code="button.target.view" /><!-- 대상자 보기 -->
                    </button>
                    <button class="btn btn-sm btn-outline-primary btn-round" id="targetSelectBtn">
                        <i class="fas fa-user-check"></i> <spring:message code="segment.msg.starget" /><!--대상자 선택 -->
                    </button>
                    </c:if>
                    <button class="btn btn-sm btn-outline-primary btn-round mr-2" id="targetSave">
                        <i class="fas fa-check"></i> <spring:message code="button.apply"/><!-- 적용 -->
                    </button>
                </div>
            </div>
            <div class="col-12 alert alert-secondary mb-0" role="alert">
                <i class="fas fa-circle fa-xs"></i>
                <button type="button" class="btn btn-sm" id="variableHelpBtn" >
                     <spring:message code="common.menu.variable.use" /><!-- 변수 입력 방법-->
                     <i class="fas fa-question-circle fa-2x"></i>
                </button>
                <div id="variableHelpDetail" class="border p-1" style="display: none;">
                      <pre class="font-size-13" style="font-family: inherit; white-space: pre-line;">
		                <spring:message code="ecare.2step.help1" /><!-- 대상자 쿼리 내 변수 입력 시 대상자 테이블(target) 컬럼 사용은 {변수} 형태, 이케어정보(context)의 필드는 #{변수} 형태로 넣어주세요.  -->
		                <spring:message code="ecare.2step.help12" />
		                <spring:message code="ecare.2step.help13" />
		                </pre>
                </div>
                </br>
                <i class="fas fa-circle fa-xs"></i>
                <button type="button" class="btn btn-sm" id="typeHelpBtn" >
                     <spring:message code="common.menu.type" /><!-- 유형 --> <i class="fas fa-question-circle fa-2x"></i>
                </button>
                  <div id="typeHelpDetail" class="border p-1" style="display: none;">
                      <pre class="font-size-13" style="font-family: inherit; white-space: pre-line;">
                        <spring:message code="ecare.2step.help10"/>
                       </pre>
                  </div>
                   <!--  부가데이터 : 1 Row 데이터 추출(단순 매핑 처리)</br>
                       부가 데이터 리스트: List Row 데이터 추출(리스트 매핑 처리)</br>
                       결과 처리 :  발송 결과 후 처리 -->
                  </br>
                 <i class="fas fa-circle fa-xs"></i>
                <button type="button" class="btn btn-sm" id="excuteTypeHelpBtn" >
                     <spring:message code="common.menu.excute.type" /><!-- 실행횟수 --><i class="fas fa-question-circle fa-2x"></i>
                </button>
                <div id="excuteTypeHelpDetail" class="border p-1" style="display: none;">
                      <pre class="font-size-13" style="font-family: inherit; white-space: pre-line;">
                        <spring:message code="ecare.2step.help11" />
                       </pre>
                 </div>
                  <!--  대상자별 : 대상자별 쿼리</br>
                         한번만 : 발송 스케줄 수행 시 한번만 쿼리 -->
                 </br>
                 <i class="fas fa-circle fa-xs"></i>
                 <button type="button" class="btn btn-sm" id="targetHelpBtn" >
                     Result Id(<spring:message code="ecare.2step.resultId.comment" />)<!-- 템플릿에서 사용할 데이터의 ID --><i class="fas fa-question-circle fa-2x"></i>
                </button>
                  <div id="targetHelpDetail" class="border p-1" style="display: none;">
                      <pre class="font-size-13" style="font-family: inherit; white-space: pre-line;">
                          <spring:message code="segment.queryType.data" /> : &lt;%=data3.get("변수")%&gt; </br>
                          <spring:message code="segment.queryType.list" /> :
                         &lt;% for(int i = 0; i < data3.size(); i++) { %&gt;
                         &nbsp;&nbsp; &lt;%=(data3.get(i).get("변수1"))%&gt;
                         &nbsp;&nbsp; &lt;%=(data3.get(i).get("변수2"))%&gt;
                          &lt;% } %&gt;
                       </pre>
                  </div>
            </div>
            <div class="table-responsive mb-0">
                <table class="table table-sm dataTable" style="margin-bottom: 0px !important" id="addQueryTable">
                    <thead class="thead-light">
                        <tr>
                            <th scope="col" width="6%"><spring:message code="common.menu.sn" /> </th><!-- 순서 -->
                            <th scope="col" width="13%">DB<spring:message code="common.menu.info" /> </th><!-- DB 정보-->
                            <th scope="col" width="15%"><spring:message code="common.menu.type" /></th><!-- 유형 -->
                            <th scope="col" width="15%"><spring:message code="common.menu.excute.type" /></th><!-- 실행횟수 -->
                            <th scope="col" width="8%">Result ID</th>
                            <th scope="col" width="*"><spring:message code="common.menu.query" /></th><!-- 쿼리 -->
                        </tr>
                    </thead>
                    <tbody id="addQueryBody">
                        <c:if test="${serviceType eq 'schedule' or serviceType eq 'scheduleM'}">
                        <tr>
                            <td>1</td>
                            <td rowspan="2">
                                <select name="dbInfoSeq" id="dbInfoSeq" class="form-control form-control-sm">
                                <c:forEach var="dbInfoVo" items="${dbInfoList}" varStatus="status">
                                    <c:choose>
                                    <c:when test="${dbInfoVo.dbInfoSeq eq targetQueryInfo.dbInfoSeq}">
                                        <option value="${dbInfoVo.dbInfoSeq}" selected="selected">${dbInfoVo.serverNm}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${dbInfoVo.dbInfoSeq}">${dbInfoVo.serverNm}</option>
                                    </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                </select>
                            </td>
                            <td><spring:message code="segment.menu.sql.pre.query" /></td><!-- 선행 쿼리 -->
                            <td>1<spring:message code="common.menu.times" /></td>  <!-- 1회 -->
                            <td>-</td>
                            <td>
                                <textarea class="form-control" name="updateQuery" id="updateQuery" rows="4">${targetQueryInfo.updateQuery}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td><spring:message code="segment.menu.sql.query" /></td> <!-- 대상자 쿼리 -->
                            <td>1<spring:message code="common.menu.times" /></td> <!-- 1회 -->
                            <td>-</td>
                            <td>
                                <textarea class="form-control" name="sqlHeadBody" id="sqlHeadBody" rows="4" >${targetQueryInfo.sqlHead}${targetQueryInfo.sqlBody}</textarea>
                            </td>
                        </tr>
                        </c:if>

                        <c:forEach var="addQuery" items="${addQueryList}" varStatus="query">
                        <tr id="addQueryTr">
                            <td id="counter">
                                <button class="btn btn-sm btn-outline-danger" type="button" id="delQueryBtn"><i class="fas fa-minus"></i></button>
                                <span>${addQuery.querySeq}</span>
                            </td>
                            <td>
                                <select id="dbInfoSeq" class="form-control form-control-sm">
                                <c:forEach var="dbInfoVo" items="${dbInfoList}" varStatus="status">
                                    <c:choose>
                                    <c:when test="${dbInfoVo.dbInfoSeq eq addQuery.dbInfoSeq}">
                                        <option value="${dbInfoVo.dbInfoSeq}" selected="selected">${dbInfoVo.serverNm}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${dbInfoVo.dbInfoSeq}">${dbInfoVo.serverNm}</option>
                                    </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                </select>
                            </td>
                            <td>
                                <select class="form-control form-control-sm" id="queryType">
                                    <option value="DATA" ${addQuery.queryType eq 'DATA'?"selected":""}><spring:message code="segment.queryType.data" /></option><!--  부가 데이터-->
                                    <option value="LIST" ${addQuery.queryType eq 'LIST'?"selected":""}><spring:message code="segment.queryType.list" /></option><!-- 부가데이터 리스트 -->
                                    <option value="RESULT" ${addQuery.queryType eq 'RESULT'?"selected":""}><spring:message code="segment.queryType.result" /></option><!--  결과 처리-->
                                </select>
                            </td>
                            <td>
                                <select class="col form-control form-control-sm" id="executeType">
                                    <option value="BYTARGET" ${addQuery.executeType eq 'BYTARGET'?"selected":""}><spring:message code="segment.executeType.bytarget" /></option><!-- 대상자별 -->
                                    <option value="ONCE" ${addQuery.executeType eq 'ONCE'?"selected":""}><spring:message code="segment.executeType.once" /></option><!--  한번만-->
                                </select>
                            </td>
                            <td id="resultIdTd"><input class="form-control" type="text" id="resultId" readonly value="${addQuery.resultId}"/></td>
                            <td>
                                <textarea class="form-control" id="addQuery[${query.index}]" name="addQuery[${query.index}]" rows="4">${addQuery.query}</textarea>
                            </td>
                        </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="text-center py-1" style="background-color:#76889c; cursor: pointer; height: 31px;" id="addQueryBtn">
                <span><i class="fas fa-plus text-secondary"> <spring:message code="segment.query.add" /></i></span><!--  쿼리 추가 -->
            </div>

            <c:if test="${(serviceType eq 'schedule' or serviceType eq 'scheduleM') and notOmniEcare}"><!-- 필수키 설정-->
            <div class="row mt-2 mb-0">
                <div class="col-6 align-items-center py-1"><!-- title -->
                    <h4 class="h3 text-primary m-0"><spring:message code="segment.requiredkey" /></h4>
                </div>
                <div class="col-6 justify-content-end pb-1">
                    <button class="btn btn-sm btn-outline-primary btn-round" id="extractKeyBtn">
                        <i class="fas fa-check"></i> <spring:message code="segment.requiredkey.extract" /><!--  필수 키 추출-->
                    </button>
                    <button class="btn btn-sm btn-outline-primary btn-round" id="reverseKeyBtn" style="display: none;">
                        <i class="fas fa-check"></i> <spring:message code="segment.requiredkey.input" /><!--  직접 입력-->
                    </button>
                </div>
            </div>
            <div class="col-12 alert alert-secondary mb-0" role="alert">
                <i class="fas fa-circle fa-xs"></i> <spring:message code="ecare.2step.help3" /></br><!--필수키 직접 입력시 대상자 쿼리의 컬럼명을 입력해야 됩니다.  -->
                <i class="fas fa-circle fa-xs"></i> <spring:message code="ecare.2step.help5" /> <!--필수값이 아닌 필수키값은 옴니채널에서 사용됩니다.-->
            </div>
            <div class="table-responsive">
                <table class="table table-sm dataTable">
                    <thead class="thead-light">
                    <tr>
                        <th><em class="required"></em><spring:message code="segment.status.K"/></th><!-- 아이디 -->
                        <th><em class="required"></em><spring:message code="segment.status.N"/></th><!-- 이름 -->
                           <c:choose>
                               <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'M'}">
                                    <th><em class="required"></em><spring:message code="segment.status.E"/><!-- 이메일 주소 --></th>
                                    <th><spring:message code="segment.status.F"/><!-- 팩스 번호--></th>
                                    <th><spring:message code="segment.status.number"/><!-- 휴대폰 번호--></th>
                               </c:when>
                               <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'F'}">
                                    <th><em class="required"></em><spring:message code="segment.status.F"/><!-- 팩스 번호--></th>
                                    <th><spring:message code="segment.status.E"/><!-- 이메일 주소 --></th>
                                    <th><spring:message code="segment.status.number"/><!-- 휴대폰 번호--></th>
                               </c:when>
                               <c:otherwise>
                                    <th><em class="required"></em><spring:message code="segment.status.number"/><!-- 휴대폰 번호--></th>
                                    <th><spring:message code="segment.status.E"/><!-- 이메일 주소 --></th>
                                    <th><spring:message code="segment.status.F"/><!-- 팩스 번호--></th>
                               </c:otherwise>
                           </c:choose>
                        </th>

                    </tr>
                    </thead>
                    <c:forEach items="${semanticFieldList}" var="item" varStatus="i">
                        <c:if test="${item.fieldKey eq 'K'}"><c:set var="semanticIdField" value="${item.fieldNm}"/></c:if>
                        <c:if test="${item.fieldKey eq 'N'}"><c:set var="semanticNmField" value="${item.fieldNm}"/></c:if>
                        <c:if test="${item.fieldKey eq 'E'}"><c:set var="semanticEmailField" value="${item.fieldNm}"/></c:if>
                        <c:if test="${item.fieldKey eq 'F'}"><c:set var="semanticFaxField" value="${item.fieldNm}"/></c:if>
                         <c:if test="${item.fieldKey eq 'S'}"><c:set var="semanticSmsField" value="${item.fieldNm}"/></c:if>
                    </c:forEach>
                    <tr>
                        <td><input type="text" id="semanticId" name="semanticId" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.K"/>" value="${semanticIdField}"/></td>
                        <td><input type="text" id="semanticNm" name="semanticNm" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.N"/>" value="${semanticNmField}"/></td>
                        <c:choose>
                               <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'M'}">
                                    <td><input type="text" id="semanticEmail" name="semanticEmail" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.E"/>" value="${semanticEmailField}"/></td>
                                    <td><input type="text" id="semanticFax" name="semanticFax" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.F"/>" value="${semanticFaxField}"/></td>
                                    <td><input type="text" id="semanticSms" name="semanticSms" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.number"/>" value="${semanticSmsField}"/></td>
                               </c:when>
                               <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'F'}">
                                    <td><input type="text" id="semanticFax" name="semanticFax" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.F"/>" value="${semanticFaxField}"/></td>
                                    <td><input type="text" id="semanticEmail" name="semanticEmail" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.E"/>" value="${semanticEmailField}" /></td>
                                    <td><input type="text" id="semanticSms" name="semanticSms" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.number"/>" value="${semanticSmsField}"/></td>
                               </c:when>
                               <c:otherwise>
                                    <td><input type="text" id="semanticSms" name="semanticSms" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.number"/>" value="${semanticSmsField}" required/></td>
                                    <td><input type="text" id="semanticEmail" name="semanticEmail" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.E"/>" value="${semanticEmailField}"/></td>
                                    <td><input type="text" id="semanticFax" name="semanticFax" class="form-control form-control-sm font-weight-bolder" placeholder="<spring:message code="segment.status.F"/>" value="${semanticFaxField}"/></td>
                               </c:otherwise>
                           </c:choose>
                    </tr>
                </table>
            </div>
            </c:if><!-- End 필수키 설정-->

            <!-- 이력테이블 부가데이터 설정-->
            <h1 class="h3 text-primary mt-2 mb-0"><spring:message code="history.additional.data" /></h1>
            <div class="alert alert-secondary mb-0" role="alert">
                <i class="fas fa-circle fa-xs"></i> <spring:message code="ecare.2step.help2" /><%-- 로그 테이블까지 전달 할 대상자(target)의 컬럼명을 입력해야 됩니다. 규칙 예시: {변수} --%>
            </div>
            <div class="table-responsive mb-3">
                <table class="table table-sm dataTable">
                    <thead class="thead-light">
                    <tr>
                        <th>SLOT1</th><th>SLOT2</th><th>SLOT3</th><th>SLOT4</th><th>SLOT5</th>
                    </tr>
                    </thead>
                    <tr>
                        <td><input type="text" class="form-control form-control-sm" id="slot1_field"  name="slot1Field" value="${ecareScenarioVo.ecareVo.slot1Field}" /></td>
                        <td><input type="text" class="form-control form-control-sm" id="slot2_field"  name="slot2Field" value="${ecareScenarioVo.ecareVo.slot2Field}"/></td>
                        <td><input type="text" class="form-control form-control-sm" id="slot3_field"  name="slot3Field" value="${ecareScenarioVo.ecareVo.slot3Field}"/></td>
                        <td><input type="text" class="form-control form-control-sm" id="slot4_field"  name="slot4Field" value="${ecareScenarioVo.ecareVo.slot4Field}"/></td>
                        <td><input type="text" class="form-control form-control-sm"  id="slot5_field"  name="slot5Field" value="${ecareScenarioVo.ecareVo.slot5Field}"/></td>
                    </tr>
                    <thead class="thead-light">
                    <tr>
                        <th>SLOT6</th><th>SLOT7</th><th>SLOT8</th><th>SLOT9</th><th>SLOT10</th>
                    </tr>
                    </thead>
                    <tr>
                        <td><input type="text" class="form-control form-control-sm" id="slot6_field"  name="slot6Field"  value="${ecareScenarioVo.ecareVo.slot6Field}"/></td>
                        <td><input type="text" class="form-control form-control-sm" id="slot7_field"  name="slot7Field"  value="${ecareScenarioVo.ecareVo.slot7Field}"/></td>
                        <td><input type="text" class="form-control form-control-sm" id="slot8_field"  name="slot8Field"  value="${ecareScenarioVo.ecareVo.slot8Field}"/></td>
                        <td><input type="text" class="form-control form-control-sm" id="slot9_field"  name="slot9Field"  value="${ecareScenarioVo.ecareVo.slot9Field}"/></td>
                        <td><input type="text" class="form-control form-control-sm" id="slot10_field" name="slot10Field"  value="${ecareScenarioVo.ecareVo.slot10Field}"/></td>
                    </tr>
                 </table>
             </div><!-- // 이력테이블 부가데이터 -->
        </div>
    </div>
</div>
</form>