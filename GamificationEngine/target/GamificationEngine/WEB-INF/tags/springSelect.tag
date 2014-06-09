<%@tag description="tag per generare la select a partire da una lista di elementi" pageEncoding="UTF-8"%>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="path" required="true"%>
<%@attribute name="label" required="false"%>
<%@attribute name="items" required="true" type="java.util.List"%>
<%@attribute name="itemLabel" required="false" %>
<%@attribute name="itemAddedLabels" required="false" %>
<%@attribute name="itemAddedLabelsSeparators" required="false" %>
<%@attribute name="itemLabelMaxLength" required="false" %>
<%@attribute name="itemValue" required="false" %>
<%@attribute name="itemSelected" required="false" type="java.lang.String"%>
<%@attribute name="disabled" required="false"%>
<%@attribute name="cssInput" required="false"%>
<%@attribute name="cssError" required="false"%>
<%@attribute name="cssdiv" required="false"%>
<%@attribute name="csslabel" required="false"%>
<%@attribute name="lengthMax" required="false"%>
<%@attribute name="onchange" required="false"%>
<%@attribute name="id" required="false"%>
<%@attribute name="readonly" required="false"%>
<%@attribute name="onclick" required="false"%>
<%@attribute name="titleInput" required="false" %>
<%@attribute name="tabindex" required="false"%>
<%@attribute name="customSelectLabel" required="false"%>
<%@attribute name="showSelectLabel" required="false"%>
<%@attribute name="inputRequired" required="false"%>
<%@attribute name="wrapped" required="false"%>
<%@attribute name="withLabel" required="false"%>
<c:if test="${not empty titleInput}">
    <c:set var="titleMsg"><spring:message code="${titleInput}"/></c:set>
</c:if>
<c:if test="${empty itemLabel && empty itemValue}">
    <c:set var="noItemLabel" value="true"/>
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
<c:if test="${empty withLabel}">
    <c:set var="withLabel" value="true"/>
</c:if>
<%-- any content can be specified here e.g.: --%>
<c:if test="${wrapped}">
<div class="${cssdiv}">
</c:if>
<c:if test="${withLabel}">
    <form:label  cssClass="${csslabel}" path="${path}" id="${path}.label">
        <spring:message code="${label}"/>
        <c:if test="${inputRequired eq 'true'}">
            <strong>* </strong>
        </c:if></form:label>
</c:if>
<c:if test="${wrapped}">
        <span class="${cssdiv}">
</c:if>
        <form:select title="${titleMsg}"
                     cssClass="${cssClassInput}"
                     path="${path}"
                     onchange="${onchange}"
                     disabled="${disabled}"
                     tabindex="${tabindex}">
            <c:if test="${not (showSelectLabel eq 'false')}">
                <c:if test="${empty customSelectLabel}">
                    <c:set var="customSelectLabel" value="form.seleziona" />
                </c:if>
                <form:option value=""><spring:message code="${customSelectLabel}"/></form:option>
            </c:if>
            <c:choose>
                <c:when test="${noItemLabel}">
                    <c:forEach items="${items}" var="o">
                        <c:choose>
                            <c:when test="${not empty itemSelected and itemSelected eq o}">                                                    
                               <option value="${o}" selected="selected">${o}</option>
                            </c:when>
                            <c:otherwise>                               
                                <form:option value="${o}" label="${o}"/>
                            </c:otherwise>
                        </c:choose>                        
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${items}" var="o">
                        <c:set var="itemFullLabel" value="${o[itemLabel]}"/>
                        <c:if test="${not empty itemAddedLabels}">
                            <c:forTokens items="${itemAddedLabels}" delims="," var="added" varStatus="row">
                                <c:set var="itemFullLabel">${itemFullLabel}<c:forTokens items="${itemAddedLabelsSeparators}" delims="," var="ita" varStatus="row1">
                                        <c:if test="${row.index == row1.index}">
                                        ${ita}
                                        </c:if>
                                    </c:forTokens>${o[added]}</c:set>
                            </c:forTokens>
                        </c:if>
                        <c:if test="${not empty itemLabelMaxLength}">
                            <c:set var="itemFullLabel" value="${fn:substring(itemFullLabel,0,itemLabelMaxLength)}"/>
                        </c:if>                    
                        <c:choose>
                            <c:when test="${not empty itemSelected and itemSelected eq o[itemValue]}">                                                    
                               <option value="${o[itemValue]}" selected="selected">${itemFullLabel}</option>
                            </c:when>
                            <c:otherwise>                               
                                <form:option value="${o[itemValue]}" label="${itemFullLabel}"/>
                            </c:otherwise>
                        </c:choose>                        
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </form:select>
<c:if test="${wrapped}">
    </span>
</c:if>${errorString}
<c:if test="${wrapped}">
</div>
</c:if>