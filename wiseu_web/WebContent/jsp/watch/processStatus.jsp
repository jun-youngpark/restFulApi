<%-------------------------------------------------------------------------------------------------
 * - [wiseWatch>프로세스 탭] LTS/MTS/ITS/BSE 프로세스 상태 출력
 * - URL : /watch/processStatus_lts.do
 * - URL : /watch/processStatus_mts.do
 * - URL : /watch/processStatus_its.do
 * - URL : /watch/processStatus_bse.do
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchLtsController
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchMtsController
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchItsController
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchBseController
 * 발송 엔진 상세내역 모니터링
 * - 이전 파일명 : watch_info_server.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Page-Enter" content="BlendTrans(Duration=0,2)"/>
<meta http-equiv="Page-Exit" content="BlendTrans(Duration=0,2)"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>wiseWatch</title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    var serverId = "${infoServer.serverId}";

    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        setInfoServer();
    }

    function setInfoServer() {
        $.mdf.getJSON("/watch/getProcessInfo.json?serverId=" + serverId, function(infoServer) {
            if(infoServer.serverStatus == "1") {
                startting(); //기동중 상태
            } else {
                stopping(); //중지중 상태
            }
            $("#jobCount").html(infoServer.jobCount);
            $("#processThreadCount").html(infoServer.processThreadCount);
            $("#worksThreadCount").html(infoServer.worksThreadCount);
            $("#openFileDescriptorCount").html(infoServer.openFileDescriptorCount);
            $("#maxMemory").html(infoServer.maxMemory);
            $("#usedMemory").html(infoServer.usedMemory);
            $("#cpuRate").html(infoServer.cpuRate);
            $("#maxQueueSize").html(infoServer.maxQueueSize);
            $("#usedQueueSize").html(infoServer.usedQueueSize);

            if(serverId.indexOf("LTS",0)>-1) setPollingActStatus();

            setTimeout(setInfoServer, 5 * 1000);
        }, {global : false});
    }

    // 서버 기동 및 정지
    function startAndStopServer(serverId) {
        var status = $("#" + serverId + "statusValue").val();

        if(status == "0") {
            if(confirm(serverId + " <spring:message code='watch.alert.msg1'/>") == true) {  // 프로세스를 실행하시겠습니까?
                $("#status").text("<spring:message code='button.starting'/>");  // 기동중
                $("#" + serverId + "statusValue").val("2");

                var param = {};
                param.serverId = serverId;
                $.post("/watch/startProcess.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='watch.alert.start'/>");  // 기동되었습니다
                        setInfoServer();
                    } else {
                        stopping();
                        alert("<spring:message code='watch.alert.fail'/>");  // 실패하였습니다.
                    }
                }) ;
            }
        } else if(status == "1") {
            if(confirm(serverId + " <spring:message code='watch.alert.msg2'/>") == true) {  // 프로세스를 멈추시겠습니까?
                $("#status").text("중지중");
                $("#" + serverId + "statusValue").val("-1");

                var param = {};
                param.serverId = serverId;
                $.post("/watch/stopProcess.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='watch.alert.stop'/>");  // 멈추었습니다.
                        setInfoServer();
                    } else {
                        startting();
                        alert("<spring:message code='watch.alert.fail'/>");  // 실패하였습니다.
                    }
                });
            }
        } else if(status == "2") {
            alert(serverId + " <spring:message code='watch.alert.run'/>");  // 기동중입니다.
        } else if(status == "-1") {
            alert(serverId + " <spring:message code='watch.alert.stop2'/>");  // 정지중입니다.
        }
    }

    // 엔진 환경설정
    function openConfig(serverId) {
        var url = '/watch/processConfigPopup.do?serverId='+serverId;  // /watch/watch_config_server.do?cmd=configServer
        $.mdf.popupGet(url, 'configPopup', 850, 755);
    }

    //기동중 상태
    function startting() {
        $("#status").text("<spring:message code='ecare.status.E'/>");  // 중지
        $("#light").removeClass("stop");
        $("#statusButton").removeClass()
        $("#statusButton").addClass("btn btn-sm btn-outline-danger");
        $("#" + serverId + "statusValue").val("1");
        $("#" + serverId + "config").css("display", "inline");
        $("#startTime").html("<dt><spring:message code='watch.menu.time'/></dt><dd>"+'${infoServer.startTime}'+"</dd>");  // 가동시간
        $("#startTime").show();
    }

    //중지중 상태
    function stopping() {
        $("#status").text("<spring:message code='button.startup'/>");  // 기동
        $("#light").addClass("stop");
        $("#statusButton").removeClass()
        $("#statusButton").addClass("btn btn-sm btn-outline-info");
        $("#startTime").html("");
        $("#startTime").hide();
        $("#" + serverId + "statusValue").val("0");
    }

    function setPollingActStatus() {
        $.mdf.getJSON("/watch/processStatus.json?serverId=" + serverId, function(result) {
            if(result.code == "OK") {
                var sTxt = "";
                if(result.value == "A") {
                    sTxt = "(Active)";
                } else if(result.value == "S") {
                    sTxt = "(StandBy)";
                } else if(result.value == "E") {
                    sTxt = "(<font color=red>Down</font>)";
                }

                $("#pollingActStatus").html(sTxt);
            }
        }, {global : false});
    }
