<%--
    Document   : header
    Created on : 9-mar-2009, 10.18.57
    Author     : Luca Piras
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:message key="header.title"/>

<a href="<c:url value="/home.htm"/>">
    <fmt:message key="header.home"/>
</a>

<%-- c:out value="${model.credenziali.identity.nome}"/>
<c:out value="${model.credenziali.identity.cognome}"/>
|
<a href="<c:url value="/logout.htm"/>">
<fmt:message key="header.userProfile"/>
</a>
|
<a href="<c:url value="/logout.htm"/>">
<fmt:message key="header.logout"/>
</a--%>
