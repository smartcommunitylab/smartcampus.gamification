<%@tag description="Tag per stampare un input di form di spring" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="path" required="true"%>
<%@attribute name="label" required="true"%>
<%@attribute name="labelArguments" required="false"%>
<%@attribute name="inputRequired" required="false"%>
<%@attribute name="disabled" required="false"%>
<%@attribute name="cssInput" required="false"%>
<%@attribute name="cssError" required="false"%>
<%@attribute name="cssdiv" required="false"%>
<%@attribute name="csslabel" required="false"%>
<%@attribute name="lengthMax" required="false"%>
<%@attribute name="onchange" required="false"%>
<%@attribute name="id" required="false"%>
<%@attribute name="divId" required="false"%>
<%@attribute name="readonly" required="false"%>
<%@attribute name="onclick" required="false"%>
<%@attribute name="titleInput" required="false" %>
<%@attribute name="titleInputArguments" required="false" %>
<%@attribute name="tabindex" required="false"%>
<%@attribute name="spaceAfterInput" required="false"%>
<%@attribute name="wrapped" required="false"%>
<%-- any content can be specified here e.g.: --%>
<c:if test="${not empty titleInput}">
    <c:if test="${empty titleInputArguments}">
        <c:set var="titleMsg"><spring:message code="${titleInput}"/></c:set>
    </c:if>
    <c:if test="${not empty titleInputArguments}">
        <c:set var="titleMsg"><spring:message code="${titleInput}" arguments="${titleInputArguments}" argumentSeparator="," /></c:set>
    </c:if>
</c:if>
<c:if test="${id eq null}">
    <c:set var="id">${path}</c:set>
</c:if>
<c:if test="${cssError eq null}">
    <c:set var="cssError">errorfield</c:set>
</c:if>
<c:set var="errorString"><form:errors path="${path}" cssClass="${cssError}"/></c:set>
<c:choose>
    <c:when test="${empty errorString}">
        <c:set var="cssClassInput" value="${cssInput}"/>
    </c:when>
    <c:otherwise>
        <c:set var="cssClassInput" value="${cssInput} ${cssError}"/>
    </c:otherwise>
</c:choose>
<c:if test="${empty wrapped}">
    <c:set var="wrapped" value="true"/>
</c:if>
<c:if test="${wrapped}">
<div <c:if test="${not empty divId}">id="${divId}"</c:if> class="${cssdiv}">
</c:if>
    <form:label  cssClass="${csslabel}" path="${path}" id="${path}.label">
        <c:if test="${empty labelArguments}">
            <spring:message code="${label}"/>
        </c:if>
        <c:if test="${not empty labelArguments}">
            <spring:message code="${label}" arguments="${labelArguments}" argumentSeparator="," />
        </c:if>
        <c:if test="${inputRequired eq 'true'}">
            <strong>* </strong>
        </c:if></form:label>
    <form:input path="${path}"
                cssClass="${cssClassInput}"
                maxlength="${lengthMax}"
                disabled="${disabled}"
                onchange="${onchange}"
                id="${id}"
                onclick="${onclick}"
                readonly="${readonly}"
                tabindex="${tabindex}"
                title="${titleMsg}" />
    <%--c:if test="${empty spaceAfterInput}"><br /></c:if--%>${errorString}
<c:if test="${wrapped}">
</div>
</c:if>