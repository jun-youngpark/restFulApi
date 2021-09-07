<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 핸들러 수정 사유 입력 -->
<div id="layerReason" style="display:none;" class="reasonWrap">
    <div class="card pop-card reason-layer" style="height: 400px; border: 1px solid #c1c1c1;">
        <div class="card-header"><!-- title -->
            <h5 class="mb-0"><spring:message code="ecare.history.title"/></h5><!-- 수정 사유 입력-->
        </div>
        <div class="card-body">
            <div class="table-responsive gridWrap">
                <table class="table table-sm dataTable table-fixed">
                    <colgroup>
                        <col width="*" />
                    </colgroup>
                    <tbody>
                    <tr>
                        <td> <textarea name="historyPopupMsg" id="historyPopupMsg" class="form-control" row="4" style="height: 150px;"></textarea></td>
                    </tr>
                    </tbody>
                </table>
                <div id="divbtn" style="text-align: center; margin-top: 30px;">
                    <button type="button" class="btn btn-outline-primary" id="reasonSaveBtn" >
                        <spring:message code="button.save"/><!-- 저장 -->
                    </button>
                    <button type="button" class="btn btn-outline-primary" id="closeBtn">
                        <spring:message code="button.close"/><!-- 닫기 -->
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>