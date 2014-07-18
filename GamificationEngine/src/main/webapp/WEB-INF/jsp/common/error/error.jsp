<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<%-- expected elements: --%>
<%-- expected beans in model or in request scope: enableLayout (optional) --%>

<tiles:useAttribute id="errorMessageTitleKey" name="errorMessageTitleKey" classname="java.lang.String" />
<h1 id="titleError"><spring:message code="${errorMessageTitleKey}" /></h1>
<%--h2>${exception}</h2--%>

<c:set var="errorsLink" value="${game_main_page_absolute}" />

<spring:message var="errorsLinkMessage" code="errorsLink.msg" />
<ct:linkOrImage cssdiv="block t-space"
                linkHref="${errorsLink}"
                linkContent="${errorsLinkMessage}" />

<c:if test="${empty enableLayout}">
    <script>
        $(document).ready(function() {
            $('a').click(function(event) {
                event.preventDefault();

                window.parent.location.href = '${errorsLink}';
            });
        });
    </script>
</c:if>