</script>
</head>

<body>
<div class="card mb-0">
    <div class="card-body pt-1">
        <div class="content_box">
            <div class="group">
                <div class="cont_head">
                    <dl>
                        <dt><spring:message code="watch.menu.id"/></dt><!-- 프로세스 ID -->
                        <dd>
                            ${infoServer.serverId}<!--  서버명 -->
                            <!-- LTS 액티브/스탠바이 모드 -->
                            <c:if test="${pollingActStatus eq 'A'}">&nbsp;<span id="pollingActStatus">(Active)</span></c:if>
                            <c:if test="${pollingActStatus eq 'S'}">&nbsp;<span id="pollingActStatus">(StandBy)</span></c:if>
                            <c:if test="${pollingActStatus eq 'E'}">&nbsp;<span id="pollingActStatus">(<font color="red">Down</font>)</span></c:if>
                        </dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="watch.menu.status"/></dt><!-- 상태 -->
                        <dd>
                            <em id="light" class="status"><i class="fas fa-lightbulb"></i></em>
                            <button id="statusButton" type="button" class="btn btn-sm btn-outline-danger" onclick="startAndStopServer('${infoServer.serverId}');">
                                <span id="status"><spring:message code="ecare.status.E"/></span><!-- 중지 -->
                            </button>
                            <input type="hidden" id="${infoServer.serverId}statusValue" value="${infoServer.serverStatus}"/>
                        </dd>
                    </dl>
                    <dl id="startTime">
                        <dt><spring:message code='watch.menu.time'/></dt><!-- 가동시간 -->
                        <dd>${infoServer.startTime}</dd>
                    </dl>
                    <div class="r_btn">
                        <button type="button" class="btn btn-sm btn-outline-primary" onclick="openConfig('${infoServer.serverId}');return false;", '_blank', 'width=500,height=400')">
                            <spring:message code='button.env.setting'/><!-- 환경설정 -->
                        </button>
                    </div>
                </div><!-- //head -->

                <div class="cont_body pb-3">
                    <div class="table-responsive">
                        <table class="table table-xs dataTable">
                            <thead class="thead-light">
                            <tr>
                                <th scope="col"><spring:message code="watch.menu.work"/></th><!-- 작업 -->
                                <th scope="col"><spring:message code="watch.menu.pthread"/></th><!-- 프로세스쓰레드 -->
                                <th scope="col"><spring:message code="watch.menu.wthread"/></th><!-- 작업쓰레드 -->
                                <th scope="col"><spring:message code="watch.menu.file"/></th><!-- 작업파일 -->
                                <th scope="col"><spring:message code="watch.menu.mmemory"/></th><!-- 최대 메모리 -->
                                <th scope="col"><spring:message code="watch.menu.umemory"/></th><!-- 사용 메모리 -->
                                <th scope="col">CPU</th>
                                <th scope="col"><spring:message code="watch.menu.mque"/></th><!-- 최대큐 -->
                                <th scope="col"><spring:message code="watch.menu.uque"/></th><!-- 사용큐 -->
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td id="jobCount">${infoServer.jobCount}</td>
                                <td id="processThreadCount">${infoServer.processThreadCount}</td>
                                <td id="worksThreadCount">${infoServer.worksThreadCount}</td>
                                <td id="openFileDescriptorCount">${infoServer.openFileDescriptorCount}</td>
                                <td id="maxMemory">${infoServer.maxMemory}</td>
                                <td id="usedMemory">${infoServer.usedMemory}</td>
                                <td id="cpuRate">${infoServer.cpuRate}</td>
                                <td id="maxQueueSize">${infoServer.maxQueueSize}</td>
                                <td id="usedQueueSize">${infoServer.usedQueueSize}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div><!-- //Light table -->
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
