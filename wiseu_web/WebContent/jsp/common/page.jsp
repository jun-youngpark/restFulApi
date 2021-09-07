<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
function gotoPage(pageNum) {
    $("#pageForm input[name=nowPage]").val(pageNum);
    $("#pageForm").submit();
}
</script>

<form id="pageForm" name="pageForm" action="${actionPath}" method="${method}" <c:if test="${not empty target}">target="${target}"</c:if>>
    <c:forEach var="hiddenItem" items="${hiddenList}" varStatus="status">
    <input type="hidden" name="${hiddenItem.name}" value="${hiddenItem.value}"/>
    </c:forEach>
    <input type="hidden" name="nowPage" />
    <input type="hidden" name="channelType" value="${channelType}"/>
    <input type="hidden" name="tagNo" value="${tagNo}"/>
    <input type="hidden" name="countPerPage" value="${countPerPage}"/>
</form>

<div class="pagination pagination-sm mb-0 pb-2 pt-1 justify-content-center"> <%-- 페이징 --%>
    <ul class="pagination">
    <c:if test="${!empty firstPage}">
        <li class="paginate_button page-item previous" id="datatable-basic_previous">
            <a href="javascript:gotoPage('${firstPage}');" aria-controls="datatable-basic" data-dt-idx="0" tabindex="0" class="page-link">
               <i class="fas fa-angle-double-left"></i>
            </a>
        </li>
    </c:if>

    <c:if test="${!empty prevPage}">
        <li class="paginate_button page-item previous" id="datatable-basic_previous">
            <a href="javascript:gotoPage('${prevPage}');" aria-controls="datatable-basic" data-dt-idx="0" tabindex="0" class="page-link">
               <i class="fas fa-angle-left"></i>
            </a>
        </li>
    </c:if>

    <c:forEach var="pageItem" items="${pageList}" varStatus="status">
        <c:choose>
            <c:when test="${pageItem != nowPage}">
                <li class="paginate_button page-item">
                    <a href="javascript:gotoPage('${pageItem}');" aria-controls="datatable-basic" data-dt-idx="0" tabindex="0" class="page-link">${pageItem}</a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="paginate_button page-item active">
                    <a href="javascript:gotoPage('${pageItem}');" aria-controls="datatable-basic" data-dt-idx="0" tabindex="0" class="page-link">${pageItem}</a>
                </li>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <c:if test="${!empty nextPage}">
        <li class="paginate_button page-item next" id="datatable-basic_next">
            <a href="javascript:gotoPage('${nextPage}');" aria-controls="datatable-basic" data-dt-idx="0" tabindex="0" class="page-link">
            <i class="fas fa-angle-right"></i>
            </a>
        </li>
    </c:if>

    <c:if test="${!empty lastPage}">
        <li class="paginate_button page-item next" id="datatable-basic_next">
            <a href="javascript:gotoPage('${lastPage}');" aria-controls="datatable-basic" data-dt-idx="0" tabindex="0" class="page-link">
               <i class="fas fa-angle-double-right"></i>
            </a>
        </li>
    </c:if>
</ul>
</div>
