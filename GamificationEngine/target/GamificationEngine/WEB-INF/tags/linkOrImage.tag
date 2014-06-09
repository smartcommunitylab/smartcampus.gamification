<%@tag description="Tag per stampare un link o un'immagine, oppure un link con un'immagine" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="cssdiv" required="false"%>
<%@attribute name="linkHref" required="false"%>
<%@attribute name="linkId" required="false"%>
<%@attribute name="linkTitle" required="false"%>
<%@attribute name="linkCssClass" required="false"%>
<%@attribute name="linkContent" required="false"%>
<%@attribute name="linkOnClick" required="false"%>
<%@attribute name="imageSrc" required="false"%>
<%@attribute name="imageId" required="false"%>
<%@attribute name="imageTitle" required="false"%>
<%@attribute name="imageAlt" required="false"%>
<%@attribute name="imageCssClass" required="false"%>
<%@attribute name="imageStyle" required="false"%>
<%@attribute name="imageOnClick" required="false"%>
<%@attribute name="wrapped" required="false"%>
<%-- any content can be specified here e.g.: --%>
<c:if test="${not empty linkTitle}">
    <spring:message var="linkTitle" code='${linkTitle}' />
</c:if>
<c:if test="${not empty imageAlt}">
    <spring:message var="imageAlt" code='${imageAlt}' />
</c:if>
<c:if test="${not empty imageTitle}">
    <spring:message var="imageTitle" code='${imageTitle}' />
</c:if>
<c:if test="${empty wrapped}">
    <c:set var="wrapped" value="true"/>
</c:if>
<c:if test="${wrapped}">
<div class="${cssdiv}">
</c:if>
    <c:if test="${not empty linkHref}">
        <a class="${linkCssClass}" title="${linkTitle}" href="${linkHref}" onclick="${linkOnClick}" 
           <c:if test="${not empty linkId}">id="${linkId}"</c:if>>
            <c:out value="${linkContent}"/>
    </c:if>
    <c:if test="${not empty imageSrc}">
        <img class="${imageCssClass}" alt="${imageAlt}" src="${imageSrc}" title="${imageTitle}" onclick="${imageOnClick}"
             <c:if test="${not empty imageId}">id="${imageId}"</c:if>
             <c:if test="${not empty imageStyle}">style="${imageStyle}"</c:if> />
        
    </c:if>
    <c:if test="${not empty linkHref}"></a></c:if>
<c:if test="${wrapped}">
</div>
</c:if>