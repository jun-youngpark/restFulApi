<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="wiseu" uri="/WEB-INF/tlds/wiseuFunctions.tld" %>
<c:set var="lang" value=""/>
<c:if test="${sessionScope.adminSessionVo.language ne 'ko'}">
	<c:set var="lang" value="_${sessionScope.adminSessionVo.language}"/>
</c:if>

