<%-------------------------------------------------------------------------------------------------
 * - [환경설정>기본핸들러 설정] 기본핸들러 등록/수정 화면 <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvDefaultHandleController <br/>
 * - 이전 파일명 : env_handlerRegist.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="env.msg.handler1"/></title>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 구분(캠페인/이케어) 라디오버튼 클릭
        $("input:radio[name=emec]").on("change", function(event) {
            if(this.value == "em") {  // 캠페인 선택시
                //divAbTestInner();
                $("#serviceTypeTr").hide();  // 서비스타입 숨김
                $("#abTestYnTr").show();  // A/B테스트 표시
                //useChannelSelect("on");
                hideHandleAttrTr();  // 핸들러 속성 숨김
            } else if(this.value == "ec") {  // 이케어 선택시
                //useChannelSelect("on");
                var channel = $("input:radio[name=channel]:checked").val();
                if(channel == 'M' && (${useSecurityMail eq true || usePdfMail eq true})) {  // 이케어 보안메일 사용시
                    showHandleAttrTr();
                } else {
                    hideHandleAttrTr();  // 핸들러 속성(기본,보안) 숨김
                }

                $("#serviceTypeTr").show();  // 서비스타입 표시
                $("#abTestYnTr").hide();  // A/B테스트 숨김
            }
        });

        // 채널 선택
        $("input:radio[name=channel]").on("change", function(event) {
            var emec = $("input:radio[name=emec]:checked").val();
            if(this.value == "M" && (${useSecurityMail eq true || usePdfMail eq true}) && emec=='ec') {  // 이케어 보안메일 사용시
                showHandleAttrTr();
            } else {
                hideHandleAttrTr();
            }
        });

        // 목록 버튼 클릭
        $("#listBtn").on("click", function(event) {
            var url = "/env/defaultHandlerList.do?nowPage=" + $.mdf.defaultIfBlank("${param.nowPage}", "1");  // /env/deafulthandler.do
            url += "&searchSeviceType=${param.searchSeviceType}&searchChannel=${param.searchChannel}&searchWord=${param.searchWord}";

            location.href = url;
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                handleNm    : {notBlank : true},
                handleType  : {required : true},
                emec        : {required : true},
                channel     : {required : true},
                abTestYn    : {required : true},
                handleAttr  : {required : true},
                serviceType : {required : true},
                handler     : {notBlank : true}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            if(!confirm("<spring:message code='common.alert.3' />")) {  // 저장 하시겠습니까?
                return;
            }

            $("input[name=emec]").removeAttr("disabled");
            $("input[name=serviceType]").removeAttr("disabled");
            $("input[name=channel]").removeAttr("disabled");
            $("input[name=handleAttr]").removeAttr("disabled");
            $("input[name=handleType]").removeAttr("disabled");
            $("input[name=abTestYn]").removeAttr("disabled");

            var seq = $("input[name=seq]").val();
            if($.mdf.isNotBlank(seq) || parseInt(seq) > 0) {  // 기존 핸들러 수정
                $('#updateForm').attr('action', "/env/updateHandler.do").submit();
                alert("<spring:message code='common.alert.5'/>");  // 수정되었습니다.
            } else {  // 신규 핸들러 등록
                $('#updateForm').attr('action', "/env/insertHandler.do").submit();
            }
        });
    }

    function initPage() {
        var seq = $("input[name=seq]").val();
        if($.mdf.isNotBlank(seq) || parseInt(seq) > 0) {  // 기존 핸들러 수정시
            $("input:radio[name=handleType][value=${envHandlerVo.handleType}]").attr("checked", true);
            $("input:radio[name=channel][value=${envHandlerVo.channel}]").attr("checked", true);
            $("input:radio[name=handleAttr][value=${envHandlerVo.handleAttr}]").attr("checked", true);

            if($.mdf.isBlank("${envHandlerVo.serviceType}")) {  // 서비스타입(실시간,준실시간,스케줄)이 없는 경우 - 캠페인인 경우
                $("input:radio[name=emec][value=em]").attr("checked", true).trigger('change');
                $("input:radio[name=abTestYn][value=${envHandlerVo.abTestYn}]").attr("checked", true);
            } else {  // 이케어인 경우
                $("input:radio[name=emec][value=ec]").attr("checked", true).trigger('change');
                $("input:radio[name=serviceType][value=${envHandlerVo.serviceType}]").attr("checked", true);
            }

            $("input:radio[name=emec]").attr("disabled","disabled");
            $("input:radio[name=serviceType]").attr("disabled","disabled");
            $("input:radio[name=channel]").attr("disabled","disabled");
            $("input:radio[name=handleAttr]").attr("disabled","disabled");
            $("input:radio[name=handleType]").attr("disabled","disabled");
            $("input:radio[name=abTestYn]").attr("disabled","disabled");
        }

        if(checkUseOnlyMail()) {
            $("input:radio[name=channel][value=M]").attr("checked", true);
        }
    }

    /*function useChannelSelect(use) {
        if(use == "on" && !checkUseOnlyMail()) {
            $("#channel_M").removeAttr("checked");
        } else {
            $("#channel_M").attr("checked", true);  // Use default Mail
        }
    }*/

    function checkUseOnlyMail() {
        var channelList = "${channelUseList}";
        var arrChannel = channelList.split(",");
        if(channelList.indexOf("M") > -1 && arrChannel.length == 1) {
            return true;
        } else {
            return false;
        }
    }

    function showHandleAttrTr() {
        $("#handleAttrTr").show();  // 핸들러 속성(기본,보안)
    }

    function hideHandleAttrTr() {
        $("#handleAttrTr").hide();  // 핸들러 속성(기본,보안)
        $("#handleAttr_D").prop("checked",true);
    }

    /*function divAbTestInner() {
        var inputHtml = "<th><img src='/images/common/content_dot_02.gif' ><spring:message code='env.msg.handler.AB' /></th>"
                      + "<td><input type='radio' id='abTestYn_Y' name='abTestYn' value='Y' /><spring:message code='env.msg.handler.ABON' />&nbsp;"  // 사용
                      + "<input type='radio' id='abTestYn_N' name='abTestYn' value='N' /><spring:message code='env.msg.handler.ABOFF' />&nbsp;</td>";  // 미사용
        $("#emBlock").html(inputHtml);
    }*/
