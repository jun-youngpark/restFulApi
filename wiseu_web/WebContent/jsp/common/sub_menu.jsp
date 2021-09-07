<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/include/taglibs.jsp" %>
<div class="sidebar  navbar-vertical" data-color="purple" data-background-color="white"><!-- Sidenav -->
    <div class="align-items-center"><!-- brand -->
        <a class="navbar-brand align-items-center" href="${logoLinkUrl}">
            <img src="/images/logo/wiseU_new_wh.png" class="navbar-brand-img" alt="logo">
        </a>
    </div>

    <div class="sidebar-wrapper"><!-- Sidenav -->
        <ul class="nav navbar-nav">
            <c:forEach var="topMenuVo" items="${userTopMenuList}">
                <c:choose>
                    <c:when test="${topMenuVo.rollOver eq true}">
                        <li class="nav-item"><!-- open(선택) 메뉴 -->
                            <a class="nav-link active" href="#navbar-${topMenuVo.moduleNm}" data-toggle="collapse" role="button"
                                aria-expanded="true" aria-controls="navbar-${topMenuVo.moduleNm}">
                                <c:choose>
                                    <c:when test="${topMenuVo.moduleNm eq 'campaign'}"><i class="ni ni-air-baloon text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'ecare'}"><i class="ni ni-notification-70 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'report'}"><i class="ni ni-chart-bar-32 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'template'}"><i class="ni ni-album-2 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'segment'}"><i class="ni ni-bullet-list-67 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'account'}"><i class="ni ni-circle-08 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'env'}"><i class="ni ni-settings-gear-65 text-Gray 600"></i></c:when>
                                </c:choose>
                                <span class="nav-link-text">${topMenuVo.menuNm}</span>
                            </a>

                            <c:forEach var="subMenuVo" items="${userSubMenuList}">
                                <c:if test="${topMenuVo.menuCd eq subMenuVo.pmenuCd}">
                                    <div class="collapse show" id="navbar-${topMenuVo.moduleNm}">
                                        <ul class="nav nav-sm flex-column">
                                            <li class="nav-item">
                                                <a href="${subMenuVo.menuLinkUrl}" class="nav-link">
                                                    <span class="sidenav-normal">${subMenuVo.menuNm}</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="#navbar-${topMenuVo.moduleNm}" data-toggle="collapse" role="button"
                                aria-expanded="false" aria-controls="navbar-${topMenuVo.moduleNm}">
                                <c:choose>
                                    <c:when test="${topMenuVo.moduleNm eq 'campaign'}"><i class="ni ni-air-baloon  text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'ecare'}"><i class="ni ni-notification-70 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'report'}"><i class="ni ni-chart-bar-32 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'template'}"><i class="ni ni-album-2 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'segment'}"><i class="ni ni-bullet-list-67 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'account'}"><i class="ni ni-circle-08 text-Gray 600"></i></c:when>
                                    <c:when test="${topMenuVo.moduleNm eq 'env'}"><i class="ni ni-settings-gear-65 text-Gray 600"></i></c:when>
                                </c:choose>
                                <span class="nav-link-text">${topMenuVo.menuNm}</span>
                            </a>

                            <c:forEach var="subMenuVo" items="${userSubMenuList}">
                                <c:if test="${topMenuVo.menuCd eq subMenuVo.pmenuCd}">
                                    <div class="collapse" id="navbar-${topMenuVo.moduleNm}">
                                        <ul class="nav nav-sm flex-column">
                                            <li class="nav-item">
                                                <a href="${subMenuVo.menuLinkUrl}" class="nav-link">
                                                    <span class="sidenav-normal">${subMenuVo.menuNm}</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </ul>
    </div>
</div>