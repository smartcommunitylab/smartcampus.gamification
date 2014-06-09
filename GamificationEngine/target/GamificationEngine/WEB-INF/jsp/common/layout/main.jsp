<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <tiles:insertAttribute name="head" />
    <body>
        <div class="container">
            <tiles:insertAttribute name="header" />
            <tiles:insertAttribute name="menu" />
            <tiles:insertAttribute name="content" />
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>