<%-------------------------------------------------------------------------------------------------
 * - [이케어>단건발송] 단건발송 메세지 작성 화면 출력 (알림톡) - MOKA에서 사용
 * - URL : /ecare/onceSend.do
 * - Controller : com.mnwise.wiseu.web.common.web.SendMappingController
 * - 이전 파일명 : send_once_altalk.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<html>
<head>
<title><spring:message code="common.send.type.once"/> - <spring:message code="common.channel.A"/></title><!-- 단건발송 - 알림톡 -->
<script src='/plugin/date/jquery.jclock-2.2.1.js'></script>
<script src="/js/common/common2step.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 실패건 문자발송 라디오버튼 클릭
        $("input:radio[name=smsSndYn]").on("change", function(event) {
            if(this.value == 'Y') {
                $("#lms_subj").show();
            } else {
                $("#lms_subj").hide();
                $("input[name=subject]").val("");
            }
        });

        // 발송일시 라디오버튼 클릭
        $("input:radio[name=send_dtm]").on("change", function(event) {
            if(this.value == 'Now') {
                $("#calndDiv").hide();
            } else {
                $("#calndDiv").show();
            }
        });

        // 발송 버튼 클릭
        $("#sendBtn").on("click", function(event) {
            var rules = {
                kakaoSenderKey : {selected : true},
                "ecareVo.kakaoTmplCd" : {notBlank : true},
                smsSndYn : {required : true},
                phoneNum : {notBlank : true, digits : true},
                callBack : {notBlank : true, digits : true},
                subject  : {notBlank : true, maxbyte : 40}
            };

            if($.mdf.validForm("#sendOnceFrm", rules) == false) {
                return false;
            }

            var send_dtm = $("input:radio[name=send_dtm]:checked").val();
            if(send_dtm == "Rev") {
                //발송 시작 시간에서 :를 제거시킨 정규식
                var startTm = $("#sendTm").val().replace(new RegExp(":","gi"), "");

                if(startTm == '') {
                    alert("<spring:message code='ecare.alert.msg.exec.14'/>");  // 발송시작 시간은 필수 항목입니다.
                    return;
                }
                if(isNaN(startTm)) {
                    alert("<spring:message code='ecare.alert.msg.exec.15'/>");  // 발송시작 시간에는 숫자만 입력하셔야 합니다.
                    return;
                }
            }

            editorIfrm.tinyMCE.triggerSave();
            editorIfrm.saveEditor(true); //탬플릿, 핸들러 저장

            var channel = $("input[name=channel]").val();
            var serviceNo = $("input[name=serviceNo]").val();
            var callBack = $.trim($("input[name=callBack]").val());
            var phoneNum = $.trim($("input[name=phoneNum]").val());
            var sendDt = $("input[name=sendDt]").val();
            var sendTm = $("input[name=sendTm]").val();
            var subject = $("input[name=subject]").val();
            var kakaoSenderKey = $("select[name=kakaoSenderKey]").val();
            var tmplCd = $("input[name='ecareVo.kakaoTmplCd']").val();
            var smsSndYn = $("input:radio[name=smsSndYn]:checked").val();

            editorIfrm.sendOnce(channel,serviceNo,send_dtm,callBack,phoneNum,sendDt,sendTm,subject,kakaoSenderKey,tmplCd,smsSndYn);
        });
    }

    function initPage() {
        $('#jclockDiv').jclock({ seedTime: ${seedTime}, format: '%Y-%m-%d %H:%M:%S' });

        new mdf.Date("#sendDt");  // 예약발송일
        new mdf.Time("#sendTm");  // 예약발송시간
    }

    function goSubmit() {
        alert("<spring:message code='ecare.alert.msg.exec.21'/>");  // 발송이 요청되었습니다.
        $("#sendOnceFrm").attr('action', '/ecare/onceSend.do?channel=A&serviceNo=7').submit();  // /ecare/send_once.do
    }

    function setYellowId(senderKey) {
        $('#kakaoSenderKey').val(senderKey);
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="common.send.type.once"/> - <spring:message code="common.channel.A"/></h3><!-- 단건발송 - 알림톡 -->
            </div>

            <form id="sendOnceFrm" name="sendOnceFrm" action="/ecare/onceSend.do" method="post"><!-- /ecare/send_once.do -->
            <input type="hidden" name="channel" value="A"/>
            <input type="hidden" name="serviceNo" value="7"/>
            <input type="hidden" name="ecareVo.templateSenderKey" id="ecareVo.templateSenderKey" />

            <div class="card-body">
                <ul class="nav nav-tabs tab02 mb-4" id="tabs-text" role="tablist"><!-- tabs -->
                    <c:forEach var="item" items="${wiseu:getProperty('channel.use.list', '')}">
                        <c:if test="${item eq 'S'}">
                            <li class="nav-item"><!-- /ecare/send_once.do -->
                                <a class="nav-link mb-sm-3 mb-md-0" href="/ecare/onceSend.do?serviceNo=5&channel=S"><spring:message code="common.channel.S"/></a>
                            </li>
                        </c:if>
                        <c:if test="${item eq 'T'}">
                            <li class="nav-item"><!-- /ecare/send_once.do -->
                                <a class="nav-link mb-sm-3 mb-md-0 " href="/ecare/onceSend.do?serviceNo=6&channel=T"><spring:message code="common.channel.T"/></a>
                            </li>
                        </c:if>
                        <c:if test="${item eq 'A'}">
                            <li class="nav-item"><!-- /ecare/send_once.do -->
                                <a class="nav-link mb-sm-3 mb-md-0 active" href="/ecare/onceSend.do?serviceNo=7&channel=A"><spring:message code="common.channel.A"/></a>
                            </li>
                        </c:if>
                        <c:if test="${item eq 'C'}">
                            <li class="nav-item"><!-- /ecare/send_once.do -->
                                <a class="nav-link mb-sm-3 mb-md-0" href="/ecare/onceSend.do?serviceNo=8&channel=C"><spring:message code="common.channel.C"/></a>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>

                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="14%" />
                            <col width="*" />
                            <col width="14%" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.yellowid"/></th><!-- 카카오톡 채널ID -->
                            <td>
                                <select class="col form-control form-control-sm" name="kakaoSenderKey" id="kakaoSenderKey">
                                    <option value=""><spring:message code="template.column.yellowid" /></option><!-- 카카오톡 채널ID -->
                                    <c:forEach var="kakaoProfile" items="${kakaoProfileList}" varStatus="status">
                                        <option value="${kakaoProfile.kakaoSenderKey}">${kakaoProfile.kakaoYellowId}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="common.channel.A"/> <spring:message code="template.column.tmplcd"/></th><!-- 알림톡 템플릿 코드 -->
                            <td><input type="text" class="form-control form-control-sm" name="ecareVo.kakaoTmplCd" id="ecareVo.kakaoTmplCd" /></td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="ecare.channel.personal_1"/></th><!-- 수신자 전화번호 -->
                            <td class="control-text-sm">
                                <div class="form-inline">
                                    <input type="text" class="form-control form-control-sm d-inline-block w-auto" name="phoneNum" id="phoneNum" />
                                    <span class="ml-1">(<spring:message code="common.msg.numeric.only"/>)</span><!-- 숫자만 입력 -->
                                </div>
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="ecare.menu.sms.send.yn"/></th><!-- 실패건 문자발송 -->
                            <td>
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" value="Y" name="smsSndYn" id="smsSndYn_Y" value="Y" class="custom-control-input" />
                                        <label class="custom-control-label" for="smsSndYn_Y"><spring:message code="ecare.menu.item.sms.send.y"/></label><!-- 발송함 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" value="N" name="smsSndYn" id="smsSndYn_N" value="N" class="custom-control-input" />
                                        <label class="custom-control-label" for="smsSndYn_N"><spring:message code="ecare.menu.item.sms.send.n"/></label><!-- 발송 안함 -->
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr class="dp-none" id="lms_subj">
                            <th scope="row"><em class="required">required</em><spring:message code="ecare.menu.sms.subject"/></th><!-- LMS/MMS 제목 -->
                            <td><input type="text" class="form-control form-control-sm" name="subject" id="subject" maxlength="40" /></td>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.sfax"/></th><!-- 발신자 번호 -->
                            <td class="control-text-sm">
                                <div class="form-inline">
                                    <input type="text" class="form-control form-control-sm  d-inline-block w-auto" name="callBack" id="callBack" value="${senderTel}" />
                                    <span class="ml-1">(<spring:message code="common.msg.numeric.only"/>)</span><!-- 숫자만 입력 -->
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.sdate"/></th><!-- 발송일시 -->
                            <td colspan="3">
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" value="Now" id="nowSend" name="send_dtm" class="custom-control-input" checked />
                                        <label class="custom-control-label" for="nowSend">
                                            <spring:message code="common.send.type.now"/> (<span class="go-send" id="jclockDiv"></span>)<!-- 즉시발송 -->
                                        </label>
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline table_option" >
                                        <input type="radio" value="Rsv" id="rsvSend" name="send_dtm" class="custom-control-input" />
                                        <label class="custom-control-label" for="rsvSend"><spring:message code="campaign.menu.reservation"/></label><!-- 예약발송 -->
                                        <span class="searchbox rv-send dp-none" id="calndDiv">
                                            <div class="input_datebox d-inline-block w-auto ml-2">
                                                <input type="hidden" name="sendDt" id="sendDt" value="${sendDt}" maxlength="10" />
                                            </div>
                                            <div class="input-group date datepicker-inline d-inline-block w-auto ml-2">
                                                <input type="hidden" name="sendTm" id="sendTm" value="${sendTm}" maxlength="8" />
                                            </div>
                                        </span>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="card-body message_contents"><!-- 에디터 -->
                <iframe id="editorIfrm" src="/editor/editor.do?type=ecare&ecareNo=0&serviceType=A&handlerType=G&channelType=T&subType=S&sendOne=O" scrolling="no" name="editorIfrm" frameborder="0" width="100%"></iframe>
            </div>
        </div>

        <div class="card-footer pb-4 btn_area">
            <button type="button" class="btn btn-outline-primary" id="sendBtn">
                <spring:message code="button.send"/><!-- 발송 -->
            </button>
        </div>
        </form>
    </div>
</div>
</body>
</html>