<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Script-Type" content="text/javascript"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>

    <tiles:useAttribute id="metaList" name="metaList" classname="java.util.List" ignore="true"/>
    <c:forEach var="item" items="${metaList}">${item}</c:forEach>
    
    <c:set var="mainCssPathAbsolute" value="${webapp_cssPath_absolute}" />
    <%--c:if test="${empty layout}">
        <c:set var="mainCssPathAbsolute" value="${webapp_cssPath_absolute}game/" />
    </c:if--%>
    
    <tiles:importAttribute name="pageTitle"/>
    <title><fmt:message key="${pageTitle}"/></title>
    
    <tiles:useAttribute id="firstCSS" name="firstCSS" classname="java.lang.String" ignore="true"/>
    <c:if test="${not empty firstCSS}">
        <link rel="stylesheet" type="text/css" href="${mainCssPathAbsolute}${firstCSS}"/>
    </c:if>
    
    <tiles:useAttribute id="secondCSS" name="secondCSS" classname="java.lang.String" ignore="true"/>
    <c:if test="${not empty secondCSS}">
        <link rel="stylesheet" type="text/css" title="secondario" href="${mainCssPathAbsolute}${secondCSS}"/>
    </c:if>
    
    <tiles:useAttribute id="pageCSS" name="pageCSS" classname="java.lang.String" ignore="true"/>
    <c:if test="${not empty pageCSS}">
        <link rel="stylesheet" type="text/css" href="${mainCssPathAbsolute}${pageCSS}"/>
    </c:if>
    
    <tiles:useAttribute id="printCSS" name="printCSS" classname="java.lang.String" ignore="true"/>
    <c:if test="${not empty printCSS}">
        <link rel="stylesheet" type="text/css" media="print" href="${mainCssPathAbsolute}${printCSS}"/>
    </c:if>
        
    <tiles:useAttribute id="mainCssList" name="mainCssList" classname="java.util.List" ignore="true"/>
    <c:forEach var="item" items="${mainCssList}">
        <link rel="stylesheet" type="text/css" href="${mainCssPathAbsolute}${item}" />
    </c:forEach>
    
    <tiles:useAttribute id="cssList" name="cssList" classname="java.util.List" ignore="true"/>
    <c:forEach var="item" items="${cssList}">
        <link rel="stylesheet" type="text/css" href="${webapp_cssPath_absolute}${item}" />
    </c:forEach>
    
    <%--link rel="shortcut icon" href="${webapp_absolute}/<tiles:getAsString name="favicon"/>" type="image/x-icon"/--%>
    
    <!-- script -->
    <tiles:useAttribute id="externalJavascripts" name="externalJavascripts" classname="java.util.List" ignore="true" />
    <c:forEach var="item" items="${externalJavascripts}">
        <script type="text/javascript" src="${item}">
                                /* */
        </script>
    </c:forEach>
        
    <%--@ include file="/WEB-INF/jsp/common/variables/jsVariables.jsp" --%>
        
    <tiles:useAttribute id="variablesJavascripts" name="variablesJavascripts" classname="java.util.List" ignore="true" />
    <c:forEach var="item" items="${variablesJavascripts}">
        <jsp:include page="/WEB-INF/jsp/common/variables/${item}" flush="true" />
    </c:forEach>
    
    <tiles:useAttribute id="baseJavascripts" name="baseJavascripts" classname="java.util.List" ignore="true"/>
    <c:forEach var="item" items="${baseJavascripts}">
        <script type="text/javascript" src="${webapp_jsPath_absolute}${item}">
                                /* */
        </script>
    </c:forEach>
        
    <tiles:useAttribute id="javascripts" name="javascripts" classname="java.util.List" ignore="true" />
    <c:forEach var="item" items="${javascripts}">
        <script type="text/javascript" src="${webapp_jsPath_absolute}${item}">
                                /* */
        </script>
    </c:forEach>
        
    <tiles:useAttribute id="mapJavascripts" name="mapJavascripts" classname="java.util.List" ignore="true" />
    <c:forEach var="item" items="${mapJavascripts}">
        <script type="text/javascript" src="${webapp_jsPath_absolute}${item}">
                                /* */
        </script>
    </c:forEach>
</head>