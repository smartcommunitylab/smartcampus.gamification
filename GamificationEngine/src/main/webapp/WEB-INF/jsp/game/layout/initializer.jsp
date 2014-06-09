<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<tiles:useAttribute id="initializerLayoutUrlParams" name="initializerLayoutUrlParams" classname="java.lang.String" />


<c:catch var="caughtException">
    <x:parse var="layout" scope="request">
        <%--c:import charEncoding="UTF-8" url="${webapp_absolute}/index.php?xsl=1897${initializerLayoutUrlParams}" /--%>
    </x:parse>
</c:catch>

<c:choose>
    <c:when test="${empty caughtException}">
        <%--<c:set var="header" scope="request">
            <x:out escapeXml="false" select="$layout//header" />
            </c:set>
            <c:set var="path" scope="request">
                <x:out escapeXml="false" select="$layout//path" />
            </c:set>
            <c:set var="menu" scope="request">
                <x:out escapeXml="false" select="$layout//menu" />
            </c:set>
            <c:set var="footer" scope="request">
                <x:out escapeXml="false" select="$layout//footer" />
            </c:set>
        --%>
        
        <c:catch var="caughtException">
            <x:set var="flow" scope="request" select="$layout//flow"/>
        </c:catch>
    </c:when>
    <c:otherwise>
        <div class="errore">
            <spring:message code="game.layout.error" />
        </div>
    </c:otherwise>
</c:choose>