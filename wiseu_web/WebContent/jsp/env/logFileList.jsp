<%-------------------------------------------------------------------------------------------------
 * - [환경설정>로그 관리] 디렉터리와 파일 목록 출력
 * - URL : /env/logFileList.do <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvLogFileController <br/>
 * - 이전 파일명 : env_logfiledown.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<title><spring:message code="env.msg.logfiledownload"/></title>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 다운로드 버튼 클릭
        $("#downloadBtn").on("click", function(event) {
            var chklen = $('input[name=downCheck]');
            var fileList = "";
            var limitSize = 50 * 1024; //50MB (단위KB)
            var size = 0;
            var selectCnt = $("input:checkbox[name=downCheck]:checked").length;
            if(chklen.length > 0) {
                for(var i=0; i<chklen.length; i++) {
                    if(chklen[i].checked == true) {
                        var chklenValue = chklen[i].value.split('|');
                        if(selectCnt > 1) {  //2개 이상의 파일인 경우 50MB이상이면 경고창을 띄운다.
                            size += parseInt(chklenValue[1]);
                            if(size > limitSize) {
                                alert('<spring:message code="env.msg.upto50MBdownload" />');  // 용량이 50MB를 초과하는 파일은 하나씩만 선택해야 합니다.
                                return;
                            }
                        }

                        //파일경로를 셋팅한다
                        fileList += (fileList == "") ? chklenValue[0] : "╊" + chklenValue[0]; //파이프
                    }
                }

                if(fileList != "") {
                    if(confirm('<spring:message code="env.msg.checkfiledownload" />')) {  // 다운로드하겠습니까?
                        donwload(fileList)
                    }

                    return;
                } else {
                    alert('<spring:message code="env.msg.downloadfail.selectfile" />');  // 로그 파일을 선택하세요.
                }
            } else {
                alert('<spring:message code="env.msg.downloadfail.selectfile" />');  // 로그 파일을 선택하세요.
            }
        });

        // 홈 디렉터리 콤보박스 선택
        $("select[name=selectedIndex]").on("change", function(event) {
            $("#pageFrm input[name=cmd]").val('list');
            $("#pageFrm input[name=selectedPaths]").val("");
            $("#pageFrm").attr('action', '/env/logFileList.do').submit();  // /env/env_logfiledown.do
        });

        // 체크박스 전체 클릭
        $("#table-check-all").on("click", function(event) {
            $.mdf.checkAll("#table-check-all");
        });
    }

    function initPage() {
        var selectValue = "${rootFolderIndex}";

        if($.mdf.isNotBlank(selectValue)) {
            $("#selectedIndex > option[value='"+selectValue+"']").attr("selected","true");
        }
    }

    // 폴더 클릭 시 (이동)
    function selectPath(value) {
        if($("#pageFrm input[name=rdValue]").val() == value) {
            alert('<spring:message code="env.msg.toplevel" />');  // 최상위 폴더입니다.
        } else {
            $("#pageFrm input[name=selectedPaths]").val(value);
            $("#pageFrm").attr('action', '/env/logFileList.do').submit();  // /env/env_logfiledown.do
        }
    }

    // 다운로드
    function donwload(fileList) {
        $("#pageFrm input[name=selectedPaths]").val(fileList);
        $("#pageFrm").attr('action', '/env/downloadLogFile.do').submit();
    }

    // 미리보기팝업
    function preview(path, size) {
        if(size > 0 && size > 1024) { //파일크기가 1MB가 넘으면 다운로드를 수행한다.
            if(confirm('<spring:message code="env.msg.checkfiledownload" />')) {  // 다운로드하겠습니까?
                donwload(path)
            }

            return;
        }

        var url = "/env/logFileViewPopup.do?selectedPaths="+path;  // /env/previewLogFile.do
        $.mdf.popupGet(url, "previewPopup", 1000, 700);
    }
</script>
</head>

<body>
<form id="pageFrm" name="pageFrm" method="post">
<input type="hidden" name="cmd" />
<input type="hidden" name="selectedPaths" />
<input type="hidden" name="rdValue" value="${currentParentDir}" />
<input type="hidden" name="fileList" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="env.title.log.management" /></h3>
            </div>

            <div class="card-body pb-0">
                <button class="btn btn-sm btn-outline-primary btn-round" id="downloadBtn">
                    <i class="fas fa-download"></i> <spring:message code="button.download" /><!-- 다운로드 -->
                </button>
            </div>

            <div class="card-body gridWrap">
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <thead class="thead-light">
                            <colgroup>
                                <col width="125" />
                                <col width="20%" />
                                <col width="140" />
                                <col width="*" />
                            </colgroup>
                        </thead>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="env.msg.homedic"/></th><!-- 홈 디렉터리 -->
                            <td>
                                <select class="form-control form-control-sm" id="selectedIndex" name="selectedIndex">
                                    <c:forEach var="rootDir" items="${rootFolderList}" varStatus="k">
                                        <option value='${rootDir.folderIndex}'>${rootDir.folderName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <th scope="row"><spring:message code="env.msg.currentdic"/></th><!-- 현재 디렉터리 -->
                            <td>${currentDir}</td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->
            </div>

            <div class="card-body">
                <div class="table-responsive">
                    <div class="table-responsive">
                        <table class="table table-sm dataTable table-hover table-fixed">
                            <thead class="thead-light">
                            <tr>
                                <th scope="col" width="20" class="text-center">
                                    <div class="custom-control custom-checkbox">
                                        <input class="custom-control-input all-check" id="table-check-all" type="checkbox">
                                        <label class="custom-control-label" for="table-check-all"></label>
                                    </div>
                                </th>
                                <th scope="col"><spring:message code="env.msg.filename"/></th>
                                <th scope="col" width="130"><spring:message code="env.msg.lastmodified"/></th>
                                <th scope="col" width="100"><spring:message code="env.msg.size"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr style="cursor: pointer;" onclick="selectPath('${parentDir}')">
                                <td></td>
                                <td class="text-left"><span style="color:#fed55f;"><i class="fas fa-folder fa-lg"></i></span> &lt;<spring:message code="env.msg.parentdic"/>&gt;</td>
                                <td></td>
                                <td></td>
                            </tr>

                            <c:forEach var="FD" items="${subDirMapList}" varStatus="j">
                            <tr style="cursor: pointer;" onclick="selectPath('${FD.folder}')">
                                <td></td>
                                <td class="text-left"><span style="color:#fed55f;"><i class="fas fa-folder fa-lg" ></i></span> ${FD.folderName}</td>
                                <td>${FD.folderDate}</td>
                                <td></td>
                            </tr>
                            </c:forEach>

                            <c:forEach var="FL" items="${fileMapList}" varStatus="i">
                            <tr style="cursor: pointer;" onclick="preview('${FL.filePath}','${FL.fileSize}')">
                                <td onclick="event.cancelBubble=true">
                                    <div class="custom-control custom-checkbox">
                                        <input class="custom-control-input" id="table-check-all_${i.count}" type="checkbox"  name="downCheck" value="${FL.filePath}|${FL.fileSize}">
                                        <label class="custom-control-label" for="table-check-all_${i.count}"></label>
                                    </div>
                                </td>
                                <td class="text-left"><i class="fas fa-file-alt fa-lg" ></i> ${FL.fileName}</td>
                                <td>${FL.fileDate}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${FL.fileSize < 1024}">
                                            ${FL.fileSize}KB
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${FL.fileSize div 1024}" pattern=".0"/>MB
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div><!-- e.Card body -->
        </div>
    </div><!--e.container-fluid-->
</div>
</form>
</body>
</html>

