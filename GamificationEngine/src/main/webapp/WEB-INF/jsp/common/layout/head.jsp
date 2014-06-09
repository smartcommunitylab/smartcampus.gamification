<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Script-Type" content="text/javascript"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>

    <tiles:importAttribute name="pageTitle"/>        
    <title><fmt:message key="${pageTitle}"/></title>

    <link rel="stylesheet" type="text/css" href="<tiles:getAsString name="firstCSS"/>"/>
    <link rel="stylesheet" type="text/css" title="secondario" href="<tiles:getAsString name="secondCSS"/>"/>
    <link rel="stylesheet" type="text/css" href="<tiles:getAsString name="pageCSS"/>"/>
    <link rel="stylesheet" type="text/css" media="print" href="<tiles:getAsString name="printCSS"/>"/>
    <!-- script -->
    <!--link rel="shortcut icon" href="http://www.gamificationengine.it/favicon.ico" type="image/x-icon"/>
</head>