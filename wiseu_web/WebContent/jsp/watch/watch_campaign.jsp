<%-------------------------------------------------------------------------------------------------
 * - [wiseWatch] 캠페인 탭
 * - URL : /watch/watch.do?menu=campaign
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchController
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Page-Enter" content="BlendTrans(Duration=0,2)" />
<meta http-equiv="Page-Exit" content="BlendTrans(Duration=0,2)" />
<title>wiseWatch - Campaign</title>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if(confirm("<spring:message code='watch.alert.del1'/>") == true) {  // 선택한 서비스를 삭제시겠습니까?
                onClickRefreshBtn();
                var param = {
                    tid : $("#tid").val()
                };
                $.post("/watch/deleteService.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='watch.alert.del2'/>");  // 서비스가 삭제 되었습니다.
                        clearServiceInfo();
                    } else {
                        alert("<spring:message code='watch.alert.del3'/>");  // 서비스를 삭제하는데 실패하였습니다.
                    }
                    onClickRefreshBtn();
                });
            }
        });

        // 일시정지 버튼 클릭
        $("#pauseBtn").on("click", function(event) {
            if($("#serviceStatus").val() == 'E') {
                alert("<spring:message code='watch.alert.re5'/>");  // 발송이 종료된 캠페인은 일시정지를 할  수 없습니다.
                return;
            }

            if(confirm("<spring:message code='watch.alert.hold1'/>") == true) {  // 선택한 서비스를 일시정지 하시겠습니까?
                onClickRefreshBtn();
                var param = {
                    tid : $("#tid").val()
                };
                $.post("/watch/pauseService.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='watch.alert.hold2'/>");  // 서비스가 일시정지 되었습니다.
                    } else {
                        alert("<spring:message code='watch.alert.hold3'/>");  // 서비스 일시정지 하는데 실패하였습니다.
                    }
                });
            }
        });

        // 정지 버튼 클릭
        $("#stopBtn").on("click", function(event) {
            if($("#serviceStatus").val() == 'E') {
                alert("<spring:message code='watch.alert.re6'/>");  // 발송이 종료된 캠페인은 정지를 할  수 없습니다.
                return;
            }

            if(confirm("<spring:message code='watch.alert.s1'/>") == true) {  // 선택한 서비스를 정지 하시겠습니까?
                onClickRefreshBtn();
                var param = {
                    tid : $("#tid").val()
                };

                $.post("/watch/stopService.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='watch.alert.s2'/>");  // 서비스가 정지 되었습니다
                    } else {
                        alert("<spring:message code='watch.alert.s3'/>");  // 서비스를 정지 하는데 실패하였습니다.
                    }
                    onClickRefreshBtn();
                });
            }
        });

        // 재기동 버튼 클릭
        $("#restartBtn").on("click", function(event) {
            if($("#serviceStatus").val() == 'E') {
                alert("<spring:message code='watch.alert.re4'/>");  // 발송이 종료된 캠페인은 재가동을 할  수 없습니다.
                return;
            }

            if(confirm("<spring:message code='watch.alert.re1'/>") == true) {  // 선택한 서비스를 재기동 하시겠습니까?
                onClickRefreshBtn();
                var param = {
                    tid : $("#tid").val()
                };
                $.post("/watch/restartService.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='watch.alert.re2'/>");  // 서비스가 재기동 되었습니다.
                    } else {
                        alert("<spring:message code='watch.alert.re3'/>");  // 서비스 재기동을 실패하였습니다.
                    }
                    onClickRefreshBtn();
                });
            }
        });

        // 새로고침 자동실행 정지/시작 버튼 클릭
        $("#refreshBtn").on("click", function(event) {
            event.preventDefault();
            onClickRefreshBtn();
        });

        // 목록수 콤보박스 선택
        $("select[name=selectNum]").on("change", function(event) {
            listService();
        });
    }

    function initPage() {
        if("${errorMsg}" == "-ERR") {
            alert("<spring:message code='watch.alert.msg7'/>");  // TMS 연결에 실패하였습니다.
        } else {
            listService();
        }
    }

    // 페이지가 로딩되면 자동리로딩이 되고 있다.
    var flag = true; // 이미지 변경 및 타이머 중지, 시작 값
    function onClickRefreshBtn() {
        if(flag == true) {
            $("#reloadStatus").text("<spring:message code='button.start'/>"); // 시작
            clearTimeout(timeout); // 새로고침 타이머는 중지됨
            flag = false;
        } else {
            $("#reloadStatus").text("<spring:message code='button.stop'/>"); // 정지
            timeout = setTimeout(listService, 10 * 1000);  // 다시 타이머 시간 설정해서 새로고침 시작함
            flag = true;
        }
    }

    // 캠페인 서비스 목록을 가져온다.
    var timeout = 0;
    function listService() {
        var selectNumCd= $("#selectNum").val();
        var tid = $("input[name='tid']").val();

        if(timeout > 0 && !flag) {
            clearTimeout(timeout);
        } else {
            timeout = setTimeout(listService, 10 * 1000);
        }

        $.post("/watch/serviceList.do?menu=campaign&tid=" + tid + "&selectNum=" + selectNumCd, null, function(result) {  // /watch/watch_list.do?cmd=listService
            $("#serviceListDiv").html(result);
        });
    }

    // 하나의 서비스에 대한 상세 내역을 가져온다.
    function selectServiceStatus(tid, serviceStatus) {
        $("input[name='tid']").val(tid);
        $("input[name='serviceStatus']").val(serviceStatus);

        $.post("/watch/serviceStatus.do?tid=" + tid, null, function(result) {  // /watch/watch_info_service.do?cmd=infoService
            $("#serviceStatusDiv").html(result);
        });
    }

    // 서비스가 삭제된후에 삭제된서비스를 가져오려고 해서 에러가 나는 부분 처리
    function clearServiceInfo() {
        //$("#infoService").attr("src", "#");
        $("input[name='tid']").val("");
        $("input[name='serviceStatus']").val("");
        location.reload(true);
    }
