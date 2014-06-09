<%@tag description="Tag per stampare una semplice label" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="labelContent" required="false"%>
<%@attribute name="labelTitle" required="false"%>
<%@attribute name="labelTitleId" required="false"%>
<%@attribute name="cssdiv" required="false"%>
<%@attribute name="cssLabelContent" required="false"%>
<%@attribute name="cssLabelTitle" required="false"%>
<%@attribute name="wrapped" required="false"%>
<%-- any content can be specified here e.g.: --%>
<c:if test="${empty wrapped}">
    <c:set var="wrapped" value="true"/>
</c:if>
<c:if test="${wrapped}">
<div class="${cssdiv}">
</c:if>
    <c:if test="${not empty labelTitle}">
        <span <c:if test="${not empty labelTitleId}">id="${labelTitleId}"</c:if>
            class="${cssLabelTitle}"><spring:message code="${labelTitle}"/></span>
    </c:if>
    <c:if test="${not empty labelContent}">
        <span class="${cssLabelContent}">${labelContent}</span>
    </c:if>
<c:if test="${wrapped}">
</div>
</c:if>