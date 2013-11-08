<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>

<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<%-- expected elements: --%>
<%-- expected beans in model or in request scope: --%>

<tiles:useAttribute id="switcherElements" name="switcherElements" ignore="false" />

<tiles:useAttribute id="switcherDisabledInputElements" name="switcherDisabledInputElements" ignore="true" />
<tiles:useAttribute id="switcherIndex" name="switcherIndex" ignore="true" />
<tiles:useAttribute id="switcherInputType" name="switcherInputType" ignore="true" />
<tiles:useAttribute id="switcherElementPath" name="switcherElementPath" ignore="true" />
<tiles:useAttribute id="switcherElementLabel" name="switcherElementLabel" ignore="true" />
<tiles:useAttribute id="switcherElementWrapped" name="switcherElementWrapped" ignore="true" />

<tiles:useAttribute id="titleAndSwitcherElementsNotInLine" name="titleAndSwitcherElementsNotInLine" ignore="true" />

<tiles:useAttribute id="switcherElementsImages" name="switcherElementsImages" ignore="true" />
<tiles:useAttribute id="switcherElementsImagesCssClass" name="switcherElementsImagesCssClass" ignore="true" />

<tiles:useAttribute id="enableSwitcherElementLabels" name="enableSwitcherElementLabels" ignore="true" />

<tiles:useAttribute id="enableSubmitButton" name="enableSubmitButton" ignore="true" />
<tiles:useAttribute id="submitButtonId" name="submitButtonId" ignore="true" />
<tiles:useAttribute id="submitButtonName" name="submitButtonName" ignore="true" />
<tiles:useAttribute id="submitButtonValueCode" name="submitButtonValueCode" ignore="true" />
<tiles:useAttribute id="scrollFlag" name="scrollFlag" ignore="true" />

<tiles:useAttribute id="enableMainDiv" name="enableMainDiv" ignore="true" />
<tiles:useAttribute id="mainDivId" name="mainDivId" ignore="true" />

<tiles:useAttribute id="titles" name="titles" classname="java.util.List" ignore="true" />
<tiles:useAttribute id="titlesCssClass" name="titlesCssClass" classname="java.util.List" ignore="true" />
<tiles:useAttribute id="firstPartCodeLabel" name="firstPartCodeLabel" ignore="true" />

<tiles:useAttribute id="switcherFieldsetCssClass" name="switcherFieldsetCssClass" ignore="true" />
<tiles:useAttribute id="switcherMainDivCssClass" name="switcherMainDivCssClass" ignore="true" />
<tiles:useAttribute id="switcherElementDivCssClass" name="switcherElementDivCssClass" ignore="true" />
<tiles:useAttribute id="switcherInputCssClass" name="switcherInputCssClass" ignore="true" />
<tiles:useAttribute id="switcherElementsLabelCssClass" name="switcherElementsLabelCssClass" ignore="true" />


<c:if test="${not empty enableSubmitButton}">
    <c:if test="${not empty submitButtonName}">
        <c:set var="submitButtonName" value="${submitButtonName}${switcherIndex}" />
        
        <c:if test="${not empty submitButtonId}">
            <c:set var="submitButtonId" value="${submitButtonId}_${switcherIndex}" />
        </c:if>
        
        <c:set var="switcherInputCssClass" value="${submitButtonName} ${switcherInputCssClass}" />
    </c:if>
</c:if>

<c:if test="${not empty mainDivId}">
    <c:set var="mainDivId" value="${mainDivId}_${switcherIndex}" />
</c:if>