</script>
</head>
<body>
<form id="serviceForm" name="serviceForm">
    <input type="hidden" name="tid" id="tid"/>
    <input type="hidden" name="serviceStatus" id="serviceStatus"/>
</form>

<div class="tab-pane" id="ecare" role="tabpanel" aria-labelledby="ecare-tab">
    <div class="form-row align-items-center py-1 table_option">
        <div class="col-6"><!-- buttons -->
            <button class="btn btn-sm btn-outline-primary btn-round" id="deleteBtn">
                <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
            </button>
            <button class="btn btn-sm btn-outline-primary btn-round" id="pauseBtn">
                <i class="fas fa-pause"></i> <spring:message code="button.pause"/><!-- 일시중지 -->
            </button>
            <button class="btn btn-sm btn-outline-primary btn-round" id="stopBtn">
                <i class="fas fa-stop"></i> <spring:message code="button.stop"/><!-- 정지 -->
            </button>
            <button class="btn btn-sm btn-outline-primary btn-round" id="restartBtn">
                <i class="fas fa-play"></i> <spring:message code="button.restart"/><!-- 재가동 -->
            </button>
        </div>

        <div class="col-6 searchWrap"><!-- search -->
            <div class="r_btn ">
                <span class="h5"><spring:message code="watch.msg.reload" /></span><!-- 새로고침 자동실행 -->
                <button type="button" class="btn btn-xs btn-outline-danger btn-round" id="refreshBtn">
                    <span id='reloadStatus'><spring:message code="button.stop"/></span><!-- 정지 -->
                </button>
            </div>
            <div class="form-group searchbox" >
                <select class="form-control form-control-sm" id="selectNum" name="selectNum">
                    <option value="10" <c:if test="${selectNum eq '10' }">selected</c:if>><spring:message code="watch.msg.ten" /></option><!-- 최근 10개 -->
                    <option value="20" <c:if test="${selectNum eq '20' }">selected</c:if>><spring:message code="watch.msg.twenty" /></option><!-- 최근 10개 -->
                    <option value="50" <c:if test="${selectNum eq '50' }">selected</c:if>><spring:message code="watch.msg.fifty" /></option><!-- 최근 50개 -->
                    <option value="100" <c:if test="${selectNum eq '100' }">selected</c:if>><spring:message code="watch.msg.hundred" /></option><!-- 최근 100개 -->
                    <option value="15" <c:if test="${selectNum eq '15day' }">selected</c:if>><spring:message code="watch.msg.fifteenDay" /></option><!-- 최근 15일 전-->
                    <option value="30" <c:if test="${selectNum eq '30day' }">selected</c:if>><spring:message code="watch.msg.thirtyDay" /></option><!-- 최근 30일 전-->
                </select>
            </div>
        </div>
    </div>

    <div id="serviceListDiv" class="table-responsive overflow-auto mb-3" style="height:370px;"><!-- 발송 목록 -->
    </div>

    <div id="serviceStatusDiv" class="px-0 pb-0"><!-- 서비스상태-->
    </div>
</div>
</body>
</html>
