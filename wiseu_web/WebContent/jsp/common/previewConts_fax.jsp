<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계>캠페인 미리보기(팝업)] 하단 미리보기 내용 출력(FAX)
 * - [이케어>이케어 등록>2단계>이케어 미리보기(팝업)] 하단 미리보기 내용 출력(FAX)
 * - URL : /common/previewMime.do
 * - Controller : com.mnwise.wiseu.web.common.web.MailMimeViewController
 * - 이전 파일명 : fax_html_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<table class="table table-sm dataTable table-fixed border-top-0 border-bottom-0" style="margin-top: 0 !important; margin-bottom: 0 !important;">
<tbody>
    <tr>
        <td>${mimeVo.mime}</td>
    </tr>
</tbody>
</table>