<c:if test="${not empty switcherInputType}">
<fieldset class="${switcherFieldsetCssClass}">
</c:if>
    <c:if test="${not empty enableMainDiv}">
    <div <c:if test="${not empty mainDivId}">id="${mainDivId}"</c:if> class="${switcherMainDivCssClass}">
    </c:if>
        <c:forEach var="item" items="${titles}" varStatus="count">
            <c:set var="currentTitleCssClass" value="" />
            <c:if test="${(not empty titlesCssClass) and (count.index lt fn:length(titlesCssClass))}">
                <c:set var="currentTitleCssClass" value="${titlesCssClass[count.index]}" />
            </c:if>
            
            <%--c:if test="${item eq 'game.on'}">
               <div style="line-height:38px; height:38px;">
               <img src="http://www.gamificationengine.it/img/8.png" class="left r-space" />
            </c:if>
            <c:if test="${item eq 'game.off'}">
               <div style="line-height:38px; height:38px;">
               <img src="http://www.gamificationengine.it/img/8.png" class="left r-space" />
            </c:if--%>
            
            <div class="${currentTitleCssClass}">
                <spring:message code="${item}" />
            </div>
            
           <%--c:if test="${item eq 'game.on' || item eq 'game.off'}">
               </div>
           </c:if--%>

            
            <c:if test="${(count.index lt (fn:length(titles) - 1)) or (not empty titleAndSwitcherElementsNotInLine)}">
                <br/>
            </c:if>
        </c:forEach>
        
        <c:forEach items="${switcherElements}" var="current" varStatus="count">
            <c:if test="${not empty switcherElementLabel}">
                <c:set var="current" value="${current[switcherElementLabel]}" />
            </c:if>
            
            <c:if test="${not empty switcherElementWrapped}">
            <div class="${switcherElementDivCssClass}">
            </c:if>
                <c:set var="currentSwitcherElementLabel" value="" />
                <c:if test="${not empty firstPartCodeLabel}">
                    <spring:message var="currentSwitcherElementLabel" code="${firstPartCodeLabel}${current}.label" />
                </c:if>
                
                <c:if test="${not empty switcherInputType}">
                    
                    <c:set var="currentInputDisabled" value="${false}" />
                    <c:if test="${not empty switcherDisabledInputElements}">
                        <c:forEach items="${switcherDisabledInputElements}" var="currentDisabled">
                            <c:if test="${current eq currentDisabled}">
                                <c:set var="currentInputDisabled" value="${true}" />
                            </c:if>
                        </c:forEach>
                    </c:if>
                    <c:choose>
                        <c:when test="${switcherInputType eq 'radioButton'}">
                            <form:radiobutton path="${switcherElementPath}" value="${current}" disabled="${currentInputDisabled}" cssClass="${switcherInputCssClass}" />
                        </c:when>
                         <c:when test="${switcherInputType eq 'checkBox'}">
                            <form:checkbox path="${switcherElementPath}" value="${current}" disabled="${currentInputDisabled}" cssClass="${switcherInputCssClass}" />
                            <c:if test="${currentInputDisabled eq true}">
                                <form:checkbox path="${switcherElementPath}" value="${current}" cssClass="hidden" />
                            </c:if>
                        </c:when>
                    </c:choose>
                </c:if>
                
                <c:if test="${not empty switcherElementsImages}">
                    <%                        
                        Map map = (Map) pageContext.getAttribute("switcherElementsImages");
                    
                        pageContext.setAttribute("currentImageUrl", 
                                                 map.get(pageContext.getAttribute("current")));
                    %>
                    
                    <c:set var="currentAltTitle" value="${current}" />
                    <c:if test="${not empty currentSwitcherElementLabel}">
                        <c:set var="currentAltTitle" value="${currentSwitcherElementLabel}" />
                    </c:if>
                    <img src="${currentImageUrl}"
                         alt="${currentAltTitle}"
                         title="${currentAltTitle}"
                         class="${switcherElementsImagesCssClass}" />
                </c:if>
                
                <c:if test="${not empty enableSwitcherElementLabels}">
                    <c:choose>
                        <c:when test="${not empty switcherInputType}">
                            <form:label path="${switcherElementPath}" cssClass="${switcherElementsLabelCssClass}">${currentSwitcherElementLabel}</form:label>
                        </c:when>
                        <c:otherwise>
                            <label class="${switcherElementsLabelCssClass}" >${currentSwitcherElementLabel}</label>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            <c:if test="${not empty switcherElementWrapped}">
            </div>
            </c:if>
        </c:forEach>

                
   <c:if test="${not empty enableSubmitButton}">
      <input type="submit" id="${submitButtonId}" name="${submitButtonName}" value="<spring:message code="${submitButtonValueCode}" />"  class="toHide right"/>
   </c:if>
        <%-- c:if test="${not empty enableSubmitButton}">
            <c:set var="submitButtonContainerId" value="${submitButtonName}_Container" />
            <div id="${submitButtonContainerId}">
                <input type="submit" id="${submitButtonId}" name="${submitButtonName}" value="<spring:message code="${submitButtonValueCode}" />"  style="float:right"/>
            </div>
        </c:if--%>
    <c:if test="${not empty enableMainDiv}">
    </div>
    </c:if>
    
    <c:if test="${not empty enableSubmitButton}">
        <script>
            $(document).ready(function() {
                var switcherElementSelector = 'input[name="${switcherElementPath}"][class^="${submitButtonName}"]';
                var submitButtonContainerSelector = '#' + '${submitButtonContainerId}';
                
                /*$(submitButtonContainerSelector).addClass('hidden');*/
                $('.toHide').addClass('hidden');
                
                $(switcherElementSelector).change(function() {
                    $('input[name="${submitButtonName}"]').click();
                });
                
                <c:if test="${not empty scrollFlag and not empty submitButtonName and scrollFlag eq submitButtonName}">
                    scrollTo(switcherElementSelector, 0);
                </c:if>
            });
        </script>
    </c:if>
<c:if test="${not empty switcherInputType}">
</fieldset>
</c:if>