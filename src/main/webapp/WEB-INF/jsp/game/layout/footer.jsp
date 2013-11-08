<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"  %>
<div class="modalLoader">&nbsp;</div>
<c:catch var="caughtException">
    <x:out escapeXml="false" select="$layout//footer" />
</c:catch>