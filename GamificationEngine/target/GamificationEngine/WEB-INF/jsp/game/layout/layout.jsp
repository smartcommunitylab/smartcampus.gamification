<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"  %>

<c:catch var="caughtException">
    <x:out escapeXml="false" select="$layout//header" />
    <x:out escapeXml="false" select="$layout//path" />
    <x:out escapeXml="false" select="$layout//menu" />
</c:catch>