</script>
</head>
<body>
<form id="updateForm" name="updateForm" action="/env/defaultHandlerList.do" method="post"><!-- /env/deafulthandler.do -->
<input type="hidden" name="seq" id="seq" value="${envHandlerVo.seq}">
<input type="hidden" name="msgType" id="msgType" value="${envHandlerVo.msgType}" />
<input type="hidden" name="nowPage" value="${param.nowPage}" />
<input type="hidden" name="searchServiceType" value="${param.searchSeviceType}" />
<input type="hidden" name="searchChannel" value="${param.searchChannel}" />
<input type="hidden" name="searchWord" value="${param.searchWord}" />

<div class="main-panel">
    <div class="container-fluid">
        <div class="card mb-4">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="env.msg.handler"/></h3><!-- 기본핸들러 설정 -->
            </div>

            <div class="card-body gridWrap">
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="10%" />
                            <col width="*" />
                            <col width="12%" />
                            <col width="35%" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.handler.name"/></th><!-- 핸들러명 -->
                            <td><input type="text" class="form-control form-control-sm" value="${envHandlerVo.handleNm}" name="handleNm" id="handleNm" /></td>
                            <th scope="row"><em class="required">required</em><spring:message code="common.menu.handler"/> <spring:message code="env.menu.handler.type"/></th><!-- 핸들러 타입 -->
                            <td>
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="handleType" id="handleType_G" value="G" class="custom-control-input">
                                        <label class="custom-control-label" for="handleType_G">GROOVY</label>
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="handleType" id="handleType_S" value="S" class="custom-control-input" >
                                        <label class="custom-control-label" for="handleType_S">SCRIPT</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.handler.service"/></th><!-- 구분 -->
                            <td colspan="3">
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="em" name="emec" value="em" class="custom-control-input">
                                        <label class="custom-control-label" for="em"><spring:message code="common.menu.campaign"/></label>
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="ec" name="emec" value="ec" class="custom-control-input">
                                        <label class="custom-control-label" for="ec"><spring:message code="common.menu.ecare_${webExecMode}"/></label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.handler.channel"/></th><!-- 채널 -->
                            <td colspan="3">
                                <div class="form-inline">
                                <c:if test="${fn:contains(channelUseList, 'M')}">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="channel" id="channel_M" value="M" class="custom-control-input">
                                        <label class="custom-control-label" for="channel_M"><spring:message code="common.channel.M"/></label>
                                    </div>
                                </c:if>
                                <c:if test="${fn:contains(channelUseList, 'S')}">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="channel" id="channel_S" value="S" class="custom-control-input">
                                        <label class="custom-control-label" for="channel_S"><spring:message code="common.channel.S"/></label>
                                    </div>
                                </c:if>
                                <c:if test="${fn:contains(channelUseList, 'T')}">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="channel" id="channel_T" value="T" class="custom-control-input">
                                        <label class="custom-control-label" for="channel_T"><spring:message code="common.channel.T"/></label>
                                    </div>
                                </c:if>
                                <c:if test="${fn:contains(channelUseList, 'A')}">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="channel" id="channel_A" value="A" class="custom-control-input">
                                        <label class="custom-control-label" for="channel_A"><spring:message code="common.channel.A"/></label>
                                    </div>
                                </c:if>
                                <c:if test="${fn:contains(channelUseList, 'C')}">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="channel" id="channel_C" value="C" class="custom-control-input">
                                        <label class="custom-control-label" for="channel_C"><spring:message code="common.channel.C"/></label>
                                    </div>
                                </c:if>
                                <c:if test="${fn:contains(channelUseList, 'B')}">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="channel" id="channel_B" value="B" class="custom-control-input">
                                        <label class="custom-control-label" for="channel_B"><spring:message code="common.channel.B"/></label>
                                    </div>
                                </c:if>
                                <c:if test="${fn:contains(channelUseList, 'P')}">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="channel" id="channel_P" value="P" class="custom-control-input">
                                        <label class="custom-control-label" for="channel_P"><spring:message code="common.channel.P"/></label>
                                    </div>
                                </c:if>
                                <c:if test="${fn:contains(channelUseList, 'F')}">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="channel" id="channel_F" value="F" class="custom-control-input">
                                        <label class="custom-control-label" for="channel_F"><spring:message code="common.channel.F"/></label>
                                    </div>
                                </c:if>
                                </div>
                            </td>
                        </tr>
                        <tr id="handleAttrTr" style="display: none;">
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.handler.hproperty"/></th><!-- 핸들러 속성 -->
                            <td colspan="3" >
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="handleAttr_D" name="handleAttr" value="D" class="custom-control-input" >
                                        <label class="custom-control-label" for="handleAttr_D"><spring:message code="env.menu.handler.default"/></label><!-- 기본 -->
                                    </div>
                                   <c:if test="${useSecurityMail}">
	                                   <div class="custom-control custom-radio custom-control-inline">
	                                       <input type="radio" id="mailtype_smail" name="handleAttr" value="SMAIL" class="custom-control-input" >
	                                        <label class="custom-control-label" for=mailtype_smail><spring:message code="ecare.mailtype.smail" /><!-- 보안메일 --></label>
	                                   </div>
                                   </c:if>
                                   <c:if test="${usePdfMail}">
	                                   <div class="custom-control custom-radio custom-control-inline">
	                                       <input type="radio" id="mailtype_pdf" name="handleAttr" value="HTMLPDF" class="custom-control-input" >
	                                       <label class="custom-control-label" for="mailtype_pdf">HTMLPDF</label>
	                                   </div>
                                   </c:if>
                                </div>
                            </td>
                        </tr>
                        <tr id="abTestYnTr">
                            <th scope="row"><em class="required">required</em><spring:message code='env.msg.handler.AB' /></th><!-- A/B테스트 -->
                            <td colspan="3">
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="abTestYn_Y" name="abTestYn" class="custom-control-input" value="Y" >
                                        <label class="custom-control-label" for="abTestYn_Y"><spring:message code='env.msg.handler.ABON' /></label><!-- 사용 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="abTestYn_N" name="abTestYn" class="custom-control-input" value="N">
                                        <label class="custom-control-label" for="abTestYn_N"><spring:message code='env.msg.handler.ABOFF' /> </label><!-- 미사용 -->
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr id="serviceTypeTr" class="dp-none">
                           <th scope="row"><em class="required">required</em><spring:message code="env.menu.handler.stype"/></th><!-- 서비스타입 -->
                            <td colspan="3">
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="serviceType_N" class="custom-control-input" name="serviceType" value="N" >
                                        <label class="custom-control-label" for="serviceType_N"><spring:message code='ecare.type.SN'/></label><!-- 준실시간 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="serviceType_S" class="custom-control-input" name="serviceType" value="S" >
                                        <label class="custom-control-label" for="serviceType_S"><spring:message code='ecare.type.S'/></label><!-- 스케쥴 -->
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="env.menu.handler.des"/></th><!-- 핸들러 설명  -->
                            <td colspan="3"><input type="text" class="form-control form-control-sm" value="${envHandlerVo.handleDesc}" id="handleDesc" name="handleDesc" /></td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->

                <div class="form-group">
                    <textarea class="form-control" id="handler" name="handler" style="height:450px;">${envHandlerVo.handler}</textarea>
                </div>

                <c:if test="${sessionScope.write eq 'W'}">
                <div class="card-footer pb-4 btn_area"><!-- button area -->
                    <button id="listBtn" class="btn btn-outline-primary btn-round">
                        <spring:message code="button.list" /><!-- 목록 -->
                    </button>
                    <button id="saveBtn" class="btn btn-outline-primary">
                        <spring:message code="button.save" /><!-- 저장 -->
                    </button>
                </div>
                </c:if>
            </div><!-- e.Card body -->
        </div>
    </div><!--e.container-fluid-->
</div>
</form>

<script type="text/javascript">
// <c:if test="${not empty total}">
    alert("<spring:message code='env.alert.handler.save'/>.\nTotal: ${total} (Success: ${success}, Pass: ${pass}, Error: ${error})");  // 기본핸들러가 저장되었습니다.
// </c:if>
</script>
</body>
</html>