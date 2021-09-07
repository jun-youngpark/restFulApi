<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<table class="dp-none">
<!-- 첨부파일 추가 시 복사를 위한 태그 Display none 처리 -->
 <tr id="attachPathClone" class="upload dp-none">
    <td class="text-center">
        <button class="btn btn-sm btn-outline-danger deleteRow" type="button" >
            <i class="fas fa-minus"></i> <!-- 삭제 -->
        </button>
    </td>
    <th scope="row">
        <spring:message code="editor.attach.path"/>
    </th>
    <td class="bg-subtotal">
        <em class="required">required</em><spring:message code="editor.file.name"/><!-- 파일명 -->
    </td>
    <td><input type="text"  class="form-control form-control-sm" placeholder="" name="fileName[0]"></td>
    <td class="bg-subtotal"><em class="required">required</em><spring:message code="editor.file.path"/></td><!-- 파일경로 -->
    <td colspan="3"><input type="text"  class="form-control form-control-sm" name="filePath[0]" ></td>
    <input type="hidden" name="fileType[0]" value="PATH"/>
</tr>

<tr id="attachSmailattClone" class="upload dp-none">
     <td class="text-center">
        <button class="btn btn-sm btn-outline-danger deleteRow" type="button">
            <i class="fas fa-minus"></i> <!-- 삭제 -->
        </button>
    </td>
    <th scope="row">
        <spring:message code="editor.attach.smailatt"/>
    </th>
    <td class="bg-subtotal">
        <em class="required">required</em>
        <spring:message code="editor.attach.path"/>
    </td><!-- 파일 경로 -->
    <td colspan="5"><input type="text"  class="form-control form-control-sm" name="filePath[0]" ></td>
    <input type="hidden" name="fileType[0]" value="SMAILATT"/>
    <input type="hidden" value="unused" class="form-control form-control-sm" placeholder="" name="fileName[0]">
</tr>

<tr id="encPdfClone" class="upload dp-none">
    <td class="text-center">
        <button class="btn btn-sm btn-outline-danger deleteRow" type="button" >
            <i class="fas fa-minus"></i> <!-- 삭제 -->
        </button>
    </td>
    <th scope="row">
        <spring:message code="editor.attach.encpdf"/>
    </th>
    <td class="bg-subtotal">
        <em class="required">required</em><spring:message code="editor.file.name"/><!-- 파일명 -->
    </td>
    <td><input type="text"  class="form-control form-control-sm" name="fileName[0]" ></td>
    <td class="bg-subtotal"><em class="required">required</em><spring:message code="editor.attach.secufield"/></td><!--  비밀번호 필드 -->
    <td><input type="text"  class="form-control form-control-sm"  name="secuField[0]" ></td>
    <td class="bg-subtotal"><em class="required">required</em><spring:message code="editor.file.path"/></td><!--  파일경로-->
    <td><input type="text"  class="form-control form-control-sm" name="filePath[0]" ></td>
    <input type="hidden" name="fileType[0]" value="ENCPDF"/>
    <input type="hidden" name="encYn[0]" value="Y"/>
</tr>
<!-- // 첨부파일 추가 시 복사를 위한 태그 Display none 처리 -->
</table>

