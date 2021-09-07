<%-------------------------------------------------------------------------------------------------
 * - [이케어>단건발송] 단건발송 메세지 작성 화면 출력 (SMS) - MOKA에서 사용
 * - URL : /ecare/onceSend.do
 * - Controller : com.mnwise.wiseu.web.common.web.SendMappingController
 * - 이전 파일명 : send_once_sms.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<html>
<head>
<title><spring:message code="common.send.type.once"/> - <spring:message code="common.channel.S"/></title><!-- 단건발송 - SMS -->
<script src='/plugin/date/jquery.jclock-2.2.1.js'></script>
<script src="/js/common/common2step.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
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
                phoneNum : {notBlank : true, digits : true},
                callBack : {notBlank : true, digits : true}
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
            var subject = "";
            var senderKey = "";
            var tmplCd = "";
            var smsSndYn = "";

            editorIfrm.sendOnce(channel,serviceNo,send_dtm,callBack,phoneNum,sendDt,sendTm,subject,senderKey,tmplCd,smsSndYn);
        });
    }

    function initPage() {
        $('#jclockDiv').jclock({ seedTime: ${seedTime}, format: '%Y-%m-%d %H:%M:%S' });

        new mdf.Date("#sendDt");  // 예약발송일
        new mdf.Time("#sendTm");  // 예약발송시간
    }

    function goSubmit() {
        alert("<spring:message code='ecare.alert.msg.exec.21'/>");  // 발송이 요청되었습니다.
        $("#sendOnceFrm").attr('action', '/ecare/onceSend.do?channel=S&serviceNo=5').submit();  // /ecare/send_once.do
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="common.send.type.once"/> - <spring:message code="common.channel.S"/></h3><!-- 단건발송 - SMS -->
            </div>

            <form id="sendOnceFrm" name="sendOnceFrm" action="/ecare/onceSend.do" method="post"><!-- /ecare/send_once.do -->
            <input type="hidden" name="channel" value="S"/>
            <input type="hidden" name="serviceNo" value="5"/>

            <div class="card-body">
                <ul class="nav nav-tabs tab02 mb-4" id="tabs-text" role="tablist"><!-- tabs -->
                    <c:forEach var="item" items="${wiseu:getProperty('channel.use.list', '')}">
                        <c:if test="${item eq 'S'}">
                            <li class="nav-item"><!-- /ecare/send_once.do -->
                                <a class="nav-link mb-sm-3 mb-md-0 active" href="/ecare/onceSend.do?serviceNo=5&channel=S"><spring:message code="common.channel.S"/></a>
                            </li>
                        </c:if>
                        <c:if test="${item eq 'T'}">
                            <li class="nav-item"><!-- /ecare/send_once.do -->
                                <a class="nav-link mb-sm-3 mb-md-0" href="/ecare/onceSend.do?serviceNo=6&channel=T"><spring:message code="common.channel.T"/></a>
                            </li>
                        </c:if>
                        <c:if test="${item eq 'A'}">
                            <li class="nav-item"><!-- /ecare/send_once.do -->
                                <a class="nav-link mb-sm-3 mb-md-0" href="/ecare/onceSend.do?serviceNo=7&channel=A"><spring:message code="common.channel.A"/></a>
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
                            <th scope="row"><em class="required">required</em><spring:message code="ecare.channel.personal_1"/></th><!-- 수신자 전화번호 -->
                            <td class="control-text-sm">
                                <div class="form-inline">
                                    <input type="text" class="form-control form-control-sm d-inline-block w-auto" name="phoneNum" id="phoneNum" />
                                    <span class="ml-1">(<spring:message code="common.msg.numeric.only"/>)</span><!-- 숫자만 입력 -->
                                </div>
                            </td>
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
                            <td colspan="3" style="line-height:31px !important;">
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" value="Now" id="nowSend" name="send_dtm" class="custom-control-input" checked />
                                        <label class="custom-control-label" for="nowSend">
                                            <spring:message code="common.send.type.now"/> (<span class="go-send" id="jclockDiv"></span>)<!-- 즉시발송 -->
                                        </label>
                                    </div>

                                    <div class="custom-control custom-radio custom-control-inline table_option">
                                        <input type="radio" value="Rsv" id="rsvSend" name="send_dtm" class="custom-control-input" style="margin-top: 16px !important;"/>
                                        <label class="custom-control-label" for="rsvSend"><spring:message code="campaign.menu.reservation"/></label><!-- 예약발송 -->
                                        <span class="rv-send dp-none" id="calndDiv">
                                            <div class="input_datebox d-inline-block w-auto ml-2">
                                                <input type="hidden" name="sendDt" id="sendDt" value="${sendDt}" maxlength="10" />
                                            </div>
                                            <div class="input-group date datepicker-inline d-inline-block ml-2">
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
                <iframe id="editorIfrm" src="/editor/editor.do?type=ecare&ecareNo=0&serviceType=S&handlerType=G&channelType=S&subType=S&sendOne=O" scrolling="no" name="editorIfrm" frameborder="0" width="100%"></iframe>
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