<%-------------------------------------------------------------------------------------------------
 * - [환경설정>DB 관리] DB 관리 <br/>
 * - URL : /env/database.do <br/>
 * - Controller :com.mnwise.wiseu.web.env.web.EnvDataBaseController  <br/>
 * - 이전 파일명 : env_database.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title><spring:message code="env.msg.db"/></title>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // DB 목록에서 DB 선택
        $('#dbListTable tbody').on('click', 'tr', function(event) {
            $('#dbListTable tr.selected').removeClass('selected');
            $(this).addClass('selected');
        });

        // DB종류 콤보박스 선택
        $("select[name=dbKind]").on("change", function(event) {
            var kind = $(this).val();

            if(kind == "ORACLE") {
                $("input[name=driverNm]").val("oracle.jdbc.OracleDriver");
                $("input[name=driverDsn]").val("jdbc:oracle:thin:@ip:port:service name");
            } else if(kind == "MSSQL2000") {
                $("input[name=driverNm]").val("com.microsoft.jdbc.sqlserver.SQLServerDriver");
                $("input[name=driverDsn]").val("jdbc:microsoft:sqlserver://ip:port;DatabaseName=DB name");
            } else if(kind == "MSSQL2005") {
                $("input[name=driverNm]").val("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                $("input[name=driverDsn]").val("jdbc:sqlserver://ip:port;DatabaseName=DB name");
            } else if(kind == "DB2") {
                $("input[name=driverNm]").val("com.ibm.db2.jcc.DB2Driver");
                $("input[name=driverDsn]").val("jdbc:db2:ip:port/DB name");
            } else if(kind == "INFORMIX") {
                $("input[name=driverNm]").val("com.informix.jdbc.IfxDriver");
                $("input[name=driverDsn]").val("jdbc:informix-sqli://ip:port/DB name:INFORMIXSERVER=server name");
            } else if(kind == "SYBASE") {
                $("input[name=driverNm]").val("com.sybase.jdbc2.jdbc.SybDriver");
                $("input[name=driverDsn]").val("jdbc:sybase:Tds:ip:port/DB name");
            } else if(kind == "MYSQL") {
                $("input[name=driverNm]").val("com.mysql.jdbc.Driver");
                $("input[name=driverDsn]").val("jdbc:mysql://ip:port/DB namee");
            }

            $("input[name=driverNm]").blur();
            $("input[name=driverDsn]").blur();
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                dbKind     : {selected : true},
                serverNm   : {notBlank : true},
                driverNm   : {notBlank : true},
                driverDsn  : {notBlank : true},
                dbUserId   : {notBlank : true},
                dbPassword : {notBlank : true},
                testQuery  : {notBlank : true}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            if($("input[name=dbTest]").val() != "success") {
                alert("<spring:message code='env.alert.db.msg1'/>");  // DB 체크가 성공하지 않았습니다.
                return;
            }

            if(!confirm("<spring:message code='env.alert.db.msg5'/>")) {  // 설정을 저장하시겠습니까?\\n변경된 설정을 발송 시 이용하려면 LTS 프로세스를 재시작해야 합니다.
                $("input").attr('readonly', false);
                $("select").attr('disabled', false);
                return;
            }

            $("input").attr('readonly', false);
            $("select").attr('disabled', false);

            var cmd = $("input[name=cmd]").val();
            if(cmd == "insert") {
                $('#updateForm').attr('action', "/env/insertDatabase.do").submit();
            } else if(cmd == "update") {
                $('#updateForm').attr('action', "/env/updateDatabase.do").submit();
            }
        });

        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if(confirm("[" + $("input[name=serverNm]").val() + "] <spring:message code = 'common.alert.4'/>")) {  // 삭제 하시겠습니까?
                $('#updateForm').attr('action', "/env/deleteDatabase.do").submit();
            }
        });

        // 초기화 버튼 클릭
        $("#resetBtn").on("click", function(event) {
            $.mdf.resetForm("#updateForm");

            $("input[name=cmd]").val("insert");
            $("input").attr('readonly', false);
            $("select").attr('disabled', false);
        });

        // DB 체크 버튼 클릭
        $("#dbCheckBtn").on("click", function(event) {
            var param = $.mdf.serializeObject('#updateForm');
            $.post("/env/checkDatabase.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    $("input[name=dbTest]").val("success");
                    $("input").attr('readonly', true);
                    $("select").attr('disabled', true);
                    alert("<spring:message code='env.alert.db.msg2'/>");  // DB 체크 성공
                } else {
                    $("input[name=dbTest]").val("fail");
                    alert("<spring:message code='env.alert.db.msg3'/>");  // DB 체크 실패
                }
            });
        });
    }

    function initPage() {
        new mdf.DataTable("#dbListTable");
    }

    // DB 목록에서 특정 DB 접속정보 클릭
    function choice(dbInfoSeq) {
        $.mdf.resetForm("#updateForm");

        $("input[name=cmd]").val("update");
        $("input[name=dbInfoSeq]").val(dbInfoSeq);
        $("select[name=dbKind]").val($('#dbKind_' + dbInfoSeq).text());
        $("input[name=serverNm]").val($('#serverNm_' + dbInfoSeq).text());
        $("input[name=driverNm]").val($('#driverNm_' + dbInfoSeq).text());
        $("input[name=driverDsn]").val($('#driverDsn_' + dbInfoSeq).text());
        $("input[name=dbUserId]").val($('#dbUserId_' + dbInfoSeq).text());
        $("input[name=dbPassword]").val('');
        $("input[name=en]").val($('#encoding_' + dbInfoSeq).text());
        $("input[name=de]").val($('#decoding_' + dbInfoSeq).text());
        $("input[name=testQuery]").val($('#testQuery_' + dbInfoSeq).text());
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="env.msg.db"/></h3><!-- DB 관리 -->
            </div>

            <div class="card-body">
                <div class="table-responsive overflow-x-hidden">
                    <table class="table table-sm dataTable table-hover table-fixed" id="dbListTable">
                        <thead class="thead-light">
                        <tr>
                            <th width="17%"><spring:message code="env.menu.db.sname"/></th><!-- 서버명 -->
                            <th width="29%">Driver Name</th>
                            <th width="*">Driver DNS</th>
                            <th width="90"><spring:message code="env.menu.db.type"/></th><!-- DB종류 -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="dbInfo" items="${databaseList}" varStatus="status">
                            <tr style="cursor: pointer;" onclick="choice('${dbInfo.dbInfoSeq}');">
                                <td id="serverNm_${dbInfo.dbInfoSeq}" class="text-left" style="cursor:pointer;">${dbInfo.serverNm}</td>
                                <td id="driverNm_${dbInfo.dbInfoSeq}" class="text-left">${dbInfo.driverNm}</td>
                                <td id="driverDsn_${dbInfo.dbInfoSeq}" class="text-left">${dbInfo.driverDsn}</td>
                                <td id="dbKind_${dbInfo.dbInfoSeq}">${dbInfo.dbKind}</td>
                                <span id="dbUserId_${dbInfo.dbInfoSeq}" style="display:none;">${dbInfo.dbUserId}</span>
                                <span id="encoding_${dbInfo.dbInfoSeq}" style="display:none;">${dbInfo.encoding}</span>
                                <span id="decoding_${dbInfo.dbInfoSeq}" style="display:none;">${dbInfo.decoding}</span>
                                <span id="testQuery_${dbInfo.dbInfoSeq}" style="display:none;">${dbInfo.testQuery}</span>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div>
        </div><!--e. table card-->

        <div class="card">
            <form id="updateForm" name="updateForm" action="/env/database.do" method="post">
            <input type="hidden" name="cmd" value="insert"/>
            <input type="hidden" name="dbInfoSeq" />
            <input type="hidden" name="dbTest" />

            <div class="card-header"><!-- title -->
                <h1 class="h3 text-primary mb-0"><spring:message code="env.msg.dbinfo"/></h1><!-- DB 정보 -->
            </div>
            <div class="card-body">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="11%" />
                            <col width="*" />
                            <col width="11%" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.db.type"/></th><!-- DB종류 -->
                            <td>
                                <div class="form-row">
                                    <div class="col-4">
                                        <select class="form-control form-control-sm d-inline-block w-100" name="dbKind">
                                            <option><spring:message code="common.option.select"/></option><!-- 선택 -->
                                            <option value="ORACLE">ORACLE</option>
                                            <option value="MSSQL2000">MSSQL2000</option>
                                            <option value="MSSQL2005">MSSQL2005</option>
                                            <option value="DB2">DB2</option>
                                            <option value="INFORMIX">INFORMIX</option>
                                            <option value="SYBASE">SYBASE</option>
                                            <option value="MYSQL">MYSQL</option>
                                        </select>
                                    </div>
                                    <div class="col-8 pl-0">
                                        <button type="button" class="btn btn-sm btn-outline-primary" id="dbCheckBtn">
                                            <spring:message code="button.check.db"/><!-- DB체크 -->
                                        </button>
                                    </div>
                                </div>
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.db.sname"/></th><!-- 서버명 -->
                            <td><input type="text" class="form-control form-control-sm" name="serverNm"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em>Driver Name</th>
                            <td><input type="text" class="form-control form-control-sm" name="driverNm"/></td>
                            <th scope="row"><em class="required">required</em>Driver DSN</th>
                            <td><input type="text" class="form-control form-control-sm" name="driverDsn"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em>User</th>
                            <td><input type="text" class="form-control form-control-sm" name="dbUserId"/></td>
                            <th scope="row"><em class="required">required</em>Password</th>
                            <td><input type="password" class="form-control form-control-sm" name="dbPassword"/></td>
                        </tr>
                        <tr>
                            <th scope="row">Encoding</th>
                            <td><input type="text" class="form-control form-control-sm" name="en"/></td>
                            <th scope="row">Decoding</th>
                            <td><input type="text" class="form-control form-control-sm" name="de"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.db.query"/></th><!-- 테스트쿼리 -->
                            <td colspan="3" class="border-right-0"><input type="text" class="form-control form-control-sm" name="testQuery" id="testQuery"/></td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- //card body -->

            <c:if test="${sessionScope.write eq 'W'}">
            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <button class="btn btn-outline-primary" id="deleteBtn">
                    <spring:message code="button.delete"/><!-- 삭제 -->
                </button>
                <button class="btn btn-outline-primary" id="resetBtn">
                    <spring:message code="button.reset"/><!-- 초기화 -->
                </button>
                <button class="btn btn-outline-primary" id="saveBtn">
                    <spring:message code="button.save"/><!-- 저장 -->
                </button>
            </div><!-- e.button area -->
            </c:if>

            </form>
        </div><!-- e. Table DB 관리-->
    </div>
</div><!-- e.main-pane-->
</body>
</html>